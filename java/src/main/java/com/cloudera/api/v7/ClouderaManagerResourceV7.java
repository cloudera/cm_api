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
package com.cloudera.api.v7;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiPrincipalList;
import com.cloudera.api.v6.ClouderaManagerResourceV6;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResourceV7 extends ClouderaManagerResourceV6 {

  /**
   * Imports the KDC Account Manager credentials needed by Cloudera
   * Manager to create kerberos principals needed by CDH services.
   *
   * @param username Username of the Account Manager. Full name including the Kerberos
   * realm must be specified.
   * @param password Password for the Account Manager.
   *
   * return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/importAdminCredentials")
  public ApiCommand importAdminCredentials(
      @QueryParam("username") String username,
      @QueryParam("password") String password);

  /**
   * Imports the Kerberos credentials for the specified principal
   * which can then be used to add to a role's keytab by running
   * Generate Credentials command.
   *
   * @param principal Name of the principal. Full name including the Kerberos
   * realm must be specified. If it already exists, it will be overwritten.
   * @param password Password for the Kerberos principal. Cloudera Manager
   * will encrypt the principal and password and use it when needed for a daemon.
   * @param kvno Key-version number of the password.
   *
   * return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/importKerberosPrincipal")
  public ApiCommand importKerberosPrincipal(
      @QueryParam("principal") String principal,
      @QueryParam("password") String password,
      @QueryParam("kvno") Long kvno);

  /**
   * Returns the Kerberos principals needed by the services being managed
   * by Cloudera Manager.
   *
   * @param missingOnly Whether to include only those principals which do
   *                    not already exist in Cloudera Manager's database.
   * @return List of kerberos principals.
   */
  @GET
  @Path("/kerberosPrincipals")
  public ApiPrincipalList getKerberosPrincipals(
      @QueryParam("missingOnly") boolean missingOnly);
}
