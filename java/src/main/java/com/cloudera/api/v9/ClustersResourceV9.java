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
package com.cloudera.api.v9;

import static com.cloudera.api.Parameters.CLUSTER_NAME;

import com.cloudera.api.model.ApiCdhUpgradeArgs;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v8.ClustersResourceV8;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV9 extends ClustersResourceV8 {

  /**
   * Perform CDH upgrade to the specified version.
   * <p>
   * Allows the following upgrades:
   * <ul>
   * <li>Major upgrades from any CDH 4 to any CDH 5</li>
   * <li>Minor upgrades from any CDH 5 to any CDH 5</li>
   * <li>Maintenance upgrades or downgrades (a.b.x to a.b.y)</li>
   * </ul>
   * <p>
   * If using packages, CDH packages on all hosts of the cluster must be
   * manually upgraded before this command is issued.
   * <p>
   * The command will upgrade the services and their configuration to the
   * version available in the CDH5 distribution.
   * <p>
   * Unless rolling upgrade options are provided, the entire cluster will
   * experience downtime. If rolling upgrade options are provided, command will
   * do a "best-effort" rolling upgrade of the given cluster, i.e. it does
   * plain upgrade of services that cannot be rolling upgraded, followed by
   * first rolling upgrading non-slaves and then rolling restarting the slave
   * roles of services that can be rolling restarted. The slave restarts are
   * done host-by-host.
   * <p>
   * Available since v9. Rolling upgrade is only available with Cloudera
   * Manager Enterprise Edition. A more limited upgrade variant available since
   * v6.
   *
   * @param clusterName The name of the cluster.
   * @param args Arguments for the command. See {@link ApiCdhUpgradeArgs}.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/upgradeCdh")
  public ApiCommand upgradeCdhCommand(
    @PathParam(CLUSTER_NAME) String clusterName,
    ApiCdhUpgradeArgs args);
}
