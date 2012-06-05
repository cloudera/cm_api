#!/usr/bin/python
''' Copyright (c) 2012 Cloudera, Inc. All rights reserved.
    Sample script for Nagios integration with Cloudera Manager via the CM API.
'''

import sys
import optparse
import tempfile
from os import getcwd, devnull
from os.path import join, isfile
from subprocess import call
from time import sleep, time
from urllib2 import quote

from cm_api.api_client import get_root_resource, ApiException
import cm_api.endpoints.clusters
import cm_api.endpoints.hosts
import cm_api.endpoints.services
import cm_api.endpoints.roles

CM_API_VERSION = 1

CM_HOST_CFG_FILENAME = "cm_hosts.cfg"
CM_SERVICE_CFG_FILENAME = "cm_services.cfg"

''' Nagios exit codes, see the Nagios plug-in developer guidelines:
    http://nagiosplug.sourceforge.net/developer-guidelines.html#AEN76
'''
EXIT_OK = 0
EXIT_WARNING = 1
EXIT_CRITICAL = 2
EXIT_UNKNOWN = 3

NAGIOS_CODE_MESSAGES = {EXIT_OK: "OK",
                        EXIT_WARNING: "Warning",
                        EXIT_CRITICAL: "Critical",
                        EXIT_UNKNOWN: "Unknown"}

CM_STATE_CODES = {"HISTORY_NOT_AVAILABLE": EXIT_UNKNOWN,
                  "NOT_AVAILABLE": EXIT_UNKNOWN,
                  "DISABLED": EXIT_UNKNOWN,
                  "GOOD": EXIT_OK,
                  "CONCERNING": EXIT_WARNING,
                  "BAD": EXIT_CRITICAL}


''' Define templates for the Nagios config objects
'''
PASSIVE_HOST_TEMPLATE = '''
define host {
        use                         generic-host
        name                        cm-host
        active_checks_enabled       0
        passive_checks_enabled      1
        register                    0
        max_check_attempts          1
        }
'''

CM_HOST_TEMPLATE = '''
define host{
        use                         cm-host
        host_name                   %s
        alias                       %s
        address                     %s
        }
'''

PASSIVE_SERVICE_TEMPLATE = '''
define command {
        command_name    cm-return-ok
        command_line    /bin/true
        }
define service {
        use                         generic-service
        name                        cm-passive-service
        check_command               cm-return-ok
        active_checks_enabled       0
        passive_checks_enabled      1
        register                    0
        max_check_attempts          1
        }
'''

CM_SERVICE_TEMPLATE = '''
define service {
        use                             cm-passive-service
        host_name                       %s
        service_description             %s
        display_name                    %s
        action_url                      %s
        }
'''


''' Define the format for naming CM hosts in Nagios to prevent duplicating
    existing names.  Note that these are just host identifiers in Nagios, but
    they must be unique.
'''
NAGIOS_HOSTNAME_FORMAT = "cm-%s"

CM_DUMMY_HOST = "cloudera-manager"

def parse_args():
  ''' Parse the script arguments
  '''
  parser = optparse.OptionParser()

  parser.add_option("-v", "--verbose", action="store_true")

  mode_group = optparse.OptionGroup(parser, "Program Mode")
  mode_group.add_option("-u", "--update-status", action="store_const",
                        dest="mode", const="update_status")
  mode_group.add_option("-g", "--generate-cfg", action="store_const",
                        dest="mode", const="generate_cfg")
  parser.add_option_group(mode_group)
  parser.set_defaults(mode="update_status")

  general_options = optparse.OptionGroup(parser, "CM API Configuration")
  general_options.add_option("-H", "--host", metavar="HOST",
                             help="CM API hostname")
  general_options.add_option("-p", "--port", help="CM API port", default=None)
  general_options.add_option("-P", "--passfile", metavar="FILE",
                             help="File containing CM API username and password, "
                                  "colon-delimited on a single line.  E.g. "
                                  "\"user:pass\"")
  general_options.add_option("--use-tls", action="store_true",
                             help="Use TLS", default=False)
  parser.add_option_group(general_options)

  polling_options = optparse.OptionGroup(parser, "Status Update Options")
  polling_options.add_option("-c", "--cmd-file", metavar="FILE",
                             help="Path to the file that Nagios checks for "
                                  "external command requests.")
  polling_options.add_option("-n", "--use-send-nsca", action="store_true",
                             default=False,
                             help="Use send_nsca to report status via a nsca "
                                  "daemon. When using this option, the "
                                  "send_nsca program must be available and the "
                                  "nsca daemon host and port must be provided."
                                  "Default is false.")
  polling_options.add_option("--send-nsca-path", metavar="PATH",
                             default="/usr/sbin/send_nsca",
                             help="Path to send_nsca, default is "
                                  "/usr/sbin/send_nsca")
  polling_options.add_option("--nsca-host", metavar="HOST",
                             default="localhost",
                             help="When using send_nsca, the hostname of NSCA "
                                  "server, default is localhost.")
  polling_options.add_option("--nsca-port", metavar="PORT", default=None,
                             help="When using send_nsca, the port on which the "
                                  "server is running, default is 5667.")
  polling_options.add_option("--send-nsca-config", metavar="FILE", default=None,
                             help="Config file passed to send_nsca -c. Default"
                                  " is to not specify the config parameter.")
  parser.add_option_group(polling_options)


  generate_options = optparse.OptionGroup(parser, "Generate Config Options")
  generate_options.add_option("--cfg-dir", metavar="DIR", default=getcwd(),
                              help="Directory for generated Nagios cfg files.")
  parser.add_option_group(generate_options)

  (options, args) = parser.parse_args()

  ''' Parse the 'passfile' - it must contain the username and password,
      colon-delimited on a single line. E.g.:
      $ cat ~/protected/cm_pass
      admin:admin
  '''
  required = ["host", "passfile"]

  if options.mode == "update_status":
    if not options.use_send_nsca:
      required.append("cmd_file")

  for required_opt in required:
    if getattr(options, required_opt) is None:
      parser.error("Please specify the required argument: --%s" %
                   (required_opt.replace('_','-'),))

  return (options, args)


