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
from cm_api.endpoints.services import *
from cm_api.endpoints.types import *
from cm_api_tests import utils

class TestWatchedDir(unittest.TestCase):

  def test_empty_list_watched_directories(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("GET", "/cm/service/watcheddir",
        retdata={ "items" : [] })

    responses = service.list_watched_directories()
    self.assertIsInstance(responses, ApiList)
    self.assertEquals(0, len(responses))

  def test_non_empty_list_watched_directories(self):
    WATCHED_DIRS_LIST = '''{
      "items": [ {
        "path" : "/dir1"
      }, {
        "path" : "/dir2"
      } ]
    }'''

    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("GET", "/cm/service/watcheddir",
        retdata=json.loads(WATCHED_DIRS_LIST))
    responses = service.list_watched_directories()

    self.assertIsInstance(responses, ApiList)
    self.assertEquals(2, len(responses))
    response = responses[0]
    self.assertIsInstance(response, ApiWatchedDir)
    self.assertEquals("/dir1", response.path)
    response = responses[1]
    self.assertIsInstance(response, ApiWatchedDir)
    self.assertEquals("/dir2", response.path)

  def test_add_watched_directory(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("POST", "/cm/service/watcheddir",
        retdata={ 'path' : '/dir' }, data={ 'path' : '/dir' })
    response = service.add_watched_directory("/dir")
    self.assertIsInstance(response, ApiWatchedDir)
    self.assertEquals("/dir", response.path)

  def test_remove_watched_directory(self):
    resource = utils.MockResource(self)
    service = ApiService(resource, name="bar")

    resource.expect("DELETE", "/cm/service/watcheddir/one_dir_path",
        retdata={ 'path' : 'one_dir_path' })
    response = service.remove_watched_directory("one_dir_path")
    self.assertIsInstance(response, ApiWatchedDir)
    self.assertEquals("one_dir_path", response.path)
