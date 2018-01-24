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

package com.cloudera.api.v18;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v8.MgmtServiceResourceV8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface MgmtServiceResourceV18 extends MgmtServiceResourceV8 {

  /**
   * Return the management roles resource handler.
   *
   * @return The roles resource handler.
   */
  @Path("/roles")
  public MgmtRolesResourceV18 getRolesResource();

  /**
   * Put Cloudera Management Service into maintenance mode. This is a
   * synchronous command. The result is known immediately upon return.
   *
   * <p>Available since API v18.</p>
   *
   * @return Synchronous command result.
   */
  @POST
  @Consumes
  @Path("/commands/enterMaintenanceMode")
  public ApiCommand enterMaintenanceMode();

  /**
   * Take Cloudera Management Service out of maintenance mode. This is a
   * synchronous command. The result is known immediately upon return.
   *
   * <p>Available since API v18.</p>
   *
   * @return Synchronous command result.
   */
  @POST
  @Consumes
  @Path("/commands/exitMaintenanceMode")
  public ApiCommand exitMaintenanceMode();
}
