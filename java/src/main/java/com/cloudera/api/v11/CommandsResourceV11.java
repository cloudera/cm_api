// Copyright (c) 2015 Cloudera, Inc. All rights reserved.
package com.cloudera.api.v11;

import static com.cloudera.api.Parameters.COMMAND_ID;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v1.CommandsResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface CommandsResourceV11 extends CommandsResource {

  /**
   * Try to rerun a command.
   *
   * @param commandId ID of the command that needs to be run.
   * @return Command that represents the retry attempt.
   */
  @Consumes()
  @POST
  @Path("/{commandId}/retry")
  public ApiCommand retry(@PathParam(COMMAND_ID) long commandId);

}
