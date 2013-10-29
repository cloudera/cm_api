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
package com.cloudera.api.v4;

import static com.cloudera.api.Parameters.CLUSTER_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiRollingRestartClusterArgs;
import com.cloudera.api.v3.ClustersResourceV3;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV4 extends ClustersResourceV3 {

  /**
   * @return The services resource handler.
   */
  @Override
  @Path("/{clusterName}/services")
  public ServicesResourceV4 getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);
  
  /**
   * Command to do a "best-effort" rolling restart of the given cluster,
   * i.e. it does plain restart of services that cannot be rolling restarted,
   * followed by first rolling restarting non-slaves and then rolling restarting
   * the slave roles of services that can be rolling restarted. The slave restarts
   * are done host-by-host.
   * <p>
   * Available since API v4. Only available with Cloudera Manager Enterprise
   * Edition.
   * 
   * @param clusterName The name of the cluster.
   * @param args Arguments for the rolling restart command.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/rollingRestart")
  public ApiCommand rollingRestart(
      @PathParam(CLUSTER_NAME) String clusterName,
      ApiRollingRestartClusterArgs args);
}
