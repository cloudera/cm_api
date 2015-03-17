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

# Deploys a CDH cluster and configures CM management services.

import socket, sys, time, ConfigParser, csv, pprint, urllib2
from subprocess import Popen, PIPE, STDOUT
from math import log as ln
from cm_api.api_client import ApiResource
from cm_api.endpoints.services import ApiService
from cm_api.endpoints.services import ApiServiceSetupInfo
from optparse import OptionParser


### Do some initial prep work ###

# Prep command line argument parser
parser = OptionParser()
parser.add_option('-i', '--hmsdbpassword', dest='HIVE_METASTORE_PASSWORD', default='', help='The password for the Hive MetaStore DB, which should have DB name "metastore" and user "???".')
parser.add_option('-f', '--firehosedbpassword', dest='FIREHOSE_DATABASE_PASSWORD', default='', help='The password for the Firehose (Activity Monitor) DB, which should have DB name "amon" and user "amon".')
parser.add_option('-n', '--navigatordbpassword', dest='NAVIGATOR_DATABASE_PASSWORD', default='', help='The password for the Navigator DB, which should have DB name "nav" and user "nav".')
parser.add_option('-r', '--headlampdbpassword', dest='HEADLAMP_DATABASE_PASSWORD', default='', help='The password for the Headlamp (Report Manager) DB, which should have DB name "rman" and user "rman".')
(options, args) = parser.parse_args()

# Prep pretty printer ###
PRETTY_PRINT = pprint.PrettyPrinter(indent=4)

# Prep for reading config props from external file
CONFIG = ConfigParser.ConfigParser()
CONFIG.read("clouderaconfig.ini")


### Set up environment-specific vars ###

# This is the host that the Cloudera Manager server is running on
CM_HOST = CONFIG.get("CM", "cm.host")

# These include hosts that will run NameNode, DataNode, JobTracker, TaskTracker, etc as well as things like gateways and zookeeper nodes
CLUSTER_HOSTS = CONFIG.get("CDH", "cluster.hosts").split(',')

# CM admin account info
ADMIN_USER = CONFIG.get("CM", "admin.name")
ADMIN_PASS = CONFIG.get("CM", "admin.password")

# Hadoop config parameters
HADOOP_DATA_DIR_PREFIX = CONFIG.get("CDH", "hadoop.data.dir.prefix")
HADOOP_DATA_DIR_PREFIX = HADOOP_DATA_DIR_PREFIX if HADOOP_DATA_DIR_PREFIX != '' else '/hadoop'

# This is the password for the Hive Metastore database
HIVE_METASTORE_PASSWORD = options.HIVE_METASTORE_PASSWORD

# These are passwords for the embedded postgres database that the Cloudera Manager management services use
FIREHOSE_DATABASE_PASSWORD = options.FIREHOSE_DATABASE_PASSWORD
NAVIGATOR_DATABASE_PASSWORD = options.NAVIGATOR_DATABASE_PASSWORD
HEADLAMP_DATABASE_PASSWORD = options.HEADLAMP_DATABASE_PASSWORD


### CM Definition ###
CMD_TIMEOUT = 180
CM_CONFIG = {
  'TSQUERY_STREAMS_LIMIT' : 1000,
  # XXX System high banner?
  #  'CUSTOM_BANNER_HTML' : ''
  #  'CUSTOM_HEADER_COLOR': ''
  # XXX Consent to monitoring?
  #  'CUSTOM_IA_POLICY' : ''
  # SSL Related configs
  #  XXX Note that when true, you need to use port 7183 instead of 7180
  #  XXX Note that after submitting this request, you'll need to restart the cloudera-scm-server service
  #  XXX For more info: http://www.cloudera.com/content/cloudera-content/cloudera-docs/CM4Ent/latest/Cloudera-Manager-Administration-Guide/cmag_config_tls_security.html
  #  'WEB_TLS' : 'true'
  #  'AGENT_TLS' : 'true'
  #  'NEED_AGENT_VALIDATION': 'true'
  #  'KEYSTORE_PASSWORD' : ''
  #  'KEYSTORE_PATH' : ''
  #  'TRUSTSTORE_PATH': ''
  #  'TRUSTSTORE_PASSWORD': ''
}
# Replace default parcel repo urls with custom urls if necessary
REMOTE_PARCEL_REPO_URLS_NAME = "remote.parcel.repo.urls"
if CONFIG.has_option("CM", REMOTE_PARCEL_REPO_URLS_NAME):
    CM_CONFIG['REMOTE_PARCEL_REPO_URLS'] = CONFIG.get("CM", REMOTE_PARCEL_REPO_URLS_NAME)

#### Cluster Definition #####
CLUSTER_NAME = CONFIG.get("CM", "cluster.name")
CDH_VERSION = "CDH5"

### Parcels ###
PARCEL_VERSION = CONFIG.get("CDH", "cdh.parcel.version")
if PARCEL_VERSION.lower() == "latest":
   # Get list of parcels from the cloudera repo to see what the latest version is. Then to parse:
   # find first item that starts with CDH-
   # strip off leading CDH-
   # strip off everything after the last - in that item, including the -
   LATEST_PARCEL_URL = 'http://archive.cloudera.com/cdh5/parcels/latest/'
   PARCEL_PREFIX = 'CDH-'
   dir_list = urllib2.urlopen(LATEST_PARCEL_URL).read()
   dir_list = dir_list[dir_list.index(PARCEL_PREFIX)+len(PARCEL_PREFIX):]
   dir_list = dir_list[:dir_list.index('"')]
   PARCEL_VERSION = dir_list[:dir_list.rfind('-')]
PARCELS = [
   { 'name' : "CDH", 'version' : PARCEL_VERSION },
   #{ 'name' : "CDH", 'version' : "5.0.1-1.cdh5.0.1.p0.47" },
   #{ 'name' : "ACCUMULO", 'version' : "1.4.3-cdh4.3.0-beta-3"}
]


### Management Services ###
# If using the embedded postgresql database, the database passwords can be found in /etc/cloudera-scm-server/db.mgmt.properties.
# The values change every time the cloudera-scm-server-db process is restarted. 
# TBD will CM have to be reconfigured each time?
MGMT_SERVICENAME = "MGMT"
MGMT_SERVICE_CONFIG = {
   'zookeeper_datadir_autocreate': 'true',
}
MGMT_ROLE_CONFIG = {
   'quorumPort': 2888,
}
AMON_ROLENAME = "ACTIVITYMONITOR"
AMON_ROLE_CONFIG = {
   'firehose_database_host': CM_HOST + ":7432",
   'firehose_database_user': 'amon',
   'firehose_database_password': FIREHOSE_DATABASE_PASSWORD,
   'firehose_database_type': 'postgresql',
   'firehose_database_name': 'amon',
   'firehose_heapsize': '215964392',
}
APUB_ROLENAME = "ALERTPUBLISHER"
APUB_ROLE_CONFIG = { }
ESERV_ROLENAME = "EVENTSERVER"
ESERV_ROLE_CONFIG = {
   'event_server_heapsize': '215964392'
}
HMON_ROLENAME = "HOSTMONITOR"
HMON_ROLE_CONFIG = { }
SMON_ROLENAME = "SERVICEMONITOR"
SMON_ROLE_CONFIG = { }
NAV_ROLENAME = "NAVIGATOR"
NAV_ROLE_CONFIG = {
   'navigator_database_host': CM_HOST + ":7432",
   'navigator_database_user': 'nav',
   'navigator_database_password': NAVIGATOR_DATABASE_PASSWORD,
   'navigator_database_type': 'postgresql',
   'navigator_database_name': 'nav',
   'navigator_heapsize': '215964392',
}
NAVMS_ROLENAME = "NAVIGATORMETADATASERVER"
NAVMS_ROLE_CONFIG = {
}
RMAN_ROLENAME = "REPORTMANAGER"
RMAN_ROLE_CONFIG = {
   'headlamp_database_host': CM_HOST + ":7432",
   'headlamp_database_user': 'rman',
   'headlamp_database_password': HEADLAMP_DATABASE_PASSWORD,
   'headlamp_database_type': 'postgresql',
   'headlamp_database_name': 'rman',
   'headlamp_heapsize': '215964392',
}

