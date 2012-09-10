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
import com.cloudera.api.model.ApiCommandList;
import com.cloudera.api.model.ApiConfigList;
import com.cloudera.api.model.ApiRole;
import com.cloudera.api.model.ApiRoleList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.MESSAGE;
import static com.cloudera.api.Parameters.ROLE_NAME;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface MgmtRolesResource {

  /**
   * Create new roles in the Cloudera Management Services.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param roles Roles to create.
   * @return List of created roles.
   */
  @POST
  @Path("/")
  public ApiRoleList createRoles(ApiRoleList roles);

  /**
   * Delete a role from the Cloudera Management Services.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param roleName The role name.
   * @return The details of the deleted role.
   */
  @DELETE
  @Path("/{roleName}")
  public ApiRole deleteRole(
      @PathParam(ROLE_NAME) String roleName);

  /**
   * List all roles of the Cloudera Management Services.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @return List of roles.
   */
  @GET
  @Path("/")
  public ApiRoleList readRoles();

  /**
   * Retrieve detailed information about a Cloudera Management Services role.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param roleName The role name.
   * @return The details of the role.
   */
  @GET
  @Path("/{roleName}")
  public ApiRole readRole(
      @PathParam(ROLE_NAME) String roleName);

  /**
   * Retrieve the configuration of a specific Cloudera Management Services role.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param roleName The role to look up.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List with configured and available configuration options.
   */
  @GET
  @Path("/{roleName}/config")
  public ApiConfigList readRoleConfig(
      @PathParam(ROLE_NAME) String roleName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Update the configuration of a Cloudera Management Services role.
   * <p>
   * If a value is set in the given configuration, it will be added
   * to the role's configuration, replacing any existing entries.
   * If a value is unset (its value is null), the existing
   * configuration for the attribute will be erased, if any.
   * <p>
   * Attributes that are not listed in the input will maintain their
   * current values in the configuration.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param roleName The role to modify.
   * @param message Optional message describing the changes.
   * @param config Configuration changes.
   * @return The new service configuration.
   */
  @PUT
  @Path("/{roleName}/config")
  public ApiConfigList updateRoleConfig(
      @PathParam(ROLE_NAME) String roleName,
      @QueryParam(MESSAGE) String message,
      ApiConfigList config);

  /**
   * List active role commands.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param roleName The role name.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return A list of active role commands.
   */
  @GET
  @Path("/{roleName}/commands")
  public ApiCommandList listActiveCommands(
      @PathParam(ROLE_NAME) String roleName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

}
