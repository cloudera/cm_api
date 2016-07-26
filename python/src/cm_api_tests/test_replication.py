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
import random
import unittest
try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import *
from cm_api.endpoints.services import ApiService
from cm_api_tests import utils

class TestReplicationTypes(unittest.TestCase):

  def test_hdfs_arguments(self):
    RAW = '''{
      "sourceService" : {
        "peerName" : "vst2",
        "clusterName" : "Cluster 1 - CDH4",
        "serviceName" : "HDFS-1"
      },
      "sourcePath" : "/data",
      "destinationPath" : "/copy/data2",
      "mapreduceServiceName" : "MAPREDUCE-1",
      "schedulerPoolName" : "medium",
      "userName" : "systest",
      "dryRun" : false,
      "abortOnError" : true,
      "removeMissingFiles" : false,
      "preserveReplicationCount" : true,
      "preserveBlockSize" : true,
      "preservePermissions" : false,
      "skipTrash" : false,
      "replicationStrategy" : "DYNAMIC",
      "logPath" : "/tmp",
      "bandwidthPerMap" : "20",
      "preserveXAttrs" : false,
      "exclusionFilters" : ["ac"]
    }'''
    args = utils.deserialize(RAW, ApiHdfsReplicationArguments)
    self.assertEquals('vst2', args.sourceService.peerName)
    self.assertEquals('Cluster 1 - CDH4', args.sourceService.clusterName)
    self.assertEquals('HDFS-1', args.sourceService.serviceName)
    self.assertEquals('/data', args.sourcePath)
    self.assertEquals('/copy/data2', args.destinationPath)
    self.assertEquals('MAPREDUCE-1', args.mapreduceServiceName)
    self.assertEquals('medium', args.schedulerPoolName)
    self.assertEquals('systest', args.userName)
    self.assertFalse(args.dryRun)
    self.assertTrue(args.abortOnError)
    self.assertFalse(args.removeMissingFiles)
    self.assertTrue(args.preserveBlockSize)
    self.assertFalse(args.preservePermissions)
    self.assertTrue(args.preserveReplicationCount)
    self.assertFalse(args.skipTrash)
    self.assertEquals('DYNAMIC', args.replicationStrategy)
    self.assertFalse(args.preserveXAttrs)

  def test_hdfs_cloud_arguments(self):
    RAW = '''{
      "sourceService" : {
        "peerName" : "vst2",
        "clusterName" : "Cluster 1 - CDH4",
        "serviceName" : "HDFS-1"
      },
      "sourcePath" : "/data",
      "destinationPath" : "/copy/data2",
      "mapreduceServiceName" : "MAPREDUCE-1",
      "schedulerPoolName" : "medium",
      "userName" : "systest",
      "dryRun" : false,
      "abortOnError" : true,
      "removeMissingFiles" : false,
      "preserveReplicationCount" : true,
      "preserveBlockSize" : true,
      "preservePermissions" : false,
      "skipTrash" : false,
      "replicationStrategy" : "DYNAMIC",
      "logPath" : "/tmp",
      "bandwidthPerMap" : "20",
      "preserveXAttrs" : false,
      "exclusionFilters" : ["ac"],
      "sourceAccount" : "someTestAccount"
    }'''
    args = utils.deserialize(RAW, ApiHdfsCloudReplicationArguments)
    self.assertEquals('vst2', args.sourceService.peerName)
    self.assertEquals('Cluster 1 - CDH4', args.sourceService.clusterName)
    self.assertEquals('HDFS-1', args.sourceService.serviceName)
    self.assertEquals('/data', args.sourcePath)
    self.assertEquals('/copy/data2', args.destinationPath)
    self.assertEquals('MAPREDUCE-1', args.mapreduceServiceName)
    self.assertEquals('medium', args.schedulerPoolName)
    self.assertEquals('systest', args.userName)
    self.assertFalse(args.dryRun)
    self.assertTrue(args.abortOnError)
    self.assertFalse(args.removeMissingFiles)
    self.assertTrue(args.preserveBlockSize)
    self.assertFalse(args.preservePermissions)
    self.assertTrue(args.preserveReplicationCount)
    self.assertFalse(args.skipTrash)
    self.assertEquals('DYNAMIC', args.replicationStrategy)
    self.assertFalse(args.preserveXAttrs)
    self.assertEquals('someTestAccount', args.sourceAccount)
    self.assertEquals(None, args.destinationAccount)

  def test_hive_arguments(self):
    RAW = '''{
      "sourceService" : {
        "peerName" : "vst2",
        "clusterName" : "Cluster 1 - CDH4",
        "serviceName" : "HIVE-1"
      },
      "force" : true,
      "replicateData" : true,
      "hdfsArguments" : {
        "mapreduceServiceName" : "MAPREDUCE-1",
        "dryRun" : false,
        "abortOnError" : false,
        "removeMissingFiles" : false,
        "preserveReplicationCount" : false,
        "preserveBlockSize" : false,
        "preservePermissions" : false,
        "skipTrash" : false,
        "logPath" : "/tmp",
        "bandwidthPerMap" : "20",
        "preserveXAttrs" : false,
        "exclusionFilters" : ["ac"]
      },
      "tableFilters" : [
        { "database" : "db1", "tableName" : "table1" }
      ],
      "dryRun" : false,
      "replicateImpalaMetadata" : true
    }'''
    args = utils.deserialize(RAW, ApiHiveReplicationArguments)
    self.assertEquals('vst2', args.sourceService.peerName)
    self.assertEquals('Cluster 1 - CDH4', args.sourceService.clusterName)
    self.assertEquals('HIVE-1', args.sourceService.serviceName)
    self.assertTrue(args.force)
    self.assertTrue(args.replicateData)
    self.assertIsInstance(args.hdfsArguments, ApiHdfsReplicationArguments)
    self.assertIsInstance(args.tableFilters, list)
    self.assertEquals(1, len(args.tableFilters))
    self.assertIsInstance(args.tableFilters[0], ApiHiveTable)
    self.assertEquals("db1", args.tableFilters[0].database)
    self.assertEquals("table1", args.tableFilters[0].tableName)
    self.assertTrue(args.replicateImpalaMetadata)

  def test_hive_results(self):
    RAW = '''{
      "phase" : "EXPORT",
      "tableCount" : 1,
      "tables" : [
        { "database" : "db1", "tableName" : "table1" }
      ],
      "impalaUDFCount" : 1,
      "impalaUDFs" : [
        { "database" : "db1", "signature" : "func1(STRING)" }
      ],
      "hiveUDFCount" : 2,
      "hiveUDFs" : [
        { "database" : "db1", "signature" : "func1(STRING)" },
        { "database" : "db2", "signature" : "func2(STRING)" }
      ],
      "errorCount" : 1,
      "errors" : [
        { "database" : "db1", "tableName" : "table2",
          "impalaUDF" : "func2(INT)", "error" : "message" }
      ],
      "dataReplicationResult" : {
        "progress" : 50
      },
      "dryRun" : false,
      "runAsUser" : "systest"
    }'''
    res = utils.deserialize(RAW, ApiHiveReplicationResult)
    self.assertEquals('EXPORT', res.phase)
    self.assertEquals(1, res.tableCount)
    self.assertEquals(1, len(res.tables))
    self.assertEquals('db1', res.tables[0].database)
    self.assertEquals('table1', res.tables[0].tableName)
    self.assertEquals(1, res.impalaUDFCount)
    self.assertEquals(1, len(res.impalaUDFs))
    self.assertEquals('db1', res.impalaUDFs[0].database)
    self.assertEquals('func1(STRING)', res.impalaUDFs[0].signature)
    self.assertEquals(2, res.hiveUDFCount)
    self.assertEquals(2, len(res.hiveUDFs))
    self.assertEquals('db1', res.hiveUDFs[0].database)
    self.assertEquals('func1(STRING)', res.hiveUDFs[0].signature)
    self.assertEquals('db2', res.hiveUDFs[1].database)
    self.assertEquals('func2(STRING)', res.hiveUDFs[1].signature)
    self.assertEquals(1, res.errorCount)
    self.assertEquals('db1', res.errors[0]['database'])
    self.assertEquals('table2', res.errors[0]['tableName'])
    self.assertEquals('func2(INT)', res.errors[0]['impalaUDF'])
    self.assertEquals('message', res.errors[0]['error'])
    self.assertEquals(50, res.dataReplicationResult.progress)
    self.assertFalse(res.dryRun)
    self.assertEquals(res.runAsUser, 'systest')

  def test_schedule(self):
    RAW = '''{
      "id" : 39,
      "startTime" : "2012-12-10T23:11:31.041Z",
      "interval" : 1,
      "intervalUnit" : "DAY",
      "paused" : false,
      "nextRun" : "2013-01-15T23:11:31.041Z",
      "active" : true,
      "history" : [ {
        "id" : 738,
        "name" : "HiveReplicationCommand",
        "startTime" : "2013-01-15T18:28:24.895Z",
        "endTime" : "2013-01-15T18:30:49.446Z",
        "active" : false,
        "success" : true,
        "canRetry" : false,
        "resultMessage" : "Hive Replication Finished Successfully.",
        "resultDataUrl" : "/cmf/command/738/download",
        "serviceRef" : {
          "clusterName" : "Cluster 1 - CDH4",
          "serviceName" : "HIVE-1"
        },
        "hiveResult" : {
          "tableCount" : 3,
          "tables" : [ {
            "database" : "default",
            "tableName" : "repl_test_1"
          }, {
            "database" : "default",
            "tableName" : "sample_07"
          }, {
            "database" : "default",
            "tableName" : "sample_08"
          } ],
          "errorCount" : 0,
          "errors" : [ ],
          "dataReplicationResult" : {
            "progress" : 100,
            "numFilesCopied" : 0,
            "numBytesCopied" : 0,
            "numFilesSkipped" : 3,
            "numBytesSkipped" : 92158,
            "numFilesDeleted" : 0,
            "numFilesCopyFailed" : 0,
            "numBytesCopyFailed" : 0,
            "dryRun" : false,
            "failedFiles": [ ]
          },
          "dryRun" : false,
          "runAsUser" : "systest"
        }
      } ],
      "alertOnStart" : false,
      "alertOnSuccess" : false,
      "alertOnFail" : false,
      "alertOnAbort" : false,
      "hiveArguments" : {
        "sourceService" : {
          "peerName" : "vst2",
          "clusterName" : "Cluster 1 - CDH4",
          "serviceName" : "HIVE-1"
        },
        "force" : true,
        "replicateData" : true,
        "hdfsArguments" : {
          "mapreduceServiceName" : "MAPREDUCE-1",
          "dryRun" : false,
          "abortOnError" : false,
          "removeMissingFiles" : false,
          "preserveReplicationCount" : false,
          "preserveBlockSize" : false,
          "preservePermissions" : false,
          "skipTrash" : false,
          "preserveXAttrs" : false
        },
        "dryRun" : false
      }
    }'''
    sched = utils.deserialize(RAW, ApiReplicationSchedule)
    self.assertEqual(39, sched.id)
    self.assertEqual(self._parse_time("2012-12-10T23:11:31.041Z"), sched.startTime)
    self.assertEqual('DAY', sched.intervalUnit)
    self.assertEqual(1, sched.interval)
    self.assertFalse(sched.paused)
    self.assertEqual(self._parse_time("2013-01-15T23:11:31.041Z"), sched.nextRun)
    self.assertFalse(sched.alertOnStart)
    self.assertIsNotNone(sched.hiveArguments)

    self.assertEqual(1, len(sched.history))
    self.assertIsInstance(sched.history[0], ApiReplicationCommand)
    self.assertEqual('default', sched.history[0].hiveResult.tables[0].database)
    self.assertEqual(92158, sched.history[0].hiveResult.dataReplicationResult.numBytesSkipped)
    self.assertEqual(3, sched.history[0].hiveResult.tableCount)
    self.assertEqual(0, sched.history[0].hiveResult.errorCount)

  def test_peers(self):
    RAW = '''{
      "name" : "peer1",
      "url" : "http://peer1",
      "username" : "user1",
      "password" : "pwd"
    }'''
    peer = ApiCmPeer.from_json_dict(json.loads(RAW), None)
    self.assertEquals("peer1", peer.name)
    self.assertEquals("http://peer1", peer.url)
    self.assertEquals("user1", peer.username)
    self.assertEquals("pwd", peer.password)

  def _parse_time(self, tstr):
    return datetime.datetime.strptime(tstr, Attr.DATE_FMT)


