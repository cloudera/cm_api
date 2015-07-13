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

# This should be run on the host on which the CM server is to run.

# Set up some vars
config_file=clouderaconfig.ini
cm_server_host=$(grep cm.host ${config_file} | awk -F'=' '{print $2}')
ntp_server=$(grep ntp.server ${config_file} | awk -F'=' '{print $2}')
# This should be set to whatever HIVE_HMS_HOST is set to in deploycloudera.py
hive_metastore_host=$(grep hive.metastore.host ${config_file} | awk -F'=' '{print $2}')
hive_metastore_password=$(grep hive.metastore.password ${config_file} | awk -F'=' '{print $2}')

# Prep Cloudera repo
sudo yum -y install wget
wget http://archive.cloudera.com/cm5/redhat/6/x86_64/cm/cloudera-manager.repo
sudo mv cloudera-manager.repo /etc/yum.repos.d/

# Turn off firewall
sudo service iptables stop

# Turn off SELINUX
echo 0 | sudo tee /selinux/enforce > /dev/null

# Set up NTP
sudo yum -y install ntp
sudo chkconfig ntpd on
sudo ntpdate "${ntp_server}"
sudo /etc/init.d/ntpd start

# Set up python
sudo rpm -ivh http://dl.fedoraproject.org/pub/epel/6/i386/epel-release-6-8.noarch.rpm
sudo yum -y install python-pip
sudo pip install cm_api

# Set up MySQL
wget http://archive.cloudera.com/cdh5/cdh/5/hive-0.12.0-cdh5.0.0.tar.gz
tar zxf hive-0.12.0-cdh5.0.0.tar.gz
sudo yum -y install mysql-server expect
sudo service mysqld start
sudo /sbin/chkconfig mysqld on
/bin/echo -e "\nY\n${hive_metastore_password}\n${hive_metastore_password}\nY\nn\nY\nY\n" | sudo tee /tmp/answers > /dev/null
sudo cat /tmp/answers | sudo /usr/bin/mysql_secure_installation
sudo rm /tmp/answers
mysql -uroot -p"${hive_metastore_password}" --execute="CREATE DATABASE metastore; USE metastore; SOURCE ./hive-0.12.0-cdh5.0.0/scripts/metastore/upgrade/mysql/hive-schema-0.12.0.mysql.sql;"
mysql -uroot -p"${hive_metastore_password}" --execute="CREATE USER 'hive'@'${hive_metastore_host}' IDENTIFIED BY '${hive_metastore_password}';"
mysql -uroot -p"${hive_metastore_password}" --execute="REVOKE ALL PRIVILEGES, GRANT OPTION FROM 'hive'@'${hive_metastore_host}';"
mysql -uroot -p"${hive_metastore_password}" --execute="GRANT SELECT,INSERT,UPDATE,DELETE,LOCK TABLES,EXECUTE ON metastore.* TO 'hive'@'${hive_metastore_host}';"
mysql -uroot -p"${hive_metastore_password}" --execute="FLUSH PRIVILEGES;"
mysql -uroot -p"${hive_metastore_password}" --execute="create database oozie; grant all privileges on oozie.* to 'oozie'@'localhost' identified by '${hive_metastore_password}'; grant all privileges on oozie.* to 'oozie'@'%' identified by '${hive_metastore_password}';"

# Make sure DNS is set up properly so all nodes can find all other nodes

# For master
sudo yum -y install cloudera-manager-agent cloudera-manager-daemons cloudera-manager-server cloudera-manager-server-db-2
sudo service cloudera-scm-server-db start
sudo service cloudera-scm-server start
sudo sed -i.bak -e"s%server_host=localhost%server_host=${cm_server_host}%" /etc/cloudera-scm-agent/config.ini
sudo service cloudera-scm-agent start

# Prep work before calling the Cloudera provisioning script.
firehostdbpassword=$(grep com.cloudera.cmf.ACTIVITYMONITOR.db.password /etc/cloudera-scm-server/db.mgmt.properties | awk -F'=' '{print $2}')
navigatordbpassword=$(grep com.cloudera.cmf.NAVIGATOR.db.password /etc/cloudera-scm-server/db.mgmt.properties | awk -F'=' '{print $2}')
headlampdbpassword=$(grep com.cloudera.cmf.REPORTSMANAGER.db.password /etc/cloudera-scm-server/db.mgmt.properties | awk -F'=' '{print $2}')

# Sleep for a while to give the agents enough time to check in with the master.
# Or better yet, make a dependency so that the slave setup scripts don't start until now and the rest of this script doesn't finish until the slaves finish.
sleep_time=180
echo "Sleeping for ${sleep_time} seconds so managed cluster nodes can get set up."
sleep ${sleep_time}
echo "Done sleeping. Deploying cluster now."

# Execute script to deploy Cloudera cluster
sudo python deploycloudera.py -i"${hive_metastore_password}" -f"${firehostdbpassword}" -n"${navigatordbpassword}" -r"${headlampdbpassword}"

# Now stop the cluster gracefully if necessary; ie if all servers are automatically rebooted at the end of the provisioning process
#sudo python stopcloudera.py
