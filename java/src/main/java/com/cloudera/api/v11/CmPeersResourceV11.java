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
package com.cloudera.api.v11;

import static com.cloudera.api.Parameters.PEER_NAME;
import static com.cloudera.api.Parameters.PEER_TYPE;

import com.cloudera.api.model.ApiCmPeer;
import com.cloudera.api.model.ApiCmPeerType;
import com.cloudera.api.model.ApiCommand;
import com.cloudera.api.v3.CmPeersResource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface CmPeersResourceV11 extends CmPeersResource {
  /**
   * Fetch information about an existing Cloudera Manager peer.
   * <p>
   * Available since API v11. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param peerName  Name of peer to retrieve.
   * @param peerType  Type of peer to retrieve. If null, REPLICATION peer type
   * will be returned.
   * @return Peer information.
   */
  @GET
  @Path("/{peerName}")
  public ApiCmPeer readPeer(@PathParam(PEER_NAME) String peerName,
                            @QueryParam(PEER_TYPE) ApiCmPeerType peerType);

  /**
   * Delete Cloudera Manager peer.
   * <p>
   * An attempt will be made to contact the peer server, so that the configured
   * user can be deleted.. Errors while contacting the remote server are
   * non-fatal.
   * <p>
   * Available since API v11. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param peerName Name of peer to delete.
   * @param peerType Type of peer to delete. If null, REPLICATION peer type will
   * be deleted.
   * @return Information about the deleted peer.
   */
  @DELETE
  @Path("/{peerName}")
  public ApiCmPeer deletePeer(@PathParam(PEER_NAME) String peerName,
                              @QueryParam(PEER_TYPE) ApiCmPeerType peerType);

  /**
   * Test the connectivity of a peer.
   * <p>
   * Available since API v11. Only available with Cloudera Manager Enterprise
   * Edition.
   *
   * @param peerName Name of peer to test.
   * @param peerType Type of peer to test. If null, REPLICATION peer type will
   * be tested.
   * @return Information about the submitted command.
   */
  @POST
  @Consumes
  @Path("/{peerName}/commands/test")
  public ApiCommand testPeer(@PathParam(PEER_NAME) String peerName,
                             @QueryParam(PEER_TYPE) ApiCmPeerType peerType);
}