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
from cm_api.endpoints.clusters import ApiCluster
from cm_api.endpoints.services import *
from cm_api.endpoints.types import *
from cm_api_tests import utils

class TestService(unittest.TestCase):

  def __init__(self, methodName):
    unittest.TestCase.__init__(self, methodName)
    self.resource = utils.MockResource(self)
    self.service = ApiService(self.resource, 'hdfs1', 'HDFS')
    self.service.__dict__['clusterRef'] = \
        ApiClusterRef(self.resource, clusterName='cluster1')

  def test_create_hdfs_tmp(self):
    self.resource.expect("POST", "/clusters/cluster1/services/hdfs1/commands/hdfsCreateTmpDir",
        retdata=ApiCommand(self.resource).to_json_dict())
    self.service.create_hdfs_tmp()

  def test_role_cmd(self):
    args = ['role1', 'role2']
    expected = ApiBulkCommandList([ApiCommand(self.resource)])
    expected.__dict__['errors'] = [ 'err1', 'err2' ]

    self.resource.expect("POST", "/clusters/cluster1/services/hdfs1/roleCommands/start",
        data=ApiList(args),
        retdata=expected.to_json_dict(True))
    ret = self.service.start_roles(*args)
    self.assertEqual(1, len(ret))
    self.assertEqual(expected.errors, ret.errors)

  def test_recommission_with_start(self):
    args = ['role1', 'role2']
    self.resource.expect("POST", "/clusters/cluster1/services/hdfs1/commands/recommissionWithStart",
        data=ApiList(args),
        retdata={})
    self.service.recommission_with_start(*args)

  def test_offline_role(self):
    args = ['role1', 'role2']
    self.resource.expect("POST", "/clusters/cluster1/services/hdfs1/commands/offline",
        data=ApiList(args),
        retdata={})
    self.service.offline(*args)
    self.resource.expect("POST", "/clusters/cluster1/services/hdfs1/commands/offline",
        data=ApiList(args),
        params={ 'timeout' : 1234567 },
        retdata={})
    self.service.offline(*args, timeout=1234567)
