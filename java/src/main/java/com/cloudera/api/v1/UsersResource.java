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
import com.cloudera.api.Enterprise;
import com.cloudera.api.model.ApiUser;
import com.cloudera.api.model.ApiUserList;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.USER_NAME;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface UsersResource {

  /**
   * Creates a list of users.
   * <p>
   * When creating new users, the <i>password</i> property of each user should
   * be their plain text password. The returned user information will not
   * contain any password information.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param users List of users to create.
   * @return Information about created users.
   */
  @Enterprise
  @POST
  @Path("/")
  public ApiUserList createUsers(ApiUserList users);

  /**
   * Deletes a user from the system.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param userName The name of the user to delete.
   * @return The details of the deleted user.
   */
  @Enterprise
  @DELETE
  @Path("/{userName}")
  @PermitAll
  public ApiUser deleteUser(@PathParam(USER_NAME) String userName);

  /**
   * Returns detailed information about a user.
   *
   * @param userName The user to read.
   * @return The user's information.
   */
  @GET
  @Path("/{userName}")
  public ApiUser readUser(@PathParam(USER_NAME) String userName);

  /**
   * Returns a list of the user names configured in the system.
   *
   * @return A list of users.
   */
  @GET
  @Path("/")
  public ApiUserList readUsers(
      @DefaultValue(DATA_VIEW_DEFAULT) @QueryParam(DATA_VIEW) DataView dataView);

  /**
   * Updates the given user's information. Note that the user's name cannot
   * be changed.
   *
   * @param userName User name being updated.
   * @param user The user information.
   */
  @PUT
  @Path("/{userName}")
  @PermitAll
  public ApiUser updateUser(@PathParam(USER_NAME) String userName,
      ApiUser user);

}
