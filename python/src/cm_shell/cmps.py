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

import sys
import getpass
import argparse
import readline
import os
import cmd
from prettytable import PrettyTable
from cm_api.api_client import ApiResource, ApiException
from urllib2 import URLError

# Config
CONFIG = {'cluster': None, 'output_type': 'table', 'seperator': None}

# Initial Prompt
INIT_PROMPT = "cloudera> "

# Banner shown at interactive shell login
BANNER = "Welcome to the Cloudera Manager Console\nSelect a cluster using 'show clusters' and 'use'"

# If true, than the user is running a non-interactive shell (ie: scripting)
EXECUTE = False

# Readline fix for hyphens
readline.set_completer_delims(readline.get_completer_delims().replace('-', ''))

# Global API object
api = None


class ClouderaShell(cmd.Cmd):
    """
    Interactive shell for communicating with your
    Cloudera Cluster making use of the cm_api
    """

    # Set initial cloudera prompt
    prompt = INIT_PROMPT

    # Set login banner
    intro = BANNER

    # Help headers
    doc_header = "Cloudera Manager Commands"
    undoc_header = "Other Commands"

    # Initial cache is blank
    # when autocomplete for one of these components
    # is triggered, it will automatically cache them
    CACHED_ROLES = {}
    CACHED_SERVICES = None
    CACHED_CLUSTERS = None

    def preloop(self):
        "Checks if the cluster was pre-defined"
        if CONFIG['cluster']:
            self.set_cluster(CONFIG['cluster'])
        else:
            self.cluster_object = None

    def generate_output(self, headers, rows, align=None):
        if CONFIG['output_type'] == "table":
            table = PrettyTable(headers)
            if align:
                for h in align:
                    table.align[h] = 'l'

            for r in rows:
                table.add_row(r)
            print(table)

        if CONFIG['output_type'] == "csv":
            print(','.join(headers))
            for r in rows:
                print(','.join(r))

        if CONFIG['output_type'] == "custom":
            SEP = CONFIG['seperator']
            print(SEP.join(headers))
            for r in rows:
                print(SEP.join(r))

    def emptyline(self):
        """Called each time a user hits enter, by
        default it will redo the last command, this
        is an extension so it does nothing."""
        pass

    def set_cluster(self, cluster):
        try:
            cluster = api.get_cluster(cluster)
        except ApiException:
            print("Cluster Not Found!")
            return None

        self.cluster_object = cluster
        if not EXECUTE:
            print("Connected to %s" % (cluster.name))
        self.prompt = cluster.name + "> "
        return True

    @property
    def cluster(self):
        if EXECUTE:
            if not self.set_cluster(CONFIG['cluster']):
                sys.exit(1)
            return self.cluster_object.name

        if self.cluster_object:
            return self.cluster_object.name
        else:
            return None

    def has_cluster(self):
        if not self.cluster:
            print("Error: No cluster currently selected")
            return None
        else:
            return True

    def get_log(self, role, log_type=None):
        if not role:
            return None

        if not self.has_cluster():
            return None

        if '-' not in role:
            print("Please enter a valid role name")
            return None

        try:
            service = api.get_cluster(self.cluster).get_service(role.split('-')[0])
            role = service.get_role(role)
            try:
                if EXECUTE:
                    output = sys.stdout
                else:
                    output = os.popen("less", "w")
                if log_type == "full":
                    output.write(role.get_full_log())
                if log_type == "stdout":
                    output.write(role.get_stdout())
                if log_type == "stderr":
                    output.write(role.get_stderr())

                if not EXECUTE:
                    output.close()
            except IOError:
                pass
        except ApiException:
            print("Error: Role or Service Not Found")

    def do_status(self, service):
        """
        List all services on the cluster
        Usage:
            > status
        """
        if service:
            self.do_show("services", single=service)
        else:
            self.do_show("services")

    def do_log(self, role):
        """
        Download log file for role
        Usage:
            > log <role>    Download log
        """
        self.get_log(role, log_type="full")

    def do_stdout(self, role):
        """
        Download stdout file for role
        Usage:
            > stdout <role>     Download stdout
        """
        self.get_log(role, log_type="stdout")

    def do_stderr(self, role):
        """
        Download stderr file for role
        Usage:
            > stderr <role>     Download stderr
        """
        self.get_log(role, log_type="stderr")

    def do_show(self, option, single=None):
        """
        General System Information
        Usage:
            > show clusters     list of clusters this CM manages
            > show hosts        list of all hosts CM manages
            > show services     list of all services on this cluster
                                including their health.
        """
        headers = []
        rows = []
        align = None
        # show clusters
        if option == "clusters":
            "Display list of clusters on system"
            headers = ["CLUSTER NAME"]
            clusters = api.get_all_clusters()
            for cluster in clusters:
                rows.append([cluster.name])

        # show hosts
        if option == "hosts":
            "Display a list of hosts avaiable on the system"
            headers = ["HOSTNAME", "IP ADDRESS", "RACK"]
            align = ["HOSTNAME", "IP ADDRESS", "RACK"]
            for host in api.get_all_hosts():
                rows.append([host.hostname, host.ipAddress, host.rackId])

        # show services
        if option == "services":
            "Show list of services on the cluster"
            headers = ["NAME", "SERVICE", "STATUS", "HEALTH", "CONFIG"]
            align = ["NAME", "SERVICE"]

            # Check if the user has selected a cluster
            if not self.has_cluster():
                print("Error: Please select a cluster first")
                return None

            if not single:
                for s in api.get_cluster(self.cluster).get_all_services():
                    if s.configStale:
                        config = "STALE"
                    else:
                        config = "UP TO DATE"
                    rows.append([s.name, s.type, s.serviceState, s.healthSummary, config])
            else:
                s = api.get_cluster(self.cluster).get_service(single)
                if s.configStale:
                    config = "STALE"
                else:
                    config = "UP TO DATE"
                rows.append([s.name, s.type, s.serviceState, s.healthSummary, config])

        self.generate_output(headers, rows, align=align)

    def complete_log(self, text, line, start_index, end_index):
        return self.roles_autocomplete(text, line, start_index, end_index)

    def complete_stdout(self, text, line, start_index, end_index):
        return self.roles_autocomplete(text, line, start_index, end_index)

    def complete_stderr(self, text, line, start_index, end_index):
        return self.roles_autocomplete(text, line, start_index, end_index)

    def complete_show(self, text, line, start_index, end_index):
        show_commands = ["clusters", "hosts", "services"]
        if text:
            return [c for c in show_commands if c.startswith(text)]
        else:
            return show_commands

    def service_action(self, service, action):
        "Perform given action on service for the selected cluster"
        try:
            service = api.get_cluster(self.cluster).get_service(service)
        except ApiException:
            print("Service not found")
            return None

        if action == "start":
            service.start()
        if action == "restart":
            service.restart()
        if action == "stop":
            service.stop()

        return True

    def services_autocomplete(self, text, line, start_index, end_index, append=[]):
        if not self.cluster:
            return None
        else:
            if not self.CACHED_SERVICES:
                services = [s.name for s in api.get_cluster(self.cluster).get_all_services()]
                self.CACHED_SERVICES = services

            if text:
                return [s for s in self.CACHED_SERVICES + append if s.startswith(text)]
            else:
                return self.CACHED_SERVICES + append

    def do_start_service(self, service):
        """
        Start a service
        Usage:
            > start_service <service>
        """
        if not self.has_cluster():
            return None

        if self.service_action(service=service, action="start"):
            print("%s is being started" % (service))
        else:
            print("Error starting service")
            return None

    def complete_start_service(self, text, line, start_index, end_index):
        return self.services_autocomplete(text, line, start_index, end_index)

    def do_restart_service(self, service):
        """
        Restart a service
        Usage:
            > restart_service <service>
        """
        if not self.has_cluster():
            return None

        if self.service_action(service=service, action="restart"):
            print("%s is being restarted" % (service))
        else:
            print("Error restarting service")
            return None

    def complete_restart_service(self, text, line, start_index, end_index):
        return self.services_autocomplete(text, line, start_index, end_index)

    def do_stop_service(self, service):
        """
        Stop a service
        Usage:
            > stop_service <service>
        """
        if not self.has_cluster():
            return None

        if self.service_action(service=service, action="stop"):
            print("%s is being stopped" % (service))
        else:
            print("Error stopping service")
            return None

    def complete_stop_service(self, text, line, start_index, end_index):
        return self.services_autocomplete(text, line, start_index, end_index)

    def do_use(self, cluster):
        """
        Connect to Cluster
        Usage:
            > use <cluster>
        """
        if not self.set_cluster(cluster):
            print("Error setting cluster")

    def cluster_autocomplete(self, text, line, start_index, end_index):
        "autocomplete for the use command, obtain list of clusters first"
        if not self.CACHED_CLUSTERS:
            clusters = [cluster.name for cluster in api.get_all_clusters()]
            self.CACHED_CLUSTERS = clusters

        if text:
            return [cluster for cluster in self.CACHED_CLUSTERS if cluster.startswith(text)]
        else:
            return self.CACHED_CLUSTERS

    def complete_use(self, text, line, start_index, end_index):
        return self.cluster_autocomplete(text, line, start_index, end_index)

    def do_roles(self, service):
        """
        Role information
        Usage:
            > roles <servicename>   Display role information for service
            > roles all             Display all role information for cluster
        """
        if not self.has_cluster():
            return None

        if not service:
            return None

        if service == "all":
            if not self.CACHED_SERVICES:
                self.services_autocomplete('', service, 0, 0)

            for s in self.CACHED_SERVICES:
                print("= " + s.upper() + " =")
                self.do_roles(s)
            return None
        try:
            service = api.get_cluster(self.cluster).get_service(service)
            headers = ["ROLE TYPE", "HOST", "ROLE NAME", "STATE", "HEALTH", "CONFIG"]
            align = ["ROLE TYPE", "ROLE NAME", "HOST"]
            rows = []
            for roletype in service.get_role_types():
                for role in service.get_roles_by_type(roletype):
                    if role.configStale:
                        config = "STALE"
                    else:
                        config = "UP TO DATE"
                    rows.append([role.type, role.hostRef.hostId, role.name, role.roleState, role.healthSummary, config])
            self.generate_output(headers, rows, align=align)
        except ApiException:
            print("Service not found")

    def complete_roles(self, text, line, start_index, end_index):
        return self.services_autocomplete(text, line, start_index, end_index, append=["all"])

    def roles_autocomplete(self, text, line, start_index, end_index):
        "Return full list of roles"
        if '-' not in line:
            # Append a dash to each service, makes for faster autocompletion of
            # roles
            return [s + '-' for s in self.services_autocomplete(text, line, start_index, end_index)]
        else:
            key, role = line.split()[1].split('-', 1)
            if key not in self.CACHED_ROLES:
                service = api.get_cluster(self.cluster).get_service(key)
                roles = []
                for t in service.get_role_types():
                    for r in service.get_roles_by_type(t):
                        roles.append(r.name)

                self.CACHED_ROLES[key] = roles

            if not role:
                return self.CACHED_ROLES[key]
            else:
                return [r for r in self.CACHED_ROLES[key] if r.startswith(line.split()[1])]

    def do_start_role(self, role):
        """
        Start a role
        Usage:
            > start_role <role>     Restarts this role
        """
        if not role:
            return None

        if not self.has_cluster():
            return None

        if '-' not in role:
            print("Please enter a valid role name")
            return None

        try:
            service = api.get_cluster(self.cluster).get_service(role.split('-')[0])
            service.start_roles(role)
            print("Starting Role")
        except ApiException:
            print("Error: Role or Service Not Found")

    def complete_start_role(self, text, line, start_index, end_index):
        return self.roles_autocomplete(text, line, start_index, end_index)

    def do_restart_role(self, role):
        """
        Restart a role
        Usage:
            > restart_role <role>   Restarts this role
        """
        if not role:
            return None

        if not self.has_cluster():
            return None

        if '-' not in role:
            print("Please enter a valid role name")
            return None

        try:
            service = api.get_cluster(self.cluster).get_service(role.split('-')[0])
            service.restart_roles(role)
            print("Restarting Role")
        except ApiException:
            print("Error: Role or Service Not Found")

    def complete_restart_role(self, text, line, start_index, end_index):
        return self.roles_autocomplete(text, line, start_index, end_index)

    def do_stop_role(self, role):
        """
        Stop a role
        Usage:
            > stop_role <role>  Stops this role
        """
        if not role:
            return None

        if not self.has_cluster():
            return None

        if '-' not in role:
            print("Please enter a valid role name")
            return None

        try:
            service = api.get_cluster(self.cluster).get_service(role.split('-')[0])
            service.stop_roles(role)
            print("Stopping Role")
        except ApiException:
            print("Error: Role or Service Not Found")

    def complete_stop_role(self, text, line, start_index, end_index):
        return self.roles_autocomplete(text, line, start_index, end_index)

    def do_stop_cluster(self, cluster):
        """
        Completely stop the cluster
        Usage:
            > stop_cluster <cluster>
        """
        try:
            cluster = api.get_cluster(cluster)
            cluster.stop()
            print("Stopping Cluster")
        except ApiException:
            print("Cluster not found")
            return None

    def complete_stop_cluster(self, text, line, start_index, end_index):
        return self.cluster_autocomplete(text, line, start_index, end_index)

    def do_start_cluster(self, cluster):
        """
        Start the cluster
        Usage:
            > start_cluster <cluster>
        """
        try:
            cluster = api.get_cluster(cluster)
            cluster.start()
            print("Starting Cluster")
        except ApiException:
            print("Cluster not found")
            return None

    def complete_start_cluster(self, text, line, start_index, end_index):
        return self.cluster_autocomplete(text, line, start_index, end_index)

    def do_version(self, cluster=None):
        """
        Obtain cluster CDH version
        Usage:
            > version
            or
            > version <cluster>
        """
        if not cluster:
            if not self.has_cluster():
                return None
            else:
                cluster = api.get_cluster(self.cluster)
        else:
            try:
                cluster = api.get_cluster(cluster)
            except ApiException:
                print("Error: Cluster not found")
                return None

        print("Version: %s" % (cluster.version))

    def complete_version(self, text, line, start_index, end_index):
        return self.cluster_autocomplete(text, line, start_index, end_index)

    def complete_status(self, text, line, start_index, end_index):
        return self.services_autocomplete(text, line, start_index, end_index)


