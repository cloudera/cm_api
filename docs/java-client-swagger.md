---
layout: default
title: Java Client (Swagger)
id: java-client-swagger
permalink: /docs/java-client-swagger/
---

#### Table of Contents ####

* toc
{:toc}


Setting Up
==========
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
Here is the latest [SDK doc](https://archive.cloudera.com/cm6/{{site.latest_cm_version}}/generic/jar/cm_api/swagger-html-sdk-docs/java/README.html),
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

