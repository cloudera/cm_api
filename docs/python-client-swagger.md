---
layout: default
title: Python Client (Swagger)
id: python-client-swagger
permalink: /docs/python-client-swagger/
---

#### Table of Contents ####

* toc
{:toc}

Installation
============
TODO: Python pip installation steps.

Basic Usage
===========
Each subsection continues from the previous one.

{% highlight python %}

import cm_client
from cm_client.rest import ApiException
from pprint import pprint

# Configure HTTP basic authorization: basic
cm_client.configuration.username = 'username'
cm_client.configuration.password = 'password'

# Create an instance of the API class
api_host = 'http://cmhost'
port = '7180'
api_version = 'v30'
# Construct base URL for API
# http://cmhost:7180/api/v30
api_url = api_host + ':' + port + '/api/' + api_version
api_client = cm_client.ApiClient(api_url)
cluster_api_instance = cm_client.ClustersResourceApi(api_client)

# Lists all known clusters.
api_response = cluster_api_instance.read_clusters(view='SUMMARY')
for cluster in api_response.items:
    print cluster.name, -, cluster.full_version

## -- Output --
# Cluster 1 - 6.0.0
# Cluster 2 - 5.14.0
{% endhighlight %}

TLS configuration can be specified in `cm_client.configuration` using parameters `verify_ssl`, `ssl_ca_cert`.
{% highlight python %}

# Configure HTTPS authentication
cm_client.configuration.username = 'username'
cm_client.configuration.password = 'password'
cm_client.configuration.verify_ssl = True
# Path of truststore file in PEM
cm_client.configuration.ssl_ca_cert = '/path/to/truststore.pem'

api_host = 'https://cmhost'
port = '7183'
api_version = 'v30'
# Construct base URL for API
# https://cmhost:7183/api/v30
api_url = api_host + ':' + port + '/api/' + api_version
api_client = cm_client.ApiClient(api_url)

# Rest same as above
{% endhighlight %}


Inspecting a Service
--------------------

Now we have identified a CDH6 cluster, find the HDFS service:

{% highlight python %}
# Look for CDH6 clusters
if cluster.full_version.startswith("6."):
    services_api_instance = cm_client.ServicesResourceApi(api_client)
    services = services_api_instance.read_services(cluster.name, view='FULL')
    for service in services.items:
        print service.display_name, "-", service.type
        if service.type == 'HDFS':
            hdfs = service

## -- Output --
# KUDU-1 - KUDU
# ZOOKEEPER-1 - ZOOKEEPER
# HDFS-1 - HDFS
# KMS-1 - KMS
# HBASE-1 - HBASE
# SENTRY-1 - SENTRY
# OOZIE-1 - OOZIE
# HIVE-1 - HIVE
# HUE-1 - HUE
# SOLR-1 - SOLR
# KS_INDEXER-1 - KS_INDEXER
# YARN-1 - YARN
# IMPALA-1 - IMPALA
# FLUME-1 - FLUME
# SQOOP_CLIENT-1 - SQOOP_CLIENT
# SPARK_ON_YARN-1 - SPARK_ON_YARN
# KAFKA-1 - KAFKA

{% endhighlight %}

Inspect the HDFS service health and status:

{% highlight python %}
print hdfs.name, hdfs.service_state, hdfs.health_summary
## -- Output --
# HDFS-1 STARTED GOOD

print hdfs.service_url
## -- Output --
# http://cm-host:7180/cmf/serviceRedirect/HDFS-1

for health_check in hdfs.health_checks:
    print health_check.name, "---", health_check.summary

## -- Output --
# HDFS_BLOCKS_WITH_CORRUPT_REPLICAS --- GOOD
# HDFS_CANARY_HEALTH --- GOOD
# HDFS_DATA_NODES_HEALTHY --- GOOD
# HDFS_FREE_SPACE_REMAINING --- GOOD
# HDFS_HA_NAMENODE_HEALTH --- GOOD
# HDFS_MISSING_BLOCKS --- GOOD
# HDFS_STANDBY_NAMENODES_HEALTHY --- DISABLED
# HDFS_UNDER_REPLICATED_BLOCKS --- GOOD
{% endhighlight %}

