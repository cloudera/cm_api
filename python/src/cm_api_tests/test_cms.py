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
from cm_api.endpoints.cms import ClouderaManager
from cm_api.endpoints.types import config_to_json, ApiConfig, ApiCmPeer,\
  ApiCommand, ApiClusterTemplate
from cm_api_tests import utils

try:
  import json
except ImportError:
  import simplejson as json

# CM v10 ApiCmPeer json format: no 'type' field
def _make_cm_v10_format_peer(name, url):
  peer_json = """
    {
      "name": "%s",
      "url": "%s"
    }
    """
  return peer_json % (name, url)

# CM v11 ApiCmPeer json format: with 'type' field
def _make_cm_v11_format_peer(name, url, peer_type):
  peer_json = """
    {
      "name": "%s",
      "type": "%s",
      "url": "%s",
      "clouderaManagerCreatedUser" : true
    }
    """
  return peer_json % (name, peer_type, url)

SAMPLE_COMMAND_JSON = """
    {
      "id": 498,
      "name": "CM Peer Test",
      "startTime": "2015-03-31T23:56:41.066Z",
      "active": true,
      "children": {
        "items": []
      }
    }"""

class TestCMS(unittest.TestCase):

  def test_all_hosts_config(self):
    SUMMARY = """
      {
        "items" : [ {
          "name" : "blacklisted_parcel_products",
          "value" : "foo,bar"
        } ]
      }
      """
    FULL = """
      {
        "items" : [ {
          "name" : "blacklisted_parcel_products",
          "value" : "foo,bar",
          "required" : false,
          "default" : "",
          "displayName" : "Blacklisted Products",
          "description" : "Parcels for blacklisted products will not be distributed to the host, nor activated for process execution. Already distributed parcels will be undistributed. Already running process will not be affected until the next restart.",
          "validationState" : "OK"
        }, {
          "name" : "rm_enabled",
          "required" : false,
          "default" : "false",
          "displayName" : "Enable Resource Management",
          "description" : "Enables resource management for all roles on this host.",
          "validationState" : "OK",
          "validationWarningsSuppressed" : false
        } ]
      }
      """

    resource = utils.MockResource(self)
    cms = ClouderaManager(resource)

    resource.expect("GET", "/cm/allHosts/config", retdata=json.loads(SUMMARY))
    cfg = cms.get_all_hosts_config()
    self.assertIsInstance(cfg, dict)
    self.assertEqual(1, len(cfg))
    self.assertEqual('foo,bar', cfg.get('blacklisted_parcel_products'))

    resource.expect("GET", "/cm/allHosts/config", params={ 'view' : 'full' },
        retdata=json.loads(FULL))
    cfg = cms.get_all_hosts_config(view='full')
    self.assertIsInstance(cfg, dict)
    self.assertEqual(2, len(cfg))
    self.assertIsInstance(cfg['blacklisted_parcel_products'], ApiConfig)
    self.assertFalse(cfg['blacklisted_parcel_products'].required)
    self.assertEqual('OK', cfg['rm_enabled'].validationState)

    cfg = { 'blacklisted_parcel_products' : 'bar' }
    resource.expect("PUT", "/cm/allHosts/config", data=config_to_json(cfg),
        retdata=json.loads(SUMMARY))
    cms.update_all_hosts_config(cfg)

  def test_host_commission(self):
    resource = utils.MockResource(self)
    cms = ClouderaManager(resource)

    resource.expect("POST", "/cm/commands/hostsDecommission",
        data=[ "host1", "host2" ],
        retdata={})
    cms.hosts_decommission([ "host1", "host2" ])

    resource.expect("POST", "/cm/commands/hostsRecommission",
        data=[ "host1", "host2" ],
        retdata={})
    cms.hosts_recommission([ "host1", "host2" ])

  def test_host_commission_with_start(self):
    resource = utils.MockResource(self)
    cms = ClouderaManager(resource)

    resource.expect("POST", "/cm/commands/hostsDecommission",
        data=[ "host1", "host2" ],
        retdata={})
    cms.hosts_decommission([ "host1", "host2" ])

    resource.expect("POST", "/cm/commands/hostsRecommissionWithStart",
        data=[ "host1", "host2" ],
        retdata={})
    cms.hosts_recommission_with_start([ "host1", "host2" ])

  def test_host_offline(self):
    resource = utils.MockResource(self)
    cms = ClouderaManager(resource)

    resource.expect("POST", "/cm/commands/hostsOfflineOrDecommission",
        data=[ "host1", "host2" ],
        retdata={})
    cms.hosts_offline_or_decommission([ "host1", "host2" ])

    resource.expect("POST", "/cm/commands/hostsOfflineOrDecommission",
        data=[ "host1", "host2" ],
        params={ 'timeout' : 123456 },
        retdata={})
    cms.hosts_offline_or_decommission([ "host1", "host2" ], timeout=123456)

  def test_get_licensed_feature_usage(self):
    resource = utils.MockResource(self)
    cms = ClouderaManager(resource)
    json_string = {
      "totals" : {
        "Core" : 8,
        "HBase" : 8,
        "Impala" : 8,
        "Search" : 2,
        "Spark" : 5,
        "Accumulo" : 0,
        "Navigator" : 8
      },
      "clusters" : {
        "Cluster 1" : {
          "Core" : 4,
          "HBase" : 4,
          "Impala" : 4,
          "Search" : 1,
          "Spark" : 1,
          "Accumulo" : 0,
          "Navigator" : 4
        },
        "Cluster 2" : {
          "Core" : 4,
          "HBase" : 4,
          "Impala" : 4,
          "Search" : 1,
          "Spark" : 4,
          "Accumulo" : 0,
          "Navigator" : 4
        }
      }
    }
    resource.expect("GET", "/cm/getLicensedFeatureUsage", retdata=json_string)
    cms.get_licensed_feature_usage()

  def test_peer_v10(self):
    json_peer = _make_cm_v10_format_peer("peer1", "url1")

    resource = utils.MockResource(self, version=10)
    cms = ClouderaManager(resource)
    peer = ApiCmPeer(resource,
        name="peer1",
        url="url1",
        username="username",
        password="password")

    # Create peer
    resource.expect("POST", "/cm/peers",
        data=peer,
        retdata=json.loads(json_peer))
    cms.create_peer("peer1", "url1", "username", "password")

    # Delete peer
    resource.expect("DELETE", "/cm/peers/peer1",
        retdata=json.loads(json_peer))
    cms.delete_peer("peer1")

    # Update peer
    resource.expect("PUT", "/cm/peers/peer1",
        data=peer,
        retdata=json.loads(json_peer))
    cms.update_peer("peer1", "peer1", "url1", "username", "password")

    # Read peer
    resource.expect("GET", "/cm/peers/peer1",
        retdata=json.loads(json_peer))
    cms.get_peer("peer1")

    # Test peer connectivity
    resource.expect("POST", "/cm/peers/peer1/commands/test",
        retdata=json.loads(SAMPLE_COMMAND_JSON))
    cms.test_peer_connectivity("peer1")

  def test_peer_v11(self):
    resource = utils.MockResource(self, version=11)
    cms = ClouderaManager(resource)

    json_peer1 = _make_cm_v11_format_peer("peer1", "url1", "REPLICATION")
    json_peer2 = _make_cm_v11_format_peer("peer2", "url2", "STATUS_AGGREGATION")

    peer1 = ApiCmPeer(resource,
        name="peer1",
        url="url1",
        username="username",
        password="password",
        type="REPLICATION")
    peer2 = ApiCmPeer(resource,
        name="peer2",
        url="url2",
        username="username",
        password="password",
        type="STATUS_AGGREGATION")

    params_replication = {
      'type':   "REPLICATION",
    }
    params_status_aggregation = {
      'type':   "STATUS_AGGREGATION",
    }

    # Create peer
    resource.expect("POST", "/cm/peers",
        data=peer1,
        retdata=json.loads(json_peer1))
    cms.create_peer("peer1", "url1", "username", "password")

    resource.expect("POST", "/cm/peers",
        data=peer2,
        retdata=json.loads(json_peer2))
    cms.create_peer("peer2", "url2", "username", "password",
        peer_type="STATUS_AGGREGATION")

    # Delete peer
    resource.expect("DELETE", "/cm/peers/peer1",
        params=params_replication, retdata=json.loads(json_peer1))
    cms.delete_peer("peer1")
    resource.expect("DELETE", "/cm/peers/peer2",
        params=params_status_aggregation, retdata=json.loads(json_peer2))
    cms.delete_peer("peer2", peer_type="STATUS_AGGREGATION")

    # Update peer
    resource.expect("PUT", "/cm/peers/peer1",
        data=peer1,
        retdata=json.loads(json_peer1))
    cms.update_peer("peer1", "peer1", "url1", "username", "password")

    resource.expect("PUT", "/cm/peers/peer2",
        data=peer2,
        retdata=json.loads(json_peer2))
    cms.update_peer("peer2", "peer2", "url2", "username", "password",
        peer_type="STATUS_AGGREGATION")

    # Read peer
    resource.expect("GET", "/cm/peers/peer1", params=params_replication,
        retdata=json.loads(json_peer1))
    cms.get_peer("peer1")
    resource.expect("GET", "/cm/peers/peer2",
        params=params_status_aggregation, retdata=json.loads(json_peer2))
    cms.get_peer("peer2", peer_type="STATUS_AGGREGATION")

    # Test peer connectivity
    resource.expect("POST", "/cm/peers/peer1/commands/test",
        params=params_replication,
        retdata=json.loads(SAMPLE_COMMAND_JSON))
    cms.test_peer_connectivity("peer1")
    resource.expect("POST", "/cm/peers/peer2/commands/test",
        params=params_status_aggregation,
        retdata=json.loads(SAMPLE_COMMAND_JSON))
    cms.test_peer_connectivity("peer2", peer_type="STATUS_AGGREGATION")

  def test_import_cluster_v12(self):
    resource = utils.MockResource(self, version=12)
    cms = ClouderaManager(resource)
    data = ApiClusterTemplate(resource).to_json_dict()
    resource.expect(
      method="POST",
      reqpath="/cm/importClusterTemplate",
      params=dict(addRepositories=True),
      data = data,
      retdata=ApiCommand(resource).to_json_dict())
    cms.import_cluster_template(data, True)

