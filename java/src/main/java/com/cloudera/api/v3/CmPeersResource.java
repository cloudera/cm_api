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

import com.cloudera.api.model.ApiCmPeer;
import com.cloudera.api.model.ApiCmPeerList;
import com.cloudera.api.model.ApiCommand;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

import static com.cloudera.api.Parameters.PEER_NAME;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface CmPeersResource {

  /**
   * Retrieves all configured Cloudera Manager peers.
   * <p>
   * Available since API v3.
   *
   * @return List of Cloudera Manager peers.
   */
  @GET
  @Path("/")
  public ApiCmPeerList listPeers();

  /**
   * Create a new Cloudera Manager peer.
   * <p>
   * The remote server will be contacted so that a user can be created
   * for use by the new peer. The <i>username</i> and <i>password</i>
   * properties of the provided peer object should contain credentials
   * of a valid admin user on the remote server. A timeout of 10 seconds
   * is enforced when contacting the remote server.
   * <p>
   * It is recommended to run the remote server with TLS enabled, since
   * creating and using peers involve transferring credentials over the
   * network.
   * <p>
   * Available since API v3.
   *
   * @param peer Peer to create (see above).
   * @return Information about the created peer.
   */
  @POST
  @Path("/")
  public ApiCmPeer createPeer(ApiCmPeer peer);

  /**
   * Fetch information about an existing Cloudera Manager peer.
   * <p>
   * Available since API v3.
   *
   * @param peerName Name of peer to retrieve.
   * @return Peer information.
   */
  @GET
  @Path("/{peerName}")
  public ApiCmPeer readPeer(@PathParam(PEER_NAME) String peerName);

  /**
   * Delete Cloudera Manager peer.
   * <p>
   * An attempt will be made to contact the peer server, so that the configured
   * user can be deleted.. Errors while contacting the remote server are
   * non-fatal.
   * <p>
   * Available since API v3.
   *
   * @param peerName Name of peer to delete.
   * @return Information about the deleted peer.
   */
  @DELETE
  @Path("/{peerName}")
  public ApiCmPeer deletePeer(@PathParam(PEER_NAME) String peerName);

  /**
   * Update information for a Cloudera Manager peer.
   * <p>
   * In administrator credentials are provided in the peer information, they
   * will be used to establish new credentials with the remote server. This
   * can be used in case the old credentials are not working anymore. An
   * attempt will be made to delete the old credentials if new ones are
   * successfully created.
   * <p>
   * If changing the peer's URL, an attempt will be made to contact the old
   * Cloudera Manager to delete the existing credentials.
   * <p>
   * Available since API v3.
   *
   * @param peerName Name of peer to update.
   * @param peer Updated peer information.
   * @return The updated peer information.
   */
  @PUT
  @Path("/{peerName}")
  public ApiCmPeer updatePeer(@PathParam(PEER_NAME) String peerName,
      ApiCmPeer peer);

  /**
   * Test the connectivity of a peer.
   * <p>
   * Available since API v3.
   *
   * @param peerName Name of peer to test.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{peerName}/commands/test")
  public ApiCommand testPeer(@PathParam(PEER_NAME) String peerName);

}
