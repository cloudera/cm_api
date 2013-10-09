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

from cm_api.endpoints.types import *

__docformat__ = "epytext"

EVENTS_PATH = "/events"

def query_events(resource_root, query_str=None):
  """
  Search for events.
  @param query_str: Query string.
  @return: A list of ApiEvent.
  """
  params = None
  if query_str:
    params = dict(query=query_str)
  return call(resource_root.get, EVENTS_PATH, ApiEventQueryResult,
      params=params)

def get_event(resource_root, event_id):
  """
  Retrieve a particular event by ID.
  @param event_id: The event ID.
  @return: An ApiEvent.
  """
  return call(resource_root.get, "%s/%s" % (EVENTS_PATH, event_id), ApiEvent)


class ApiEvent(BaseApiObject):
  _ATTRIBUTES = {
    'id'            : ROAttr(),
    'content'       : ROAttr(),
    'timeOccurred'  : ROAttr(datetime.datetime),
    'timeReceived'  : ROAttr(datetime.datetime),
    'category'      : ROAttr(),
    'severity'      : ROAttr(),
    'alert'         : ROAttr(),
    'attributes'    : ROAttr(),
  }

class ApiEventQueryResult(ApiList):
  _ATTRIBUTES = {
    'totalResults' : ROAttr(),
  }
  _MEMBER_CLASS = ApiEvent
