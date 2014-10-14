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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * Arguments used for enable Llama HA command.
 */
@XmlRootElement(name="enableLlamaHaArgs")
public class ApiEnableLlamaHaArguments {

  private String newLlamaHostId;
  private String newLlamaRoleName;
  private String zkServiceName;

  /** HostId of the host on which the second Llama role will be created. */
  @XmlElement
  public String getNewLlamaHostId() {
    return newLlamaHostId;
  }

  public void setNewLlamaHostId(String newLlamaHostId) {
    this.newLlamaHostId = newLlamaHostId;
  }

  /** Name of the second Llama role to be created (optional). */
  @XmlElement
  public String getNewLlamaRoleName() {
    return newLlamaRoleName;
  }

  public void setNewLlamaRoleName(String newLlamaRoleName) {
    this.newLlamaRoleName = newLlamaRoleName;
  }

  /**
   * Name of the ZooKeeper service that will be used for auto-failover.
   * This argument may be omitted if the ZooKeeper dependency for Impala
   * is already configured.
   */
  @XmlElement
  public String getZkServiceName() {
    return zkServiceName;
  }

  public void setZkServiceName(String zkServiceName) {
    this.zkServiceName = zkServiceName;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("newLlamaHostId", newLlamaHostId)
        .add("newLlamaRoleName", newLlamaRoleName)
        .add("zkServiceName", zkServiceName)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiEnableLlamaHaArguments that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(newLlamaHostId, that.getNewLlamaHostId()) &&
        Objects.equal(newLlamaRoleName, that.getNewLlamaRoleName()) &&
        Objects.equal(zkServiceName, that.getZkServiceName()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(newLlamaHostId, newLlamaRoleName, zkServiceName);
  }
}
