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
  return call(resource_root.post,
      _get_roles_path(cluster_name, service_name),
      ApiRole, True, data=[apirole])[0]

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
  return call(resource_root.get, path, ApiRole)

def get_all_roles(resource_root, service_name, cluster_name="default", view=None):
  """
  Get all roles
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param cluster_name: Cluster name
  @return: A list of ApiRole objects.
  """
  return call(resource_root.get,
      _get_roles_path(cluster_name, service_name),
      ApiRole, True, params=view and dict(view=view) or None)

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
  return call(resource_root.delete,
      _get_role_path(cluster_name, service_name, name), ApiRole)


class ApiRole(BaseApiResource):
  _ATTRIBUTES = {
    'name'                  : None,
    'type'                  : None,
    'hostRef'               : Attr(ApiHostRef),
    'roleState'             : ROAttr(),
    'healthSummary'         : ROAttr(),
    'healthChecks'          : ROAttr(),
    'serviceRef'            : ROAttr(ApiServiceRef),
    'configStale'           : ROAttr(),
    'configStalenessStatus' : ROAttr(),
    'haStatus'              : ROAttr(),
    'roleUrl'               : ROAttr(),
    'commissionState'       : ROAttr(),
    'maintenanceMode'       : ROAttr(),
    'maintenanceOwners'     : ROAttr(),
    'roleConfigGroupRef'    : ROAttr(ApiRoleConfigGroupRef),
    'zooKeeperServerMode'   : ROAttr(),
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

  def _get_log(self, log):
    path = "%s/logs/%s" % (self._path(), log)
    return self._get_resource_root().get(path)

  def get_commands(self, view=None):
    """
    Retrieve a list of running commands for this role.

    @param view: View to materialize ('full' or 'summary')
    @return: A list of running commands.
    """
    return self._get("commands", ApiCommand, True,
        params = view and dict(view=view) or None)

  def get_config(self, view = None):
    """
    Retrieve the role's configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return: Dictionary with configuration data.
    """
    return self._get_config("config", view)

  def update_config(self, config):
    """
    Update the role's configuration.

    @param config: Dictionary with configuration to update.
    @return: Dictionary with updated configuration.
    """
    return self._update_config("config", config)

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

  def get_stacks_log(self):
    """
    Retrieve the contents of the role's stacks log file.

    @return: Contents of stacks log file.
    @since: API v8
    """
    return self._get_log('stacks')

  def get_stacks_logs_bundle(self):
    """
    Retrieve a zip file of the role's stacks log files.

    @return: A zipfile of stacks log files.
    @since: API v8
    """
    return self._get_log('stacksBundle')

  def get_metrics(self, from_time=None, to_time=None, metrics=None, view=None):
    """
    This endpoint is not supported as of v6. Use the timeseries API
    instead. To get all metrics for a role with the timeseries API use
    the query:

    'select * where roleName = $ROLE_NAME'.

    To get specific metrics for a role use a comma-separated list of
    the metric names as follows:

    'select $METRIC_NAME1, $METRIC_NAME2 where roleName = $ROLE_NAME'.

    For more information see http://tiny.cloudera.com/tsquery_doc
    @param from_time: A datetime; start of the period to query (optional).
    @param to_time: A datetime; end of the period to query (default = now).
    @param metrics: List of metrics to query (default = all).
    @param view: View to materialize ('full' or 'summary')
    @return: List of metrics and their readings.
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

  def list_commands_by_name(self):
    """
    Lists all the commands that can be executed by name
    on the provided role.

    @return: A list of command metadata objects
    @since: API v6
    """
    return self._get("commandsByName", ApiCommandMetadata, True, api_version=6)


