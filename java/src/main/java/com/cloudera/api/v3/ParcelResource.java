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

import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.model.ApiParcel;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * This interface describes a parcel resource and all the operations that can
 * be performed on it. All the commands in this resource are modeled as
 * follows: When you execute a command, for example startDownload, that command
 * simply starts the download and returns right away. This is why they are
 * labeled as a synchronous commands. In order to see the progress of the command
 * a call to {@link ParcelResource#readParcel()} needs to be made. This will
 * show the current stage and any progress information if the parcel is in a
 * transition stage. Eg. DOWNLOADING, DISTRIBUTING etc.
 * If the command for some reason could not execute properly, an error
 * is returned to the user as part of the synchronous command.
 *
 * A Parcel goes through many stages during its lifecycle. The various stages
 * and their order is described on in {@link ApiParcel}
 */
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ParcelResource {

  /**
   * Retrieves detailed information about a parcel.
   *
   * @return the parcel
   */
  @GET
  @Path("/")
  public ApiParcel readParcel();

  /**
   * A synchronous command that starts the parcel download.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   * In order to see the progress of the download, a call
   * to {@link ParcelResource#readParcel()} needs to be made.
   *
   * @return synchronous command result
   */
  @POST
  @Path("/commands/startDownload")
  @Consumes
  public ApiCommand startDownloadCommand();

  /**
   * A synchronous command that cancels the parcel download.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   *
   * @return synchronous command result
   * @throws IllegalArgumentException if parcel is not in the DOWNLOADING state
   */
  @POST
  @Path("/commands/cancelDownload")
  @Consumes
  public ApiCommand cancelDownloadCommand();

  /**
   * A synchronous command that removes the downloaded parcel.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   *
   * @return synchronous command result
   */
  @POST
  @Path("/commands/removeDownload")
  @Consumes
  public ApiCommand removeDownloadCommand();

  /**
   * A synchronous command that starts the distribution of the parcel
   * to the cluster.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   * In order to see the progress of the distribution, a call
   * to {@link ParcelResource#readParcel()} needs to be made.
   *
   * @return synchronous command result
   */
  @POST
  @Path("/commands/startDistribution")
  @Consumes
  public ApiCommand startDistributionCommand();

  /**
   * A synchronous command that cancels the parcel distribution.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   *
   * @return synchronous command result
   * @throws IllegalArgumentException if parcel is not in the DISTRIBUTING state
   */
  @POST
  @Path("/commands/cancelDistribution")
  @Consumes
  public ApiCommand cancelDistributionCommand();

  /**
   * A synchronous command that removes the distribution from the hosts in
   * the cluster.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   * In order to see the progress of the removal, a call
   * to {@link ParcelResource#readParcel()} needs to be made.
   *
   * @return synchronous command result
   */
  @POST
  @Path("/commands/startRemovalOfDistribution")
  @Consumes
  public ApiCommand startRemovalOfDistributionCommand();

 /**
   * A synchronous command that activates the parcel on the cluster.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   *
   * @return synchronous command result
   */
  @POST
  @Path("/commands/activate")
  @Consumes
  public ApiCommand activateCommand();

   /**
   * A synchronous command that deactivates the parcel on the cluster.
   * <p>
   * Since it is synchronous, the result is known immediately upon return.
   *
   * @return synchronous command result
   */
  @POST
  @Path("/commands/deactivate")
  @Consumes
  public ApiCommand deactivateCommand();
}
