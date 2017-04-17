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
package com.cloudera.api;

import com.cloudera.api.v1.RootResourceV1;
import com.cloudera.api.v10.RootResourceV10;
import com.cloudera.api.v11.RootResourceV11;
import com.cloudera.api.v12.RootResourceV12;
import com.cloudera.api.v13.RootResourceV13;
import com.cloudera.api.v14.RootResourceV14;
import com.cloudera.api.v15.RootResourceV15;
import com.cloudera.api.v16.RootResourceV16;
import com.cloudera.api.v2.RootResourceV2;
import com.cloudera.api.v3.RootResourceV3;
import com.cloudera.api.v4.RootResourceV4;
import com.cloudera.api.v5.RootResourceV5;
import com.cloudera.api.v6.RootResourceV6;
import com.cloudera.api.v7.RootResourceV7;
import com.cloudera.api.v8.RootResourceV8;
import com.cloudera.api.v9.RootResourceV9;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Root resource for the API. Provides access to the version-specific
 * resources.
 */
@Path("/")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ApiRootResource {

  /**
   * @return The v1 root resource.
   */
  @Path("/v1")
  RootResourceV1 getRootV1();

  /**
   * @return The v2 root resource.
   */
  @Path("/v2")
  RootResourceV2 getRootV2();

  /**
   * @return The v3 root resource.
   */
  @Path("/v3")
  RootResourceV3 getRootV3();

  /**
   * @return The v4 root resource.
   */
  @Path("/v4")
  RootResourceV4 getRootV4();

  /**
   * @return The v5 root resource.
   */
  @Path("/v5")
  RootResourceV5 getRootV5();

  /**
   * @return The v6 root resource.
   */
  @Path("/v6")
  RootResourceV6 getRootV6();

  /**
   * @return The v7 root resource.
   */
  @Path("/v7")
  RootResourceV7 getRootV7();

  /**
   * @return The v8 root resource.
   */
  @Path("/v8")
  RootResourceV8 getRootV8();

  /**
   * @return The v9 root resource.
   */
  @Path("/v9")
  RootResourceV9 getRootV9();

  /**
   * @return The v10 root resource.
   */
  @Path("/v10")
  RootResourceV10 getRootV10();

  /**
   * @return The v11 root resource.
   */
  @Path("/v11")
  RootResourceV11 getRootV11();

  /**
   * @return The v12 root resource.
   */
  @Path("/v12")
  RootResourceV12 getRootV12();

  /**
   * @return The v13 root resource.
   */
  @Path("/v13")
  RootResourceV13 getRootV13();

  /**
   * @return The v14 root resource.
   */
  @Path("/v14")
  RootResourceV14 getRootV14();

  /**
   * @return The v15 root resource.
   */
  @Path("/v15")
  RootResourceV15 getRootV15();

  /**
   * @return The v15 root resource.
   */
  @Path("/v16")
  RootResourceV16 getRootV16();

  /**
   * Important: Update {@link ApiRootResourceExternal} interface as well
   * on adding new CM API version.
   */

  /**
   * Fetch the current API version supported by the server.
   * <p>
   * Available since API v2.
   *
   * @return The current API version (e.g., "v2").
   */
  @GET
  @Path("/version")
  @Consumes()
  @Produces({ MediaType.APPLICATION_JSON, MediaType.TEXT_PLAIN })
  String getCurrentVersion();

}
