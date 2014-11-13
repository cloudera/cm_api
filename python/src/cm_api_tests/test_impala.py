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

import datetime
import unittest
from cm_api.endpoints.clusters import *
from cm_api.endpoints.services import *
from cm_api.endpoints.types import *
from cm_api_tests import utils

class TestImpala(unittest.TestCase):

  def test_get_queries(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    time = datetime.datetime.now()

    resource.expect("GET", "/cm/service/impalaQueries",
        retdata={ 'queries': [], 'warnings' : [] },
        params={ 'from':time.isoformat(), 'to':time.isoformat(), \
            'filter':'', 'limit':100, 'offset':0 })
    resp = service.get_impala_queries(time, time)
    self.assertEqual(0, len(resp.queries))

  def test_get_details(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("GET", "/cm/service/impalaQueries/randomId",
        retdata={ 'details': '' },
        params={ 'format':'text' } )
    resp = service.get_query_details('randomId')
    self.assertEqual('', resp.details)

  def test_cancel_query(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("POST", "/cm/service/impalaQueries/randomId/cancel",
        retdata={ 'warning' : 'test' })
    resp = service.cancel_impala_query('randomId')
    self.assertEqual('test', resp.warning)

  def test_attributes(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("GET", "/cm/service/impalaQueries/attributes",
        retdata=[{ 'name' : 'test',
                  'type' : 'STRING',
                  'displayName' : 'testDisplayName',
                  'supportsHistograms' : True,
                  'description' : 'testDescription' }])
    resp = service.get_impala_query_attributes()
    self.assertEqual(1, len(resp))
    attr = resp[0]
    self.assertIsInstance(attr, ApiImpalaQueryAttribute)
    self.assertEqual('test', attr.name)
