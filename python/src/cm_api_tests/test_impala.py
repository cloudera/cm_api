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
    self.assertEquals(0, len(resp.queries))

  def test_get_details(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("GET", "/cm/service/impalaQueries/randomId",
        retdata={ 'details': '' },
        params={ 'format':'text' } )
    resp = service.get_query_details('randomId')
    self.assertEquals('', resp.details)

  def test_cancel_query(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("POST", "/cm/service/impalaQueries/randomId/cancel",
        retdata={ 'warning' : 'test' })
    resp = service.cancel_impala_query('randomId')
    self.assertEquals('test', resp.warning)

  def test_attributes(self):
    IMPALA_QUERY_ATTRS = '''{
      "items": [
        {
          "name": "query_type",
          "type": "STRING",
          "displayName": "Query Type",
          "supportsHistograms": true,
          "description": "The type of the query's SQL statement (DML, DDL, Query). Called 'query_type' in searches."
        },
        {
          "name": "query_state",
          "type": "STRING",
          "displayName": "Query State",
          "supportsHistograms": true,
          "description": "The current state of the query (running, finished, and so on). Called 'query_state' in searches."
        },
        {
          "name": "query_duration",
          "type": "MILLISECONDS",
          "displayName": "Duration",
          "supportsHistograms": true,
          "description": "The duration of the query in milliseconds. Called 'query_duration' in searches."
        }]
    }'''

    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("GET", "/cm/service/impalaQueries/attributes",
                    retdata=json.loads(IMPALA_QUERY_ATTRS))
    resp = service.get_impala_query_attributes()
    self.assertEquals(3, len(resp))
    attr = resp[0]
    self.assertIsInstance(attr, ApiImpalaQueryAttribute)
    self.assertEquals('query_type', attr.name)
    self.assertEquals(True, attr.supportsHistograms)


