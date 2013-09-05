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
package com.cloudera.api.v4;

import static com.cloudera.api.Parameters.*;

import com.cloudera.api.ApiTimeAggregation;
import com.cloudera.api.Enterprise;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiDisableJtHaArguments;
import com.cloudera.api.model.ApiEnableJtHaArguments;
import com.cloudera.api.model.ApiHdfsUsageReport;
import com.cloudera.api.model.ApiMrUsageReport;
import com.cloudera.api.v3.ServicesResourceV3;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV4 extends ServicesResourceV3 {

  /**
   * Create the Hive Metastore Database. Only works with embedded postgresql
   * database.
   * <p>
   * This command is to be run whenever a new login and database needs to be
   * created in the embedded postgresql database for a Hive service. This
   * command should usually be followed by a call to
   * hiveCreateMetastoreDatabaseTables.
   * <p>
   * Available since API v4.
   *
   * @param serviceName
   *          Name of the Hive service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/hiveCreateMetastoreDatabase")
  public ApiCommand hiveCreateMetastoreDatabaseCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Update Hive Metastore to point to a NameNode's Nameservice name instead of
   * hostname.
   * <p>
   * <strong>Back up the Hive Metastore Database before running this command.</strong>
   * <p>
   * This command is to be run after enabling HDFS High Availability. Only
   * available when all Hive Metastore Servers are stopped.
   * <p>
   * Available since API v4.
   *
   * @param serviceName
   *          Name of the Hive service on which to run the command.
   * @return Information about the submitted command
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/hiveUpdateMetastoreNamenodes")
  public ApiCommand hiveUpdateMetastoreNamenodesCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Creates the user directory of a Sqoop service in HDFS.
   *
   * <p>
   * Available since API v4.
   *
   * @param serviceName The Sqoop service name.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/createSqoopUserDir")
  public ApiCommand createSqoopUserDirCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Initializes the Solr service in Zookeeper.
   *
   * <p>
   * Available since API v4.
   *
   * @param serviceName The Solr service name.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/initSolr")
  public ApiCommand initSolrCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Creates the home directory of a Solr service in HDFS.
   *
   * <p>
   * Available since API v4.
   *
   * @param serviceName The Solr service name.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/createSolrHdfsHomeDir")
  public ApiCommand createSolrHdfsHomeDirCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Create the Hive user directory
   * <p>
   * Available since API v4.
   * </p>
   * @param serviceName The Hive service name.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/hiveCreateHiveUserDir")
  public ApiCommand createHiveUserDirCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The roles resource handler.
   */
  @Override
  @Path("/{serviceName}/roles")
  public RolesResourceV4 getRolesResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Enable high availability (HA) for a JobTracker.
   * <p>
   * This command only applies to CDH4 MapReduce services.
   * <p>
   * The command will create a new JobTracker on the specified host and then
   * create an active/standby pair with the existing JobTracker. Autofailover
   * will be enabled using ZooKeeper. A ZNode will be created for this purpose.
   * Command arguments provide option to forcefully create this ZNode if one
   * already exists. A node may already exists if JobTracker was previously
   * enabled in HA mode but HA mode was disabled later on. The ZNode is not
   * deleted when HA is disabled.
   * <p>
   * As part of enabling HA, any services that depends on the MapReduce service
   * being modified will be stopped. Command will redeploy the client
   * configurations for services of the cluster after HA has been enabled.
   *
   * @param serviceName The MapReduce service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/enableJtHa")
  public ApiCommand enableJtHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiEnableJtHaArguments args);

  /**
   * Disable high availability (HA) for JobTracker.
   *
   * As part of disabling HA, any services that depend on the MapReduce service
   * being modified will be stopped. The command arguments provide options to
   * specify name of JobTracker that will be preserved. The Command will
   * redeploy the client configurations for services of the cluster after HA
   * has been disabled.
   *
   * @param serviceName The MapReduce service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/disableJtHa")
  public ApiCommand disableJtHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiDisableJtHaArguments args);

  /**
   * Return the Impala queries resource handler.
   * <p/>
   *
   * @return The Impala queries resource handler
   */
  @Path("/{serviceName}/impalaQueries")
  public ImpalaQueriesResource getImpalaQueriesResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The replications resource handler.
   */
  @Path("/{serviceName}/replications")
  @Enterprise
  @Override
  public ReplicationsResourceV4 getReplicationsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The role command resource handler.
   */
  @Override
  @Path("/{serviceName}/roleCommands")
  public RoleCommandsResourceV4 getRoleCommandsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Fetch the HDFS usage report. For the requested time range, at the
   * specified aggregation intervals, the report shows HDFS disk usages
   * per user.
   * <p>
   * This call supports returning JSON or CSV, as determined by the
   * "Accept" header of application/json or text/csv.
   * <p>
   * Available since API v4.
   *
   * @param hdfsServiceName The HDFS service name.
   * @param nameService The (optional) HDFS nameservice. Required for HA setup.
   * @param from The (optional) start time of the report. Default to 24 hours before "to" time.
   * @param to The (optional) end time of the report. Default to now.
   * @param aggregation The (optional) aggregation period for the data.
   *                    Supports "hourly", "daily" (default) and "weekly".
   * @return Report data.
   */
  @GET
  @Path("/{serviceName}/reports/hdfsUsageReport")
  @Produces({ MediaType.APPLICATION_JSON, "text/csv" })
  @Enterprise
  public ApiHdfsUsageReport getHdfsUsageReport(
      @PathParam(SERVICE_NAME) String hdfsServiceName,
      @QueryParam(NAMESERVICE_NAME) String nameService,
      @QueryParam(FROM) String from,
      @QueryParam(TO) @DefaultValue(DATE_TIME_NOW) String to,
      @QueryParam("aggregation") @DefaultValue(DAILY_AGGREGATION)
      ApiTimeAggregation aggregation);

  /**
   * Fetch the MR usage report. For the requested time range, at the
   * specified aggregation intervals, the report shows job CPU usages (and other
   * metrics) per user.
   * <p>
   * This call supports returning JSON or CSV, as determined by the
   * "Accept" header of application/json or text/csv.
   * <p>
   * Available since API v4.
   *
   * @param mrServiceName The MR service name.
   * @param from The (optional) start time of the report. Default to 24 hours before "to" time.
   * @param to The (optional) end time of the report. Default to now.
   * @param aggregation The (optional) aggregation period for the data.
   *                    Supports "hourly", "daily" (default) and "weekly".
   * @return Report data.
   */
  @GET
  @Path("/{serviceName}/reports/mrUsageReport")
  @Produces({ MediaType.APPLICATION_JSON, "text/csv" })
  @Enterprise
  public ApiMrUsageReport getMrUsageReport(
      @PathParam(SERVICE_NAME) String mrServiceName,
      @QueryParam(FROM) String from,
      @QueryParam(TO) @DefaultValue(DATE_TIME_NOW) String to,
      @QueryParam("aggregation") @DefaultValue(DAILY_AGGREGATION)
      ApiTimeAggregation aggregation);
}