Inspecting a Role
-----------------

Find the NameNode and get basic info:

{% highlight python %}
roles = services_api_instance.read_roles(cluster.name, hdfs.name)
for role in roles.items:
    if role.type == 'NAMENODE':
        nn = role
        print "Role name: %s\nState: %s\nHealth: %s\nHost: %s" % (
            nn.name, nn.role_state, nn.health_summary, nn.host_ref.host_id)

## -- Output --
# Role name: HDFS-1-NAMENODE-f21bfbd1733503c8dc264d9d2aae01c2
# State: STARTED
# Health: GOOD
# Host: f622a819-9355-460b-bf09-ca9e1f56b634

{% endhighlight %}

Similar to the service example, roles also expose their health checks.


Getting Metrics
---------------

First we look at what metrics are available:

{% highlight python %}
# NOTE: this does not work starting in v6 of the api (CM5.0.0). Use the
# timeseries endpoint dicussed below or set your api version to v5.

api_url_v5 = api_host + '/api/' + 'v5'
api_client_v5 = cm_client.ApiClient(api_url_v5)
services_api_instance_v5 = cm_client.ServicesResourceApi(api_client_v5)
metrics = services_api_instance_v5.get_metrics(cluster.name, hdfs.name)
for m in metrics.items:
    print "%s (%s)" % (m.name, m.unit)

## -- Output --
# fsync_nanos_avg_time_datanode_min (nanos)
# fsync_nanos_avg_time_datanode_weighted_std_dev (nanos)
# files_total (blocks)
# excess_blocks (blocks)
# block_capacity (blocks)
# pending_replication_blocks (blocks)
# scheduled_replication_blocks (blocks)
# rpc_processing_time_time_datanode_sum (ms)
# rpc_processing_time_avg_time_datanode_max (ms)
# rpc_processing_time_avg_time_datanode_min (ms)
# rpc_processing_time_avg_time_datanode_weighted_std_dev (ms)
# rpc_processing_time_avg_time_datanode_weighted_avg (ms)
# events_important (events)
# heartbeats_num_ops_datanode_std_dev_rate (operations per second)
# total_load (transceivers)
# fsync_nanos_time_datanode_sum (nanos)
# expired_heartbeats (heartbeats)
# heartbeats_num_ops_datanode_min_rate (operations per second)
#     ... omitting the other 600+ metrics
{% endhighlight %}

Reading a metric: Suppose we are interested in the `files_total` and
`dfs_capacity_used` metrics, over the last 30 minutes.

{% highlight python %}
import time
import datetime

api_instance = cm_client.TimeSeriesResourceApi(api_client)
from_time = datetime.datetime.fromtimestamp(time.time() - 180000)
to_time = datetime.datetime.fromtimestamp(time.time())
query = "select files_total, dfs_capacity_used " \
        "where serviceName = HDFS-1 " \
        "  and category = SERVICE"
# Retrieve time-series data from the Cloudera Manager (CM) time-series data store using a tsquery.
result = api_instance.query_time_series(_from=from_time, query=query, to=to_time)
ts_list = result.items[0]
for ts in ts_list.time_series:
    print "--- %s: %s ---" % (ts.metadata.attributes['entityName'], ts.metadata.metric_name)
    for point in ts.data:
        print "%s:\t%s" % (point.timestamp.isoformat(), point.value)

## -- Output --
# --- HDFS-1: files_total ---
# 2013-09-04T22:12:34.983000:     157.0
# 2013-09-04T22:13:34.984000:     157.0
# 2013-09-04T22:14:34.984000:     157.0
#     ... omitting a bunch of values
# 
# --- HDFS-1: dfs_capacity_used ---
# 2013-09-04T22:12:34.983000:     186310656.0
# 2013-09-04T22:13:34.984000:     186310656.0
# 2013-09-04T22:14:34.984000:     186310656.0
#     ... omitting a bunch of values
{% endhighlight %}