### ZooKeeper ###
# ZK quorum will be the first three hosts 
ZOOKEEPER_HOSTS = list(CLUSTER_HOSTS[:3])
ZOOKEEPER_SERVICE_NAME = "ZOOKEEPER"
ZOOKEEPER_SERVICE_CONFIG = {
   'zookeeper_datadir_autocreate': 'true',
}
ZOOKEEPER_ROLE_CONFIG = {
    'quorumPort': 2888,
    'electionPort': 3888,
    'dataLogDir': '/var/lib/zookeeper',
    'dataDir': '/var/lib/zookeeper',
    'maxClientCnxns': '1024',
}

### HDFS ###
HDFS_SERVICE_NAME = "HDFS"
HDFS_SERVICE_CONFIG = {
  'dfs_replication': 3,
  'dfs_permissions': 'false',
  'dfs_block_local_path_access_user': 'impala,hbase,mapred,spark',
}
HDFS_NAMENODE_SERVICE_NAME = "nn"
HDFS_NAMENODE_HOST = CLUSTER_HOSTS[0]
HDFS_NAMENODE_CONFIG = {
  #'dfs_name_dir_list': '/data01/hadoop/namenode',
  'dfs_name_dir_list': HADOOP_DATA_DIR_PREFIX + '/namenode',
  'dfs_namenode_handler_count': 30, #int(ln(len(DATANODES))*20),
}
HDFS_SECONDARY_NAMENODE_HOST = CLUSTER_HOSTS[1]
HDFS_SECONDARY_NAMENODE_CONFIG = {
  #'fs_checkpoint_dir_list': '/data01/hadoop/namesecondary',
  'fs_checkpoint_dir_list': HADOOP_DATA_DIR_PREFIX + '/namesecondary',
}
HDFS_DATANODE_HOSTS = list(CLUSTER_HOSTS)
#dfs_datanode_du_reserved must be smaller than the amount of free space across the data dirs
#Ideally each data directory will have at least 1TB capacity; they need at least 100GB at a minimum 
#dfs_datanode_failed_volumes_tolerated must be less than the number of different data dirs (ie volumes) in dfs_data_dir_list
HDFS_DATANODE_CONFIG = {
  #'dfs_data_dir_list': '/data01/hadoop/datanode,/data02/hadoop/datanode,/data03/hadoop/datanode,/data04/hadoop/datanode,/data05/hadoop/datanode,/data06/hadoop/datanode,/data07/hadoop/datanode,/data08/hadoop/datanode',
  'dfs_data_dir_list': HADOOP_DATA_DIR_PREFIX + '/datanode',
  'dfs_datanode_handler_count': 30,
  #'dfs_datanode_du_reserved': 42949672960,
  'dfs_datanode_du_reserved': 1073741824,
  'dfs_datanode_failed_volumes_tolerated': 0,
  'dfs_datanode_data_dir_perm': 755,
}
HDFS_GATEWAY_HOSTS = list(CLUSTER_HOSTS)
HDFS_GATEWAY_HOSTS.append(CM_HOST)
HDFS_GATEWAY_CONFIG = {
  'dfs_client_use_trash' : 'true'
}

### MapReduce ###
MAPRED_SERVICE_NAME = "MAPRED"
MAPRED_SERVICE_CONFIG = {
  'hdfs_service': HDFS_SERVICE_NAME,
}
MAPRED_JT_HOST = CLUSTER_HOSTS[0]
MAPRED_JT_CONFIG = {
  'mapred_jobtracker_restart_recover': 'true',
  'mapred_job_tracker_handler_count': 30, #int(ln(len(DATANODES))*20),
  #'jobtracker_mapred_local_dir_list': '/data01/hadoop/mapred',
  'jobtracker_mapred_local_dir_list': HADOOP_DATA_DIR_PREFIX + '/mapred',
  #'mapreduce_jobtracker_staging_root_dir': '/data01/hadoop/staging',
  'mapreduce_jobtracker_staging_root_dir': HADOOP_DATA_DIR_PREFIX + '/staging',
  'mapreduce_jobtracker_split_metainfo_maxsize': '100000000',
}
MAPRED_TT_HOSTS = list(CLUSTER_HOSTS)
MAPRED_TT_CONFIG = {
  #'tasktracker_mapred_local_dir_list': '/data01/hadoop/mapred,/data02/hadoop/mapred,/data03/hadoop/mapred,/data04/hadoop/mapred,/data05/hadoop/mapred,/data06/hadoop/mapred,/data07/hadoop/mapred,/data08/hadoop/mapred',
  'tasktracker_mapred_local_dir_list': HADOOP_DATA_DIR_PREFIX + '/mapred',
  'mapred_tasktracker_map_tasks_maximum': 6,
  'mapred_tasktracker_reduce_tasks_maximum': 3,
  'override_mapred_child_java_opts_base': '-Xmx4g -Djava.net.preferIPv4Stack=true',
  'override_mapred_child_ulimit': 8388608,
  'override_mapred_reduce_parallel_copies': 5,
  'tasktracker_http_threads': 40,
  'override_mapred_output_compress': 'true',
  'override_mapred_output_compression_type': 'BLOCK',
  'override_mapred_output_compression_codec': 'org.apache.hadoop.io.compress.SnappyCodec',
  'override_mapred_compress_map_output': 'true',
  'override_mapred_map_output_compression_codec': 'org.apache.hadoop.io.compress.SnappyCodec',
  'override_io_sort_record_percent': '0.15',
}
MAPRED_GW_HOSTS = list(CLUSTER_HOSTS)
MAPRED_GW_CONFIG = {
  'mapred_reduce_tasks' : int(MAPRED_TT_CONFIG['mapred_tasktracker_reduce_tasks_maximum']*len(HDFS_DATANODE_HOSTS)/2),
  'mapred_submit_replication': 3,
}

### YARN ###
YARN_SERVICE_NAME = "YARN"
YARN_SERVICE_CONFIG = {
  'hdfs_service': HDFS_SERVICE_NAME,
}
YARN_RM_HOST = CLUSTER_HOSTS[0]
YARN_RM_CONFIG = { }
YARN_JHS_HOST = CLUSTER_HOSTS[0]
YARN_JHS_CONFIG = { }
YARN_NM_HOSTS = list(CLUSTER_HOSTS)
YARN_NM_CONFIG = {
  #'yarn_nodemanager_local_dirs': '/data01/hadoop/yarn/nm',
  'yarn_nodemanager_local_dirs': HADOOP_DATA_DIR_PREFIX + '/yarn/nm',
}
YARN_GW_HOSTS = list(CLUSTER_HOSTS)
YARN_GW_CONFIG = {
  'mapred_submit_replication': min(3, len(YARN_GW_HOSTS))
}

