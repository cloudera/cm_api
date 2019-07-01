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
package com.cloudera.api.v17;

import static com.cloudera.api.Parameters.*;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiRoleNameList;
import com.cloudera.api.v16.ServicesResourceV16;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ServicesResourceV17 extends ServicesResourceV16 {
  /**
   * Offline roles of a service.
   * <p>
   * Currently the offline operation is only supported by HDFS.
   * <p>
   * For HDFS, the offline operation will put DataNodes into <em>HDFS IN MAINTENANCE</em> state which
   * prevents unnecessary re-replication which could occur if decommissioned.
   * <p>
   * The <em>timeout</em> parameter is used to specify a timeout for offline. For HDFS, when the
   * timeout expires, the DataNode will automatically transition out of <em>HDFS IN MAINTENANCE</em>
   * state, back to <em>HDFS IN SERVICE</em> state.
   * <p>
   * @param serviceName The service name.
   * @param roleNames List of role names to offline.
   * @param timeout Offline timeout in seconds. Offlined roles will automatically transition from offline
   * state to normal state after timeout.
   * Specify as null to get the default timeout (4 hours).
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/offline")
  public ApiCommand offlineCommand(
      @PathParam(SERVICE_NAME) String serviceName,
      ApiRoleNameList roleNames,
      @QueryParam("timeout") Long timeout);

  /**
   * Validate the Hive Metastore Schema.
   * <p>
   * This command checks the Hive metastore schema for any errors and corruptions.
   * This command is to be run on two instances:
   * <li>After the Hive Metastore database tables are created.</li>
   * <li>Both before and after upgrading the Hive metastore database schema./li>
   * * <p>
   * Available since API v17.
   *
   * @param serviceName Name of the Hive service on which to run the command.
   * @return Information about the submitted command.
   */
  @POST
  @Path("/{serviceName}/commands/hiveValidateMetastoreSchema")
  public ApiCommand hiveValidateMetastoreSchemaCommand(
      @PathParam(SERVICE_NAME) String serviceName);
}
