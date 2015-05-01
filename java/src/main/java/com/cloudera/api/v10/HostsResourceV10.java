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

import static com.cloudera.api.Parameters.HOST_ID;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiMigrateRolesArguments;
import com.cloudera.api.v2.HostsResourceV2;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface HostsResourceV10 extends HostsResourceV2 {

  /**
   * Migrate roles to a different host.
   * <p>
   * This command applies only to HDFS NameNode, JournalNode, and Failover
   * Controller roles. In order to migrate these roles:
   * <ul>
   * <li>HDFS High Availability must be enabled, using quorum-based storage.</li>
   * <li>HDFS must not be configured to use a federated nameservice.</li>
   * </ul>
   * <b>Migrating a NameNode or JournalNode role requires cluster downtime</b>.
   * HDFS, along with all of its dependent services, will be stopped at the
   * beginning of the migration process, and restarted at its conclusion.
   * <p>If the active NameNode is selected for migration, a manual failover
   * will be performed before the role is migrated. The role will remain in
   * standby mode after the migration is complete.
   * <p>When migrating a NameNode role, the co-located Failover Controller
   * role must be migrated as well if automatic failover is enabled. The
   * Failover Controller role name must be included in the list of role
   * names to migrate specified in the arguments to this command (it will
   * not be included implicitly). This command does not allow a Failover
   * Controller role to be moved by itself, although it is possible to move
   * a JournalNode independently.
   * <p>
   * Available since API v10.
   *
   * @param hostId The ID of the host on which the roles to migrate currently
   *               reside
   * @param args Arguments for the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{hostId}/commands/migrateRoles")
  public ApiCommand migrateRoles(@PathParam(HOST_ID) String hostId,
      ApiMigrateRolesArguments args);
}
