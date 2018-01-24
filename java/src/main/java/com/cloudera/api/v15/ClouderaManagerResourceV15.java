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

package com.cloudera.api.v15;

import com.cloudera.api.Parameters;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiConfigList;
import com.cloudera.api.model.ApiShutdownReadiness;
import com.cloudera.api.model.ApiHostNameList;
import com.cloudera.api.v14.ClouderaManagerResourceV14;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface ClouderaManagerResourceV15 extends ClouderaManagerResourceV14 {

  /**
   * Update the Cloudera Manager settings.
   * <p>
   * If a value is set in the given configuration, it will be added to the
   * manager's settings, replacing any existing entry. If a value is unset (its
   * value is null), the existing the setting will be erased.
   * <p>
   * Settings that are not listed in the input will maintain their current
   * values.
   *
   * @param config Settings to update.
   * @param message Optional message describing the changes.
   * @return The updated configuration.
   */
  @PUT
  @Path("/config")
  public ApiConfigList updateConfig(
      ApiConfigList config,
      @QueryParam(Parameters.MESSAGE) String message);

  /**
   * Retrieve Cloudera Manager's readiness for shutdown and destroy.
   * Applications that wish to destroy Cloudera Manager and its managed cluster
   * should poll this API, repeatedly if necessary, to respect its readiness.
   *
   * @param lastActivityTime End time of the last known activity/workload
   * against the managed clusters, in ISO 8601 format.
   * @return Cloudera Manager readiness for shutdown
   */
  @GET
  @Path("/shutdownReadiness")
  public ApiShutdownReadiness getShutdownReadiness(
      @QueryParam("lastActivityTime")
      String lastActivityTime);


  /**
   * Recommission the given hosts. If slave roles support start when decommissioned,
   * start those roles before recommission.
   * All slave roles on the hosts will be recommissioned.
   *
   * Warning: Evolving. This method may change in the future and does not offer standard compatibility guarantees.
   * Recommission the given hosts. If possible, start those roles before recommission. All slave roles on the hosts
   * will be recommissioned.
   * Do not use without guidance from Cloudera.
   *
   * Currently, only HDFS DataNodes will be started by this command.
   * 
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/commands/hostsRecommissionWithStart")
  public ApiCommand hostsRecommissionWithStartCommand(
      ApiHostNameList hostNameList);
}