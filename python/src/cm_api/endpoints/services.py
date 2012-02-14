# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import ApiList, BaseApiObject

__docformat__ = "epytext"

SERVICES_PATH = "/clusters/%s/services"

def create_service(resource_root, name, service_type, version,
                   cluster_name="default"):
  """
  Create a service
  @param resource_root: The root Resource object.
  @param name: Service name
  @param service_type: Service type
  @param version: Service version
  @param cluster_name: Cluster name
  @return: An ApiService object
  """
  apiservice = ApiService(name, service_type, version)
  apiservice_list = ApiList([apiservice])
  body = json.dumps(apiservice_list.to_json_dict())
  resp = resource_root.post(SERVICES_PATH % (cluster_name,), data=body)
  # The server returns a list of created services (with size 1)
  return ApiList.from_json_dict(ApiService, resp)[0]

def get_service(resource_root, name, cluster_name="default"):
  """
  Lookup a service by name
  @param resource_root: The root Resource object.
  @param name: Service name
  @param cluster_name: Cluster name
  @return: An ApiService object
  """
  dic = resource_root.get("%s/%s" % (SERVICES_PATH % (cluster_name,), name))
  return ApiService.from_json_dict(dic)

def get_all_services(resource_root, cluster_name="default"):
  """
  Get all services
  @param resource_root: The root Resource object.
  @param cluster_name: Cluster name
  @return: A list of ApiService objects.
  """
  dic = resource_root.get(SERVICES_PATH % (cluster_name,))
  return ApiList.from_json_dict(ApiService, dic)

def delete_service(resource_root, name, cluster_name="default"):
  """
  Delete a service by name
  @param resource_root: The root Resource object.
  @param name: Service name
  @param cluster_name: Cluster name
  @return: The deleted ApiService object
  """
  resp = resource_root.delete("%s/%s" % (SERVICES_PATH % (cluster_name,), name))
  return ApiService.from_json_dict(resp)


class ApiService(BaseApiObject):
  RO_ATTR = ('serviceState', 'healthSummary', 'clusterRef')
  RW_ATTR = ('name', 'type', 'version')

  def __init__(self, name, type, version):
    # Unfortunately, the json key is called "type". So our input arg
    # needs to be called "type" as well, despite it being a python keyword.
    BaseApiObject.ctor_helper(**locals())
