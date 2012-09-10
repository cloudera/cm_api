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

package com.cloudera.api.v2;

import com.cloudera.api.model.ApiCluster;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v1.ClustersResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import static com.cloudera.api.Parameters.CLUSTER_NAME;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV2 extends ClustersResource {

  /**
   * @return The services resource handler.
   */
  @Path("/{clusterName}/services")
  public ServicesResourceV2 getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * <p>Update an existing cluster.</p>
   *
   * <p>You may not change a cluster's CDH version using this API call.
   * Use the "upgradeService" cluster command instead.</p>
   *
   * <p>To rename the cluster, pass in a json structure that has the new
   * cluster name.</p>
   *
   * <p>Available since API v2.</p>
   *
   * @param clusterName The name of the cluster.
   * @return Details of the affected cluster.
   */
  @PUT
  @Path("/{clusterName}")
  public ApiCluster updateCluster(
      @PathParam(CLUSTER_NAME) String clusterName,
      ApiCluster cluster);

  /**
   * Put the cluster into maintenance mode. This is a synchronous command.
   * The result is known immediately upon return.
   *
   * <p>Available since API v2.</p>
   *
   * @param clusterName The name of the cluster
   * @return Synchronous command result.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/enterMaintenanceMode")
  public ApiCommand enterMaintenanceMode(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Take the cluster out of maintenance mode. This is a synchronous command.
   * The result is known immediately upon return.
   *
   * <p>Available since API v2.</p>
   *
   * @param clusterName The name of the cluster
   * @return Synchronous command result.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/exitMaintenanceMode")
  public ApiCommand exitMaintenanceMode(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Deploy the cluster-wide client configuration.
   *
   * <p>For each service in the cluster, deploy the service's client configuration
   * to all the hosts that the service runs on.</p>
   *
   * <p>Available since API v2.</p>
   *
   * @param clusterName The name of the cluster
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/deployClientConfig")
  public ApiCommand deployClientConfig(
      @PathParam(CLUSTER_NAME) String clusterName);
}
