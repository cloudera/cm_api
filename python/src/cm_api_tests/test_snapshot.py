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

class TestSnapshotTypes(unittest.TestCase):
  def __init__(self, methodName):
    super(TestSnapshotTypes, self).__init__(methodName)
    self.resource = utils.MockResource(self)

  def test_hdfs_arguments(self):
    RAW = '''{"pathPatterns" : "/user/oozie"}'''
    args = utils.deserialize(RAW, ApiHdfsSnapshotPolicyArguments)
    self.assertEquals('/user/oozie', args.pathPatterns)

  def test_hbase_arguments(self):
    RAW = '''{"tableRegExps" : "table1", "storage" : "LOCAL"}'''
    args = utils.deserialize(RAW, ApiHBaseSnapshotPolicyArguments)
    self.assertEquals('table1', args.tableRegExps)
    self.assertEquals('LOCAL', args.storage)

  def test_hbase_snapshot(self):
    RAW = '''{
      "snapshotName" : "sn1",
      "tableName" : "table1",
      "creationTime" : "2012-12-10T23:11:31.041Z",
      "storage" : "LOCAL" }'''
    args = utils.deserialize(RAW, ApiHBaseSnapshot)
    self.assertEquals('sn1', args.snapshotName)
    self.assertEquals('table1', args.tableName)
    self.assertEquals(self._parse_time("2012-12-10T23:11:31.041Z"), args.creationTime)
    self.assertEquals('LOCAL', args.storage)

  def test_hdfs_snapshot(self):
    RAW = '''{
      "path" : "/abc",
      "snapshotName" : "sn1",
      "snapshotPath" : "/abc/.snapshot/sn1",
      "creationTime" : "2012-12-10T23:11:31.041Z" }'''
    args = utils.deserialize(RAW, ApiHdfsSnapshot)
    self.assertEquals('/abc', args.path)
    self.assertEquals('sn1', args.snapshotName)
    self.assertEquals('/abc/.snapshot/sn1', args.snapshotPath)
    self.assertEquals(self._parse_time("2012-12-10T23:11:31.041Z"), args.creationTime)

  def test_hbase_snapshot_error(self):
    RAW = '''{
      "snapshotName" : "sn1",
      "tableName" : "table1",
      "error" : "bad snapshot",
      "storage" : "LOCAL" }'''
    args = utils.deserialize(RAW, ApiHBaseSnapshotError)
    self.assertEquals('sn1', args.snapshotName)
    self.assertEquals('table1', args.tableName)
    self.assertEquals('bad snapshot', args.error)
    self.assertEquals('LOCAL', args.storage)

  def test_hdfs_snapshot_error(self):
    RAW = '''{
      "snapshotPath" : "/abc/.snapshot/sn1",
      "snapshotName" : "sn1",
      "path" : "/abc",
      "error" : "bad snapshot" }'''
    args = utils.deserialize(RAW, ApiHdfsSnapshotError)
    self.assertEquals('/abc/.snapshot/sn1', args.snapshotPath)
    self.assertEquals('/abc', args.path)
    self.assertEquals('sn1', args.snapshotName)
    self.assertEquals('bad snapshot', args.error)

  def test_hbase_snapshot_result(self):
    RAW = '''{
      "processedTableCount" : 5,
      "processedTables"     : ["t1", "t2", "t3", "t4", "t5"],
      "unprocessedTableCount" : "2",
      "unprocessedTables" : ["nt1", "nt2"],
      "createdSnapshotCount" : 5,
      "createdSnapshots" : [
          {"snapshotName" : "sn1",
          "tableName" : "t1",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn2",
          "tableName" : "t2",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn3",
          "tableName" : "t3",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn4",
          "tableName" : "t4",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn5",
          "tableName" : "t5",
          "creationTime" : "2012-12-10T23:11:31.041Z"}],
      "deletedSnapshotCount" : 1,
      "deletedSnapshots" : [
          {"snapshotName" : "dn1",
          "tableName" : "t1",
          "creationTime" : "2012-12-10T23:11:31.041Z"}],
      "creationErrorCount" : 1,
      "creationErrors" : [{
          "snapshotName" : "sn1",
          "tableName" : "table1",
          "error" : "bad snapshot",
          "storage" : "LOCAL"}],
      "deletionErrorCount" : 0,
      "deletionErrors" : []
       }'''

    args = utils.deserialize(RAW, ApiHBaseSnapshotResult)
    self.assertEquals(5, args.processedTableCount)
    self.assertEquals(["t1", "t2", "t3", "t4", "t5"], args.processedTables)
    self.assertEquals('2', args.unprocessedTableCount)
    self.assertEquals(['nt1', 'nt2'], args.unprocessedTables)
    self.assertEquals(5, args.createdSnapshotCount)
    self.assertEquals('t3', args.createdSnapshots[2].tableName)
    self.assertEquals(1, args.deletedSnapshotCount)
    self.assertEquals('dn1', args.deletedSnapshots[0].snapshotName)
    self.assertEquals(1, args.creationErrorCount)
    self.assertEquals("bad snapshot", args.creationErrors[0].error)
    self.assertEquals("LOCAL", args.creationErrors[0].storage)
    self.assertEquals(0, args.deletionErrorCount)
    self.assertEquals([], args.deletionErrors)

  def test_hdfs_snapshot_result(self):
    RAW = '''{
      "processedPathCount" : 5,
      "processedPaths"     : ["/t1", "/t2", "/t3", "/t4", "/t5"],
      "unprocessedPathCount" : "2",
      "unprocessedPaths" : ["nt1", "nt2"],
      "createdSnapshotCount" : 5,
      "createdSnapshots" : [
          {"snapshotName" : "sn1",
          "snapshotPath" : "/t1/.snapshot/sn1",
          "path" : "/t1",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn2",
          "snapshotPath" : "/t1/.snapshot/sn1",
          "path" : "/t2",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn3",
          "snapshotPath" : "/t1/.snapshot/sn1",
          "path" : "/t3",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn4",
          "snapshotPath" : "/t1/.snapshot/sn1",
          "path" : "/t4",
          "creationTime" : "2012-12-10T23:11:31.041Z"},
          {"snapshotName" : "sn5",
          "snapshotPath" : "/t1/.snapshot/sn1",
          "path" : "/t5",
          "creationTime" : "2012-12-10T23:11:31.041Z"}],
      "deletedSnapshotCount" : 1,
      "deletedSnapshots" : [
          {"snapshotName" : "dn1",
          "path" : "/t1",
          "snapshotPath" : "/t1/.snapshot/dn1",
          "creationTime" : "2012-12-10T23:11:31.041Z"}],
      "creationErrorCount" : 1,
      "creationErrors" : [{
          "snapshotName" : "sn1",
          "path" : "/t1",
          "snapshotPath" : "/t1/.snapshot/sn1",
          "error" : "bad snapshot"}],
      "deletionErrorCount" : 0,
      "deletionErrors" : []
       }'''

    args = utils.deserialize(RAW, ApiHdfsSnapshotResult)
    self.assertEquals(5, args.processedPathCount)
    self.assertEquals(["/t1", "/t2", "/t3", "/t4", "/t5"], args.processedPaths)
    self.assertEquals('2', args.unprocessedPathCount)
    self.assertEquals(['nt1', 'nt2'], args.unprocessedPaths)
    self.assertEquals(5, args.createdSnapshotCount)
    self.assertEquals('/t3', args.createdSnapshots[2].path)
    self.assertEquals(1, args.deletedSnapshotCount)
    self.assertEquals('dn1', args.deletedSnapshots[0].snapshotName)
    self.assertEquals(1, args.creationErrorCount)
    self.assertEquals("bad snapshot", args.creationErrors[0].error)
    self.assertEquals(0, args.deletionErrorCount)
    self.assertEquals([], args.deletionErrors)


  def _parse_time(self, tstr):
    return datetime.datetime.strptime(tstr, Attr.DATE_FMT) 


