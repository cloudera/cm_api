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

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import *

__docformat__ = "epytext"

BATCH_PATH = "/batch"
SUCCESS_KEY = "success"

def do_batch(resource_root, elements):
  """
  Execute a batch request with one or more elements. If any element fails,
  the entire request is rolled back and subsequent elements are ignored.
  @param elements: A list of ApiBatchRequestElements
  @return: 2-tuple (overall success, list of ApiBatchResponseElements).
  """
  element_list = ApiList(elements)
  body = json.dumps(element_list.to_json_dict())
  resp = resource_root.post(BATCH_PATH, data=body)
  return resp[SUCCESS_KEY], ApiList.from_json_dict(ApiBatchResponseElement,
                                                   resp, resource_root)
