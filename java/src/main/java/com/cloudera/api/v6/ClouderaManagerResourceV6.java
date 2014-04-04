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
import com.cloudera.api.model.ApiLicensedFeatureUsage;
import com.cloudera.api.v3.ClouderaManagerResourceV3;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResourceV6 extends ClouderaManagerResourceV3 {

  /**
   * Begin trial license.
   */
  @POST
  @Consumes
  @Path("/trial/begin")
  public void beginTrial();

  /**
   * End trial license.
   */
  @POST
  @Consumes
  @Path("/trial/end")
  public void endTrial();

  /**
   * Retrieve a summary of licensed feature usage.
   * <p/>
   * This command will return information about what Cloudera Enterprise
   * licensed features are in use in the clusters being managed by this Cloudera
   * Manager, as well as totals for usage across all clusters.
   * <p/>
   * The specific features described can vary between different versions of
   * Cloudera Manager.
   * <p/>
   * Available since API v6.
   */
  @GET
  @Path("/licensedFeatureUsage")
  public ApiLicensedFeatureUsage getLicensedFeatureUsage();

  /**
   * Perform installation on a set of hosts.
   * <p/>
   * This command installs Cloudera Manager Agent on a
   * set of hosts.
   * <p/>
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

  /**
   * Return the Cloudera Management Services resource.
   * <p/>
   *
   * @return The Cloudera Management Services resource.
   */
  @Override
  @Path("/service")
  public MgmtServiceResourceV6 getMgmtServiceResource();
}
