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

from cm_api.endpoints.types import *

__docformat__ = "epytext"

HOST_TEMPLATES_PATH = "/clusters/%s/hostTemplates"
HOST_TEMPLATE_PATH = "/clusters/%s/hostTemplates/%s"
APPLY_HOST_TEMPLATE_PATH = HOST_TEMPLATE_PATH + "/commands/applyHostTemplate"

def create_host_template(resource_root, name, cluster_name):
  """
  Create a host template.
  @param resource_root: The root Resource object.
  @param name: Host template name
  @param cluster_name: Cluster name
  @return: An ApiHostTemplate object for the created host template.
  """
  apitemplate = ApiHostTemplate(resource_root, name, [])
  apitemplate_list = ApiList([apitemplate])
  body = json.dumps(apitemplate_list.to_json_dict())
  resp = resource_root.post(HOST_TEMPLATES_PATH % (cluster_name,), data=body)
  return ApiList.from_json_dict(ApiHostTemplate, resp, resource_root)[0]

def get_host_template(resource_root, name, cluster_name):
  """
  Lookup a host template by name in the specified cluster.
  @param resource_root: The root Resource object.
  @param name: Host template name.
  @param cluster_name: Cluster name.
  @return An ApiHostTemplate object.
  """
  resp = resource_root.get(HOST_TEMPLATE_PATH % (cluster_name, name))
  return ApiHostTemplate.from_json_dict(resp, resource_root)

def get_all_host_templates(resource_root, cluster_name="default"):
  """
  Get all host templates in a cluster.
  @param cluster_name: Cluster name.
  @return ApiList of ApiHostTemplate objects for all host templates in a cluster.
  """
  resp = resource_root.get(HOST_TEMPLATES_PATH % (cluster_name,))
  return ApiList.from_json_dict(ApiHostTemplate, resp, resource_root)

def delete_host_template(resource_root, name, cluster_name):
  """
  Delete a host template identified by name in the specified cluster.
  @param resource_root: The root Resource object.
  @param name: Host template name.
  @param cluster_name: Cluster name.
  @return The deleted ApiHostTemplate object.
  """
  resp = resource_root.delete(HOST_TEMPLATE_PATH % (cluster_name, name))
  return ApiHostTemplate.from_json_dict(resp, resource_root)

def update_host_template(resource_root, name, cluster_name, api_host_template):
  """
  Update a host template identified by name in the specified cluster.
  @param resource_root: The root Resource object.
  @param name: Host template name.
  @param cluster_name: Cluster name.
  @param api_host_template: The updated host template.
  @return: The updated ApiHostTemplate.
  """
  body = json.dumps(api_host_template.to_json_dict())
  resp = resource_root.put(HOST_TEMPLATE_PATH % (cluster_name, name), body)
  return ApiHostTemplate.from_json_dict(resp, resource_root)

def apply_host_template(resource_root, name, cluster_name, host_ids, start_roles):
  """
  Apply a host template identified by name on the specified hosts and
  optionally start them.
  @param resource_root: The root Resource object.
  @param name: Host template name.
  @param cluster_name: Cluster name.
  @param host_ids: List of host ids.
  @param start_roles: Whether to start the created roles or not.
  @return: An ApiCommand object.
  """
  host_refs = []
  for host_id in host_ids:
    host_refs.append(ApiHostRef(resource_root, host_id))

  params = {"startRoles" : start_roles}
  body = json.dumps(ApiList(host_refs).to_json_dict())
  resp = resource_root.post(APPLY_HOST_TEMPLATE_PATH % (cluster_name, name), params=params, data=body)
  return ApiCommand.from_json_dict(resp, resource_root)


class ApiHostTemplate(BaseApiObject):
  _ATTRIBUTES = {
    'name'                : None,
    'roleConfigGroupRefs' : Attr(ApiRoleConfigGroupRef),
    'clusterRef'          : ROAttr(ApiClusterRef),
  }

  def __init__(self, resource_root, name=None, roleConfigGroupRefs=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiHostTemplate>: %s (cluster %s)" % (self.name, self.clusterRef.clusterName)

  def _path(self):
    return HOST_TEMPLATE_PATH % (self.clusterRef.clusterName, self.name)

  def _put(self, dic):
    resp = self._get_resource_root().put(self._path(), data=json.dumps(dic))
    host_template = ApiHostTemplate.from_json_dict(resp, self._get_resource_root())

    self._update(host_template)
    return self

  def rename(self, new_name):
    """
    Rename a host template.
    @param new_name: New host template name.
    @return: An ApiHostTemplate object.
    """
    dic = self.to_json_dict()
    dic['name'] = new_name
    return self._put(dic)

  def set_role_config_groups(self, role_config_group_refs):
    """
    Updates the role config groups in a host template.
    @param role_config_group_refs: List of role config group refs.
    @return: An ApiHostTemplate object.
    """
    dic = self.to_json_dict()
    dic['roleConfigGroupRefs'] = [ x.to_json_dict() for x in role_config_group_refs ]
    return self._put(dic)

  def apply_host_template(self, host_ids, start_roles):
    """
    Apply a host template identified by name on the specified hosts and
    optionally start them.
    @param host_ids: List of host ids.
    @param start_roles: Whether to start the created roles or not.
    @return: An ApiCommand object.
    """
    return apply_host_template(self._get_resource_root(), self.name, self.clusterRef.clusterName, host_ids, start_roles)
