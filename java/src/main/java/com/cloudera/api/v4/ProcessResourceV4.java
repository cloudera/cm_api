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
package com.cloudera.api.v4;

import com.cloudera.api.model.ApiProcess;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.ext.multipart.InputStreamDataSource;

import static com.cloudera.api.Parameters.CONFIG_FILE_NAME;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ProcessResourceV4 {

  /**
   * @return The process associated with this resource.
   * @throws IOException
   */
  @GET
  @Path("/")
  public ApiProcess getProcess();
  
  /**
   * Returns the contents of the specified config file.
   * A multi-level file name (e.g. hadoop-conf/hdfs-site.xml) is
   * acceptable here.
   * 
   * @param configFileName Name of the config file to get.
   * @return Contents of the specified config file
   * @throws IOException
   */
  @GET
  @Path("/configFiles/{configFileName : .*}")
  @Produces(MediaType.APPLICATION_OCTET_STREAM)
  public InputStreamDataSource getConfigFile(
      @PathParam(CONFIG_FILE_NAME) String configFileName);
}
