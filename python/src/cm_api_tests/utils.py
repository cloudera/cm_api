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

from cm_api import api_client
from cm_api.endpoints.types import Attr
from cm_api.resource import Resource

try:
  import json
except ImportError:
  import simplejson as json

class MockResource(Resource):
  """
  Allows code to control the behavior of a resource's "invoke" method for
  unit testing.
  """

  def __init__(self, test, version=api_client.API_CURRENT_VERSION):
    Resource.__init__(self, None)
    self._next_expect = None
    self.test = test
    self.version = version

  @property
  def base_url(self):
    return ""

  def invoke(self, method, relpath=None, params=None, data=None, headers=None):
    """
    Checks the expected input data and returns the appropriate data to the caller.
    """
    exp_method, exp_path, exp_params, exp_data, exp_headers, retdata = self._next_expect
    self._next_expect = None

    if exp_method is not None:
      self.test.assertEquals(exp_method, method)
    if exp_path is not None:
      self.test.assertEquals(exp_path, relpath)
    if exp_params is not None:
      self.test.assertEquals(exp_params, params)
    if exp_data is not None:
      if not isinstance(exp_data, str):
        exp_data = json.dumps(Attr(is_api_list=True).to_json(exp_data, False))
      self.test.assertEquals(exp_data, data)
    if exp_headers is not None:
      self.test.assertEquals(exp_headers, headers)
    return retdata

  def expect(self, method, reqpath, params=None, data=None, headers=None,
      retdata=None):
    """
    Sets the data to expect in the next call to invoke().

    @param method: method to expect, or None for any.
    @param reqpath: request path, or None for any.
    @param params: query parameters, or None for any.
    @param data: request body, or None for any.
    @param headers: request headers, or None for any.
    @param retdata: data to return from the invoke call.
    """
    self._next_expect = (method, reqpath, params, data, headers, retdata)

def deserialize(raw_data, cls):
  """
  Deserializes raw JSON data into an instance of cls.

  The data is deserialized, serialized again using the class's to_json_dict()
  implementation, and deserialized again, to make sure both from_json_dict()
  and to_json_dict() are working.
  """
  instance = cls.from_json_dict(json.loads(raw_data), None)
  return cls.from_json_dict(instance.to_json_dict(preserve_ro=True), None)
