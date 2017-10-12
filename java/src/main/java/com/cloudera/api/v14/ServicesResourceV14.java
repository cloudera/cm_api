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
package com.cloudera.api.v14;

import static com.cloudera.api.Parameters.SERVICE_NAME;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v13.ServicesResourceV13;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV14 extends ServicesResourceV13 {

  /**
   * Returns the watched directory resource handler.
   *
   * @param serviceName The service name.
   * @return the watched directory resource handler
   */
  @Path("/{serviceName}/watcheddir")
  public WatchedDirResource getWatchedDirResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Returns the nameservices resource handler.
   *
   * @param serviceName The service name.
   * @return The nameservices resource handler.
   */
  @Path("/{serviceName}/nameservices")
  @Override
  public NameservicesResourceV14 getNameservicesResource(
      @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Dump the Oozie Server Database.
   *
   * @param serviceName The name of the service
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/oozieDumpDatabase")
  public ApiCommand oozieDumpDatabaseCommand(
          @PathParam(SERVICE_NAME) String serviceName);

  /**
   * Load the Oozie Server Database from dump.
   *
   * @param serviceName The name of the service
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/{serviceName}/commands/oozieLoadDatabase")
  public ApiCommand oozieLoadDatabaseCommand(
          @PathParam(SERVICE_NAME) String serviceName);
}
