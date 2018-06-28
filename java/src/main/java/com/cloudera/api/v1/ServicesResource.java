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

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.DATE_TIME_NOW;
import static com.cloudera.api.Parameters.FROM;
import static com.cloudera.api.Parameters.METRICS;
import static com.cloudera.api.Parameters.SERVICE_NAME;
import static com.cloudera.api.Parameters.TO;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiCommandList;
import com.cloudera.api.model.ApiHdfsDisableHaArguments;
import com.cloudera.api.model.ApiHdfsFailoverArguments;
import com.cloudera.api.model.ApiHdfsHaArguments;
import com.cloudera.api.model.ApiMetricList;
import com.cloudera.api.model.ApiRoleNameList;
import com.cloudera.api.model.ApiRoleTypeList;
import com.cloudera.api.model.ApiService;
import com.cloudera.api.model.ApiServiceConfig;
import com.cloudera.api.model.ApiServiceList;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResource {

  /**
   * Creates a list of services.
   * <p>
   * There are typically two service creation strategies:
   * <ol>
   *    <li>
   *      The caller may choose to set up a new service piecemeal, by first
   *      creating the service itself (without any roles or configuration),
   *      and then create the roles, and then specify configuration.
   *    </li>
   *    <li>
   *      Alternatively, the caller can pack
   *      all the information in one call, by fully specifying the fields in
   *      the {@link com.cloudera.api.model.ApiService} object, with
   *      <ul>
   *        <li>service config and role type config, and</li>
   *        <li>role to host assignment.</li>
   *      </ul>
   *    </li>
   * </ol>
   *
   * <table>
   *   <thead>
   *     <tr>
   *       <th>Cluster Version</th>
   *       <th>Available Service Types</th>
   *     </tr>
   *   </thead>
   *   <tbody>
   *     <tr>
   *       <td>CDH4</td>
   *       <td>HDFS, MAPREDUCE, HBASE, OOZIE, ZOOKEEPER, HUE, YARN, IMPALA, FLUME, HIVE, SOLR, SQOOP, KS_INDEXER</td>
   *     </tr>
   *     <tr>
   *       <td>CDH5</td>
   *       <td>HDFS, MAPREDUCE, HBASE, OOZIE, ZOOKEEPER, HUE, YARN, IMPALA,
   *           FLUME, HIVE, SOLR, SQOOP, KS_INDEXER, SQOOP_CLIENT, SENTRY,
   *           ACCUMULO16, KMS, SPARK_ON_YARN
   *       </td>
   *     </tr>
   *   </tbody>
   * </table>
   *
   * As of V6, GET /{clusterName}/serviceTypes should be used to get
   * the service types available to the cluster.
   *
   * @param services Details of the services to create.
   * @return List of created services.
   */
  @POST
  @Path("/")
  public ApiServiceList createServices(ApiServiceList services);

  /**
   * Deletes a service from the system.
   *
   * @param serviceName The name of the service to delete.
   * @return The details of the deleted service.
   */
  @DELETE
  @Path("/{serviceName}")
  public ApiService deleteService(@PathParam(SERVICE_NAME) String serviceName);

  /**
   * Lists all services registered in the cluster.
   *
   * @return List of services.
   */
  @GET
  @Path("/")
  public ApiServiceList readServices(@DefaultValue(DATA_VIEW_DEFAULT)
                                     @QueryParam(DATA_VIEW) DataView dataView);

  /**
   * Retrieves details information about a service.
   *
   * @param serviceName The service name.
   * @return The details of the service.
   */
  @GET
  @Path("/{serviceName}")
  public ApiService readService(@PathParam(SERVICE_NAME) String serviceName);

  /**
   * Retrieves the configuration of a specific service.
   * <p>
   * The "summary" view contains only the configured parameters, and
   * configuration for role types that contain configured parameters.
   * <p>
   * The "full" view contains all available configuration parameters for
   * the service and its role types.
   * This mode performs validation on the configuration, which could take
   * a few seconds on a large cluster (around 500 nodes or more).
   *
   * @param serviceName The service to query.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of service and role types configuration parameters.
   */
  @GET
  @Path("/{serviceName}/config")
  public ApiServiceConfig readServiceConfig(
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Updates the service configuration with the given values.
   * <p>
   * If a value is set in the given configuration, it will be added
   * to the service's configuration, replacing any existing entries.
   * If a value is unset (its value is null), the existing
   * configuration for the attribute will be erased, if any.
   * <p>
   * Attributes that are not listed in the input will maintain their
   * current values in the configuration.
   *
   * @param serviceName The service to modify.
   * @param message Optional message describing the changes.
   * @param config Configuration changes.
   * @return The new service configuration.
   */
  @PUT
  @Path("/{serviceName}/config")
  public ApiServiceConfig updateServiceConfig(
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam(value = "message") String message,
      ApiServiceConfig config);


  /**
   * List the supported role types for a service.
   *
   * @param serviceName The service to modify.
   * @return List of role types the service supports.
   */
  @GET
  @Path("/{serviceName}/roleTypes")
  public ApiRoleTypeList listRoleTypes(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Fetch metric readings for a particular service.
   * <p>
   * By default, this call will look up all metrics available for the service.
   * If only specific metrics are desired, use the <i>metrics</i> parameter.
   * <p>
   * By default, the returned results correspond to a 5 minute window based on
   * the provided end time (which defaults to the current server time). The
   * <i>from</i> and <i>to</i> parameters can be used to control the window
   * being queried. A maximum window of 3 hours is enforced.
   * <p>
   * When requesting a "full" view, aside from the extended properties of the
   * returned metric data, the collection will also contain information about
   * all metrics available for the service, even if no readings are available
   * in the requested window.
   * <p>
   * HDFS services that have more than one nameservice will not expose
   * any metrics. Instead, the nameservices should be queried separately.
   * <p/>
   *
   * @param serviceName The name of the service.
   * @param from Start of the period to query.
   * @param to End of the period to query.
   * @param metrics Filter for which metrics to query.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of readings from the monitors.
   * @deprecated This endpoint is not supported as of v6. Use the timeseries API
   * instead. To get all metrics for a service with the timeseries API use
   * the query:
   * <br>
   * <br>
   * 'select * where serviceName = $SERVICE_NAME'.
   * <br>
   * <br>
   * To get specific metrics for a service use a comma-separated list of
   * the metric names as follows:
   * <br>
   * <br>
   * 'select $METRIC_NAME1, $METRIC_NAME2 where serviceName = $SERVICE_NAME'.
   * <br>
   * <br>
   * For more information see the <a href="http://tiny.cloudera.com/cm_tsquery">
   * tsquery language documentation</a>.<p/>
   */
  @Deprecated
  @GET
  @Path("/{serviceName}/metrics")
  public ApiMetricList getMetrics(
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam(FROM) String from,
      @QueryParam(TO)
        @DefaultValue(DATE_TIME_NOW)
        String to,
      @QueryParam(METRICS) List<String> metrics,
      @QueryParam(DATA_VIEW)
        @DefaultValue(DATA_VIEW_DEFAULT)
        DataView dataView);

  /**
   * List active service commands.
   *
   * @param serviceName The service to which the role belongs.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return A list of active service commands.
   */
  @GET
  @Path("/{serviceName}/commands")
  public ApiCommandList listActiveCommands(
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Disable auto-failover for a highly available HDFS nameservice.
   * <p>
   * The command will modify the nameservice's NameNodes configuration to
   * disable automatic failover, and delete the existing failover controllers.
   * <p>
   * The ZooKeeper dependency of the service will not be removed.
   *
   * @param serviceName The HDFS service name.
   * @param nameservice The nameservice name.
   * @return Information about the submitted command.
   * @deprecated This endpoint is not supported v6 onwards. Use hdfsDisableNnHa on the HDFS service instead.
   */
  @Deprecated
  @POST
  @Path("/{serviceName}/commands/hdfsDisableAutoFailover")
  @Consumes({MediaType.TEXT_PLAIN,MediaType.APPLICATION_JSON})
  public ApiCommand hdfsDisableAutoFailoverCommand(
      @PathParam(SERVICE_NAME) String serviceName, String nameservice);

  /**
   * Disable high availability (HA) for an HDFS NameNode.
   * <p>
   * The NameNode to be kept must be running before HA can be disabled.
   * <p>
   * As part of disabling HA, any services that depend on the HDFS service being
   * modified will be stopped. The command arguments provide options to
   * re-start these services and to re-deploy the client configurations for
   * services of the cluster after HA has been disabled.
   * 
   * @param serviceName The HDFS service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   * @deprecated This endpoint is not supported v6 onwards. Use hdfsDisableNnHa on the HDFS service instead.
   */
  @Deprecated
  @POST
  @Path("/{serviceName}/commands/hdfsDisableHa")
  public ApiCommand hdfsDisableHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiHdfsDisableHaArguments args);

  /**
   * Enable auto-failover for an HDFS nameservice.
   * <p>
   * This command requires that the nameservice exists, and HA has been
   * configured for that nameservice.
   * <p>
   * The command will create the needed failover controllers, perform the
   * needed initialization and configuration, and will start the new roles.
   * The existing NameNodes which are part of the nameservice will be
   * re-started in the process.
   * <p>
   * This process may require changing the service's configuration, to add a
   * dependency on the provided ZooKeeper service. This will be done if such a
   * dependency has not been configured yet, and will cause roles that are
   * not affected by this command to show an "outdated configuration" status.
   * <p>
   * If a ZooKeeper dependency has already been set up by some other means,
   * it does not need to be provided in the command arguments.
   *
   * @param serviceName The HDFS service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   * @deprecated This endpoint is not supported v6 onwards. Use hdfsEnableNnHa on the HDFS service instead.
   */
  @Deprecated
  @POST
  @Path("/{serviceName}/commands/hdfsEnableAutoFailover")
  public ApiCommand hdfsEnableAutoFailoverCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiHdfsFailoverArguments args);

  /**
   * Enable high availability (HA) for an HDFS NameNode.
   * <p>
   * The command will set up the given "active" and "stand-by" NameNodes as
   * an HA pair. Both nodes need to already exist.
   * <p>
   * If there is a SecondaryNameNode associated with either given NameNode
   * instance, it will be deleted.
   * <p>
   * Note that while the shared edits path may be different for both nodes,
   * they need to point to the same underlying storage (e.g., an NFS share).
   * <p>
   * As part of enabling HA, any services that depend on the HDFS service being
   * modified will be stopped. The command arguments provide options to
   * re-start these services and to re-deploy the client configurations for
   * services of the cluster after HA has been enabled.
   *
   * @param serviceName The HDFS service name.
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   * @deprecated This endpoint is not supported v6 onwards. Use hdfsEnableNnHa on the HDFS service instead.
   */
  @Deprecated
  @POST
  @Path("/{serviceName}/commands/hdfsEnableHa")
  public ApiCommand hdfsEnableHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiHdfsHaArguments args);

  /**
   * Initiate a failover in an HDFS HA NameNode pair.
   * <p>
   * The arguments should contain the names of the two NameNodes in
   * the HA pair. The first one should be the currently active NameNode,
   * the second one the NameNode to be made active.
   *
   * @param serviceName The HDFS service name.
   * @param force Whether to force failover.
   * @param roleNames Names of the NameNodes in the HA pair.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hdfsFailover")
  public ApiCommand hdfsFailoverCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam("force")
        @DefaultValue("false")
        boolean force,
      ApiRoleNameList roleNames);

  /**
   * Create the Beeswax role's Hive warehouse directory, on Hue services.
   *
   * @param serviceName
   *          The Hue service name.
   * @deprecated Use hiveCreateHiveWarehouse on the Hive service instead.
   *             Deprecated since V4.
   * @return Information about the submitted command.
   */
  @Deprecated
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/hueCreateHiveWarehouse")
  public ApiCommand createBeeswaxWarehouseCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Creates the root directory of an HBase service.
   *
   * @param serviceName The HBase service name.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/hbaseCreateRoot")
  public ApiCommand createHBaseRootCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Decommission roles of a service.
   * <p>
   * For HBase services, the list should contain names of RegionServers to
   * decommission.
   * <p>
   * For HDFS services, the list should contain names of DataNodes to
   * decommission.
   *
   * @param serviceName The HBase service name.
   * @param roleNames List of role names to decommision.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/decommission")
  public ApiCommand decommissionCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiRoleNameList roleNames);

  /**
   * Deploy a service's client configuration.
   * <p>
   * The client configuration is deployed to the hosts where the given roles
   * are running.
   * <p/>
   * Added in v3: passing null for the role name list will deploy client configs
   *              to all known service roles.
   * Added in v6: passing an empty role name list will deploy client configs
   *              to all known service roles.
   * <p/>
   * In Cloudera Manager 5.3 and newer, client configurations are fully managed,
   * meaning that the server maintains state about which client configurations
   * should exist and be managed by alternatives, and the agents actively
   * rectify their hosts with this state. Consequently, if this API call is made
   * with a specific set of roles, Cloudera Manager will deactivate, from
   * alternatives, any deployed client configs from any non-gateway roles that
   * are <em>not</em> specified as arguments. Gateway roles are always preserved,
   * and calling this API with an empty or null argument continues to deploy to
   * all roles.
   * <p/>
   * @param serviceName The service name.
   * @param roleNames List of role names.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/deployClientConfig")
  public ApiCommand deployClientConfigCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiRoleNameList roleNames);

  /**
   * Clean up all running server instances of a ZooKeeper service.
   * <p>
   * This command removes snapshots and transaction log files kept by
   * ZooKeeper for backup purposes. Refer to the ZooKeeper documentation
   * for more details.
   *
   * @param serviceName The service to start.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/zooKeeperCleanup")
  public ApiCommand zooKeeperCleanupCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Initializes all the server instances of a ZooKeeper service.
   * <p>
   * ZooKeeper server roles need to be initialized before they
   * can be used.
   *
   * @param serviceName The service to start.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/zooKeeperInit")
  public ApiCommand zooKeeperInitCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Start the service.
   * @param serviceName The service to start.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes(/* No POST body. Explicitly consumes nothing */)
  @Path("/{serviceName}/commands/start")
  public ApiCommand startCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Stop the service.
   * @param serviceName The service to stop.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes(/* No POST body. Explicitly consumes nothing */)
  @Path("/{serviceName}/commands/stop")
  public ApiCommand stopCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Restart the service.
   * @param serviceName The service to start.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes(/* No POST body. Explicitly consumes nothing */)
  @Path("/{serviceName}/commands/restart")
  public ApiCommand restartCommand(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The nameservices resource handler.
   */
  @Path("/{serviceName}/nameservices")
  public NameservicesResource getNameservicesResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The roles resource handler.
   */
  @Path("/{serviceName}/roles")
  public RolesResource getRolesResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * @return The role command resource handler.
   */
  @Path("/{serviceName}/roleCommands")
  public RoleCommandsResource getRoleCommandsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Return the activities resource handler.
   * <p/>
   *
   * @return The activities resource handler
   */
  @Path("/{serviceName}/activities")
  public ActivitiesResource getActivitiesResource(
      @PathParam(SERVICE_NAME) String serviceName);

}
