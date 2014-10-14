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

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.model.ApiImpalaQueryAttributeList;
import com.cloudera.api.v4.ImpalaQueriesResource;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ImpalaQueriesResourceV6 extends ImpalaQueriesResource {

  /**
   * Returns the list of all attributes that the Service Monitor can associate
   * with Impala queries.
   * <p>
   * Examples of attributes include the user who issued the query and the
   * number of HDFS bytes read by the query.
   * <p>
   * These attributes can be used to search for specific Impala queries through
   * the getImpalaQueries API. For example the 'user' attribute could be used
   * in the search 'user = root'. If the attribute is numeric it can also be used
   * as a metric in a tsquery (ie, 'select hdfs_bytes_read from IMPALA_QUERIES').
   * <p>
   * Note that this response is identical for all Impala services.
   * <p>
   * Available since API v6.
   */
  @GET
  @Path("/attributes")
  public ApiImpalaQueryAttributeList getImpalaQueryAttributes();
}
