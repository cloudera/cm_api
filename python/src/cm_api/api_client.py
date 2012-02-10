# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.

import logging
try:
  import json
except ImportError:
  import simplejson as json

from cm_api.http_client import HttpClient, RestException
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


def get_root_resource(server_host, server_port=None,
                      username="admin", password="admin",
                      use_tls=False, version=1):
  """
  @param server_host: The hostname of the Cloudera Manager server.
  @param server_port: The port of the server. Defaults to 7180 (http) or
    7183 (https).
  @param username: Login name.
  @param password: Login password.
  @param use_tls: Whether to use tls (https).
  @param version: API version.
  @return Resource object referring to the root.

  Creates a connect root Resource object.
  """
  protocol = use_tls and "https" or "http"
  if server_port is None:
    server_port = use_tls and 7183 or 7180
  base_url = "%s://%s:%s/api/v%s" % \
      (protocol, server_host, server_port, version)

  client = HttpClient(base_url, exc_class=ApiException)
  client.set_basic_auth(username, password, API_AUTH_REALM)
  client.set_headers( { "Content-Type" : "application/json" } )
  return Resource(client)
