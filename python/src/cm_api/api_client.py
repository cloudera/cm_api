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

import logging
try:
  import json
except ImportError:
  import simplejson as json

from cm_api.http_client import HttpClient, RestException
from cm_api.endpoints import batch, cms, clusters, events, hosts, external_accounts, tools
from cm_api.endpoints import types, users, timeseries
from cm_api.resource import Resource

__docformat__ = "epytext"

LOG = logging.getLogger(__name__)

API_AUTH_REALM = "Cloudera Manager"
API_CURRENT_VERSION = 14

class ApiException(RestException):
  """
  Any error result from the API is converted into this exception type.
  This handles errors from the HTTP level as well as the API level.
  """
  def __init__(self, error):
    # The parent class will set up _code and _message
    RestException.__init__(self, error)
    try:
      # See if the body is json
      json_body = json.loads(self._message)
      self._message = json_body['message']
    except (ValueError, KeyError):
      pass    # Ignore json parsing error


class ApiResource(Resource):
  """
  Resource object that provides methods for managing the top-level API resources.
  """

  def __init__(self, server_host, server_port=None,
               username="admin", password="admin",
               use_tls=False, version=API_CURRENT_VERSION):
    """
    Creates a Resource object that provides API endpoints.

    @param server_host: The hostname of the Cloudera Manager server.
    @param server_port: The port of the server. Defaults to 7180 (http) or
      7183 (https).
    @param username: Login name.
    @param password: Login password.
    @param use_tls: Whether to use tls (https).
    @param version: API version.
    @return: Resource object referring to the root.
    """
    self._version = version
    protocol = use_tls and "https" or "http"
    if server_port is None:
      server_port = use_tls and 7183 or 7180
    base_url = "%s://%s:%s/api/v%s" % \
        (protocol, server_host, server_port, version)

    client = HttpClient(base_url, exc_class=ApiException)
    client.set_basic_auth(username, password, API_AUTH_REALM)
    client.set_headers( { "Content-Type" : "application/json" } )
    Resource.__init__(self, client)

  @property
  def version(self):
    """
    Returns the API version (integer) being used.
    """
    return self._version

  # CMS ops.

  def get_cloudera_manager(self):
    """
    Returns a Cloudera Manager object.
    """
    return cms.ClouderaManager(self)

  # Cluster ops.

  def create_cluster(self, name, version=None, fullVersion=None):
    """
    Create a new cluster.

    @param name: Cluster name.
    @param version: Cluster major CDH version, e.g. 'CDH5'. Ignored if
      fullVersion is specified.
    @param fullVersion: Complete CDH version, e.g. '5.1.2'. Overrides major
      version if both specified.
    @return: The created cluster.
    """
    return clusters.create_cluster(self, name, version, fullVersion)

  def delete_cluster(self, name):
    """
    Delete a cluster by name.

    @param name: Cluster name
    @return: The deleted ApiCluster object
    """
    return clusters.delete_cluster(self, name)

  def get_all_clusters(self, view = None):
    """
    Retrieve a list of all clusters.
    @param view: View to materialize ('full' or 'summary').
    @return: A list of ApiCluster objects.
    """
    return clusters.get_all_clusters(self, view)

  def get_cluster(self, name):
    """
    Look up a cluster by name.

    @param name: Cluster name.
    @return: An ApiCluster object.
    """
    return clusters.get_cluster(self, name)

  # Host ops.

  def create_host(self, host_id, name, ipaddr, rack_id = None):
    """
    Create a host.

    @param host_id:  The host id.
    @param name:     Host name
    @param ipaddr:   IP address
    @param rack_id:  Rack id. Default None.
    @return: An ApiHost object
    """
    return hosts.create_host(self, host_id, name, ipaddr, rack_id)

  def delete_host(self, host_id):
    """
    Delete a host by id.

    @param host_id: Host id
    @return: The deleted ApiHost object
    """
    return hosts.delete_host(self, host_id)

  def get_all_hosts(self, view = None):
    """
    Get all hosts

    @param view: View to materialize ('full' or 'summary').
    @return: A list of ApiHost objects.
    """
    return hosts.get_all_hosts(self, view)

  def get_host(self, host_id):
    """
    Look up a host by id.

    @param host_id: Host id
    @return: An ApiHost object
    """
    return hosts.get_host(self, host_id)

  # Users

  def get_all_users(self, view = None):
    """
    Get all users.

    @param view: View to materialize ('full' or 'summary').
    @return: A list of ApiUser objects.
    """
    return users.get_all_users(self, view)

  def get_user(self, username):
    """
    Look up a user by username.

    @param username: Username to look up
    @return: An ApiUser object
    """
    return users.get_user(self, username)

  def create_user(self, username, password, roles):
    """
    Create a user.

    @param username: Username
    @param password: Password
    @param roles: List of roles for the user. This should be [] for a
                  regular user, or ['ROLE_ADMIN'] for an admin.
    @return: An ApiUser object
    """
    return users.create_user(self, username, password, roles)

  def delete_user(self, username):
    """
    Delete user by username.

    @param username: Username
    @return: An ApiUser object
    """
    return users.delete_user(self, username)

  def update_user(self, user):
    """
    Update user with the supplied new user object.

    @param user: ApiUser object to be applied
    @return: An ApiUser object
    """
    return users.update_user(self, user)

  # Events

  def query_events(self, query_str = None):
    """
    Query events.
    @param query_str: Query string.
    @return: A list of ApiEvent.
    """
    return events.query_events(self, query_str)

  def get_event(self, event_id):
    """
    Retrieve a particular event by ID.
    @param event_id: The event ID.
    @return: An ApiEvent.
    """
    return events.get_event(self, event_id)

  # Tools

  def echo(self, message):
    """Have the server echo a message back."""
    return tools.echo(self, message)

  def echo_error(self, message):
    """Generate an error, but we get to set the error message."""
    return tools.echo_error(self, message)

  # Metrics

  def get_metrics(self, path, from_time, to_time, metrics, view, params=None):
    """
    Generic function for querying metrics.

    @param from_time: A datetime; start of the period to query (optional).
    @param to_time: A datetime; end of the period to query (default = now).
    @param metrics: List of metrics to query (default = all).
    @param view: View to materialize ('full' or 'summary')
    @param params: Other query parameters.
    @return: List of metrics and their readings.
    """
    if not params:
      params = { }
    if from_time:
      params['from'] = from_time.isoformat()
    if to_time:
      params['to'] = to_time.isoformat()
    if metrics:
      params['metrics'] = metrics
    if view:
      params['view'] = view
    resp = self.get(path, params=params)
    return types.ApiList.from_json_dict(resp, self, types.ApiMetric)

  def query_timeseries(self, query, from_time=None, to_time=None, by_post=False):
    """
    Query time series.
    @param query: Query string.
    @param from_time: Start of the period to query (optional).
    @param to_time: End of the period to query (default = now).
    @return: A list of ApiTimeSeriesResponse.
    """
    return timeseries.query_timeseries(self, query, from_time, to_time, by_post=by_post)

  def get_metric_schema(self):
    """
    Get the schema for all of the metrics.
    @return: A list of ApiMetricSchema.
    """
    return timeseries.get_metric_schema(self)

  # Batch

  def do_batch(self, elements):
    """
    Execute a batch request with one or more elements. If any element fails,
    the entire request is rolled back and subsequent elements are ignored.
    @param elements: A list of ApiBatchRequestElements
    @return: 2-tuple (overall success, list of ApiBatchResponseElements).
    """
    return batch.do_batch(self, elements)

  def get_supported_external_account_categories(self):
    """
    Lookup all supported categories.
    @return: An ApiExternalAcccountCategory list
    """
    return external_accounts.get_supported_categories(self)

  def get_supported_external_account_types(self, category_name):
    """
    Lookup all supported types in a category.
    @param category_name: The category name
    @return: An ApiExternalAcccountType list
    """
    return external_accounts.get_supported_types(self, category_name)

  def create_external_account(self, name, display_name, type_name,
                              account_configs=None):
    """
    Create an external account
    @param name: Immutable external account name
    @param display_name: Display name
    @param type_name: Account type
    @param account_configs: Optional account configuration
    @return: An ApiExternalAccount object
    """
    return external_accounts.create_external_account(
      self, name, display_name, type_name, account_configs)


  def get_external_account(self, name, view=None):
    """
    Lookup an external account by name
    @param name: Account name
    @param view: View
    @return: An ApiExternalAccount object
    """
    return external_accounts.get_external_account(
      self, name, view)


  def get_external_account_by_display_name(
    self, display_name, view=None):
    """
    Lookup an external account by display name
    @param display_name: Account display name
    @param view: View
    @return: An ApiExternalAccount object
    """
    return external_accounts.get_external_account_by_display_name(
      self, display_name, view)

  def get_all_external_accounts(self, type_name, view=None):
    """
    Lookup all external accounts of a particular type, by type name.
    @param type_name: Type name
    @param view: View
    @return: A list of ApiExternalAccount objects.
    """
    return external_accounts.get_all_external_accounts(
      self, type_name, view)

  def update_external_account(self, account):
    """
    Update an external account
    @param account: Account to update, account name must be specified.
    @return: An ApiExternalAccount object
    """
    return external_accounts.update_external_account(self, account)

  def delete_external_account(self, name):
    """
    Delete an external account by name
    @param name: Account name
    @return: An ApiExternalAccount object
    """
    return external_accounts.delete_external_account(self, name)

def get_root_resource(server_host, server_port=None,
                      username="admin", password="admin",
                      use_tls=False, version=API_CURRENT_VERSION):
  """
  See ApiResource.
  """
  return ApiResource(server_host, server_port, username, password, use_tls,
      version)
