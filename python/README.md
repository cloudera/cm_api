Welcome to Cloudera Manager API Client!

Python Client
=============
The python source is in the `python` directory. The Python client comes with a 
`cm_api` Python client module, and examples on performing certain Hadoop cluster 
administrative tasks using the Python client.

Getting Started
---------------
Here is a short snippet on using the `cm_api` Python client:

    Python 2.7.2+ (default, Oct  4 2011, 20:06:09) 
    [GCC 4.6.1] on linux2
    Type "help", "copyright", "credits" or "license" for more information.
    >>> from cm_api.api_client import ApiResource
    >>> api = ApiResource('rhel62-1.ent.cloudera.com', 7180, 'admin', 'admin')
    >>> for h in api.get_all_hosts():
    ...   print h.hostname
    ... 
    rhel62-2.ent.cloudera.com
    rhel62-4.ent.cloudera.com
    rhel62-3.ent.cloudera.com
    rhel62-1.ent.cloudera.com
    >>> 

Another example: getting all the services in a cluster:

    >>> for c in api.get_all_clusters():
    ...   print c.name
    ... 
    Cluster 1 - CDH4
    >>> for s in api.get_cluster('Cluster 1 - CDH4').get_all_services():
    ...  print s.name
    ... 
    hdfs1
    mapreduce1
    zookeeper1
    hbase1
    oozie1
    yarn1
    hue1
    >>> 

Shell
-----
After installing the `cm_api` Python package, you can use the API shell `cmps`
(CM Python Shell):

    $ cmps -H <host> --user admin --password admin
    Welcome to the Cloudera Manager Console
    Select a cluster using 'show clusters' and 'use'
    cloudera> show clusters
    +------------------+
    |   CLUSTER NAME   |
    +------------------+
    | Cluster 1 - CDH4 |
    | Cluster 2 - CDH3 |
    +------------------+
    cloudera> 

Please see the `SHELL_README.md` file for more.

Example Scripts
---------------
You can find example scripts in the `python/examples` directory.

* `bulk_config_update.py` ---
  Useful for heterogenous hardware environment. It sets the configuration on
  the roles running on a given set of hosts.
