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
package com.cloudera.api.v6;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiHostInstallArguments;
import com.cloudera.api.v3.ClouderaManagerResourceV3;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResourceV6 extends ClouderaManagerResourceV3 {

  /**
   * Perform installation on a set of hosts.
   * This command installs Cloudera Manager Agent on a
   * set of hosts.
   * <p>
   * Available since API v6.
   *
   * @param hostInstall Hosts to perform installation on
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/commands/hostInstall")
  public ApiCommand hostInstallCommand(
      ApiHostInstallArguments hostInstall);
}