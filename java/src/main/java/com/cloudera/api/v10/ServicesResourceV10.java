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
package com.cloudera.api.v10;

import static com.cloudera.api.Parameters.SERVICE_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v8.ServicesResourceV8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV10 extends ServicesResourceV8 {

  /**
   * @return The role command resource handler.
   */
  @Override
  @Path("/{serviceName}/roleCommands")
  public RoleCommandsResourceV10 getRoleCommandsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Runs Hue's dumpdata command.
   *
   * Available since API v10.
   *
   * @param serviceName The name of the service
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hueDumpDb")
  public ApiCommand hueDumpDbCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Runs Hue's loaddata command.
   *
   * Available since API v10.
   *
   * @param serviceName The name of the service
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hueLoadDb")
  public ApiCommand hueLoadDbCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Runs Hue's syncdb command.
   *
   * Available since API v10.
   *
   * @param serviceName The name of the service
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hueSyncDb")
  public ApiCommand hueSyncDbCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Create the Oozie Server Database. Only works with embedded postgresql
   * database.
   * <p>
   * This command is to be run whenever a new user and database need to be
   * created in the embedded postgresql database for an Oozie service. This
   * command should usually be followed by a call to createOozieDb.
   * <p>
   * Available since API v10.
   *
   * @param serviceName
   *          Name of the Oozie service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/oozieCreateEmbeddedDatabase")
  public ApiCommand oozieCreateEmbeddedDatabaseCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Create the Sqoop2 Server Database tables.
   * <p>
   * This command is to be run whenever a new database has been specified. Will
   * do nothing if tables already exist. Will not perform an upgrade. Only
   * available when Sqoop2 Server is stopped.
   * <p>
   * Available since API v10.
   *
   * @param serviceName Name of the Sentry service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/sqoopCreateDatabaseTables")
  public ApiCommand sqoopCreateDatabaseTablesCommand(
      @PathParam(SERVICE_NAME) String serviceName);
}
