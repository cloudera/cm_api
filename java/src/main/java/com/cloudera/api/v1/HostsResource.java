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
import com.cloudera.api.model.ApiConfigList;
import com.cloudera.api.model.ApiHost;
import com.cloudera.api.model.ApiHostList;
import com.cloudera.api.model.ApiMetricList;

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
import java.util.Set;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.DATE_TIME_NOW;
import static com.cloudera.api.Parameters.FROM;
import static com.cloudera.api.Parameters.HOST_ID;
import static com.cloudera.api.Parameters.MESSAGE;
import static com.cloudera.api.Parameters.METRICS;
import static com.cloudera.api.Parameters.TO;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface HostsResource {
  /**
   * <p>Create one or more hosts.</p>
   * <p>You must specify at least the
   * hostname and ipAddress in the request objects.
   * If no hostId is specified, it will be set to the
   * hostname.  It is an error to try and create
   * host with the same hostId as another host.</p>
   *
   * @param hosts The list of hosts to create
   * @return The newly created host objects
   */
  @POST
  @Path("/")
  public ApiHostList createHosts(ApiHostList hosts);

  /**
   * Returns the hostIds for all hosts in the system.
   *
   * @param dataView The view to materialize
   * @return A list of hostIds
   */
  @GET
  @Path("/")
  public ApiHostList readHosts(
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT)
      DataView dataView);

  /**
   * Returns a specific Host in the system
   *
   * @param hostId The ID of the host to read
   * @return The Host object with the specified hostId
   */
  @GET
  @Path("/{hostId}")
  public ApiHost readHost(@PathParam(HOST_ID) String hostId);

  /**
   * <p>Update an existing host in the system.</p>
   * <p>Currently, only updating the rackId is supported.  All other fields
   * of the host will be ignored.</p>
   *
   * @param hostId The hostId to update
   * @param host The updated host object.
   * @return The updated host
   */
  @PUT
  @Path("/{hostId}")
  public ApiHost updateHost(@PathParam(HOST_ID) String hostId,
                            ApiHost host);

  /**
   * Delete a host from the system
   *
   * @param hostId The ID of the host to remove
   * @return The deleted Host
   */
  @DELETE
  @Path("/{hostId}")
  public ApiHost deleteHost(@PathParam(HOST_ID) String hostId);

  /**
   * Delete all hosts in the system
   * @return The list of deleted hosts
   */
  @DELETE
  @Path("/")
  public ApiHostList deleteAllHosts();

  /**
   * Retrieves the configuration of a specific host.
   *
   * @param hostId The ID of the host.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of host configuration parameters.
   */
  @GET
  @Path("/{hostId}/config")
  public ApiConfigList readHostConfig(
      @PathParam(HOST_ID) String hostId,
      @QueryParam(DATA_VIEW)
        @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Updates the host configuration with the given values.
   * <p>
   * If a value is set in the given configuration, it will be added
   * to the host's configuration, replacing any existing entries.
   * If a value is unset (its value is null), the existing
   * configuration for the attribute will be erased, if any.
   * <p>
   * Attributes that are not listed in the input will maintain their
   * current values in the configuration.
   *
   * @param hostId The ID of the host.
   * @param message Optional message describing the changes.
   * @param config Configuration changes.
   * @return The new host configuration.
   */
  @PUT
  @Path("/{hostId}/config")
  public ApiConfigList updateHostConfig(
      @PathParam(HOST_ID) String hostId,
      @QueryParam(MESSAGE) String message,
      ApiConfigList config);

  /**
   * Fetch metric readings for a host.
   * <p>
   * By default, this call will look up all metrics available for the host. If
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
   * <p>
   * Host metrics also include per-network interface and per-storage device
   * metrics. Since collecting this data incurs in more overhead, query
   * parameters can be used to choose which network interfaces and storage
   * devices to query, or to these metrics altogether.
   * <p>
   * Storage metrics are collected at different levels; for example, per-disk
   * and per-partition metrics are available. The "storageIds" parameter can be
   * used to filter specific storage IDs.
   * <p>
   * In the returned data, the network interfaces and storage IDs can be
   * identified by looking at the "context" property of the metric objects.
   *
   * @param hostId The host's ID.
   * @param from Start of the period to query.
   * @param to End of the period to query.
   * @param queryNw Whether to query for network interface metrics.
   * @param ifs Network interfaces to query for metrics (default = all).
   * @param queryStorage Whether to query for storage metrics.
   * @param storageIds Storage context IDs to query for metrics (default = all).
   * @param metrics Filter for which metrics to query.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of readings from the monitors.
   */
  @GET
  @Path("/{hostId}/metrics")
  public ApiMetricList getMetrics(
      @PathParam(HOST_ID) String hostId,
      @QueryParam(FROM) String from,
      @QueryParam(TO)
        @DefaultValue(DATE_TIME_NOW)
        String to,
      @QueryParam("queryNw")
        @DefaultValue("true")
        boolean queryNw,
      @QueryParam("ifs") Set<String> ifs,
      @QueryParam("queryStorage")
        @DefaultValue("true")
        boolean queryStorage,
      @QueryParam("storageIds") Set<String> storageIds,
      @QueryParam(METRICS) Set<String> metrics,
      @QueryParam(DATA_VIEW)
        @DefaultValue(DATA_VIEW_DEFAULT)
        DataView dataView);
}
