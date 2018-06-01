---
layout: default
title: Quick Start
id: quick-start
permalink: /docs/quick-start/
---

<!-- Convenient variable for API url prefix -->
{% capture urlprefix %}http://cm-host:7180/api/v{{ site:latest_api_version }}{% endcapture %}


Server Setup
============
The Cloudera Manager API does not require any setup. Once your Cloudera Manager is installed
and running, you can use the API. The API is served on the same webserver, under
`{{ urlprefix }}/`.

Note that v{{ site:latest_api_version }} is the latest version. You may have an older CM instance. 
Access `http://cm-host:7180/api/version` to find out the highest supported version.


A First Taste of API
====================
An easy way to access the API is curl. This exercises the "echo" debugging endpoint:

    $ curl -X GET -u "admin:admin" -i \
      {{ urlprefix }}/tools/echo?message=hello

- The `-X GET` sends a GET request, which is the default and can be omitted. You would use PUT, POST
  and DELETE for other usage scenarios.

- The `-u "admin:admin"` authenticates with CM using HTTP basic auth. `admin:admin` is the CM
  default user account.

- The `-i` tells curl to include HTTP headers in the output, which is useful for debugging.

To look at a POST example, this defines a new (empty) cluster in CM:

    $ curl -X POST -u "admin:admin" -i \
      -H "content-type:application/json" \
      -d '{ "items": [
              {
                "name": "test",
                "version": "CDH4"
              }
          ] }'  \
      {{ urlprefix }}/clusters

- The `-H "content-type:application/json"` adds an HTTP header to the request, because the request
  data is in JSON. (The CM API uses JSON for data representation.)

- The `-d <data_string>` specifies the data to post. Another option is to do `-d @<filename>`, where
  the data to post is stored in a file.

  The call takes a list of cluster objects. But in this case, it is a list of one
  because we are only defining one cluster. The cluster has 2 mandatory fields: `name` and
  `version`. All the API calls and their arguments are listed in the
  [API Docs](https://archive.cloudera.com/cm{{ site.latest_cm_major_version }}/{{ site.latest_cm_version }}/generic/jar/cm_api/apidocs/index.html).

- You may notice that the API returns a JSON describing the "test" cluster that you just created.
  This is typical of the API.

As you can see, using curl can quickly leads to escaping and quoting problems (and typos), when 
dealing with more complex objects. We strongly recommend you use the Python or Java client for any
serious API development.
