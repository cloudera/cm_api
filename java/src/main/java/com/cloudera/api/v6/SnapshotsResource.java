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
package com.cloudera.api.v6;

import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;
import static com.cloudera.api.Parameters.LIMIT;
import static com.cloudera.api.Parameters.OFFSET;
import static com.cloudera.api.Parameters.POLICY_NAME;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiSnapshotCommandList;
import com.cloudera.api.model.ApiSnapshotPolicy;
import com.cloudera.api.model.ApiSnapshotPolicyList;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * Available since API v6. Only available with Cloudera Manager Enterprise
 * Edition.
 */
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface SnapshotsResource {

  /**
   * Creates one or more snapshot policies.
   *
   * @param policies List of the snapshot policies to create.
   * @return List of newly added policies.
   */
  @POST
  @Path("/policies")
  public ApiSnapshotPolicyList createPolicies(ApiSnapshotPolicyList policies);

  /**
   * Returns information for all snapshot policies.
   *
   * @param view The view to materialize.
   * @return List of snapshot policies.
   */
  @GET
  @Path("/policies")
  public ApiSnapshotPolicyList readPolicies(
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT)
      DataView view);

  /**
   * Returns information for a specific snapshot policy.
   *
   * @param policyName Name of an existing snapshot policy.
   * @param view The view to materialize.
   * @return Snapshot policy.
   */
  @GET
  @Path("/policies/{policyName}")
  public ApiSnapshotPolicy readPolicy(
      @PathParam(POLICY_NAME) String policyName,
      @QueryParam(DATA_VIEW)
      @DefaultValue(DATA_VIEW_DEFAULT)
      DataView view);

  /**
   * Updates an existing snapshot policy.
   *
   * @param policyName Name of an existing snapshot policy.
   * @param policy Modified policy.
   * @return The snapshot policy after the update.
   */
  @PUT
  @Path("/policies/{policyName}")
  public ApiSnapshotPolicy updatePolicy(
      @PathParam(POLICY_NAME) String policyName,
      ApiSnapshotPolicy policy);

  /**
   * Deletes an existing snapshot policy.
   *
   * @param policyName Name of an existing snapshot policy.
   * @return The deleted snapshot policy.
   */
  @DELETE
  @Path("/policies/{policyName}")
  public ApiSnapshotPolicy deletePolicy(
      @PathParam(POLICY_NAME) String policyName);

  /**
   * Returns a list of commands triggered by a snapshot policy.
   *
   * @param policyName Name of an existing snapshot policy.
   * @param limit Maximum number of commands to retrieve.
   * @param offset Index of first command to retrieve.
   * @param dataView The view to materialize.
   * @return List of commands for the policy.
   */
  @GET
  @Path("/policies/{policyName}/history")
  ApiSnapshotCommandList readHistory(
      @PathParam(POLICY_NAME) String policyName,
      @QueryParam(LIMIT) @DefaultValue("20") int limit,
      @QueryParam (OFFSET) @DefaultValue("0") int offset,
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView dataView);
}
