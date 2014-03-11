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

from cm_api.endpoints.types import *
from cm_api.endpoints.roles import ApiRole

__docformat__ = "epytext"

ROLE_CONFIG_GROUPS_PATH = "/clusters/%s/services/%s/roleConfigGroups"
CM_ROLE_CONFIG_GROUPS_PATH = "/cm/service/roleConfigGroups"

def _get_role_config_groups_path(cluster_name, service_name):
  if cluster_name:
    return ROLE_CONFIG_GROUPS_PATH % (cluster_name, service_name)
  else:
    return CM_ROLE_CONFIG_GROUPS_PATH

def _get_role_config_group_path(cluster_name, service_name, name):
  path = _get_role_config_groups_path(cluster_name, service_name)
  return "%s/%s" % (path, name)

def create_role_config_groups(resource_root, service_name, apigroup_list,
    cluster_name="default"):
  """
  Create role config groups.
  @param resource_root: The root Resource object.
  @param service_name: Service name.
  @param apigroup_list: List of role config groups to create.
  @param cluster_name: Cluster name.
  @return: New ApiRoleConfigGroup object.
  @since: API v3
  """
  return call(resource_root.post,
      _get_role_config_groups_path(cluster_name, service_name),
      ApiRoleConfigGroup, True, data=apigroup_list, api_version=3)

def create_role_config_group(resource_root, service_name, name, display_name,
    role_type, cluster_name="default"):
  """
  Create a role config group.
  @param resource_root: The root Resource object.
  @param service_name: Service name.
  @param name: The name of the new group.
  @param display_name: The display name of the new group.
  @param role_type: The role type of the new group.
  @param cluster_name: Cluster name.
  @return: List of created role config groups.
  """
  apigroup = ApiRoleConfigGroup(resource_root, name, display_name, role_type)
  return create_role_config_groups(resource_root, service_name, [apigroup],
      cluster_name)[0]

def get_role_config_group(resource_root, service_name, name,
    cluster_name="default"):
  """
  Find a role config group by name.
  @param resource_root: The root Resource object.
  @param service_name: Service name.
  @param name: Role config group name.
  @param cluster_name: Cluster name.
  @return: An ApiRoleConfigGroup object.
  """
  return _get_role_config_group(resource_root, _get_role_config_group_path(
      cluster_name, service_name, name))

def _get_role_config_group(resource_root, path):
  return call(resource_root.get, path, ApiRoleConfigGroup, api_version=3)

def get_all_role_config_groups(resource_root, service_name,
    cluster_name="default"):
  """
  Get all role config groups in the specified service.
  @param resource_root: The root Resource object.
  @param service_name: Service name.
  @param cluster_name: Cluster name.
  @return: A list of ApiRoleConfigGroup objects.
  @since: API v3
  """
  return call(resource_root.get,
      _get_role_config_groups_path(cluster_name, service_name),
      ApiRoleConfigGroup, True, api_version=3)

def update_role_config_group(resource_root, service_name, name, apigroup,
    cluster_name="default"):
  """
  Update a role config group by name.
  @param resource_root: The root Resource object.
  @param service_name: Service name.
  @param name: Role config group name.
  @param apigroup: The updated role config group.
  @param cluster_name: Cluster name.
  @return: The updated ApiRoleConfigGroup object.
  @since: API v3
  """
  return call(resource_root.put,
      _get_role_config_group_path(cluster_name, service_name, name),
      ApiRoleConfigGroup, data=apigroup, api_version=3)

def delete_role_config_group(resource_root, service_name, name,
    cluster_name="default"):
  """
  Delete a role config group by name.
  @param resource_root: The root Resource object.
  @param service_name: Service name.
  @param name: Role config group name.
  @param cluster_name: Cluster name.
  @return: The deleted ApiRoleConfigGroup object.
  @since: API v3
  """
  return call(resource_root.delete,
      _get_role_config_group_path(cluster_name, service_name, name),
      ApiRoleConfigGroup, api_version=3)

def move_roles(resource_root, service_name, name, role_names,
    cluster_name="default"):
  """
  Moves roles to the specified role config group.

  The roles can be moved from any role config group belonging
  to the same service. The role type of the destination group
  must match the role type of the roles.

  @param name: The name of the group the roles will be moved to.
  @param role_names: The names of the roles to move.
  @return: List of roles which have been moved successfully.
  @since: API v3
  """
  return call(resource_root.put,
      _get_role_config_group_path(cluster_name, service_name, name) + '/roles',
      ApiRole, True, data=role_names, api_version=3)

def move_roles_to_base_role_config_group(resource_root, service_name,
     role_names, cluster_name="default"):
  """
  Moves roles to the base role config group.

  The roles can be moved from any role config group belonging to the same
  service. The role type of the roles may vary. Each role will be moved to
  its corresponding base group depending on its role type.

  @param role_names: The names of the roles to move.
  @return: List of roles which have been moved successfully.
  @since: API v3
  """
  return call(resource_root.put,
      _get_role_config_groups_path(cluster_name, service_name) + '/roles',
      ApiRole, True, data=role_names, api_version=3)


class ApiRoleConfigGroup(BaseApiResource):
  """
  name is RW only temporarily; once all RCG names are unique,
  this property will be auto-generated and Read-only

  @since: API v3
  """
  _ATTRIBUTES = {
    'name'        : None,
    'displayName' : None,
    'roleType'    : None,
    'config'      : Attr(ApiConfig),
    'base'        : ROAttr(),
    'serviceRef'  : ROAttr(ApiServiceRef),
  }

  def __init__(self, resource_root, name=None, displayName=None, roleType=None,
      config=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiRoleConfigGroup>: %s (cluster: %s; service: %s)" % (
        self.name, self.serviceRef.clusterName, self.serviceRef.serviceName)

  def _api_version(self):
    return 3

  def _path(self):
    return _get_role_config_group_path(self.serviceRef.clusterName,
                          self.serviceRef.serviceName,
                          self.name)

  def get_config(self, view = None):
    """
    Retrieve the group's configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary').
    @return: Dictionary with configuration data.
    """
    path = self._path() + '/config'
    resp = self._get_resource_root().get(path,
        params = view and dict(view=view) or None)
    return json_to_config(resp, view == 'full')

  def update_config(self, config):
    """
    Update the group's configuration.

    @param config: Dictionary with configuration to update.
    @return: Dictionary with updated configuration.
    """
    path = self._path() + '/config'
    resp = self._get_resource_root().put(path, data = config_to_json(config))
    return json_to_config(resp)

  def get_all_roles(self):
    """
    Retrieve the roles in this role config group.

    @return: List of roles in this role config group.
    """
    return self._get("roles", ApiRole, True)

  def move_roles(self, roles):
    """
    Moves roles to this role config group.

    The roles can be moved from any role config group belonging
    to the same service. The role type of the destination group
    must match the role type of the roles.

    @param roles: The names of the roles to move.
    @return: List of roles which have been moved successfully.
    """
    return move_roles(self._get_resource_root(), self.serviceRef.serviceName,
        self.name, roles, self.serviceRef.clusterName)
