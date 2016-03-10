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
from sys import api_version

__docformat__ = "epytext"

CLUSTERS_PATH = "/clusters"

def create_cluster(resource_root, name, version=None, fullVersion=None):
  """
  Create a cluster
  @param resource_root: The root Resource object.
  @param name: Cluster name
  @param version: Cluster CDH major version (eg: "CDH4")
                  - The CDH minor version will be assumed to be the
                    latest released version for CDH4, or 5.0 for CDH5.
  @param fullVersion: Cluster's full CDH version. (eg: "5.1.1")
                        - If specified, 'version' will be ignored.
                        - Since: v6
  @return: An ApiCluster object
  """
  if version is None and fullVersion is None:
    raise Exception("Either 'version' or 'fullVersion' must be specified")
  if fullVersion is not None:
    api_version = 6
    version = None
  else:
    api_version = 1

  apicluster = ApiCluster(resource_root, name, version, fullVersion)
  return call(resource_root.post, CLUSTERS_PATH, ApiCluster, True,
              data=[apicluster], api_version=api_version)[0]

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
    'clusterUrl'        : None,
    'version'           : None,
    'fullVersion'       : None,
    'hostsUrl'          : ROAttr(),
    'maintenanceMode'   : ROAttr(),
    'maintenanceOwners' : ROAttr(),
    'entityStatus'      : ROAttr(),
  }

  def __init__(self, resource_root, name=None, version=None, fullVersion=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiCluster>: %s; version: %s" % (self.name, self.version)

  def _path(self):
    return "%s/%s" % (CLUSTERS_PATH, self.name)

  def _put_cluster(self, dic, params=None):
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
    if self._get_resource_root().version < 6:
      dic['name'] = newname
    else:
      dic['displayName'] = newname
    return self._put_cluster(dic)

  def update_cdh_version(self, new_cdh_version):
    """
    Manually set the CDH version.

    @param new_cdh_version: New CDH version, e.g. 4.5.1
    @return: An ApiCluster object
    @since: API v6
    """
    dic = self.to_json_dict()
    dic['fullVersion'] = new_cdh_version
    return self._put_cluster(dic)

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

    @param name: Service name
    @return: The deleted ApiService object
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

  def restart(self, restart_only_stale_services=None,
    redeploy_client_configuration=None,
    restart_service_names=None):
    """
    Restart all services in the cluster.
    Services are restarted in the appropriate order given their dependencies.

    @param restart_only_stale_services: Only restart services that have stale
                                        configuration and their dependent
                                        services. Default is False.
    @param redeploy_client_configuration: Re-deploy client configuration for
                                          all services in the cluster. Default
                                          is False.
    @param restart_service_names: Only restart services that are specified and their dependent services.
                                  Available since API v11.
    @since API v6

    @return: Reference to the submitted command.
    """
    if self._get_resource_root().version < 6:
      return self._cmd('restart')
    else:
      args = dict()
      args['restartOnlyStaleServices'] = restart_only_stale_services
      args['redeployClientConfiguration'] = redeploy_client_configuration
      if self._get_resource_root().version >= 11:
        args['restartServiceNames'] = restart_service_names
      return self._cmd('restart', data=args, api_version=6)

  def deploy_client_config(self):
    """
    Deploys Service client configuration to the hosts on the cluster.

    @return: Reference to the submitted command.
    @since: API v2
    """
    return self._cmd('deployClientConfig')

  def deploy_cluster_client_config(self, hostIds=[]):
    """
    Deploys Cluster client configuration (Kerberos configuration) to the
    hosts on the cluster. Any hosts that are decommissioned or have running
    roles will be skipped.

    @param hostIds: hostIds of hosts to deploy to. If empty, deploys to all
                    hosts in the cluster.
    @return: Reference to the submitted command.
    @since: API v7
    """
    return self._cmd('deployClusterClientConfig', data=hostIds,
      api_version=7)

  def upgrade_services(self):
    """
    This command is no longer recommended with API v6 onwards. It simply does
    not work when parcels are used, and even with packages it may fail due to
    a race. Use upgrade_cdh instead.

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
    @deprecated: since API v6
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
    @return: ApiList of ApiHostTemplate objects.
    """
    return host_templates.get_all_host_templates(self._get_resource_root(), self.name)

  def get_host_template(self, name):
    """
    Retrieves a host templates by name.
    @param name: Host template name.
    @return: An ApiHostTemplate object.
    """
    return host_templates.get_host_template(self._get_resource_root(), name, self.name)

  def create_host_template(self, name):
    """
    Creates a host template.
    @param name: Name of the host template to create.
    @return: An ApiHostTemplate object.
    """
    return host_templates.create_host_template(self._get_resource_root(), name, self.name)

  def delete_host_template(self, name):
    """
    Deletes a host template.
    @param name: Name of the host template to delete.
    @return: An ApiHostTemplate object.
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
    @param slave_batch_size: Number of hosts with slave roles to restart at a time
           Must be greater than 0. Default is 1.
    @param slave_fail_count_threshold: The threshold for number of slave host batches that
           are allowed to fail to restart before the entire command is considered failed.
           Must be >= 0. Default is 0.
    @param sleep_seconds: Number of seconds to sleep between restarts of slave host batches.
           Must be >=0. Default is 0.
    @param stale_configs_only: Restart roles with stale configs only. Default is false.
    @param unupgraded_only: Restart roles that haven't been upgraded yet. Default is false.
    @param roles_to_include: Role types to restart. Default is slave roles only.
    @param restart_service_names: List of specific services to restart.
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

  def rolling_upgrade(self, upgrade_from_cdh_version,
                      upgrade_to_cdh_version,
                      upgrade_service_names,
                      slave_batch_size=None,
                      slave_fail_count_threshold=None,
                      sleep_seconds=None):
    """
    Command to do a rolling upgrade of services in the given cluster

    This command does not handle any services that don't support rolling
    upgrades. The command will throw an error and not start if upgrade of
    any such service is requested.

    This command does not upgrade the full CDH Cluster. You should normally
    use the upgradeCDH Command for upgrading the cluster. This is primarily
    helpful if you need to need to recover from an upgrade failure or for
    advanced users to script an alternative to the upgradeCdhCommand.

    This command expects the binaries to be available on hosts and activated.
    It does not change any binaries on the hosts.

    @param upgrade_from_cdh_version: Current CDH Version of the services.
           Example versions are: "5.1.0", "5.2.2" or "5.4.0"
    @param upgrade_to_cdh_version: Target CDH Version for the services.
           The CDH version should already be present and activated on the nodes.
           Example versions are: "5.1.0", "5.2.2" or "5.4.0"
    @param upgrade_service_names: List of specific services to be upgraded and restarted.
    @param slave_batch_size: Number of hosts with slave roles to restart at a time
           Must be greater than 0. Default is 1.
    @param slave_fail_count_threshold: The threshold for number of slave host batches that
           are allowed to fail to restart before the entire command is considered failed.
           Must be >= 0. Default is 0.
    @param sleep_seconds: Number of seconds to sleep between restarts of slave host batches.
           Must be >=0. Default is 0.

    @return: Reference to the submitted command.
    @since: API v10
    """
    args = dict()
    args['upgradeFromCdhVersion'] = upgrade_from_cdh_version
    args['upgradeToCdhVersion'] = upgrade_to_cdh_version
    args['upgradeServiceNames'] = upgrade_service_names

    if slave_batch_size:
      args['slaveBatchSize'] = slave_batch_size
    if slave_fail_count_threshold:
      args['slaveFailCountThreshold'] = slave_fail_count_threshold
    if sleep_seconds:
      args['sleepSeconds'] = sleep_seconds

    return self._cmd('rollingUpgrade', data=args, api_version=10)

  def auto_assign_roles(self):
    """
    Automatically assign roles to hosts and create the roles for all the services in a cluster.

    Assignments are done based on services in the cluster and hardware specifications.
    Existing roles will be taken into account and their assignments will be not be modified.
    @since: API v6
    """
    self._put("autoAssignRoles", None, api_version=6)

  def auto_configure(self):
    """
    Automatically configures roles and services in a cluster.

    Overwrites some existing configurations. Might create new role config
    groups. Only default role config groups must exist before calling this
    endpoint. Other role config groups must not exist. If they do, an exception
    will be thrown preventing any configuration. Ignores the Cloudera
    Management Service even if colocated with roles of this cluster. To avoid
    over-committing the heap on hosts, assign hosts to this cluster that are
    not being used by the Cloudera Management Service.
    @since: API v6
    """
    self._put("autoConfigure", None, api_version=6)

  def first_run(self):
    """
    Prepare and start services in a cluster.
    Perform all the steps needed to prepare each service in a
    cluster and start the services in order.

    @return: Reference to the submitted command.
    @since: API v7
    """
    return self._cmd('firstRun', None, api_version=7)

  def upgrade_cdh(self, deploy_client_config=True, start_all_services=True,
      cdh_parcel_version=None, cdh_package_version=None,
      rolling_restart=False, slave_batch_size=None, sleep_seconds=None,
      slave_fail_count_threshold=None):
    """
    Perform CDH upgrade to the next major version. In v9+, also supports
    minor CDH 5 upgrades (5.a.b to 5.x.y where x > a) and supports maintenance
    release changes (a.b.x to a.b.y).

    If using packages, CDH packages on all hosts of the cluster must be
    manually upgraded before this command is issued.

    The command will upgrade the services and their configuration to the
    requested version. All running services will be stopped before proceeding,
    unless rolling restart is requested and is available.

    @param deploy_client_config: Whether to deploy client configurations
           after the upgrade. Default is True. Has no effect in v9+;
           client configurations are always deployed.
    @param start_all_services: Whether to start all services after the upgrade.
           Default is True. Has no effect in v9+; services are always
           restarted.
    @param cdh_parcel_version: If upgrading to parcels, the full version of an
           already distributed parcel for the next CDH version. Default
           is None. Example versions are: '5.0.0-1.cdh5.0.0.p0.11' or
           '5.0.2-1.cdh5.0.2.p0.32'.
    @param cdh_package_version: If upgrading to packages, the full version of an
           already installed package for the next CDH version. Default
           is None. Example versions are: '5.2.0' or '4.5.0'. Only available
           since v9.
    @param rolling_restart: If you'd like to do a rolling restart, set this to
           True. Default is False. Only available since v9.
    @param slave_batch_size: Controls the rolling restart slave batch size.
           Only applicable when rolling_restart is True.
    @param sleep_seconds: Controls how many seconds to sleep betweein rolling
           restart batches. Only applicable when rolling_restart is True.
    @param slave_fail_count_threshold: Controls how many slave restart failures
           are tolerated in a rolling restart. Only applicable when
           rolling_restart is True.
    @return: Reference to the submitted command.
    @since: API v6 for major upgrades only, v9 for maintenance and CDH 5 minor
            releases.
    """
    args = dict()
    args['deployClientConfig'] = deploy_client_config
    args['startAllServices'] = start_all_services
    if cdh_parcel_version:
      args['cdhParcelVersion'] = cdh_parcel_version
    if cdh_package_version:
      args['cdhPackageVersion'] = cdh_package_version
    if rolling_restart:
      args['rollingRestartArgs'] = {
          'slaveBatchSize' : slave_batch_size,
          'sleepSeconds' : sleep_seconds,
          'slaveFailCountThreshold' : slave_fail_count_threshold
        }
    return self._cmd('upgradeCdh', data=args, api_version=6)

  def configure_for_kerberos(self, datanode_transceiver_port=None,
    datanode_web_port=None):
    """
    Command to configure the cluster to use Kerberos for authentication.

    This command will configure all relevant services on a cluster for
    Kerberos usage.  This command will trigger a GenerateCredentials command
    to create Kerberos keytabs for all roles in the cluster.

    @param datanode_transceiver_port: The HDFS DataNode transceiver port to use.
           This will be applied to all DataNode role configuration groups. If
           not specified, this will default to 1004.
    @param datanode_web_port: The HDFS DataNode web port to use.  This will be
           applied to all DataNode role configuration groups. If not specified,
           this will default to 1006.
    @return: Reference to the submitted command.
    @since: API v11
    """
    args = dict()
    if datanode_transceiver_port:
        args['datanodeTransceiverPort'] = datanode_transceiver_port
    if datanode_web_port:
        args['datanodeWebPort'] = datanode_web_port
    return self._cmd('configureForKerberos', data=args, api_version=11)

  def export(self, export_auto_config=False):
    """
    Export the cluster template for the given cluster. ccluster must have host
    templates defined. It cluster does not have host templates defined it will
    export host templates based on roles assignment.

    @param export_auto_config: Also export auto configured configs
    @return: Return cluster template
    @since: API v12
    """

    return self._get("export", ApiClusterTemplate, False,
                     params=dict(exportAutoConfig=export_auto_config), api_version=12)

  def pools_refresh(self):
    """
    Refresh Dynamic Pools configurations for relevant services..

    @return: Reference to the submitted command.
    @since: API v6
    """
    return self._cmd('poolsRefresh', api_version=6)

  def list_dfs_services(self, view=None):
    """
    List available DFS (distributed file system) services in a cluster.
    @param view: View to materialize
    @return: List of available distributed file system services in the cluster.
    @since: API v12
    """
    if view:
      return self._get_resource_root().get("%s/%s?view=%s" % (self._path(), 'dfsServices', view))
    else:
      return self._get_resource_root().get("%s/%s" % (self._path(), 'dfsServices'))
