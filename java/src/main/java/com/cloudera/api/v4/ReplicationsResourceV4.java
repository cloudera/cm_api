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

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiReplicationCommandList;
import com.cloudera.api.v3.ReplicationsResource;
import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.LIMIT;
import static com.cloudera.api.Parameters.OFFSET;
import static com.cloudera.api.Parameters.SCHEDULE_ID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ReplicationsResourceV4 extends ReplicationsResource {

  /**
   * Returns a list of commands triggered by a schedule.
   *
   * @param scheduleId Id of an existing replication schedule.
   * @param limit Maximum number of commands to retrieve.
   * @param offset Index of first command to retrieve.
   * @param dataView The view to materialize.
   * @return List of commands for the schedule.
   */
  @GET
  @Path("/{scheduleId}/history")
  ApiReplicationCommandList readHistory(
      @PathParam(SCHEDULE_ID) long scheduleId,
      @QueryParam(LIMIT) @DefaultValue("20") int limit,
      @QueryParam (OFFSET) @DefaultValue("0") int offset,
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

}
