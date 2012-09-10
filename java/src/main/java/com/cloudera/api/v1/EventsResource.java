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

import com.cloudera.api.ServiceLocatorException;
import com.cloudera.api.model.ApiEvent;
import com.cloudera.api.model.ApiEventQueryResult;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface EventsResource {

  /**
   * Allows you to query events in the system.
   *
   * @param maxResults The maximum number of events to return.
   * @param resultOffset Specified the offset of events to return.
   * @param query
   *    The query to perform to find events in the system. It accepts
   *    querying the intersection of a list of constraints,
   *    joined together with semicolons (without spaces). For example:
   *    </p>
   *    <dl>
   *      <dt>alert==true</dt>
   *      <dd>looks for alerts.</dd>
   *      <dt>alert==true;attributes.host!=flaky.mysite.com</dt>
   *      <dd>looks for alerts, but exclude those with the host attribute of
   *          "flaky.mysite.com".</dd>
   *      <dt>category==log_event;attributes.log_level==ERROR</dt>
   *      <dd>looks for error log events. Event attribute matching is
   *          case sensitive.</dd>
   *      <dt>attributes.service==hbase1;content==hlog</dt>
   *      <dd>looks for any events from the "hbase1" service that
   *          mention "hlog".</dd>
   *      <dt>attributes.service==hbase1;content!=hlog</dt>
   *      <dd>looks for any events from the "hbase1" service that
   *          do not mention "hlog".<br/>
   *          A query must not contain only negative
   *          constraints (<em>!=</em>). It returns empty results because
   *          there is nothing to perform exclusion on.</dd>
   *      <dt>attributes.role_type==NAMENODE;severity==critical important</dt>
   *      <dd>looks for any important or critical events related to
   *          all NameNodes.</dd>
   *      <dt>severity==critical;timeReceived=ge=2012-05-04T00:00;timeReceived=lt=2012-05-04T00:10</dt>
   *      <dd>looks for critical events received between the given 10 minute
   *          range. <br/>
   *          When polling for events, use <em>timeReceived</em>
   *          instead of <em>timeOccurred</em> because events arrive
   *          out of order.</dd>
   *    </dl>
   *
   *    You may query any fields present in the ApiEvent object. You can
   *    also query by event attribute values using the <em>attributes.*</em>
   *    syntax. Values for date time fields (e.g. <em>timeOccurred</em>,
   *    <em>timeReceived</em>) should be ISO8601 timestamps.
   *    <p>
   *    The other valid comparators are <em>=lt=</em>, <em>=le=</em>,
   *    <em>=ge=</em>, and <em>=gt=</em>. They stand for "&lt;", "&lt;=",
   *    "&gt;=", "&gt;" respectively. These comparators are only applicable
   *    for date time fields.
   *
   * @return The results of the query
   * @throws java.io.IOException If the event server fails to handle request
   */
  @GET
  @Path("/")
  public ApiEventQueryResult readEvents(
      @QueryParam(value = "maxResults")
      @DefaultValue("100") Integer maxResults,
      @QueryParam(value = "resultOffset")
      @DefaultValue("0") Integer resultOffset,
      @QueryParam(value = "query") String query)
      throws IOException, ServiceLocatorException;

  /**
   * Returns a specific event in the system
   *
   * @param eventUUID The UUID of the event to read
   * @return The Event object with the specified UUID
   * @throws java.io.IOException If the event service fail to handle request
   */
  @GET
  @Path("/{eventId}")
  public ApiEvent readEvent(@PathParam(value = "eventId") String eventUUID)
      throws IOException, ServiceLocatorException;

}
