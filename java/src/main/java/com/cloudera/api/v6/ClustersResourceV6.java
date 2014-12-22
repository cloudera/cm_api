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

import com.cloudera.api.model.ApiCdhUpgradeArgs;
import com.cloudera.api.model.ApiCluster;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiRestartClusterArgs;
import com.cloudera.api.model.ApiServiceTypeList;
import com.cloudera.api.v5.ClustersResourceV5;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
   * Updates all refreshable configuration files for services with
   * Dynamic Resource Pools.
   * <p>
   * Available since API v6.
   *
   * @param clusterName The name of the cluster
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{clusterName}/commands/poolsRefresh")
  public ApiCommand poolsRefresh(
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

  /**
   * Update an existing cluster.
   * <p>
   * To update the CDH version, provide the new value in the "fullVersion"
   * property. Setting a correct version that matches the actual installed software
   * will ensure the correct version-specific features, such as services, roles,
   * commands, and configurations. This need not be manually set for clusters
   * using parcels. In general this action is only necessary after the CDH
   * packages have been manually updated. Note that a downgrade may be rejected
   * if it would render existing services or roles unusable. For major upgrade,
   * the "upgradeService" cluster command should be used instead.</p>
   * <p>
   * To rename the cluster, provide the new name in the "displayName"
   * property for API >= 6, or in the "name" property for API <=5.
   * <p>
   * Available since API v2.
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
   * Automatically assign roles to hosts and create the roles for all the services in a cluster.
   * <p>
   * Assignments are done based on services and hosts in the cluster, and hardware specifications.
   * If no hosts are added to the cluster, an exception will be thrown preventing any
   * role assignments.
   * Existing roles will be taken into account and their assignments will be
   * not be modified.
   * <p>
   * Available since API v6.
   *
   * @param clusterName The name of the cluster.
   */
  @PUT
  @Path("/{clusterName}/autoAssignRoles")
  public void autoAssignRoles(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Automatically configures roles and services in a cluster.
   * <p>
   * Overwrites some existing configurations. Might create new role config groups.
   * Only default role config groups must exist before calling this endpoint.
   * Other role config groups must not exist. If they do, an exception will be thrown
   * preventing any configuration.
   * Ignores the Cloudera Management Service even if colocated with roles of this
   * cluster. To avoid over-committing the heap on hosts, assign hosts to this cluster
   * that are not being used by the Cloudera Management Service.
   * <p>
   * Available since API v6.
   *
   * @param clusterName The name of the cluster.
   */
  @PUT
  @Path("/{clusterName}/autoConfigure")
  public void autoConfigure(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Perform CDH upgrade to the next major version.
   * <p>
   * If using packages, CDH packages on all hosts of the cluster must be
   * manually upgraded before this command is issued.
   * <p>
   * The command will upgrade the services and their configuration to the
   * version available in the CDH5 distribution. All running services will
   * be stopped before proceeding.
   * <p>
   * Available since v6.
   *
   * @param clusterName The name of the cluster.
   * @param args Arguments for the command. See {@link ApiCdhUpgradeArgs}.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/upgradeCdh")
  public ApiCommand upgradeCdhCommand(
    @PathParam(CLUSTER_NAME) String clusterName,
    ApiCdhUpgradeArgs args);
}
