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
from cm_api.endpoints.timeseries import *
from cm_api.endpoints.types import ApiList
from cm_api_tests import utils

try:
  import json
except ImportError:
  import simplejson as json

class TestTimeSeries(unittest.TestCase):

  def test_query_timeseries(self):
    TIME_SERIES = '''{
      "items" : [ {
        "timeSeries" : [ {
          "metadata" : {
            "metricName" : "cpu_percent",
            "entityName" : "localhost",
            "startTime" : "2013-05-08T22:58:28.868Z",
            "endTime" : "2013-05-08T23:03:28.868Z",
            "attributes" : {
              "hostname" : "localhost",
              "hostId" : "localhost",
              "rackId" : "/default",
              "category" : "HOST",
              "entityName" : "localhost"
            },
            "unitNumerators" : [ "percent" ],
            "unitDenominators" : [ ]
          },
          "data" : [ {
            "timestamp" : "2013-05-08T22:59:06.000Z",
            "value" : 1.7,
            "type" : "SAMPLE"
          }, {
            "timestamp" : "2013-05-08T23:00:06.000Z",
            "value" : 3.5,
            "type" : "SAMPLE"
          }, {
            "timestamp" : "2013-05-08T23:01:06.000Z",
            "value" : 2.1,
            "type" : "SAMPLE"
          }, {
            "timestamp" : "2013-05-08T23:02:06.000Z",
            "value" : 1.5,
            "type" : "SAMPLE"
          }, {
            "timestamp" : "2013-05-08T23:03:06.000Z",
            "value" : 1.7,
            "type" : "SAMPLE"
          } ]
        } ],
        "warnings" : [ ],
        "timeSeriesQuery" : "select cpu_percent"
      } ]
    }'''

    api_resource = utils.MockResource(self)
    api_resource.expect("GET", "/timeseries", 
                        retdata=json.loads(TIME_SERIES))
    responses = query_timeseries(api_resource, 
                                 "select cpu_percent", 
                                 None, 
                                 None)
    
    self.assertIsInstance(responses, ApiList)
    self.assertEqual(1, len(responses))
    response = responses[0]
    self.assertIsInstance(response, ApiTimeSeriesResponse)
    self.assertEqual("select cpu_percent", response.timeSeriesQuery)
    self.assertFalse(response.warnings)
    self.assertFalse(response.errors)
    self.assertEqual(1, len(response.timeSeries))
    timeseries = response.timeSeries[0]
    self.assertIsInstance(timeseries, ApiTimeSeries)
    metadata = timeseries.metadata
    self.assertIsInstance(metadata, ApiTimeSeriesMetadata)
    self.assertEqual("cpu_percent", metadata.metricName)
    self.assertEqual("localhost", metadata.entityName)
    self.assertIsInstance(metadata.attributes, dict)
    self.assertEqual("cpu_percent", metadata.metricName)
    self.assertEqual(5, len(timeseries.data))
    for data in timeseries.data:
      self.assertIsInstance(data, ApiTimeSeriesData)
      self.assertEqual("SAMPLE", data.type)
      self.assertIsInstance(data.timestamp, datetime.datetime)
      self.assertTrue(data.value)


  def test_get_metric_schema(self):
    METRICS = '''{
      "items" : [ {
        "name" : "event_drain_success_count_flume_sink_min_rate",
        "isCounter" : false,
        "unitNumerator" : "events",
        "unitDenominator" : "seconds",
        "aliases" : [ ],
        "sources" : {
          "CLUSTER" : [ "cdh3", "cdh4" ],
          "FLUME" : [ "cdh3", "cdh4" ]
        }
      }, {
        "name" : "drop_receive",
        "isCounter" : true,
        "unitNumerator" : "packets",
        "aliases" : [ "network_interface_drop_receive" ],
        "sources" : {
          "NETWORK_INTERFACE" : [ "enterprise" ]
        }
      } ]
    }'''

    api_resource = utils.MockResource(self)
    api_resource.expect("GET", "/timeseries/schema", 
                        retdata=json.loads(METRICS))
    metrics = get_metric_schema(api_resource)
    
    self.assertIsInstance(metrics, ApiList)
    self.assertEqual(2, len(metrics))
    metric = metrics[0]
    self.assertIsInstance(metric, ApiMetricSchema)
    self.assertEqual("event_drain_success_count_flume_sink_min_rate", 
                     metric.name)
    self.assertFalse(metric.isCounter)
    self.assertEqual("events", metric.unitNumerator)
    self.assertEqual("seconds", metric.unitDenominator)
    self.assertFalse(metric.aliases)
    self.assertIsInstance(metric.sources, dict)
    metric = metrics[1]
    self.assertIsInstance(metric, ApiMetricSchema)
    self.assertEqual("drop_receive", metric.name)
    self.assertTrue(metric.isCounter)
    self.assertEqual("packets", metric.unitNumerator)
    self.assertFalse( metric.unitDenominator)
    self.assertIsInstance(metric.aliases, list)
    self.assertEquals("network_interface_drop_receive", metric.aliases[0])
    self.assertIsInstance(metric.sources, dict)
    self.assertEquals("enterprise", metric.sources["NETWORK_INTERFACE"][0])
