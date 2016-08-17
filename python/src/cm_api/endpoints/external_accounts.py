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

EXTERNAL_ACCOUNT_PATH = "/externalAccounts/%s"
EXTERNAL_ACCOUNT_FETCH_PATH = "/externalAccounts/%s/%s"

def get_supported_categories(resource_root):
  """
  Lookup all supported categories.
  @param resource_root: The root Resource object.
  @return: An ApiExternalAcccountCategory list
  """
  return call(resource_root.get, EXTERNAL_ACCOUNT_PATH % ("supportedCategories",) ,
      ApiExternalAccountCategory, True)

def get_supported_types(resource_root, category_name):
  """
  Lookup all supported types in a category.
  @param resource_root: The root Resource object.
  @param category_name: The category name
  @return: An ApiExternalAcccountType list
  """
  return call(resource_root.get,
      EXTERNAL_ACCOUNT_FETCH_PATH % ("supportedTypes", category_name,),
      ApiExternalAccountType, True)

def create_external_account(resource_root, name, display_name, type_name,
                            account_configs=None):
  """
  Create an external account
  @param resource_root: The root Resource object.
  @param name: Immutable external account name
  @param display_name: Display name
  @param type_name: Account type
  @param account_configs: Optional account configuration (ApiList of ApiConfig objects)
  @return: An ApiExternalAccount object matching the newly created account
  """
  account = ApiExternalAccount(resource_root,
                               name=name,
                               displayName=display_name,
                               typeName=type_name,
                               accountConfigs=account_configs)
  return call(resource_root.post,
      EXTERNAL_ACCOUNT_PATH % ("create",),
      ApiExternalAccount, False, data=account)

def get_external_account(resource_root, name, view=None):
  """
  Lookup an external account by name
  @param resource_root: The root Resource object.
  @param name: Account name
  @param view: View
  @return: An ApiExternalAccount object
  """
  return call(resource_root.get,
      EXTERNAL_ACCOUNT_FETCH_PATH % ("account", name,),
      ApiExternalAccount, False, params=view and dict(view=view) or None)

def get_external_account_by_display_name(resource_root,
                                         display_name, view=None):
  """
  Lookup an external account by display name
  @param resource_root: The root Resource object.
  @param display_name: Account display name
  @param view: View
  @return: An ApiExternalAccount object
  """
  return call(resource_root.get,
      EXTERNAL_ACCOUNT_FETCH_PATH % ("accountByDisplayName", display_name,),
      ApiExternalAccount, False, params=view and dict(view=view) or None)

def get_all_external_accounts(resource_root, type_name, view=None):
  """
  Lookup all external accounts of a particular type, by type name.
  @param resource_root: The root Resource object.
  @param type_name: Type name
  @param view: View
  @return: An ApiList of ApiExternalAccount objects matching the specified type
  """
  return call(resource_root.get,
      EXTERNAL_ACCOUNT_FETCH_PATH % ("type", type_name,),
      ApiExternalAccount, True, params=view and dict(view=view) or None)

def update_external_account(resource_root, account):
  """
  Update an external account
  @param resource_root: The root Resource object.
  @param account: Account to update, account name must be specified.
  @return: An ApiExternalAccount object, representing the updated external account
  """
  return call(resource_root.put,
      EXTERNAL_ACCOUNT_PATH % ("update",),
      ApiExternalAccount, False, data=account)

def delete_external_account(resource_root, name):
  """
  Delete an external account by name
  @param resource_root: The root Resource object.
  @param name: Account name
  @return: The deleted ApiExternalAccount object
  """
  return call(resource_root.delete,
      EXTERNAL_ACCOUNT_FETCH_PATH % ("delete", name,),
      ApiExternalAccount, False)


class ApiExternalAccountCategory(BaseApiObject):
  _ATTRIBUTES = {
    'name'        : None,
    'displayName' : None,
    'description' : None
  }

  def __str__(self):
    return "<ApiExternalAccountCategory>: %s" % (
        self.name)

class ApiExternalAccountType(BaseApiObject):
  _ATTRIBUTES = {
    'name'         : None,
    'displayName'  : None,
    'type'         : None,
    'categoryName' : None,
    'description'  : None,
    'allowedAccountConfigs' : Attr(ApiConfig)
  }

  def __str__(self):
    return "<ApiExternalAccountType>: %s (categoryName: %s)" % (
        self.name, self.typeName)

class ApiExternalAccount(BaseApiObject):
  _ATTRIBUTES = {
    'name'             : None,
    'displayName'      : None,
    'typeName'         : None,
    'createdTime'      : ROAttr(),
    'lastModifiedTime' : ROAttr(),
    'accountConfigs'   : Attr(ApiConfig)
  }

  def __init__(self, resource_root, name=None, displayName=None,
               typeName=None, accountConfigs=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiExternalAccount>: %s (typeName: %s)" % (
        self.name, self.typeName)

