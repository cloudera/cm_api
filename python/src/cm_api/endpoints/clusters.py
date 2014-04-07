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
from cm_api.endpoints import services, parcels, host_templates

__docformat__ = "epytext"

CLUSTERS_PATH = "/clusters"

def create_cluster(resource_root, name, version):
  """
  Create a cluster
  @param resource_root: The root Resource object.
  @param name: Cluster name
  @param version: Cluster CDH version
  @return: An ApiCluster object
  """
  apicluster = ApiCluster(resource_root, name, version)
  return call(resource_root.post, CLUSTERS_PATH, ApiCluster, True,
      data=[apicluster])[0]

def get_cluster(resource_root, name):
  """
  Lookup a cluster by name
  @param resource_root: The root Resource object.
  @param name: Cluster name
  @return: An ApiCluster object
  """
  return call(resource_root.get, "%s/%s" % (CLUSTERS_PATH, name), ApiCluster)

def get_all_clusters(resource_root, view=None):
  """
  Get all clusters
  @param resource_root: The root Resource object.
  @return: A list of ApiCluster objects.
  """
  return call(resource_root.get, CLUSTERS_PATH, ApiCluster, True,
      params=view and dict(view=view) or None)

def delete_cluster(resource_root, name):
  """
  Delete a cluster by name
  @param resource_root: The root Resource object.
  @param name: Cluster name
  @return: The deleted ApiCluster object
  """
  return call(resource_root.delete, "%s/%s" % (CLUSTERS_PATH, name), ApiCluster)