### Spark ###
SPARK_SERVICE_NAME = "SPARK"
SPARK_SERVICE_CONFIG = {
  'hdfs_service': HDFS_SERVICE_NAME,
}
SPARK_MASTER_HOST = CLUSTER_HOSTS[0]
SPARK_MASTER_CONFIG = {
#   'master_max_heapsize': 67108864,
}
SPARK_WORKER_HOSTS = list(CLUSTER_HOSTS)
SPARK_WORKER_CONFIG = {
#   'executor_total_max_heapsize': 67108864,
#   'worker_max_heapsize': 67108864,
}
SPARK_GW_HOSTS = list(CLUSTER_HOSTS)
SPARK_GW_CONFIG = { }

### HBase ###
HBASE_SERVICE_NAME = "HBASE"
HBASE_SERVICE_CONFIG = {
  'hdfs_service': HDFS_SERVICE_NAME,
  'zookeeper_service': ZOOKEEPER_SERVICE_NAME,
}
HBASE_HM_HOST = CLUSTER_HOSTS[0]
HBASE_HM_CONFIG = { }
HBASE_RS_HOSTS = list(CLUSTER_HOSTS)
HBASE_RS_CONFIG = {
  'hbase_hregion_memstore_flush_size': 1024000000,
  'hbase_regionserver_handler_count': 10,
  'hbase_regionserver_java_heapsize': 2048000000,
  'hbase_regionserver_java_opts': '',
}
HBASE_THRIFTSERVER_SERVICE_NAME = "HBASETHRIFTSERVER"
HBASE_THRIFTSERVER_HOST = CLUSTER_HOSTS[0]
HBASE_THRIFTSERVER_CONFIG = { }
HBASE_GW_HOSTS = list(CLUSTER_HOSTS)
HBASE_GW_CONFIG = { }

### Hive ###
HIVE_SERVICE_NAME = "HIVE"
HIVE_SERVICE_CONFIG = {
  'hive_metastore_database_host': CM_HOST,
  'hive_metastore_database_name': 'metastore',
  'hive_metastore_database_password': HIVE_METASTORE_PASSWORD,
  'hive_metastore_database_port': 3306,
  'hive_metastore_database_type': 'mysql',
  'mapreduce_yarn_service': MAPRED_SERVICE_NAME,
  'zookeeper_service': ZOOKEEPER_SERVICE_NAME,
  'mapreduce_yarn_service': YARN_SERVICE_NAME,
}
HIVE_HMS_HOST = CLUSTER_HOSTS[0]
HIVE_HMS_CONFIG = {
  'hive_metastore_java_heapsize': 85306784,
}
HIVE_HS2_HOST = CLUSTER_HOSTS[0]
HIVE_HS2_CONFIG = { }
HIVE_WHC_HOST = CLUSTER_HOSTS[0]
HIVE_WHC_CONFIG = { }
HIVE_GW_HOSTS = list(CLUSTER_HOSTS)
HIVE_GW_CONFIG = { }

### Impala ###
IMPALA_SERVICE_NAME = "IMPALA"
IMPALA_SERVICE_CONFIG = {
  'hdfs_service': HDFS_SERVICE_NAME,
  'hbase_service': HBASE_SERVICE_NAME,
  'hive_service': HIVE_SERVICE_NAME,
}
IMPALA_SS_HOST = CLUSTER_HOSTS[0]
IMPALA_SS_CONFIG = { }
IMPALA_CS_HOST = CLUSTER_HOSTS[0]
IMPALA_CS_CONFIG = { }
IMPALA_ID_HOSTS = list(CLUSTER_HOSTS)
IMPALA_ID_CONFIG = { }

### Search ###
SEARCH_SERVICE_NAME = "SEARCH"
SEARCH_SERVICE_CONFIG = {
  'hdfs_service': HDFS_SERVICE_NAME,
  'zookeeper_service': ZOOKEEPER_SERVICE_NAME,
}
SEARCH_SOLR_HOST = CLUSTER_HOSTS[0]
SEARCH_SOLR_CONFIG = { }
SEARCH_GW_HOSTS = list(CLUSTER_HOSTS)
SEARCH_GW_CONFIG = { }

### Flume ###
FLUME_SERVICE_NAME = "FLUME"
FLUME_SERVICE_CONFIG = {
  'hdfs_service': HDFS_SERVICE_NAME,
  'hbase_service': HBASE_SERVICE_NAME,
  'solr_service': SEARCH_SERVICE_NAME,
}
FLUME_AGENT_HOSTS = list(CLUSTER_HOSTS)
FLUME_AGENT_CONFIG = { }

### Oozie ###
OOZIE_SERVICE_NAME = "OOZIE"
OOZIE_SERVICE_CONFIG = {
  'mapreduce_yarn_service': YARN_SERVICE_NAME,
}
OOZIE_SERVER_HOST = CLUSTER_HOSTS[0]
OOZIE_SERVER_CONFIG = { 
   'oozie_java_heapsize': 207881018,
   'oozie_database_host': CM_HOST,
   'oozie_database_name': 'oozie',
   'oozie_database_password': HIVE_METASTORE_PASSWORD,
   'oozie_database_type': 'mysql',
   'oozie_database_user': 'oozie',
}

### Sqoop ###
SQOOP_SERVICE_NAME = "SQOOP"
SQOOP_SERVICE_CONFIG = {
  'mapreduce_yarn_service': YARN_SERVICE_NAME,
}
SQOOP_SERVER_HOST = CLUSTER_HOSTS[0]
SQOOP_SERVER_CONFIG = { 
   'sqoop_java_heapsize': 207881018,
}

### HUE ###
HUE_SERVICE_NAME = "HUE"
HUE_SERVICE_CONFIG = {
  'hive_service': HIVE_SERVICE_NAME,
  'hbase_service': HBASE_SERVICE_NAME,
  'impala_service': IMPALA_SERVICE_NAME,
  'oozie_service': OOZIE_SERVICE_NAME,
  'sqoop_service': SQOOP_SERVICE_NAME,
  'hue_webhdfs': HDFS_SERVICE_NAME + "-" + HDFS_NAMENODE_SERVICE_NAME,
  'hue_hbase_thrift': HBASE_SERVICE_NAME + "-" + HBASE_THRIFTSERVER_SERVICE_NAME,
}
HUE_SERVER_HOST = CLUSTER_HOSTS[0]
HUE_SERVER_CONFIG = { 
   'hue_server_hue_safety_valve': '[search]\r\n## URL of the Solr Server\r\nsolr_url=http://' + SEARCH_SOLR_HOST + ':8983/solr',
}
HUE_KTR_HOST = CLUSTER_HOSTS[0]
HUE_KTR_CONFIG = { }

### Accumulo ###
ACCUMULO_SERVICE_NAME = "accumulo"
ACCUMULO_SERVICE_CONFIG = {
   'accumulo_instance_name' : "accumulo",
   'mapreduce_service' : MAPRED_SERVICE_NAME,
   'zookeeper_service' : ZOOKEEPER_SERVICE_NAME,
}
ACCUMULO_MASTER_HOSTS = [ CLUSTER_HOSTS[0] ]
ACCUMULO_MASTER_CONFIG = { }
ACCUMULO_TRACER_HOSTS = list(CLUSTER_HOSTS)
ACCUMULO_TRACER_CONFIG = { }
ACCUMULO_TSERVER_HOSTS = list(CLUSTER_HOSTS)
ACCUMULO_TSERVER_CONFIG = { }
ACCUMULO_LOGGER_HOSTS = list(CLUSTER_HOSTS)
ACCUMULO_LOGGER_CONFIG = { }
ACCUMULO_GATEWAY_HOSTS = list(CLUSTER_HOSTS)
ACCUMULO_GATEWAY_CONFIG = { }
ACCUMULO_MONITOR_HOST = CLUSTER_HOSTS[0]
ACCUMULO_MONITOR_CONFIG = { }
ACCUMULO_GC_HOST = CLUSTER_HOSTS[0]
ACCUMULO_GC_CONFIG = { }


