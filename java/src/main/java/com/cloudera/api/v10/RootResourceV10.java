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
package com.cloudera.api.v10;

import com.cloudera.api.v9.RootResourceV9;

import javax.ws.rs.Path;

@Path("")
public interface RootResourceV10 extends RootResourceV9 {

  /**
   * @return The clusters resource handler.
   */
  @Override
  @Path("/clusters")
  public ClustersResourceV10 getClustersResource();

  /**
   * @return The hosts resource handler.
   */
  @Override
  @Path("/hosts")
  public HostsResourceV10 getHostsResource();

  /**
   * @return Audits resource handler.
   */
  @Override
  @Path("/audits")
  public AuditsResourceV10 getAuditsResource();

}
