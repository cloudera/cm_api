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
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiCommandList;
import com.cloudera.api.model.ApiRoleTypeList;
import com.cloudera.api.model.ApiService;
import com.cloudera.api.model.ApiServiceConfig;
import static com.cloudera.api.Parameters.MESSAGE;
import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface MgmtServiceResource {

  /**
   * Retrieve information about the Cloudera Management Services.
   *
   * @return Details about the management service.
   */
  @GET
  @Path("/")
  public ApiService readService();

  /**
   * Retrieve the configuration of the Cloudera Management Services.
   *
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List with configured and available configuration options.
   */
  @GET
  @Path("/config")
  public ApiServiceConfig readServiceConfig(
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Update the Cloudera Management Services configuration.
   * <p>
   * If a value is set in the given configuration, it will be added
   * to the service's configuration, replacing any existing entries.
   * If a value is unset (its value is null), the existing
   * configuration for the attribute will be erased, if any.
   * <p>
   * Attributes that are not listed in the input will maintain their
   * current values in the configuration.
   *
   * @param message Optional message describing the changes.
   * @param config Configuration changes.
   * @return The new service configuration.
   */
  @PUT
  @Path("/config")
  public ApiServiceConfig updateServiceConfig(
      @QueryParam(MESSAGE) String message,
      ApiServiceConfig config);

  /**
   * List the supported role types for the Cloudera Management Services.
   *
   * @return List of role types the service supports.
   */
  @GET
  @Path("/roleTypes")
  public ApiRoleTypeList listRoleTypes();

  /**
   * List active Cloudera Management Services commands.
   *
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return A list of active role commands.
   */
  @GET
  @Path("/commands")
  public ApiCommandList listActiveCommands(
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Start the Cloudera Management Services.
   *
   * @return A reference to the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/start")
  public ApiCommand startCommand();

  /**
   * Stop the Cloudera Management Services.
   *
   * @return A reference to the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/stop")
  public ApiCommand stopCommand();

  /**
   * Restart the Cloudera Management Services.
   *
   * @return A reference to the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/restart")
  public ApiCommand restartCommand();

  /**
   * Setup the Cloudera Management Services.
   * <p>
   * Configure the CMS instance and create all the management roles. The
   * provided configuration data can be used to set up host mappings for
   * each role, and required configuration such as database connection
   * information for specific roles.
   * <p>
   * Regardless of the list of roles provided in the input data, all
   * management roles are created by this call. The input is used to
   * override any default settings for the specific roles.
   * <p>
   * This method needs a valid CM license to be installed beforehand.
   * <p>
   * This method does not start any services or roles.
   * <p>
   * This method will fail if a CMS instance already exists.
   * <p>
   * Available role types:
   * <ul>
   *   <li>SERVICEMONITOR</li>
   *   <li>ACTIVITYMONITOR</li>
   *   <li>HOSTMONITOR</li>
   *   <li>REPORTSMANAGER</li>
   *   <li>EVENTSERVER</li>
   *   <li>ALERTPUBLISHER</li>
   *   <li>NAVIGATOR</li>
   *   <li>NAVIGATORMETASERVER</li>
   * </ul>
   *
   * <p/>
   * REPORTSMANAGER, NAVIGATOR and NAVIGATORMETASERVER are only available with
   * Cloudera Manager Enterprise Edition.
   *
   * @param service Role configuration overrides.
   * @return ApiService The CMS information.
   */
  @PUT
  @Path("/")
  public ApiService setupCMS(ApiService service);

  /**
   * Return the management roles resource handler.
   *
   * @return The roles resource handler.
   */
  @Path("/roles")
  public MgmtRolesResource getRolesResource();

  /**
   * Returns the management role commands resource handler.
   *
   * @return The roles command handler.
   */
  @Path("/roleCommands")
  public MgmtRoleCommandsResource getMgmtRoleCommandsResource();

}
