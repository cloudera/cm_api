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

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiHostNameList;
import com.cloudera.api.model.ApiKerberosInfo;
import com.cloudera.api.v16.ClouderaManagerResourceV16;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResourceV17 extends ClouderaManagerResourceV16 {

  /**
   * Provides Cloudera Manager Kerberos information
   *
   * @return Cloudera Manager Kerberos information
   */
  @GET
  @Path("kerberosInfo")
  public ApiKerberosInfo getKerberosInfo();

  /**
   * Delete existing Kerberos credentials.
   * <p>
   * This command will affect all services that have been configured to use
   * Kerberos, and have existing credentials.
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/deleteCredentials")
  public ApiCommand deleteCredentialsCommand();

  /**
   * Decommission the given hosts.
   * All slave roles on the hosts will be offlined or decommissioned with
   * preference being offlined if supported by the service.
   * <p>
   * Currently the offline operation is only supported by HDFS, where
   * the offline operation will put DataNodes into <em>HDFS IN MAINTENANCE</em> state which
   * prevents unnecessary re-replication which could occur if decommissioned.
   * <p>
   * All other roles on the hosts will be stopped.
   * <p>
   * The <em>offlineTimeout</em> parameter is used to specify a timeout for offline. For HDFS, when the
   * timeout expires, the DataNode will automatically transition out of <em>HDFS IN MAINTENANCE</em> state,
   * back to <em>HDFS IN SERVICE</em> state.
   * <p>
   * @param hostNameList list of host names to decommission.
   * @param offlineTimeout offline timeout in seconds. Specify as null to get the default timeout (4 hours).
   * Ignored if service does not support he offline operation.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/commands/hostsOfflineOrDecommission")
  public ApiCommand hostsOfflineOrDecommissionCommand(
      ApiHostNameList hostNameList,
      @QueryParam("offlineTimeout") Long offlineTimeout);
}
