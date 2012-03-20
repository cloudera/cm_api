# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.

import logging
try:
  import json
except ImportError:
  import simplejson as json

from cm_api.http_client import HttpClient, RestException
from cm_api.endpoints import cms, clusters, hosts, tools, users
from cm_api.resource import Resource

__docformat__ = "epytext"

LOG = logging.getLogger(__name__)

API_AUTH_REALM = "Cloudera Manager"


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
               use_tls=False, version=1):
    """
    Creates a Resource object that provides API endpoints.

    @param server_host: The hostname of the Cloudera Manager server.
    @param server_port: The port of the server. Defaults to 7180 (http) or
      7183 (https).
    @param username: Login name.
    @param password: Login password.
    @param use_tls: Whether to use tls (https).
    @param version: API version.
    @return Resource object referring to the root.
    """
    protocol = use_tls and "https" or "http"
    if server_port is None:
      server_port = use_tls and 7183 or 7180
    base_url = "%s://%s:%s/api/v%s" % \
        (protocol, server_host, server_port, version)

    client = HttpClient(base_url, exc_class=ApiException)
    client.set_basic_auth(username, password, API_AUTH_REALM)
    client.set_headers( { "Content-Type" : "application/json" } )
    Resource.__init__(self, client)

  # CMS ops.

  def get_cloudera_manager(self):
    """
    Returns a Cloudera Manager object.
    """
    return cms.ClouderaManager(self)

  # Cluster ops.

  def create_cluster(self, name):
    """
    Create a new cluster.

    @param name Cluster name.
    @return The created cluster.
    """
    return clusters.create_cluster(self, name)

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
    @param view View to materialize ('full' or 'summary').
    @return: A list of ApiCluster objects.
    """
    return clusters.get_all_clusters(self, view)

  def get_cluster(self, name):
    """
    Look up a cluster by name.

    @param name Cluster name.
    @return An ApiCluster object.
    """
    return clusters.get_cluster(self, name)

  # Host ops.

  def create_host(self, host_id, name, ipaddr, rack_id = None):
    """
    Create a host.

    @param host_id  The host id.
    @param name     Host name
    @param ipaddr   IP address
    @param rack_id  Rack id. Default None.
    @return: An ApiHost object
    """
    return hosts.create_host(self, host_id, name, ipaddr, rack_id)

  def delete_host(self, host_id):
    """
    Delete a host by id.

    @param host_id Host id
    @return The deleted ApiHost object
    """
    return hosts.delete_host(self, host_id)

  def get_all_hosts(self, view = None):
    """
    Get all hosts

    @param view View to materialize ('full' or 'summary').
    @return A list of ApiHost objects.
    """
    return hosts.get_all_hosts(self, view)

  def get_host(self, host_id):
    """
    Look up a host by id.

    @param host_id Host id
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

    @param: username Username
    @return: An ApiUser object
    """
    return users.delete_user(self, username)

  # Tools

  def echo(self, message):
    """Have the server echo a message back."""
    return tools.echo(self, message)

  def echo_error(self, message):
    """Generate an error, but we get to set the error message."""
    return tools.echo_error(self, message)


def get_root_resource(server_host, server_port=None,
                      username="admin", password="admin",
                      use_tls=False, version=1):
  """
  See ApiResource.
  """
  return ApiResource(server_host, server_port, username, password, use_tls,
      version)
