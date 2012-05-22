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
import logging

from cm_api.endpoints.types import ApiList, BaseApiObject
from cm_api.utils import api_time_to_datetime

__docformat__ = "epytext"

EVENTS_PATH = "/events"
LOG = logging.getLogger(__name__)

def query_events(resource_root, query_str=None):
  """
  Search for events.
  @param query_str: Query string.
  @return: A list of ApiEvent.
  """
  if query_str:
    params = dict(query=query_str)
  else:
    params = { }
  resp = resource_root.get(EVENTS_PATH, params=params)
  return ApiList.from_json_dict(ApiEvent, resp, resource_root)

def get_event(resource_root, event_id):
  """
  Retrieve a particular event by ID.
  @param event_id: The event ID.
  @return: An ApiEvent.
  """
  resp = resource_root.get("%s/%s" % (EVENTS_PATH, event_id))
  return ApiEvent.from_json_dict(resp, resource_root)


class ApiEvent(BaseApiObject):
  RO_ATTR = ('id', 'content', 'timeOccurred', 'timeReceived', 'category',
             'severity', 'alert', 'attributes')
  RW_ATTR = ( )

  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

  def _setattr(self, k, v):
    if k == 'timeOccurred' and v is not None:
      self.timeOccurred = api_time_to_datetime(v)
    elif k == 'timeReceived' and v is not None:
      self.timeReceived = api_time_to_datetime(v)
    else:
      BaseApiObject._setattr(self, k, v)
