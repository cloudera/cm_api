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

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import *
from cm_api.endpoints import roles, role_config_groups

__docformat__ = "epytext"

SERVICES_PATH = "/clusters/%s/services"
SERVICE_PATH = "/clusters/%s/services/%s"
ROLETYPES_CFG_KEY = 'roleTypeConfigs'

def create_service(resource_root, name, service_type,
                   cluster_name="default"):
  """
  Create a service
  @param resource_root: The root Resource object.
  @param name: Service name
  @param service_type: Service type
  @param cluster_name: Cluster name
  @return: An ApiService object
  """
  apiservice = ApiService(resource_root, name, service_type)
  return call(resource_root.post,
      SERVICES_PATH % (cluster_name,),
      ApiService, True, data=[apiservice])[0]

def get_service(resource_root, name, cluster_name="default"):
  """
  Lookup a service by name
  @param resource_root: The root Resource object.
  @param name: Service name
  @param cluster_name: Cluster name
  @return: An ApiService object
  """
  return _get_service(resource_root, "%s/%s" % (SERVICES_PATH % (cluster_name,), name))

def _get_service(resource_root, path):
  return call(resource_root.get, path, ApiService)

def get_all_services(resource_root, cluster_name="default", view=None):
  """
  Get all services
  @param resource_root: The root Resource object.
  @param cluster_name: Cluster name
  @return: A list of ApiService objects.
  """
  return call(resource_root.get,
      SERVICES_PATH % (cluster_name,),
      ApiService, True, params=view and dict(view=view) or None)

def delete_service(resource_root, name, cluster_name="default"):
  """
  Delete a service by name
  @param resource_root: The root Resource object.
  @param name: Service name
  @param cluster_name: Cluster name
  @return: The deleted ApiService object
  """
  return call(resource_root.delete,
      "%s/%s" % (SERVICES_PATH % (cluster_name,), name),
      ApiService)


