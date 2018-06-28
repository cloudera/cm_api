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
package com.cloudera.api.v18;

import static com.cloudera.api.Parameters.*;

import com.cloudera.api.model.ApiClusterUtilization;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v17.ClustersResourceV17;

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

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV18 extends ClustersResourceV17 {

  /**
   * @return The services resource handler.
   */
  @Override
  @Path("/{clusterName}/services")
  public ServicesResourceV18 getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Provides the resource utilization of the entire cluster as well as the
   * resource utilization per tenant. Only available with Cloudera Manager Enterprise Edition.
   *
   * @param clusterName cluster name
   * @param from Start of the time range to report utilization in ISO 8601 format.
   * @param to End of the the time range to report utilization in ISO 8601 format (defaults to now).
   * @param tenantType The type of the tenant (POOL or USER).
   * @param daysOfWeek The days of the week for which the user wants to report utilization.
   * Days is a list of number between 1 to 7, where 1 corresponds to Monday,
   * and 7 corrensponds to Sunday. All 7 days are included if this is not specified.
   * @param startHourOfDay The start hour of a day for which the user wants to report utilization.
   * The hour is a number between [0-23]. Default value is 0 if this is not specified.
   * @param endHourOfDay The end hour of a day for which the user wants to report utilization.
   * The hour is a number between [0-23]. Default value is 23 if this is not specified.
   * @return Cluster utilization report resource handler.
   */
  @GET
  @Path("/{clusterName}/utilization")
  public ApiClusterUtilization getUtilizationReport(
      @PathParam(CLUSTER_NAME) String clusterName,
      @QueryParam(FROM) String from,
      @QueryParam(TO) @DefaultValue(DATE_TIME_NOW) String to,
      @QueryParam(TENANT_TYPE) @DefaultValue(POOL) String tenantType,
      @QueryParam(DAYS_OF_WEEK) List<String> daysOfWeek,
      @QueryParam(START_HOUR_OF_DAY) @DefaultValue("0") int startHourOfDay,
      @QueryParam(END_HOUR_OF_DAY) @DefaultValue("23") int endHourOfDay);

  /**
   * Delete existing Kerberos credentials for the cluster.
   * <p>
   * This command will affect all services that have been configured to use
   * Kerberos, and have existing credentials.
   *
   * @param clusterName cluster name
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{clusterName}/commands/deleteCredentials")
  public ApiCommand deleteClusterCredentialsCommand(
      @PathParam(CLUSTER_NAME) String clusterName);
}
