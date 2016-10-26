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
package com.cloudera.api.v13;

import static com.cloudera.api.Parameters.SERVICE_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v11.ServicesResourceV11;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV13 extends ServicesResourceV11 {
  /**
   * Retrieves the replication resource.
   * Only available with Cloudera Manager Enterprise Edition.
   * @param serviceName The service name.
   * @return The replications resource handler.
   */
  @Path("/{serviceName}/replications")
  @Override
  public ReplicationsResourceV13 getReplicationsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Creates the HDFS directory where YARN container usage metrics are
   * stored by NodeManagers for CM to read and aggregate into app usage metrics.
   * <p>
   * Available since API v13.
   * </p>
   * @param serviceName The YARN service name.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/yarnCreateCmContainerUsageInputDirCommand")
  public ApiCommand createYarnCmContainerUsageInputDirCommand(
      @PathParam(SERVICE_NAME) String serviceName);
}