### Deployment/Initialization Functions ###

# Creates the cluster and adds hosts
def init_cluster(api, cluster_name, cdh_version, hosts, cm_host):
   cluster = api.create_cluster(cluster_name, cdh_version)
   # Add the CM host to the list of hosts to add in the cluster so it can run the management services
   all_hosts = list(CLUSTER_HOSTS)
   all_hosts.append(CM_HOST)
   cluster.add_hosts(all_hosts)
   return cluster

# Downloads and distributes parcels
def deploy_parcels(cluster, parcels):
   for parcel in parcels:
      p = cluster.get_parcel(parcel['name'], parcel['version'])
      p.start_download()
      while True:
         p = cluster.get_parcel(parcel['name'], parcel['version'])
         if p.stage == "DOWNLOADED":
            break
         if p.state.errors:
            raise Exception(str(p.state.errors))
         print "Downloading %s: %s / %s" % (parcel['name'], p.state.progress, p.state.totalProgress)
         time.sleep(15)
      print "Downloaded %s" % (parcel['name'])
      p.start_distribution()
      while True:
         p = cluster.get_parcel(parcel['name'], parcel['version'])
         if p.stage == "DISTRIBUTED":
            break
         if p.state.errors:
            raise Exception(str(p.state.errors))
         print "Distributing %s: %s / %s" % (parcel['name'], p.state.progress, p.state.totalProgress)
         time.sleep(15)
      print "Distributed %s" % (parcel['name'])
      p.activate()

# Deploys management services. Not all of these are currently turned on because some require a license.
# This function also starts the services.
def deploy_management(manager, mgmt_servicename, mgmt_service_conf, mgmt_role_conf, amon_role_name, amon_role_conf, apub_role_name, apub_role_conf, eserv_role_name, eserv_role_conf, hmon_role_name, hmon_role_conf, smon_role_name, smon_role_conf, nav_role_name, nav_role_conf, navms_role_name, navms_role_conf, rman_role_name, rman_role_conf):
   mgmt = manager.create_mgmt_service(ApiServiceSetupInfo())
   
   # create roles. Note that host id may be different from host name (especially in CM 5). Look it it up in /api/v5/hosts
   mgmt.create_role(amon_role_name + "-1", "ACTIVITYMONITOR", CM_HOST)
   mgmt.create_role(apub_role_name + "-1", "ALERTPUBLISHER", CM_HOST)
   mgmt.create_role(eserv_role_name + "-1", "EVENTSERVER", CM_HOST)
   mgmt.create_role(hmon_role_name + "-1", "HOSTMONITOR", CM_HOST)
   mgmt.create_role(smon_role_name + "-1", "SERVICEMONITOR", CM_HOST)
   #mgmt.create_role(nav_role_name + "-1", "NAVIGATOR", CM_HOST)
   #mgmt.create_role(navms_role_name + "-1", "NAVIGATORMETADATASERVER", CM_HOST)
   #mgmt.create_role(rman_role_name + "-1", "REPORTSMANAGER", CM_HOST)
   
   # now configure each role   
   for group in mgmt.get_all_role_config_groups():
       if group.roleType == "ACTIVITYMONITOR":
           group.update_config(amon_role_conf)
       elif group.roleType == "ALERTPUBLISHER":
           group.update_config(apub_role_conf)
       elif group.roleType == "EVENTSERVER":
           group.update_config(eserv_role_conf)
       elif group.roleType == "HOSTMONITOR":
           group.update_config(hmon_role_conf)
       elif group.roleType == "SERVICEMONITOR":
           group.update_config(smon_role_conf)
    #   elif group.roleType == "NAVIGATOR":
    #       group.update_config(nav_role_conf)
    #   elif group.roleType == "NAVIGATORMETADATASERVER":
    #       group.update_config(navms_role_conf)
    #   elif group.roleType == "REPORTSMANAGER":
    #       group.update_config(rman_role_conf)
   
   # now start the management service
   mgmt.start().wait()
   
   return mgmt


# Deploys and initializes ZooKeeper
def deploy_zookeeper(cluster, zk_name, zk_hosts, zk_service_conf, zk_role_conf):
   zk = cluster.create_service(zk_name, "ZOOKEEPER")
   zk.update_config(zk_service_conf)
   
   zk_id = 0
   for zk_host in zk_hosts:
      zk_id += 1
      zk_role_conf['serverId'] = zk_id
      role = zk.create_role(zk_name + "-" + str(zk_id), "SERVER", zk_host)
      role.update_config(zk_role_conf)
   
   zk.init_zookeeper()
   
   return zk


# Deploys HDFS - NN, DNs, SNN, gateways.
# This does not yet support HA yet.
def deploy_hdfs(cluster, hdfs_service_name, hdfs_config, hdfs_nn_service_name, hdfs_nn_host, hdfs_nn_config, hdfs_snn_host, hdfs_snn_config, hdfs_dn_hosts, hdfs_dn_config, hdfs_gw_hosts, hdfs_gw_config):
   hdfs_service = cluster.create_service(hdfs_service_name, "HDFS")
   hdfs_service.update_config(hdfs_config)
   
   nn_role_group = hdfs_service.get_role_config_group("{0}-NAMENODE-BASE".format(hdfs_service_name))
   nn_role_group.update_config(hdfs_nn_config)
   nn_service_pattern = "{0}-" + hdfs_nn_service_name
   hdfs_service.create_role(nn_service_pattern.format(hdfs_service_name), "NAMENODE", hdfs_nn_host)
   
   snn_role_group = hdfs_service.get_role_config_group("{0}-SECONDARYNAMENODE-BASE".format(hdfs_service_name))
   snn_role_group.update_config(hdfs_snn_config)
   hdfs_service.create_role("{0}-snn".format(hdfs_service_name), "SECONDARYNAMENODE", hdfs_snn_host)
   
   dn_role_group = hdfs_service.get_role_config_group("{0}-DATANODE-BASE".format(hdfs_service_name))
   dn_role_group.update_config(hdfs_dn_config)
   
   gw_role_group = hdfs_service.get_role_config_group("{0}-GATEWAY-BASE".format(hdfs_service_name))
   gw_role_group.update_config(hdfs_gw_config)
   
   datanode = 0
   for host in hdfs_dn_hosts:
      datanode += 1
      hdfs_service.create_role("{0}-dn-".format(hdfs_service_name) + str(datanode), "DATANODE", host)
   
   gateway = 0
   for host in hdfs_gw_hosts:
      gateway += 1
      hdfs_service.create_role("{0}-gw-".format(hdfs_service_name) + str(gateway), "GATEWAY", host)
   
   return hdfs_service


# Initializes HDFS - format the file system
def init_hdfs(hdfs_service, hdfs_name, timeout):
   cmd = hdfs_service.format_hdfs("{0}-nn".format(hdfs_name))[0]
   if not cmd.wait(timeout).success:
      print "WARNING: Failed to format HDFS, attempting to continue with the setup" 


