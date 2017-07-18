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
package com.cloudera.api.v6;

import static com.cloudera.api.Parameters.DATE_TIME_NOW;
import static com.cloudera.api.Parameters.FROM;
import static com.cloudera.api.Parameters.QUERY;
import static com.cloudera.api.Parameters.CONTENT_TYPE;
import static com.cloudera.api.Parameters.DESIRED_ROLLUP;
import static com.cloudera.api.Parameters.MUST_USE_DESIRED_ROLLUP;
import static com.cloudera.api.Parameters.ROLLUP_DEFAULT;
import static com.cloudera.api.Parameters.TO;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.cloudera.api.v4.TimeSeriesResource;

public interface TimeSeriesResourceV6 extends TimeSeriesResource {
  /**
   * Retrieve time-series data from the Cloudera Manager (CM) time-series
   * data store using a tsquery.
   *
   * Please see the
   * <a href="http://tiny.cloudera.com/tsquery_doc">
   * tsquery language documentation</a>.
   * <p/>
   * Available since API v6.
   *
   * @param query Tsquery to run against the CM time-series data store.
   * @param from Start of the period to query in ISO 8601 format (defaults to 5
   * minutes before the end of the period).
   * @param to End of the period to query in ISO 8601 format (defaults to
   * current time).
   * @param contentType to return the response in. The content types
   * "application/json" and "text/csv" are supported. This defaults to
   * "application/json". If "text/csv" is specified then we return one row
   * per time series data point, and we don't return any of the metadata.
   * @param desiredRollup Aggregate rollup level desired for the response data.
   * Valid values are RAW, TEN_MINUTELY, HOURLY, SIX_HOURLY, DAILY, and WEEKLY.
   * Note that if the mustUseDesiredRollup parameter is not set, then the
   * monitoring server can decide to return a different rollup level.
   * @param mustUseDesiredRollup If set then the tsquery will return
   * data with the desired aggregate rollup level.
   * @return List of time series that match the tsquery.
   */
  @GET
  @Path("/")
  public Response queryTimeSeries(
      @QueryParam(QUERY) String query,
      @QueryParam(FROM) String from,
      @QueryParam(TO)
        @DefaultValue(DATE_TIME_NOW)
        String to,
      @QueryParam(CONTENT_TYPE)
        @DefaultValue(MediaType.APPLICATION_JSON)
        String contentType,
      @QueryParam(DESIRED_ROLLUP)
        @DefaultValue(ROLLUP_DEFAULT)
        String desiredRollup,
      @QueryParam(MUST_USE_DESIRED_ROLLUP)
        @DefaultValue("false")
        boolean mustUseDesiredRollup);

  /**
   * @return The dashboards resource handler.
   */
  @Path("/dashboards")
  public DashboardsResource getDashboardsResource();
}
