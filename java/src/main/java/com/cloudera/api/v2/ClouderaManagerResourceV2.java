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

import com.cloudera.api.DataView;
import com.cloudera.api.Parameters;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiDeployment;
import com.cloudera.api.model.ApiHostNameList;
import com.cloudera.api.v1.ClouderaManagerResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface ClouderaManagerResourceV2 extends ClouderaManagerResource {

  /**
   * Retrieve full description of the entire Cloudera Manager deployment
   * including all hosts, clusters, services, roles, users, settings, etc.
   * <p/>
   * This object can be used to reconstruct your entire deployment
   * <p/>
   * Note: Only users with sufficient privileges are allowed to call this.
   * <ul>
   * <li>Full Administrators</li>
   * <li>Cluster Administrators (but Navigator config will be redacted)</li>
   * </ul>
   * <p/>
   * Note: starting with v3, the deployment information contais data about
   * Cloudera Manager peers configured for the instance. This data contains
   * plain text authentication information used to connect to the remote peer.
   *
   * @param dataView May be one of "export" (default) or "export_redacted".  The latter
   * replaces configurations that are sensitive with the word "REDACTED".
   * @return A complete deployment description
   */
  @GET
  @Path("/deployment")
  public ApiDeployment getDeployment(
      @DefaultValue(Parameters.DATA_VIEW_EXPORT)
      @QueryParam(Parameters.DATA_VIEW) DataView dataView);

  /**
   * Apply the supplied deployment description to the system. This will
   * create the clusters, services, hosts and other objects specified in
   * the argument. This call does not allow for any merge conflicts. If
   * an entity already exists in the system, this call will fail.
   * You can request, however, that all entities in the system are deleted
   * before instantiating the new ones.
   * <p/>
   * You may specify a complete or partial deployment, e.g. you can provide
   * host info with no clusters.  However, if you request that
   * the current deployment be deleted, you are required to specify at
   * least one admin user or this call will fail. This is to protect
   * you from creating a system that cannot be logged into again.
   * <p/>
   * If there are any errors creating (or optionally deleting) a deployment,
   * all changes will be rolled back leaving the system exactly as it was
   * before calling this method.  The system will never be left in a state
   * where part of the deployment is created and other parts are not.
   * <p/>
   * If the submitted deployment contains entities that require Cloudera
   * Enterprise license, then the license should be provided to Cloudera Manager
   * before making this API call.
   *
   * @param deployment              The deployment to create
   * @param deleteCurrentDeployment If true, the current deployment is deleted
   *                                before the specified deployment is applied
   * @return The system deployment info after successfully applying the given deployment.
   */
  @PUT
  @Path("/deployment")
  public ApiDeployment updateDeployment(ApiDeployment deployment,
                                        @DefaultValue("false")
                                        @QueryParam("deleteCurrentDeployment")
                                        Boolean deleteCurrentDeployment);

  /**
   * Decommission the given hosts.
   * All slave roles on the hosts will be decommissioned.
   * All other roles will be stopped.
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/commands/hostsDecommission")
  public ApiCommand hostsDecommissionCommand(
      ApiHostNameList hostNameList);

  /**
   * Recommission the given hosts.
   * All slave roles on the hosts will be recommissioned.
   * Roles are not started after this command. Use hostsStartRoles command
   * for that.
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/commands/hostsRecommission")
  public ApiCommand hostsRecommissionCommand(
      ApiHostNameList hostNameList);

  /**
   * Start all the roles on the given hosts.
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/commands/hostsStartRoles")
  public ApiCommand hostsStartRolesCommand(
      ApiHostNameList hostNameList);

  /**
   * Return the Cloudera Management Services resource.
   * <p/>
   *
   * @return The Cloudera Management Services resource.
   */
  @Path("/service")
  public MgmtServiceResourceV2 getMgmtServiceResource();
}