# Deploys MapReduce - JT, TTs, gateways.
# This does not yet support HA yet.
# This shouldn't be run if YARN is deployed.
def deploy_mapreduce(cluster, mapred_service_name, mapred_service_config, mapred_jt_host, mapred_jt_config, mapred_tt_hosts, mapred_tt_config, mapred_gw_hosts, mapred_gw_config ):
   mapred_service = cluster.create_service(mapred_service_name, "MAPREDUCE")
   mapred_service.update_config(mapred_service_config)
      
   jt = mapred_service.get_role_config_group("{0}-JOBTRACKER-BASE".format(mapred_service_name))
   jt.update_config(mapred_jt_config)
   mapred_service.create_role("{0}-jt".format(mapred_service_name), "JOBTRACKER", mapred_jt_host)
   
   tt = mapred_service.get_role_config_group("{0}-TASKTRACKER-BASE".format(mapred_service_name))
   tt.update_config(mapred_tt_config)
   
   gw = mapred_service.get_role_config_group("{0}-GATEWAY-BASE".format(mapred_service_name))
   gw.update_config(mapred_gw_config)
   
   tasktracker = 0
   for host in mapred_tt_hosts:
      tasktracker += 1
      mapred_service.create_role("{0}-tt-".format(mapred_service_name) + str(tasktracker), "TASKTRACKER", host)
   
   gateway = 0
   for host in mapred_gw_hosts:
      gateway += 1
      mapred_service.create_role("{0}-gw-".format(mapred_service_name) + str(gateway), "GATEWAY", host)
   
   return mapred_service


# Deploys YARN - RM, JobHistoryServer, NMs, gateways
# This shouldn't be run if MapReduce is deployed.
def deploy_yarn(cluster, yarn_service_name, yarn_service_config, yarn_rm_host, yarn_rm_config, yarn_jhs_host, yarn_jhs_config, yarn_nm_hosts, yarn_nm_config, yarn_gw_hosts, yarn_gw_config):
   yarn_service = cluster.create_service(yarn_service_name, "YARN")
   yarn_service.update_config(yarn_service_config)
      
   rm = yarn_service.get_role_config_group("{0}-RESOURCEMANAGER-BASE".format(yarn_service_name))
   rm.update_config(yarn_rm_config)
   yarn_service.create_role("{0}-rm".format(yarn_service_name), "RESOURCEMANAGER", yarn_rm_host)
      
   jhs = yarn_service.get_role_config_group("{0}-JOBHISTORY-BASE".format(yarn_service_name))
   jhs.update_config(yarn_jhs_config)
   yarn_service.create_role("{0}-jhs".format(yarn_service_name), "JOBHISTORY", yarn_jhs_host)
   
   nm = yarn_service.get_role_config_group("{0}-NODEMANAGER-BASE".format(yarn_service_name))
   nm.update_config(yarn_nm_config)
   
   nodemanager = 0
   for host in yarn_nm_hosts:
      nodemanager += 1
      yarn_service.create_role("{0}-nm-".format(yarn_service_name) + str(nodemanager), "NODEMANAGER", host)
   
   gw = yarn_service.get_role_config_group("{0}-GATEWAY-BASE".format(yarn_service_name))
   gw.update_config(yarn_gw_config)
   
   gateway = 0
   for host in yarn_gw_hosts:
      gateway += 1
      yarn_service.create_role("{0}-gw-".format(yarn_service_name) + str(gateway), "GATEWAY", host)
   
   #TODO need api version 6 for these, but I think they are done automatically?
   #yarn_service.create_yarn_job_history_dir()
   #yarn_service.create_yarn_node_manager_remote_app_log_dir()
   
   return yarn_service


# Deploys spark - master, workers, gateways
def deploy_spark(cluster, spark_service_name, spark_service_config, spark_master_host, spark_master_config, spark_worker_hosts, spark_worker_config, spark_gw_hosts, spark_gw_config):
   spark_service = cluster.create_service(spark_service_name, "SPARK")
   spark_service.update_config(spark_service_config)
      
   sm = spark_service.get_role_config_group("{0}-SPARK_MASTER-BASE".format(spark_service_name))
   sm.update_config(spark_master_config)
   spark_service.create_role("{0}-sm".format(spark_service_name), "SPARK_MASTER", spark_master_host)
   
   sw = spark_service.get_role_config_group("{0}-SPARK_WORKER-BASE".format(spark_service_name))
   sw.update_config(spark_worker_config)
   
   worker = 0
   for host in spark_worker_hosts:
      worker += 1
      spark_service.create_role("{0}-sw-".format(spark_service_name) + str(worker), "SPARK_WORKER", host)
   
   gw = spark_service.get_role_config_group("{0}-GATEWAY-BASE".format(spark_service_name))
   gw.update_config(spark_gw_config)
   
   gateway = 0
   for host in spark_gw_hosts:
      gateway += 1
      spark_service.create_role("{0}-gw-".format(spark_service_name) + str(gateway), "GATEWAY", host)
   
   #TODO - CreateSparkUserDirCommand, SparkUploadJarServiceCommand???
   
   return spark_service


# Deploys HBase - HMaster, RSes, HBase Thrift Server, gateways
def deploy_hbase(cluster, hbase_service_name, hbase_service_config, hbase_hm_host, hbase_hm_config, hbase_rs_hosts, hbase_rs_config, hbase_thriftserver_service_name, hbase_thriftserver_host, hbase_thriftserver_config, hbase_gw_hosts, hbase_gw_config ):
   hbase_service = cluster.create_service(hbase_service_name, "HBASE")
   hbase_service.update_config(hbase_service_config)
      
   hm = hbase_service.get_role_config_group("{0}-MASTER-BASE".format(hbase_service_name))
   hm.update_config(hbase_hm_config)
   hbase_service.create_role("{0}-hm".format(hbase_service_name), "MASTER", hbase_hm_host)
   
   rs = hbase_service.get_role_config_group("{0}-REGIONSERVER-BASE".format(hbase_service_name))
   rs.update_config(hbase_rs_config)
   
   ts = hbase_service.get_role_config_group("{0}-HBASETHRIFTSERVER-BASE".format(hbase_service_name))
   ts.update_config(hbase_thriftserver_config)
   ts_name_pattern = "{0}-" + hbase_thriftserver_service_name
   hbase_service.create_role(ts_name_pattern.format(hbase_service_name), "HBASETHRIFTSERVER", hbase_thriftserver_host)
   
   gw = hbase_service.get_role_config_group("{0}-GATEWAY-BASE".format(hbase_service_name))
   gw.update_config(hbase_gw_config)
   
   regionserver = 0
   for host in hbase_rs_hosts:
      regionserver += 1
      hbase_service.create_role("{0}-rs-".format(hbase_service_name) + str(regionserver), "REGIONSERVER", host)
   
   gateway = 0
   for host in hbase_gw_hosts:
      gateway += 1
      hbase_service.create_role("{0}-gw-".format(hbase_service_name) + str(gateway), "GATEWAY", host)
   
   hbase_service.create_hbase_root()
   
   return hbase_service


