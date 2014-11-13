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
from cm_api.endpoints.types import config_to_json, ApiConfig
from cm_api_tests import utils

try:
  import json
except ImportError:
  import simplejson as json

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
          "validationState" : "OK"
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