This example uses the new-style `/cm/timeseries` endpoint (which uses
[tsquery](http://tiny.cloudera.com/tsquery)) to get metric data points.
Even though the example is querying HDFS metrics, the processing logic is the
same for all queries.

The old-style `.../metrics` endpoints (which exists under host, service
and role objects) are mostly useful for exploring what metrics are available.


Service Lifecycle and Commands
------------------------------

Restart HDFS. Start and stop work similarly:

{% highlight python %}
cmd = services_api_instance.restart_command(cluster.name, service.name)
print cmd.active
## -- Output --
# True
{% endhighlight %}

Example of wait() method to poll and wait for asynchronous command like restart
{% highlight python %}
def wait(cmd, timeout=None):
    SYNCHRONOUS_COMMAND_ID = -1
    if cmd.id == SYNCHRONOUS_COMMAND_ID:
        return cmd

    SLEEP_SECS = 5
    if timeout is None:
        deadline = None
    else:
        deadline = time.time() + timeout

    try:
        cmd_api_instance = cm_client.CommandsResourceApi(api_client)
        while True:
            cmd = cmd_api_instance.read_command(long(cmd.id))
            pprint(cmd)
            if not cmd.active:
                return cmd

            if deadline is not None:
                now = time.time()
                if deadline < now:
                    return cmd
                else:
                    time.sleep(min(SLEEP_SECS, deadline - now))
            else:
                time.sleep(SLEEP_SECS)
    except ApiException as e:
        print "Exception reading and waiting for command %s\n" %e

wait(cmd)
print "Active: %s. Success: %s" % (cmd.active, cmd.success)
## -- Output --
# Active: False. Success: True
{% endhighlight %}

Restart the NameNodes. Commands on roles are issued at the RoleCommands endpoint
under service and can be done in bulk.

{% highlight python %}
role_response = services_api_instance.read_roles(cluster.name, hdfs.name)
# Extract NAMENODE roles from the HDFS service
nn_roles = [role.name for role in role_response.items if role.type == 'NAMENODE']
roles_cmd_api_instance = cm_client.RoleCommandsResourceApi(api_client)
role_names = cm_client.ApiRoleNameList(nn_roles)
cmd_list = roles_cmd_api_instance.restart_command(cluster.name, hdfs.name, body=role_names)
for cmd in cmd_list.items:
    print cmd.name, "(", cmd.id, cmd.active, cmd.success, ")"

## -- Output --
# Restart ( 293.0 True None )
{% endhighlight %}

Configuring Services and Roles
------------------------------
First, lets look at all possible service configs. For legacy reasons, this is a
2-tuple of service configs and an empty dictionary of role_type_configs (as of API v3).
{% highlight python %}
configs = services_api_instance.read_service_config(cluster.name, hdfs.name, view='FULL')
for config in configs.items:
    print "%s - %s - %s" % (config.name, config.related_name, config.description)

## -- Output --
# dfs_replication - dfs.replication - Default block replication. The number of replications to make when the file is created. The default value is used if a replication number is not specified.
{% endhighlight %}

Now let's change dfs_replication to 2. We use "dfs_replication" and not
dfs.replication" because we must match the keys of the config view. This is
also the same value as ApiConfig.name.

{% highlight python %}
new_config = cm_client.ApiConfig(name="dfs_replication", value=2)
body = cm_client.ApiServiceConfig([new_config])
updated_configs = services_api_instance.update_service_config(cluster.name, hdfs.name, body=body)
for updated_config in updated_configs.items:
    print "%s - %s" % (updated_config.name, updated_config.value)

# returns current configs, excluding defaults. Same as hdfs.get_config()
## -- Output --
# dfs_replication - 2
# hdfs_blocks_with_corrupt_replicas_thresholds - {"warning":"2.5","critical":"3.0"}
# zookeeper_service - 3

{% endhighlight %}

Configuring roles is done similarly. Normally you want to modify groups instead
of modifying each role one by one.

First, find the group(s).
{% highlight python %}
rcg_configs = rcg_api_instance.read_role_config_groups(cluster.name, hdfs.name)
dn_groups = [rcg_config.name for rcg_config in rcg_configs.items
 if rcg_config.role_type == 'DATANODE']
{% endhighlight %}

See all possible role configuration. It's the same for all groups of the same
role type in clusters with the same CDH version.

{% highlight python %}
rcg_config = rcg_api_instance.read_config(cluster.name, dn_groups[0], hdfs.name, view='FULL')
for config in rcg_config.items:
    print "%s - %s - %s" % (config.name, config.related_name, config.description)

## -- Output -- (just one line of many)
# process_auto_restart -  - When set, this role's process is automatically (and transparently) restarted in the event of an unexpected failure.
{% endhighlight %}

Let's configure our data nodes to auto-restart:

{% highlight python %}
new_config = cm_client.ApiConfig(name='process_auto_restart', value=True)
new_config_list = cm_client.ApiConfigList([new_config])
updated_config_list = rcg_api_instance.update_config(cluster.name, dn_groups[0], hdfs.name, body=new_config_list)
for updated_config in updated_config_list.items:
    if updated_config.name == 'process_auto_restart':
        print updated_config.name, updated_config.value

# returns config summary for group.
## -- Output --
# process_auto_restart true
{% endhighlight %}

To reset a config to default, pass in a value of None:

{% highlight python %}
new_config = cm_client.ApiConfig(name='process_auto_restart', value=None)
new_config_list = cm_client.ApiConfigList([new_config])
updated_config_list = rcg_api_instance.update_config(cluster.name, dn_groups[0], hdfs.name, body=new_config_list)
for updated_config in updated_config_list.items:
    print updated_config.name, updated_config.value
# note process_auto_restart is missing from return value now
## -- Output (just 2 of many) --
# dfs_data_dir_list /dfs/dn
# dfs_datanode_du_reserved 10736126771

{% endhighlight %}

Managing Parcels
------------------------------

These examples cover how to get a new parcel up and running on
a cluster. Normally you would pick a specific parcel repository
and parcel version you want to install.

Add a CDH parcel repository. Note that in CDH 4, Impala and Solr are
in separate parcels. They are included in the CDH 5 parcel.

These examples require v5 of the CM API or higher.

{% highlight python %}
# replace parcel_repo with the parcel repo you want to use
parcel_repo = 'http://archive.cloudera.com/cdh5/parcels/5.14.2/'
cm_api_instance = cm_client.ClouderaManagerResourceApi(api_client)
cm_configs = cm_api_instance.get_config(view='full')
old_parcel_repo_urls = None
for cm_config in cm_configs.items:
    if cm_config.name == 'REMOTE_PARCEL_REPO_URLS':
        old_parcel_repo_urls = parcel_repo
        break;
# value is a comma-separated list
new_parcel_repo_urls = old_parcel_repo_urls + ", " + parcel_repo
new_cm_config = cm_client.ApiConfig(name='REMOTE_PARCEL_REPO_URLS', value=new_parcel_repo_urls)
new_cm_configs = cm_client.ApiConfigList([new_cm_config])
updated_cm_configs = cm_api_instance.update_config(body=new_cm_configs)
# wait to make sure parcels are refreshed
time.sleep(10)
{% endhighlight %}

Download the parcel to the CM server:

{% highlight python %}
# replace cluster_name with the name of your cluster
cluster_name = 'Cluster 1'
cluster = api.get_cluster(cluster_name)
# replace parcel_version with the specific parcel version you want to install
# After adding your parcel repository to CM, you can use the API to list all parcels and get the precise version string by inspecting:
# cluster.get_all_parcels() or looking at the URL http://<cm_host>:7180/api/v5/clusters/<cluster_name>/parcels/
parcels_api_instance = cm_client.ParcelsResourceApi(api_client)
parcels = parcels_api_instance.read_parcels(cluster_name, view=view)
parcel_version = None
parcel_product = 'CDH'
for parcel in parcels.items:
    if parcel.product == parcel_product:
        parcel_version = parcel.version

print("Starting download\n")
parcel_api_instance = cm_client.ParcelResourceApi(api_client)
parcel_api_instance.start_download_command(cluster_name, parcel_product, parcel_version)

# unlike other commands, check progress by looking at parcel stage and status
target_stage='DOWNLOADED'
while True:
    parcel = parcel_api_instance.read_parcel(cluster_name, parcel_product, parcel_version)
    if parcel.stage == target_stage:
        break
    if parcel.state.errors:
        raise Exception(str(parcel.state.errors))
    print("progress: %s / %s" % (parcel.state.progress, parcel.state.total_progress))
    time.sleep(15)

print("%s CDH parcel version %s on cluster %s" % (target_stage, parcel_version, cluster_name))
{% endhighlight %}

Distribute the parcel so all agents on that cluster have a local copy of the parcel.
{% highlight python %}
print("Starting distribution\n")
parcel_api_instance.start_distribution_command(cluster_name, parcel_product, parcel_version)

target_stage='DISTRIBUTED'
while True:
    parcel = parcel_api_instance.read_parcel(cluster_name, parcel_product, parcel_version)
    if parcel.stage == target_stage:
        break
    if parcel.state.errors:
        raise Exception(str(parcel.state.errors))
    print("progress: %s / %s" % (parcel.state.progress, parcel.state.total_progress))
    time.sleep(15)

print("%s CDH parcel version %s on cluster %s" % (target_stage, parcel_version, cluster_name))
{% endhighlight %}

Activate the parcel so services pick up the new binaries upon next restart:

{% highlight python %}
parcel_api_instance.activate_command(cluster_name, parcel_product, parcel_version)
{% endhighlight %}

Restart your cluster to pick up the new parcel:

{% highlight python %}
clusters_api_instance = cm_client.ClustersResourceApi(api_client)
restart_args = cm_client.ApiRestartClusterArgs()
restart_command = clusters_api_instance.restart_command(cluster_name, body=restart_args)
wait(restart_command)
{% endhighlight %}

Cluster Template
------------------------------

These examples cover how to export and import cluster template.

These examples requires v12 of the CM API or higher.

Import following modules:

{% highlight python %}
import cm_client
from cm_client.rest import ApiException
from pprint import pprint
import json
{% endhighlight %}

Export the cluster template as a json file:

{% highlight python %}
# Configure HTTP basic authorization: basic
cm_client.configuration.username = '<username>'
cm_client.configuration.password = '<password>'

api_url = "http://source-host:7180/api/v19"
api_client = cm_client.ApiClient(api_url)

# create an instance of the API class
cluster_name = 'Cluster 1' # str |
clusters_api_instance = cm_client.ClustersResourceApi(api_client)
template = clusters_api_instance.export(cluster_name)
with open('/tmp/cluster_template.json', 'w') as out_file:
    json.dump(template.to_dict(), out_file, indent=4, sort_keys=True)

{% endhighlight %}

Make the required changes in the template file manually or using the python API. User needs to map the hosts in the target cluster with right host templates and provide information about all the variables,like database information in the target cluster.

{% highlight xml %}
  "instantiator" : {
    "clusterName" : "<changeme>",
    "hosts" : [ {
      "hostName" : "<changeme>",
      "hostTemplateRefName" : "<changeme>",
      "roleRefNames" : [ "HDFS-1-NAMENODE-18041ba96f26361b0735d72598476dc1" ]
    }, {
      "hostName" : "<changeme>",
      "hostTemplateRefName" : "<changeme>"
    }, {
      "hostNameRange" : "<HOST[0001-0002]>",
      "hostTemplateRefName" : "<changeme>"
    } ],
    "variables" : [ {
      "name" : "FLUME-1-flume_truststore_password",
      "value" : "<changeme>"
    }, {
      "name" : "HBASE-1-HBASERESTSERVER-BASE-hbase_restserver_keystore_keypassword",
      "value" : "<changeme>"
    }, {
    .
    .
{% endhighlight %}

Invoking import cluster template on the target cluster:

{% highlight python %}
# Configure HTTP basic authorization for destination CM
cm_client.configuration.username = 'username'
cm_client.configuration.password = 'password'

api_url = "http://dst-host:7180/api/v19"
api_client = cm_client.ApiClient(api_url)

# Load the updated cluster template
with open('/tmp/cluster_template.json') as in_file:
    data = json.load(in_file)
dst_cluster_template = cm_client.ApiClusterTemplate(**data)

cm_api_instance = cm_client.ClouderaManagerResourceApi(api_client)
command = cm_api_instance.import_cluster_template(body=dst_cluster_template)
{% endhighlight %}

User can use this command to track the progress. The progress can be tracked by command details page in UI
or wait using the method mentioned above.

