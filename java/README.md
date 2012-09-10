Cloudera Manager RESTful API Java Client
========================================

The project provides all the source you need to build a Java client for the [Cloudera Manager]((http://www.cloudera.com/products-services/tools/) RESTful API.

The Cloudera Manager RESTful API is implemented with [Apache CXF](http://cxf.apache.org/)
using [JAX-RS](http://en.wikipedia.org/wiki/Java_API_for_RESTful_Web_Services) and JAX-B.
The Java models and interfaces provided here serve as a contract between the Cloudera 
Manager service and Java client. Cloudera uses this same source internally in the 
server implementation. We're providing this source to make it easier for partners and
the open-source community to easily integrate with Cloudera Manager.

The RESTful API follows standard [Create-Read-Update-Delete (CRUD)](http://en.wikipedia.org/wiki/Create,_read,_update_and_delete) semantics.
Each *resource* in the API is capable of creating, reading, updating or deleting a particular entity in the system. A *resource* can
have subresources e.g. the cluster resource has a service resource.

To create the root resource for the API, use the client builder:
```java
ApiRootResource root = new ClouderaManagerClientBuilder()
    .withHost("myhost.foo.com")
    .withUsernamePassword("admin", "admin")
    .build();
```

The `ApiRootResource` has subresources for each of the API versions that it supports. For example...
```java
RootResourceV1 v1 = root.getRootV1();
RootResourceV2 v2 = root.getRootV2();
```

To list the names of all clusters under management, simply use the cluster resource as follows:
```java
ClustersResourceV2 clustersResource = v2.getClustersResource();
ApiClusterList clusters = clustersResource.readClusters(DataView.FULL);
for (ApiCluster cluster: clusters) {
  System.out.println(cluster.getName());
}
```

We can use the same pattern to print all clusters, services and roles in a nice hierarchy using
the clusters, services and roles resources. For example,
```java
ClustersResourceV2 clustersResource = v2.getClustersResource();
for (ApiCluster cluster : clustersResource.readClusters(DataView.FULL)) {
  System.out.println(cluster.getName());
  ServicesResourceV2 servicesResource = clustersResource.getServicesResource(cluster.getName());
  for (ApiService service : servicesResource.readServices(DataView.FULL)) {
    System.out.println("\t" + service.getName());
    RolesResourceV2 rolesResource = servicesResource.getRolesResource(service.getName());
    for (ApiRole role : rolesResource.readRoles()) {
      System.out.println("\t\t" + role.getName());
    }
  }
}
```

