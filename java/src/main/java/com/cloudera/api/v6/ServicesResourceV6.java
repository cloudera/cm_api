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

import static com.cloudera.api.Parameters.SERVICE_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v4.ServicesResourceV4;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV6 extends ServicesResourceV4 {

  @Override
  public RolesResourceV6 getRolesResource(String serviceName);

  /**
   * Available since API v6. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @return The snapshots resource handler.
   */
  @Path("/{serviceName}/snapshots")
  public SnapshotsResource getSnapshotsResource(
      @PathParam(SERVICE_NAME) String serviceName);
  
  /**
   * @return The YARN applications resource handler.
   */
  @Path("/{serviceName}/yarnApplications")
  public YarnApplicationsResource getYarnApplicationsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Import MapReduce configuration into Yarn, overwriting Yarn configuration.
   * <p>
   * You will lose existing Yarn configuration. Read all MapReduce
   * configuration, role assignments, and role configuration groups and update
   * Yarn with corresponding values. MR1 configuration will be converted into
   * the equivalent MR2 configuration.
   * <p>
   * Before running this command, Yarn must be stopped and MapReduce must exist
   * with valid configuration.
   * <p>
   * Available since API v6.
   * 
   * @param serviceName
   *          Name of the Yarn service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Path("/{serviceName}/commands/importMrConfigsIntoYarn")
  public ApiCommand importMrConfigsIntoYarn(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Change the cluster to use MR2 instead of MR1. Services will be restarted.
   * <p>
   * Will perform the following steps:
   * <ul>
   * <li>Update all services that depend on MapReduce to instead depend on Yarn.
   * </li>
   * <li>Stop MapReduce</li>
   * <li>Start Yarn (includes MR2)</li>
   * <li>Deploy Yarn (MR2) Client Configuration</li>
   * </ul>
   * <p>
   * Available since API v6.
   * 
   * @param serviceName
   *          Name of the Yarn service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Path("/{serviceName}/commands/switchToMr2")
  public ApiCommand switchToMr2(
      @PathParam(SERVICE_NAME) String serviceName);
}