class ApiCluster(BaseApiResource):
  _ATTRIBUTES = {
    'name'              : None,
    'displayName'       : None,
    'version'           : None,
    'fullVersion'       : None,
    'maintenanceMode'   : ROAttr(),
    'maintenanceOwners' : ROAttr(),
  }

  def __init__(self, resource_root, name=None, version=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiCluster>: %s; version: %s" % (self.name, self.version)

  def _path(self):
    return "%s/%s" % (CLUSTERS_PATH, self.name)

  def _put(self, dic, params=None):
    """Change cluster attributes"""
    cluster = self._put('', ApiCluster, data=dic, params=params)
    self._update(cluster)
    return self

  def get_service_types(self):
    """
    Get all service types supported by this cluster.

    @return: A list of service types (strings)
    """
    resp = self._get_resource_root().get(self._path() + '/serviceTypes')
    return resp[ApiList.LIST_KEY]

  def get_commands(self, view=None):
    """
    Retrieve a list of running commands for this cluster.

    @param view: View to materialize ('full' or 'summary')
    @return: A list of running commands.
    """
    return self._get("commands", ApiCommand, True,
        params = view and dict(view=view) or None)

  def rename(self, newname):
    """
    Rename a cluster.

    @param newname: New cluster name
    @return: An ApiCluster object
    @since: API v2
    """
    dic = self.to_json_dict()
    dic['name'] = newname
    return self._put(dic)

  def create_service(self, name, service_type):
    """
    Create a service.

    @param name: Service name
    @param service_type: Service type
    @return: An ApiService object
    """
    return services.create_service(self._get_resource_root(), name,
        service_type, self.name)

  def delete_service(self, name):
    """
    Delete a service by name.

    @param name Service name
    @return The deleted ApiService object
    """
    return services.delete_service(self._get_resource_root(), name, self.name)

  def get_service(self, name):
    """
    Lookup a service by name.

    @param name: Service name
    @return: An ApiService object
    """
    return services.get_service(self._get_resource_root(), name, self.name)

  def get_all_services(self, view = None):
    """
    Get all services in this cluster.

    @return: A list of ApiService objects.
    """
    return services.get_all_services(self._get_resource_root(), self.name, view)

  def get_parcel(self, product, version):
    """
    Lookup a parcel by product and version.

    @param product: the product name
    @param version: the product version
    @return: An ApiParcel object
    """
    return parcels.get_parcel(self._get_resource_root(), product, version, self.name)

  def get_all_parcels(self, view = None):
    """
    Get all parcels in this cluster.

    @return: A list of ApiParcel objects.
    """
    return parcels.get_all_parcels(self._get_resource_root(), self.name, view)

  def list_hosts(self):
    """
    Lists all the hosts that are associated with this cluster.

    @return: A list of ApiHostRef objects of the hosts in the cluster.
    @since: API v3
    """
    return self._get("hosts", ApiHostRef, True, api_version=3)

  def remove_host(self, hostId):
    """
    Removes the association of the host with the cluster.

    @return: A ApiHostRef of the host that was removed.
    @since: API v3
    """
    return self._delete("hosts/" + hostId, ApiHostRef, api_version=3)

  def remove_all_hosts(self):
    """
    Removes the association of all the hosts with the cluster.

    @return: A list of ApiHostRef objects of the hosts that were removed.
    @since: API v3
    """
    return self._delete("hosts", ApiHostRef, True, api_version=3)

  def add_hosts(self, hostIds):
    """
    Adds a host to the cluster.

    @param hostIds: List of IDs of hosts to add to cluster.
    @return: A list of ApiHostRef objects of the new
             hosts that were added to the cluster
    @since: API v3
    """
    hostRefList = [ApiHostRef(self._get_resource_root(), x) for x in hostIds]
    return self._post("hosts", ApiHostRef, True, data=hostRefList,
        api_version=3)

  def start(self):
    """
    Start all services in a cluster, respecting dependencies.

    @return: Reference to the submitted command.
    """
    return self._cmd('start')

  def stop(self):
    """
    Stop all services in a cluster, respecting dependencies.

    @return: Reference to the submitted command.
    """
    return self._cmd('stop')

  def restart(self):
    """
    Restart all services in the cluster.
    Services are restarted in the appropriate order given their dependencies.

    @return: Reference to the submitted command.
    """
    return self._cmd('restart')

  def deploy_client_config(self):
    """
    Deploys client configuration to the hosts on the cluster.

    @return: Reference to the submitted command.
    @since: API v2
    """
    return self._cmd('deployClientConfig')

  def upgrade_services(self):
    """
    Upgrades the services in the cluster to CDH5 version.
    This command requires that the CDH packages in the hosts used by the
    cluster be upgraded to CDH5 before this command is issued. Once issued,
    this command will stop all running services before proceeding.

    If parcels are used instead of CDH system packages then the following
    steps need to happen in order:
    1. Stop all services manually
    2. Activate parcel
    3. Run this upgrade command

    The command will upgrade the services and their configuration to the
    version available in the CDH5 distribution.

    @return: Reference to the submitted command.
    """
    return self._cmd('upgradeServices')

  def enter_maintenance_mode(self):
    """
    Put the cluster in maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('enterMaintenanceMode')
    if cmd.success:
      self._update(get_cluster(self._get_resource_root(), self.name))
    return cmd

  def exit_maintenance_mode(self):
    """
    Take the cluster out of maintenance mode.

    @return: Reference to the completed command.
    @since: API v2
    """
    cmd = self._cmd('exitMaintenanceMode')
    if cmd.success:
      self._update(get_cluster(self._get_resource_root(), self.name))
    return cmd

  def get_all_host_templates(self):
    """
    Retrieves all host templates in the cluster.
    @return ApiList of ApiHostTemplate objects.
    """
    return host_templates.get_all_host_templates(self._get_resource_root(), self.name)

  def get_host_template(self, name):
    """
    Retrieves a host templates by name.
    @param name: Host template name.
    @return An ApiHostTemplate object.
    """
    return host_templates.get_host_template(self._get_resource_root(), name, self.name)

  def create_host_template(self, name):
    """
    Creates a host template.
    @param name: Name of the host template to create.
    @return An ApiHostTemplate object.
    """
    return host_templates.create_host_template(self._get_resource_root(), name, self.name)

  def delete_host_template(self, name):
    """
    Deletes a host template.
    @param name: Name of the host template to delete.
    @return An ApiHostTemplate object.
    """
    return host_templates.delete_host_template(self._get_resource_root(), name, self.name)

  def rolling_restart(self, slave_batch_size=None,
                      slave_fail_count_threshold=None,
                      sleep_seconds=None,
                      stale_configs_only=None,
                      unupgraded_only=None,
                      roles_to_include=None,
                      restart_service_names=None):
    """
    Command to do a "best-effort" rolling restart of the given cluster,
    i.e. it does plain restart of services that cannot be rolling restarted,
    followed by first rolling restarting non-slaves and then rolling restarting
    the slave roles of services that can be rolling restarted. The slave restarts
    are done host-by-host.
    @param: slave_batch_size Number of hosts with slave roles to restart at a time
            Must be greater than 0. Default is 1.
    @param: slave_fail_count_threshold The threshold for number of slave host batches that
            are allowed to fail to restart before the entire command is considered failed.
            Must be >= 0. Default is 0.
    @param: sleep_seconds Number of seconds to sleep between restarts of slave host batches.
            Must be >=0. Default is 0.
    @param: stale_configs_only Restart roles with stale configs only. Default is false.
    @param: unupgraded_only Restart roles that haven't been upgraded yet. Default is false.
    @param: roles_to_include Role types to restart. Default is slave roles only.
    @param: restart_service_names List of specific services to restart.
    @return: Reference to the submitted command.
    @since: API v4
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
    if roles_to_include:
      args['rolesToInclude'] = roles_to_include
    if restart_service_names:
      args['restartServiceNames'] = restart_service_names

    return self._cmd('rollingRestart', data=args, api_version=4)
