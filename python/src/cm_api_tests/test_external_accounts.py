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

import unittest
from cm_api.endpoints.types import *
from cm_api.endpoints.external_accounts import *
from cm_api_tests import utils

class TestExternalAccount(unittest.TestCase):

  def __init__(self, methodName):
    unittest.TestCase.__init__(self, methodName)
    self.resource = utils.MockResource(self)
    self.account = ApiExternalAccount(self.resource, name='test1',
      displayName='test_d1', typeName='AWS_ACCESS_KEY_AUTH',
      accountConfigs=ApiList([ApiConfig(self, "aws_access_key", "foo"),
        ApiConfig(self, "aws_secret_key", "bar")]))

  def test_create_acct(self):
    test_acct=ApiExternalAccount(self.resource,
            name=self.account.name, displayName=self.account.displayName,
            typeName=self.account.typeName,
            accountConfigs=None).to_json_dict()
    self.resource.expect("POST", "/externalAccounts/create", retdata=test_acct)
    ret = create_external_account(self.resource, self.account.name,
        self.account.displayName, self.account.typeName)
    self.checkEqual(self.account, ret)

  def test_update_acct(self):
    test_acct=ApiExternalAccount(self.resource,
            name=self.account.name, displayName=self.account.displayName,
            typeName=self.account.typeName,
            accountConfigs=self.account.accountConfigs).to_json_dict()
    self.resource.expect("PUT", "/externalAccounts/update", retdata=test_acct)
    ret = update_external_account(self.resource, test_acct)
    self.checkEqual(self.account, ret, True)

  def test_delete_acct(self):
    test_acct=ApiExternalAccount(self.resource,
            name=self.account.name, displayName=self.account.displayName,
            typeName=self.account.typeName,
            accountConfigs=self.account.accountConfigs).to_json_dict()
    self.resource.expect("DELETE",
      "/externalAccounts/delete/%s" %self.account.name, retdata=test_acct)
    ret = delete_external_account(self.resource, self.account.name)
    self.checkEqual(self.account, ret, True)

  def test_get_acct(self):
    test_acct=ApiExternalAccount(self.resource,
            name=self.account.name, displayName=self.account.displayName,
            typeName=self.account.typeName,
            accountConfigs=self.account.accountConfigs).to_json_dict()
    self.resource.expect("GET",
      "/externalAccounts/account/%s" %self.account.name, retdata=test_acct)
    ret = get_external_account(self.resource, self.account.name)
    self.checkEqual(self.account, ret, True)

  def test_get_by_display_name(self):
    test_acct=ApiExternalAccount(self.resource,
            name=self.account.name, displayName=self.account.displayName,
            typeName=self.account.typeName,
            accountConfigs=self.account.accountConfigs).to_json_dict()
    self.resource.expect("GET",
      "/externalAccounts/accountByDisplayName/%s" %self.account.displayName,
      retdata=test_acct)
    ret = get_external_account_by_display_name(
      self.resource, self.account.displayName)
    self.checkEqual(self.account, ret, True)

  def test_get_all_accts(self):
    test_acct=ApiList([ ApiExternalAccount(self.resource,
            name=self.account.name, displayName=self.account.displayName,
            typeName=self.account.typeName,
            accountConfigs=self.account.accountConfigs)]).to_json_dict()
    self.resource.expect("GET",
      "/externalAccounts/type/%s" %self.account.typeName,
      retdata=ApiList(test_acct))
    ret = get_all_external_accounts(self.resource, self.account.typeName)
    self.checkEqualList([self.account], ret, True)

  def test_supported_categories(self):
    test_cats=ApiList([ApiExternalAccountCategory(self.resource,
            name="a1", displayName="a2", description="a3"),
            ApiExternalAccountCategory(self.resource,
            name="b1", displayName="b2", description="b3")])
    self.resource.expect("GET",
      "/externalAccounts/supportedCategories",
      retdata=test_cats.to_json_dict())
    ret = get_supported_categories(self.resource)
    self.assertEqual(test_cats[0].name, ret[0].name)
    self.assertEqual(test_cats[0].displayName, ret[0].displayName)
    self.assertEqual(test_cats[0].description, ret[0].description)
    self.assertEqual(test_cats[1].name, ret[1].name)
    self.assertEqual(test_cats[1].displayName, ret[1].displayName)
    self.assertEqual(test_cats[1].description, ret[1].description)

  def test_supported_types(self):
    test_cat_name="foo"
    test_types=ApiList([ApiExternalAccountType(self.resource,
            name="a1", displayName="a2", description="a3",
            categoryName = test_cat_name, allowedAccountConfigs=
              ApiList([ApiConfig(self, "a4", "a5")])),
            ApiExternalAccountType(self.resource,
            name="b1", displayName="b2", description="b3",
            categoryName = test_cat_name, allowedAccountConfigs=
              ApiList([ApiConfig(self, "b4", "b5")]))])
    self.resource.expect("GET",
      "/externalAccounts/supportedTypes/%s" %test_cat_name,
      retdata=test_types.to_json_dict())
    types = get_supported_types(self.resource, test_cat_name)
    self.assertEqual(test_types[0].name, types[0].name)
    self.assertEqual(test_types[0].displayName, types[0].displayName)
    self.assertEqual(test_types[0].description, types[0].description)
    self.assertEqual(test_types[0].categoryName, types[0].categoryName)
    self.chkConfigsEqual(
        test_types[0].allowedAccountConfigs,
        types[0].allowedAccountConfigs)
    self.assertEqual(test_types[1].name, types[1].name)
    self.assertEqual(test_types[1].displayName, types[1].displayName)
    self.assertEqual(test_types[1].description, types[1].description)
    self.assertEqual(test_types[1].categoryName, types[1].categoryName)
    self.chkConfigsEqual(
        test_types[1].allowedAccountConfigs,
        types[1].allowedAccountConfigs)

  def checkEqualList(self, expected, fetched, chkConfigs=False):
    self.assertTrue(isinstance(expected, list))
    self.assertTrue(isinstance(fetched, ApiList))
    self.assertEqual(len(expected), len(fetched))
    for i in range(len(expected)):
      self.checkEqual(expected[i], fetched[i])

  def checkEqual(self, expected, fetched, chk_configs=False):
    self.assertEqual(expected.name, fetched.name)
    self.assertEqual(expected.typeName, fetched.typeName)
    self.assertEqual(expected.displayName, fetched.displayName)
    if chk_configs:
      self.chkConfigsEqual(
        expected.accountConfigs, fetched.accountConfigs)

  def chkConfigsEqual(self, expectedConfigs, fetchedConfigs):
      if expectedConfigs is None and fetchedConfigs is None:
        return
      for k in range(len(expectedConfigs)):
        self.assertEqual(
          fetchedConfigs[expectedConfigs[k].name].value,
          expectedConfigs[k].value)

  def test_get_acct_config(self):
    test_config = self.account.accountConfigs.to_json_dict();
    self.resource.expect("GET",
      "/externalAccounts/account/%s/config" % self.account.name,
      retdata=test_config)
    ret = config_to_api_list(self.account.get_config())
    for entry in ret['items']:
      k = entry['name']
      if k == 'aws_secret_key':
        self.assertTrue(entry["value"] == 'bar')
      elif k == "aws_access_key":
        self.assertTrue(entry["value"] == 'foo')
      else:
        self.assertFailure()

  def test_update_acct_config(self):
    test_config = config_to_api_list({"aws_secret_key": "bar2", "aws_access_key": "foo"})
    update_config = {"aws_secret_key": "bar2"}
    self.resource.expect("PUT",
      "/externalAccounts/account/%s/config" % self.account.name,
      data=config_to_api_list(update_config), retdata=test_config)
    ret = self.account.update_config(update_config)
    for entry in ret.iteritems():
      if entry[0] == 'aws_secret_key':
        self.assertTrue(entry[1] == 'bar2')
      elif entry[0] == "aws_access_key":
        self.assertTrue(entry[1] == 'foo')
      else:
        self.assertFailure()

  def test_s3_guard_prune_cmd(self):
    command_name = 'S3GuardPrune'
    data = '"test1"'
    self.resource.expect("POST",
                         "/externalAccounts/account/%s/commands/%s" % ('test1', command_name),
                         data=data, retdata={'name' : 'S3GuardPrune'})
    resp = self.account.external_account_cmd_by_name(command_name)
    self.assertEquals('S3GuardPrune', resp.name)
