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
package com.cloudera.api.v3;

import static com.cloudera.api.Parameters.SERVICE_NAME;

import com.cloudera.api.Enterprise;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiRollEditsArgs;
import com.cloudera.api.model.ApiRollingRestartArgs;
import com.cloudera.api.model.ApiService;
import com.cloudera.api.v2.ServicesResourceV2;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV3 extends ServicesResourceV2 {

  /**
   * @return The replications resource handler.
   */
  @Path("/{serviceName}/replications")
  @Enterprise
  public ReplicationsResource getReplicationsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Command to run rolling restart of roles in a service. The sequence is:
   * <ol>
   * <li>Restart all the non-slave roles
   * <li>If slaves are present restart them in batches of size specified in RollingRestartCmdArgs
   * <li>Perform any post-command needed after rolling restart
   * </ol>
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   */
  @POST
  @Path("/{serviceName}/commands/rollingRestart")
  @Enterprise
  public ApiCommand rollingRestart(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiRollingRestartArgs args);

  /**
   * Creates directory for Oozie user in HDFS and installs the ShareLib in it.
   *
   * <p>
   * Available since API v3.
   * </p>
   *
   * @param serviceName Name of the Oozie service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Path("/{serviceName}/commands/installOozieShareLib")
  public ApiCommand installOozieShareLib(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The role config group resource handler.
   */
  @Path("/{serviceName}/roleConfigGroups")
  public RoleConfigGroupsResource getRoleConfigGroupsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Roll the edits of an HDFS NameNode or Nameservice.
   * <p>
   * Available since API v3.
   *
   * @param serviceName The HDFS service name.
   * @param rollEditsArgs Arguments to the Roll Edits command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hdfsRollEdits")
  public ApiCommand hdfsRollEditsCommand(
      @PathParam(SERVICE_NAME) String serviceName, ApiRollEditsArgs rollEditsArgs);

  /**
   * Updates service information.
   * <p/>
   * This method will update only writable fields of the service information.
   * Currently this only includes the service display name.
   * <p/>
   * Available since API v3.
   *
   * @param serviceName The service name.
   * @param service Updated service information.
   * @return The updated service information.
   */
  @PUT
  @Path("/{serviceName}")
  public ApiService updateService(@PathParam(SERVICE_NAME) String serviceName,
      ApiService service);

  /**
   * Create the Hive Metastore Database tables.
   * <p>
   * This command is to be run whenever a new database has been specified. Will
   * do nothing if tables already exist. Will not perform an upgrade. Only
   * Available when all Hive Metastore Servers are stopped.
   * <p>
   * Available since API v3.
   *
   * @param serviceName Name of the Hive service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/hiveCreateMetastoreDatabaseTables")
  public ApiCommand hiveCreateMetastoreDatabaseTablesCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Create the Hive warehouse directory, on Hive services.
   *
   * @param serviceName The Hive service name.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/hiveCreateHiveWarehouse")
  public ApiCommand createHiveWarehouseCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The role command resource handler.
   */
  @Override
  @Path("/{serviceName}/roleCommands")
  public RoleCommandsResourceV3 getRoleCommandsResource(
      @PathParam(SERVICE_NAME) String serviceName);

}
