# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

import time

__docformat__ = "epytext"


class BaseApiObject(object):
  """
  The BaseApiObject helps with (de)serialization from/to JSON.
  To take advantage of it, the derived class needs to define

  RW_ATTR - A list of mutable attributes
  RO_ATTR - A list of immutable attributes

  The derived class's ctor must take all the RW_ATTR as arguments.
  When de-serializing from JSON, all attributes will be set. Their
  names are taken from the keys in the JSON object.

  Reference objects (e.g. hostRef, clusterRef) are automatically
  deserialized. They can be accessed as attributes.
  """
  RO_ATTR = ( )         # Derived classes should define this
  RW_ATTR = ( )         # Derived classes should define this

  def __init__(self, resource_root, **rw_attrs):
    self._resource_root = resource_root
    for k, v in rw_attrs.items():
      if k not in self.RW_ATTR:
        raise ValueError("Unexpected ctor argument '%s' in %s" %
                         (k, self.__class__.__name__))
      self._setattr(k, v)

  def _get_resource_root(self):
    return self._resource_root

  @staticmethod
  def ctor_helper(self=None, **kwargs):
    """
    Note that we need a kw arg called `self'. The callers typically just
    pass their locals() to us.
    """
    BaseApiObject.__init__(self, **kwargs)

  def _setattr(self, k, v):
    """Set an attribute. We take care of instantiating reference objects."""
    # We play tricks when we notice that the attribute `k' ends with `Ref'.
    # We treat it as a reference object, i.e. another object to be deserialized.
    if isinstance(v, dict) and k.endswith("Ref"):
      # A reference, `v' should be a json dictionary
      cls_name = "Api" + k[0].upper() + k[1:]
      cls = globals()[cls_name]
      v = cls(self._get_resource_root(), **v)
    setattr(self, k, v)

  def to_json_dict(self):
    dic = { }
    for attr in self.RW_ATTR:
      value = getattr(self, attr)
      try:
        # If the value has to_json_dict(), call it
        value = value.to_json_dict()
      except AttributeError, ignored:
        pass
      dic[attr] = value
    return dic

  @classmethod
  def from_json_dict(cls, dic, resource_root):
    rw_dict = { }
    for k, v in dic.items():
      if k in cls.RW_ATTR:
        rw_dict[k] = v
        del dic[k]
    # Construct object based on RW_ATTR
    obj = cls(resource_root, **rw_dict)

    # Initialize all RO_ATTR to be None
    for attr in cls.RO_ATTR:
      obj._setattr(attr, None)

    # Now set the RO_ATTR based on the json
    for k, v in dic.items():
      if k in cls.RO_ATTR:
        obj._setattr(k, v)
      else:
        raise KeyError("Unexpected attribute '%s' in %s json" %
                       (k, cls.__name__))
    return obj


class ApiList(object):
  """A list of some api object"""
  LIST_KEY = "items"

  def __init__(self, objects, count=None):
    self.objects = objects
    if count is None:
      self.count = len(objects)
    else:
      self.count = count

  def to_json_dict(self):
    return { ApiList.LIST_KEY :
            [ x.to_json_dict() for x in self.objects ] }

  def __len__(self):
    return self.objects.__len__()

  def __iter__(self):
    return self.objects.__iter__()

  def __getitem__(self, i):
    return self.objects.__getitem__(i)

  def __getslice(self, i, j):
    return self.objects.__getslice__(i, j)

  @staticmethod
  def from_json_dict(member_cls, dic, resource_root):
    json_list = dic[ApiList.LIST_KEY]
    objects = [ member_cls.from_json_dict(x, resource_root) for x in json_list ]
    return ApiList(objects, dic['count'])


