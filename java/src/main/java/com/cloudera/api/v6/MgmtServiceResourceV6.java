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

import com.cloudera.api.model.ApiService;
import com.cloudera.api.v3.MgmtServiceResourceV3;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface MgmtServiceResourceV6 extends MgmtServiceResourceV3 {

  /**
   * Delete the Cloudera Management Services.
   * <p>
   * This method will fail if a CMS instance doesn't already exist.
   *
   * @return ApiService The deleted CMS information.
   */
  @DELETE
  @Path("/")
  public ApiService deleteCMS();

  /**
   * Automatically assign roles to hosts and create the roles for the Cloudera Management Service.
   * <p>
   * Assignments are done based on number of hosts in the deployment and hardware specifications.
   * If no hosts are part of the deployment, an exception will be thrown preventing any role assignments.
   * Existing roles will be taken into account and their assignments will be not be modified.
   * The deployment should not have any clusters when calling this endpoint. If it does,
   * an exception will be thrown preventing any role assignments.
   * <p>
   * Available since API v6.
   */
  @PUT
  @Path("/autoAssignRoles")
  public void autoAssignRoles();

  /**
   * Automatically configures roles of the Cloudera Management Service.
   * <p>
   * Overwrites some existing configurations.
   * Only default role config groups must exist before calling this endpoint.
   * Other role config groups must not exist. If they do, an exception will be thrown
   * preventing any configuration.
   * Ignores any clusters (and their services and roles) colocated with the Cloudera
   * Management Service. To avoid over-committing the heap on hosts, place the
   * Cloudera Management Service roles on machines not used by any of the clusters.
   * <p>
   * Available since API v6.
   */
  @PUT
  @Path("/autoConfigure")
  public void autoConfigure();
}
