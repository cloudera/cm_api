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

import com.cloudera.api.Parameters;
import com.cloudera.api.model.ApiCommandMetadataList;
import com.cloudera.api.model.ApiRoleList;
import com.cloudera.api.model.ApiRoleNameList;
import com.cloudera.api.v4.RolesResourceV4;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RolesResourceV6 extends RolesResourceV4 {

  /**
   * Bulk delete roles in a particular service by name. Fails if any role cannot
   * be found.
   *
   * @param roleNames
   *          list of role names to be deleted
   * @return list of role IDs deleted, index-aligned with roleNames
   */
  @POST
  @Path("/bulkDelete")
  public ApiRoleList bulkDeleteRoles(ApiRoleNameList roleNames);

  /**
   * Lists all roles of a given service that match the provided filter.
   *
   * @param filter
   *    Optional query to filter the roles by.
   *    <p>
   *    The query specifies the intersection of a list of constraints,
   *    joined together with semicolons (without spaces). For example:
   *    hostname==host1.abc.com;type==DATANODE
   *    </p>
   *
   *    Currently supports filtering by:
   *    <ul>
   *      <li>hostname: The hostname of the host the role is running on.</li>
   *      <li>hostId: The unique identifier of the host the role is running on.</li>
   *      <li>type: The role's type.</li>
   *    </ul>
   * @return List of roles.
   */
  @GET
  @Path("/")
  public ApiRoleList readRoles(
      @QueryParam(Parameters.FILTER) @DefaultValue(Parameters.FILTER_DEFAULT) String filter);

  /**
   * Lists all the commands that can be executed by name
   * on the provided role.
   *
   * @param roleName the role name.
   * @return a list of command metadata objects.
   */
  @GET
  @Path("/{roleName}/commandsByName")
  public ApiCommandMetadataList listCommands(@PathParam(Parameters.ROLE_NAME) String roleName);
}
