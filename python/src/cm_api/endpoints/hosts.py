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

import datetime

from cm_api.endpoints.types import *

__docformat__ = "epytext"

HOSTS_PATH = "/hosts"

def create_host(resource_root, host_id, name, ipaddr, rack_id=None):
  """
  Create a host
  @param resource_root: The root Resource object.
  @param host_id: Host id
  @param name: Host name
  @param ipaddr: IP address
  @param rack_id: Rack id. Default None
  @return: An ApiHost object
  """
  apihost = ApiHost(resource_root, host_id, name, ipaddr, rack_id)
  return call(resource_root.post, HOSTS_PATH, ApiHost, True, data=[apihost])[0]

def get_host(resource_root, host_id):
  """
  Lookup a host by id
  @param resource_root: The root Resource object.
  @param host_id: Host id
  @return: An ApiHost object
  """
  return call(resource_root.get, "%s/%s" % (HOSTS_PATH, host_id), ApiHost)

def get_all_hosts(resource_root, view=None):
  """
  Get all hosts
  @param resource_root: The root Resource object.
  @return: A list of ApiHost objects.
  """
  return call(resource_root.get, HOSTS_PATH, ApiHost, True,
          params=view and dict(view=view) or None)

def delete_host(resource_root, host_id):
  """
  Delete a host by id
  @param resource_root: The root Resource object.
  @param host_id: Host id
  @return: The deleted ApiHost object
  """
  return call(resource_root.delete, "%s/%s" % (HOSTS_PATH, host_id), ApiHost)


class ApiHost(BaseApiResource):
  _ATTRIBUTES = {
    'hostId'            : None,
    'hostname'          : None,
    'ipAddress'         : None,
    'rackId'            : None,
    'status'            : ROAttr(),
    'lastHeartbeat'     : ROAttr(datetime.datetime),
    'roleRefs'          : ROAttr(ApiRoleRef),
    'healthSummary'     : ROAttr(),
    'healthChecks'      : ROAttr(),
    'hostUrl'           : ROAttr(),
    'commissionState'   : ROAttr(),
    'maintenanceMode'   : ROAttr(),
    'maintenanceOwners' : ROAttr(),
    'numCores'          : ROAttr(),
    'numPhysicalCores'  : ROAttr(),
    'totalPhysMemBytes' : ROAttr(),
  }

  def __init__(self, resource_root, hostId=None, hostname=None,
      ipAddress=None, rackId=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiHost>: %s (%s)" % (self.hostId, self.ipAddress)

  def _path(self):
    return HOSTS_PATH + '/' + self.hostId

  def _put_host(self):
    """
    Update this resource.
    @return: The updated object.
    """
    return self._put('', ApiHost, data=self)

  def get_config(self, view=None):
    """
    Retrieve the host's configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return: Dictionary with configuration data.
    """
    return self._get_config("config", view)

  def update_config(self, config):
    """
    Update the host's configuration.

    @param config: Dictionary with configuration to update.
    @return: Dictionary with updated configuration.
    """
    return self._update_config("config", config)

  def get_metrics(self, from_time=None, to_time=None, metrics=None,
      ifs=[], storageIds=[], view=None):
    """
    This endpoint is not supported as of v6. Use the timeseries API
    instead. To get all metrics for a host with the timeseries API use
    the query:

    'select * where hostId = $HOST_ID'.

    To get specific metrics for a host use a comma-separated list of
    the metric names as follows:

    'select $METRIC_NAME1, $METRIC_NAME2 where hostId = $HOST_ID'.

    For more information see http://tiny.cloudera.com/tsquery_doc
    @param from_time: A datetime; start of the period to query (optional).
    @param to_time: A datetime; end of the period to query (default = now).
    @param metrics: List of metrics to query (default = all).
    @param ifs: network interfaces to query. Default all, use None to disable.
    @param storageIds: storage IDs to query. Default all, use None to disable.
    @param view: View to materialize ('full' or 'summary')
    @return: List of metrics and their readings.
    """
    params = { }
    if ifs:
      params['ifs'] = ifs
    elif ifs is None:
      params['queryNw'] = 'false'
    if storageIds:
      params['storageIds'] = storageIds
    elif storageIds is None:
      params['queryStorage'] = 'false'
    return self._get_resource_root().get_metrics(self._path() + '/metrics',
        from_time, to_time, metrics, view, params)

  def enter_maintenance_mode(self):
    """
    Put the host in maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('enterMaintenanceMode')
    if cmd.success:
      self._update(get_host(self._get_resource_root(), self.hostId))
    return cmd

  def exit_maintenance_mode(self):
    """
    Take the host out of maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('exitMaintenanceMode')
    if cmd.success:
      self._update(get_host(self._get_resource_root(), self.hostId))
    return cmd

  def migrate_roles(self, role_names_to_migrate, destination_host_id,
      clear_stale_role_data):
    """
    Migrate roles from this host to a different host.

    Currently, this command applies only to HDFS NameNode, JournalNode,
    and Failover Controller roles. In order to migrate these roles:

      - HDFS High Availability must be enabled, using quorum-based storage.
      - HDFS must not be configured to use a federated nameservice.

    I{B{Migrating a NameNode role requires cluster downtime.}} HDFS, along
    with all of its dependent services, will be stopped at the beginning
    of the migration process, and restarted at its conclusion.

    If the active NameNode is selected for migration, a manual failover
    will be performed before the role is migrated. The role will remain in
    standby mode after the migration is complete.

    When migrating a NameNode role, the co-located Failover Controller
    role must be migrated as well. The Failover Controller role name must
    be included in the list of role names to migrate specified in the
    arguments to this command (it will not be included implicitly). This
    command does not allow a Failover Controller role to be moved by itself,
    although it is possible to move a JournalNode independently.

    @param role_names_to_migrate: list of role names to migrate.
    @param destination_host_id: the id of the host to which the roles
                                should be migrated.
    @param clear_stale_role_data: true to delete existing stale role data,
                                  if any. For example, when migrating a
                                  NameNode, if the destination host has
                                  stale data in the NameNode data
                                  directories (possibly because a NameNode
                                  role was previously located there), this
                                  stale data will be deleted before migrating
                                  the role.
    @return: Reference to the submitted command.
    @since: API v10
    """
    args = dict(
      roleNamesToMigrate = role_names_to_migrate,
      destinationHostId = destination_host_id,
      clearStaleRoleData = clear_stale_role_data)
    return self._cmd('migrateRoles', data=args, api_version=10)

  def set_rack_id(self, rackId):
    """
    Update the rack ID of this host.
    """
    self.rackId = rackId
    self._put_host()
