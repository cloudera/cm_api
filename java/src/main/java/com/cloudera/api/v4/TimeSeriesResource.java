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

package com.cloudera.api.v4;

import com.cloudera.api.model.ApiMetricSchemaList;
import com.cloudera.api.model.ApiTimeSeriesResponseList;
import static com.cloudera.api.Parameters.DATE_TIME_NOW;
import static com.cloudera.api.Parameters.FROM;
import static com.cloudera.api.Parameters.QUERY;
import static com.cloudera.api.Parameters.TO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface TimeSeriesResource {

  /**
   * Retrieve time-series data from the Cloudera Manager (CM) time-series
   * data store using a tsquery.
   * <p>
   * Please see the
   * <a href="http://tiny.cloudera.com/tsquery_doc">
   * tsquery language documentation</a>.
   * </p>
   * Available since API v4.
   *
   * @param query Tsquery to run against the CM time-series data store.
   * @param from Start of the period to query in ISO 8601 format (defaults to 5 minutes before the
   * end of the period).
   * @param to End of the period to query in ISO 8601 format (defaults to current time).
   *
   * @return List of time series that match the tsquery.
   */
  @GET
  @Path("/")
  public ApiTimeSeriesResponseList queryTimeSeries(
      @QueryParam(QUERY) String query,
      @QueryParam(FROM) String from,
      @QueryParam(TO)
        @DefaultValue(DATE_TIME_NOW)
        String to);

  /**
   * Retrieve schema for all metrics
   * <p/>
   * The schema is fixed for a product version.
   * The schema may change for an API versions
   * <p/>
   * Available since API v4.
   *
   * @return List of metric schema.
   */
  @GET
  @Path("/schema")
  public ApiMetricSchemaList getMetricSchema();
}
