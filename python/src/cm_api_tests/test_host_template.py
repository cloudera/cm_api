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
from cm_api.endpoints.host_templates import *
from cm_api.endpoints.types import *
from cm_api_tests import utils

class TestHostTemplates(unittest.TestCase):

  def test_update(self):
    res = utils.MockResource(self)

    cluster = ApiClusterRef(res, clusterName='c1')
    tmpl = ApiHostTemplate(res, name='foo')
    tmpl.__dict__['clusterRef'] = cluster

    rcgs = [
      ApiRoleConfigGroupRef(res, roleConfigGroupName='rcg1'),
      ApiRoleConfigGroupRef(res, roleConfigGroupName='rcg2'),
    ]
    expected = ApiHostTemplate(res, name='foo', roleConfigGroupRefs=rcgs)

    res.expect('PUT', '/clusters/c1/hostTemplates/foo',
        data=expected,
        retdata=expected.to_json_dict())
    ret = tmpl.set_role_config_groups(rcgs)
    self.assertEqual(len(rcgs), len(ret.roleConfigGroupRefs))
