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

class ClouderaManager(BaseApiObject):
  """
  The Cloudera Manager instance.

  Provides access to CM configuration and services.
  """

  def __init__(self, resource_root):
    BaseApiObject.init(self, resource_root)

  def _cmd(self, command, data = None):
    """
    Invokes a global command.

    @param command: Command name.
    @param data: Optional data to send to the command.
    @return Information about the submitted command.
    """
    resp = self._get_resource_root().post("/cm/commands/" + command, data=data)
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

  def get_commands(self, view=None):
    """
    Retrieve a list of running global commands.

    @param view: View to materialize ('full' or 'summary')
    @return: A list of running commands.
    """
    resp = self._get_resource_root().get(
        "/cm/commands",
        params = view and dict(view=view) or None)
    return ApiList.from_json_dict(ApiCommand, resp, self._get_resource_root())

  def create_mgmt_service(self, service_setup_info):
    """
    Setup the Cloudera Management Service.

    @param service_setup_info: ApiServiceSetupInfo object.
    @return: The management service instance.
    """
    body = json.dumps(service_setup_info.to_json_dict())
    resp = self._get_resource_root().put('/cm/service', data=body)
    return ApiService.from_json_dict(resp, self._get_resource_root())

  def get_service(self):
    """
    Return the Cloudera Management Services instance.

    @return: An ApiService instance.
    """
    resp = self._get_resource_root().get('/cm/service')
    return ApiService.from_json_dict(resp, self._get_resource_root())

  def get_license(self):
    """
    Return information about the currently installed license.

    @return: License information.
    """
    resp = self._get_resource_root().get('/cm/license')
    return ApiLicense.from_json_dict(resp, self._get_resource_root())

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
    resp = self._get_resource_root().get('/cm/config',
        params = view and dict(view=view) or None)
    return json_to_config(resp, view == 'full')

  def update_config(self, config):
    """
    Update the CM configuration.

    @param: config Dictionary with configuration to update.
    @return: Dictionary with updated configuration.
    """
    resp = self._get_resource_root().put('/cm/config',
        data = config_to_json(config))
    return json_to_config(resp, False)

  def generate_credentials(self):
    """
    Generate credentials for services configured with Kerberos.

    @return: Information about the submitted command.
    """
    return self._cmd('generateCredentials')

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
    return self._cmd('collectDiagnosticData', data=json.dumps(args))

  def collect_diagnostic_data_45(self, end_datetime, bundle_size_bytes):
    """
    Issue the command to collect diagnostic data.

    @param end_datetime: The end of the collection period. Type datetime.
    @param bundle_size_bytes: The target size for the support bundle in bytes
    """
    args = {
        'endTime': end_datetime.isoformat(),
        'bundleSizeBytes': bundle_size_bytes
    }
    return self._cmd('collectDiagnosticData', data=json.dumps(args))

  def hosts_decommission(self, host_names):
    """
    Decommission the specified hosts by decommissioning the slave roles
    and stopping the remaining ones.

    @param host_names: List of names of hosts to be decommissioned.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsDecommission', data=json.dumps({ApiList.LIST_KEY : host_names}))

  def hosts_recommission(self, host_names):
    """
    Recommission the specified hosts by recommissioning the slave roles.
    This command doesn't start the roles. Use hosts_start_roles for that.

    @param host_names: List of names of hosts to be recommissioned.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsRecommission', data=json.dumps({ApiList.LIST_KEY : host_names}))

  def hosts_start_roles(self, host_names):
    """
    Start all the roles on the specified hosts.

    @param host_names: List of names of hosts on which to start all roles.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsStartRoles', data=json.dumps({ApiList.LIST_KEY : host_names}))

  def create_peer(self, name, url, username, password):
    """
    Create a new peer for replication.

    @param name: The name of the peer.
    @param url: The url of the peer.
    @param username: The admin username to use to setup the remote side of the peer connection.
    @param password: The password of the admin user.
    @return: The newly created peer.
    @since: API v3
    """
    self._require_min_api_version(3)
    body = json.dumps(
        ApiCmPeer(self._get_resource_root(),
                  name=name,
                  url=url,
                  username=username,
                  password=password).to_json_dict())
    resp = self._get_resource_root().post('/cm/peers', data=body)
    return ApiCmPeer.from_json_dict(resp, self._get_resource_root())

  def delete_peer(self, name):
    """
    Delete a replication peer.

    @param name: The name of the peer.
    @return: The deleted peer.
    @since: API v3
    """
    self._require_min_api_version(3)
    resp = self._get_resource_root()\
        .delete("/cm/peers/%s" % ( name, ))
    return ApiCmPeer.from_json_dict(resp, self._get_resource_root())

  def update_peer(self,
      current_name,
      new_name, new_url, username, password):
    """
    Update a replication peer.

    @param current_name: The name of the peer to updated.
    @param new_name: The new name for the peer.
    @param new_url: The new url for the peer.
    @param username: The admin username to use to setup the remote side of the peer connection.
    @param password: The password of the admin user.
    @return: The updated peer.
    @since: API v3
    """
    self._require_min_api_version(3)
    body = json.dumps(
        ApiCmPeer(self._get_resource_root(),
                  name=new_name,
                  url=new_url,
                  username=username,
                  password=password).to_json_dict())
    resp = self._get_resource_root().put('/cm/peers/%s' % (current_name, ), data=body)
    return ApiCmPeer.from_json_dict(resp, self._get_resource_root())

  def get_peers(self):
    """
    Retrieve a list of replication peers.

    @return: A list of replication peers.
    @since: API v3
    """
    self._require_min_api_version(3)
    resp = self._get_resource_root().get("/cm/peers")
    return ApiList.from_json_dict(ApiCmPeer, resp, self._get_resource_root())

  def get_peer(self, name):
    """
    Retrieve a replication peer by name.

    @param name: The name of the peer.
    @return: The peer.
    @since: API v3
    """
    self._require_min_api_version(3)
    resp = self._get_resource_root().get("/cm/peers/%s" % (name, ))
    return ApiCmPeer.from_json_dict(resp, self._get_resource_root())

  def test_peer_connectivity(self, name):
    """
    Test connectivity for a replication peer.

    @param name: The name of the peer to test.
    @return: The command representing the test.
    @since: API v3
    """
    self._require_min_api_version(3)
    resp = self._get_resource_root().post('/cm/peers/%s/commands/test' % (name, ))
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

  def get_all_hosts_config(self, view=None):
    """
    Retrieve the default configuration for all hosts.

    @param view: View to materialize.
    @param view: View to materialize ('full' or 'summary')
    @return: Dictionary with configuration data.
    """
    params = view and dict(view=view) or None
    resp = self._get_resource_root().get('/cm/allHosts/config', params=params)
    return json_to_config(resp, view == 'full')

  def update_all_hosts_config(self, config):
    """
    Update the default configuration for all hosts.

    @param: config Dictionary with configuration to update.
    @return: Dictionary with updated configuration.
    """
    resp = self._get_resource_root().put('/cm/allHosts/config',
        data=config_to_json(config))
    return json_to_config(resp, False)
