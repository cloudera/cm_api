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

package com.cloudera.api.v10;

import com.cloudera.api.model.ApiBulkCommandList;
import com.cloudera.api.model.ApiRoleNameList;
import com.cloudera.api.v8.RoleCommandsResourceV8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RoleCommandsResourceV10 extends RoleCommandsResourceV8 {
  /**
   * Refresh a role's data.
   * <p>
   * For MapReduce services, this command should be executed on JobTracker
   * roles. It refreshes the role's queue and node information.
   * <p>
   * For HDFS services, this command should be executed on NameNode or
   * DataNode roles. For NameNodes, it refreshes the role's node list.
   * For DataNodes, it refreshes the role's data directory list and other
   * configuration.
   * <p>
   * For YARN services, this command should be executed on ResourceManager
   * roles. It refreshes the role's queue and node information.
   * <p>
   * Available since API v1. DataNode data directories refresh available
   * since API v10.
   *
   * @param roleNames The names of the roles.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/refresh")
  public ApiBulkCommandList refreshCommand(
      ApiRoleNameList roleNames);
}
