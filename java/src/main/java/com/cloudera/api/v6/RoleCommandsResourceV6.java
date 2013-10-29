// Copyright (c) 2014 Cloudera, Inc. All rights reserved.
package com.cloudera.api.v6;

import static com.cloudera.api.Parameters.*;

import com.cloudera.api.model.ApiBulkCommandList;
import com.cloudera.api.model.ApiRoleNameList;
import com.cloudera.api.v4.RoleCommandsResourceV4;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RoleCommandsResourceV6 extends RoleCommandsResourceV4 {

  /**
   * Execute a role command by name.
   * <p/>
   * Available since API v6.
   *
   * @param commandName the name of command to execute.
   * @param roleNames the roles to run this command on.
   * @return List of submitted commands.
   */
  @POST
  @Path("/{commandName}")
  public ApiBulkCommandList roleCommandByName(@PathParam(COMMAND_NAME) String commandName,
                                              ApiRoleNameList roleNames);
}
