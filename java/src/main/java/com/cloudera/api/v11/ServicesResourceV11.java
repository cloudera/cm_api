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

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_FULL;
import static com.cloudera.api.Parameters.SERVICE_NAME;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiService;
import com.cloudera.api.v10.ServicesResourceV10;
import com.cloudera.api.v4.ReplicationsResourceV4;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV11 extends ServicesResourceV10 {

  @Override
  public RolesResourceV11 getRolesResource(String serviceName);

  /**
   * Retrieves the replication resource.
   * Only available with Cloudera Manager Enterprise Edition.
   * @param serviceName The service name.
   * @return The replications resource handler.
   */
  @Path("/{serviceName}/replications")
  @Override
  public ReplicationsResourceV11 getReplicationsResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Retrieves details information about a service.
   *
   * @param serviceName The service name.
   * @param dataView DataView to materialize. Defaults to 'full'.
   * @return The details of the service.
   */
  @GET
  @Path("/{serviceName}")
  public ApiService readService(
      @PathParam(SERVICE_NAME) String serviceName,
      @DefaultValue(DATA_VIEW_FULL) @QueryParam(DATA_VIEW) DataView dataView);
}
