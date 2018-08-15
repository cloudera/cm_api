---
layout: default
title: Java Client
id: java-client-swagger
permalink: /docs/java-client-swagger/
---

#### Table of Contents ####

* toc
{:toc}


Version
=======
Cloudera Manager(CM) 6.0 introduces new Java API client based
on [Swagger](https://swagger.io/). This new API client supports all CM API versions.

[Older Java client]({{ site.url }}/docs/java-client/) will still be supported for
API version less than 30. So older Java API client can still be used
against Cloudera Manager version 6.0 and later as long as API version 19 or earlier is used.

For e.g. customer can use old CM API client version 5.14 against CM version 6.0
which by default will invoke API version 19. If customer wants to use new features that were
introduced in Cloudera Manager 6.0 i.e. API version 30 like "Fine Grained Access Control" then
customer must use this new API client.

Older Java client and new Swagger based Java client can co-exist in an application
to allow for incremental transition to new Swagger based java client.


Setting Up
==========

TODO: May need update based on RELENG-3378.

Merge the following to your Maven project's `pom.xml`:

    <project>
      <repositories>
        <repository>
          <id>cm.repo</id>
          <url>https://repository.cloudera.com/artifactory/cloudera-repos</url>
          <name>Cloudera Repository</name>
        </repository>
        …
      </repositories>
      <dependencies>
        <dependency>
          <groupId>com.cloudera.api.swagger</groupId>
          <artifactId>cloudera-manager-api-swagger</artifactId>
          <version>{{ site.latest_cm_version }}</version>  <!-- or CM version 6.0 and above -->
        </dependency>
        …
      </dependencies>
      ...
    </project>

You can also directly download the jar(and sources) from [Cloudera repository](https://archive.cloudera.com/cm6/{{site.latest_cm_version}}/generic/jar/cm_api/).

SDK doc
=======
Here is the latest [SDK doc](https://archive.cloudera.com/cm{{ site.latest_cm_major_version }}/{{ site.latest_cm_version }}/generic/jar/cm_api/swagger-html-sdk-docs/java/README.html),
for API version {{ site.latest_api_version }} (CM {{site.latest_cm_version}}).

Basic Usage
===========

{% highlight java %}
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cloudera.api.swagger.ClustersResourceApi;
import com.cloudera.api.swagger.client.ApiClient;
import com.cloudera.api.swagger.client.ApiException;
import com.cloudera.api.swagger.client.Configuration;
import com.cloudera.api.swagger.model.ApiCluster;
import com.cloudera.api.swagger.model.ApiClusterList;

public class ListClusters {

  public static void main(String[] args) throws IOException {
    ApiClient cmClient = Configuration.getDefaultApiClient();

    // Configure HTTP basic authorization: basic
    cmClient.setBasePath("https://cm-host:7183/api/v30");
    cmClient.setUsername("username");
    cmClient.setPassword("password");

    // Configure TLS for secure communication
    cmClient.setVerifyingSsl(true);

    Path truststorePath = Paths.get("/path/to/ca_cert_file.pem");
    byte[] truststoreBytes = Files.readAllBytes(truststorePath);
    cmClient.setSslCaCert(new ByteArrayInputStream(truststoreBytes));

    ClustersResourceApi apiInstance = new ClustersResourceApi(cmClient);
    try {
      ApiClusterList clusterList = apiInstance.readClusters("SUMMARY");
      for (ApiCluster cluster : clusterList.getItems()) {
        System.out.printf("Name: %s, Version: %s", cluster.getDisplayName(),
          cluster.getFullVersion());
      }
    } catch (ApiException e) {
      System.err.println("Exception when calling ClustersResourceApi#readClusters");
      e.printStackTrace();
    }
  }
}

{% endhighlight %}

The above example invokes API over secure HTTPS channel. If TLS is not enabled on
Cloudera Manager Admin Console then update the base path (setBasePath()) to point to right URL
and TLS configuration (setVerifyingSSL(), setSslCaCert()) for API client can be dropped.

