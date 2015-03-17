#!/usr/bin/env python
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

# Stops the cluster gracefully.

import ConfigParser
from cm_api.api_client import ApiResource
from cm_api.endpoints.services import ApiService


### Do some initial prep work ###

# Prep for reading config props from external file
CONFIG = ConfigParser.ConfigParser()
CONFIG.read("clouderaconfig.ini")


### Set up environment-specific vars ###

# This is the host that the Cloudera Manager server is running on
CM_HOST = CONFIG.get("CM", "cm.host")

# CM admin account info
ADMIN_USER = CONFIG.get("CM", "admin.name")
ADMIN_PASS = CONFIG.get("CM", "admin.password")

#### Cluster Definition #####
CLUSTER_NAME = CONFIG.get("CM", "cluster.name")
CDH_VERSION = "CDH5"


### Main function ###
def main():
   API = ApiResource(CM_HOST, version=5, username=ADMIN_USER, password=ADMIN_PASS)
   print "Connected to CM host on " + CM_HOST

   CLUSTER = API.get_cluster(CLUSTER_NAME)

   print "About to stop cluster."
   CLUSTER.stop().wait()
   print "Done stopping cluster."


if __name__ == "__main__":
   main()