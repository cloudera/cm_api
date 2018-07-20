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
package com.cloudera.api.v16;

import static com.cloudera.api.Parameters.COMMAND_NAME;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.EXTERNAL_ACCOUNT_NAME;
import static com.cloudera.api.Parameters.EXTERNAL_ACCOUNT_TYPE_NAME;

import com.cloudera.api.DataView;
import com.cloudera.api.Parameters;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiCommandMetadataList;
import com.cloudera.api.model.ApiConfigList;
import com.cloudera.api.v14.ExternalAccountsResourceV14;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Manage external accounts used by various Cloudera Manager features, for
 * performing external tasks.
 */
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ExternalAccountsResourceV16
    extends ExternalAccountsResourceV14 {

  /**
   * Get configs of external account for the given account name.
   *
   * @param external account name
   * @param view The view to materialize, either "summary" or "full".
   * @return the current external account configurations.
   */
  @GET
  @Path("/account/{" + Parameters.EXTERNAL_ACCOUNT_NAME + "}" + "/config")
  ApiConfigList readConfig(
      @PathParam(Parameters.EXTERNAL_ACCOUNT_NAME) String name,
      @QueryParam(Parameters.DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView view);

  /**
   * Upadate configs of external account for the given account name.
   *
   * @param external account name
   * @param config Settings to update.
   * @param message Optional message describing the changes.
   * @return The updated configuration.
   */
  @PUT
  @Path("/account/{" + Parameters.EXTERNAL_ACCOUNT_NAME + "}" + "/config")
  ApiConfigList updateConfig(
      @PathParam(Parameters.EXTERNAL_ACCOUNT_NAME) String name,
      ApiConfigList config,
      @QueryParam(Parameters.MESSAGE) String message);

  /**
   * Executes a command on the external account specified
   * by name.
   * <p>
   * Available since API v16.
   *
   * @param name The external account name.
   * @param commandName The command name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/account/{" + Parameters.EXTERNAL_ACCOUNT_NAME + "}/commands/{" +
      Parameters.COMMAND_NAME + "}")
  public ApiCommand externalAccountCommandByName(
      @PathParam(EXTERNAL_ACCOUNT_NAME) String name,
      @PathParam(COMMAND_NAME) String commandName);

  /**
   * Lists all the commands that can be executed by name on the
   * provided external account type.
   * <p>
   * Available since API v16.
   *
   * @param typeName The external account type name
   * @return a list of command metadata objects
   */
  @GET
  @Path(("/typeInfo/{" + Parameters.EXTERNAL_ACCOUNT_TYPE_NAME +
      "}/commandsByName"))
  public ApiCommandMetadataList listExternalAccountCommands(
      @PathParam(EXTERNAL_ACCOUNT_TYPE_NAME) String typeName);
}
