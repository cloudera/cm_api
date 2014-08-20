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

from cm_api.endpoints.types import *

__docformat__ = "epytext"

TIME_SERIES_PATH   = "/timeseries"
METRIC_SCHEMA_PATH = "/timeseries/schema"

def query_timeseries(resource_root, query, from_time=None, to_time=None,
    desired_rollup=None, must_use_desired_rollup=None):
  """
  Query for time series data from the CM time series data store.
  @param query: Query string.
  @param from_time: Start of the period to query (optional).
  @type from_time: datetime.datetime Note the that datetime must either be time
                   zone aware or specified in the server time zone. See the
                   python datetime documentation for more details about python's
                   time zone handling.
  @param to_time: End of the period to query (default = now).
                  This may be an ISO format string, or a datetime object.
  @type to_time: datetime.datetime Note the that datetime must either be time
                 zone aware or specified in the server time zone. See the
                 python datetime documentation for more details about python's
                 time zone handling.
  @param desired_rollup: The aggregate rollup to get data for. This can be
                         RAW, TEN_MINUTELY, HOURLY, SIX_HOURLY, DAILY, or
                         WEEKLY. Note that rollup desired is only a hint unless
                         must_use_desired_rollup is set to true.
  @param must_use_desired_rollup: Indicates that the monitoring server should
                                  return the data at the rollup desired.
  @return: List of ApiTimeSeriesResponse
  """
  params = {}
  if query:
    params['query'] = query
  if from_time:
    params['from'] = from_time.isoformat()
  if to_time:
    params['to'] = to_time.isoformat()
  if desired_rollup:
    params['desiredRollup'] = desired_rollup
  if must_use_desired_rollup:
    params['mustUseDesiredRollup'] = must_use_desired_rollup
  return call(resource_root.get, TIME_SERIES_PATH,
      ApiTimeSeriesResponse, True, params=params)

def get_metric_schema(resource_root):
  """
  Get the schema for all of the metrics.
  @return: List of metric schema.
  """
  return call(resource_root.get, METRIC_SCHEMA_PATH,
      ApiMetricSchema, True)

class ApiTimeSeriesCrossEntityMetadata(BaseApiObject):
  _ATTRIBUTES = {
    'maxEntityDisplayName' : ROAttr(),
    'minEntityDisplayName' : ROAttr(),
    'numEntities'          : ROAttr()
    }

class ApiTimeSeriesAggregateStatistics(BaseApiObject):
  _ATTRIBUTES = {
    'sampleTime'  : ROAttr(datetime.datetime),
    'sampleValue' : ROAttr(),
    'count'       : ROAttr(),
    'min'         : ROAttr(),
    'minTime'     : ROAttr(datetime.datetime),
    'max'         : ROAttr(),
    'maxTime'     : ROAttr(datetime.datetime),
    'mean'        : ROAttr(),
    'stdDev'      : ROAttr(),
    'crossEntityMetadata' : ROAttr(ApiTimeSeriesCrossEntityMetadata)
    }

class ApiTimeSeriesData(BaseApiObject):
  _ATTRIBUTES = {
    'timestamp' : ROAttr(datetime.datetime),
    'value'     : ROAttr(),
    'type'      : ROAttr(),
    'aggregateStatistics' : ROAttr(ApiTimeSeriesAggregateStatistics)
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
    'expression'        : ROAttr(),
    'alias'             : ROAttr(),
    'metricCollectionFrequencyMs': ROAttr(),
    'rollupUsed'        : ROAttr()
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
    'displayName'     : ROAttr(),
    'description'     : ROAttr(),
    'isCounter'       : ROAttr(),
    'unitNumerator'   : ROAttr(),
    'unitDenominator' : ROAttr(),
    'aliases'         : ROAttr(),
    'sources'         : ROAttr(),
    }

