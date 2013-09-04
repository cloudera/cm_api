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
from cm_api.endpoints.clusters import *
from cm_api.endpoints.types import *
from cm_api_tests import utils

class TestCluster(unittest.TestCase):

  def test_add_hosts(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")

    data = ApiList([ ApiHostRef(resource, hostId='foo') ])

    resource.expect("POST", "/clusters/foo/hosts",
        data=data,
        retdata={ 'items' : [ { 'hostId' : 'foo' } ] })
    cluster.add_hosts(['foo'])