def main():
    parser = argparse.ArgumentParser(description='Cloudera Manager Shell')
    parser.add_argument('-H', '--host', '--hostname', action='store', dest='hostname', required=True)
    parser.add_argument('-p', '--port', action='store', dest='port', type=int, default=7180)
    parser.add_argument('-u', '--user', '--username',  action='store', dest='username')
    parser.add_argument('-c', '--cluster', action='store', dest='cluster')
    parser.add_argument('--password', action='store', dest='password')
    parser.add_argument('-e', '--execute', action='store', dest='execute')
    parser.add_argument('-s', '--seperator', action='store', dest='seperator')
    parser.add_argument('-t', '--tls', action='store_const', dest='use_tls', const=True, default=False)
    args = parser.parse_args()

    # Check if a username was suplied, if not, prompt the user
    if not args.username:
        args.username = raw_input("Enter Username: ")

    # Check if the password was supplied, if not, prompt the user
    if not args.password:
        args.password = getpass.getpass("Enter Password: ")

    # Attempt to authenticate using the API
    global api
    api = ApiResource(args.hostname, args.port, args.username, args.password, args.use_tls)
    try:
        api.echo("ping")
    except ApiException:
        try:
            api = ApiResource(args.hostname, args.port, args.username, args.password, args.use_tls, version=1)
            api.echo("ping")
        except ApiException:
            print("Unable to Authenticate")
            sys.exit(1)
    except URLError:
        print("Error: Could not connect to %s" % (args.hostname))
        sys.exit(1)

    CONFIG['cluster'] = args.cluster

    # Check if a custom seperator was supplied for the output
    if args.seperator:
        CONFIG['output_type'] = 'custom'
        CONFIG['seperator'] = args.seperator

    # Check if user is attempting non-interactive shell
    if args.execute:
        EXECUTE = True
        shell = ClouderaShell()
        for command in args.execute.split(';'):
            shell.onecmd(command)
        sys.exit(0)

    try:
        ClouderaShell().cmdloop()
    except KeyboardInterrupt:
        sys.stdout.write("\n")
        sys.exit(0)

if __name__ == "__main__":
    main()
