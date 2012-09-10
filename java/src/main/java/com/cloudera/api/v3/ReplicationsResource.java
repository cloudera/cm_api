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

import static com.cloudera.api.Parameters.SCHEDULE_ID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiReplicationSchedule;
import com.cloudera.api.model.ApiReplicationScheduleList;
import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ReplicationsResource {

  /**
   * Creates one or more replication schedules.
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param schedules List of the replication schedules to create.
   * @return List of newly added replication schedules.
   */
  @POST
  @Path("/")
  public ApiReplicationScheduleList createSchedules(
      ApiReplicationScheduleList schedules);

  /**
   * Returns information for all replication schedules.
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param dataView The view to materialize.
   * @return List of replication schedules.
   */
  @GET
  @Path("/")
  public ApiReplicationScheduleList readSchedules(
      @QueryParam(DATA_VIEW)
        @DefaultValue(DATA_VIEW_DEFAULT)
        DataView dataView);

  /**
   * Returns information for a specific replication schedule.
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param scheduleId Id of an existing replication schedule.
   * @param dataView The view to materialize.
   * @return Replication schedule.
   */
  @GET
  @Path("/{scheduleId}")
  public ApiReplicationSchedule readSchedule(
      @PathParam(SCHEDULE_ID) long scheduleId,
      @QueryParam(DATA_VIEW)
        @DefaultValue(DATA_VIEW_DEFAULT)
        DataView dataView);

  /**
   * Updates an existing replication schedule.
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param scheduleId Id of an existing replication schedule.
   * @return The replication schedule after the update.
   */
  @PUT
  @Path("/{scheduleId}")
  public ApiReplicationSchedule updateSchedule(
      @PathParam(SCHEDULE_ID) long scheduleId,
      ApiReplicationSchedule schedule);

  /**
   * Deletes an existing replication schedule.
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param scheduleId Id of an existing replication schedule.
   * @return The deleted replication schedule.
   */
  @DELETE
  @Path("/{scheduleId}")
  public ApiReplicationSchedule deleteSchedule(
      @PathParam(SCHEDULE_ID) long scheduleId);

  /**
   * Deletes all existing replication schedules.
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @return List of deleted replication schedules.
   */
  @DELETE
  @Path("/")
  public ApiReplicationScheduleList deleteAllSchedules();

  /**
   * Run the schedule immediately.
   * <p>
   * The replication command will be triggered with the configured arguments,
   * and will be recorded in the schedule's history.
   * <p>
   * Available since API v3. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param scheduleId Id of an existing replication schedule.
   * @param dryRun Whether to execute a dry run.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{scheduleId}/run")
  @Consumes
  public ApiCommand runSchedule(@PathParam(SCHEDULE_ID) long scheduleId,
      @QueryParam("dryRun") @DefaultValue("false") boolean dryRun);

}
