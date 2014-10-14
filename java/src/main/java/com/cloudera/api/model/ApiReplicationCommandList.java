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
package com.cloudera.api.model;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A list of replication commands.
 */
@XmlRootElement(name = "replicationCommandList")
public class ApiReplicationCommandList extends
    ApiListBase<ApiReplicationCommand> {

  public ApiReplicationCommandList() {
    // For JAX-B.
  }

  public ApiReplicationCommandList(List<ApiReplicationCommand> commands) {
    super(commands);
  }

  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  public List<ApiReplicationCommand> getCommands() {
    return values;
  }

  public void setCommands(List<ApiReplicationCommand> commands) {
    this.values = commands;
  }

}
