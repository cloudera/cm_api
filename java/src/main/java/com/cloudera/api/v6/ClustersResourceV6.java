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
package com.cloudera.api.v6;

import static com.cloudera.api.Parameters.CLUSTER_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiRestartClusterArgs;
import com.cloudera.api.model.ApiServiceTypeList;
import com.cloudera.api.v5.ClustersResourceV5;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV6 extends ClustersResourceV5 {

  /**
   * @return The services resource handler.
   */
  @Override
  @Path("/{clusterName}/services")
  public ServicesResourceV6 getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Updates all refreshable configuration files in the cluster. Will not
   * restart any roles.
   * <p>
   * Available since API v6.
   *
   * @param clusterName The name of the cluster
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{clusterName}/commands/refresh")
  public ApiCommand refresh(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Restart all services in the cluster.
   * <p>
   * Services are stopped then started in the appropriate order given their
   * dependencies. The command can optionally restart only stale services and
   * their dependencies as well as redeploy client configuration on all services
   * in the cluster.
   *
   * @param clusterName The name of the cluster.
   * @param args arguments for the restart command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{clusterName}/commands/restart")
  public ApiCommand restartCommand(
      @PathParam(CLUSTER_NAME) String clusterName,
      ApiRestartClusterArgs args);

  /**
   * List the supported service types for a cluster.
   *
   * @param clusterName The cluster.
   * @return List of service types the cluster supports.
   */
  @GET
  @Path("/{clusterName}/serviceTypes")
  public ApiServiceTypeList listServiceTypes(
      @PathParam(CLUSTER_NAME) String clusterName);
}
