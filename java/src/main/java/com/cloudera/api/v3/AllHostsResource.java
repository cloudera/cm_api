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
package com.cloudera.api.v3;

import com.cloudera.api.DataView;
import com.cloudera.api.Parameters;
import com.cloudera.api.model.ApiConfigList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface AllHostsResource {

  /**
   * Retrieve the default configuration for all hosts.
   * <p/>
   * These values will apply to all hosts managed by CM unless overridden
   * at the host level.
   *
   * @param dataView The view of the data to materialize,
   *                  either "summary" or "full".
   * @return List of config values.
   */
  @GET
  @Path("/config")
  ApiConfigList readConfig(
      @QueryParam(Parameters.DATA_VIEW)
        @DefaultValue(Parameters.DATA_VIEW_DEFAULT)
        DataView dataView);

  /**
   * Update the default configuration values for all hosts.
   * <p/>
   * Note that this does not override values set at the host level. It just
   * updates the default values that will be inherited by each host's
   * configuration.
   *
   * @param message Optional message describing the changes.
   * @param config The config values to update.
   * @return Updated list of config values.
   */
  @PUT
  @Path("/config")
  public ApiConfigList updateConfig(
      @QueryParam(Parameters.MESSAGE) String message,
      ApiConfigList config);

}
