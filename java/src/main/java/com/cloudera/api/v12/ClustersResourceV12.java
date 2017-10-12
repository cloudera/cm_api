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
package com.cloudera.api.v12;

import static com.cloudera.api.Parameters.CLUSTER_NAME;
import static com.cloudera.api.Parameters.DATA_VIEW;
import static com.cloudera.api.Parameters.DATA_VIEW_DEFAULT;

import com.cloudera.api.DataView;
import com.cloudera.api.model.ApiClusterTemplate;
import com.cloudera.api.model.ApiServiceList;
import com.cloudera.api.v11.ClustersResourceV11;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public interface ClustersResourceV12 extends ClustersResourceV11 {

  /**
   * Export the cluster template for the given cluster. If cluster does not have
   * host templates defined it will export host templates based on roles
   * assignment.
   *
   * @param clusterName cluster name
   * @param exportAutoConfig export configs set by the auto configuration
   * @return Cluster template
   */
  @GET
  @Consumes
  @Path("/{clusterName}/export")
  public ApiClusterTemplate export(
      @PathParam(CLUSTER_NAME) String clusterName,
      @QueryParam("exportAutoConfig") @DefaultValue("false") boolean exportAutoConfig);


  /**
   * List the services that can provide distributed file system (DFS) capabilities in this cluster.
   *
   * @param clusterName cluster name
   * @param view data view required for matching services
   * @return Services that provide DFS capabilities in this cluster
   */
  @GET
  @Path("/{clusterName}/dfsServices")
  public ApiServiceList listDfsServices(
      @PathParam(CLUSTER_NAME) String clusterName,
      @QueryParam(DATA_VIEW) @DefaultValue(DATA_VIEW_DEFAULT) DataView view);
}