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
from cm_api.endpoints.dashboards import *
from cm_api_tests import utils

class TestDashboards(unittest.TestCase):

  @staticmethod
  def get_test_dashboard():
    return  "{\"name\": \"MyDash\",\"json\":\"jsonBlob\"}"

  @staticmethod
  def get_test_dashboard_list():
    return "{\"items\": [" + TestDashboards.get_test_dashboard() + "]}"

  def test_get_dashboards(self):
    resource = utils.MockResource(self)
    resource.expect("GET", "/timeseries/dashboards",
      retdata=json.loads(TestDashboards.get_test_dashboard_list()),
      params=None)

    resp = get_dashboards(resource)
    self.assertEqual(1, len(resp))

  def test_get_dashboard(self):
    resource = utils.MockResource(self)
    resource.expect("GET", "/timeseries/dashboards/myDash",
      retdata=json.loads(TestDashboards.get_test_dashboard()),
      params=None)

    resp = get_dashboard(resource, "myDash")
    self.assertIsInstance(resp, ApiDashboard)
    self.assertEqual("MyDash", resp.name)
    self.assertEqual("jsonBlob", resp.json)

  def test_create_dashboards(self):
    resource = utils.MockResource(self)
    dashboard = ApiDashboard("newDash", "newJsonBlob")
    resource.expect("POST", "/timeseries/dashboards",
      retdata=json.loads(TestDashboards.get_test_dashboard_list()),
      params=None,
      data=[dashboard])

    resp = create_dashboards(resource, [dashboard])
    self.assertEquals(1, len(resp))

  def test_delete_dashboard(self):
    resource = utils.MockResource(self)
    resource.expect("DELETE", "/timeseries/dashboards/oldDash",
      retdata=json.loads(TestDashboards.get_test_dashboard()),
      params=None)

    resp = delete_dashboard(resource, "oldDash")

