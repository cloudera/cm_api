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
import com.cloudera.api.model.ApiHostRefList;
import com.cloudera.api.model.ApiHostTemplate;
import com.cloudera.api.model.ApiHostTemplateList;

import static com.cloudera.api.Parameters.HOST_TEMPLATE_NAME;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface HostTemplatesResource {
  
  /**
   * Creates new host templates.
   * <p>
   * Host template names must be unique across clusters.
   * <p>
   * Available since API v3.
   *
   * @param hostTemplates The list of host templates to create.
   * @return The created host templates.
   */
  @POST
  @Path("/")
  public ApiHostTemplateList createHostTemplates(
      ApiHostTemplateList hostTemplates);
  
  /**
   * Lists all host templates in a cluster.
   * <p>
   * Available since API v3.
   * 
   * @return List of host templates in the cluster.
   */
  @GET
  @Path("/")
  public ApiHostTemplateList readHostTemplates();
  
  /**
   * Retrieves information about a host template.
   * <p>
   * Available since API v3.
   * 
   * @return The requested host template.
   */
  @GET
  @Path("/{hostTemplateName}")
  public ApiHostTemplate readHostTemplate(
      @PathParam(HOST_TEMPLATE_NAME) String hostTemplateName);

  /**
   * Updates an existing host template.
   * <p>
   * Can be used to update the role config groups in a host template
   * or rename it. 
   * <p>
   * Available since API v3.
   * 
   * @param hostTemplateName Host template with updated fields.
   * @return The updated host template.
   */
  @PUT
  @Path("/{hostTemplateName}")
  public ApiHostTemplate updateHostTemplate(
      @PathParam(HOST_TEMPLATE_NAME) String hostTemplateName,
      ApiHostTemplate hostTemplate);
  
  /**
   * Deletes a host template.
   * <p>
   * Available since API v3.
   * 
   * @param hostTemplateName Host template to delete.
   * @return Deleted host template.
   */
  @DELETE
  @Path("/{hostTemplateName}")
  public ApiHostTemplate deleteHostTemplate(
      @PathParam(HOST_TEMPLATE_NAME) String hostTemplateName);

  /**
   * Applies a host template to a collection of hosts. This will create a role
   * for each role config group on each of the hosts.
   * <p>
   * The provided hosts must not have any existing roles on them and if the
   * cluster is not using parcels, the hosts must have a CDH version matching
   * that of the cluster version.
   * <p>
   * Available since API v3.
   * 
   * @param hostTemplateName Host template to apply.
   * @param hosts List of hosts to apply the host template to.
   * @param startRoles Whether to start the newly created roles or not.
   * @return Synchronous command result.
   */
  @POST
  @Path("/{hostTemplateName}/commands/applyHostTemplate")
  public ApiCommand applyHostTemplate(
      @PathParam(HOST_TEMPLATE_NAME) String hostTemplateName,
      ApiHostRefList hosts,
      @QueryParam("startRoles") boolean startRoles);
}
