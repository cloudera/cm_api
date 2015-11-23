package com.cloudera.api.v11;
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

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.SCHEDULE_ID;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiReplicationDiagnosticsCollectionArgs;
import com.cloudera.api.model.ApiReplicationScheduleDataLimits;
import com.cloudera.api.model.ApiReplicationScheduleList;
import com.cloudera.api.v4.ReplicationsResourceV4;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ReplicationsResourceV11 extends ReplicationsResourceV4 {

  /**
   * Returns information for all replication schedules.
   * <p>
   * Available since API v11.
   *
   * @param limits Various limits on contents of returned schedules.
   * @return List of replication schedules.
   */
  @GET
  @Path("/")
  public ApiReplicationScheduleList readSchedules(
      @QueryParam("limits") ApiReplicationScheduleDataLimits limits);


  /**
   * Collect diagnostic data for a schedule, optionally for a subset of commands
   * on that schedule, matched by schedule ID.
   *
   * The returned command's resultDataUrl property, upon the commands completion,
   * will refer to the generated diagnostic data.
   * Available since API v11.
   *
   * @param scheduleId Schedule ID
   * @param replicationCollectionArgs Replication collection arguments
   * @param view view to materialize
   * @return Command generated to collect the diagnostics data.
   */
  @POST
  @Path("/{scheduleId}/collectDiagnosticData")
  public ApiCommand collectDiagnosticData(
      @PathParam(SCHEDULE_ID) long scheduleId,
      ApiReplicationDiagnosticsCollectionArgs replicationCollectionArgs,
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView view);
}