def get_host_map(root):
  ''' Gets a mapping between CM hostId and Nagios host information

      The key is the CM hostId
      The value is an object containing the Nagios hostname and host address
  '''
  hosts_map = {}
  for host in root.get_all_hosts():
    hosts_map[host.hostId] = {"hostname": NAGIOS_HOSTNAME_FORMAT % (host.hostname,),
                              "address": host.ipAddress}

  ''' Also define "virtual hosts" for the CM clusters- they will be the hosts
      to which CM services are mapped
  '''
  for cluster in root.get_all_clusters():
    hosts_map[cluster.name] = {"hostname": cluster.name,
                               "address": quote(cluster.name)}
  hosts_map[CM_DUMMY_HOST] = {"hostname": CM_DUMMY_HOST,
                              "address": CM_DUMMY_HOST}
  return hosts_map


def get_status(api_subject):
  ''' Gets a string representing the status of the Api subject (role or service)
      based on the health summary and health checks.
  '''
  summary = api_subject.healthSummary
  if summary is None:
    return None
  # status string always starts with "<nagios code>: <summary>"
  status = "%s: %s" % (NAGIOS_CODE_MESSAGES[CM_STATE_CODES[summary]], summary)
  if summary != "GOOD" and summary != "DISABLED":
    # if the summary is CONCERNING or BAD, then append the health checks
    for health_check in api_subject.healthChecks:
      if health_check['summary'] != "GOOD" and health_check['summary'] != "DISABLED":
        status = ("%s, %s=%s" % (status, health_check['name'], health_check['summary']))
  return status

def get_services(root, hosts_map, view=None):
  ''' Gets a list of objects representing the Nagios services.

      Each object contains the Nagios hostname, service name, service display
      name, and service health summary.
  '''
  services_list = []
  mgmt_service = root.get_cloudera_manager().get_service()
  services_list.append({"hostname": CM_DUMMY_HOST,
                        "name": mgmt_service.name,
                        "display_name": "CM Managed Service: %s" % (mgmt_service.name,),
                        "status": get_status(mgmt_service),
                        "url": mgmt_service.serviceUrl,
                        "health_summary": mgmt_service.healthSummary})
  for cm_role in root.get_cloudera_manager().get_service().get_all_roles(view):
    services_list.append({"hostname": hosts_map[cm_role.hostRef.hostId]["hostname"],
                          "name": cm_role.name,
                          "display_name": "CM Management Service: %s" % (cm_role.name,),
                          "status": get_status(cm_role),
                          "url": cm_role.roleUrl,
                          "health_summary": cm_role.healthSummary})
  for cm_host in root.get_all_hosts(view):
    services_list.append({"hostname": hosts_map[cm_host.hostId]["hostname"],
                          "name": "cm-host-%s" % (cm_host.hostname,),
                          "display_name": "CM Managed Host: %s" % (cm_host.hostname,),
                          "status": get_status(cm_host),
                          "url": cm_host.hostUrl,
                          "health_summary": cm_host.healthSummary})
  for cluster in root.get_all_clusters(view):
    for service in cluster.get_all_services(view):
      services_list.append({"hostname": cluster.name,
                            "name": service.name,
                            "display_name": "CM Managed Service: %s" % (service.name,),
                            "status": get_status(service),
                            "url": service.serviceUrl,
                            "health_summary": service.healthSummary})
      for role in service.get_all_roles(view):
        services_list.append({"hostname": hosts_map[role.hostRef.hostId]["hostname"],
                              "name": role.name,
                              "display_name": "%s:%s" % (cluster.name, role.name,),
                              "status": get_status(role),
                              "url": role.roleUrl,
                              "health_summary": role.healthSummary})
  return services_list


