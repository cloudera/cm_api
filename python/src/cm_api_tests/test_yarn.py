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

try:
  import json
except ImportError:
  import simplejson as json

class TestYarn(unittest.TestCase):

  def test_get_yarn_applications(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    time = datetime.datetime.now()

    resource.expect("GET", "/cm/service/yarnApplications",
    retdata={ 'applications': [], 'warnings' : [] },
    params={ 'from':time.isoformat(), 'to':time.isoformat(), \
        'filter':'', 'limit':100, 'offset':0 })
    resp = service.get_yarn_applications(time, time)
    self.assertEquals(0, len(resp.applications))

  def test_kill_application(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("POST", "/cm/service/yarnApplications/randomId/kill",
        retdata={ 'warning' : 'test' })
    resp = service.kill_yarn_application('randomId')
    self.assertEquals('test', resp.warning)

  def test_attributes(self):
    YARN_APPLICATION_ATTRS = '''{
      "items": [
        {
          "name": "name",
          "type": "STRING",
          "displayName": "Name",
          "supportsHistograms": true,
          "description": "Name of the YARN application. Called 'name' in searches."
        },
        {
          "name": "user",
          "type": "STRING",
          "displayName": "User",
          "supportsHistograms": true,
          "description": "The user who ran the YARN application. Called 'user' in searches."
        },
        {
          "name": "executing",
          "type": "BOOLEAN",
          "displayName": "Executing",
          "supportsHistograms": false,
          "description": "Whether the YARN application is currently executing. Called 'executing' in searches."
        }]
    }'''

    self.resource = utils.MockResource(self)
    self.resource.expect("GET", "yarnApplications/attributes",
                        retdata=json.loads(YARN_APPLICATION_ATTRS))

    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("GET", "/cm/service/yarnApplications/attributes",
                    retdata=json.loads(YARN_APPLICATION_ATTRS))
    resp = service.get_yarn_application_attributes()
    self.assertEquals(3, len(resp))
    attr = resp[0]
    self.assertIsInstance(attr, ApiYarnApplicationAttribute)
    self.assertEquals('name', attr.name)
    self.assertEquals(True, attr.supportsHistograms)

  def test_collect_yarn_application_diagnostics(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("POST", "/cm/service/commands/yarnApplicationDiagnosticsCollection",
        retdata={ 'name' : 'YarnApplicationDiagnosticsCollection' })
    resp = service.collect_yarn_application_diagnostics('randomId-1', 'randomId-2', 'randomId-3')

    self.assertEquals('YarnApplicationDiagnosticsCollection', resp.name)

  def test_create_yarn_application_diagnostics_bundle(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("POST", "/cm/service/commands/yarnApplicationDiagnosticsCollection",
        retdata={ 'name' : 'YarnApplicationDiagnosticsCollection' })
    resp = service.create_yarn_application_diagnostics_bundle(['randomId-1', 'randomId-2', 'randomId-3'], 'test_ticket', 'test comment')

    self.assertEquals('YarnApplicationDiagnosticsCollection', resp.name)
