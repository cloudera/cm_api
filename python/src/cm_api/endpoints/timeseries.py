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
try:
  import json
except ImportError:
  import simplejson as json
import logging

from cm_api.endpoints.types import *

__docformat__ = "epytext"

TIME_SERIES_PATH   = "/timeseries"
METRIC_SCHEMA_PATH = "/timeseries/schema"

LOG = logging.getLogger(__name__)

def query_timeseries(resource_root, query, from_time=None, to_time=None):
  """
  Query for time series data from the CM time series data store.
  @param query: Query string.
  @param from_time: Start of the period to query (optional).
                    This may be an ISO format string, or a datetime object.
  @param to_time: End of the period to query (default = now).
                  This may be an ISO format string, or a datetime object.
  @return List of ApiTimeSeriesResponse
  """
  params = {}
  if query:
    params['query'] = query
  if from_time:
    if isinstance(from_time, datetime.datetime):
      from_time = from_time.isoformat()
    params['from'] = from_time
  if to_time:
    if isinstance(to_time, datetime.datetime):
      to_time = to_time.isoformat()
    params['to'] = to_time
  
  resp = resource_root.get(TIME_SERIES_PATH, params=params)
  return ApiList.from_json_dict(ApiTimeSeriesResponse, resp, resource_root)

def get_metric_schema(resource_root):
  """
  Get the schema for all of the metrics.
  @return List of metric schema.
  """
  resp = resource_root.get(METRIC_SCHEMA_PATH)
  return ApiList.from_json_dict(ApiMetricSchema, resp, resource_root)


class ApiTimeSeriesData(BaseApiObject):
  _ATTRIBUTES = {
    'timestamp' : ROAttr(datetime.datetime),
    'value'     : ROAttr(),
    'type'      : ROAttr(),
    }

class ApiTimeSeriesMetadata(BaseApiObject):
  _ATTRIBUTES = {
    'metricName'        : ROAttr(),
    'entityName'        : ROAttr(),
    'startTime'         : ROAttr(datetime.datetime),
    'endTime'           : ROAttr(datetime.datetime),
    'attributes'        : ROAttr(),
    'unitNumerators'    : ROAttr(),
    'unitDenominators'  : ROAttr(),
    }

class ApiTimeSeries(BaseApiObject):
  _ATTRIBUTES = {
    'metadata'          : ROAttr(ApiTimeSeriesMetadata),
    'data'              : ROAttr(ApiTimeSeriesData),
    }

class ApiTimeSeriesResponse(BaseApiObject):
  _ATTRIBUTES = {
    'timeSeries'      : ROAttr(ApiTimeSeries),
    'warnings'        : ROAttr(),
    'errors'          : ROAttr(),
    'timeSeriesQuery' : ROAttr(),
    }

class ApiMetricSchema(BaseApiObject):
  _ATTRIBUTES = {
    'name'            : ROAttr(),
    'isCounter'       : ROAttr(),
    'unitNumerator'   : ROAttr(),
    'unitDenominator' : ROAttr(),
    'aliases'         : ROAttr(),
    'sources'         : ROAttr(),
    }

