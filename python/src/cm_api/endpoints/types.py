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

import datetime
import logging
import time

__docformat__ = "epytext"

LOG = logging.getLogger(__name__)

def _encode(value):
  """
  Encode a value into JSON.

  . if the value has a 'to_json_dict' method, call it.
  . if the value is a list, return a list of the encoded elements.
  . if the value is a date, encode it according to the expected API format.
  . otherwise, return the value itself.
  """
  try:
    # If the value has to_json_dict(), call it
    value = value.to_json_dict()
  except AttributeError, ignored:
    pass
  try:
    # Maybe it's a date?
    value = value.strftime("%Y-%m-%dT%H:%M:%S.%fZ")
  except AttributeError, ignored:
    pass

  # If value is a list, encode each element.
  if isinstance(value, list):
    value = [ _encode(x) for x in value ]

  return value

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

  def _update(self, api_obj):
    """Copy state from api_obj to this object."""
    if not isinstance(self, api_obj.__class__):
      raise ValueError(
          "Class %s does not derive from %s; cannot update attributes." %
          (self.__class__, api_obj.__class__))

    for attr in self.RW_ATTR + self.RO_ATTR:
      try:
        val = getattr(api_obj, attr)
        setattr(self, attr, val)
      except AttributeError, ignored:
        pass

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
      v = fix_unicode_kwargs(v)
      v = cls(self._get_resource_root(), **v)
    setattr(self, k, v)

  def to_json_dict(self):
    dic = { }
    for attr in self.RW_ATTR:
      try:
        value = getattr(self, attr)
        dic[attr] = _encode(value)
      except AttributeError, ignored:
        pass
    return dic

  @classmethod
  def from_json_dict(cls, dic, resource_root):
    rw_dict = { }
    for k, v in dic.items():
      if k in cls.RW_ATTR:
        rw_dict[k] = v
        del dic[k]
    # Construct object based on RW_ATTR
    rw_dict = fix_unicode_kwargs(rw_dict)
    obj = cls(resource_root, **rw_dict)

    # Initialize all RO_ATTR to be None
    for attr in cls.RO_ATTR:
      obj._setattr(attr, None)

    # Now set the RO_ATTR based on the json
    for k, v in dic.items():
      if k in cls.RO_ATTR:
        obj._setattr(k, v)
      else:
        LOG.debug("Unexpected attribute '%s' in %s json" %
                  (k, cls.__name__))
    return obj

  def _require_min_api_version(self, version):
    """
    Raise an exception if the version of the api is less than the given version.

    @param version: The minimum required version.
    """
    actual_version = self._get_resource_root().version
    if actual_version < version:
      raise Exception("API version %s is required but %s is in use."
          % (version, actual_version))

class ApiList(object):
  """A list of some api object"""
  LIST_KEY = "items"

  def __init__(self, objects):
    self.objects = objects

  def __str__(self):
    return "<ApiList>(%d): [%s]" % (
        len(self.objects),
        ", ".join([str(item) for item in self.objects]))

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
    return ApiList(objects)


class ApiCommand(BaseApiObject):
  """Information about a command."""
  RW_ATTR = ( )
  RO_ATTR = ('id', 'name', 'startTime', 'endTime', 'active', 'success',
             'resultMessage', 'clusterRef', 'serviceRef', 'roleRef', 'hostRef',
             'children', 'parent', 'resultDataUrl',)
  SYNCHRONOUS_COMMAND_ID = -1

  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

  def __str__(self):
    return "<ApiCommand>: '%s' (id: %s; active: %s; success: %s)" % (
        self.name, self.id, self.active, self.success)

  def _path(self):
    return '/commands/%d' % self.id

  def _setattr(self, k, v):
    if k == 'children' and v is not None:
      v = ApiList.from_json_dict(ApiCommand, v, self._get_resource_root())
    elif k == 'parent' and v is not None:
      v = ApiCommand.from_json_dict(v, self._get_resource_root())
    BaseApiObject._setattr(self, k, v)

  def fetch(self):
    """
    Retrieve updated data about the command from the server.

    @param resource_root: The root Resource object.
    @return: A new ApiCommand object.
    """
    if self.id == ApiCommand.SYNCHRONOUS_COMMAND_ID:
      return self

    resp = self._get_resource_root().get(self._path())
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

  def wait(self, timeout=None):
    """
    Wait for command to finish.

    @param timeout: (Optional) Max amount of time (in seconds) to wait. Wait
                    forever by default.
    @return: The final ApiCommand object, containing the last known state.
             The command may still be running in case of timeout.
    """
    if self.id == ApiCommand.SYNCHRONOUS_COMMAND_ID:
      return self

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
    if self.id == ApiCommand.SYNCHRONOUS_COMMAND_ID:
      return self

    path = self._path() + '/abort'
    resp = self._get_resource_root().post(path)
    return ApiCommand.from_json_dict(resp, self._get_resource_root())

#
# Metrics.
#

class ApiMetricData(BaseApiObject):
  """Metric reading data."""
  RW_ATTR = ( )
  RO_ATTR = ( 'timestamp', 'value' )

  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())


class ApiMetric(BaseApiObject):
  """Metric information."""
  RW_ATTR = ( )
  RO_ATTR = ( 'name', 'context', 'unit', 'data', 'displayName', 'description' )

  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

  def _setattr(self, k, v):
    if k == 'data':
      if v:
        assert isinstance(v, list)
        self.data = [ ApiMetricData.from_json_dict(x, self._get_resource_root()) for x in v ]
    else:
      BaseApiObject._setattr(self, k, v)

#
# Activities.
#