class ApiCommand(BaseApiObject):
  """Information about a command."""
  RW_ATTR = ( )
  RO_ATTR = ('id', 'name', 'startTime', 'endTime', 'active', 'success',
             'resultMessage', 'serviceRef', 'roleRef', 'hostRef',
             'children', 'parent', 'resultDataUrl')

  SERVICE_CMD_PATH = "/clusters/%s/services/%s/commands/%d"
  ROLE_CMD_PATH = "/clusters/%s/services/%s/roles/%s/commands/%d"

  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

  def _setattr(self, k, v):
    if k == 'children' and v is not None:
      v = ApiList.from_json_dict(ApiCommand, v, self._get_resource_root())
    elif k == 'parent' and v is not None:
      v = ApiCommand.from_json_dict(v, self._get_resource_root())
    BaseApiObject._setattr(self, k, v)

  def _getpath(self):
    if self.roleRef:
      if self.roleRef.clusterName:
        return ApiCommand.ROLE_CMD_PATH % (self.roleRef.clusterName,
                                           self.roleRef.serviceName,
                                           self.roleRef.roleName,
                                           self.id)
      else:
        return '/cm/service/roles/%s/commands/%d' % (self.roleRef.roleName,
                                                     self.id)
    elif self.serviceRef:
      if self.serviceRef.clusterName:
        return ApiCommand.SERVICE_CMD_PATH % (self.serviceRef.clusterName,
                                              self.serviceRef.serviceName,
                                              self.id)
      else:
        return '/cm/service/commands/%d' % self.id
    else:
      # Assume global command for now.
      # TODO: fix this when host and cluster commands are added.
      return '/cm/commands/%d' % self.id

  def fetch(self):
    """
    Retrieve updated data about the command from the server.

    @param resource_root: The root Resource object.
    @return: A new ApiCommand object.
    """
    resp = self._get_resource_root().get(self._getpath())
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

  def wait(self, timeout=None):
    """
    Wait for command to finish.

    @param timeout: (Optional) Max amount of time (in seconds) to wait.
    @return: The final ApiCommand object.
    """
    SLEEP_SEC = 5

    if timeout is None:
      deadline = None
    else:
      deadline = time.time() + timeout

    while True:
      cmd = self.fetch()
      if not cmd.active:
        return cmd

      if deadline is not None:
        now = time.time()
        if deadline < now:
          return cmd
        else:
          time.sleep(min(SLEEP_SEC, deadline - now))
      else:
        time.sleep(SLEEP_SEC)


  def abort(self):
    """
    Abort a running command.

    @param resource_root: The root Resource object.
    @return: A new ApiCommand object with the updated information.
    """
    path = self._getpath() + '/abort'
    resp = self._get_resource_root().post(path)
    return ApiCommand.from_json_dict(resp, self._get_resource_root())


#
# Configuration helpers.
#

class ApiConfig(BaseApiObject):
  RW_ATTR = ('name', 'value')
  RO_ATTR = ('required', 'default', 'displayName', 'description')
  def __init__(self, resource_root, name, value = None):
    BaseApiObject.ctor_helper(**locals())


def config_to_api_list(dic):
  """
  Converts a python dictionary into a list containing the proper
  ApiConfig encoding for configuration data.

  @param dic Key-value pairs to convert.
  @return JSON dictionary of an ApiConfig list (*not* an ApiList).
  """
  config = [ ]
  for k, v in dic.iteritems():
    config.append({ 'name' : k, 'value': v })
  return { ApiList.LIST_KEY : config }

def config_to_json(dic):
  """
  Converts a python dictionary into a JSON payload.

  The payload matches the expected "apiConfig list" type used to update
  configuration parameters using the API.

  @param dic Key-value pairs to convert.
  @return String with the JSON-encoded data.
  """
  return json.dumps(config_to_api_list(dic))

def json_to_config(dic, full = False):
  """
  Converts a JSON-decoded config dictionary to a python dictionary.

  When materializing the full view, the values in the dictionary will be
  instances of ApiConfig, instead of strings.

  @param dic JSON-decoded config dictionary.
  @param full Whether to materialize the full view of the config data.
  @return Python dictionary with config data.
  """
  config = { }
  for entry in dic['items']:
    k = entry['name']
    if full:
      config[k] = ApiConfig.from_json_dict(entry, None)
    else:
      config[k] = entry.get('value')
  return config

#
# In order for BaseApiObject to automatically instantiate reference objects,
# it's more convenient for the reference classes to be defined here.
#

class ApiHostRef(BaseApiObject):
  RW_ATTR = ('hostId',)
  def __init__(self, resource_root, hostId):
    BaseApiObject.ctor_helper(**locals())

class ApiServiceRef(BaseApiObject):
  RW_ATTR = ('clusterName', 'serviceName')
  def __init__(self, resource_root, serviceName, clusterName = None):
    BaseApiObject.ctor_helper(**locals())

class ApiClusterRef(BaseApiObject):
  RW_ATTR = ('clusterName',)
  def __init__(self, resource_root, clusterName = None):
    BaseApiObject.ctor_helper(**locals())

class ApiRoleRef(BaseApiObject):
  RW_ATTR = ('clusterName', 'serviceName', 'roleName')
  def __init__(self, resource_root, serviceName, roleName, clusterName = None):
    BaseApiObject.ctor_helper(**locals())
