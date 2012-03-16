# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import ApiList, BaseApiObject

__docformat__ = "epytext"

HOSTS_PATH = "/hosts"

def create_host(resource_root, host_id, name, ipaddr, rack_id=None):
  """
  Create a host
  @param resource_root: The root Resource object.
  @param host_id: Host id
  @param name: Host name
  @param ipaddr: IP address
  @param rack_id: Rack id. Default None
  @return: An ApiHost object
  """
  apihost = ApiHost(resource_root, host_id, name, ipaddr, rack_id)
  apihost_list = ApiList([apihost])
  body = json.dumps(apihost_list.to_json_dict())
  resp = resource_root.post(HOSTS_PATH, data=body)
  # The server returns a list of created hosts (with size 1)
  return ApiList.from_json_dict(ApiHost, resp, resource_root)[0]

def get_host(resource_root, host_id):
  """
  Lookup a host by id
  @param resource_root: The root Resource object.
  @param host_id: Host id
  @return: An ApiHost object
  """
  dic = resource_root.get("%s/%s" % (HOSTS_PATH, host_id))
  return ApiHost.from_json_dict(dic, resource_root)

def get_all_hosts(resource_root, view=None):
  """
  Get all hosts
  @param resource_root: The root Resource object.
  @return: A list of ApiHost objects.
  """
  dic = resource_root.get(HOSTS_PATH,
          params=view and dict(view=view) or None)
  return ApiList.from_json_dict(ApiHost, dic, resource_root)

def delete_host(resource_root, host_id):
  """
  Delete a host by id
  @param resource_root: The root Resource object.
  @param host_id: Host id
  @return: The deleted ApiHost object
  """
  resp = resource_root.delete("%s/%s" % (HOSTS_PATH, host_id))
  return ApiHost.from_json_dict(resp, resource_root)


class ApiHost(BaseApiObject):
  RO_ATTR = ('status', 'lastHeartbeat', 'roleRefs')
  RW_ATTR = ('hostId', 'hostname', 'ipAddress', 'rackId')

  def __init__(self, resource_root, hostId, hostname,
      ipAddress=None, rackId=None):
    # Note about "ipAddress = None":
    #
    # This generally happens when you bring up SCM and it gets an
    # "optimized" heartbeat from an agent, and you query the host info
    # before it's fully constructed. The JSON returned wouldn't have
    # the ipAddress field, and a TypeError would be raised.
    BaseApiObject.ctor_helper(**locals())
