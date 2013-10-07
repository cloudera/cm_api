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
from cm_api.endpoints import batch
from cm_api.endpoints.types import *
from cm_api_tests import utils

class TestBatch(unittest.TestCase):

  def test_execute_batch(self):
    resource = utils.MockResource(self)
    elems = []
    elems.append(ApiBatchRequestElement(resource,
                                        method='GET',
                                        url='/1/2/3'))
    elems.append(ApiBatchRequestElement(resource,
                                        method='POST',
                                        url='/4/5/6/7',
                                        body='asdf'))
    resource.expect("POST", "/batch",
                    data=elems,
                    retdata={ 'success' : False, 'items' : [] })
    ret = batch.do_batch(resource, elems)
    self.assertIsInstance(ret, ApiBatchResponseList)
    self.assertIsInstance(ret.success, bool)
    self.assertFalse(ret.success)
    self.assertEquals(0, len(ret))
