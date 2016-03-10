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
from cm_api.endpoints.clusters import *
from cm_api.endpoints.types import *
from cm_api_tests import utils

class TestCluster(unittest.TestCase):

  def test_add_hosts(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")

    data = ApiList([ ApiHostRef(resource, hostId='foo') ])

    resource.expect("POST", "/clusters/foo/hosts",
        data=data,
        retdata={ 'items' : [ { 'hostId' : 'foo' } ] })
    cluster.add_hosts(['foo'])

  def test_update_cdh_version(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")

    data = ApiCluster(resource, name='foo', fullVersion='4.2.1')

    resource.expect("PUT", "/clusters/foo",
        data=data,
        retdata={ 'name' : 'foo'})
    cluster.update_cdh_version('4.2.1')

  def test_upgrade_cdh(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")

    data = dict()
    data['deployClientConfig'] = False
    data['startAllServices'] = True
    data['cdhParcelVersion'] = '5.0.0.1-cdh5-1.2.3'

    resource.expect("POST", "/clusters/foo/commands/upgradeCdh",
        data=data,
        retdata={ 'name' : 'foo'})
    cluster.upgrade_cdh(False, True, data['cdhParcelVersion'])

  def test_restart(self):
    resource = utils.MockResource(self, version=5)
    cluster = ApiCluster(resource, name="foo")
    resource.expect(
      method="POST",
      reqpath="/clusters/foo/commands/restart",
      data=None,
      retdata={'name' : 'foo'})
    cluster.restart();

    resource = utils.MockResource(self, version=7)
    newCluster = ApiCluster(resource, name="bar")
    data = dict()
    data['restartOnlyStaleServices'] = False
    data['redeployClientConfiguration'] = True
    resource.expect(
      method="POST",
      reqpath="/clusters/bar/commands/restart",
      data=data,
      retdata={'name' : 'bar'})
    newCluster.restart(False, True);

    resource = utils.MockResource(self, version=11)
    newCluster = ApiCluster(resource, name="bar")
    data = dict()
    data['restartOnlyStaleServices'] = False
    data['redeployClientConfiguration'] = True
    data['restartServiceNames'] = ["A", "B"]
    resource.expect(
      method="POST",
      reqpath="/clusters/bar/commands/restart",
      data=data,
      retdata={'name' : 'bar'})
    newCluster.restart(False, True, ["A", "B"]);

  def test_configure_for_kerberos(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")

    data = dict()
    data['datanodeTransceiverPort'] = 23456
    data['datanodeWebPort'] = 12345

    resource.expect("POST", "/clusters/foo/commands/configureForKerberos",
        data=data,
        retdata={ 'name' : 'foo'})
    cluster.configure_for_kerberos(23456, 12345)

  def test_export_cluster_template(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")
    resource.expect(
      method="GET",
      reqpath="/clusters/foo/export",
      params=dict(exportAutoConfig=True),
      retdata=ApiClusterTemplate(resource).to_json_dict())
    cluster.export(export_auto_config=True)

  def test_pools_refresh(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")

    resource.expect("POST", "/clusters/foo/commands/poolsRefresh",
        data=None,
        retdata={ 'name' : 'foo'})
    cluster.pools_refresh()

  def test_list_dfs_services(self):
    resource = utils.MockResource(self)
    cluster = ApiCluster(resource, name="foo")
    data = None
    resource.expect("GET", "/clusters/foo/dfsServices",
        data=data,
        retdata={ 'name' : 'foo'})
    cluster.list_dfs_services()

    data = None
    resource.expect("GET", "/clusters/foo/dfsServices?view=EXPORT",
        data=data,
        retdata={ 'name' : 'foo'})
    cluster.list_dfs_services(view="EXPORT")
