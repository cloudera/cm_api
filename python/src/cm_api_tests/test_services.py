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

  def test_create_hdfs_tmp(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, 'hdfs1', 'HDFS')
    service.__dict__['clusterRef'] = ApiClusterRef(resource, clusterName='cluster1')

    resource.expect("POST", "/clusters/cluster1/services/hdfs1/commands/hdfsCreateTmpDir",
        retdata=ApiCommand(resource).to_json_dict())
    service.create_hdfs_tmp()
