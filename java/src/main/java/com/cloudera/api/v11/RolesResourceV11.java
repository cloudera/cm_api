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
package com.cloudera.api.v11;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.DATA_VIEW_FULL;
import static com.cloudera.api.Parameters.ROLE_NAME;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiRole;
import com.cloudera.api.model.ApiRoleList;
import com.cloudera.api.v8.RolesResourceV8;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RolesResourceV11 extends RolesResourceV8 {

  /**
   * Lists all roles of a given service.
   * @param dataView DataView for getting roles. Defaults to 'summary'.
   * @return List of roles.
   */
  @GET
  @Path("/")
  public ApiRoleList readRoles(
      @DefaultValue(DATA_VIEW_DEFAULT) @QueryParam(DATA_VIEW) DataView dataView);

  /**
   * Retrieves detailed information about a role.
   *
   * @param roleName The role name.
   * @param dataView The view to materialize. Defaults to 'full'.
   * @return The details of the role.
   */
  @GET
  @Path("/{roleName}")
  public ApiRole readRole(
      @PathParam(ROLE_NAME) String roleName,
      @DefaultValue(DATA_VIEW_FULL) @QueryParam(DATA_VIEW) DataView dataView);
}