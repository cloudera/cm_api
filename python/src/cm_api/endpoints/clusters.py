# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import ApiList, BaseApiObject

__docformat__ = "epytext"

CLUSTERS_PATH = "/clusters"

def create_cluster(resource_root, name):
  """
  Create a cluster
  @param resource_root: The root Resource object.
  @param name: Cluster name
  @return: An ApiCluster object
  """
  apicluster = ApiCluster(name)
  apicluster_list = ApiList([apicluster], "clusters")
  body = json.dumps(apicluster_list.to_json_dict())
  resp = resource_root.post(CLUSTERS_PATH, data=body)
  # The server returns a list of created clusters (with size 1)
  return ApiList.from_json_dict(ApiCluster, resp)[0]

def get_cluster(resource_root, name):
  """
  Lookup a cluster by name
  @param resource_root: The root Resource object.
  @param name: Cluster name
  @return: An ApiCluster object
  """
  dic = resource_root.get("%s/%s" % (CLUSTERS_PATH, name))
  return ApiCluster.from_json_dict(dic)

def get_all_clusters(resource_root):
  """
  Get all clusters
  @param resource_root: The root Resource object.
  @return: A list of ApiCluster objects.
  """
  dic = resource_root.get(CLUSTERS_PATH)
  return ApiList.from_json_dict(ApiCluster, dic)

def delete_cluster(resource_root, name):
  """
  Delete a cluster by name
  @param resource_root: The root Resource object.
  @param name: Cluster name
  @return: The deleted ApiCluster object
  """
  resp = resource_root.delete("%s/%s" % (CLUSTERS_PATH, name))
  return ApiCluster.from_json_dict(resp)


class ApiCluster(BaseApiObject):
  RW_ATTR = ('name',)

  def __init__(self, name):
    BaseApiObject.ctor_helper(**locals())
