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
package com.cloudera.api.v6;

import static com.cloudera.api.Parameters.DATE_TIME_NOW;
import static com.cloudera.api.Parameters.FROM;
import static com.cloudera.api.Parameters.LIMIT;
import static com.cloudera.api.Parameters.OFFSET;
import static com.cloudera.api.Parameters.SERVICE_NAME;
import static com.cloudera.api.Parameters.TO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.model.ApiYarnApplicationAttributeList;
import com.cloudera.api.model.ApiYarnApplicationResponse;
import com.cloudera.api.model.ApiYarnKillResponse;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface YarnApplicationsResource {

  /**
   * Returns a list of applications that satisfy the filter
   * <p>
   * Available since API v6.
   *
   * @param serviceName The name of the service
   * @param filter A filter to apply to the applications. A basic filter tests the
   * value of an attribute and looks something like 'executing = true' or
   * 'user = root'. Multiple basic filters can be combined into a complex
   * expression using standard and / or boolean logic and parenthesis.
   * An example of a complex filter is: 'application_duration > 5s and (user = root or
   * user = myUserName').
   * @param from Returns applications that were active between the from and to times.
   *             Defaults to 5 minutes before the 'to' time.
   * @param to Returns applications that were active between the from and to times.
   *           Defaults to now.
   * @param limit The maximum number of applications to return. Applications will be
   * returned in the following order:
   * <ul>
   *   <li> All executing applications, ordered from longest to shortest running </li>
   *   <li> All completed applications order by end time descending. </li>
   * </ul>
   * @param offset The offset to start returning applications from. This is useful
   * for paging through lists of applications. Note that this has non-deterministic
   * behavior if executing applications are included in the response because they
   * can disappear from the list while paging. To exclude executing applications
   * from the response and a 'executing = false' clause to your filter.

   * @return A list of YARN applications and warnings.
   */
  @GET
  @Path("/")
  public ApiYarnApplicationResponse getYarnApplications(
    @PathParam(SERVICE_NAME) String serviceName,
    @QueryParam("filter") @DefaultValue("") String filter,
    @QueryParam(FROM) String from,
    @QueryParam(TO) @DefaultValue(DATE_TIME_NOW) String to,
    @QueryParam(LIMIT) @DefaultValue("100") int limit,
    @QueryParam(OFFSET) @DefaultValue("0") int offset);

  /**
   * Kills an YARN Application
   * <p>
   * Available since API v6.
   *
   * @param serviceName The name of the service
   * @param applicationId The applicationId to kill
   * @return A warning if there was one. Otherwise null.
   */
  @POST
  // All application ids contain an underscore preceeded and followed by
  // alphanumeric characters and additional underscores.
  @Path("/{applicationId: [a-zA-Z0-9]+_[a-zA-Z0-9_]*}/kill")
  public ApiYarnKillResponse killYarnApplication(
      @PathParam(SERVICE_NAME) String serviceName,
      @PathParam("applicationId") String applicationId);

  /**
   * Returns the list of all attributes that the Service Monitor can associate
   * with YARN applications.
   * <p>
   * Examples of attributes include the user who ran the application and the
   * number of maps completed by the application.
   * <p>
   * These attributes can be used to search for specific YARN applications through
   * the getYarnApplications API. For example the 'user' attribute could be used
   * in the search 'user = root'. If the attribute is numeric it can also be used
   * as a metric in a tsquery (ie, 'select maps_completed from YARN_APPLICATIONS').
   * <p>
   * Note that this response is identical for all YARN services.
   * <p>
   * Available since API v6.
   */
  @GET
  @Path("/attributes")
  public ApiYarnApplicationAttributeList getYarnApplicationAttributes();
}
