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
import logging

from cm_api.endpoints.types import config_to_json, json_to_config, \
    config_to_api_list, ApiCommand, ApiHostRef, ApiList, BaseApiObject, \
    ApiActivity
from cm_api.endpoints import roles

__docformat__ = "epytext"

SERVICES_PATH = "/clusters/%s/services"
SERVICE_PATH = "/clusters/%s/services/%s"
ROLETYPES_CFG_KEY = 'roleTypeConfigs'

LOG = logging.getLogger(__name__)


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
  apiservice_list = ApiList([apiservice])
  body = json.dumps(apiservice_list.to_json_dict())
  resp = resource_root.post(SERVICES_PATH % (cluster_name,), data=body)
  # The server returns a list of created services (with size 1)
  return ApiList.from_json_dict(ApiService, resp, resource_root)[0]

def get_service(resource_root, name, cluster_name="default"):
  """
  Lookup a service by name
  @param resource_root: The root Resource object.
  @param name: Service name
  @param cluster_name: Cluster name
  @return: An ApiService object
  """
  dic = resource_root.get("%s/%s" % (SERVICES_PATH % (cluster_name,), name))
  return ApiService.from_json_dict(dic, resource_root)

def get_all_services(resource_root, cluster_name="default", view=None):
  """
  Get all services
  @param resource_root: The root Resource object.
  @param cluster_name: Cluster name
  @return: A list of ApiService objects.
  """
  dic = resource_root.get(SERVICES_PATH % (cluster_name,),
          params=view and dict(view=view) or None)
  return ApiList.from_json_dict(ApiService, dic, resource_root)

def delete_service(resource_root, name, cluster_name="default"):
  """
  Delete a service by name
  @param resource_root: The root Resource object.
  @param name: Service name
  @param cluster_name: Cluster name
  @return: The deleted ApiService object
  """
  resp = resource_root.delete("%s/%s" % (SERVICES_PATH % (cluster_name,), name))
  return ApiService.from_json_dict(resp, resource_root)