class TestSnapshotRequests(unittest.TestCase):

  def __init__(self, methodName):
    super(TestSnapshotRequests, self).__init__(methodName)
    self.resource = utils.MockResource(self)

  def test_snapshot(self):
    service = ApiService(self.resource, 'hdfs1', 'HDFS')
    service.__dict__['clusterRef'] = ApiClusterRef(self.resource, clusterName='cluster1')

    hdfs_args = ApiHdfsSnapshotPolicyArguments(self.resource, pathPatterns='/user/oozie')


    return_policy = ApiSnapshotPolicy(self.resource, name='sn1',
                                   weeklySnapshots=2,
                                   hdfsArguments=hdfs_args)
    return_policy.__dict__['id'] = 1
    return_list = ApiList([ return_policy ]).to_json_dict()
    self.resource.expect("POST",
        "/clusters/cluster1/services/hdfs1/snapshots/policies",
        retdata=return_list)

    policy = service.create_snapshot_policy(return_policy)
    self._test_policy(return_policy, policy)

    self.resource.expect("GET",
        "/clusters/cluster1/services/hdfs1/snapshots/policies",
        retdata=return_list)
    policies = service.get_snapshot_policies()
    self.assertEqual(1, len(policies))
    self._test_policy(return_policy, policies[0])

    self.resource.expect("GET",
        "/clusters/cluster1/services/hdfs1/snapshots/policies/sn1",
        retdata=return_policy.to_json_dict())
    self._test_policy(return_policy, service.get_snapshot_policy("sn1"))

    return_policy.dayOfWeek=5
    return_policy.paused=True
    self.resource.expect("PUT",
        "/clusters/cluster1/services/hdfs1/snapshots/policies/sn1",
        data=return_policy,
        retdata=return_policy.to_json_dict())
    policy = service.update_snapshot_policy('sn1', return_policy)
    self._test_policy(return_policy, policy)

    self.resource.expect("DELETE",
        "/clusters/cluster1/services/hdfs1/snapshots/policies/sn1",
        retdata=return_policy.to_json_dict())
    policy = service.delete_snapshot_policy('sn1')
    self._test_policy(return_policy, policy)

  def _test_policy(self, exp_policy, policy):
    self.assertIsInstance(policy, ApiSnapshotPolicy)
    self.assertEqual(exp_policy.name, policy.name)
    self.assertIsInstance(policy.hdfsArguments, ApiHdfsSnapshotPolicyArguments)
    self.assertEqual(exp_policy.hdfsArguments.pathPatterns, policy.hdfsArguments.pathPatterns)
    self.assertEqual(exp_policy.weeklySnapshots, policy.weeklySnapshots)
    self.assertEqual(exp_policy.dayOfWeek, policy.dayOfWeek)
    self.assertEqual(exp_policy.paused, policy.paused)

