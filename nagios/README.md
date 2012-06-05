CM API Sample for Nagios
========================

Sample script to generate Nagios configs and send Nagios passive updates.

WARNING: This is experimental code and shouldn't be used in a production
         environment. Please use this code as an example of how you may
         use the CM API to integrate with Nagios or other systems.

Overview
--------
`cm_nagios.py` performs two functions:

1. To generate Nagios configuration files mapping Nagios hosts and services
   to CM hosts, services, and roles.

       CM hosts     =>  Nagios hosts
       CM clusters  =>  Nagios hosts (are 'virtual hosts')
       CM services  =>  Nagios services (with 'virtual hosts')
       CM roles     =>  Nagios services (with physical hosts)

   Run with the -g parameter to generate the config files and exit.

   *These configuration files should be edited/customized as appropriate.*

2. To poll the CM API for health status and update Nagios.

   Updating Nagios can happen in one of two ways:
   1.  Write to the Nagios "External Command File". (Default)
       The command file is sepecified with the -c/--cmd-file parameter.

       See 'External Commands':
       http://nagios.sourceforge.net/docs/3_0/extcommands.html

   2.  Use `send_nsca` to update Nagios via a NSCA daemon.
       Enabled with the -n/--use-send-nsca option. Parameters are used
       to specify the location of the send_nsca command, the NSCA server
       and port, and the send_nsca configuration file.

   Currently, CM services and roles report a healthSummary which is used
   to determine a Nagios status code:

       HISTORY_NOT_AVAILABLE  =>  UNKNOWN
       NOT_AVAILABLE          =>  UNKNOWN
       DISABLED               =>  UNKNOWN
       GOOD                   =>  OK
       CONCERNING             =>  WARNING
       BAD                    =>  CRITICAL

   The script will terminate after submitting the status updates.

Usage
-----
1. Install the `cm_api` Python client.
2. Create a file containing the CM username and password.

   E.g. `echo "admin:admin" > /safe-directory/cm_password`

   *NOTE*: Set permissions appropriately! If this mechanism isn't secure
           enough for your purposes, feel free to adapt the code to meet
           your needs.

3. Generate Nagios cfg files for the CM services, roles and hosts:

   E.g. the following command generates Nagios configs for the CM host
   specified. See the overview section for an explanation of how the CM
   services, roles, and hosts map to Nagios.

   `python cm_nagios.py --generate-cfg \
      --host $CM_HOST --passfile $CM_PASSFILE \
      --cfg-dir=$NAGIOS_CM_CFG_DIR`

   See the help `cm_nagios.py -h` for all arguments.

   Ensure that Nagios can find the generated cfg files. If the directory
   `$NAGIOS_CM_CFG_DIR` is a non-standard location, you may have to add
   a `cfg_dir` parameter in the Nagios configuration (usually found in
   `/etc/nagios/nagios.cfg`).
  
   You may need to reload the Nagios configuration, e.g.:
   `/etc/init.d/nagios reload`

   See the Nagios documentation for help with configuration:
   http://nagios.sourceforge.net/docs/3_0/configmain.html

4. Run the script to send passive updates to Nagios

   `python cm_nagios.py --update-status \
      --host $CM_HOST --passfile $CM_PASSFILE \
      --cmd-file $NAGIOS_CMD_FILE`

   The script writes updates for each of the CM services, roles, and
   hosts to the Nagios external commands file.
   
   This process can easily be configured to run periodically with cron
   or mechanisms.
