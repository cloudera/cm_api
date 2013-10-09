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
from cm_api.endpoints.events import *
from cm_api_tests import utils

class TestEvents(unittest.TestCase):

  def test_event_query(self):
    res = utils.MockResource(self)

    query = 'foo'
    event = ApiEvent(res)
    event.__dict__['content'] = 'bar'
    expected = ApiEventQueryResult([ event ])
    expected.__dict__['totalResults'] = 42

    res.expect('GET', '/events', params=dict(query=query),
        retdata=expected.to_json_dict(True))
    ret = query_events(res, query)
    self.assertEqual(1, len(ret))
    self.assertEqual(expected[0].content, ret[0].content)
    self.assertEqual(42, expected.totalResults)