class ApiService(BaseApiObject):
  RO_ATTR = ('serviceState', 'healthSummary', 'healthChecks', 'clusterRef',
             'configStale', 'serviceUrl')
  RW_ATTR = ('name', 'type')

  def __init__(self, resource_root, name, type):
    # Unfortunately, the json key is called "type". So our input arg
    # needs to be called "type" as well, despite it being a python keyword.
    BaseApiObject.ctor_helper(**locals())

  def _get_cluster_name(self):
    if self.clusterRef:
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

  def _cmd(self, cmd, data=None, params=None):
    path = self._path() + '/commands/' + cmd
    resp = self._get_resource_root().post(path, data=data, params=params)
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

  def _role_cmd(self, cmd, roles):
    path = self._path() + '/roleCommands/' + cmd
    data = json.dumps({ ApiList.LIST_KEY : roles })
    resp = self._get_resource_root().post(path, data = data)
    return ApiList.from_json_dict(ApiCommand, resp, self._get_resource_root())

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

  def get_running_activities(self):
    path = self._path() + "/activities"
    resp = self._get_resource_root().get(path)
    return ApiList.from_json_dict(ApiActivity, resp, self._get_resource_root())

  def query_activities(self, query_str=None):
    path = self._path() + "/activities"
    params = { }
    if query_str:
      params['query'] = query_str
    resp = self._get_resource_root().get(path, params=params)
    return ApiList.from_json_dict(ApiActivity, resp, self._get_resource_root())

  def get_activity(self, job_id):
    path = self._path() + "/activities/%s" % (job_id,)
    resp = self._get_resource_root().get(path)
    return ApiActivity.from_json_dict(resp, self._get_resource_root())

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
    @return 2-tuple (service config dictionary, role type configurations)
    """
    path = self._path() + '/config'
    resp = self._get_resource_root().get(path,
        params = view and dict(view=view) or None)
    return self._parse_svc_config(resp, view)

  def update_config(self, svc_config, **rt_configs):
    """
    Update the service's configuration.

    @param svc_config Dictionary with service configuration to update.
    @param rt_configs Dict of role type configurations to update.
    @return 2-tuple (service config dictionary, role type configurations)
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

    @param name Role name
    @return The deleted ApiRole object
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

  def get_metrics(self, from_time=None, to_time=None, metrics=None, view=None):
    """
    Retrieve metric readings for the service.

    @param from_time: A datetime; start of the period to query (optional).
    @param to_time: A datetime; end of the period to query (default = now).
    @param metrics: List of metrics to query (default = all).
    @param view: View to materialize ('full' or 'summary')
    @return List of metrics and their readings.
    """
    return self._get_resource_root().get_metrics(self._path() + '/metrics',
        from_time, to_time, metrics, view)

  def start(self):
    """
    Start a service.

    @return Reference to the submitted command.
    """
    return self._cmd('start')

  def stop(self):
    """
    Stop a service.

    @return Reference to the submitted command.
    """
    return self._cmd('stop')

  def restart(self):
    """
    Restart a service.

    @return Reference to the submitted command.
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

  def create_beeswax_warehouse(self):
    """
    Create the Beeswax role's warehouse for a Hue service.
    @return: Reference to the submitted command.
    """
    return self._cmd('hueCreateHiveWarehouse')

  def create_hbase_root(self):
    """
    Create the root directory of an HBase service.

    @return Reference to the submitted command.
    """
    return self._cmd('hbaseCreateRoot')

  def refresh(self, *role_names):
    """
    Execute the "refresh" command on a set of roles.

    @param: role_names Names of the roles to refresh.
    @return: Reference to the submitted command.
    """
    return self._role_cmd('refresh', role_names)

  def decommission(self, *role_names):
    """
    Decommission roles in a service.

    @param role_names Names of the roles to decommission.
    @return Reference to the submitted command.
    """
    data = json.dumps({ ApiList.LIST_KEY : role_names })
    return self._cmd('decommission', data)

  def recommission(self, *role_names):
    """
    Recommission roles in a service.

    @param role_names Names of the roles to recommission.
    @return Reference to the submitted command.
    @since: API v2
    """
    data = json.dumps({ ApiList.LIST_KEY : role_names })
    return self._cmd('recommission', data)

  def deploy_client_config(self, *role_names):
    """
    Deploys client configuration to the hosts where roles are running.

    @param: role_names Names of the roles to decommission.
    @return: Reference to the submitted command.
    """
    data = json.dumps({ ApiList.LIST_KEY : role_names })
    return self._cmd('deployClientConfig', data)

  def disable_hdfs_auto_failover(self, nameservice):
    """
    Disable auto-failover for a highly available HDFS nameservice.

    @param nameservice: Affected nameservice.
    @return: Reference to the submitted command.
    """
    return self._cmd('hdfsDisableAutoFailover', data = json.dumps(nameservice))

  def disable_hdfs_ha(self, active_name, secondary_name,
      start_dependent_services=True, deploy_client_configs=True,
                      disable_quorum_journal=False):
    """
    Disable high availability for an HDFS NameNode.

    @param active_name: Name of the NameNode to keep.
    @param secondary_name: Name of (existing) SecondaryNameNode to link to
                           remaining NameNode.
    @param start_dependent_services: whether to re-start dependent services.
    @param deploy_client_configs: whether to re-deploy client configurations.
    @param disable_quorum_journal: whether to disable Quorum Journal. Available since API v2.
                                   Quorum Journal will be disabled for all
                                   nameservices that have Quorum Journal
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
      if disable_quorum_journal:
        raise AttributeError("Quorum Journal is not supported prior to Cloudera Manager 4.1.")
    else:
      args['disableQuorumJournal'] = disable_quorum_journal

    return self._cmd('hdfsDisableHa', data = json.dumps(args))

  def enable_hdfs_auto_failover(self, nameservice, active_fc_name,
      standby_fc_name, zk_service):
    """
    Enable auto-failover for an HDFS nameservice.

    @param nameservice: Nameservice for which to enable auto-failover.
    @param active_fc_name: Name of failover controller to create for active node.
    @param standby_fc_name: Name of failover controller to create for stand-by node.
    @param zk_service: ZooKeeper service to use.
    @return: Reference to the submitted command.
    """
    args = dict(
      nameservice = nameservice,
      activeFCName = active_fc_name,
      standByFCName = standby_fc_name,
      zooKeeperService = dict(
        clusterName = zk_service.clusterRef.clusterName,
        serviceName = zk_service.name,
        ),
      )
    return self._cmd('hdfsEnableAutoFailover', data = json.dumps(args))

  def enable_hdfs_ha(self, active_name, active_shared_path, standby_name,
      standby_shared_path, nameservice, start_dependent_services=True,
      deploy_client_configs=True, enable_quorum_journal=False):
    """
    Enable high availability for an HDFS NameNode.

    @param active_name: name of active NameNode.
    @param active_shared_path: shared edits path for active NameNode.
                               Ignored if Quorum Journal is being enabled.
    @param standby_name: name of stand-by NameNode.
    @param standby_shared_path: shared edits path for stand-by NameNode.
                                Ignored if Quourm Journal is being enabled.
    @param nameservice: nameservice for the HA pair.
    @param start_dependent_services: whether to re-start dependent services.
    @param deploy_client_configs: whether to re-deploy client configurations.
    @param enable_quorum_journal: whether to enable Quorum Journal. Available since API v2.
                                  Quorum Journal will be enabled for all
                                  nameservices except those configured with NFS High
                                  Availability.
    @return: Reference to the submitted command.
    """
    args = dict(
      activeName = active_name,
      standByName = standby_name,
      nameservice = nameservice,
      startDependentServices = start_dependent_services,
      deployClientConfigs = deploy_client_configs,
    )

    if enable_quorum_journal:
      version = self._get_resource_root().version
      if version < 2:
        raise AttributeError("Quorum Journal is not supported prior to Cloudera Manager 4.1.")
      else:
        args['enableQuorumJournal'] = enable_quorum_journal
    else:
      args['activeSharedEditsPath'] = active_shared_path
      args['standBySharedEditsPath'] = standby_shared_path

    return self._cmd('hdfsEnableHa', data = json.dumps(args))

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
    return self._cmd('hdfsFailover', data = json.dumps(args))

  def format_hdfs(self, *namenodes):
    """
    Format NameNode instances of an HDFS service.

    @param namenodes Name of NameNode instances to format.
    @return List of submitted commands.
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

    @param namenodes Name of NameNode instances.
    @return List of submitted commands.
    """
    return self._role_cmd('hdfsInitializeSharedDir', namenodes)

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

    @param: servers Name of Hue Server roles to synchronize.
    @return: List of submitted commands.
    """
    return self._role_cmd('hueSyncDb', servers)


class ApiServiceSetupInfo(ApiService):
  RO_ATTR = ( )
  RW_ATTR = ('name', 'type', 'config', 'roles')

  def __init__(self, name=None, type=None,
               config=None, roles=None):
    # The BaseApiObject expects a resource_root, which we don't care about
    resource_root = None
    # Unfortunately, the json key is called "type". So our input arg
    # needs to be called "type" as well, despite it being a python keyword.
    BaseApiObject.ctor_helper(**locals())

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

