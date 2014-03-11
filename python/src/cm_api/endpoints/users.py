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

from cm_api.endpoints.types import *

USERS_PATH = "/users"

def get_all_users(resource_root, view=None):
  """
  Get all users.

  @param resource_root: The root Resource object
  @param view: View to materialize ('full' or 'summary').
  @return: A list of ApiUser objects.
  """
  return call(resource_root.get, USERS_PATH, ApiUser, True,
      params=view and dict(view=view) or None)

def get_user(resource_root, username):
  """
  Look up a user by username.

  @param resource_root: The root Resource object
  @param username: Username to look up
  @return: An ApiUser object
  """
  return call(resource_root.get,
      '%s/%s' % (USERS_PATH, username), ApiUser)

def create_user(resource_root, username, password, roles):
  """
  Create a user.

  @param resource_root: The root Resource object
  @param username: Username
  @param password: Password
  @param roles: List of roles for the user. This should be [] or ['ROLE_USER']
                for a regular user, ['ROLE_ADMIN'] for an admin, or
                ['ROLE_LIMITED'] for a limited admin.
  @return: An ApiUser object
  """
  apiuser = ApiUser(resource_root, username, password=password, roles=roles)
  return call(resource_root.post, USERS_PATH, ApiUser, True,
      data=[apiuser])[0]

def delete_user(resource_root, username):
  """
  Delete user by username.

  @param resource_root: The root Resource object
  @param username: Username
  @return: An ApiUser object
  """
  return call(resource_root.delete,
      '%s/%s' % (USERS_PATH, username), ApiUser)

def update_user(resource_root, user):
  """
  Update a user.

  Replaces the user's details with those provided.

  @param resource_root: The root Resource object
  @param user: An ApiUser object
  @return: An ApiUser object
  """
  return call(resource_root.put,
      '%s/%s' % (USERS_PATH, user.name), ApiUser, data=user)

class ApiUser(BaseApiResource):
  _ATTRIBUTES = {
    'name'      : None,
    'password'  : None,
    'roles'     : None,
  }

  def __init__(self, resource_root, name=None, password=None, roles=None):
    BaseApiObject.init(self, resource_root, locals())

  def _path(self):
    return '%s/%s' % (USERS_PATH, self.name)

  def grant_admin_role(self):
    """
    Grant admin access to a user. If the user already has admin access, this
    does nothing. If the user currently has a non-admin role, it will be replaced
    with the admin role.

    @return: An ApiUser object
    """
    apiuser = ApiUser(self._get_resource_root(), self.name, roles=['ROLE_ADMIN'])
    return self._put('', ApiUser, data=apiuser)

  def revoke_admin_role(self):
    """
    Revoke admin access from a user. If the user does not have admin access,
    this does nothing. After revocation, the user will have the un-privileged
    regular user role.

    @return: An ApiUser object
    """
    apiuser = ApiUser(self._get_resource_root(), self.name, roles=[])
    return self._put('', ApiUser, data=apiuser)
