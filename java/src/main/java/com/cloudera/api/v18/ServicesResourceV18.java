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

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiDisableSentryHaArgs;
import com.cloudera.api.model.ApiEnableSentryHaArgs;
import com.cloudera.api.model.ApiImpalaUtilization;
import com.cloudera.api.model.ApiYarnUtilization;
import com.cloudera.api.v17.ServicesResourceV17;

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
public interface ServicesResourceV18 extends ServicesResourceV17{

  /**
   * Enable high availability (HA) for Sentry service.
   * <p>
   * This command only applies to CDH 5.13+ Sentry services.
   * <p>
   * The command will create a new Sentry server on the specified host
   * and set the ZooKeeper configs needed for Sentry HA.
   * <p>
   * As part of enabling HA, all services that depend on HDFS will be
   * restarted after enabling Sentry HA.
   * <p>
   * Note: Sentry doesn't support Rolling Restart.
   *
   * @param serviceName A String representing the Sentry service name.
   * @param args An instance of {@link ApiEnableSentryHaArgs}
   * representing the arguments to the command.
   * @return the created command instance
   */
  @POST
  @Path("/{serviceName}/commands/enableSentryHa")
  ApiCommand enableSentryHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiEnableSentryHaArgs args);

  /**
   * Disable high availability (HA) for Sentry service.
   * <p>
   * This command only applies to CDH 5.13+ Sentry services.
   * <p>
   * The command will keep exactly one Sentry server, on the specified host,
   * and update the ZooKeeper configs needed for Sentry.
   * <p>
   * All services that depend on HDFS will be restarted after enabling Sentry HA.
   * <p>
   * Note: Sentry doesn't support Rolling Restart.
   *
   * @param serviceName A String representing the Sentry service name.
   * @param args An instance of {@link ApiDisableSentryHaArgs}
   * representing the arguments to the command.
   * @return the created command instance
   */
  @POST
  @Path("/{serviceName}/commands/disableSentryHa")
  ApiCommand disableSentryHaCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiDisableSentryHaArgs args);

  /**
   * Provides the resource utilization of the yarn service as well as the
   * resource utilization per tenant. Only available with Cloudera Manager Enterprise Edition.
   *
   * @param serviceName service name
   * @param from Start of the time range to report utilization in ISO 8601 format.
   * @param to End of the the time range to report utilization in ISO 8601 format (defaults to now).
   * @param tenantType The type of the tenant (POOL or USER).
   * @param daysOfWeek The days of the week for which the user wants to report utilization.
   * Days is a list of number between 1 to 7, where 1 corresponds to Mon. and 7 corresponds to Sun.
   * All 7 days are included if this is not specified.
   * @param startHourOfDay The start hour of a day for which the user wants to report utilization.
   * The hour is a number between [0-23]. Default value is 0 if this is not specified.
   * @param endHourOfDay The end hour of a day for which the user wants to report utilization.
   * The hour is a number between [0-23]. Default value is 23 if this is not specified.
   * @return utilization report of yarn service.
   */
  @GET
  @Path("/{serviceName}/yarnUtilization")
  public ApiYarnUtilization getYarnUtilization(
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam(FROM) String from,
      @QueryParam(TO) @DefaultValue(DATE_TIME_NOW) String to,
      @QueryParam(TENANT_TYPE) @DefaultValue("POOL") String tenantType,
      @QueryParam(DAYS_OF_WEEK) List<String> daysOfWeek,
      @QueryParam(START_HOUR_OF_DAY) @DefaultValue("0") int startHourOfDay,
      @QueryParam(END_HOUR_OF_DAY) @DefaultValue("23") int endHourOfDay);

  /**
   * Provides the resource utilization of the Impala service as well as the
   * resource utilization per tenant. Only available with Cloudera Manager Enterprise Edition.
   *
   * @param serviceName service name
   * @param from Start of the time range to report utilization in ISO 8601 format.
   * @param to End of the the time range to report utilization in ISO 8601 format (defaults to now).
   * @param tenantType The type of the tenant (POOL or USER).
   * @param daysOfWeek The days of the week for which the user wants to report utilization.
   * Days is a list of number between 1 to 7, where 1 corresponds to Mon. and 7 corresponds to Sun.
   * All 7 days are included if this is not specified.
   * @param startHourOfDay The start hour of a day for which the user wants to report utilization.
   * The hour is a number between [0-23]. Default value is 0 if this is not specified.
   * @param endHourOfDay The end hour of a day for which the user wants to report utilization.
   * The hour is a number between [0-23]. Default value is 23 if this is not specified.
   * @return utilization report of Impala service.
   */
  @GET
  @Path("/{serviceName}/impalaUtilization")
  public ApiImpalaUtilization getImpalaUtilization(
      @PathParam(SERVICE_NAME) String serviceName,
      @QueryParam(FROM) String from,
      @QueryParam(TO) @DefaultValue(DATE_TIME_NOW) String to,
      @QueryParam(TENANT_TYPE) @DefaultValue("POOL") String tenantType,
      @QueryParam(DAYS_OF_WEEK) List<String> daysOfWeek,
      @QueryParam(START_HOUR_OF_DAY) @DefaultValue("0") int startHourOfDay,
      @QueryParam(END_HOUR_OF_DAY) @DefaultValue("23") int endHourOfDay);

  /**
   * Retrieves the replication resource.
   * Only available with Cloudera Manager Enterprise Edition.
   * @param serviceName The service name.
   * @return The replications resource handler.
   */
  @Path("/{serviceName}/replications")
  @Override
  public ReplicationsResourceV18 getReplicationsResource(
      @PathParam(SERVICE_NAME) String serviceName);
}
