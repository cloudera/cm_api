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
import com.cloudera.api.DateTimeUtil;
import com.cloudera.api.Enterprise;
import com.cloudera.api.ServiceLocatorException;
import com.cloudera.api.model.ApiMetricList;
import com.cloudera.api.model.ApiNameservice;
import com.cloudera.api.model.ApiNameserviceList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.NAMESERVICE_NAME;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface NameservicesResource {

  /**
   * List the nameservices of an HDFS service.
   *
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of nameservices.
   */
  @GET
  @Path("/")
  public ApiNameserviceList listNameservices(
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Retrieve information about a nameservice.
   *
   * @param nameservice The nameservice to retrieve.
   * @return Details of the nameservice.
   */
  @GET
  @Path("/{nameservice}")
  public ApiNameservice readNameservice(
      @PathParam(NAMESERVICE_NAME) String nameservice);

  /**
   * Fetch metric readings for a particular nameservice.
   * <p>
   * By default, this call will look up all metrics available. If only specific
   * metrics are desired, use the <i>metrics</i> parameter.
   * <p>
   * By default, the returned results correspond to a 5 minute window based on
   * the provided end time (which defaults to the current server time). The
   * <i>from</i> and <i>to</i> parameters can be used to control the window
   * being queried. A maximum window of 3 hours is enforced.
   * <p>
   * When requesting a "full" view, aside from the extended properties of the
   * returned metric data, the collection will also contain information about
   * all metrics available, even if no readings are available in the requested
   * window.
   *
   * @param nameservice The nameservice.
   * @param from Start of the period to query.
   * @param to End of the period to query.
   * @param metrics Filter for which metrics to query.
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return List of readings from the monitors.
   */
  @Enterprise
  @GET
  @Path("/{nameservice}/metrics")
  public ApiMetricList getMetrics(
      @PathParam(NAMESERVICE_NAME) String nameservice,
      @QueryParam("from") String from,
      @QueryParam("to")
        @DefaultValue(DateTimeUtil.NOW_KEYWORD)
        String to,
      @QueryParam("metrics") List<String> metrics,
      @QueryParam(DATA_VIEW)
        @DefaultValue(DATA_VIEW_DEFAULT)
        DataView dataView) throws ServiceLocatorException;

}
