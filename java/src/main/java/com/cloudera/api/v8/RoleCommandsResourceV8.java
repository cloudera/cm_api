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

package com.cloudera.api.v8;

import com.cloudera.api.model.ApiBulkCommandList;
import com.cloudera.api.model.ApiRoleNameList;
import com.cloudera.api.v6.RoleCommandsResourceV6;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RoleCommandsResourceV8 extends RoleCommandsResourceV6 {

  /**
   * Run the lsof diagnostic command. This command runs the lsof utility to
   * list a role's open files.
   * <p/>
   * Available since API v8.
   *
   * @param roleNames the names of the roles to lsof.
   * @return List of submitted commands.
   */
  @POST
  @Path("/lsof")
  public ApiBulkCommandList lsof(ApiRoleNameList roleNames);

  /**
   * Run the jstack diagnostic command. The command runs the jstack utility to
   * capture a role's java thread stacks.
   * <p/>
   * Available since API v8.
   *
   * @param roleNames the names of the roles to jstack.
   * @return List of submitted commands.
   */
  @POST
  @Path("/jstack")
  public ApiBulkCommandList jstack(ApiRoleNameList roleNames);

  /**
   * Run the jmapHisto diagnostic command. The command runs the jmap utility to
   * capture a histogram of the objects on the role's java heap.
   * <p/>
   * Available since API v8.
   *
   * @param roleNames the names of the roles to jmap.
   * @return List of submitted commands.
   */
  @POST
  @Path("/jmapHisto")
  public ApiBulkCommandList jmapHisto(ApiRoleNameList roleNames);

  /**
   * Run the jmapDump diagnostic command. The command runs the jmap utility to
   * capture a dump of the role's java heap.
   * <p/>
   * Available since API v8.
   *
   * @param roleNames the names of the roles to jmap.
   * @return List of submitted commands.
   */
  @POST
  @Path("/jmapDump")
  public ApiBulkCommandList jmapDump(ApiRoleNameList roleNames);

}
