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
from cm_api.endpoints.services import ApiService

class ApiLicense(BaseApiObject):
  """Model for a CM license."""
  _ATTRIBUTES = {
    'owner'       : ROAttr(),
    'uuid'        : ROAttr(),
    'expiration'  : ROAttr(),
  }

  def __init__(self, resource_root):
    BaseApiObject.init(self, resource_root)

class ClouderaManager(BaseApiResource):
  """
  The Cloudera Manager instance.

  Provides access to CM configuration and services.
  """

  def __init__(self, resource_root):
    BaseApiObject.init(self, resource_root)

  def _path(self):
    return '/cm'

  def get_commands(self, view=None):
    """
    Retrieve a list of running global commands.

    @param view: View to materialize ('full' or 'summary')
    @return: A list of running commands.
    """
    return self._get("commands", ApiCommand, True,
        params = view and dict(view=view) or None)

  def create_mgmt_service(self, service_setup_info):
    """
    Setup the Cloudera Management Service.

    @param service_setup_info: ApiServiceSetupInfo object.
    @return: The management service instance.
    """
    return self._put("service", ApiService, data=service_setup_info)

  def delete_mgmt_service(self):
    """
    Delete the Cloudera Management Service.

    @return: The deleted management service instance.
    """
    return self._delete("service", ApiService, api_version=6)

  def get_service(self):
    """
    Return the Cloudera Management Services instance.

    @return: An ApiService instance.
    """
    return self._get("service", ApiService)

  def get_license(self):
    """
    Return information about the currently installed license.

    @return: License information.
    """
    return self._get("license", ApiLicense)

  def update_license(self, license_text):
    """
    Install or update the Cloudera Manager license.

    @param license_text: the license in text form
    """
    content = (
        '--MULTI_BOUNDARY',
        'Content-Disposition: form-data; name="license"',
        '',
        license_text,
        '--MULTI_BOUNDARY--',
        '')
    resp = self._get_resource_root().post('cm/license',
        data="\r\n".join(content),
        contenttype='multipart/form-data; boundary=MULTI_BOUNDARY')
    return ApiLicense.from_json_dict(resp, self._get_resource_root())

  def get_config(self, view = None):
    """
    Retrieve the Cloudera Manager configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return: Dictionary with configuration data.
    """
    return self._get_config("config", view)

  def update_config(self, config):
    """
    Update the CM configuration.

    @param config: Dictionary with configuration to update.
    @return: Dictionary with updated configuration.
    """
    return self._update_config("config", config)

  def generate_credentials(self):
    """
    Generate credentials for services configured with Kerberos.

    @return: Information about the submitted command.
    """
    return self._cmd('generateCredentials')

  def import_admin_credentials(self, username, password):
    """
    Imports the KDC Account Manager credentials needed by Cloudera
    Manager to create kerberos principals needed by CDH services.

    @param username Username of the Account Manager. Full name including the Kerberos
           realm must be specified.
    @param password Password for the Account Manager.

    @return: Information about the submitted command.

    @since API v7
    """
    return self._cmd('importAdminCredentials', params=dict(username=username, password=password))

  def get_licensed_feature_usage(self):
    """
    Retrieve a summary of licensed feature usage.

    This command will return information about what Cloudera Enterprise
    licensed features are in use in the clusters being managed by this Cloudera
    Manager, as well as totals for usage across all clusters.

    The specific features described can vary between different versions of
    Cloudera Manager.

    Available since API v6.
    """
    return self._get('getLicensedFeatureUsage',
                     ret_type=ApiLicensedFeatureUsage,
                     ret_is_list=False,
                     api_version=6)

  def inspect_hosts(self):
    """
    Runs the host inspector on the configured hosts.

    @return: Information about the submitted command.
    """
    return self._cmd('inspectHosts')

  def collect_diagnostic_data(self, start_datetime, end_datetime, includeInfoLog=False):
    """
    This method is deprecated as of CM 4.5.
    You should use collect_diagnostic_data_45.
    Issue the command to collect diagnostic data.

    @param start_datetime: The start of the collection period. Type datetime.
    @param end_datetime: The end of the collection period. Type datetime.
    @param includeInfoLog: Whether to include INFO level log messages.
    """
    args = {
        'startTime': start_datetime.isoformat(),
        'endTime': end_datetime.isoformat(),
        'includeInfoLog': includeInfoLog,
    }
    # This method is deprecated as of CM API version 3 which was introduced
    # in CM 4.5.
    return self._cmd('collectDiagnosticData', data=args, api_version=2)

  def collect_diagnostic_data_45(self, end_datetime, bundle_size_bytes, cluster_name=None,
                                 roles=None, collect_metrics=False, start_datetime=None):
    """
    Issue the command to collect diagnostic data.
    If start_datetime is specified, diagnostic data is collected for the entire period between
    start_datetime and end_datetime provided that bundle size is less than or equal to
    bundle_size_bytes. Diagnostics data collection fails if the bundle size is greater than
    bundle_size_bytes.

    If start_datetime is not specified, diagnostic data is collected starting from end_datetime
    and collecting backwards upto a maximum of bundle_size_bytes.

    @param end_datetime: The end of the collection period. Type datetime.
    @param bundle_size_bytes: The target size for the support bundle in bytes
    @param cluster_name: The cluster to collect or None for all clusters
    @param roles: Role ids of roles to restrict log and metric collection to. Valid since v10.
    @param collect_metrics: Whether to collect metrics for viewing as charts. Valid since v13.
    @param start_datetime: The start of the collection period. Type datetime. Valid since v13.
    """
    args = {
        'endTime': end_datetime.isoformat(),
        'bundleSizeBytes': bundle_size_bytes,
        'clusterName': cluster_name
    }
    if self._get_resource_root().version >= 10:
      args['roles'] = roles
    if self._get_resource_root().version >= 13:
      args['enableMonitorMetricsCollection'] = collect_metrics
      if start_datetime is not None:
          args['startTime'] = start_datetime.isoformat()
    return self._cmd('collectDiagnosticData', data=args)

  def hosts_decommission(self, host_names):
    """
    Decommission the specified hosts by decommissioning the slave roles
    and stopping the remaining ones.

    @param host_names: List of names of hosts to be decommissioned.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsDecommission', data=host_names)

  def hosts_recommission(self, host_names):
    """
    Recommission the specified hosts by recommissioning the slave roles.
    This command doesn't start the roles. Use hosts_start_roles for that.

    @param host_names: List of names of hosts to be recommissioned.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsRecommission', data=host_names)

  def hosts_start_roles(self, host_names):
    """
    Start all the roles on the specified hosts.

    @param host_names: List of names of hosts on which to start all roles.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsStartRoles', data=host_names)

  def create_peer(self, name, url, username, password, peer_type="REPLICATION"):
    """
    Create a new peer for replication.

    @param name: The name of the peer.
    @param url: The url of the peer.
    @param username: The admin username to use to setup the remote side of the peer connection.
    @param password: The password of the admin user.
    @param peer_type: Added in v11. The type of the peer. Defaults to 'REPLICATION'.
    @return: The newly created peer.
    @since: API v3
    """
    if self._get_resource_root().version < 11:
      peer_type = None
    peer = ApiCmPeer(self._get_resource_root(),
        name=name,
        url=url,
        username=username,
        password=password,
        type=peer_type)
    return self._post("peers", ApiCmPeer, data=peer, api_version=3)

  def _get_peer_type_param(self, peer_type):
    """
    Checks if the resource_root's API version is >= 11 and construct type param.
    """
    params = None
    if self._get_resource_root().version >= 11:
      params = {
        'type':   peer_type,
      }
    return params

  def delete_peer(self, name, peer_type="REPLICATION"):
    """
    Delete a replication peer.

    @param name: The name of the peer.
    @param peer_type: Added in v11. The type of the peer. Defaults to 'REPLICATION'.
    @return: The deleted peer.
    @since: API v3
    """
    params = self._get_peer_type_param(peer_type)
    return self._delete("peers/" + name, ApiCmPeer, params=params, api_version=3)

  def update_peer(self,
      current_name,
      new_name, new_url, username, password, peer_type="REPLICATION"):
    """
    Update a replication peer.

    @param current_name: The name of the peer to updated.
    @param new_name: The new name for the peer.
    @param new_url: The new url for the peer.
    @param username: The admin username to use to setup the remote side of the peer connection.
    @param password: The password of the admin user.
    @param peer_type: Added in v11. The type of the peer. Defaults to 'REPLICATION'.
    @return: The updated peer.
    @since: API v3
    """
    if self._get_resource_root().version < 11:
      peer_type = None
    peer = ApiCmPeer(self._get_resource_root(),
        name=new_name,
        url=new_url,
        username=username,
        password=password,
        type=peer_type)
    return self._put("peers/" + current_name, ApiCmPeer, data=peer, api_version=3)

  def get_peers(self):
    """
    Retrieve a list of replication peers.

    @return: A list of replication peers.
    @since: API v3
    """
    return self._get("peers", ApiCmPeer, True, api_version=3)

  def get_peer(self, name, peer_type="REPLICATION"):
    """
    Retrieve a replication peer by name.

    @param name: The name of the peer.
    @param peer_type: Added in v11. The type of the peer. Defaults to 'REPLICATION'.
    @return: The peer.
    @since: API v3
    """
    params = self._get_peer_type_param(peer_type)
    return self._get("peers/" + name, ApiCmPeer, params=params, api_version=3)

  def test_peer_connectivity(self, name, peer_type="REPLICATION"):
    """
    Test connectivity for a replication peer.

    @param name: The name of the peer to test.
    @param peer_type: Added in v11. The type of the peer to test. Defaults to 'REPLICATION'.
    @return: The command representing the test.
    @since: API v3
    """
    params = self._get_peer_type_param(peer_type)
    return self._post("peers/%s/commands/test" % name, ApiCommand, params=params,
        api_version=3)

  def get_all_hosts_config(self, view=None):
    """
    Retrieve the default configuration for all hosts.

    @param view: View to materialize.
    @param view: View to materialize ('full' or 'summary')
    @return: Dictionary with configuration data.
    """
    return self._get_config("allHosts/config", view)

  def update_all_hosts_config(self, config):
    """
    Update the default configuration for all hosts.

    @param config: Dictionary with configuration to update.
    @return: Dictionary with updated configuration.
    """
    return self._update_config("allHosts/config", config)

  def auto_assign_roles(self):
    """
    Automatically assign roles to hosts and create the roles for the Cloudera
    Management Service.

    Assignments are done based on number of hosts in the deployment and hardware
    specifications. Existing roles will be taken into account and their
    assignments will be not be modified. The deployment should not have any
    clusters when calling this endpoint. If it does, an exception will be thrown
    preventing any role assignments.
    @since: API v6
    """
    self._put("service/autoAssignRoles", None, api_version=6)

  def auto_configure(self):
    """
    Automatically configures roles of the Cloudera Management Service.

    Overwrites some existing configurations. Only default role config groups
    must exist before calling this endpoint. Other role config groups must not
    exist. If they do, an exception will be thrown preventing any
    configuration. Ignores any clusters (and their services and roles)
    colocated with the Cloudera Management Service. To avoid over-committing
    the heap on hosts, place the Cloudera Management Service roles on machines
    not used by any of the clusters.
    @since: API v6
    """
    self._put("service/autoConfigure", None, api_version=6)

  def host_install(self, user_name, host_names, ssh_port=None, password=None,
          private_key=None, passphrase=None, parallel_install_count=None,
          cm_repo_url=None, gpg_key_custom_url=None,
          java_install_strategy=None, unlimited_jce=None):
    """
    Install Cloudera Manager Agent on a set of hosts.

    @param user_name: The username used to authenticate with the hosts. Root access
                      to your hosts is required to install Cloudera packages. The
                      installer will connect to your hosts via SSH and log in either
                      directly as root or as another user with password-less sudo
                      privileges to become root.
    @param host_names: List of names of hosts to configure for use with
                       Cloudera Manager. A host may be specified by a
                       hostname(FQDN) or an IP address.
    @param ssh_port: SSH port. If unset, defaults to 22.
    @param password: The password used to authenticate with the hosts. Specify
                     either this or a private key. For password-less login, use
                     an empty string as password.
    @param private_key: The private key to authenticate with the hosts. Specify
                        either this or a password.
    @param passphrase: The passphrase associated with the private key used to
                       authenticate with the hosts (optional).
    @param parallel_install_count: Number of simultaneous installations.
                                   Defaults to 10. Running a large number of
                                   installations at once can consume large amounts
                                   of network bandwidth and other system resources.
    @param cm_repo_url: The Cloudera Manager repository URL to use (optional).
                        Example for SLES, Redhat or other RPM based distributions:
                        http://archive-primary.cloudera.com/cm5/redhat/6/x86_64/cm/5/
                        Example for Ubuntu or other Debian based distributions:
                        "deb http://archive.cloudera.com/cm5/ubuntu/lucid/amd64/cm/ lucid-cm5 contrib"
    @param gpg_key_custom_url: The Cloudera Manager public GPG key (optional).
                               Example for SLES, Redhat or other RPM based distributions:
                               http://archive-primary.cloudera.com/cm5/redhat/6/x86_64/cm/RPM-GPG-KEY-cloudera
                               Example for Ubuntu or other Debian based distributions:
                               http://archive.cloudera.com/debian/archive.key
    @param java_install_strategy: Added in v8: Strategy to use for JDK installation. Valid values are 1.
                                  AUTO (default): Cloudera Manager will install the JDK versions that are
                                  required when the "AUTO" option is selected. Cloudera Manager may
                                  overwrite any of the existing JDK installations. 2. NONE: Cloudera
                                  Manager will not install any JDK when "NONE" option is selected. It
                                  should be used if an existing JDK installation has to be used.
    @param unlimited_jce: Added in v8: Flag for unlimited strength JCE policy files installation If
                          unset, defaults to false
    @return: Information about the submitted command.
    @since: API v6
    """
    host_install_args = {}
    if user_name:
     host_install_args['userName'] = user_name
    if host_names:
      host_install_args['hostNames'] = host_names
    if ssh_port:
     host_install_args['sshPort'] = ssh_port
    if password:
     host_install_args['password'] = password
    if private_key:
     host_install_args['privateKey'] = private_key
    if passphrase:
     host_install_args['passphrase'] = passphrase
    if parallel_install_count:
     host_install_args['parallelInstallCount'] = parallel_install_count
    if cm_repo_url:
     host_install_args['cmRepoUrl'] = cm_repo_url
    if gpg_key_custom_url:
     host_install_args['gpgKeyCustomUrl'] = gpg_key_custom_url
    if java_install_strategy is not None:
     host_install_args['javaInstallStrategy'] = java_install_strategy
    if unlimited_jce:
     host_install_args['unlimitedJCE'] = unlimited_jce
    return self._cmd('hostInstall', data=host_install_args)

  def begin_trial(self):
    """
    Begin the trial license for this Cloudera Manager instance.

    This allows the user to have enterprise-level features for a 60-day trial
    period.

    @since: API v6
    """
    self._post("trial/begin", None, api_version=6)

  def end_trial(self):
    """
    End the trial license for this Cloudera Manager instance.

    @since: API v6
    """
    self._post("trial/end", None, api_version=6)

  def import_cluster_template(self, api_cluster_template, add_repositories=False):
    """
    Create a cluster according to the provided template

    @param api_cluster_template: cluster template to import
    @param add_repositories: if true the parcels repositories in the cluster template will be added.
    @return: Command handing cluster import
    @since: API v12
    """
    return self._post("importClusterTemplate", ApiCommand, False, api_cluster_template, params=dict(addRepositories=add_repositories), api_version=12)
