---
layout: default
title: CM Concepts
id: cm-concepts
permalink: /docs/cm-concepts/
---

#### Table of Contents ####

* toc
{:toc}

To effectively use the API, you need to understand the basic CM concepts, as
well as how CM models a Hadoop cluster.

Cloudera Manager Concepts
=========================

Deployment
----------
A configuration of CM and all the services and hosts it manages.
Deployment information is available under the `/cm/deployment` resource.


Cluster
-------
A cluster is a set of hosts running inter-dependent services. All hosts in a
cluster have the same CDH version. A CM installation may have
multiple clusters, which are uniquely identified by different names.

You can issue commands against a cluster.

Host
----
A logical or physical machine that runs a CM agent.
You can assign role instances to hosts.
A host may belong to only one cluster. A host may also not be part of any
cluster, in which case it will not run any service roles.

Service
-------
A service is an abstract entity providing a capability in a cluster. For example:
HDFS, MapReduce, YARN, and HBase. A service is usually distributed,
and contains a set of roles that physically run on the cluster. A service has
its own configuration, status, metrics, and roles. You may issue commands
against a service, or against a set of roles in bulk. Additionally, an HDFS
service has nameservices, and a MapReduce service has activities.

All services belong to a cluster (except for the Cloudera Management Service),
and is uniquely identified by its name, which is different from the mutable
"display name", within a CM installation.
The types of services available depends on the CDH version of the cluster.

Role
----
A role performs specific actions for a service, and is assigned to a host. It
usually runs as a daemon process, such as a DataNode or a TaskTracker. (There
are exceptions -- not all roles are daemon processes.) Once created, a role cannot
be reassigned to a different host. You need to delete and re-create it.

A role is identified by its role name, which cannot be changed after creation.
It has its own configuration, status and metrics. API commands on roles are
always issued in bulk at the service level.

Role Type
---------
Role type refers to the class that a role belongs to. For example, an HBase
service has the Master role type and the RegionServer role type. Different
service types have different sets of role types. This is not to be confused with
a role, which refers to a specific role instance that is physically assigned to
a host.

Role Configuration Group
------------------------
Also called "Role Group", it is a grouping of roles (of the same
time) for configuration purpose. When you update the configuration of a role
group, all the members of that group get the new configuration.

For example, suppose a group called _Datanode-beefy-group_ has 2 members:
_DN-host1_ and _DN-host2_. If you change the heap size on the group, both member
DataNodes will inherit that new heap size.

The main purpose of role configuration group is to handle heterogeneous hardware.

Host Template
-------------
A set of role configuration groups. When a template is applied to a host, a role
instance from each role group is created and assigned to that host.

Cloudera Manager
----------------
Everything related to the operation of Cloudera Manager itself is available
under the `/cm` resource. This includes system configuration, global commands,
and the Management Service.

Management Service
------------------
The Management Service provides monitoring, diagnostic, reporting and auditing
features for your Hadoop clusters. (Some are only available with the appropriate
licenses.) The operation of this service is similar to other Hadoop services,
except that the Management Service does not belong to a cluster.


Architecture Overview
=====================
Also see the [Cloudera Manager
Primer](http://www.cloudera.com/content/cloudera-content/cloudera-docs/CM4Ent/latest/Cloudera-Manager-Introduction/cmi_primer.html)
for an overview on CM's architecture.