class ApiService(BaseApiResource):
  _ATTRIBUTES = {
    'name'                        : None,
    'type'                        : None,
    'displayName'                 : None,
    'serviceState'                : ROAttr(),
    'healthSummary'               : ROAttr(),
    'healthChecks'                : ROAttr(),
    'clusterRef'                  : ROAttr(ApiClusterRef),
    'configStale'                 : ROAttr(),
    'configStalenessStatus'       : ROAttr(),
    'clientConfigStalenessStatus' : ROAttr(),
    'serviceUrl'                  : ROAttr(),
    'roleInstancesUrl'            : ROAttr(),
    'maintenanceMode'             : ROAttr(),
    'maintenanceOwners'           : ROAttr(),
    'entityStatus'                : ROAttr(),
  }

  def __init__(self, resource_root, name=None, type=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiService>: %s (cluster: %s)" % (
        self.name, self._get_cluster_name())

  def _get_cluster_name(self):
    if hasattr(self, 'clusterRef') and self.clusterRef:
      return self.clusterRef.clusterName
    return None

  def _path(self):
    """
    Return the API path for this service.

    This method assumes that lack of a cluster reference means that the
    object refers to the Cloudera Management Services instance.
    """
    if self._get_cluster_name():
      return SERVICE_PATH % (self._get_cluster_name(), self.name)
    else:
      return '/cm/service'

  def _role_cmd(self, cmd, roles, api_version=1):
    return self._post("roleCommands/" + cmd, ApiBulkCommandList,
        data=roles, api_version=api_version)

  def _parse_svc_config(self, json_dic, view = None):
    """
    Parse a json-decoded ApiServiceConfig dictionary into a 2-tuple.

    @param json_dic: The json dictionary with the config data.
    @param view: View to materialize.
    @return: 2-tuple (service config dictionary, role type configurations)
    """
    svc_config = json_to_config(json_dic, view == 'full')
    rt_configs = { }
    if json_dic.has_key(ROLETYPES_CFG_KEY):
      for rt_config in json_dic[ROLETYPES_CFG_KEY]:
        rt_configs[rt_config['roleType']] = \
            json_to_config(rt_config, view == 'full')

    return (svc_config, rt_configs)

  def get_commands(self, view=None):
    """
    Retrieve a list of running commands for this service.

    @param view: View to materialize ('full' or 'summary')
    @return: A list of running commands.
    """
    return self._get("commands", ApiCommand, True,
        params = view and dict(view=view) or None)

  def get_running_activities(self):
    return self.query_activities()

  def query_activities(self, query_str=None):
    return self._get("activities", ApiActivity, True,
        params=query_str and dict(query=query_str) or dict())

  def get_activity(self, job_id):
    return self._get("activities/" + job_id, ApiActivity)

  def list_watched_directories(self):
    """
    Returns a list of directories being watched by the Reports Manager.

    @return: A list of directories being watched
    @since: API v14
    """
    return self._get("watcheddir", ApiWatchedDir, ret_is_list=True, api_version=14)

  def add_watched_directory(self, dir_path):
    """
    Adds a directory to the watching list.

    @param dir_path: The path of the directory to be added to the watching list
    @return: The added directory, or null if failed
    @since: API v14
    """
    req = ApiWatchedDir(self._get_resource_root(), path=dir_path)
    return self._post("watcheddir", ApiWatchedDir, data=req, api_version=14)

  def remove_watched_directory(self, dir_path):
    """
    Removes a directory from the watching list.

    @param dir_path: The path of the directory to be removed from the watching list
    @return: The removed directory, or null if failed
    @since: API v14
    """
    return self._delete("watcheddir/%s" % dir_path, ApiWatchedDir, api_version=14)

  def get_impala_queries(self, start_time, end_time, filter_str="", limit=100,
     offset=0):
    """
    Returns a list of queries that satisfy the filter

    @type start_time: datetime.datetime. Note that the datetime must either be
                      time zone aware or specified in the server time zone. See
                      the python datetime documentation for more details about
                      python's time zone handling.
    @param start_time: Queries must have ended after this time
    @type end_time: datetime.datetime. Note that the datetime must either be
                    time zone aware or specified in the server time zone. See
                    the python datetime documentation for more details about
                    python's time zone handling.
    @param end_time: Queries must have started before this time
    @param filter_str: A filter to apply to the queries. For example:
                       'user = root and queryDuration > 5s'
    @param limit: The maximum number of results to return
    @param offset: The offset into the return list
    @since: API v4
    """
    params = {
      'from':   start_time.isoformat(),
      'to':     end_time.isoformat(),
      'filter': filter_str,
      'limit':  limit,
      'offset': offset,
    }
    return self._get("impalaQueries", ApiImpalaQueryResponse,
        params=params, api_version=4)

  def cancel_impala_query(self, query_id):
    """
    Cancel the query.

    @param query_id: The query ID
    @return: The warning message, if any.
    @since: API v4
    """
    return self._post("impalaQueries/%s/cancel" % query_id,
        ApiImpalaCancelResponse, api_version=4)

  def get_query_details(self, query_id, format='text'):
    """
    Get the query details

    @param query_id: The query ID
    @param format: The format of the response ('text' or 'thrift_encoded')
    @return: The details text
    @since: API v4
    """
    return self._get("impalaQueries/" + query_id, ApiImpalaQueryDetailsResponse,
        params=dict(format=format), api_version=4)

  def get_impala_query_attributes(self):
    """
    Returns the list of all attributes that the Service Monitor can associate
    with Impala queries.

    Examples of attributes include the user who issued the query and the
    number of HDFS bytes read by the query.

    These attributes can be used to search for specific Impala queries through
    the get_impala_queries API. For example the 'user' attribute could be used
    in the search 'user = root'. If the attribute is numeric it can also be used
    as a metric in a tsquery (ie, 'select hdfs_bytes_read from IMPALA_QUERIES').

    Note that this response is identical for all Impala services.

    @return: A list of the Impala query attributes
    @since API v6
    """
    return self._get("impalaQueries/attributes", ApiImpalaQueryAttribute,
        ret_is_list=True, api_version=6)

  def create_impala_catalog_database(self):
    """
    Create the Impala Catalog Database. Only works with embedded postgresql
    database. This command should usually be followed by a call to
    create_impala_catalog_database_tables.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('impalaCreateCatalogDatabase', api_version=6)

  def create_impala_catalog_database_tables(self):
    """
    Creates the Impala Catalog Database tables in the configured database.
    Will do nothing if tables already exist. Will not perform an upgrade.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('impalaCreateCatalogDatabaseTables', api_version=6)

  def create_impala_user_dir(self):
    """
    Create the Impala user directory

    @return: Reference to submitted command.
    @since: API v6
    """
    return self._cmd('impalaCreateUserDir', api_version=6)

  def enable_llama_rm(self, llama1_host_id, llama1_role_name=None,
                      llama2_host_id=None, llama2_role_name=None,
                      zk_service_name=None, skip_restart=False):
    """
    Enable Llama-based resource management for Impala.

    This command only applies to CDH 5.1+ Impala services.

    This command configures YARN and Impala for Llama resource management,
    and then creates one or two Llama roles, as specified by the parameters.
    When two Llama roles are created, they are configured as an active-standby
    pair. Auto-failover from active to standby Llama will be enabled using
    ZooKeeper.

    If optional role name(s) are specified, the new Llama role(s) will be
    named accordingly; otherwise, role name(s) will be automatically generated.

    By default, YARN, Impala, and any dependent services will be restarted,
    and client configuration will be re-deployed across the cluster. These
    default actions may be suppressed.

    In order to enable Llama resource management, a YARN service must be
    present in the cluster, and Cgroup-based resource management must be
    enabled for all hosts with NodeManager roles. If these preconditions
    are not met, the command will fail.

    @param llama1_host_id: id of the host where the first Llama role will
                           be created.
    @param llama1_role_name: Name of the first Llama role. If omitted, a
                             name will be generated automatically.
    @param llama2_host_id: id of the host where the second Llama role will
                           be created. If omitted, only one Llama role will
                           be created (i.e., high availability will not be
                           enabled).
    @param llama2_role_name: Name of the second Llama role. If omitted, a
                             name will be generated automatically.
    @param zk_service_name: Name of the ZooKeeper service to use for
                            auto-failover. Only relevant when enabling
                            Llama RM in HA mode (i.e., when creating two
                            Llama roles). If Impala's ZooKeeper dependency
                            is already set, then that ZooKeeper service will
                            be used for auto-failover, and this parameter
                            may be omitted.
    @param skip_restart: true to skip the restart of Yarn, Impala, and
                         their dependent services, and to skip deployment
                         of client configuration. Default is False (i.e.,
                         by default dependent services are restarted and
                         client configuration is deployed).
    @return: Reference to the submitted command.
    @since: API v8
    """
    args = dict(
      llama1HostId = llama1_host_id,
      llama1RoleName = llama1_role_name,
      llama2HostId = llama2_host_id,
      llama2RoleName = llama2_role_name,
      zkServiceName = zk_service_name,
      skipRestart = skip_restart
    )
    return self._cmd('impalaEnableLlamaRm', data=args, api_version=8)

  def disable_llama_rm(self):
    """
    Disable Llama-based resource management for Impala.

    This command only applies to CDH 5.1+ Impala services.

    This command disables resource management for Impala by removing all
    Llama roles present in the Impala service. Any services that depend
    on the Impala service being modified are restarted by the command,
    and client configuration is deployed for all services of the cluster.

    @return: Reference to the submitted command.
    @since: API v8
    """
    return self._cmd('impalaDisableLlamaRm', api_version=8)

  def enable_llama_ha(self, new_llama_host_id, zk_service_name=None,
                      new_llama_role_name=None):
    """
    Enable high availability for an Impala Llama ApplicationMaster.

    This command only applies to CDH 5.1+ Impala services.

    @param new_llama_host_id: id of the host where the second Llama role
                              will be added.
    @param zk_service_name: Name of the ZooKeeper service to use for
                            auto-failover. If Impala's ZooKeeper dependency
                            is already set, then that ZooKeeper service will
                            be used for auto-failover, and this parameter
                            may be omitted.
    @param new_llama_role_name: Name of the new Llama role. If omitted, a
                                name will be generated automatically.
    @return: Reference to the submitted command.
    @since: API v8
    """
    args = dict(
      newLlamaHostId = new_llama_host_id,
      zkServiceName = zk_service_name,
      newLlamaRoleName = new_llama_role_name
    )
    return self._cmd('impalaEnableLlamaHa', data=args, api_version=8)

  def disable_llama_ha(self, active_name):
    """
    Disable high availability for an Impala Llama active-standby pair.

    This command only applies to CDH 5.1+ Impala services.

    @param active_name: name of the Llama role that will be active after
                        the disable operation. The other Llama role will
                        be removed.

    @return: Reference to the submitted command.
    @since: API v8
    """
    args = dict(
      activeName = active_name
    )
    return self._cmd('impalaDisableLlamaHa', data=args, api_version=8)

  def get_yarn_applications(self, start_time, end_time, filter_str="", limit=100,
      offset=0):
    """
    Returns a list of YARN applications that satisfy the filter
    @type start_time: datetime.datetime. Note that the datetime must either be
                      time zone aware or specified in the server time zone. See
                      the python datetime documentation for more details about
                      python's time zone handling.
    @param start_time: Applications must have ended after this time
    @type end_time: datetime.datetime. Note that the datetime must either be
                    time zone aware or specified in the server time zone. See
                    the python datetime documentation for more details about
                    python's time zone handling.
    @param filter_str: A filter to apply to the applications. For example:
                       'user = root and applicationDuration > 5s'
    @param limit: The maximum number of results to return
    @param offset: The offset into the return list
    @since: API v6
    """
    params = {
      'from':   start_time.isoformat(),
      'to':     end_time.isoformat(),
      'filter': filter_str,
      'limit':  limit,
      'offset': offset
    }
    return self._get("yarnApplications", ApiYarnApplicationResponse,
        params=params, api_version=6)

  def kill_yarn_application(self, application_id):
    """
    Kills the application.

    @return: The warning message, if any.
    @since: API v6
    """
    return self._post("yarnApplications/%s/kill" % (application_id, ),
        ApiYarnKillResponse, api_version=6)

  def get_yarn_application_attributes(self):
    """
    Returns the list of all attributes that the Service Monitor can associate
    with YARN applications.

    Examples of attributes include the user who ran the application and the
    number of maps completed by the application.

    These attributes can be used to search for specific YARN applications through
    the get_yarn_applications API. For example the 'user' attribute could be used
    in the search 'user = root'. If the attribute is numeric it can also be used
    as a metric in a tsquery (ie, 'select maps_completed from YARN_APPLICATIONS').

    Note that this response is identical for all YARN services.

    @return: A list of the YARN application attributes
    @since API v6
    """
    return self._get("yarnApplications/attributes", ApiYarnApplicationAttribute,
        ret_is_list=True, api_version=6)

  def create_yarn_job_history_dir(self):
    """
    Create the Yarn job history directory.

    @return: Reference to submitted command.
    @since: API v6
    """
    return self._cmd('yarnCreateJobHistoryDirCommand', api_version=6)

  def create_yarn_node_manager_remote_app_log_dir(self):
    """
    Create the Yarn NodeManager remote application log directory.

    @return: Reference to submitted command.
    @since: API v6
    """
    return self._cmd('yarnNodeManagerRemoteAppLogDirCommand', api_version=6)

  def collect_yarn_application_diagnostics(self, *application_ids):
    """
    DEPRECATED: use create_yarn_application_diagnostics_bundle on the Yarn service. Deprecated since v10.

    Collects the Diagnostics data for Yarn applications.

    @param application_ids: An array of strings containing the ids of the
                            yarn applications.
    @return: Reference to the submitted command.
    @since: API v8
    """
    args = dict(applicationIds = application_ids)
    return self._cmd('yarnApplicationDiagnosticsCollection', api_version=8, data=args)

  def create_yarn_application_diagnostics_bundle(self, application_ids, ticket_number=None, comments=None):
    """
    Collects the Diagnostics data for Yarn applications.

    @param application_ids: An array of strings containing the ids of the
                            yarn applications.
    @param ticket_number: If applicable, the support ticket number of the issue
                          being experienced on the cluster.
    @param comments: Additional comments
    @return: Reference to the submitted command.
    @since: API v10
    """
    args = dict(applicationIds = application_ids,
                ticketNumber = ticket_number,
                comments = comments)

    return self._cmd('yarnApplicationDiagnosticsCollection', api_version=10, data=args)

  def get_config(self, view = None):
    """
    Retrieve the service's configuration.

    Retrieves both the service configuration and role type configuration
    for each of the service's supported role types. The role type
    configurations are returned as a dictionary, whose keys are the
    role type name, and values are the respective configuration dictionaries.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return: 2-tuple (service config dictionary, role type configurations)
    """
    path = self._path() + '/config'
    resp = self._get_resource_root().get(path,
        params = view and dict(view=view) or None)
    return self._parse_svc_config(resp, view)

  def update_config(self, svc_config, **rt_configs):
    """
    Update the service's configuration.

    @param svc_config: Dictionary with service configuration to update.
    @param rt_configs: Dict of role type configurations to update.
    @return: 2-tuple (service config dictionary, role type configurations)
    """
    path = self._path() + '/config'

    if svc_config:
      data = config_to_api_list(svc_config)
    else:
      data = { }
    if rt_configs:
      rt_list = [ ]
      for rt, cfg in rt_configs.iteritems():
        rt_data = config_to_api_list(cfg)
        rt_data['roleType'] = rt
        rt_list.append(rt_data)
      data[ROLETYPES_CFG_KEY] = rt_list

    resp = self._get_resource_root().put(path, data = json.dumps(data))
    return self._parse_svc_config(resp)

  def create_role(self, role_name, role_type, host_id):
    """
    Create a role.

    @param role_name: Role name
    @param role_type: Role type
    @param host_id: ID of the host to assign the role to
    @return: An ApiRole object
    """
    return roles.create_role(self._get_resource_root(), self.name, role_type,
        role_name, host_id, self._get_cluster_name())

  def delete_role(self, name):
    """
    Delete a role by name.

    @param name: Role name
    @return: The deleted ApiRole object
    """
    return roles.delete_role(self._get_resource_root(), self.name, name,
        self._get_cluster_name())

  def get_role(self, name):
    """
    Lookup a role by name.

    @param name: Role name
    @return: An ApiRole object
    """
    return roles.get_role(self._get_resource_root(), self.name, name,
        self._get_cluster_name())

  def get_all_roles(self, view = None):
    """
    Get all roles in the service.

    @param view: View to materialize ('full' or 'summary')
    @return: A list of ApiRole objects.
    """
    return roles.get_all_roles(self._get_resource_root(), self.name,
        self._get_cluster_name(), view)

  def get_roles_by_type(self, role_type, view = None):
    """
    Get all roles of a certain type in a service.

    @param role_type: Role type
    @param view: View to materialize ('full' or 'summary')
    @return: A list of ApiRole objects.
    """
    return roles.get_roles_by_type(self._get_resource_root(), self.name,
        role_type, self._get_cluster_name(), view)

  def get_role_types(self):
    """
    Get a list of role types in a service.

    @return: A list of role types (strings)
    """
    resp = self._get_resource_root().get(self._path() + '/roleTypes')
    return resp[ApiList.LIST_KEY]

  def get_all_role_config_groups(self):
    """
    Get a list of role configuration groups in the service.

    @return: A list of ApiRoleConfigGroup objects.
    @since: API v3
    """
    return role_config_groups.get_all_role_config_groups(
        self._get_resource_root(), self.name, self._get_cluster_name())

  def get_role_config_group(self, name):
    """
    Get a role configuration group in the service by name.

    @param name: The name of the role config group.
    @return: An ApiRoleConfigGroup object.
    @since: API v3
    """
    return role_config_groups.get_role_config_group(
        self._get_resource_root(), self.name, name, self._get_cluster_name())

  def create_role_config_group(self, name, display_name, role_type):
    """
    Create a role config group.

    @param name: The name of the new group.
    @param display_name: The display name of the new group.
    @param role_type: The role type of the new group.
    @return: New ApiRoleConfigGroup object.
    @since: API v3
    """
    return role_config_groups.create_role_config_group(
        self._get_resource_root(), self.name, name, display_name, role_type,
        self._get_cluster_name())

  def update_role_config_group(self, name, apigroup):
    """
    Update a role config group.

    @param name: Role config group name.
    @param apigroup: The updated role config group.
    @return: The updated ApiRoleConfigGroup object.
    @since: API v3
    """
    return role_config_groups.update_role_config_group(
        self._get_resource_root(), self.name, name, apigroup,
        self._get_cluster_name())

  def delete_role_config_group(self, name):
    """
    Delete a role config group by name.

    @param name: Role config group name.
    @return: The deleted ApiRoleConfigGroup object.
    @since: API v3
    """
    return role_config_groups.delete_role_config_group(
        self._get_resource_root(), self.name, name, self._get_cluster_name())

  def get_metrics(self, from_time=None, to_time=None, metrics=None, view=None):
    """
    This endpoint is not supported as of v6. Use the timeseries API
    instead. To get all metrics for a service with the timeseries API use
    the query:

    'select * where serviceName = $SERVICE_NAME'.

    To get specific metrics for a service use a comma-separated list of
    the metric names as follows:

    'select $METRIC_NAME1, $METRIC_NAME2 where serviceName = $SERVICE_NAME'.

    For more information see http://tiny.cloudera.com/tsquery_doc

    Retrieve metric readings for the service.
    @param from_time: A datetime; start of the period to query (optional).
    @param to_time: A datetime; end of the period to query (default = now).
    @param metrics: List of metrics to query (default = all).
    @param view: View to materialize ('full' or 'summary')
    @return: List of metrics and their readings.
    """
    return self._get_resource_root().get_metrics(self._path() + '/metrics',
        from_time, to_time, metrics, view)

  def start(self):
    """
    Start a service.

    @return: Reference to the submitted command.
    """
    return self._cmd('start')

  def stop(self):
    """
    Stop a service.

    @return: Reference to the submitted command.
    """
    return self._cmd('stop')

  def restart(self):
    """
    Restart a service.

    @return: Reference to the submitted command.
    """
    return self._cmd('restart')

  def start_roles(self, *role_names):
    """
    Start a list of roles.

    @param role_names: names of the roles to start.
    @return: List of submitted commands.
    """
    return self._role_cmd('start', role_names)

  def stop_roles(self, *role_names):
    """
    Stop a list of roles.

    @param role_names: names of the roles to stop.
    @return: List of submitted commands.
    """
    return self._role_cmd('stop', role_names)

  def restart_roles(self, *role_names):
    """
    Restart a list of roles.

    @param role_names: names of the roles to restart.
    @return: List of submitted commands.
    """
    return self._role_cmd('restart', role_names)

  def bootstrap_hdfs_stand_by(self, *role_names):
    """
    Bootstrap HDFS stand-by NameNodes.

    Initialize their state by syncing it with the respective HA partner.

    @param role_names: NameNodes to bootstrap.
    @return: List of submitted commands.
    """
    return self._role_cmd('hdfsBootstrapStandBy', role_names)

  def finalize_metadata_upgrade(self, *role_names):
    """
    Finalize HDFS NameNode metadata upgrade. Should be done after doing
    HDFS upgrade with full downtime (and not with rolling upgrade).

    @param role_names: NameNodes for which to finalize the upgrade.
    @return: List of submitted commands.
    @since: API v3
    """
    return self._role_cmd('hdfsFinalizeMetadataUpgrade', role_names, api_version=3)

  def create_beeswax_warehouse(self):
    """
    DEPRECATED: use create_hive_warehouse on the Hive service. Deprecated since v3.

    Create the Beeswax role's warehouse for a Hue service.

    @return: Reference to the submitted command.
    """
    return self._cmd('hueCreateHiveWarehouse')

  def create_hbase_root(self):
    """
    Create the root directory of an HBase service.

    @return: Reference to the submitted command.
    """
    return self._cmd('hbaseCreateRoot')

  def create_hdfs_tmp(self):
    """
    Create the /tmp directory in HDFS with appropriate ownership and permissions.

    @return: Reference to the submitted command
    @since: API v2
    """
    return self._cmd('hdfsCreateTmpDir')

  def refresh(self, *role_names):
    """
    Execute the "refresh" command on a set of roles.

    @param role_names: Names of the roles to refresh.
    @return: Reference to the submitted command.
    """
    return self._role_cmd('refresh', role_names)

  def decommission(self, *role_names):
    """
    Decommission roles in a service.

    @param role_names: Names of the roles to decommission.
    @return: Reference to the submitted command.
    """
    return self._cmd('decommission', data=role_names)

  def recommission(self, *role_names):
    """
    Recommission roles in a service.

    @param role_names: Names of the roles to recommission.
    @return: Reference to the submitted command.
    @since: API v2
    """
    return self._cmd('recommission', data=role_names)

  def deploy_client_config(self, *role_names):
    """
    Deploys client configuration to the hosts where roles are running.

    @param role_names: Names of the roles to decommission.
    @return: Reference to the submitted command.
    """
    return self._cmd('deployClientConfig', data=role_names)

  def disable_hdfs_auto_failover(self, nameservice):
    """
    Disable auto-failover for a highly available HDFS nameservice.
    This command is no longer supported with API v6 onwards. Use disable_nn_ha instead.

    @param nameservice: Affected nameservice.
    @return: Reference to the submitted command.
    """
    return self._cmd('hdfsDisableAutoFailover', data=nameservice)

  def disable_hdfs_ha(self, active_name, secondary_name,
      start_dependent_services=True, deploy_client_configs=True,
                      disable_quorum_storage=False):
    """
    Disable high availability for an HDFS NameNode.
    This command is no longer supported with API v6 onwards. Use disable_nn_ha instead.

    @param active_name: Name of the NameNode to keep.
    @param secondary_name: Name of (existing) SecondaryNameNode to link to
                           remaining NameNode.
    @param start_dependent_services: whether to re-start dependent services.
    @param deploy_client_configs: whether to re-deploy client configurations.
    @param disable_quorum_storage: whether to disable Quorum-based Storage. Available since API v2.
                                   Quorum-based Storage will be disabled for all
                                   nameservices that have Quorum-based Storage
                                   enabled.
    @return: Reference to the submitted command.
    """
    args = dict(
      activeName = active_name,
      secondaryName = secondary_name,
      startDependentServices = start_dependent_services,
      deployClientConfigs = deploy_client_configs,
    )

    version = self._get_resource_root().version
    if version < 2:
      if disable_quorum_storage:
        raise AttributeError("Quorum-based Storage requires at least API version 2 available in Cloudera Manager 4.1.")
    else:
      args['disableQuorumStorage'] = disable_quorum_storage

    return self._cmd('hdfsDisableHa', data=args)

  def enable_hdfs_auto_failover(self, nameservice, active_fc_name,
      standby_fc_name, zk_service):
    """
    Enable auto-failover for an HDFS nameservice.
    This command is no longer supported with API v6 onwards. Use enable_nn_ha instead.

    @param nameservice: Nameservice for which to enable auto-failover.
    @param active_fc_name: Name of failover controller to create for active node.
    @param standby_fc_name: Name of failover controller to create for stand-by node.
    @param zk_service: ZooKeeper service to use.
    @return: Reference to the submitted command.
    """
    version = self._get_resource_root().version

    args = dict(
      nameservice = nameservice,
      activeFCName = active_fc_name,
      standByFCName = standby_fc_name,
      zooKeeperService = dict(
        clusterName = zk_service.clusterRef.clusterName,
        serviceName = zk_service.name,
        ),
      )
    return self._cmd('hdfsEnableAutoFailover', data=args)

  def enable_hdfs_ha(self, active_name, active_shared_path, standby_name,
      standby_shared_path, nameservice, start_dependent_services=True,
      deploy_client_configs=True, enable_quorum_storage=False):
    """
    Enable high availability for an HDFS NameNode.
    This command is no longer supported with API v6 onwards. Use enable_nn_ha instead.

    @param active_name: name of active NameNode.
    @param active_shared_path: shared edits path for active NameNode.
                               Ignored if Quorum-based Storage is being enabled.
    @param standby_name: name of stand-by NameNode.
    @param standby_shared_path: shared edits path for stand-by NameNode.
                                Ignored if Quourm Journal is being enabled.
    @param nameservice: nameservice for the HA pair.
    @param start_dependent_services: whether to re-start dependent services.
    @param deploy_client_configs: whether to re-deploy client configurations.
    @param enable_quorum_storage: whether to enable Quorum-based Storage. Available since API v2.
                                  Quorum-based Storage will be enabled for all
                                  nameservices except those configured with NFS High
                                  Availability.
    @return: Reference to the submitted command.
    """
    version = self._get_resource_root().version

    args = dict(
      activeName = active_name,
      standByName = standby_name,
      nameservice = nameservice,
      startDependentServices = start_dependent_services,
      deployClientConfigs = deploy_client_configs,
    )

    if enable_quorum_storage:
      if version < 2:
        raise AttributeError("Quorum-based Storage is not supported prior to Cloudera Manager 4.1.")
      else:
        args['enableQuorumStorage'] = enable_quorum_storage
    else:
      if active_shared_path is None or standby_shared_path is None:
        raise AttributeError("Active and standby shared paths must be specified if not enabling Quorum-based Storage")
      args['activeSharedEditsPath'] = active_shared_path
      args['standBySharedEditsPath'] = standby_shared_path

    return self._cmd('hdfsEnableHa', data=args)

  def enable_nn_ha(self, active_name, standby_host_id, nameservice, jns,
      standby_name_dir_list=None, qj_name=None, standby_name=None,
      active_fc_name=None, standby_fc_name=None, zk_service_name=None,
      force_init_znode=True, clear_existing_standby_name_dirs=True, clear_existing_jn_edits_dir=True):
    """
    Enable High Availability (HA) with Auto-Failover for an HDFS NameNode.
    @param active_name: Name of Active NameNode.
    @param standby_host_id: ID of host where Standby NameNode will be created.
    @param nameservice: Nameservice to be used while enabling HA.
                        Optional if Active NameNode already has this config set.
    @param jns: List of Journal Nodes to be created during the command.
                Each element of the list must be a dict containing the following keys:
                  - B{jnHostId}: ID of the host where the new JournalNode will be created.
                  - B{jnName}: Name of the JournalNode role (optional)
                  - B{jnEditsDir}: Edits dir of the JournalNode. Can be omitted if the config
                    is already set at RCG level.
    @param standby_name_dir_list: List of directories for the new Standby NameNode.
                                  If not provided then it will use same dirs as Active NameNode.
    @param qj_name: Name of the journal located on each JournalNodes' filesystem.
                    This can be optionally provided if the config hasn't been already set for the Active NameNode.
                    If this isn't provided and Active NameNode doesn't also have the config,
                    then nameservice is used by default.
    @param standby_name: Name of the Standby NameNode role to be created (Optional).
    @param active_fc_name: Name of the Active Failover Controller role to be created (Optional).
    @param standby_fc_name: Name of the Standby Failover Controller role to be created (Optional).
    @param zk_service_name: Name of the ZooKeeper service to use for auto-failover.
                            If HDFS service already depends on a ZooKeeper service then that ZooKeeper
                            service will be used for auto-failover and in that case this parameter
                            can either be omitted or should be the same ZooKeeper service.
    @param force_init_znode: Indicates if the ZNode should be force initialized if it is
                             already present. Useful while re-enabling High Availability. (Default: TRUE)
    @param clear_existing_standby_name_dirs: Indicates if the existing name directories for Standby NameNode
                                             should be cleared during the workflow.
                                             Useful while re-enabling High Availability. (Default: TRUE)
    @param clear_existing_jn_edits_dir: Indicates if the existing edits directories for the JournalNodes
                                        for the specified nameservice should be cleared during the workflow.
                                        Useful while re-enabling High Availability. (Default: TRUE)
    @return: Reference to the submitted command.
    @since: API v6
    """
    args = dict (
      activeNnName = active_name,
      standbyNnName = standby_name,
      standbyNnHostId = standby_host_id,
      standbyNameDirList = standby_name_dir_list,
      nameservice = nameservice,
      qjName = qj_name,
      activeFcName = active_fc_name,
      standbyFcName = standby_fc_name,
      zkServiceName = zk_service_name,
      forceInitZNode = force_init_znode,
      clearExistingStandbyNameDirs = clear_existing_standby_name_dirs,
      clearExistingJnEditsDir = clear_existing_jn_edits_dir,
      jns = jns
    )
    return self._cmd('hdfsEnableNnHa', data=args, api_version=6)

  def disable_nn_ha(self, active_name, snn_host_id, snn_check_point_dir_list,
      snn_name=None):
    """
    Disable high availability with automatic failover for an HDFS NameNode.

    @param active_name: Name of the NamdeNode role that is going to be active after
                        High Availability is disabled.
    @param snn_host_id: Id of the host where the new SecondaryNameNode will be created.
    @param snn_check_point_dir_list : List of directories used for checkpointing
                                      by the new SecondaryNameNode.
    @param snn_name: Name of the new SecondaryNameNode role (Optional).
    @return: Reference to the submitted command.
    @since: API v6
    """
    args = dict(
      activeNnName = active_name,
      snnHostId = snn_host_id,
      snnCheckpointDirList = snn_check_point_dir_list,
      snnName = snn_name
    )
    return self._cmd('hdfsDisableNnHa', data=args, api_version=6)

  def enable_jt_ha(self, new_jt_host_id, force_init_znode=True, zk_service_name=None,
      new_jt_name=None, fc1_name=None, fc2_name=None):
    """
    Enable high availability for a MR JobTracker.

    @param zk_service_name: Name of the ZooKeeper service to use for auto-failover.
           If MapReduce service depends on a ZooKeeper service then that ZooKeeper
           service will be used for auto-failover and in that case this parameter
           can be omitted.
    @param new_jt_host_id: id of the host where the second JobTracker
                        will be added.
    @param force_init_znode: Initialize the ZNode used for auto-failover even if
                             it already exists. This can happen if JobTracker HA
                             was enabled before and then disabled. Disable operation
                             doesn't delete this ZNode. Defaults to true.
    @param new_jt_name: Name of the second JobTracker role to be created.
    @param fc1_name: Name of the Failover Controller role that is co-located with
                     the existing JobTracker.
    @param fc2_name: Name of the Failover Controller role that is co-located with
                     the new JobTracker.
    @return: Reference to the submitted command.
    @since: API v5
    """
    args = dict(
      newJtHostId = new_jt_host_id,
      forceInitZNode = force_init_znode,
      zkServiceName = zk_service_name,
      newJtRoleName = new_jt_name,
      fc1RoleName = fc1_name,
      fc2RoleName = fc2_name
    )
    return self._cmd('enableJtHa', data=args)

  def disable_jt_ha(self, active_name):
    """
    Disable high availability for a MR JobTracker active-standby pair.

    @param active_name: name of the JobTracker that will be active after
                        the disable operation. The other JobTracker and
                        Failover Controllers will be removed.
    @return: Reference to the submitted command.
    """
    args = dict(
      activeName = active_name,
    )
    return self._cmd('disableJtHa', data=args)

  def enable_rm_ha(self, new_rm_host_id, zk_service_name=None):
    """
    Enable high availability for a YARN ResourceManager.

    @param new_rm_host_id: id of the host where the second ResourceManager
                           will be added.
    @param zk_service_name: Name of the ZooKeeper service to use for auto-failover.
           If YARN service depends on a ZooKeeper service then that ZooKeeper
           service will be used for auto-failover and in that case this parameter
           can be omitted.
    @return: Reference to the submitted command.
    @since: API v6
    """
    args = dict(
      newRmHostId = new_rm_host_id,
      zkServiceName = zk_service_name
    )
    return self._cmd('enableRmHa', data=args)

  def disable_rm_ha(self, active_name):
    """
    Disable high availability for a YARN ResourceManager active-standby pair.

    @param active_name: name of the ResourceManager that will be active after
                        the disable operation. The other ResourceManager
                        will be removed.
    @return: Reference to the submitted command.
    @since: API v6
    """
    args = dict(
      activeName = active_name
    )
    return self._cmd('disableRmHa', data=args)

  def enable_oozie_ha(self, new_oozie_server_host_ids, new_oozie_server_role_names=None,
    zk_service_name=None, load_balancer_host_port=None):
    """
    Enable high availability for Oozie.

    @param new_oozie_server_host_ids: List of IDs of the hosts on which new Oozie Servers
                                      will be added.
    @param new_oozie_server_role_names: List of names of the new Oozie Servers. This is an
                                        optional argument, but if provided, it should
                                        match the length of host IDs provided.
    @param zk_service_name: Name of the ZooKeeper service that will be used for Oozie HA.
                            This is an optional parameter if the Oozie to ZooKeeper
                            dependency is already set.
    @param load_balancer_host_port: Address and port of the load balancer used for Oozie HA.
                                    This is an optional parameter if this config is already set.
    @return: Reference to the submitted command.
    @since: API v6
    """
    args = dict(
      newOozieServerHostIds = new_oozie_server_host_ids,
      newOozieServerRoleNames = new_oozie_server_role_names,
      zkServiceName = zk_service_name,
      loadBalancerHostPort = load_balancer_host_port
    )
    return self._cmd('oozieEnableHa', data=args, api_version=6)

  def disable_oozie_ha(self, active_name):
    """
    Disable high availability for Oozie

    @param active_name: Name of the Oozie Server that will be active after
                        High Availability is disabled.
    @return: Reference to the submitted command.
    @since: API v6
    """
    args = dict(
      activeName = active_name
    )
    return self._cmd('oozieDisableHa', data=args, api_version=6)

  def failover_hdfs(self, active_name, standby_name, force=False):
    """
    Initiate a failover of an HDFS NameNode HA pair.

    This will make the given stand-by NameNode active, and vice-versa.

    @param active_name: name of currently active NameNode.
    @param standby_name: name of NameNode currently in stand-by.
    @param force: whether to force failover.
    @return: Reference to the submitted command.
    """
    params = { "force" : "true" and force or "false" }
    args = { ApiList.LIST_KEY : [ active_name, standby_name ] }
    return self._cmd('hdfsFailover', data=[ active_name, standby_name ],
        params = { "force" : "true" and force or "false" })

  def format_hdfs(self, *namenodes):
    """
    Format NameNode instances of an HDFS service.

    @param namenodes: Name of NameNode instances to format.
    @return: List of submitted commands.
    """
    return self._role_cmd('hdfsFormat', namenodes)

  def init_hdfs_auto_failover(self, *controllers):
    """
    Initialize HDFS failover controller metadata.

    Only one controller per nameservice needs to be initialized.

    @param controllers: Name of failover controller instances to initialize.
    @return: List of submitted commands.
    """
    return self._role_cmd('hdfsInitializeAutoFailover', controllers)

  def init_hdfs_shared_dir(self, *namenodes):
    """
    Initialize a NameNode's shared edits directory.

    @param namenodes: Name of NameNode instances.
    @return: List of submitted commands.
    """
    return self._role_cmd('hdfsInitializeSharedDir', namenodes)

  def roll_edits_hdfs(self, nameservice=None):
    """
    Roll the edits of an HDFS NameNode or Nameservice.

    @param nameservice: Nameservice whose edits should be rolled.
                        Required only with a federated HDFS.
    @return: Reference to the submitted command.
    @since: API v3
    """
    args = dict()
    if nameservice:
      args['nameservice'] = nameservice

    return self._cmd('hdfsRollEdits', data=args)

  def upgrade_hdfs_metadata(self):
    """
    Upgrade HDFS Metadata as part of a major version upgrade.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('hdfsUpgradeMetadata', api_version=6)

  def upgrade_hbase(self):
    """
    Upgrade HBase data in HDFS and ZooKeeper as part of upgrade from CDH4 to CDH5.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('hbaseUpgrade', api_version=6)

  def create_sqoop_user_dir(self):
    """
    Creates the user directory of a Sqoop service in HDFS.

    @return: Reference to the submitted command.
    @since: API v4
    """
    return self._cmd('createSqoopUserDir', api_version=4)

  def create_sqoop_database_tables(self):
    """
    Creates the Sqoop2 Server database tables in the configured database.
    Will do nothing if tables already exist. Will not perform an upgrade.

    @return: Reference to the submitted command.
    @since: API v10
    """
    return self._cmd('sqoopCreateDatabaseTables', api_version=10)

  def upgrade_sqoop_db(self):
    """
    Upgrade Sqoop Database schema as part of a major version upgrade.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('sqoopUpgradeDb', api_version=6)

  def upgrade_hive_metastore(self):
    """
    Upgrade Hive Metastore as part of a major version upgrade.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('hiveUpgradeMetastore', api_version=6)

  def cleanup_zookeeper(self, *servers):
    """
    Cleanup a ZooKeeper service or roles.

    If no server role names are provided, the command applies to the whole
    service, and cleans up all the server roles that are currently running.

    @param servers: ZK server role names (optional).
    @return: Command reference (for service command) or list of command
             references (for role commands).
    """
    if servers:
      return self._role_cmd('zooKeeperCleanup', servers)
    else:
      return self._cmd('zooKeeperCleanup')

  def init_zookeeper(self, *servers):
    """
    Initialize a ZooKeeper service or roles.

    If no server role names are provided, the command applies to the whole
    service, and initializes all the configured server roles.

    @param servers: ZK server role names (optional).
    @return: Command reference (for service command) or list of command
             references (for role commands).
    """
    if servers:
      return self._role_cmd('zooKeeperInit', servers)
    else:
      return self._cmd('zooKeeperInit')

  def sync_hue_db(self, *servers):
    """
    Synchronize the Hue server's database.

    @param servers: Name of Hue Server roles to synchronize. Not required starting with API v10.
    @return: List of submitted commands.
    """

    actual_version = self._get_resource_root().version
    if actual_version < 10:
      return self._role_cmd('hueSyncDb', servers)

    return self._cmd('hueSyncDb', api_version=10)


  def dump_hue_db(self):
    """
    Dump the Hue server's database; it can be loaded later.

    @return: List of submitted commands.
    """
    return self._cmd('hueDumpDb', api_version=10)

  def load_hue_db(self):
    """
    Load data into Hue server's database from a previous data dump.

    @return: List of submitted commands.
    """
    return self._cmd('hueLoadDb', api_version=10)

  def lsof(self, *rolenames):
    """
    Run the lsof diagnostic command. This command runs the lsof utility to list
    a role's open files.

    @param rolenames: Name of the role instances
    @return: List of submitted commands.
    @since: API v8
    """
    return self._role_cmd('lsof', rolenames)

  def jstack(self, *rolenames):
    """
    Run the jstack diagnostic command. The command runs the jstack utility to
    capture a role's java thread stacks.

    @param rolenames: Name of the role instances
    @return: List of submitted commands.
    @since: API v8
    """
    return self._role_cmd('jstack', rolenames)

  def jmap_histo(self, *rolenames):
    """
    Run the jmapHisto diagnostic command. The command runs the jmap utility to
    capture a histogram of the objects on the role's java heap.

    @param rolenames: Name of the role instances
    @return: List of submitted commands.
    @since: API v8
    """
    return self._role_cmd('jmapHisto', rolenames)

  def jmap_dump(self, *rolenames):
    """
    Run the jmapDump diagnostic command. The command runs the jmap utility to
    capture a dump of the role's java heap.

    @param rolenames: Name of the role instances
    @return: List of submitted commands.
    @since: API v8
    """
    return self._role_cmd('jmapDump', rolenames)

  def enter_maintenance_mode(self):
    """
    Put the service in maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('enterMaintenanceMode')
    if cmd.success:
      self._update(_get_service(self._get_resource_root(), self._path()))
    return cmd

  def exit_maintenance_mode(self):
    """
    Take the service out of maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('exitMaintenanceMode')
    if cmd.success:
      self._update(_get_service(self._get_resource_root(), self._path()))
    return cmd

  def rolling_restart(self, slave_batch_size=None,
                      slave_fail_count_threshold=None,
                      sleep_seconds=None,
                      stale_configs_only=None,
                      unupgraded_only=None,
                      restart_role_types=None,
                      restart_role_names=None):
    """
    Rolling restart the roles of a service. The sequence is:
      1. Restart all the non-slave roles
      2. If slaves are present restart them in batches of size specified
      3. Perform any post-command needed after rolling restart

    @param slave_batch_size: Number of slave roles to restart at a time
           Must be greater than 0. Default is 1.
           For HDFS, this number should be less than the replication factor (default 3)
           to ensure data availability during rolling restart.
    @param slave_fail_count_threshold: The threshold for number of slave batches that
           are allowed to fail to restart before the entire command is considered failed.
           Must be >= 0. Default is 0.
    @param sleep_seconds: Number of seconds to sleep between restarts of slave role batches.
            Must be >=0. Default is 0.
    @param stale_configs_only: Restart roles with stale configs only. Default is false.
    @param unupgraded_only: Restart roles that haven't been upgraded yet. Default is false.
    @param restart_role_types: Role types to restart. If not specified, all startable roles are restarted.
    @param restart_role_names: List of specific roles to restart.
            If none are specified, then all roles of specified role types are restarted.
    @return: Reference to the submitted command.
    @since: API v3
    """
    args = dict()
    if slave_batch_size:
      args['slaveBatchSize'] = slave_batch_size
    if slave_fail_count_threshold:
      args['slaveFailCountThreshold'] = slave_fail_count_threshold
    if sleep_seconds:
      args['sleepSeconds'] = sleep_seconds
    if stale_configs_only:
      args['staleConfigsOnly'] = stale_configs_only
    if unupgraded_only:
      args['unUpgradedOnly'] = unupgraded_only
    if restart_role_types:
      args['restartRoleTypes'] = restart_role_types
    if restart_role_names:
      args['restartRoleNames'] = restart_role_names

    return self._cmd('rollingRestart', data=args)

  def create_replication_schedule(self,
      start_time, end_time, interval_unit, interval, paused, arguments,
      alert_on_start=False, alert_on_success=False, alert_on_fail=False,
      alert_on_abort=False):
    """
    Create a new replication schedule for this service.

    The replication argument type varies per service type. The following types
    are recognized:
      - HDFS: ApiHdfsReplicationArguments
      - Hive: ApiHiveReplicationArguments

    @type  start_time: datetime.datetime
    @param start_time: The time at which the schedule becomes active and first executes.
    @type  end_time: datetime.datetime
    @param end_time: The time at which the schedule will expire.
    @type  interval_unit: str
    @param interval_unit: The unit of time the `interval` represents. Ex. MINUTE, HOUR,
                          DAY. See the server documentation for a full list of values.
    @type  interval: int
    @param interval: The number of time units to wait until triggering the next replication.
    @type  paused: bool
    @param paused: Should the schedule be paused? Useful for on-demand replication.
    @param arguments: service type-specific arguments for the replication job.
    @param alert_on_start: whether to generate alerts when the job is started.
    @param alert_on_success: whether to generate alerts when the job succeeds.
    @param alert_on_fail: whether to generate alerts when the job fails.
    @param alert_on_abort: whether to generate alerts when the job is aborted.
    @return: The newly created schedule.
    @since: API v3
    """
    schedule = ApiReplicationSchedule(self._get_resource_root(),
      startTime=start_time, endTime=end_time, intervalUnit=interval_unit, interval=interval,
      paused=paused, alertOnStart=alert_on_start, alertOnSuccess=alert_on_success,
      alertOnFail=alert_on_fail, alertOnAbort=alert_on_abort)

    if self.type == 'HDFS':
      if isinstance(arguments, ApiHdfsCloudReplicationArguments):
        schedule.hdfsCloudArguments = arguments
      elif isinstance(arguments, ApiHdfsReplicationArguments):
        schedule.hdfsArguments = arguments
      else:
        raise TypeError, 'Unexpected type for HDFS replication argument.'
    elif self.type == 'HIVE':
      if not isinstance(arguments, ApiHiveReplicationArguments):
        raise TypeError, 'Unexpected type for Hive replication argument.'
      schedule.hiveArguments = arguments
    else:
      raise TypeError, 'Replication is not supported for service type ' + self.type

    return self._post("replications", ApiReplicationSchedule, True, [schedule],
        api_version=3)[0]

  def get_replication_schedules(self):
    """
    Retrieve a list of replication schedules.

    @return: A list of replication schedules.
    @since: API v3
    """
    return self._get("replications", ApiReplicationSchedule, True,
        api_version=3)

  def get_replication_schedule(self, schedule_id):
    """
    Retrieve a single replication schedule.

    @param schedule_id: The id of the schedule to retrieve.
    @return: The requested schedule.
    @since: API v3
    """
    return self._get("replications/%d" % schedule_id, ApiReplicationSchedule,
        api_version=3)

  def delete_replication_schedule(self, schedule_id):
    """
    Delete a replication schedule.

    @param schedule_id: The id of the schedule to delete.
    @return: The deleted replication schedule.
    @since: API v3
    """
    return self._delete("replications/%s" % schedule_id, ApiReplicationSchedule,
        api_version=3)

  def update_replication_schedule(self, schedule_id, schedule):
    """
    Update a replication schedule.

    @param schedule_id: The id of the schedule to update.
    @param schedule: The modified schedule.
    @return: The updated replication schedule.
    @since: API v3
    """
    return self._put("replications/%s" % schedule_id, ApiReplicationSchedule,
        data=schedule, api_version=3)

  def get_replication_command_history(self, schedule_id, limit=20, offset=0,
                                      view=None):
    """
    Retrieve a list of commands for a replication schedule.

    @param schedule_id: The id of the replication schedule.
    @param limit: Maximum number of commands to retrieve.
    @param offset: Index of first command to retrieve.
    @param view: View to materialize. Valid values are 'full', 'summary', 'export', 'export_redacted'.
    @return: List of commands executed for a replication schedule.
    @since: API v4
    """
    params = {
      'limit':  limit,
      'offset': offset,
    }
    if view:
      params['view'] = view

    return self._get("replications/%s/history" % schedule_id,
                     ApiReplicationCommand, True, params=params, api_version=4)

  def trigger_replication_schedule(self, schedule_id, dry_run=False):
    """
    Trigger replication immediately. Start and end dates on the schedule will be
    ignored.

    @param schedule_id: The id of the schedule to trigger.
    @param dry_run: Whether to execute a dry run.
    @return: The command corresponding to the replication job.
    @since: API v3
    """
    return self._post("replications/%s/run" % schedule_id, ApiCommand,
        params=dict(dryRun=dry_run),
        api_version=3)

  def create_snapshot_policy(self, policy):
    """
    Create a new snapshot policy for this service.
    @param policy: The snapshot policy to create
    @return: The newly created policy.
    @since: API v6
    """
    return self._post("snapshots/policies", ApiSnapshotPolicy, True, [policy],
        api_version=6)[0]

  def get_snapshot_policies(self, view=None):
    """
    Retrieve a list of snapshot policies.

    @param view: View to materialize. Valid values are 'full', 'summary', 'export', 'export_redacted'.
    @return: A list of snapshot policies.
    @since: API v6
    """
    return self._get("snapshots/policies", ApiSnapshotPolicy, True,
        params=view and dict(view=view) or None, api_version=6)

  def get_snapshot_policy(self, name, view=None):
    """
    Retrieve a single snapshot policy.

    @param name: The name of the snapshot policy to retrieve.
    @param view: View to materialize. Valid values are 'full', 'summary', 'export', 'export_redacted'.
    @return: The requested snapshot policy.
    @since: API v6
    """
    return self._get("snapshots/policies/%s" % name, ApiSnapshotPolicy,
        params=view and dict(view=view) or None, api_version=6)

  def delete_snapshot_policy(self, name):
    """
    Delete a snapshot policy.

    @param name: The name of the snapshot policy to delete.
    @return: The deleted snapshot policy.
    @since: API v6
    """
    return self._delete("snapshots/policies/%s" % name, ApiSnapshotPolicy, api_version=6)

  def update_snapshot_policy(self, name, policy):
    """
    Update a snapshot policy.

    @param name: The name of the snapshot policy to update.
    @param policy: The modified snapshot policy.
    @return: The updated snapshot policy.
    @since: API v6
    """
    return self._put("snapshots/policies/%s" % name, ApiSnapshotPolicy, data=policy,
        api_version=6)

  def get_snapshot_command_history(self, name, limit=20, offset=0, view=None):
    """
    Retrieve a list of commands triggered by a snapshot policy.

    @param name: The name of the snapshot policy.
    @param limit: Maximum number of commands to retrieve.
    @param offset: Index of first command to retrieve.
    @param view: View to materialize. Valid values are 'full', 'summary', 'export', 'export_redacted'.
    @return: List of commands triggered by a snapshot policy.
    @since: API v6
    """
    params = {
      'limit':  limit,
      'offset': offset,
    }
    if view:
      params['view'] = view

    return self._get("snapshots/policies/%s/history" % name, ApiSnapshotCommand, True,
        params=params, api_version=6)


  def install_oozie_sharelib(self):
    """
    Installs the Oozie ShareLib. Oozie must be stopped before running this
    command.

    @return: Reference to the submitted command.
    @since: API v3
    """
    return self._cmd('installOozieShareLib', api_version=3)

  def create_oozie_embedded_database(self):
    """
    Create the Oozie Server Database. Only works with embedded postgresql
    database. This command should usually be followed by a call to
    create_oozie_db.

    @return: Reference to the submitted command.
    @since: API v10
    """
    return self._cmd('oozieCreateEmbeddedDatabase', api_version=10)

  def create_oozie_db(self):
    """
    Creates the Oozie Database Schema in the configured database.
    This command does not create database. This command creates only tables
    required by Oozie. To create database, please refer to create_oozie_embedded_database.

    @return: Reference to the submitted command.
    @since: API v2
    """
    return self._cmd('createOozieDb', api_version=2)

  def upgrade_oozie_db(self):
    """
    Upgrade Oozie Database schema as part of a major version upgrade.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('oozieUpgradeDb', api_version=6)

  def init_solr(self):
    """
    Initializes the Solr service in Zookeeper.

    @return: Reference to the submitted command.
    @since: API v4
    """
    return self._cmd('initSolr', api_version=4)

  def create_solr_hdfs_home_dir(self):
    """
    Creates the home directory of a Solr service in HDFS.

    @return: Reference to the submitted command.
    @since: API v4
    """
    return self._cmd('createSolrHdfsHomeDir', api_version=4)

  def create_hive_metastore_tables(self):
    """
    Creates the Hive metastore tables in the configured database.
    Will do nothing if tables already exist. Will not perform an upgrade.

    @return: Reference to the submitted command.
    @since: API v3
    """
    return self._cmd('hiveCreateMetastoreDatabaseTables', api_version=3)

  def create_hive_warehouse(self):
    """
    Creates the Hive warehouse directory in HDFS.

    @return: Reference to the submitted command.
    @since: API v3
    """
    return self._cmd('hiveCreateHiveWarehouse')

  def create_hive_userdir(self):
    """
    Creates the Hive user directory in HDFS.

    @return: Reference to the submitted command.
    @since: API v4
    """
    return self._cmd('hiveCreateHiveUserDir')

  def create_hive_metastore_database(self):
    """
    Create the Hive Metastore Database. Only works with embedded postgresql
    database. This command should usually be followed by a call to
    create_hive_metastore_tables.

    @return: Reference to the submitted command.
    @since: API v4
    """
    return self._cmd('hiveCreateMetastoreDatabase', api_version=4)

  def create_sentry_database(self):
    """
    Create the Sentry Server Database. Only works with embedded postgresql
    database. This command should usually be followed by a call to
    create_sentry_database_tables.

    @return: Reference to the submitted command.
    @since: API v7
    """
    return self._cmd('sentryCreateDatabase', api_version=7)

  def create_sentry_database_tables(self):
    """
    Creates the Sentry Server database tables in the configured database.
    Will do nothing if tables already exist. Will not perform an upgrade.

    @return: Reference to the submitted command.
    @since: API v7
    """
    return self._cmd('sentryCreateDatabaseTables', api_version=7)

  def upgrade_sentry_database_tables(self):
    """
    Upgrades the Sentry Server database tables in the configured database.

    @return: Reference to the submitted command.
    @since: API v8
    """
    return self._cmd('sentryUpgradeDatabaseTables', api_version=8)

  def update_metastore_namenodes(self):
    """
    Update Hive Metastore to point to a NameNode's Nameservice name instead of
    hostname. Only available when all Hive Metastore Servers are stopped and
    HDFS has High Availability.

    Back up the Hive Metastore Database before running this command.

    @return: Reference to the submitted command.
    @since: API v4
    """
    return self._cmd('hiveUpdateMetastoreNamenodes', api_version=4)

  def import_mr_configs_into_yarn(self):
    """
    Import MapReduce configuration into Yarn, overwriting Yarn configuration.

    You will lose existing Yarn configuration. Read all MapReduce
    configuration, role assignments, and role configuration groups and update
    Yarn with corresponding values. MR1 configuration will be converted into
    the equivalent MR2 configuration.

    Before running this command, Yarn must be stopped and MapReduce must exist
    with valid configuration.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('importMrConfigsIntoYarn', api_version=6)

  def switch_to_mr2(self):
    """
    Change the cluster to use MR2 instead of MR1. Services will be restarted.

    Will perform the following steps:
    * Update all services that depend on MapReduce to instead depend on Yarn.
    * Stop MapReduce
    * Start Yarn (includes MR2)
    * Deploy Yarn (MR2) Client Configuration

    Available since API v6.

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('switchToMr2', api_version=6)

  def finalize_rolling_upgrade(self):
    """
    Finalizes the rolling upgrade for HDFS by updating the NameNode
    metadata permanently to the next version. Should be done after
    doing a rolling upgrade to a CDH version >= 5.2.0.

    @return: Reference to the submitted command.
    @since: API v8
    """
    return self._cmd('hdfsFinalizeRollingUpgrade', api_version=8)

  def role_command_by_name(self, command_name, *role_names):
    """
    Executes a role command by name on the specified
    roles

    @param command_name: The name of the command.
    @param role_names: The role names to execute this command on.
    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._role_cmd(command_name, role_names, api_version=6)

  def service_command_by_name(self, command_name):
    """
    Executes a command on the service specified
    by name.

    @param command_name: The name of the command.
    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd(command_name, api_version=6)

  def list_commands_by_name(self):
    """
    Lists all the commands that can be executed by name
    on the provided service.

    @return: A list of command metadata objects
    @since: API v6
    """
    return self._get("commandsByName", ApiCommandMetadata, True,
        api_version=6)

  def create_yarn_cm_container_usage_input_dir(self):
    """
    Creates the HDFS directory where YARN container usage metrics are
    stored by NodeManagers for CM to read and aggregate into app usage metrics.

    @return: Reference to submitted command.
    @since: API v13
    """
    return self._cmd('yarnCreateCmContainerUsageInputDirCommand', api_version=13)

class ApiServiceSetupInfo(ApiService):
  _ATTRIBUTES = {
    'name'    : None,
    'type'    : None,
    'config'  : Attr(ApiConfig),
    'roles'   : Attr(roles.ApiRole),
  }

  def __init__(self, name=None, type=None,
               config=None, roles=None):
    # The BaseApiObject expects a resource_root, which we don't care about
    resource_root = None
    # Unfortunately, the json key is called "type". So our input arg
    # needs to be called "type" as well, despite it being a python keyword.
    BaseApiObject.init(self, None, locals())

  def set_config(self, config):
    """
    Set the service configuration.

    @param config: A dictionary of config key/value
    """
    if self.config is None:
      self.config = { }
    self.config.update(config_to_api_list(config))

  def add_role_type_info(self, role_type, config):
    """
    Add a role type setup info.

    @param role_type: Role type
    @param config: A dictionary of role type configuration
    """
    rt_config = config_to_api_list(config)
    rt_config['roleType'] = role_type

    if self.config is None:
      self.config = { }
    if not self.config.has_key(ROLETYPES_CFG_KEY):
      self.config[ROLETYPES_CFG_KEY] = [ ]
    self.config[ROLETYPES_CFG_KEY].append(rt_config)

  def add_role_info(self, role_name, role_type, host_id, config=None):
    """
    Add a role info. The role will be created along with the service setup.

    @param role_name: Role name
    @param role_type: Role type
    @param host_id: The host where the role should run
    @param config: (Optional) A dictionary of role config values
    """
    if self.roles is None:
      self.roles = [ ]
    api_config_list = config is not None and config_to_api_list(config) or None
    self.roles.append({
        'name' : role_name,
        'type' : role_type,
        'hostRef' : { 'hostId' : host_id },
        'config' : api_config_list })

  def first_run(self):
    """
    Prepare and start this service.
    Perform all the steps needed to prepare and start this service.

    @return: Reference to the submitted command.
    @since: API v7
    """
    return self._cmd('firstRun', None, api_version=7)
