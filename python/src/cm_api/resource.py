# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json
import logging

__docformat__ = "epytext"

LOG = logging.getLogger(__name__)

class ResourceException(Exception):
  """
  Any error result from the API is converted into this exception type.
  """
  def __init__(self, error):
    Exception.__init__(self, error)
    self._error = error

  @property
  def message(self):
    return self._error['message']


class Resource(object):
  """
  Encapsulates a resource, and provides actions to invoke on it.
  """
  def __init__(self, path, client):
    """
    @param path: The full path of the resource.
    @param client: A Client object.
    """
    self._path = path.strip('/')
    self._client = client

  def _get_uri(self, relpath):
    if relpath is None:
      return self._path
    return self._path + '/' + relpath.strip('/')

  def _invoke(self, method, relpath=None, **params):
    path = self._get_uri(relpath)
    res = self._client.execute(method, path, **params)
    json_dict = json.loads(res)
    return self._extract_json_value(json_dict)

  def _extract_json_value(self, json_dict):
    if json_dict['success']:
      return json_dict['value']
    raise ResourceException(json_dict['error'])


  def get(self, relpath=None, **params):
    """
    Invoke the GET method on a resource.
    @param relpath: Optional. A relative path to this resource's path.
    @param params: Key-value data.

    @return: A dictionary of the JSON result.
    """
    return self._invoke("GET", relpath, **params)


  def post(self, relpath=None, **params):
    """
    Invoke the POST method on a resource.
    @param relpath: Optional. A relative path to this resource's path.
    @param params: Key-value data.

    @return: A dictionary of the JSON result.
    """
    return self._invoke("POST", relpath, **params)
