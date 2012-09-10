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
import com.cloudera.api.model.ApiActivity;
import com.cloudera.api.model.ApiActivityList;
import com.cloudera.api.model.ApiMetricList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

import static com.cloudera.api.Parameters.ACTIVITY_ID;
import static com.cloudera.api.Parameters.CLUSTER_NAME;
import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.SERVICE_NAME;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ActivitiesResource {

  /**
   * Read all activities in the system
   *
   * @param clusterName The name of the cluster
   * @param serviceName The name of the service
   * @param maxResults The maximum number of activities to return.
   * @param resultOffset Specified the offset of activities to return.
   * @param query
   *    The query to perform to find activities in the system. By default,
   *    this call returns top level (i.e. root) activities that have currently
   *    started.
   *    <p>
   *    The query specifies the intersection of a list of constraints,
   *    joined together with semicolons (without spaces). For example:
   *    </p>
   *    <dl>
   *      <dt>status==started;parent==</dt>
   *      <dd>looks for running root activities. This is also the
   *          default query.</dd>
   *      <dt>status==failed;finishTime=gt=2012-04-01T20:30:00.000Z</dt>
   *      <dd>looks for failed activities after the given date time.</dd>
   *      <dt>name==Pi Estimator;startTime=gt=2012-04-01T20:30:00.000Z</dt>
   *      <dd>looks for activities started after the given date time,
   *          with the name of "Pi Estimator".</dd>
   *      <dt>startTime=lt=2012-01-02T00:00:00.000Z;finishTime=ge=2012-01-01T00:00:00.000Z</dt>
   *      <dd>looks for activities that are active on 2012 New Year's Day.
   *          Note that they may start before or finish after that day.</dd>
   *      <dt>status==failed;parent==000014-20120425161321-oozie-joe</dt>
   *      <dd>looks for failed child activities of the given parent
   *          activity id.</dd>
   *      <dt>status==started;metrics.cpu_user=gt=10</dt>
   *      <dd>looks for started activities that are using more than 10 cores
   *          per second.</dd>
   *      <dt>type==hive;metrics.user==bc;finishTime=gt=2012-04-01T20:30:00.000Z</dt>
   *      <dd>looks for all hive queries submitted by user bc after the
   *          given date time.</dd>
   *    </dl>
   *
   *    You may query any fields present in the ApiActivity object. You can
   *    also query by activity metric values using the <em>metrics.*</em>
   *    syntax. Values for date time fields should be ISO8601 timestamps.
   *    <p>
   *    The valid comparators are <em>==</em>, <em>!=</em>, <em>=lt=</em>,
   *    <em>=le=</em>, <em>=ge=</em>, and <em>=gt=</em>.
   *    They stand for "==", "!=", "&lt;", "&lt;=",
   *    "&gt;=", "&gt;" respectively.
   *
   * @param dataView The view of the activities to materialize
   * @return A list of activities
   * @throws java.io.IOException on Activity Monitor i/o failure
   * @throws com.cloudera.api.ServiceLocatorException
   *    when Activity Monitor service connection could not be created
   */
  @GET
  @Path("/")
  public ApiActivityList readActivities(
      @PathParam(CLUSTER_NAME) String clusterName,
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam("maxResults") @DefaultValue("100") Integer maxResults,
      @QueryParam("resultOffset") @DefaultValue("0") Integer resultOffset,
      @QueryParam("query") @DefaultValue("status==started;parent==") String query,
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView)
      throws IOException, ServiceLocatorException;

  /**
   * Returns a specific activity in the system
   *
   * @param clusterName The name of the cluster
   * @param serviceName The name of the service
   * @param activityId  The id of the activity to retrieve
   * @param dataView The view of the activity to materialize
   * @return The Activity object with the specified id
   * @throws java.io.IOException on Activity Monitor i/o failure
   * @throws com.cloudera.api.ServiceLocatorException
   *                             when Activity
   *                             Monitor service connection could not be created
   */
  @GET
  @Path("/{activityId}")
  public ApiActivity readActivity(@PathParam(CLUSTER_NAME)
                                  String clusterName,
                                  @PathParam(SERVICE_NAME)
                                  String serviceName,
                                  @PathParam(ACTIVITY_ID)
                                  String activityId,
                                  @QueryParam(DATA_VIEW)
                                  @DefaultValue(DATA_VIEW_DEFAULT)
                                  DataView dataView)
      throws IOException, ServiceLocatorException;


  /**
   * Returns the child activities
   *
   * @param clusterName The name of the cluster
   * @param serviceName The name of the service
   * @param activityId  The id of the activity
   * @param maxResults The maximum number of activities to return.
   * @param resultOffset Specified the offset of activities to return.
   * @param dataView The view of the children to materialize
   * @return The list of child activities for the specified activity
   * @throws IOException on Activity Monitor i/o failure
   * @throws com.cloudera.api.ServiceLocatorException
   *                     when Activity
   *                     Monitor service connection could not be created
   */
  @GET
  @Path("/{activityId}/children")
  public ApiActivityList readChildActivities(
      @PathParam(CLUSTER_NAME) String clusterName,
      @PathParam(SERVICE_NAME) String serviceName,
      @PathParam(ACTIVITY_ID) String activityId,
      @QueryParam("maxResults") @DefaultValue("100") Integer maxResults,
      @QueryParam("resultOffset") @DefaultValue("0") Integer resultOffset,
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView)
      throws IOException, ServiceLocatorException;

  /**
   * Returns a list of similar activities
   *
   * @param clusterName The name of the cluster
   * @param serviceName The name of the service
   * @param activityId  The id of the activity
   * @param dataView The view of the activities to materialize
   * @return The list of similar activities to the specified activity
   * @throws IOException on Activity Monitor i/o failure
   * @throws com.cloudera.api.ServiceLocatorException
   *                     when Activity
   *                     Monitor service connection could not be created
   */
  @GET
  @Path("/{activityId}/similar")
  public ApiActivityList readSimilarActivities(
      @PathParam(CLUSTER_NAME)
      String clusterName,
      @PathParam(SERVICE_NAME)
      String serviceName,
      @PathParam(ACTIVITY_ID)
      String activityId,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT)
      DataView dataView) throws IOException, ServiceLocatorException;

  /**
   * Fetch metric readings for a particular activity.
   * <p>
   * By default, this call will look up all metrics available for the activity. If
   * only specific metrics are desired, use the <i>metrics</i> parameter.
   * <p>
   * By default, the returned results correspond to a 5 minute window based on
   * the provided end time (which defaults to the current server time). The
   * <i>from</i> and <i>to</i> parameters can be used to control the window
   * being queried. A maximum window of 3 hours is enforced.
   * <p>
   * When requesting a "full" view, aside from the extended properties of the
   * returned metric data, the collection will also contain information about
   * all metrics available for the activity, even if no readings are available
   * in the requested window.
   *
   * @param clusterName The name of the cluster.
   * @param serviceName The name of the service.
   * @param activityId The name of the activity.
   * @param from Start of the period to query.
   * @param to End of the period to query.
   * @param metrics Filter for which metrics to query.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of readings from the monitors.
   * @throws com.cloudera.api.ServiceLocatorException when Activity
   * Monitor service connection could not be created
   */
  @GET
  @Path("/{activityId}/metrics")
  public ApiMetricList getMetrics(
      @PathParam(CLUSTER_NAME) String clusterName,
      @PathParam(SERVICE_NAME) String serviceName,
      @PathParam(ACTIVITY_ID) String activityId,
      @QueryParam("from") String from,
      @QueryParam("to")
      @DefaultValue(DateTimeUtil.NOW_KEYWORD)
      String to,
      @QueryParam("metrics") List<String> metrics,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT)
      DataView dataView) throws ServiceLocatorException;

}
