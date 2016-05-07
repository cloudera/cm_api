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
package com.cloudera.api.v11;

import static com.cloudera.api.Parameters.CLUSTER_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiConfigureForKerberosArguments;
import com.cloudera.api.v10.ClustersResourceV10;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV11 extends ClustersResourceV10 {

  /**
   * Command to configure the cluster to use Kerberos for authentication.
   *
   * This command will configure all relevant services on a cluster for
   * Kerberos usage.  This command will trigger a GenerateCredentials command
   * to create Kerberos keytabs for all roles in the cluster.
   *
   * @param clusterName The name of the cluster.
   * @param args Arguments for the configure for kerberos command.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{clusterName}/commands/configureForKerberos")
  public ApiCommand configureForKerberos(
      @PathParam(CLUSTER_NAME) String clusterName,
      ApiConfigureForKerberosArguments args);

  /**
   * @return The services resource handler.
   */
  @Override
  @Path("/{clusterName}/services")
  public ServicesResourceV11 getServicesResource(
      @PathParam(CLUSTER_NAME) String clusterName);
}