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

import com.cloudera.api.v13.RootResourceV13;

import javax.ws.rs.Path;

@Path("")
public interface RootResourceV14 extends RootResourceV13 {

  @Override
  @Path("/cm")
  public ClouderaManagerResourceV14 getClouderaManagerResource();

  /**
   * @return The external accounts resource handler.
   */
  @Path("/externalAccounts")
  ExternalAccountsResourceV14 getExternalAccountsResource();

  /**
   * @return The clusters resource handler.
   */
  @Override
  @Path("/clusters")
  public ClustersResourceV14 getClustersResource();
}
