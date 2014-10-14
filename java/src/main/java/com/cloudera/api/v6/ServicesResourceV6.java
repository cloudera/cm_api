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
package com.cloudera.api.v6;

import static com.cloudera.api.Parameters.COMMAND_NAME;
import static com.cloudera.api.Parameters.SERVICE_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiCommandMetadataList;
import com.cloudera.api.model.ApiDisableNnHaArguments;
import com.cloudera.api.model.ApiDisableOozieHaArguments;
import com.cloudera.api.model.ApiDisableRmHaArguments;
import com.cloudera.api.model.ApiEnableNnHaArguments;
import com.cloudera.api.model.ApiEnableOozieHaArguments;
import com.cloudera.api.model.ApiEnableRmHaArguments;
import com.cloudera.api.v4.ServicesResourceV4;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV6 extends ServicesResourceV4 {

  @Override
  public RolesResourceV6 getRolesResource(String serviceName);

  /**
   * Available since API v6. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @return The snapshots resource handler.
   */
  @Path("/{serviceName}/snapshots")
  public SnapshotsResource getSnapshotsResource(
      @PathParam(SERVICE_NAME) String serviceName);
  
  /**
   * @return The YARN applications resource handler.
   */
  @Path("/{serviceName}/yarnApplications")
  public YarnApplicationsResource getYarnApplicationsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Import MapReduce configuration into Yarn, overwriting Yarn configuration.
   * <p>
   * You will lose existing Yarn configuration. Read all MapReduce
   * configuration, role assignments, and role configuration groups and update
   * Yarn with corresponding values. MR1 configuration will be converted into
   * the equivalent MR2 configuration.
   * <p>
   * Before running this command, Yarn must be stopped and MapReduce must exist
   * with valid configuration.
   * <p>
   * Available since API v6.
   * 
   * @param serviceName
   *          Name of the Yarn service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Path("/{serviceName}/commands/importMrConfigsIntoYarn")
  public ApiCommand importMrConfigsIntoYarn(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Change the cluster to use MR2 instead of MR1. Services will be restarted.
   * <p>
   * Will perform the following steps:
   * <ul>
   * <li>Update all services that depend on MapReduce to instead depend on Yarn.
   * </li>
   * <li>Stop MapReduce</li>
   * <li>Start Yarn (MR2 Included)</li>
   * <li>Deploy Yarn (MR2) Client Configuration</li>
   * </ul>
   * <p>
   * Available since API v6.
   * 
   * @param serviceName
   *          Name of the Yarn service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Path("/{serviceName}/commands/switchToMr2")
  public ApiCommand switchToMr2(
      @PathParam(SERVICE_NAME) String serviceName);
  
  /**
   * Enable high availability (HA) for a YARN ResourceManager.
   * <p>
   * This command only applies to CDH5+ YARN services.
   * <p>
   * The command will create a new ResourceManager on the specified host and then
   * create an active/standby pair with the existing ResourceManager. Autofailover
   * will be enabled using ZooKeeper.
   * <p>
   * As part of enabling HA, any services that depends on the YARN service
   * being modified will be stopped. Command will redeploy the client
   * configurations for services of the cluster after HA has been enabled.
   *
   * @param serviceName The YARN service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/enableRmHa")
  public ApiCommand enableRmHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiEnableRmHaArguments args);
  
  /**
   * Disable high availability (HA) for ResourceManager.
   *
   * As part of disabling HA, any services that depend on the YARN service
   * being modified will be stopped. The command arguments provide options to
   * specify name of ResourceManager that will be preserved. The command will
   * redeploy the client configurations for services of the cluster after HA
   * has been disabled.
   *
   * @param serviceName The YARN service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/disableRmHa")
  public ApiCommand disableRmHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiDisableRmHaArguments args);

  /**
   * Enable high availability (HA) for Oozie service.
   * <p>
   * This command only applies to CDH5+ Oozie services.
   * <p>
   * The command will create new Oozie Servers on the specified hosts and
   * set the ZooKeeper and Load Balancer configs needed for Oozie HA.
   * <p>
   * As part of enabling HA, any services that depends on the Oozie service
   * being modified will be stopped and restarted after enabling Oozie HA.
   *
   * @param serviceName The Oozie service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/oozieEnableHa")
  public ApiCommand enableOozieHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiEnableOozieHaArguments args);

  /**
   * Disable high availability (HA) for Oozie.
   *
   * As part of disabling HA, any services that depend on the Oozie service
   * being modified will be stopped. The command arguments provide options to
   * specify name of Oozie Server that will be preserved. After deleting,
   * other Oozie servers, all the services that were stopped are restarted.
   *
   * @param serviceName The Oozie service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/oozieDisableHa")
  public ApiCommand disableOozieHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiDisableOozieHaArguments args);
  
  /**
   * Enable High Availability (HA) with Automatic Failover for an HDFS NameNode.
   * <p>
   * The command will create a Standby NameNode for the given nameservice
   * and create FailoverControllers for both Active and Standby NameNodes.
   * The SecondaryNameNode associated with the Active NameNode will be deleted.
   * <p>
   * The command will also create JournalNodes needed for HDFS HA if they
   * do not already exist.
   * <p>
   * As part of enabling HA, any services that depend on the HDFS service being
   * modified will be stopped. They will be restarted after HA has been enabled.
   * Finally, client configs for HDFS and its depedents will be re-deployed.
   *
   * @param serviceName The HDFS service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hdfsEnableNnHa")
  public ApiCommand hdfsEnableNnHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiEnableNnHaArguments args);

  /**
   * Disable High Availability (HA) with Automatic Failover for an HDFS NameNode.
   * <p>
   * As part of disabling HA, any services that depend on the HDFS service being
   * modified will be stopped. The command will delete the Standby NameNode
   * associated with the specified NameNode. Any FailoverControllers associated
   * with the NameNode's nameservice are also deleted. A SecondaryNameNode
   * is created on the host specified by the arugments.
   * <p>
   * If no nameservices uses Quorum Journal after HA is disabled for the specified
   * nameservice, then all JournalNodes are also deleted.
   * <p>
   * Then, HDFS service is restarted and all services that were stopped
   * are started again afterwards.
   * Finally, client configs for HDFS and its depedents will be re-deployed.
   * 
   * @param serviceName The HDFS service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hdfsDisableNnHa")
  public ApiCommand hdfsDisableNnHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiDisableNnHaArguments args);

  /**
   * Upgrade HDFS Metadata as part of a major version upgrade.
   * <p/>
   * When doing a major version upgrade for HDFS, it is necessary to start HDFS
   * in a special mode where it will do any necessary upgrades of stored
   * metadata. Trying to start HDFS normally will result in an error message and
   * the NameNode(s) failing to start.
   * <p/>
   * The metadata upgrade must eventually be finalized, using the
   * hdfsFinalizeMetadataUpgrade command on the NameNode.
   * <p/>
   * Available since API v6.
   *
   * @param serviceName The HDFS service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hdfsUpgradeMetadata")
  public ApiCommand hdfsUpgradeMetadataCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Upgrade Hive Metastore as part of a major version upgrade.
   * <p/>
   * When doing a major version upgrade for Hive, it is necessary to upgrade
   * data in the metastore database.
   * <p/>
   * Available since API v6.
   *
   * @param serviceName The Hive service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hiveUpgradeMetastore")
  public ApiCommand hiveUpgradeMetastoreCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Upgrade Oozie Database schema as part of a major version upgrade.
   * <p/>
   * When doing a major version upgrade for Oozie, it is necessary to upgrade
   * the schema of its database before Oozie can run successfully.
   * <p/>
   * Available since API v6.
   *
   * @param serviceName The Oozie service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/oozieUpgradeDb")
  public ApiCommand oozieUpgradeDbCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Upgrade HBase data in HDFS and ZooKeeper as part of upgrade from CDH4 to CDH5.
   * <p/>
   * This is required in order to run HBase after upgrade.
   * <p/>
   * Available since API v6.
   *
   * @param serviceName The HBase service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hbaseUpgrade")
  public ApiCommand hbaseUpgradeCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Upgrade Sqoop Database schema as part of a major version upgrade.
   * <p/>
   * When doing a major version upgrade for Sqoop, it is necessary to upgrade
   * the schema of its database before Sqoop can run successfully.
   * <p/>
   * Available since API v6.
   *
   * @param serviceName The Sqoop service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/sqoopUpgradeDb")
  public ApiCommand sqoopUpgradeDbCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Create the Yarn job history directory
   * <p>
   * Available since API v6.
   * </p>
   * @param serviceName The YARN service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/yarnCreateJobHistoryDirCommand")
  public ApiCommand createYarnJobHistoryDirCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Create the Yarn NodeManager remote application log directory
   * <p>
   * Available since API v6.
   * </p>
   * @param serviceName The YARN service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/yarnNodeManagerRemoteAppLogDirCommand")
  public ApiCommand createYarnNodeManagerRemoteAppLogDirCommand(
      @PathParam(SERVICE_NAME) String serviceName);

 /**
  * Create the Impala user directory
  * <p>
  * Available since API v6.
  * </p>
  * @param serviceName The Impala service name.
  * @return Information about the submitted command.
  */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/impalaCreateUserDir")
  public ApiCommand createImpalaUserDirCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * <strong>Not needed in CM 5.0.0 Release, since Impala Catalog Database
   * is not yet available in CDH as of this release.</strong>
   * Create the Impala Catalog Database. Only works with embedded postgresql
   * database.
   * <p>
   * This command is to be run whenever a new user and database needs to be
   * created in the embedded postgresql database for the Impala Catalog Server.
   * This command should usually be followed by a call to
   * impalaCreateCatalogDatabaseTables.
   * <p>
   * Available since API v6.
   *
   * @param serviceName Name of the Impala service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/impalaCreateCatalogDatabase")
  public ApiCommand impalaCreateCatalogDatabaseCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * <strong>Not needed in CM 5.0.0 Release, since Impala Catalog Database
   * is not yet available in CDH as of this release.</strong>
   * Create the Impala Catalog Database tables.
   * <p>
   * This command is to be run whenever a new database has been specified. Will
   * do nothing if tables already exist. Will not perform an upgrade. Only
   * available when all Impala Catalog Servers are stopped.
   * <p>
   * Available since API v6.
   *
   * @param serviceName Name of the Impala service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/impalaCreateCatalogDatabaseTables")
  public ApiCommand impalaCreateCatalogDatabaseTablesCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The role command resource handler.
   */
  @Override
  @Path("/{serviceName}/roleCommands")
  public RoleCommandsResourceV6 getRoleCommandsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Executes a command on the service specified
   * by name.
   * <p>
   * Available since API v6.
   * </p>
   * @param serviceName The service name.
   * @param commandName The command name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/{commandName}")
  public ApiCommand serviceCommandByName(
      @PathParam(SERVICE_NAME) String serviceName,
      @PathParam(COMMAND_NAME) String commandName);

  /**
   * Lists all the commands that can be executed by name
   * on the provided service.
   *
   * <p>
   * Available since API v6.
   * </p>
   * @param serviceName The service name.
   * @return a list of command metadata objects.
   */
  @GET
  @Path("/{serviceName}/commandsByName")
  public ApiCommandMetadataList listServiceCommands(
          @PathParam(SERVICE_NAME) String serviceName);
}