# Deploys Hive - hive metastore, hiveserver2, webhcat, gateways
def deploy_hive(cluster, hive_service_name, hive_service_config, hive_hms_host, hive_hms_config, hive_hs2_host, hive_hs2_config, hive_whc_host, hive_whc_config, hive_gw_hosts, hive_gw_config):
   hive_service = cluster.create_service(hive_service_name, "HIVE")
   hive_service.update_config(hive_service_config)
   
   hms = hive_service.get_role_config_group("{0}-HIVEMETASTORE-BASE".format(hive_service_name))
   hms.update_config(hive_hms_config)
   hive_service.create_role("{0}-hms".format(hive_service_name), "HIVEMETASTORE", hive_hms_host)
   
   hs2 = hive_service.get_role_config_group("{0}-HIVESERVER2-BASE".format(hive_service_name))
   hs2.update_config(hive_hs2_config)
   hive_service.create_role("{0}-hs2".format(hive_service_name), "HIVESERVER2", hive_hs2_host)
   
   whc = hive_service.get_role_config_group("{0}-WEBHCAT-BASE".format(hive_service_name))
   whc.update_config(hive_whc_config)
   hive_service.create_role("{0}-whc".format(hive_service_name), "WEBHCAT", hive_whc_host)
   
   gw = hive_service.get_role_config_group("{0}-GATEWAY-BASE".format(hive_service_name))
   gw.update_config(hive_gw_config)
   
   gateway = 0
   for host in hive_gw_hosts:
      gateway += 1
      hive_service.create_role("{0}-gw-".format(hive_service_name) + str(gateway), "GATEWAY", host)
   
   return hive_service


# Initialized hive
def init_hive(hive_service):
   hive_service.create_hive_metastore_database()
   hive_service.create_hive_metastore_tables()
   hive_service.create_hive_warehouse()
   #don't think that the create_hive_userdir call is needed as the create_hive_warehouse already creates it
   #hive_service.create_hive_userdir()


# Deploys Impala - statestore, catalogserver, impalads
def deploy_impala(cluster, impala_service_name, impala_service_config, impala_ss_host, impala_ss_config, impala_cs_host, impala_cs_config, impala_id_hosts, impala_id_config):
   impala_service = cluster.create_service(impala_service_name, "IMPALA")
   impala_service.update_config(impala_service_config)
   
   ss = impala_service.get_role_config_group("{0}-STATESTORE-BASE".format(impala_service_name))
   ss.update_config(impala_ss_config)
   impala_service.create_role("{0}-ss".format(impala_service_name), "STATESTORE", impala_ss_host)
   
   cs = impala_service.get_role_config_group("{0}-CATALOGSERVER-BASE".format(impala_service_name))
   cs.update_config(impala_cs_config)
   impala_service.create_role("{0}-cs".format(impala_service_name), "CATALOGSERVER", impala_cs_host)
   
   id = impala_service.get_role_config_group("{0}-IMPALAD-BASE".format(impala_service_name))
   id.update_config(impala_id_config)
   
   impalad = 0
   for host in impala_id_hosts:
      impalad += 1
      impala_service.create_role("{0}-id-".format(impala_service_name) + str(impalad), "IMPALAD", host)

   # Don't think we need these at the end:
   #impala_service.create_impala_catalog_database()
   #impala_service.create_impala_catalog_database_tables()
   #impala_service.create_impala_user_dir()
   
   return impala_service
   

# Deploys Search - solr server, gateways
def deploy_search(cluster, search_service_name, search_service_config, search_solr_host, search_solr_config, search_gw_hosts, search_gw_config):
   search_service = cluster.create_service(search_service_name, "SOLR")
   search_service.update_config(search_service_config)
   
   solr = search_service.get_role_config_group("{0}-SOLR_SERVER-BASE".format(search_service_name))
   solr.update_config(search_solr_config)
   search_service.create_role("{0}-solr".format(search_service_name), "SOLR_SERVER", search_solr_host)
   
   gw = search_service.get_role_config_group("{0}-GATEWAY-BASE".format(search_service_name))
   gw.update_config(search_gw_config)
   
   gateway = 0
   for host in search_gw_hosts:
      gateway += 1
      search_service.create_role("{0}-gw-".format(search_service_name) + str(gateway), "GATEWAY", host)
      
   #solrctl init
   zk_ensemble = ZOOKEEPER_HOSTS[0] + ":2181," + ZOOKEEPER_HOSTS[1] + ":2181," + ZOOKEEPER_HOSTS[2] + ":2181/solr" 
   shell_command = ["export SOLR_ZK_ENSEMBLE=" + zk_ensemble + "; solrctl init"]
   solrctl_init_output = Popen(shell_command, shell=True, stdin=PIPE, stdout=PIPE, stderr=STDOUT, close_fds=True).stdout.read()
   
   return search_service


# Deploys Flume - agents
def deploy_flume(cluster, flume_service_name, flume_service_config, flume_agent_hosts, flume_agent_config):
   flume_service = cluster.create_service(flume_service_name, "FLUME")
   flume_service.update_config(flume_service_config)
   
   gw = flume_service.get_role_config_group("{0}-AGENT-BASE".format(flume_service_name))
   gw.update_config(flume_agent_config)
   
   agent = 0
   for host in flume_agent_hosts:
      agent += 1
      flume_service.create_role("{0}-agent-".format(flume_service_name) + str(agent), "AGENT", host)
   
   return flume_service


# Deploys Oozie - oozie server.
# This does not support HA yet.
def deploy_oozie(cluster, oozie_service_name, oozie_service_config, oozie_server_host, oozie_server_config):
   oozie_service = cluster.create_service(oozie_service_name, "OOZIE")
   oozie_service.update_config(oozie_service_config)
   
   oozie_server = oozie_service.get_role_config_group("{0}-OOZIE_SERVER-BASE".format(oozie_service_name))
   oozie_server.update_config(oozie_server_config)
   oozie_service.create_role("{0}-server".format(oozie_service_name), "OOZIE_SERVER", oozie_server_host)
   
   oozie_service.install_oozie_sharelib()
   
   return oozie_service


# Deploys Sqoop - sqoop server
def deploy_sqoop(cluster, sqoop_service_name, sqoop_service_config, sqoop_server_host, sqoop_server_config):
   sqoop_service = cluster.create_service(sqoop_service_name, "SQOOP")
   sqoop_service.update_config(sqoop_service_config)
   
   oozie_server = sqoop_service.get_role_config_group("{0}-SQOOP_SERVER-BASE".format(sqoop_service_name))
   oozie_server.update_config(sqoop_server_config)
   sqoop_service.create_role("{0}-server".format(sqoop_service_name), "SQOOP_SERVER", sqoop_server_host)
   
   return sqoop_service


# Deploys HUE - hue server
def deploy_hue(cluster, hue_service_name, hue_service_config, hue_server_host, hue_server_config, hue_ktr_host, hue_ktr_config):
   hue_service = cluster.create_service(hue_service_name, "HUE")
   hue_service.update_config(hue_service_config)
   
   hue_server = hue_service.get_role_config_group("{0}-HUE_SERVER-BASE".format(hue_service_name))
   hue_server.update_config(hue_server_config)
   hue_service.create_role("{0}-server".format(hue_service_name), "HUE_SERVER", hue_server_host)
   
   #ktr = hue_service.get_role_config_group("{0}-KT_RENEWER-BASE".format(hue_service_name))
   #ktr.update_config(hue_ktr_config)
   #hue_service.create_role("{0}-ktr".format(hue_service_name), "KT_RENEWER", hue_ktr_host)
   
   return hue_service


