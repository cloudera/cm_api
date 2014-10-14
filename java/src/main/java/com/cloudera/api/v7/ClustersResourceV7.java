// Licensed to Cloudera, Inc. under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  Cloudera, Inc. licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.cloudera.api.v7;

import static com.cloudera.api.Parameters.*;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.InputStreamDataSource;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiHostRefList;
import com.cloudera.api.v6.ClustersResourceV6;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV7 extends ClustersResourceV6 {

  /**
   * @return The services resource handler.
   */
  @Override
  @Path("/{clusterName}/services")
  public ServicesResourceV7 getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Deploy the Cluster's Kerberos client configuration.
   *
   * <p>
   * Deploy krb5.conf to hosts in a cluster. Does not deploy to decommissioned
   * hosts or hosts with active processes.
   * </p>
   *
   * <p>
   * Available since API v7.
   * </p>
   *
   * @param clusterName The name of the cluster
   * @param hosts
   *          Hosts in cluster to deploy to. If empty, will target all eligible
   *          hosts in the cluster.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/deployClusterClientConfig")
  public ApiCommand deployClusterClientConfig(
      @PathParam(CLUSTER_NAME) String clusterName,
      ApiHostRefList hosts);

  /**
   * Download a zip-compressed archive of the client configuration, of a
   * specific cluster. Currently, this only includes Kerberos Client
   * Configuration (krb5.conf). For client configuration of services, use the
   * clientConfig endpoint of the services resource. This resource does not
   * require any authentication.
   *
   * @param clusterName The cluster name.
   * @return The archive data.
   */
  @GET
  @PermitAll
  @Path("/{clusterName}/clientConfig")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public InputStreamDataSource getClientConfig(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Prepare and start services in a cluster.
   *
   * <p>
   * Perform all the steps needed to prepare each service in a cluster and start the services
   * in order.
   * </p>
   *
   * <p>
   * Available since API v7.
   * </p>
   *
   * @param clusterName The name of the cluster.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{clusterName}/commands/firstRun")
  public ApiCommand firstRun(
      @PathParam(CLUSTER_NAME) String clusterName);
}
