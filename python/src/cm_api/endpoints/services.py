# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json
import logging

from cm_api.endpoints.types import config_to_json, json_to_config, \
    config_to_api_list, ApiCommand, ApiHostRef, ApiList, BaseApiObject
from cm_api.endpoints import roles, roletypes

__docformat__ = "epytext"

SERVICES_PATH = "/clusters/%s/services"
SERVICE_PATH = "/clusters/%s/services/%s"
ROLETYPES_PATH = "/clusters/%s/services/%s/roletypes"

LOG = logging.getLogger(__name__)


def create_service(resource_root, name, service_type, version,
                   cluster_name="default"):
  """
  Create a service
  @param resource_root: The root Resource object.
  @param name: Service name
  @param service_type: Service type
  @param version: Service version
  @param cluster_name: Cluster name
  @return: An ApiService object
  """
  apiservice = ApiService(resource_root, name, service_type, version)
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
  RO_ATTR = ('serviceState', 'healthSummary', 'healthChecks', 'clusterRef', 'configStale')
  RW_ATTR = ('name', 'type', 'version')

  def __init__(self, resource_root, name, type, version):
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

  def _cmd(self, cmd, data=None):
    path = self._path() + '/commands/' + cmd
    resp = self._get_resource_root().post(path, data=data)
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

  def _role_cmd(self, cmd, roles):
    path = self._path() + '/roleCommands/' + cmd
    data = json.dumps({ ApiList.LIST_KEY : roles })
    resp = self._get_resource_root().post(path, data = data)
    return ApiList.from_json_dict(ApiCommand, resp, self._get_resource_root())

  def get_config(self, view = None):
    """
    Retrieve the service's configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return Dictionary with configuration data.
    """
    path = self._path() + '/config'
    resp = self._get_resource_root().get(path,
        params = view and dict(view=view) or None)
    return json_to_config(resp, view == 'full')

  def update_config(self, config):
    """
    Update the service's configuration.

    @param config Dictionary with configuration to update.
    @return Dictionary with updated configuration.
    """
    path = self._path() + '/config'
    resp = self._get_resource_root().put(path, data = config_to_json(config))
    return json_to_config(resp)

  def create_role(self, role_name, role_type, host_id):
    """
    Create a role.

    @param role_name: Role name
    @param role_type: Role type
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
    if self.type == 'MGMT':
      LOG.error("Management service does not support /roleTypes")
      return None
    resp = self._get_resource_root().get(self._path() + '/roleTypes')
    return resp[ApiList.LIST_KEY]

  def get_role_type(self, roletype):
    """
    Get a role type resource by name.

    @param roletype: Role type
    @return: An ApiRoleType object
    """
    if self.type == 'MGMT':
      LOG.error("Management service does not support /roleTypes")
      return None
    return roletypes.ApiRoleType(self._get_resource_root(), self, roletype)

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

    @param: role_names Names of the roles to decommission.
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

  def deploy_client_config(self, *role_names):
    """
    Deploys client configuration to the hosts where roles are running.

    @param: role_names Names of the roles to decommission.
    @return: Reference to the submitted command.
    """
    data = json.dumps({ ApiList.LIST_KEY : role_names })
    return self._cmd('deployClientConfig', data)

  def enable_hdfs_ha(self, active_name, active_shared_path, standby_name,
      standby_shared_path, nameservice):
    """
    Enable high availability for an HDFS NameNode.

    @param active_name: name of active NameNode.
    @param active_shared_path: shared edits path for active NameNode.
    @param standby_name: name of stand-by NameNode.
    @param standby_shared_path: shared edits path for stand-by NameNode.
    @param nameservice: name service for the HA pair.
    @return: Reference to the submitted command.
    """
    args = dict(
      activeName = active_name,
      activeSharedEditsPath = active_shared_path,
      standByName = standby_name,
      standBySharedEditsPath = standby_shared_path,
      nameservice = nameservice,
    )
    return self._cmd('hdfsEnableHa', data = json.dumps(args))

  def failover_hdfs(self, active_name, standby_name):
    """
    Initiate a failover of an HDFS NameNode HA pair.

    @param active_name: name of active NameNode.
    @param standby_name: name of stand-by NameNode.
    @return: Reference to the submitted command.
    """
    args = { ApiList.LIST_KEY : [ active_name, standby_name ] }
    return self._cmd('hdfsFailover', data = json.dumps(args))

  def format_hdfs(self, *namenodes):
    """
    Format NameNode instances of an HDFS service.

    @param namenodes Name of NameNode instances to format.
    @return List of submitted commands.
    """
    return self._role_cmd('hdfsFormat', namenodes)

  def sync_hue_db(self, *servers):
    """
    Synchronize the Hue server's database.

    @param: servers Name of Hue Server roles to synchronize.
    @return: List of submitted commands.
    """
    return self._role_cmd('hueSyncDb', servers)


class ApiServiceSetupInfo(ApiService):
  RO_ATTR = ( )
  RW_ATTR = ('name', 'type', 'version', 'config', 'roleTypes', 'roles')

  def __init__(self, name=None, type=None, version=None,
               config=None, roleTypes=None, roles=None):
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
    self.config = config_to_api_list(config)

  def add_role_type_info(self, role_type, config):
    """
    Add a role type setup info.

    @param role_type: Role type
    @param config: A dictionary of role type configuration
    """
    if self.roleTypes is None:
      self.roleTypes = [ ]
    self.roleTypes.append({
        'roleType' : role_type,
        'config' : config_to_api_list(config) })

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

