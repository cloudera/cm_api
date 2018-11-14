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
package com.cloudera.api.v15;

import static com.cloudera.api.Parameters.*;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiRoleNameList;
import com.cloudera.api.v14.ServicesResourceV14;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV15 extends ServicesResourceV14 {
  /**
   * Start and recommission roles of a service.
   * <p>
   * The list should contain names of slave roles to start and recommission.
   * </p>
   *
   * <p>
   * Warning: Evolving. This method may change in the future and does not offer standard compatibility guarantees.
   * Only support by HDFS.
   * Do not use without guidance from Cloudera.
   * </p>
   *
   * <p>
   * Available since API v15.
   * </p>
   *
   * @param serviceName Name of the service on which to run the command.
   * @param roleNames List of role names to recommision.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/recommissionWithStart")
  public ApiCommand recommissionWithStartCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiRoleNameList roleNames);
}
