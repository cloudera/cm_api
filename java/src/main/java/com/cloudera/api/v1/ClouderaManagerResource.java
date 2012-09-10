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
package com.cloudera.api.v1;

import com.cloudera.api.DataView;
import com.cloudera.api.Enterprise;
import com.cloudera.api.model.ApiCollectDiagnosticDataArguments;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiCommandList;
import com.cloudera.api.model.ApiConfigList;
import com.cloudera.api.model.ApiLicense;
import com.cloudera.api.model.ApiVersionInfo;
import org.apache.cxf.jaxrs.ext.multipart.Multipart;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClouderaManagerResource {

  /**
   * Retrieve the Cloudera Manager settings.
   *
   * @param dataView The view to materialize, either "summary" or "full".
   * @return The current Cloudera Manager settings.
   */
  @GET
  @Path("/config")
  public ApiConfigList getConfig(
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Update the Cloudera Manager settings.
   * <p>
   * If a value is set in the given configuration, it will be added to the
   * manager's settings, replacing any existing entry. If a value is unset (its
   * value is null), the existing the setting will be erased.
   * <p>
   * Settings that are not listed in the input will maintain their current
   * values.
   *
   * @param config Settings to update.
   * @return The updated configuration.
   */
  @PUT
  @Path("/config")
  public ApiConfigList updateConfig(ApiConfigList config);

  /**
   * Retrieve information about the Cloudera Manager license.
   *
   * @return The current license information.
   */
  @GET
  @Path("/license")
  public ApiLicense readLicense();

  /**
   * Updates the Cloudera Manager license.
   * <p>
   * After a new license is installed, the Cloudera Manager needs to be
   * restarted for the changes to take effect.
   * <p>
   * The license file should be uploaded using a request with content type
   * "multipart/form-data", instead of being encoded into a JSON representation.
   *
   * @param license The license file to install.
   * @return The new license information.
   */
  @POST
  @Path("/license")
  @Consumes(MediaType.MULTIPART_FORM_DATA)
  public ApiLicense updateLicense(
    @Multipart(value = "license") byte[] license);

  /**
   * List active global commands.
   *
   * @param dataView The view of the data to materialize,
   *                 either "summary" or "full".
   * @return A list of active global commands.
   */
  @GET
  @Path("/commands")
  public ApiCommandList listActiveCommands(
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);

  /**
   * Generate missing Kerberos credentials.
   * <p>
   * This command will affect all services that have been configured to use
   * Kerberos, and haven't had their credentials generated yet.
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes()
  @Path("/commands/generateCredentials")
  public ApiCommand generateCredentialsCommand();

  /**
   * Runs the host inspector on the configured hosts.
   *
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/commands/inspectHosts")
  public ApiCommand inspectHostsCommand();

  /**
   * Return the Cloudera Management Services resource.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @return The Cloudera Management Services resource.
   */
  @Enterprise
  @Path("/service")
  public MgmtServiceResource getMgmtServiceResource();

  /**
   * Collect diagnostic data from hosts managed by Cloudera Manager.
   * <p>
   * After the command has completed, the {@link ApiCommand} will contain a
   * resultDataUrl from where you can download the result.
   * <p/>
   * Only available with Cloudera Manager Enterprise Edition.
   *
   * @param args The command arguments.
   * @return Detailed command information.
   */
  @Enterprise
  @POST
  @Path("/commands/collectDiagnosticData")
  public ApiCommand collectDiagnosticDataCommand(
      ApiCollectDiagnosticDataArguments args);

  /**
   * Provides version information of Cloudera Manager itself.
   *
   * @return Version information
   */
  @GET
  @Path("version")
  public ApiVersionInfo getVersion();

  /**
   * Returns the entire contents of the Cloudera Manager log file
   */
  @GET
  @Path("/log")
  @Produces(MediaType.TEXT_PLAIN)
  public InputStream getLog() throws IOException;

}
