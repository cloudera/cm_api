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


import com.cloudera.api.v16.RootResourceV16;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * ApiRootResourceExternal interface is used specifically to generate proxies by CXF for
 * direct consumers of {@link ClouderaManagerClientBuilder}.
 *
 * It must be kept in-sync with {@link ApiRootResource}.
 *
 * In order to reduce memory usage while generating proxies, single RootResource version
 * is returned.
 *
 * On adding a new API version:
 *  - add corresponding getRootV() method with newer version of RootResource as data type
 *  of return parameter
 *  - update return data type of all existing getRootV() methods as well
 */
@Path("/")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
interface ApiRootResourceExternal {

  /**
   * @return The v1 root resource.
   */
  @Path("/v1")
  RootResourceV16 getRootV1();

  /**
   * @return The v2 root resource.
   */
  @Path("/v2")
  RootResourceV16 getRootV2();

  /**
   * @return The v3 root resource.
   */
  @Path("/v3")
  RootResourceV16 getRootV3();

  /**
   * @return The v4 root resource.
   */
  @Path("/v4")
  RootResourceV16 getRootV4();

  /**
   * @return The v5 root resource.
   */
  @Path("/v5")
  RootResourceV16 getRootV5();

  /**
   * @return The v6 root resource.
   */
  @Path("/v6")
  RootResourceV16 getRootV6();

  /**
   * @return The v7 root resource.
   */
  @Path("/v7")
  RootResourceV16 getRootV7();

  /**
   * @return The v8 root resource.
   */
  @Path("/v8")
  RootResourceV16 getRootV8();

  /**
   * @return The v9 root resource.
   */
  @Path("/v9")
  RootResourceV16 getRootV9();

  /**
   * @return The v10 root resource.
   */
  @Path("/v10")
  RootResourceV16 getRootV10();

  /**
   * @return The v11 root resource.
   */
  @Path("/v11")
  RootResourceV16 getRootV11();

  /**
   * @return The v12 root resource.
   */
  @Path("/v12")
  RootResourceV16 getRootV12();

  /**
   * @return The v13 root resource.
   */
  @Path("/v13")
  RootResourceV16 getRootV13();

  /**
   * @return The v14 root resource.
   */
  @Path("/v14")
  RootResourceV16 getRootV14();

  /**
   * @return The v15 root resource.
   */
  @Path("/v15")
  RootResourceV16 getRootV15();

  /**
   * @return The v15 root resource.
   */
  @Path("/v16")
  RootResourceV16 getRootV16();

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