# Deploys Accumulo - masters, tracers, tservers, loggers, gateways, monitor, GC
def deploy_accumulo(cluster, service_name, service_config, master_hosts, master_config, tracer_hosts, tracer_config, tserver_hosts, tserver_config, logger_hosts, logger_config, monitor_host, monitor_config, gc_host, gc_config, gw_hosts, gw_config):

   accumulo = cluster.create_service(service_name, "csd_accumulo")
   accumulo.update_config(service_config)

   accumulo_masters_group = accumulo.get_role_config_group("{0}-MASTER-BASE".format(service_name))
   accumulo_masters_group.update_config(master_config)

   accumulo_tracers_group = accumulo.get_role_config_group("{0}-TRACER-BASE".format(service_name))
   accumulo_tracers_group.update_config(tracer_config)

   accumulo_tservers_group = accumulo.get_role_config_group("{0}-TSERVER-BASE".format(service_name))
   accumulo_tservers_group.update_config(tserver_config)

   accumulo_loggers_group = accumulo.get_role_config_group("{0}-LOGGER-BASE".format(service_name))
   accumulo_loggers_group.update_config(logger_config)

   accumulo_gateways_group = accumulo.get_role_config_group("{0}-GATEWAY-BASE".format(service_name))
   accumulo_gateways_group.update_config(gw_config)

   accumulo_monitor_group = accumulo.get_role_config_group("{0}-MONITOR-BASE".format(service_name))
   accumulo_monitor_group.update_config(monitor_config)

   accumulo_gc_group = accumulo.get_role_config_group("{0}-GC-BASE".format(service_name))
   accumulo_gc_group.update_config(gc_config)

   #Create Accumulo Masters
   count = 0
   for host in master_hosts:
      count += 1
      accumulo.create_role("{0}-master".format(service_name) + str(count), "csd_accumulo_master", host)

   #Create Accumulo Loggers
   count = 0
   for host in logger_hosts:
      count += 1
      accumulo.create_role("{0}-logger".format(service_name) + str(count), "csd_accumulo_logger", host)

   #Create Accumulo TServers
   count = 0
   for host in tserver_hosts:
      count += 1
      accumulo.create_role("{0}-tserver".format(service_name) + str(count), "csd_accumulo_tserver", host)

   #Create Accumulo Gateways
   count = 0
   for host in gw_hosts:
      count += 1
      accumulo.create_role("{0}-gateway".format(service_name) + str(count), "GATEWAY", host)

   #Create Accumulo Tracers
   count = 0
   for host in tracer_hosts:
      count += 1
      accumulo.create_role("{0}-tracer".format(service_name) + str(count), "csd_accumulo_tracer", host)

   #Create Accumulo GC
   accumulo.create_role("{0}-gc".format(service_name), "csd_accumulo_gc", gc_host)

   #Create Accumulo Monitor
   accumulo.create_role("{0}-monitor".format(service_name), "csd_accumulo_monitor", monitor_host)

   return accumulo


# Executes steps that need to be done after the final startup once everything is deployed and running.
def post_startup(cluster, hdfs_service, oozie_service):
   # Create HDFS temp dir
   hdfs_service.create_hdfs_tmp()
   
   # Create hive warehouse dir
   shell_command = ['curl -i -H "Content-Type: application/json" -X POST -u "' + ADMIN_USER + ':' + ADMIN_PASS + '" -d "serviceName=' + HIVE_SERVICE_NAME + ';clusterName=' + CLUSTER_NAME + '" http://' + CM_HOST + ':7180/api/v5/clusters/' + CLUSTER_NAME + '/services/' + HIVE_SERVICE_NAME + '/commands/hiveCreateHiveWarehouse']
   create_hive_warehouse_output = Popen(shell_command, shell=True, stdin=PIPE, stdout=PIPE, stderr=STDOUT, close_fds=True).stdout.read()
   
   # Create oozie database
   oozie_service.stop().wait()
   shell_command = ['curl -i -H "Content-Type: application/json" -X POST -u "' + ADMIN_USER + ':' + ADMIN_PASS + '" -d "serviceName=' + OOZIE_SERVICE_NAME + ';clusterName=' + CLUSTER_NAME + '" http://' + CM_HOST + ':7180/api/v5/clusters/' + CLUSTER_NAME + '/services/' + OOZIE_SERVICE_NAME + '/commands/createOozieDb']
   create_oozie_db_output = Popen(shell_command, shell=True, stdin=PIPE, stdout=PIPE, stderr=STDOUT, close_fds=True).stdout.read()
   # give the create db command time to complete
   time.sleep(30)
   oozie_service.start().wait()
   
   # Deploy client configs to all necessary hosts
   cmd = cluster.deploy_client_config()
   if not cmd.wait(CMD_TIMEOUT).success:
      print "Failed to deploy client configs for {0}".format(cluster.name)
   
   # Noe change permissions on the /user dir so YARN will work
   shell_command = ['sudo -u hdfs hadoop fs -chmod 775 /user']
   user_chmod_output = Popen(shell_command, shell=True, stdin=PIPE, stdout=PIPE, stderr=STDOUT, close_fds=True).stdout.read()


