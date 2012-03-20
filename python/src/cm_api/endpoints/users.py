# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

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
  RW_ATTR = ('name', 'password', 'roles')
  def __init__(self, resource_root, name, password=None, roles=None):
    BaseApiObject.ctor_helper(**locals())

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
