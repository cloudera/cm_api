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

import com.cloudera.api.model.ApiBulkCommandList;
import com.cloudera.api.model.ApiRoleNameList;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RoleCommandsResource {

  /**
   * Bootstrap HDFS stand-by NameNodes.
   * <p>
   * Submit a request to synchronize HDFS NameNodes with their assigned HA
   * partners. The command requires that the target NameNodes are part of
   * existing HA pairs, which can be accomplished by setting the nameservice
   * configuration parameter in the NameNode's configuration.
   * <p>
   * The HA partner must already be formatted and running for this command
   * to run.
   *
   * @param roleNames The names of the stand-by NameNodes to bootstrap.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/hdfsBootstrapStandBy")
  public ApiBulkCommandList hdfsBootstrapStandByCommand(
      ApiRoleNameList roleNames);

  /**
   * Format HDFS NameNodes.
   * <p>
   * Submit a format request to a list of NameNodes on a service. Note that
   * trying to format a previously formatted NameNode will fail.
   * <p>
   * Note about high availability: when two NameNodes are working in an HA
   * pair, only one of them should be formatted.
   * <p>
   * Bulk command operations are not atomic, and may contain partial failures.
   * The returned list will contain references to all successful commands, and
   * a list of error messages identifying the roles on which the command
   * failed.
   *
   * @param roleNames The names of the NameNodes to format.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/hdfsFormat")
  public ApiBulkCommandList formatCommand(
      ApiRoleNameList roleNames);

  /**
   * Initialize HDFS HA failover controller metadata.
   * <p>
   * The controllers being initialized must already exist and be properly
   * configured. The command will make sure the needed data is initialized
   * for the controller to work.
   * <p>
   * Only one controller per nameservice needs to be initialized.
   *
   * @param roleNames The names of the controllers to initialize.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/hdfsInitializeAutoFailover")
  public ApiBulkCommandList hdfsInitializeAutoFailoverCommand(
      ApiRoleNameList roleNames);

  /**
   * Initialize HDFS NameNodes' shared edit directory.
   * <p>
   * Shared edit directories are used when two HDFS NameNodes are operating as
   * a high-availability pair. This command initializes the shared directory to
   * include the necessary metadata.
   * <p>
   * The provided role names should reflect one of the NameNodes in the
   * respective HA pair; the role must be stopped and its data directory must
   * already have been formatted. The shared edits directory must be empty for
   * this command to succeed.
   *
   * @param roleNames The names of the NameNodes.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/hdfsInitializeSharedDir")
  public ApiBulkCommandList hdfsInitializeSharedDirCommand(
      ApiRoleNameList roleNames);

  /**
   * Create / update the Hue database schema.
   * <p>
   * This command is to be run whenever a new database has been specified or,
   * as necessary, after an upgrade.
   * <p>
   * This request should be sent to Hue servers only.
   *
   * @param roleNames The names of the Hue server roles.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/hueSyncDb")
  public ApiBulkCommandList syncHueDbCommand(
      ApiRoleNameList roleNames);

  /**
   * Refresh a role's data.
   * <p>
   * For MapReduce services, this command should be executed on JobTracker
   * roles. It refreshes the role's queue and node information.
   * <p>
   * For HDFS services, this command should be executed on NameNode roles.
   * It refreshes the NameNode's node list.
   * <p>
   * For Yarn services, this command should be executed on ResourceManager
   * roles. It refreshes the role's queue and node information.
   *
   * @param roleNames The names of the roles.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/refresh")
  public ApiBulkCommandList refreshCommand(
      ApiRoleNameList roleNames);

  /**
   * Cleanup a list of ZooKeeper server roles.
   * <p>
   * This command removes snapshots and transaction log files kept by
   * ZooKeeper for backup purposes. Refer to the ZooKeeper documentation
   * for more details.
   *
   * @param roleNames The names of the roles.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/zooKeeperCleanup")
  public ApiBulkCommandList zooKeeperCleanupCommand(
      ApiRoleNameList roleNames);

  /**
   * Initialize a list of ZooKeeper server roles.
   * <p>
   * This applies to ZooKeeper services from CDH4. Before ZooKeeper server
   * roles can be used, they need to be initialized.
   *
   * @param roleNames The names of the roles.
   * @return A list of submitted commands.
   */
  @POST
  @Path("/zooKeeperInit")
  public ApiBulkCommandList zooKeeperInitCommand(
      ApiRoleNameList roleNames);

  /**
   * Start a set of role instances.
   * <p>
   * Bulk command operations are not atomic, and may contain partial failures.
   * The returned list will contain references to all successful commands, and
   * a list of error messages identifying the roles on which the command
   * failed.
   *
   * @param roleNames The names of the roles to start.
   * @return A reference to the submitted command.
   */
  @POST
  @Path("/start")
  public ApiBulkCommandList startCommand(
      ApiRoleNameList roleNames);

  /**
   * Stop a set of role instances.
   * <p>
   * Bulk command operations are not atomic, and may contain partial failures.
   * The returned list will contain references to all successful commands, and
   * a list of error messages identifying the roles on which the command
   * failed.
   *
   * @param roleNames The role type.
   * @return A reference to the submitted command.
   */
  @POST
  @Path("/stop")
  public ApiBulkCommandList stopCommand(
      ApiRoleNameList roleNames);

  /**
   * Restart a set of role instances
   * <p>
   * Bulk command operations are not atomic, and may contain partial failures.
   * The returned list will contain references to all successful commands, and
   * a list of error messages identifying the roles on which the command
   * failed.
   *
   * @param roleNames The name of the roles to restart.
   * @return A reference to the submitted command.
   */
  @POST
  @Path("/restart")
  public ApiBulkCommandList restartCommand(
      ApiRoleNameList roleNames);

}
