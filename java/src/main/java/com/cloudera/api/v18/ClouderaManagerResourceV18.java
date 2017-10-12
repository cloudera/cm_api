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

package com.cloudera.api.v18;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v17.ClouderaManagerResourceV17;

import static com.cloudera.api.Parameters.DELETE_CREDENTIALS_MODE;
import static com.cloudera.api.Parameters.DELETE_CREDENTIALS_MODE_DEFAULT;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResourceV18 extends ClouderaManagerResourceV17 {

  /**
   * Return the Cloudera Management Services resource.
   *
   * @return The Cloudera Management Services resource.
   */
  @Path("/service")
  public MgmtServiceResourceV18 getMgmtServiceResource();

  /**
   * Delete existing Kerberos credentials.
   * <p>
   * This command will affect all services that have been configured to use
   * Kerberos, and have existing credentials. In V18 this takes a new
   * paramater to determine whether it needs to delete all credentials
   * or just unused ones.
   *
   * @param mode this can be set to "all" or "unused"
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/deleteCredentials")
  public ApiCommand deleteCredentialsCommand(
      @QueryParam(DELETE_CREDENTIALS_MODE)
      @DefaultValue(DELETE_CREDENTIALS_MODE_DEFAULT) String mode);
}
