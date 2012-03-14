# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import config_to_json, json_to_config, \
    ApiList, BaseApiObject, ApiHostRef

__docformat__ = "epytext"

ROLES_PATH = "/clusters/%s/services/%s/roles"
CONFIG_PATH = ROLES_PATH + "/%s/config"

CM_ROLES_PATH = "/cm/service/roles"
CM_CONFIG_PATH = CM_ROLES_PATH + "/%s/config"

def _get_roles_path(cluster_name, service_name):
  if cluster_name:
    return ROLES_PATH % (cluster_name, service_name)
  else:
    return CM_ROLES_PATH

def _get_role_config_path(cluster_name, service_name, role_name):
  if cluster_name:
    return CONFIG_PATH % (cluster_name, service_name, role_name)
  else:
    return CM_CONFIG_PATH % (role_name,)

def create_role(resource_root,
                service_name,
                role_type,
                role_name,
                host_id,
                cluster_name="default"):
  """
  Create a role
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param role_type: Role type
  @param role_name: Role name
  @param cluster_name: Cluster name
  @return: An ApiRole object
  """
  apirole = ApiRole(resource_root, role_name, role_type,
                    ApiHostRef(resource_root, host_id))
  apirole_list = ApiList([apirole])
  body = json.dumps(apirole_list.to_json_dict())
  resp = resource_root.post(_get_roles_path(cluster_name, service_name),
      data=body)
  # The server returns a list of created roles (with size 1)
  return ApiList.from_json_dict(ApiRole, resp, resource_root)[0]

def get_role(resource_root, service_name, name, cluster_name="default"):
  """
  Lookup a role by name
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param name: Role name
  @param cluster_name: Cluster name
  @return: An ApiRole object
  """
  dic = resource_root.get("%s/%s" %
      (_get_roles_path(cluster_name, service_name), name))
  return ApiRole.from_json_dict(dic, resource_root)

def get_all_roles(resource_root, service_name, cluster_name="default", view=None):
  """
  Get all roles
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param cluster_name: Cluster name
  @return: A list of ApiRole objects.
  """
  dic = resource_root.get(_get_roles_path(cluster_name, service_name),
          params=view and dict(view=view) or None)
  return ApiList.from_json_dict(ApiRole, dic, resource_root)

def get_roles_by_type(resource_root, service_name, role_type,
                      cluster_name="default", view=None):
  """
  Get all roles of a certain type in a service
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param role_type: Role type
  @param cluster_name: Cluster name
  @return: A list of ApiRole objects.
  """
  roles = get_all_roles(resource_root, service_name, cluster_name, view)
  return [ r for r in roles if r.type == role_type ]

def delete_role(resource_root, service_name, name, cluster_name="default"):
  """
  Delete a role by name
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param name: Role name
  @param cluster_name: Cluster name
  @return: The deleted ApiRole object
  """
  resp = resource_root.delete("%s/%s" %
          (_get_roles_path(cluster_name, service_name), name))
  return ApiRole.from_json_dict(resp, resource_root)


class ApiRole(BaseApiObject):
  RO_ATTR = ('roleState', 'healthSummary', 'healthChecks', 'serviceRef', 'configStale')
  RW_ATTR = ('name', 'type', 'hostRef')

  def __init__(self, resource_root, name, type, hostRef):
    # Unfortunately, the json key is called "type". So our input arg
    # needs to be called "type" as well, despite it being a python keyword.
    BaseApiObject.ctor_helper(**locals())

  def _get_log(self, log):
    path = _get_roles_path(self.serviceRef.clusterName,
                           self.serviceRef.serviceName)
    path = "%s/%s/logs/%s" % (path, self.name, log)
    return self._get_resource_root().get(path)

  def get_config(self, view = None):
    """
    Retrieve the role's configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return Dictionary with configuration data.
    """
    path = _get_role_config_path(self.serviceRef.clusterName,
                                 self.serviceRef.serviceName,
                                 self.name)
    resp = self._get_resource_root().get(path,
        params = view and dict(view=view) or None)
    return json_to_config(resp, view == 'full')

  def update_config(self, config):
    """
    Update the role's configuration.

    @param config Dictionary with configuration to update.
    @return Dictionary with updated configuration.
    """
    path = _get_role_config_path(self.serviceRef.clusterName,
                                 self.serviceRef.serviceName,
                                 self.name)
    resp = self._get_resource_root().put(path, data = config_to_json(config))
    return json_to_config(resp)

  def get_full_log(self):
    """
    Retrieve the contents of the role's log file.

    @return: Contents of log file.
    """
    return self._get_log('full')

  def get_stdout(self):
    """
    Retrieve the contents of the role's standard output.

    @return: Contents of stdout.
    """
    return self._get_log('stdout')

  def get_stderr(self):
    """
    Retrieve the contents of the role's standard error.

    @return: Contents of stderr.
    """
    return self._get_log('stderr')
