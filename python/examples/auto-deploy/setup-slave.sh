#!/bin/bash
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

# This is to be run on all nodes that will be managed by CM. This includes all Hadoop nodes,
# as well as nodes that run CM management services (if those aren't running on the CM server node).

# Set up some vars
config_file=clouderaconfig.ini
cm_server_host=$(grep cm.host ${config_file} | awk -F'=' '{print $2}')
ntp_server=$(grep ntp.server ${config_file} | awk -F'=' '{print $2}')

# Prep Cloudera repo
sudo yum -y install wget
wget http://archive.cloudera.com/cm5/redhat/6/x86_64/cm/cloudera-manager.repo
sudo mv cloudera-manager.repo /etc/yum.repos.d/

# Turn off firewall
sudo service iptables stop

# Turn off SELINUX
echo 0 | tee /selinux/enforce > /dev/null

#Set up NTP
sudo yum -y install ntp
sudo chkconfig ntpd on
sudo ntpdate "${ntp_server}"
sudo /etc/init.d/ntpd start

# Make the mysql driver available to hive
sudo yum -y install mysql-connector-java
sudo mkdir -p /usr/lib/hive/lib/
sudo ln -s /usr/share/java/mysql-connector-java.jar /usr/lib/hive/lib/mysql-connector-java.jar

# Make sure DNS is set up properly so all nodes can find all other nodes

# For slaves
sudo yum -y install cloudera-manager-agent cloudera-manager-daemons
sudo sed -i.bak -e"s%server_host=localhost%server_host=${cm_server_host}%" /etc/cloudera-scm-agent/config.ini

# Sleep a while so the CM server can come up
sleep_time=120
echo "Sleeping for ${sleep_time} seconds so CM server can start up."
sleep ${sleep_time}
echo "Done sleeping. Starting CM agent now."

sudo service cloudera-scm-agent start
