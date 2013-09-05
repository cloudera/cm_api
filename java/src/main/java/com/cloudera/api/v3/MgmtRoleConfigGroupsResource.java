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
package com.cloudera.api.v3;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.ROLE_CONFIG_GROUP_NAME;

import com.cloudera.api.DataView;
import com.cloudera.api.Parameters;
import com.cloudera.api.model.ApiConfigList;
import com.cloudera.api.model.ApiRoleConfigGroup;
import com.cloudera.api.model.ApiRoleConfigGroupList;
import com.cloudera.api.model.ApiRoleList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface MgmtRoleConfigGroupsResource {

  /**
   * Returns the information for all role config groups in
   * the Cloudera Management Services.
   * <p>
   * Available since API v3.
   *
   * @return The list of role config groups for the given service.
   */  
  @GET
  @Path("/")
  public ApiRoleConfigGroupList readRoleConfigGroups();
  
  /**
   * Returns the information for a given role config group
   * in the Cloudera Management Services.
   * <p>
   * Available since API v3.
   *
   * @param roleConfigGroupName The name of the requested group.
   * @return The requested role config group.
   */
  @GET
  @Path("/{roleConfigGroupName}")
  public ApiRoleConfigGroup readRoleConfigGroup(
      @PathParam(ROLE_CONFIG_GROUP_NAME) String roleConfigGroupName);

  /**
   * Updates an existing role config group in the Cloudera Management Services.
   * <p>
   * Available since API v3.
   *
   * @param roleConfigGroupName The name of the group to update.
   * @param roleConfigGroup The updated role config group.
   * @param message The optional message describing the changes.
   * @return Role updated role config group.
   */
  @PUT
  @Path("/{roleConfigGroupName}")
  public ApiRoleConfigGroup updateRoleConfigGroup(
      @PathParam(ROLE_CONFIG_GROUP_NAME) String roleConfigGroupName,
      ApiRoleConfigGroup roleConfigGroup,
      @QueryParam(Parameters.MESSAGE) String message);

  /**
   * Returns all roles in the given role config group
   * in the Cloudera Management Services.
   * <p>
   * Available since API v3.
   *
   * @param roleConfigGroupName The name of the role config group.
   * @return The roles in the role config group.
   */
  @GET
  @Path("/{roleConfigGroupName}/roles")
  public ApiRoleList readRoles(
      @PathParam(ROLE_CONFIG_GROUP_NAME) String roleConfigGroupName);

  /**
   * Returns the current revision of the config
   * for the specified role config group in the Cloudera Management Services.
   * <p>
   * Available since API v3.
   *
   * @param roleConfigGroupName The name of the role config group.
   * @param dataView The view of the data to materialize,
   *                  either "summary" or "full".
   * @return The current configuration of the role config group.
   */
  @GET
  @Path("/{roleConfigGroupName}/config")
  public ApiConfigList readConfig(
      @PathParam(ROLE_CONFIG_GROUP_NAME) String roleConfigGroupName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Updates the config for the given role config group
   * in the Cloudera Management Services.
   * <p>
   * Available since API v3.
   *
   * @param roleConfigGroupName The name of the role config group.
   * @param message Optional message describing the changes.
   * @param config The new config information for the group.
   * @return The updated config of the role config group.
   */
  @PUT
  @Path("/{roleConfigGroupName}/config")
  public ApiConfigList updateConfig(
      @PathParam(ROLE_CONFIG_GROUP_NAME) String roleConfigGroupName,
      @QueryParam(Parameters.MESSAGE) String message,
      ApiConfigList config);
}
