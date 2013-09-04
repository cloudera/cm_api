---
layout: default
title: Python Client
id: python-client
permalink: /docs/python-client/
---

#### Table of Contents ####

* toc
{:toc}

Installation
============
To install the Python API client, simply:

    $ pip install cm_api

Alternatively, you can also install from source:

    $ git clone git://github.com/cloudera/cm_api.git
    $ cd cm_api/python
    $ python setup.py install
    $ pip dist/cm_api-*.tar.gz
    ## ... the distribution tarball is in ./dist/


If your system does not have `pip`, you can get it from your distro:

    $ sudo apt-get install python-pip
    ## ... or use `yum install' if you are on CentOS


Basic Usage
===========
Each subsection continues from the previous one.

{% highlight python %}
# Get a handle to the API client
from cm_api.api_client import ApiResource

cm_host = "cm-host"
api = ApiResource(cm_host, username="admin", password="admin")

# Get a list of all clusters
cdh4 = None
for c in api.get_all_clusters():
  print c.name
  if c.version == "CDH4":
    cdh4 = c

## -- Output --
# Cluster 1 - CDH4
# Cluster 2 - CDH3
{% endhighlight %}


The `ApiResource` constructor takes other optional arguments, to specify
the API version, CM server port, HTTPS vs HTTP, etc.


Inspecting a Service
--------------------

Now we have identified a CDH4 cluster. Find the HDFS service:

{% highlight python %}
for s in cdh4.get_all_services():
  print s
  if s.type == "HDFS":
    hdfs = s

## -- Output --
# <ApiService>: ZOOKEEPER-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: HDFS-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: HBASE-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: MAPREDUCE-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: YARN-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: OOZIE-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: HIVE-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: HUE-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: IMPALA-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: SOLR-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: KS_INDEXER-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: SQOOP-1 (cluster: Cluster 1 - CDH4)
# <ApiService>: FLUME-1 (cluster: Cluster 1 - CDH4)
{% endhighlight %}

Inspect the HDFS service health and status:

{% highlight python %}
print hdfs.name, hdfs.serviceState, hdfs.healthSummary
## -- Output --
# HDFS-1 STARTED GOOD

print hdfs.serviceUrl
## -- Output --
# http://cm-host:7180/cmf/serviceRedirect/HDFS-1

for chk in hdfs.healthChecks:
  print "%s --- %s" % (chk['name'], chk['summary'])

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
nn = None
for r in hdfs.get_all_roles():
  if r.type == 'NAMENODE':
    nn = r
print "Role name: %s\nState: %s\nHealth: %s\nHost: %s" % (
    nn.name, nn.roleState, nn.healthSummary, nn.hostRef.hostId)

## -- Output --
# Role name: HDFS-1-NAMENODE-03d442721157b71e72b7469113f254f5
# State: STOPPED
# Health: GOOD
# Host: nightly47-1.ent.cloudera.com
{% endhighlight %}

Similar to the service example, roles also expose their health checks.


Getting Metrics
---------------

First we look at what metrics are available:

{% highlight python %}
metrics = hdfs.get_metrics()
print len(metrics)
## -- Output --
# 651

for m in metrics:
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

Reading a metric. Suppose we are interested in the `files_total` and
`dfs_capacity_used` metrics, over the last 30 minutes.

{% highlight python %}
import time
import datetime

from_time = datetime.datetime.fromtimestamp(time.time() - 1800)
to_time = datetime.datetime.fromtimestamp(time.time())
query = "select files_total, dfs_capacity_used " \
        "where serviceName = HDFS-1 " \
        "  and category = SERVICE"

result = api.query_timeseries(query, from_time, to_time)
ts_list = result[0]
for ts in ts_list.timeSeries:
  print "--- %s: %s ---" % (ts.metadata.entityName, ts.metadata.metricName)
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
[tsquery](http://tiny.cloudera.com/tsquery_doc)) to get metric data points.
Even though the example is querying HDFS metrics, the processing logic is the
same for all queries.

The old-style `.../metrics` endpoints (which exists under host, service
and role objects) are mostly useful for exploring what metrics are available.


Service Lifecycle and Commands
------------------------------

Restart HDFS. Start and stop work similarly:

{% highlight python %}
cmd = hdfs.restart()
print cmd.active
## -- Output --
# True

cmd = cmd.wait()
print "Active: %s. Success: %s" % (cmd.active, cmd.success)
## -- Output --
# Active: False. Success: True
{% endhighlight %}

You can also chain the `wait()` call:

{% highlight python %}
cmd = hdfs.restart().wait()
{% endhighlight %}

Restart the NameNode. Commands on roles are issued at the service level,
and may be done in bulk.

{% highlight python %}
cmds = hdfs.restart_roles(nn.name)
for cmd in cmds:
  print cmd

## -- Output --
<ApiCommand>: 'Restart' (id: 225; active: True; success: None)
{% endhighlight %}
