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

import com.cloudera.api.model.ApiImpalaCancelResponse;
import com.cloudera.api.model.ApiImpalaQueryDetailsResponse;
import com.cloudera.api.model.ApiImpalaQueryResponse;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ImpalaQueriesResource {

  /**
   * Returns a list of queries that satisfy the filter
   * <p>
   * Available since API v4.
   *
   * @param serviceName The name of the service
   * @param query A filter to apply to the queries. A basic filter tests the
   * value of an attribute and looks something like 'rowsFetched = 1' or
   * 'user = root'. Multiple basic filters can be combined into a complex
   * expression using standard and / or boolean logic and parenthesis.
   * An example of a complex filter is: 'query_duration > 5s and (user = root or
   * user = myUserName)'. An example of expected full query string in requested
   * URL is: '?filter=(query_duration > 5s and (user = root or user = myUserName))'.
   * @param from Start of the period to query in ISO 8601 format (defaults to 5
   * minutes before the 'to' time).
   * @param to End of the period to query in ISO 8601 format (defaults to
   * current time).
   * @param limit The maximum number of queries to return. Queries will be
   * returned in the following order:
   * <ul>
   *   <li> All executing queries, ordered from longest to shortest running </li>
   *   <li> All completed queries order by end time descending. </li>
   * </ul>
   * @param offset The offset to start returning queries from. This is useful
   * for paging through lists of queries. Note that this has non-deterministic
   * behavior if executing queries are included in the response because they
   * can disappear from the list while paging. To exclude executing queries
   * from the response and a 'executing = false' clause to your filter.

   * @return A list of impala queries and warnings
   */
  @GET
  @Path("/")
  public ApiImpalaQueryResponse getImpalaQueries(
    @PathParam(SERVICE_NAME) String serviceName,
    @QueryParam("filter") @DefaultValue("") String query,
    @QueryParam(FROM) String from,
    @QueryParam(TO) @DefaultValue(DATE_TIME_NOW) String to,
    @QueryParam(LIMIT) @DefaultValue("100") int limit,
    @QueryParam(OFFSET) @DefaultValue("0") int offset);

  /**
   * Returns details about the query. Not all queries have details, check
   * the detailsAvailable field from the getQueries response.
   * <p>
   * Available since API v4.
   *
   * @param queryId The queryId to get information about
   * @param format There are two valid format parameters:
   * <ul>
   *   <li>
   *   'text': this is a text based, human readable representation of the
   *   Impala runtime profile.
   *   </li>
   *   <li>
   *   'thrift_encoded': this a compact-thrift, base64 encoded representation
   *   of the Impala RuntimeProfile.thrift object. See the Impala documentation
   *   for more details.
   *   </li>
   * </ul>
   * @return The query details
   */
  @GET
  // All query ids are GUIDs of the specified form
  @Path("/{queryId: [a-zA-Z0-9]+:[a-zA-Z0-9]+}")
  public ApiImpalaQueryDetailsResponse getQueryDetails(
      @PathParam("queryId") String queryId,
      @QueryParam("format") @DefaultValue("text") String format);

  /**
   * Cancels an Impala Query.
   * <p>
   * Available since API v4.
   *
   * @param serviceName The name of the service
   * @param queryId The queryId to cancel
   * @return A warning if there was one. Otherwise null.
   */
  @POST
  @Path("/{queryId}/cancel")
  public ApiImpalaCancelResponse cancelImpalaQuery(
      @PathParam(SERVICE_NAME) String serviceName,
      @PathParam("queryId") String queryId);
}
