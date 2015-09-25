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
            "type" : "SAMPLE",
            "aggregateStatistics" : {
              "sampleTime": "2013-05-08T22:58:06.000Z",
              "sampleValue": 317,
              "count": 3,
              "min": 305,
              "minTime": "2013-05-08T22:58:06.000Z",
              "max": 317,
              "maxTime": "2013-05-08T22:58:06.000Z",
              "mean": 311.6666666666667,
              "stdDev": 6.110100926606199,
              "crossEntityMetadata": {
                "maxEntityDisplayName": "DATANODE (host1.com)",
                "maxEntityName": "e37d8344",
                "minEntityDisplayName": "DATANODE (host2.com)",
                "minEntityName": "0891ee5b",
                "numEntities": 3
              }
            }
          }, {
            "timestamp" : "2013-05-08T23:00:06.000Z",
            "value" : 3.5,
            "type" : "SAMPLE",
            "aggregateStatistics" : {
              "sampleTime": "2013-05-08T22:58:07.000Z",
              "sampleValue": 319,
              "count": 3,
              "min": 304,
              "minTime": "2013-05-08T22:58:07.000Z",
              "max": 319,
              "maxTime": "2013-05-08T22:58:07.000Z",
              "mean": 311.6666666666667,
              "stdDev": 6.110100926606199
            }
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
    time = datetime.datetime.now()
    api_resource.expect("GET", "/timeseries",
                        retdata=json.loads(TIME_SERIES),
                        params={ 'from':time.isoformat(),
                                 'to':time.isoformat(),
                                 'query':'select cpu_percent'})
    responses = query_timeseries(api_resource,
                                 "select cpu_percent",
                                 time,
                                 time)
    self._verify_timeseries_response(responses)

    # Test the with-rollups call
    api_resource.expect("GET", "/timeseries",
                        retdata=json.loads(TIME_SERIES),
                        params={ 'from':time.isoformat(),
                                 'to':time.isoformat(),
                                 'query':'select cpu_percent',
                                 'desiredRollup':'RAW',
                                 'mustUseDesiredRollup': True})
    responses = query_timeseries(api_resource,
                                 "select cpu_percent",
                                 time,
                                 time,
                                 "RAW",
                                 True)
    self._verify_timeseries_response(responses)

    # Test query_timeseries_with_long_query_string
    api_resource.expect("POST", "/timeseries",
                        retdata=json.loads(TIME_SERIES),
                        data={ 'query':'select cpu_percent'},
                        params={ 'from':time.isoformat(),
                                 'to':time.isoformat()})
    responses = query_timeseries(
      api_resource, "select cpu_percent", time, time, by_post=True)
    self._verify_timeseries_response(responses)

    api_resource.expect("POST", "/timeseries",
                        retdata=json.loads(TIME_SERIES),
                        data={ 'query':'select cpu_percent'},
                        params={ 'from':time.isoformat(),
                                 'to':time.isoformat(),
                                 'desiredRollup':'RAW',
                                 'mustUseDesiredRollup': True})
    responses = query_timeseries(api_resource, "select cpu_percent",
      time, time, "RAW", True, by_post=True)
    self._verify_timeseries_response(responses)

  def _verify_timeseries_response(self, responses):
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
    # first and second points have aggregate data.
    data  = timeseries.data[0]
    self.assertIsNotNone(data.aggregateStatistics)
    self.assertIsInstance(data.aggregateStatistics, ApiTimeSeriesAggregateStatistics)
    self.assertEqual(317, data.aggregateStatistics.sampleValue)
    xEntityMetadata = data.aggregateStatistics.crossEntityMetadata
    self.assertIsNotNone(xEntityMetadata)
    self.assertIsInstance(xEntityMetadata, ApiTimeSeriesCrossEntityMetadata)
    self.assertEqual("DATANODE (host1.com)", xEntityMetadata.maxEntityDisplayName)
    self.assertEqual("e37d8344", xEntityMetadata.maxEntityName)
    self.assertEqual("DATANODE (host2.com)", xEntityMetadata.minEntityDisplayName)
    self.assertEqual("0891ee5b", xEntityMetadata.minEntityName)
    data  = timeseries.data[1]
    self.assertIsNotNone(data.aggregateStatistics)
    self.assertIsInstance(data.aggregateStatistics, ApiTimeSeriesAggregateStatistics)
    self.assertEqual(319, data.aggregateStatistics.sampleValue)
    xEntityMetadata = data.aggregateStatistics.crossEntityMetadata
    self.assertIsNone(xEntityMetadata)

  def test_get_metric_schema(self):
    METRICS = '''{
      "items" : [ {
        "name" : "event_drain_success_count_flume_sink_min_rate",
        "displayName" : "Event Drain Successes Across Flume Sinks",
        "description" : "Event Drain Successes aggregate computed across all Flume Sinks.",
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
        "displayName" : "Dropped Packets (Receive)",
        "description" : "The number of packets dropped by the network interface (receive)",
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
    self.assertEqual("Event Drain Successes Across Flume Sinks",
                     metric.displayName)
    self.assertEqual("Event Drain Successes aggregate computed across all Flume Sinks.",
                     metric.description)
    self.assertFalse(metric.isCounter)
    self.assertEqual("events", metric.unitNumerator)
    self.assertEqual("seconds", metric.unitDenominator)
    self.assertFalse(metric.aliases)
    self.assertIsInstance(metric.sources, dict)
    metric = metrics[1]
    self.assertIsInstance(metric, ApiMetricSchema)
    self.assertEqual("drop_receive", metric.name)
    self.assertEqual("Dropped Packets (Receive)",
                     metric.displayName)
    self.assertEqual("The number of packets dropped by the network interface (receive)",
                     metric.description)
    self.assertTrue(metric.isCounter)
    self.assertEqual("packets", metric.unitNumerator)
    self.assertFalse( metric.unitDenominator)
    self.assertIsInstance(metric.aliases, list)
    self.assertEquals("network_interface_drop_receive", metric.aliases[0])
    self.assertIsInstance(metric.sources, dict)
    self.assertEquals("enterprise", metric.sources["NETWORK_INTERFACE"][0])

  def test_get_entity_types(self):
    ENTITY_TYPES = '''{
      "items": [ {
        "name": "CLUSTER",
        "category": "CLUSTER",
        "nameForCrossEntityAggregateMetrics": "clusters",
        "displayName": "Cluster",
        "description": "A Cloudera Manager cluster. Called 'CLUSTER' in queries.",
        "immutableAttributeNames": [
          "clusterName",
          "clusterId"],
        "mutableAttributeNames": [
          "clusterDisplayName",
          "version"],
        "entityNameFormat": [ "clusterId" ],
        "entityDisplayNameForamt": "$clusterDisplayName",
        "parentMetricEntityTypeNames": []
      }, {
        "name": "RACK",
        "category": "RACK",
        "nameForCrossEntityAggregateMetrics": "racks",
        "displayName": "Rack",
        "description": "A rack. Called 'RACK' in queries.",
        "immutableAttributeNames": [ "rackId" ],
        "mutableAttributeNames": [],
        "entityNameFormat": [ "rackId" ],
        "parentMetricEntityTypeNames": []
      } ]
    }'''

    api_resource = utils.MockResource(self)
    api_resource.expect("GET", "/timeseries/entityTypes",
                        retdata=json.loads(ENTITY_TYPES))
    entity_types = get_entity_types(api_resource)

    self.assertIsInstance(entity_types, ApiList)
    self.assertEqual(2, len(entity_types))
    cluster_entity = entity_types[0]
    self.assertIsInstance(cluster_entity, ApiTimeSeriesEntityType)
    self.assertEqual("CLUSTER",
                     cluster_entity.name)
    self.assertEqual("CLUSTER",
                     cluster_entity.category)
    self.assertEqual("clusters",
                     cluster_entity.nameForCrossEntityAggregateMetrics)
    self.assertEqual("Cluster",
                     cluster_entity.displayName)
    self.assertEqual("A Cloudera Manager cluster. Called 'CLUSTER' in queries.",
                     cluster_entity.description)
    self.assertEqual("$clusterDisplayName",
                     cluster_entity.entityDisplayNameForamt)

    self.assertIsInstance(cluster_entity.immutableAttributeNames, list)
    self.assertEqual(2, len(cluster_entity.immutableAttributeNames))
    self.assertEqual("clusterName",
                     cluster_entity.immutableAttributeNames[0])
    self.assertEqual("clusterId",
                     cluster_entity.immutableAttributeNames[1])

    self.assertIsInstance(cluster_entity.mutableAttributeNames, list)
    self.assertEqual(2, len(cluster_entity.mutableAttributeNames))
    self.assertEqual("clusterDisplayName",
                     cluster_entity.mutableAttributeNames[0])
    self.assertEqual("version",
                     cluster_entity.mutableAttributeNames[1])

    self.assertIsInstance(cluster_entity.entityNameFormat, list)
    self.assertEqual(1, len(cluster_entity.entityNameFormat))
    self.assertEqual("clusterId",
                     cluster_entity.entityNameFormat[0])

    self.assertIsInstance(cluster_entity.parentMetricEntityTypeNames, list)
    self.assertEqual(0, len(cluster_entity.parentMetricEntityTypeNames))

  def test_get_entity_attributes(self):
    ENTITY_ATTRIBUTES = '''{
      "items": [ {
        "name": "entityName",
        "displayName": "Entity Name",
        "description": "The name of this entity.",
        "isValueCaseSensitive": false
      }, {
        "name": "roleConfigGroup",
        "displayName": "Role Config Group",
        "description": "The name of the role config group.",
        "isValueCaseSensitive": true
      } ]
    }'''

    api_resource = utils.MockResource(self)
    api_resource.expect("GET", "/timeseries/entityTypeAttributes",
                        retdata=json.loads(ENTITY_ATTRIBUTES))
    entity_attrs = get_entity_attributes(api_resource)

    self.assertIsInstance(entity_attrs, ApiList)
    self.assertEqual(2, len(entity_attrs))
    attr_1 = entity_attrs[0]
    self.assertIsInstance(attr_1, ApiTimeSeriesEntityAttribute)
    self.assertEqual("entityName",
                     attr_1.name)
    self.assertEqual(False,
                     attr_1.isValueCaseSensitive)
    self.assertEqual("Entity Name",
                     attr_1.displayName)
    self.assertEqual("The name of this entity.",
                     attr_1.description)

    attr_2 = entity_attrs[1]
    self.assertIsInstance(attr_2, ApiTimeSeriesEntityAttribute)
    self.assertEqual("roleConfigGroup",
                     attr_2.name)
    self.assertEqual(True,
                     attr_2.isValueCaseSensitive)
    self.assertEqual("Role Config Group",
                     attr_2.displayName)
    self.assertEqual("The name of the role config group.",
                     attr_2.description)
