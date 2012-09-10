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
import com.cloudera.api.ServiceLocatorException;
import com.cloudera.api.DateTimeUtil;
import com.cloudera.api.model.ApiCommandList;
import com.cloudera.api.model.ApiConfigList;
import com.cloudera.api.model.ApiMetricList;
import com.cloudera.api.model.ApiRole;
import com.cloudera.api.model.ApiRoleList;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.ROLE_NAME;

import java.io.InputStream;
import java.io.IOException;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RolesResource {

  /**
   * Create new roles in a given service.
   *
   * <table>
   *   <thead>
   *     <tr>
   *       <th>Service Type</th>
   *       <th>Available Role Types</th>
   *     </tr>
   *   </thead>
   *   <tbody>
   *     <tr>
   *       <td>HDFS (CDH3)</td>
   *       <td>NAMENODE, DATANODE, SECONDARYNAMENODE, BALANCER, GATEWAY</td>
   *     </tr>
   *     <tr>
   *       <td>HDFS (CDH4)</td>
   *       <td>NAMENODE, DATANODE, SECONDARYNAMENODE, BALANCER, HTTPFS,
   *           FAILOVERCONTROLLER, GATEWAY</td>
   *     </tr>
   *     <tr>
   *       <td>MAPREDUCE</td>
   *       <td>JOBTRACKER, TASKTRACKER, GATEWAY</td>
   *     </tr>
   *     <tr>
   *       <td>HBASE</td>
   *       <td>MASTER, REGIONSERVER, GATEWAY</td>
   *     </tr>
   *     <tr>
   *       <td>YARN</td>
   *       <td>RESROUCEMANAGER, NODEMANAGER, JOBHISTORY, GATEWAY</td>
   *     </tr>
   *     <tr>
   *       <td>OOZIE</td>
   *       <td>OOZIE_SERVER</td>
   *     </tr>
   *     <tr>
   *       <td>ZOOKEEPER</td>
   *       <td>SERVER</td>
   *     </tr>
   *     <tr>
   *       <td>HUE (CDH3)</td>
   *       <td>HUE_SERVER, BEESWAX_SERVER, KT_RENEWER, JOBSUBD</td>
   *     </tr>
   *     <tr>
   *       <td>HUE (CDH4)</td>
   *       <td>HUE_SERVER, BEESWAX_SERVER, KT_RENEWER</td>
   *     </tr>
   *   </tbody>
   *
   * </table>
   *
   *
   * @param roles Roles to create.
   * @return List of created roles.
   */
  @POST
  @Path("/")
  public ApiRoleList createRoles(ApiRoleList roles);

  /**
   * Deletes a role from a given service.
   *
   * @param roleName The role name.
   * @return The details of the deleted role.
   */
  @DELETE
  @Path("/{roleName}")
  public ApiRole deleteRole(
      @PathParam(ROLE_NAME) String roleName);

  /**
   * Lists all roles of a given service.
   *
   * @return List of roles.
   */
  @GET
  @Path("/")
  public ApiRoleList readRoles();

  /**
   * Retrieves detailed information about a role.
   *
   * @param roleName The role name.
   * @return The details of the role.
   */
  @GET
  @Path("/{roleName}")
  public ApiRole readRole(
      @PathParam(ROLE_NAME) String roleName);

  /**
   * Retrieves the configuration of a specific role. Note that the "full" view
   * performs validation on the configuration, which could take a few seconds
   * on a large cluster (around 500 nodes or more).
   *
   * @param roleName The role to look up.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of role configuration parameters.
   */
  @GET
  @Path("/{roleName}/config")
  public ApiConfigList readRoleConfig(
      @PathParam(ROLE_NAME) String roleName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Updates the role configuration with the given values.
   * <p>
   * If a value is set in the given configuration, it will be added
   * to the role's configuration, replacing any existing entries.
   * If a value is unset (its value is null), the existing
   * configuration for the attribute will be erased, if any.
   * <p>
   * Attributes that are not listed in the input will maintain their
   * current values in the configuration.
   *
   * @param roleName The role to modify.
   * @param message Optional message describing the changes.
   * @param config Configuration changes.
   * @return The new service configuration.
   */
  @PUT
  @Path("/{roleName}/config")
  public ApiConfigList updateRoleConfig(
      @PathParam(ROLE_NAME) String roleName,
      @QueryParam(value = "message") String message,
      ApiConfigList config);

  /**
   * Fetch metric readings for a particular role.
   * <p>
   * By default, this call will look up all metrics available for the role. If
   * only specific metrics are desired, use the <i>metrics</i> parameter.
   * <p>
   * By default, the returned results correspond to a 5 minute window based on
   * the provided end time (which defaults to the current server time). The
   * <i>from</i> and <i>to</i> parameters can be used to control the window
   * being queried. A maximum window of 3 hours is enforced.
   * <p>
   * When requesting a "full" view, aside from the extended properties of the
   * returned metric data, the collection will also contain information about
   * all metrics available for the role, even if no readings are available
   * in the requested window.
   *
   * @param roleName The name of the role.
   * @param from Start of the period to query.
   * @param to End of the period to query.
   * @param metrics Filter for which metrics to query.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of readings from the monitors.
   */
  @GET
  @Path("/{roleName}/metrics")
  public ApiMetricList getMetrics(
      @PathParam(ROLE_NAME) String roleName,
      @QueryParam("from") String from,
      @QueryParam("to")
        @DefaultValue(DateTimeUtil.NOW_KEYWORD)
        String to,
      @QueryParam("metrics") List<String> metrics,
      @QueryParam(DATA_VIEW)
        @DefaultValue(DATA_VIEW_DEFAULT)
        DataView dataView) throws ServiceLocatorException;

  /**
   * List active role commands.
   *
   * @param roleName The role to start.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return A list of active role commands.
   */
  @GET
  @Path("/{roleName}/commands")
  public ApiCommandList listActiveCommands(
      @PathParam(ROLE_NAME) String roleName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Retrieves the log file for the role's main process.
   * <p>
   * If the role is not started, this will be the log file associated with
   * the last time the role was run.
   * <p>
   * Log files are returned as plain text (type "text/plain").
   *
   * @param roleName The role to start.
   * @return Contents of the role's log file.
   */
  @GET
  @Path("/{roleName}/logs/full")
  @Produces(MediaType.TEXT_PLAIN)
  public InputStream getFullLog(
      @PathParam(ROLE_NAME) String roleName)
      throws IOException;

  /**
   * Retrieves the role's standard output.
   * <p>
   * If the role is not started, this will be the output associated with
   * the last time the role was run.
   * <p>
   * Log files are returned as plain text (type "text/plain").
   *
   * @param roleName The role to start.
   * @return Contents of the role's standard output.
   */
  @GET
  @Path("/{roleName}/logs/stdout")
  @Produces(MediaType.TEXT_PLAIN)
  public InputStream getStandardOutput(
      @PathParam(ROLE_NAME) String roleName)
      throws IOException;

  /**
   * Retrieves the role's standard error output.
   * <p>
   * If the role is not started, this will be the output associated with
   * the last time the role was run.
   * <p>
   * Log files are returned as plain text (type "text/plain").
   *
   * @param roleName The role to start.
   * @return Contents of the role's standard error output.
   */
  @GET
  @Path("/{roleName}/logs/stderr")
  @Produces(MediaType.TEXT_PLAIN)
  public InputStream getStandardError(
      @PathParam(ROLE_NAME) String roleName)
      throws IOException;

}
