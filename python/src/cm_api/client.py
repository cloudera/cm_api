# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.


import cookielib
import logging
import urllib
import urllib2

__docformat__ = "epytext"

# This is the realm defined in cmf-{config,security}.xml
_API_AUTH_REALM = "Cloudera Manager"

LOG = logging.getLogger(__name__)

class Client(object):
  """
  Basic HTTP client tailored for Cloudera Manager API.
  """
  def __init__(self, server_host, server_port=None,
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

    Creates an HTTP(S) client to connect to the Cloudera Manager API.
    """
    self._username = username
    self._password = password
    if server_port is None:
      if use_tls:
        server_port = 7183
      else:
        server_port = 7180
    self._base_url = 'http%s://%s:%s/api/v%s' % (
      's' if use_tls else '',
      server_host, server_port, version)

    # Make a basic auth handler
    passmgr = urllib2.HTTPPasswordMgrWithDefaultRealm()
    passmgr.add_password(_API_AUTH_REALM, self._base_url, username, password)
    authhandler = urllib2.HTTPBasicAuthHandler(passmgr)

    # Make a cookie processor
    cookiejar = cookielib.CookieJar()

    self._opener = urllib2.build_opener(
      urllib2.HTTPCookieProcessor(cookiejar), authhandler)

  @property
  def base_url(self):
    return self._base_url

  def execute(self, http_method, path, **params):
    """
    Submit an HTTP request.
    @param http_method: GET, POST, PUT, DELETE
    @param path: The path of the resource.
    @param params: Key-value data.

    @rtype: json
    @return: The JSON result of the API call.
    """
    # Prepare URL and params
    param_str = urllib.urlencode(params)
    if http_method == "GET":
      url = "%s/%s?%s" % (self._base_url, path, param_str)
      data = None
    elif http_method == "POST":
      url = "%s/%s" % (self._base_url, path)
      data = param_str
    else:
      raise NotImplementedError("Method type %s not supported" % (http_method,))

    # Setup the request
    request = urllib2.Request(url)
    request.get_method = lambda: http_method
    request.get_data = lambda: data

    # Call it
    LOG.debug("%s %s" % (http_method, url))
    try:
      call = self._opener.open(request)
      resp = call.read()
      LOG.debug("%s Got response: %s" % (http_method, resp))
    except urllib2.HTTPError, ex:
      raise ex
    except Exception, ex:
      raise Exception("Command '%s %s' failed: %s" %
                      (http_method, path, ex))
    return resp
