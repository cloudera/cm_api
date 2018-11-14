// Licensed to Cloudera, Inc. under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership. Cloudera, Inc. licenses this file
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
package com.cloudera.api.v11;

import com.cloudera.api.model.ApiTimeSeriesEntityAttributeList;
import com.cloudera.api.model.ApiTimeSeriesEntityTypeList;
import com.cloudera.api.model.ApiTimeSeriesRequest;
import com.cloudera.api.v6.TimeSeriesResourceV6;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

public interface TimeSeriesResourceV11 extends TimeSeriesResourceV6 {
  /**
   * Retrieve all metric entity types monitored by Cloudera Manager.
   * It is guaranteed that parent types appear before their children.
   * <p/>
   * Available since API v11.
   *
   * @return List of time series entity type.
   */
  @GET
  @Path("/entityTypes")
  public ApiTimeSeriesEntityTypeList getEntityTypes();

  /**
   * Retrieve all metric entity type attributes monitored by Cloudera Manager.
   * <p/>
   * Available since API v11.
   *
   * @return List of time series entity attributes.
   */
  @GET
  @Path("/entityTypeAttributes")
  public ApiTimeSeriesEntityAttributeList getEntityTypeAttributes();

  /**
   * Retrieve time-series data from the Cloudera Manager (CM) time-series
   * data store accepting HTTP POST request. This method differs
   * from queryTimeSeries() in v6 that this could accept query strings that are
   * longer than HTTP GET request limit.
   *
   * Available since API v11.
   *
   * @param timeSeriesRequest Request object containing information used when
   * retrieving timeseries data.
   * @return List of time series that match the tsquery.
   */
  @POST
  @Path("/")
  public Response queryTimeSeries(ApiTimeSeriesRequest timeSeriesRequest);
}
