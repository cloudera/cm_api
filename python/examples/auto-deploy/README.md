Cloudera Automatic Provisioning
===============================

These scripts deploy a Cloudera cluster, consisting of CDH and Cloudera Manager (CM). The cluster runs all of the available services in CDH, as well as the CM management services. You can use this as is to deploy a cluster or use it as a starting point to develop a more customized version that doesn't deploy all services or that uses different configurations.

Some limitations are as follows:

* It does not automatically validate the installation.
* The configurations used have not been extensively tested. So while the cluster comes up with all greens from a health check perspective, you should still validate that everything works.
* Not all management services are turned on, as some (ie Navigator) require a Cloudera Enterprise subscription.
* Accumulo is currently not installed.
* It currently works with CDH5.0. It can be modified to support CDH4.x, but it won't work without some modifications.

Prerequisites:
--------------
- http://www.cloudera.com/content/cloudera-content/cloudera-docs/CM5/latest/Cloudera-Manager-Installation-Guide/cm5ig_cm_requirements.html


Assumptions:
------------
- Running on a RHEL 6.x machine
- All yum repos are set up. This is for common things like MySQL, expect, etc. A Cloudera repo is downloaded for Cloudera specific packages.
- The clouderaconfig.ini file must contain settings applicable to a given environment (eg the proper hostnames, the ntp server, etc). 
- The cdh.parcel.version property in clouderaconfig.ini should match a version that is available in http://archive.cloudera.com/cdh5/parcels/latest/ or in a customer parcel repo that you set up via the remote.parcel.repo.urls property. You can use 'latest' for the cdh.parcel.version property to make it automatically determine the parcel version to use from the latest avaiable versions in the Cloudera parcel repo.


Steps To Deploy a Cluster:
--------------------------

1. Ensure that clouderaconfig.ini is updated with the hostnames for the cluster, the root data directory (note that the default is to have one data directory, which is not recommended for production use), etc.

2. Execute the following scripts at the same time. There are sleeps at the appropriate points to account for temporal dependencies amongst the slave and master processes. Something like Ansible could of course be used to do this in a less hackish way.
    * sudo ./setup-slave.sh #run on all of the slave nodes (ie every node other than the master).
    * sudo ./setup-master.sh #run on the CM master node.

3. Go to http://$CM_MASTER_HOSTNAME:7180/cmf and log in (see the clouderaconfig.ini file for the credentials) to view the cluster.

Tests to Validate the Installation:
-----------------------------------

```
#Run these on one of the datanodes in the cluster

#Create hive table
hive shell
hive> CREATE TABLE justin(id INT, name STRING) ROW FORMAT DELIMITED FIELDS TERMINATED BY ' ' STORED AS TEXTFILE;
hive> exit;

#Create HDFS file
echo "1 justin" > /tmp/test
sudo -u hdfs hadoop fs -put /tmp/test /user/hive/warehouse/justin/

#Query hive table
hive shell
hive> select * from justin where id=1;
hive> exit;

#Query same table but in impala
impala-shell
impala> invalidate metadata;
impala> select * from justin;
impala> exit;

#Run mapreduce job
hadoop jar /opt/cloudera/parcels/CDH/lib/hadoop-0.20-mapreduce/hadoop-examples.jar pi 10 10

#Test hbase
hbase shell
hbase> create 'test', 'cf'
hbase> list 'test'
hbase> put 'test', 'row1', 'cf:a', 'value1'
hbase> scan 'test'
hbase> exit

#Test spark - replace $NAMENODE with the correct hostname that's running the NN
echo "this is the end. the only end. my friend." > /tmp/sparkin
hadoop fs -put /tmp/sparkin /tmp/
spark-shell
scala> val file = sc.textFile("hdfs://$NAMENODE:8020/tmp/sparkin")
scala> val counts = file.flatMap(line => line.split(" ")).map(word => (word, 1)).reduceByKey(_ + _)
scala> counts.saveAsTextFile("hdfs://$NAMENODE:8020/tmp/sparkout")
scala> exit
hadoop fs -cat /tmp/sparkout
```
