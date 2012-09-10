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

import com.cloudera.api.model.ApiEcho;
import static com.cloudera.api.Parameters.MESSAGE;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ToolsResource {

  /**
   * Echoes the provided message back to the caller.
   *
   * @param message The message to echo back
   * @return The original message
   */
  @GET
  @Path("echo")
  public ApiEcho echo(
      @QueryParam(MESSAGE)
      @DefaultValue("Hello, World!") String message);

  /**
   * Throws an error containing the given input message. This is
   * what an error response looks like.
   *
   * <pre>
   *   {
   *     "message": "An error message",
   *     "causes": [ "A list of causes", "Potentially null" ]
   *   }
   * </pre>
   *
   * <p>The <em>message</em> field contains a description of the error.
   * The <em>causes</em> field, if not null, contains a list of causes
   * for the error.
   * </p>
   *
   * <p>Note that this <strong>never</strong> returns an echoMessage.
   * Instead, the result (and all error results) has the above structure.
   * </p>
   *
   * @param message The error message to echo
   * @return Will always be an exception
   */
  @GET
  @Path("echoError")
  public ApiEcho echoError(
      @QueryParam(MESSAGE)
      @DefaultValue(value = "Default error message") String message);

}