def submit_status_external_cmd(cmd_file, status_file):
  ''' Submits the status lines in the status_file to Nagios' external cmd file.
  '''
  try:
    with open(cmd_file, 'a') as cmd_file:
      cmd_file.write(status_file.read())
  except IOError:
    exit("Fatal error: Unable to write to Nagios external command file '%s'.\n"
         "Make sure that the file exists and is writable." % (cmd_file,))

def submit_status_send_nsca(nsca_path, host, port, config, status_file, verbose):
  # Prepare params for /usr/sbin/send_nsca -H <host> [-p port] [-c config]
  nsca_cmd = [nsca_path, "-H", host]

  if not port is None:
    nsca_cmd.append("-p")
    nsca_cmd.append(port)

  if not config is None:
    nsca_cmd.append("-c")
    nsca_cmd.append(config)

  # If None, output isn't redirected - so send to /dev/null unless -v is set
  fout = None
  ferr = None
  if not verbose:
    fout = open(devnull, 'w')
    ferr = open(devnull, 'w')

  # send_nsca accepts status updates via stdin
  nsca_return = call(nsca_cmd, stdin=status_file, stdout=fout, stderr=ferr)

  if not verbose:
    fout.close()
    ferr.close()

  if nsca_return != 0:
    exit("Fatal error: send_nsca exited with %s\n" % nsca_return)


def main():
  (options, args) = parse_args()

  try:
    (username, password) = open(options.passfile, 'r').readline().rstrip('\n').split(':')
  except:
    print >> sys.stderr, "Unable to read username and password from file '%s'. "
    "Make sure the file is readable and contains a single line of "
    "the form \"<username>:<password>\"" % options.passfile

  root = get_root_resource(options.host, options.port, username,
                           password, options.use_tls, CM_API_VERSION)

  hosts_map = get_host_map(root)

  if options.mode == "update_status":

    ''' The way in which the status is reported depends on whether or not the
        -n/--use-send-nsca option was set.  By default, the status updates are
        simply appended to the external command file.  If nsca is used, the
        status updates are piped via stdin to send_nsca.
    '''
    # <host>\t<service>\t<return_code>\t<message>\n
    NSCA_STATUS_FORMAT = "{0}\t{1}\t{2}\t{3}\n"
    # [<timestamp>] PROCESS_SERVICE_CHECK_RESULT;<host>;<service>;<return_code>;<message>\n
    CMD_STATUS_FORMAT = "[{0}] PROCESS_SERVICE_CHECK_RESULT;{1};{2};{3};{4}\n"

    try:
      services_list = get_services(root, hosts_map, "full")
      timestamp = long(time()) # approximate time the services were queried

      tmp_status_file = tempfile.TemporaryFile()
      try:
        for service in services_list:
          health = service["health_summary"]
          if not health is None:
            code = CM_STATE_CODES[health]
            if options.use_send_nsca:
              status_line = NSCA_STATUS_FORMAT.format(
                service["hostname"], service["name"], code, service["status"])
            else:
              status_line = CMD_STATUS_FORMAT.format(
                timestamp, service["hostname"], service["name"], code, service["status"])

            tmp_status_file.write(status_line)

        tmp_status_file.flush()
        tmp_status_file.seek(0)

        if options.use_send_nsca:
          submit_status_send_nsca(options.send_nsca_path, options.nsca_host,
                                  options.nsca_port, options.send_nsca_config,
                                  tmp_status_file, options.verbose)
        else:
          submit_status_external_cmd(options.cmd_file, tmp_status_file)
      finally:
        tmp_status_file.close()
    except SystemExit as e:
      sys.exit(e)
    except Exception as e:
      print >> sys.stderr, "An unknown error occurred: %s" % str(e)
      raise

  elif options.mode == "generate_cfg":

    host_cfg_file = open(join(options.cfg_dir, CM_HOST_CFG_FILENAME), 'w+')
    host_cfg_file.write(PASSIVE_HOST_TEMPLATE)

    for host in hosts_map.values():
      host_cfg_file.write(CM_HOST_TEMPLATE %
                          (host["hostname"], host["hostname"], host["address"],))

    host_cfg_file.close()

    services_cfg_file = open(join(options.cfg_dir, CM_SERVICE_CFG_FILENAME), 'w+')
    services_cfg_file.write(PASSIVE_SERVICE_TEMPLATE)

    services_list = get_services(root, hosts_map)
    for service in services_list:
      services_cfg_file.write(CM_SERVICE_TEMPLATE %
                              (service["hostname"],
                              service["name"],
                              service["display_name"],
                              service["url"]))

    services_cfg_file.close()

  else:
    print >> sys.stderr, "Fatal error: Unrecognized mode"


if __name__ == "__main__":
  main()
