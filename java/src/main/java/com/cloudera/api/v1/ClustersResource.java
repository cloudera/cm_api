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
package com.cloudera.api.v1;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiCluster;
import com.cloudera.api.model.ApiClusterList;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiCommandList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static com.cloudera.api.Parameters.CLUSTER_NAME;
import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;


@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResource {

  /**
   * Creates a collection of clusters.
   *
   * @param clusters List of clusters to created.
   * @return List of created clusters.
   */
  @POST
  @Path("/")
  public ApiClusterList createClusters(ApiClusterList clusters);

  /**
   * Deletes a cluster.
   *
   * @param clusterName Name of cluster to delete.
   * @return Details of deleted cluster.
   */
  @DELETE
  @Path("/{clusterName}")
  public ApiCluster deleteCluster(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Lists all known clusters.
   *
   * @return List of known clusters.
   */
  @GET
  @Path("/")
  public ApiClusterList readClusters(@DefaultValue(DATA_VIEW_DEFAULT)
                                     @QueryParam(DATA_VIEW) DataView dataView);

  /**
   * Reads information about a cluster.
   *
   * @param clusterName Name of cluster to look up.
   * @return Details of requested cluster.
   */
  @GET
  @Path("/{clusterName}")
  public ApiCluster readCluster(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * @return The services resource handler.
   */
  @Path("/{clusterName}/services")
  public ServicesResource getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * List active cluster commands.
   *
   * @param clusterName The name of the cluster.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return A list of active cluster commands.
   */
  @GET
  @Path("/{clusterName}/commands")
  public ApiCommandList listActiveCommands(
      @PathParam(CLUSTER_NAME) String clusterName,
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Upgrades the services in the cluster to the CDH4 version.
   * <p>
   * This command requires that the CDH packages in the hosts used by the
   * cluster be upgraded to CDH4 before this command is issued. Once issued,
   * this command will stop all running services before proceeding.
   * <p>
   * If parcels are used instead of CDH system packages then the following
   * steps need to happen in order:
   * <ol>
   *   <li>Stop all services manually</li>
   *   <li>Activate parcel</li>
   *   <li>Run this upgrade command</li>
   * </ol>
   * The command will upgrade the services and their configuration to the
   * version available in the CDH4 distribution.
   *
   * @param clusterName The name of the cluster.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/upgradeServices")
  public ApiCommand upgradeServicesCommand(
    @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Start all services in the cluster.
   * <p>
   * Services are started in the appropriate order given their dependencies.
   *
   * @param clusterName The name of the cluster.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/start")
  public ApiCommand startCommand(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Stop all services in the cluster.
   * <p>
   * Services are stopped in the appropriate order given their dependencies.
   *
   * @param clusterName The name of the cluster.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/stop")
  public ApiCommand stopCommand(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Restart all services in the cluster.
   * <p>
   * Services are restarted in the appropriate order given their dependencies.
   *
   * @param clusterName The name of the cluster.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/restart")
  public ApiCommand restartCommand(
      @PathParam(CLUSTER_NAME) String clusterName);
}
