# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import ApiList, BaseApiObject, ApiHostRef

__docformat__ = "epytext"

ROLES_PATH = "/clusters/%s/services/%s/roles"

def create_role(resource_root,
                service_name,
                role_type,
                role_name,
                host_id,
                cluster_name="default"):
  """
  Create a role
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param role_type: Role type
  @param role_name: Role name
  @param cluster_name: Cluster name
  @return: An ApiRole object
  """
  apirole = ApiRole(role_name, role_type, ApiHostRef(host_id))
  apirole_list = ApiList([apirole])
  body = json.dumps(apirole_list.to_json_dict())
  resp = resource_root.post(ROLES_PATH % (cluster_name, service_name), data=body)
  # The server returns a list of created roles (with size 1)
  return ApiList.from_json_dict(ApiRole, resp)[0]

def get_role(resource_root, service_name, name, cluster_name="default"):
  """
  Lookup a role by name
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param name: Role name
  @param cluster_name: Cluster name
  @return: An ApiRole object
  """
  dic = resource_root.get("%s/%s" %
          (ROLES_PATH % (cluster_name, service_name), name))
  return ApiRole.from_json_dict(dic)

def get_all_roles(resource_root, service_name, cluster_name="default"):
  """
  Get all roles
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param cluster_name: Cluster name
  @return: A list of ApiRole objects.
  """
  dic = resource_root.get(ROLES_PATH % (cluster_name, service_name))
  return ApiList.from_json_dict(ApiRole, dic)

def get_roles_by_type(resource_root, service_name, role_type,
                      cluster_name="default"):
  """
  Get all roles of a certain type in a service
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param role_type: Role type
  @param cluster_name: Cluster name
  @return: A list of ApiRole objects.
  """
  roles = get_all_roles(resource_root, service_name, cluster_name)
  return [ r for r in roles if r.type == role_type ]

def delete_role(resource_root, service_name, name, cluster_name="default"):
  """
  Delete a role by name
  @param resource_root: The root Resource object.
  @param service_name: Service name
  @param name: Role name
  @param cluster_name: Cluster name
  @return: The deleted ApiRole object
  """
  resp = resource_root.delete("%s/%s" %
          (ROLES_PATH % (cluster_name, service_name), name))
  return ApiRole.from_json_dict(resp)


class ApiRole(BaseApiObject):
  RO_ATTR = ('roleState', 'healthSummary', 'serviceRef')
  RW_ATTR = ('name', 'type', 'hostRef')

  def __init__(self, name, type, hostRef):
    # Unfortunately, the json key is called "type". So our input arg
    # needs to be called "type" as well, despite it being a python keyword.
    BaseApiObject.ctor_helper(**locals())
