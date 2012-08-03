#!/usr/bin/env python
# Licensed to Cloudera, Inc. under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  Cloudera, Inc. licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


"""
Given a list of hostnames, update the config of all the DataNodes,
TaskTrackers and RegionServers on those hosts. The configuration for
the various role types is hardcoded in the code.

Usage: %s [options]

Options:
  -f <hostname_file>        Path to a file containing the set of hostnames
                            to update configuration for. The hostnames
                            should be listed in the file one per line.
                            If not specified, the program will use the
                            constant defined in the code.
"""

import getopt
import inspect
import logging
import sys
import textwrap

from cm_api.api_client import ApiResource

#
# Customize these constants for your Cloudera Manager.
#
CM_HOST = 'localhost'
CM_USER = 'admin'
CM_PASSWD = 'admin'

#
# Hostnames for the set of hosts to change configs for.
#
# The program will throw an exception if any of these hosts are not
# found in Cloudera Manager.
#
HOSTS = [
  'foo1.cloudera.com',
  'foo2.cloudera.com',
  'foo3.cloudera.com',
]

#
# Samples: These are the config settings for the given group of hosts
#
# Change the values to the optimal setting for your hardware profile.
# You do not need all these configs. And the config that you want to
# change may not be here. Remove (or comment out) lines, and add new
# config settings as necessary.
#
# To see what other config parameters are available (for a DN for
# example), access:
#   http://cm:7180/api/v1/clusters/myCDH/services/hdfs1/roles/dn1/config?view=full
#
# Replace "cm", "myCDH", "hdfs1", "dn1" with the actual values from your
# environment.
#
DATANODE_CONF = {
  'dfs_data_dir_list': '/data/1/dfs/dn,/data/2/dfs/dn',
  'dfs_datanode_handler_count': 10,
  'dfs_datanode_du_reserved': 10737418240,
  'dfs_datanode_max_xcievers': 4096,
  'datanode_java_heapsize': 2048000000,
  'datanode_java_opts': ''
}

TASKTRACKER_CONF = {
  'tasktracker_mapred_local_dir_list': '/data/1/mapred/local,/data/2/mapred/local',
  'mapred_tasktracker_map_tasks_maximum': 8,
  'mapred_tasktracker_reduce_tasks_maximum': 8,
  'tasktracker_java_opts': '',
  'task_tracker_java_heapsize': 2048000000,
  # Override client values
  'override_mapred_child_java_opts_max_heap': 2048000000,
  'override_mapred_child_ulimit': 2048000,      # In KiB
  'override_io_sort_mb': 100,
  'override_io_sort_factor': 50,
}

REGIONSERVER_CONF = {
  'hbase_hregion_memstore_flush_size': 1024000000,
  'hbase_regionserver_handler_count': 10,
  'hbase_regionserver_java_heapsize': 2048000000,
  'hbase_regionserver_java_opts': '',
}


LOG = logging.getLogger(__name__)

def do_bulk_config_update(hostnames):
  """
  Given a list of hostnames, update the configs of all the
  datanodes, tasktrackers and regionservers on those hosts.
  """
  api = ApiResource(CM_HOST, username=CM_USER, password=CM_PASSWD)
  hosts = collect_hosts(api, hostnames)

  # Set config
  for h in hosts:
    configure_roles_on_host(api, h)


def collect_hosts(api, wanted_hostnames):
  """
  Return a list of ApiHost objects for the set of hosts that
  we want to change config for.
  """
  all_hosts = api.get_all_hosts(view='full')
  all_hostnames = set([ h.hostname for h in all_hosts])
  wanted_hostnames = set(wanted_hostnames)

  unknown_hosts = wanted_hostnames.difference(all_hostnames)
  if len(unknown_hosts) != 0:
    msg = "The following hosts are not found in Cloudera Manager. "\
          "Please check for typos:\n%s" % ('\n'.join(unknown_hosts))
    LOG.error(msg)
    raise RuntimeError(msg)

  return [ h for h in all_hosts if h.hostname in wanted_hostnames ]


def configure_roles_on_host(api, host):
  """
  Go through all the roles on this host, and configure them if they
  match the role types that we care about.
  """
  for role_ref in host.roleRefs:
    # Mgmt service/role has no cluster name. Skip over those.
    if role_ref.get('clusterName') is None:
      continue

    # Get the role and inspect the role type
    role = api.get_cluster(role_ref['clusterName'])\
              .get_service(role_ref['serviceName'])\
              .get_role(role_ref['roleName'])
    LOG.debug("Evaluating %s (%s)" % (role.name, host.hostname))

    config = None
    if role.type == 'DATANODE':
      config = DATANODE_CONF
    elif role.type == 'TASKTRACKER':
      config = TASKTRACKER_CONF
    elif role.type == 'REGIONSERVER':
      config = REGIONSERVER_CONF
    else:
      continue

    # Set the config
    LOG.info("Configuring %s (%s)" % (role.name, host.hostname))
    role.update_config(config)


def read_host_file(path):
  """
  Read the host file. Return a list of hostnames.
  """
  res = []
  for l in file(path).xreadlines():
    hostname = l.strip()
    if hostname:
      res.append(hostname)
  return res


def setup_logging(level):
  logging.basicConfig()
  logging.getLogger().setLevel(level)


def usage():
  doc = inspect.getmodule(usage).__doc__
  print >>sys.stderr, textwrap.dedent(doc % (sys.argv[0],))


def main(argv):
  setup_logging(logging.INFO)

  # Argument parsing
  try:
    opts, args = getopt.getopt(argv[1:], "hf:")
  except getopt.GetoptError, err:
    print >>sys.stderr, err
    usage()
    return -1

  host_file = None
  for option, val in opts:
    if option == '-h':
      usage()
      return -1
    elif option == '-f':
      host_file = val
    else:
      print >>sys.stderr, "Unknown flag:", option
      usage()
      return -1

  if args:
    print >>sys.stderr, "Unknown trailing argument:", args
    usage()
    return -1

  # Decide which host list to use
  if host_file is not None:
    hostnames = read_host_file(host_file)
    LOG.info("Using host list from file '%s'. Found %d hosts." %
             (host_file, len(hostnames)))
  else:
    hostnames = HOSTS
    LOG.info("Using built-in host list. Found %d hosts." % (len(hostnames),))

  # Do work
  do_bulk_config_update(hostnames)
  return 0


#
# The "main" entry
#
if __name__ == '__main__':
  sys.exit(main(sys.argv))
