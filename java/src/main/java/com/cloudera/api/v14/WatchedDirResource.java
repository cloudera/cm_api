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
package com.cloudera.api.v14;

import static com.cloudera.api.Parameters.DIR_PATH;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.cloudera.api.model.ApiWatchedDir;
import com.cloudera.api.model.ApiWatchedDirList;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface WatchedDirResource {

  /**
   * Lists all the watched directories.
   * <p>
   * Available since API v14. Only available with Cloudera Manager Enterprise
   * Edition.
   * <p>
   *
   * @return List of currently watched directories.
   */
  @GET
  @Path("/")
  public ApiWatchedDirList listWatchedDirectories();

  /**
   * Adds a directory to the watching list.
   * <p>
   * Available since API v14. Only available with Cloudera Manager Enterprise
   * Edition.
   * <p>
   *
   * @param dir The directory to be added.
   * @return Added directory.
   */
  @POST
  @Path("/")
  public ApiWatchedDir addWatchedDirectory(ApiWatchedDir dir);

  /**
   * Removes a directory from the watching list.
   * <p>
   * Available since API v14. Only available with Cloudera Manager Enterprise
   * Edition.
   * <p>
   *
   * @param directoryPath The directory path to be removed.
   * @return Removed directory.
   */
  @DELETE
  @Path("/{directoryPath}")
  public ApiWatchedDir removeWatchedDirectory(@PathParam(DIR_PATH) String directoryPath);
}