class ApiActivity(BaseApiObject):
  RW_ATTR = ( )
  RO_ATTR = ( 'name', 'type', 'parent', 'startTime', 'finishTime', 'id',
      'status', 'user', 'group', 'inputDir', 'outputDir', 'mapper', 'combiner',
      'reducer', 'queueName', 'schedulerPriority' )
  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

  def __str__(self):
    return "<ApiActivity>: %s (%s)" % (self.name, self.status)

#
# Replication
#

class ApiCmPeer(BaseApiObject):
  RW_ATTR = ( 'name', 'url', 'username', 'password' )
  RO_ATTR = ( )

  def __repr__(self):
    return "<ApiPeer>: %s (%s)" % (self.name, self.url)

class ApiReplicationSchedule(BaseApiObject):
  RW_ATTR = ( 'startTime', 'endTime', 'interval', 'intervalUnit', 'paused',
      'hdfsArguments', 'hiveArguments', 'alertOnStart', 'alertOnSuccess',
      'alertOnFail', 'alertOnAbort' )
  RO_ATTR = ( 'id', 'nextRun', 'history' )

  def _setattr(self, name, value):
    DATES = ( 'startTime', 'endTime', 'nextRun' )
    if name in DATES and value and not isinstance(value, datetime.datetime):
      setattr(self, name, datetime.datetime.strptime(value, "%Y-%m-%dT%H:%M:%S.%fZ"))
    elif name == 'hdfsArguments' and isinstance(value, dict):
      self.hdfsArguments = ApiHdfsReplicationArguments(
          self._get_resource_root(), **value)
    elif name == 'hiveArguments' and isinstance(value, dict):
      self.hiveArguments = ApiHiveReplicationArguments(
          self._get_resource_root(), **value)
    else:
      super(ApiReplicationSchedule, self)._setattr(name, value)

class ApiHdfsReplicationArguments(BaseApiObject):
  RW_ATTR = ( 'sourceService', 'sourcePath', 'destinationPath',
      'mapreduceServiceName', 'userName', 'numMaps',
      'dryRun', 'schedulerPoolName', 'abortOnError', 'preservePermissions',
      'preserveBlockSize', 'preserveReplicationCount', 'removeMissingFiles',
      'skipChecksumChecks' )
  RO_ATTR = ( )

  def _setattr(self, name, value):
    if name == "sourceService" and isinstance(value, dict):
      self.sourceService = ApiServiceRef(self._get_resource_root(), **value)
    else:
      super(ApiHdfsReplicationArguments, self)._setattr(name, value)

class ApiHiveTable(BaseApiObject):
  RW_ATTR = ('database', 'tableName')
  RO_ATTR = ( )

  def __str__(self):
    return "<ApiHiveTable>: %s, %s" % (self.database, self.tableName)

class ApiHiveReplicationArguments(BaseApiObject):
  RW_ATTR = ('sourceService', 'tableFilters', 'exportDir', 'force',
      'replicateData', 'hdfsArguments', 'dryRun')
  RO_ATTR = ( )

  def _setattr(self, name, value):
    if name == "sourceService" and isinstance(value, dict):
      self.sourceService = ApiServiceRef(self._get_resource_root(), **value)
    elif name == 'hdfsArguments' and isinstance(value, dict):
      self.hdfsArguments = ApiHdfsReplicationArguments(self._get_resource_root(),
          **value)
    elif name == 'tableFilters' and isinstance(value, list):
      self.tableFilters = [ ApiHiveTable(self._get_resource_root(), **x) for x in value ]
    else:
      super(ApiHiveReplicationArguments, self)._setattr(name, value)

#
# Configuration helpers.
#

class ApiConfig(BaseApiObject):
  RW_ATTR = ('name', 'value')
  RO_ATTR = ('required', 'default', 'displayName', 'description',
      'relatedName', 'validationState', 'validationMessage')
  def __init__(self, resource_root, name, value = None):
    BaseApiObject.ctor_helper(**locals())

  def __str__(self):
    return "<ApiConfig>: %s = %s" % (self.name, self.value)

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

def fix_unicode_kwargs(dic):
  """
  This works around http://bugs.python.org/issue2646
  We use unicode strings as keys in kwargs.
  """
  res = { }
  for k, v in dic.iteritems():
    res[str(k)] = v
  return res


#
# In order for BaseApiObject to automatically instantiate reference objects,
# it's more convenient for the reference classes to be defined here.
#

class ApiHostRef(BaseApiObject):
  RW_ATTR = ('hostId',)
  def __init__(self, resource_root, hostId):
    BaseApiObject.ctor_helper(**locals())
  
  def __str__(self):
    return "<ApiHostRef>: %s" % (self.hostId)

class ApiServiceRef(BaseApiObject):
  RW_ATTR = ('clusterName', 'serviceName', 'peerName')
  def __init__(self, resource_root, serviceName, clusterName = None, peerName = None):
    BaseApiObject.ctor_helper(**locals())

class ApiClusterRef(BaseApiObject):
  RW_ATTR = ('clusterName',)
  def __init__(self, resource_root, clusterName = None):
    BaseApiObject.ctor_helper(**locals())

class ApiRoleRef(BaseApiObject):
  RW_ATTR = ('clusterName', 'serviceName', 'roleName')
  def __init__(self, resource_root, serviceName, roleName, clusterName = None):
    BaseApiObject.ctor_helper(**locals())

class ApiRoleConfigGroupRef(BaseApiObject):
  RW_ATTR = ('roleConfigGroupName',)
  def __init__(self, resource_root, roleConfigGroupName):
    BaseApiObject.ctor_helper(**locals())
