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
package com.cloudera.api.v8;

import static com.cloudera.api.Parameters.SERVICE_NAME;

import com.cloudera.api.model.ApiYarnApplicationDiagnosticsCollectionArgs;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiDisableLlamaHaArguments;
import com.cloudera.api.model.ApiEnableLlamaHaArguments;
import com.cloudera.api.model.ApiEnableLlamaRmArguments;
import com.cloudera.api.v7.ServicesResourceV7;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV8 extends ServicesResourceV7 {

  @Override
  public RolesResourceV8 getRolesResource(String serviceName);

  /**
   * @return The role command resource handler.
   */
  @Override
  @Path("/{serviceName}/roleCommands")
  public RoleCommandsResourceV8 getRoleCommandsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Upgrade the Sentry Server Database tables.
   * <p>
   * This command is to be run whenever Sentry requires an upgrade to its
   * database tables.
   * <p>
   * Available since API v8.
   *
   * @param serviceName Name of the Sentry service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/sentryUpgradeDatabaseTables")
  public ApiCommand sentryUpgradeDatabaseTablesCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Enable Llama-based resource management for Impala.
   * <p>
   * This command only applies to CDH 5.1+ Impala services.
   * <p>
   * This command configures YARN and Impala for Llama resource management,
   * and then creates one or two Llama roles, as specified by the arguments.
   * When two Llama roles are created, they are configured as an active/standby
   * pair. Auto-failover from active to standby Llama will be enabled using
   * ZooKeeper.
   * <p>
   * If an optional role name(s) are supplied, the new Llama role(s) will be
   * named accordingly; otherwise, role name(s) will be automatically generated.
   * <p>
   * By default, YARN, Impala, and any dependent services will be restarted,
   * and client configuration will be re-deployed across the cluster. These
   * default actions may be suppressed via {@code setSkipRestart()}.
   * <p>
   * In order to enable Llama resource management, a YARN service must be
   * present in the cluster, and Cgroup-based resource management must be
   * enabled for all hosts with NodeManager roles. If these preconditions
   * are not met, the command will fail.
   * <p>
   * Available since API v8.
   *
   * @param serviceName The Impala service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/impalaEnableLlamaRm")
  public ApiCommand enableLlamaRmCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiEnableLlamaRmArguments args);

  /**
   * Disable Llama-based resource management for Impala.
   * <p>
   * This command only applies to CDH 5.1+ Impala services.
   * <p>
   * This command disables resource management for Impala by removing all
   * Llama roles present in the Impala service. Any services that depend
   * on the Impala service being modified are restarted by the command,
   * and client configuration is deployed for all services of the cluster.
   * <p>
   * Note that any configuration changes made to YARN and Impala when
   * enabling resource management are not reverted by this command.
   * <p>
   * Available since API v8.
   *
   * @param serviceName The Impala service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/impalaDisableLlamaRm")
  public ApiCommand disableLlamaRmCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Formats the state store in ZooKeeper used for Resource Manager
   * High Availability. Typically used while moving from non-secure
   * to secure cluster or vice-versa.
   * <p>
   * Available since API v8.
   * @param serviceName The YARN service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/yarnFormatStateStore")
  public ApiCommand yarnFormatStateStore(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Finalizes the rolling upgrade for HDFS by updating the NameNode
   * metadata permanently to the next version. Should be done after
   * doing a rolling upgrade to a CDH version >= 5.2.0.
   * <p>
   * Available since API v8.
   * @param serviceName The HDFS service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hdfsFinalizeRollingUpgrade")
  public ApiCommand hdfsFinalizeRollingUpgrade(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Enable high availability (HA) for an Impala Llama ApplicationMaster.
   * <p>
   * This command only applies to CDH 5.1+ Impala services.
   * <p>
   * The command will create a new Llama role on the specified host, and then
   * create an active/standby pair with the existing Llama role. Autofailover
   * will be enabled using ZooKeeper.
   * <p>
   * If an optional role name is supplied, the new Llama role will be named
   * accordingly; otherwise, a role name will be automatically generated.
   * <p>
   * As part of enabling HA, any services that depend on the Impala service
   * being modified will be stopped. The command will redeploy the client
   * configurations for services of the cluster after HA has been enabled.
   * <p>
   * Available since API v8.
   *
   * @param serviceName The Impala service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/impalaEnableLlamaHa")
  public ApiCommand enableLlamaHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiEnableLlamaHaArguments args);

  /**
   * Disable high availability (HA) for an Impala Llama ApplicationMaster.
   * <p>
   * This command only applies to CDH 5.1+ Impala services.
   * <p>
   * The command argument specifies the name of the Llama role to be retained.
   * The other Llama role in the HA pair will be removed. As part of disabling
   * HA, any services that depend on the Impala service being modified will be
   * stopped. The command will redeploy the client configurations for all
   * services of the cluster after HA has been disabled.
   * <p>
   * Available since API v8.
   *
   * @param serviceName The Impala service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/impalaDisableLlamaHa")
  public ApiCommand disableLlamaHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiDisableLlamaHaArguments args);

  /**
   * Collect the Diagnostics data for Yarn applications
   * <p>
   * Available since API v8.
   *
   * @param serviceName Name of the YARN service on which to run the command.
   * @param args Arguments used for collecting diagnostics data for Yarn applications
   * @return Information about the submitted command
   */
  @POST
  @Path("/{serviceName}/commands/yarnApplicationDiagnosticsCollection")
  public ApiCommand collectYarnApplicationDiagnostics(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiYarnApplicationDiagnosticsCollectionArgs args);
}
