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

from cm_api.endpoints.types import BaseApiObject, ApiList

USERS_PATH = "/users"

def get_all_users(resource_root, view=None):
  """
  Get all users.

  @param resource_root: The root Resource object
  @param view: View to materialize ('full' or 'summary').
  @return: A list of ApiUser objects.
  """
  dic = resource_root.get(USERS_PATH,
          params=view and dict(view=view) or None)
  return ApiList.from_json_dict(ApiUser, dic, resource_root)

def get_user(resource_root, username):
  """
  Look up a user by username.

  @param resource_root: The root Resource object
  @param username: Username to look up
  @return: An ApiUser object
  """
  dic = resource_root.get('%s/%s' % (USERS_PATH, username))
  return ApiUser.from_json_dict(dic, resource_root)

def _grant_admin_role(resource_root, username):
  """
  Grant admin access to a user. If the user already has admin access, this
  does nothing.

  @param resource_root: The root Resource object
  @param username: Username to give admin access to.
  @return: An ApiUser object
  """
  apiuser = ApiUser(resource_root, username, roles=['ROLE_ADMIN'])
  body = json.dumps(apiuser.to_json_dict())
  resp = resource_root.put('%s/%s' % (USERS_PATH, username), data=body)
  return ApiUser.from_json_dict(resp, resource_root)

def _revoke_admin_role(resource_root, username):
  """
  Revoke admin access from a user. If the user does not have admin access,
  this does nothing.

  @param resource_root: The root Resource object
  @param username: Username to give admin access to.
  @return: An ApiUser object
  """
  apiuser = ApiUser(resource_root, username, roles=[])
  body = json.dumps(apiuser.to_json_dict())
  resp = resource_root.put('%s/%s' % (USERS_PATH, username), data=body)
  return ApiUser.from_json_dict(resp, resource_root)

def create_user(resource_root, username, password, roles):
  """
  Create a user.

  @param resource_root: The root Resource object
  @param username: Username
  @param password: Password
  @param roles: List of roles for the user. This should be [] for a
                regular user, or ['ROLE_ADMIN'] for an admin.
  @return: An ApiUser object
  """
  apiuser = ApiUser(resource_root, username, password=password, roles=roles)
  apiuser_list = ApiList([apiuser])
  body = json.dumps(apiuser_list.to_json_dict())
  resp = resource_root.post(USERS_PATH, data=body)
  return ApiList.from_json_dict(ApiUser, resp, resource_root)[0]

def delete_user(resource_root, username):
  """
  Delete user by username.

  @param resource_root: The root Resource object
  @param: username Username
  @return: An ApiUser object
  """
  resp = resource_root.delete('%s/%s' % (USERS_PATH, username))
  return ApiUser.from_json_dict(resp, resource_root)

class ApiUser(BaseApiObject):
  _ATTRIBUTES = {
    'name'      : None,
    'password'  : None,
    'roles'     : None,
  }

  def __init__(self, resource_root, name=None, password=None, roles=None):
    BaseApiObject.init(self, resource_root, locals())

  def grant_admin_role(self):
    """
    Grant admin access to a user. If the user already has admin access, this
    does nothing.

    @return: An ApiUser object
    """
    return _grant_admin_role(self._get_resource_root(), self.name)

  def revoke_admin_role(self):
    """
    Revoke admin access from a user. If the user does not have admin access,
    this does nothing.

    @return: An ApiUser object
    """
    return _revoke_admin_role(self._get_resource_root(), self.name)
