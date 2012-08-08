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

from cm_api.endpoints.types import config_to_json, json_to_config, \
    BaseApiObject, ApiCommand, ApiList
from cm_api.endpoints.services import ApiService

class ApiLicense(BaseApiObject):
  """Model for a CM license."""
  RO_ATTR = ('owner', 'uuid', 'expiration')
  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

class ClouderaManager(BaseApiObject):
  """
  The Cloudera Manager instance.

  Provides access to CM configuration and services.
  """

  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

  def _cmd(self, command, data = None):
    """
    Invokes a global command.

    @param command: Command name.
    @param data: Optional data to send to the command.
    @return Information about the submitted command.
    """
    resp = self._get_resource_root().post("/cm/commands/" + command, data=data)
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

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

  def hosts_decommission(self, host_names):
    """
    Decommission the specified hosts by decommissioning the slave roles
    and stopping the remaining ones.

    @param host_names: List of names of hosts to be decommissioned.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsDecommission', data=json.dumps({ApiList.LIST_KEY : host_names}))

  def hosts_start_roles(self, host_names):
    """
    Start all the roles on the specified hosts.

    @param host_names: List of names of hosts on which to start all roles.
    @return: Information about the submitted command.
    @since: API v2
    """
    return self._cmd('hostsStartRoles', data=json.dumps({ApiList.LIST_KEY : host_names}))
