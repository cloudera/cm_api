#!/usr/bin/env python
#
# Copyright (c) 2013-2014 Cloudera, Inc. All rights reserved.

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
CLUSTER_NAME = "TestCluster"
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