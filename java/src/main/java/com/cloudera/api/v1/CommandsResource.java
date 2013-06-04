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

import com.cloudera.api.model.ApiCommand;
import static com.cloudera.api.Parameters.COMMAND_ID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface CommandsResource {

  /**
   * Retrieve detailed information on an asynchronous command.
   *
   * <p>Cloudera Manager keeps the results and statuses of asynchronous
   * commands, which have non-negative command IDs. On the other hand,
   * synchronous commands complete immediately, and their results are passed
   * back in the return object of the command execution API call.
   * Outside of that return object, there is no way to check the result
   * of a synchronous command.</p>
   *
   * @param commandId The command id.
   * @return Detailed command information.
   */
  @GET
  @Path("/{commandId}")
  public ApiCommand readCommand(@PathParam(COMMAND_ID) long commandId);

  /**
   * Abort a running command.
   *
   * @param commandId The command id.
   * @return Detailed command information.
   */
  @Consumes()
  @POST
  @Path("/{commandId}/abort")
  public ApiCommand abortCommand(@PathParam(COMMAND_ID) long commandId);

}
