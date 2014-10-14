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
package com.cloudera.api.v8;

import com.cloudera.api.Parameters;
import com.cloudera.api.v6.RolesResourceV6;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface RolesResourceV8 extends RolesResourceV6 {

  /**
   * Retrieves the stacks log file, if any, for the role's main process. Note
   * that not all roles support periodic stacks collection.
   *
   * The log files are returned as plain text (type "text/plain").
   *
   * @param roleName The role to fetch stacks logs from.
   * @return Contents of the role's log file.
   */
  @GET
  @Path("/{roleName}/logs/stacks")
  @Produces(MediaType.TEXT_PLAIN)
  public InputStream getStacksLog(
      @PathParam(Parameters.ROLE_NAME) String roleName);

  /**
   * Download a zip-compressed archive of role stacks logs. Note that not all
   * roles support periodic stacks collection.
   *
   * @param roleName The role to fetch the stacks logs bundle from.
   * @return The archive data.
   */
  @GET
  @Path("/{roleName}/logs/stacksBundle")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public InputStream getStacksLogsBundle(
      @PathParam(Parameters.ROLE_NAME) String roleName);
}