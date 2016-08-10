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
from cm_api.endpoints.users import *
from cm_api_tests import utils

class TestUsers(unittest.TestCase):

  def test_grant_admin(self):
    res = utils.MockResource(self)

    user = ApiUser(res, "alice")
    expected = { 'name' : 'alice', 'roles' : [ 'ROLE_ADMIN' ] }
    res.expect("PUT", "/users/alice", data=expected, retdata=expected)
    updated = user.grant_admin_role()
    self.assertTrue('ROLE_ADMIN' in updated.roles)
    self.assertEqual(1, len(updated.roles))

  def test_revoke_admin(self):
    res = utils.MockResource(self)

    user = ApiUser(res, "alice", roles=[ 'ROLE_ADMIN' ] )
    expected = { 'name' : 'alice', 'roles' : [ ] }
    res.expect("PUT", "/users/alice", data=expected, retdata=expected)
    updated = user.revoke_admin_role()
    self.assertEqual(0, len(updated.roles))

  def test_update_user(self):
    res = utils.MockResource(self)
    user = ApiUser(res, "alice", roles=[ 'ROLE_LIMITED' ])

    # Update user role
    new_user = ApiUser(res, "alice", roles=[ 'ROLE_USER' ])
    expected = { 'name' : 'alice', 'roles' : [ 'ROLE_USER'] }
    res.expect("PUT", "/users/alice", data=expected, retdata=expected)
    updated = update_user(res, new_user)
    self.assertTrue('ROLE_USER' in updated.roles)
    self.assertEqual(1, len(updated.roles))