class TestReplicationRequests(unittest.TestCase):

  def __init__(self, methodName):
    super(TestReplicationRequests, self).__init__(methodName)
    self.resource = utils.MockResource(self)

  def test_replication_crud(self):
    service = ApiService(self.resource, 'hdfs1', 'HDFS')
    service.__dict__['clusterRef'] = ApiClusterRef(self.resource, clusterName='cluster1')

    hdfs_args = ApiHdfsReplicationArguments(self.resource)
    hdfs_args.sourceService = ApiServiceRef('cluster2', 'hdfs2')
    hdfs_args.sourcePath = '/src'
    hdfs_args.destinationPath = '/dst'

    return_sched = ApiReplicationSchedule(self.resource,
        interval=2, intervalUnit='DAY')
    return_sched.hdfsArguments = hdfs_args
    return_sched.__dict__['id'] = 1
    return_list = ApiList([ return_sched ]).to_json_dict()

    self.resource.expect("POST",
        "/clusters/cluster1/services/hdfs1/replications",
        retdata=return_list)

    sched = service.create_replication_schedule(
        None, None, 'DAY', 2, True, hdfs_args, alert_on_fail=True)
    self.assertEqual(return_sched.intervalUnit, sched.intervalUnit)
    self.assertEqual(return_sched.interval, sched.interval)
    self.assertIsInstance(sched.hdfsArguments, ApiHdfsReplicationArguments)

    self.resource.expect("GET",
        "/clusters/cluster1/services/hdfs1/replications",
        retdata=return_list)
    service.get_replication_schedules()

    self.resource.expect("GET",
        "/clusters/cluster1/services/hdfs1/replications/1",
        retdata=return_sched.to_json_dict())
    service.get_replication_schedule(1)

    self.resource.expect("PUT",
        "/clusters/cluster1/services/hdfs1/replications/1",
        data=return_sched,
        retdata=return_sched.to_json_dict())
    service.update_replication_schedule(1, return_sched)

    self.resource.expect("DELETE",
        "/clusters/cluster1/services/hdfs1/replications/1",
        retdata=return_sched.to_json_dict())
    service.delete_replication_schedule(1)

  def test_hdfs_cloud_replication_crud(self):
    service = ApiService(self.resource, 'hdfs1', 'HDFS')
    service.__dict__['clusterRef'] = ApiClusterRef(self.resource, clusterName='cluster1')

    hdfs_args = ApiHdfsCloudReplicationArguments(self.resource)
    hdfs_args.sourceService = ApiServiceRef('cluster2', 'hdfs2')
    hdfs_args.sourcePath = '/src'
    hdfs_args.destinationPath = 's3a://somebucket/dst'
    hdfs_args.destinationAccount = 'someTestAccount'

    return_sched = ApiReplicationSchedule(self.resource,
        interval=2, intervalUnit='DAY')
    return_sched.hdfsCloudArguments = hdfs_args
    return_sched.__dict__['id'] = 1
    return_list = ApiList([ return_sched ]).to_json_dict()

    self.resource.expect("POST",
        "/clusters/cluster1/services/hdfs1/replications",
        retdata=return_list)

    sched = service.create_replication_schedule(
        None, None, 'DAY', 2, True, hdfs_args, alert_on_fail=True)
    self.assertEqual(return_sched.intervalUnit, sched.intervalUnit)
    self.assertEqual(return_sched.interval, sched.interval)
    self.assertIsInstance(sched.hdfsCloudArguments, ApiHdfsCloudReplicationArguments)

    self.resource.expect("GET",
        "/clusters/cluster1/services/hdfs1/replications",
        retdata=return_list)
    service.get_replication_schedules()

    self.resource.expect("GET",
        "/clusters/cluster1/services/hdfs1/replications/1",
        retdata=return_sched.to_json_dict())
    service.get_replication_schedule(1)

    self.resource.expect("PUT",
        "/clusters/cluster1/services/hdfs1/replications/1",
        data=return_sched,
        retdata=return_sched.to_json_dict())
    service.update_replication_schedule(1, return_sched)

    self.resource.expect("DELETE",
        "/clusters/cluster1/services/hdfs1/replications/1",
        retdata=return_sched.to_json_dict())
    service.delete_replication_schedule(1)

if __name__ == '__main__':
    unittest.main()
