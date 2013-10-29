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

import com.cloudera.api.model.ApiBatchRequest;
import com.cloudera.api.model.ApiBatchResponse;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface BatchResource {

  /**
   * Executes a batch of API requests in one database transaction. If any
   * request fails, execution halts and the transaction is rolled back.
   *
   * @param bReq Batch of request to execute.
   * @return Batch response, containing one element for each executed request
   *         element. If a request element was skipped (i.e. a previous request
   *         element failed), it will not have a corresponding response element.
   */
  @PermitAll
  @POST
  @Path("/")
  public ApiBatchResponse execute(ApiBatchRequest bReq);
}
