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

__docformat__ = "epytext"

ROLES_PATH = "/clusters/%s/services/%s/roles"
CM_ROLES_PATH = "/cm/service/roles"

def _get_roles_path(cluster_name, service_name):
  if cluster_name:
    return ROLES_PATH % (cluster_name, service_name)
  else:
    return CM_ROLES_PATH

def _get_role_path(cluster_name, service_name, role_name):
  path = _get_roles_path(cluster_name, service_name)
  return "%s/%s" % (path, role_name)

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
  return _get_role(resource_root, _get_role_path(cluster_name, service_name, name))

def _get_role(resource_root, path):
  dic = resource_root.get(path)
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
  resp = resource_root.delete(_get_role_path(cluster_name, service_name, name))
  return ApiRole.from_json_dict(resp, resource_root)


class ApiRole(BaseApiObject):
  _ATTRIBUTES = {
    'name'                : None,
    'type'                : None,
    'hostRef'             : Attr(ApiHostRef),
    'roleState'           : ROAttr(),
    'healthSummary'       : ROAttr(),
    'healthChecks'        : ROAttr(),
    'serviceRef'          : ROAttr(ApiServiceRef),
    'configStale'         : ROAttr(),
    'haStatus'            : ROAttr(),
    'roleUrl'             : ROAttr(),
    'commissionState'     : ROAttr(),
    'maintenanceMode'     : ROAttr(),
    'maintenanceOwners'   : ROAttr(),
    'roleConfigGroupRef'  : ROAttr(ApiRoleConfigGroupRef),
  }

  def __init__(self, resource_root, name=None, type=None, hostRef=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiRole>: %s (cluster: %s; service: %s)" % (
        self.name, self.serviceRef.clusterName, self.serviceRef.serviceName)

  def _path(self):
    return _get_role_path(self.serviceRef.clusterName,
                          self.serviceRef.serviceName,
                          self.name)

  def _cmd(self, cmd, data=None):
    path = self._path() + '/commands/' + cmd
    resp = self._get_resource_root().post(path, data=data)
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

  def _get_log(self, log):
    path = "%s/logs/%s" % (self._path(), log)
    return self._get_resource_root().get(path)

  def get_commands(self, view=None):
    """
    Retrieve a list of running commands for this role.

    @param view: View to materialize ('full' or 'summary')
    @return: A list of running commands.
    """
    resp = self._get_resource_root().get(
        self._path() + '/commands',
        params = view and dict(view=view) or None)
    return ApiList.from_json_dict(ApiCommand, resp, self._get_resource_root())

  def get_config(self, view = None):
    """
    Retrieve the role's configuration.

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
    Update the role's configuration.

    @param config Dictionary with configuration to update.
    @return Dictionary with updated configuration.
    """
    path = self._path() + '/config'
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

  def get_metrics(self, from_time=None, to_time=None, metrics=None, view=None):
    """
    Retrieve metric readings for the role.

    @param from_time: A datetime; start of the period to query (optional).
    @param to_time: A datetime; end of the period to query (default = now).
    @param metrics: List of metrics to query (default = all).
    @param view: View to materialize ('full' or 'summary')
    @return List of metrics and their readings.
    """
    return self._get_resource_root().get_metrics(self._path() + '/metrics',
        from_time, to_time, metrics, view)

  def enter_maintenance_mode(self):
    """
    Put the role in maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('enterMaintenanceMode')
    if cmd.success:
      self._update(_get_role(self._get_resource_root(), self._path()))
    return cmd

  def exit_maintenance_mode(self):
    """
    Take the role out of maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('exitMaintenanceMode')
    if cmd.success:
      self._update(_get_role(self._get_resource_root(), self._path()))
    return cmd
