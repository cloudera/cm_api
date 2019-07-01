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
package com.cloudera.api.v11;

import com.cloudera.api.model.ApiUserSessionList;
import com.cloudera.api.v1.UsersResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({MediaType.APPLICATION_JSON})
@Produces({MediaType.APPLICATION_JSON})
public interface UsersResourceV11 extends UsersResource {
  /**
   * Return a list of the sessions associated with interactive authenticated
   * users in Cloudera Manager.
   * <p>
   * Note that these sessions are only associated with users who log into the
   * web interface. API users will not appear.
   *
   * @return A list of user sessions
   */
  @GET
  @Path("/sessions")
  public ApiUserSessionList getSessions();
}