### Main function ###
def main():
   API = ApiResource(CM_HOST, version=5, username=ADMIN_USER, password=ADMIN_PASS)
   MANAGER = API.get_cloudera_manager()
   MANAGER.update_config(CM_CONFIG)
   print "Connected to CM host on " + CM_HOST + " and updated CM configuration"

   CLUSTER = init_cluster(API, CLUSTER_NAME, CDH_VERSION, CLUSTER_HOSTS, CM_HOST)
   print "Initialized cluster " + CLUSTER_NAME + " which uses CDH version " + CDH_VERSION

   deploy_management(MANAGER, MGMT_SERVICENAME, MGMT_SERVICE_CONFIG, MGMT_ROLE_CONFIG, AMON_ROLENAME, AMON_ROLE_CONFIG, APUB_ROLENAME, APUB_ROLE_CONFIG, ESERV_ROLENAME, ESERV_ROLE_CONFIG, HMON_ROLENAME, HMON_ROLE_CONFIG, SMON_ROLENAME, SMON_ROLE_CONFIG, NAV_ROLENAME, NAV_ROLE_CONFIG, NAVMS_ROLENAME, NAVMS_ROLE_CONFIG, RMAN_ROLENAME, RMAN_ROLE_CONFIG)
   print "Deployed CM management service " + MGMT_SERVICENAME + " to run on " + CM_HOST
   
   deploy_parcels(CLUSTER, PARCELS)
   print "Downloaded and distributed parcels: "
   PRETTY_PRINT.pprint(PARCELS)

   zookeeper_service = deploy_zookeeper(CLUSTER, ZOOKEEPER_SERVICE_NAME, ZOOKEEPER_HOSTS, ZOOKEEPER_SERVICE_CONFIG, ZOOKEEPER_ROLE_CONFIG)
   print "Deployed ZooKeeper " + ZOOKEEPER_SERVICE_NAME + " to run on: "
   PRETTY_PRINT.pprint(ZOOKEEPER_HOSTS)
   
   hdfs_service = deploy_hdfs(CLUSTER, HDFS_SERVICE_NAME, HDFS_SERVICE_CONFIG, HDFS_NAMENODE_SERVICE_NAME, HDFS_NAMENODE_HOST, HDFS_NAMENODE_CONFIG, HDFS_SECONDARY_NAMENODE_HOST, HDFS_SECONDARY_NAMENODE_CONFIG, HDFS_DATANODE_HOSTS, HDFS_DATANODE_CONFIG, HDFS_GATEWAY_HOSTS, HDFS_GATEWAY_CONFIG)
   print "Deployed HDFS service " + HDFS_SERVICE_NAME + " using NameNode on " + HDFS_NAMENODE_HOST + ", SecondaryNameNode on " + HDFS_SECONDARY_NAMENODE_HOST + ", and DataNodes running on: "
   PRETTY_PRINT.pprint(HDFS_DATANODE_HOSTS)
   init_hdfs(hdfs_service, HDFS_SERVICE_NAME, CMD_TIMEOUT)
   print "Initialized HDFS service"

   # mapred and yarn are mutually exclusive; only deploy one of them
   #mapred_service = deploy_mapreduce(CLUSTER, MAPRED_SERVICE_NAME, MAPRED_SERVICE_CONFIG, MAPRED_JT_HOST, MAPRED_JT_CONFIG, MAPRED_TT_HOSTS, MAPRED_TT_CONFIG, MAPRED_GW_HOSTS, MAPRED_GW_CONFIG)
   print "Deployed MapReduce service " + MAPRED_SERVICE_NAME + " using JobTracker on " + MAPRED_JT_HOST + " and TaskTrackers running on "
   PRETTY_PRINT.pprint(MAPRED_TT_HOSTS)
   
   yarn_service = deploy_yarn(CLUSTER, YARN_SERVICE_NAME, YARN_SERVICE_CONFIG, YARN_RM_HOST, YARN_RM_CONFIG, YARN_JHS_HOST, YARN_JHS_CONFIG, YARN_NM_HOSTS, YARN_NM_CONFIG, YARN_GW_HOSTS, YARN_GW_CONFIG)
   print "Deployed YARN service " + YARN_SERVICE_NAME + " using ResourceManager on " + YARN_RM_HOST + ", JobHistoryServer on " + YARN_JHS_HOST + ", and NodeManagers on "
   PRETTY_PRINT.pprint(YARN_NM_HOSTS)
   
   spark_service = deploy_spark(CLUSTER, SPARK_SERVICE_NAME, SPARK_SERVICE_CONFIG, SPARK_MASTER_HOST, SPARK_MASTER_CONFIG, SPARK_WORKER_HOSTS, SPARK_WORKER_CONFIG, SPARK_GW_HOSTS, SPARK_GW_CONFIG)
   print "Deployed SPARK service " + SPARK_SERVICE_NAME + " using SparkMaster on " + SPARK_MASTER_HOST + " and SparkWorkers on "
   PRETTY_PRINT.pprint(SPARK_WORKER_HOSTS)
   
   deploy_hbase(CLUSTER, HBASE_SERVICE_NAME, HBASE_SERVICE_CONFIG, HBASE_HM_HOST, HBASE_HM_CONFIG, HBASE_RS_HOSTS, HBASE_RS_CONFIG, HBASE_THRIFTSERVER_SERVICE_NAME, HBASE_THRIFTSERVER_HOST, HBASE_THRIFTSERVER_CONFIG, HBASE_GW_HOSTS, HBASE_GW_CONFIG)
   print "Deployed HBase service " + HBASE_SERVICE_NAME + " using HMaster on " + HBASE_HM_HOST + " and RegionServers on "
   PRETTY_PRINT.pprint(HBASE_RS_HOSTS)
   
   hive_service = deploy_hive(CLUSTER, HIVE_SERVICE_NAME, HIVE_SERVICE_CONFIG, HIVE_HMS_HOST, HIVE_HMS_CONFIG, HIVE_HS2_HOST, HIVE_HS2_CONFIG, HIVE_WHC_HOST, HIVE_WHC_CONFIG, HIVE_GW_HOSTS, HIVE_GW_CONFIG)
   print "Depoyed Hive service " + HIVE_SERVICE_NAME + " using HiveMetastoreServer on " + HIVE_HMS_HOST + " and HiveServer2 on " + HIVE_HS2_HOST
   init_hive(hive_service)
   print "Initialized Hive service"
   
   impala_service = deploy_impala(CLUSTER, IMPALA_SERVICE_NAME, IMPALA_SERVICE_CONFIG, IMPALA_SS_HOST, IMPALA_SS_CONFIG, IMPALA_CS_HOST, IMPALA_CS_CONFIG, IMPALA_ID_HOSTS, IMPALA_ID_CONFIG)
   print "Deployed Impala service " + IMPALA_SERVICE_NAME + " using StateStore on " + IMPALA_SS_HOST + ", CatalogServer on " + IMPALA_CS_HOST + ", and ImpalaDaemons on "
   PRETTY_PRINT.pprint(IMPALA_ID_HOSTS)
   
   #Need to start the cluster now as subsequent services need the cluster to be runnign
   #TODO can we just start ZK, and maybe HDFS, instead of everything? It's just needed for the search service
   print "About to restart cluster"
   CLUSTER.stop().wait()
   CLUSTER.start().wait()
   print "Done restarting cluster"

   search_service = deploy_search(CLUSTER, SEARCH_SERVICE_NAME, SEARCH_SERVICE_CONFIG, SEARCH_SOLR_HOST, SEARCH_SOLR_CONFIG, SEARCH_GW_HOSTS, SEARCH_GW_CONFIG)
   print "Deployed Search service " + SEARCH_SERVICE_NAME + " using SOLRHost " + SEARCH_SOLR_HOST
   
   flume_service = deploy_flume(CLUSTER, FLUME_SERVICE_NAME, FLUME_SERVICE_CONFIG, FLUME_AGENT_HOSTS, FLUME_AGENT_CONFIG)
   print "Deployed Flume service " + FLUME_SERVICE_NAME + " using FlumeAgents on "
   PRETTY_PRINT.pprint(FLUME_AGENT_HOSTS)
   
   oozie_service = deploy_oozie(CLUSTER, OOZIE_SERVICE_NAME, OOZIE_SERVICE_CONFIG, OOZIE_SERVER_HOST, OOZIE_SERVER_CONFIG)
   print "Deployed Oozie service " + OOZIE_SERVICE_NAME + " using OozieServer on " + OOZIE_SERVER_HOST
   
   sqoop_service = deploy_sqoop(CLUSTER, SQOOP_SERVICE_NAME, SQOOP_SERVICE_CONFIG, SQOOP_SERVER_HOST, SQOOP_SERVER_CONFIG)
   print "Deployed Sqoop service " + SQOOP_SERVICE_NAME + " using SqoopServer on " + SQOOP_SERVER_HOST
   
   hue_service = deploy_hue(CLUSTER, HUE_SERVICE_NAME, HUE_SERVICE_CONFIG, HUE_SERVER_HOST, HUE_SERVER_CONFIG, HUE_KTR_HOST, HUE_KTR_CONFIG)
   print "Deployed HUE service " + HUE_SERVICE_NAME + " using HueServer on " + HUE_SERVER_HOST
   
   #deploy_accumulo(CLUSTER, ACCUMULO_SERVICE_NAME, ACCUMULO_SERVICE_CONFIG, ACCUMULO_MASTER_HOSTS, ACCUMULO_MASTER_CONFIG, ACCUMULO_TRACER_HOSTS, ACCUMULO_TRACER_CONFIG, ACCUMULO_TSERVER_HOSTS, ACCUMULO_TSERVER_CONFIG, ACCUMULO_LOGGER_HOSTS, ACCUMULO_LOGGER_CONFIG, ACCUMULO_MONITOR_HOST, ACCUMULO_MONITOR_CONFIG, ACCUMULO_GC_HOST, ACCUMULO_GC_CONFIG, ACCUMULO_GATEWAY_HOSTS, ACCUMULO_GATEWAY_CONFIG)
   
   print "About to restart cluster."
   CLUSTER.stop().wait()
   CLUSTER.start().wait()
   print "Done restarting cluster."
   
   post_startup(CLUSTER, hdfs_service, oozie_service)

   print "Finished deploying Cloudera cluster. Go to http://" + CM_HOST + ":7180 to administer the cluster."
   print "If the Oozie service (and therefore the HUE service as well, which depends on it) did not start properly, go to the Oozie service, stop it, click on the Actions button and choose 'Create Database', then start it."
   print "If there are any other services not running, restart them now."
   
   
if __name__ == "__main__":
   main()
