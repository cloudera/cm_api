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

package com.cloudera.api.v16;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v15.ClouderaManagerResourceV15;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResourceV16 extends ClouderaManagerResourceV15 {

  /**
   * <p>
   * Submit a command to refresh parcels information.
   * </p>
   * <p>
   * This API could be used following two scenarios.<br>
   * - User updated Cloudera Manager's local parcel repository. <br>
   * - User updated remote parcel locations.
   * <p>
   * User wants to invoke this API to make sure that Cloudera Manager gets
   * latest parcels information. User can then monitor the returned command
   * before proceeding to the next step.
   * </p>
   *
   * @return Information about the submitted command.
   */
  @POST
  @Path("/commands/refreshParcelRepos")
  public ApiCommand refreshParcelRepos();
}