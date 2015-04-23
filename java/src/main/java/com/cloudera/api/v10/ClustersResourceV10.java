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

import static com.cloudera.api.Parameters.CLUSTER_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiRollingUpgradeServicesArgs;
import com.cloudera.api.v9.ClustersResourceV9;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV10 extends ClustersResourceV9 {

  /**
   * @return The services resource handler.
   */
  @Override
  @Path("/{clusterName}/services")
  public ServicesResourceV10 getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);

  /**
   * Command to do a rolling upgrade of specific services in the given cluster
   *
   * This command does not handle any services that don't support rolling
   * upgrades. The command will throw an error and not start if upgrade of any
   * such service is requested.
   *
   * This command does not upgrade the full CDH Cluster. You should normally
   * use the upgradeCDH Command for upgrading the cluster. This is primarily
   * helpful if you need to need to recover from an upgrade failure or for
   * advanced users to script an alternative to the upgradeCdhCommand.
   *
   * This command expects the binaries to be available on hosts and activated.
   * It does not change any binaries on the hosts.
   *
   * @param clusterName The name of the cluster.
   * @param args Arguments for the rolling upgrade command.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/rollingUpgrade")
  public ApiCommand rollingUpgrade(
      @PathParam(CLUSTER_NAME) String clusterName,
      ApiRollingUpgradeServicesArgs args);
}
