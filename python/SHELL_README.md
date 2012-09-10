Cloudera Manager Python Shell
============================


Getting Started
---------------

### Installation ###

> Run as a privileged user, or in a virtualenv

    $ python setup.py install

### Usage ###

    $ cmps
    usage: cmps [-h] -H HOSTNAME [-p PORT] [-u USERNAME] [-c CLUSTER]
           [--password PASSWORD] [-e EXECUTE] [-s SEPERATOR]
    cmps: error: argument -H/--host/--hostname is required

### Login ###

    $ cmps -H <host>
    Enter Username: admin
    Enter Password: 
    Welcome to the Cloudera Manager Console
    Select a cluster using 'show clusters' and 'use'
    cloudera> 

### Using Help ###

    cloudera> help
    Cloudera Manager Commands
    =========================
    log              show           status        stop_role   
    restart_role     start_cluster  stderr        stop_service
    restart_service  start_role     stdout        use         
    roles            start_service  stop_cluster  version     

    Other Commands
    ==============
    help

    cloudera> help stop_cluster
        Completely stop the cluster
        Usage:
            > stop_cluster <cluster>

### Connecting to a Cluster ###

> Autocomplete works

    cloudera> use cdh4
    Connected to cdh4
    cdh4> status
    +------------+-----------+---------+--------+------------+
    | NAME       | SERVICE   |  STATUS | HEALTH |   CONFIG   |
    +------------+-----------+---------+--------+------------+
    | hbase1     | HBASE     | STARTED |  GOOD  | UP TO DATE |
    | hdfs1      | HDFS      | STARTED |  GOOD  | UP TO DATE |
    | mapreduce1 | MAPREDUCE | STARTED |  GOOD  | UP TO DATE |
    | zookeeper1 | ZOOKEEPER | STARTED |  GOOD  | UP TO DATE |
    +------------+-----------+---------+--------+------------+

### View Roles ###

    cdh4> roles hbase1
    +--------------+---------------------+-----------------------+---------+--------+------------+
    | ROLE TYPE    | HOST                | ROLE NAME             |  STATE  | HEALTH |   CONFIG   |
    +--------------+---------------------+-----------------------+---------+--------+------------+
    | MASTER       | hbase.localdomain   | hbase1-MASTER-1       | STARTED |  GOOD  | UP TO DATE |
    | REGIONSERVER | hbase-2.localdomain | hbase1-REGIONSERVER-2 | STARTED |  GOOD  | UP TO DATE |
    | REGIONSERVER | hbase.localdomain   | hbase1-REGIONSERVER-1 | STARTED |  GOOD  | UP TO DATE |
    +--------------+---------------------+-----------------------+---------+--------+------------+

### Stopping / Starting Services and Roles ###
    
    cdh4> restart_service hbase1
    hbase1 is being restarted
    cdh4> status hbase1
    status hbase1
    +--------+---------+----------+--------+------------+
    | NAME   | SERVICE |  STATUS  | HEALTH |   CONFIG   |
    +--------+---------+----------+--------+------------+
    | hbase1 | HBASE   | STARTING |  GOOD  | UP TO DATE |
    +--------+---------+----------+--------+------------+

    cdh4> stop_role hbase1-REGIONSERVER-2
    Stopping Role
    cdh4> roles hbase1
    roles hbase1
    +--------------+---------------------+-----------------------+---------+--------+------------+
    | ROLE TYPE    | HOST                | ROLE NAME             |  STATE  | HEALTH |   CONFIG   |
    +--------------+---------------------+-----------------------+---------+--------+------------+
    | MASTER       | hbase.localdomain   | hbase1-MASTER-1       | STARTED |  GOOD  | UP TO DATE |
    | REGIONSERVER | hbase-2.localdomain | hbase1-REGIONSERVER-2 | STOPPED |  GOOD  | UP TO DATE |
    | REGIONSERVER | hbase.localdomain   | hbase1-REGIONSERVER-1 | STARTED |  GOOD  | UP TO DATE |
    +--------------+---------------------+-----------------------+---------+--------+------------+

### Viewing Logs ###

> Interactive shells will use less

> Non-interactive shells will send to stdout
    
    cdh4> log hbase1-REGIONSERVER-2
    cdh4> stdout hbase1-REGIONSERVER-2
    cdh4> stderr hbase1-REGIONSERVER-2

### Non-Interactive ###

    $ cmps -H 192.168.2.105 -u admin --password admin -e "show hosts; show clusters"
    +---------------------+---------------+----------+
    | HOSTNAME            | IP ADDRESS    | RACK     |
    +---------------------+---------------+----------+
    | hbase.localdomain   | 192.168.2.105 | /default |
    | hbase-2.localdomain | 192.168.2.110 | /default |
    +---------------------+---------------+----------+
    +--------------+
    | CLUSTER NAME |
    +--------------+
    |     cdh4     |
    +--------------+ 

### Custom Output Delimiter ###

    $ cmps -H 192.168.2.105 -u admin --password admin -e "roles hbase1" -c cdh4 -s ,
    ROLE TYPE,HOST,ROLE NAME,STATE,HEALTH,CONFIG
    MASTER,hbase.localdomain,hbase1-MASTER-1,STARTED,GOOD,UP TO DATE
    REGIONSERVER,hbase-2.localdomain,hbase1-REGIONSERVER-2,STARTED,GOOD,UP TO DATE
    REGIONSERVER,hbase.localdomain,hbase1-REGIONSERVER-1,STARTED,GOOD,UP TO DATE

### Scripting Example ###

> Obtain log files for all the region servers

    $ for i in $(cmps -H 192.168.2.105 -u admin --password admin -e "roles hbase1" -c cdh4 -s , | grep REGIONSERVER | awk -F, '{print $3}');  
    do 
        cmps -H 192.168.2.105 -u admin --password admin -c cdh4 -e "log $i" > $i.out;
    done
    $ du -h *.out
    2.4M    hbase1-REGIONSERVER-1.out
    1.9M    hbase1-REGIONSERVER-2.out
