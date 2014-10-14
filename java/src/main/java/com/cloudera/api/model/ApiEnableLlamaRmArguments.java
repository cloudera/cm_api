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

import com.cloudera.api.ApiUtils;

import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Arguments used for enable Llama RM command.
 */
@XmlRootElement(name="enableLlamaRmArgs")
public class ApiEnableLlamaRmArguments {

  private String llama1HostId;
  private String llama1RoleName;
  private String llama2HostId;
  private String llama2RoleName;
  private String zkServiceName;
  private boolean skipRestart;

  /** HostId of the host on which the first Llama role will be created. */
  @XmlElement
  public String getLlama1HostId() {
    return llama1HostId;
  }

  public void setLlama1HostId(String llama1HostId) {
    this.llama1HostId = llama1HostId;
  }

  /** Name of the first Llama role to be created (optional). */
  @XmlElement
  public String getLlama1RoleName() {
    return llama1RoleName;
  }

  public void setLlama1RoleName(String llama1RoleName) {
    this.llama1RoleName = llama1RoleName;
  }

  /** HostId of the host on which the second Llama role will be created. */
  @XmlElement
  public String getLlama2HostId() {
    return llama2HostId;
  }

  public void setLlama2HostId(String llama2HostId) {
    this.llama2HostId = llama2HostId;
  }

  /** Name of the second Llama role to be created (optional). */
  @XmlElement
  public String getLlama2RoleName() {
    return llama2RoleName;
  }

  public void setLlama2RoleName(String llama2RoleName) {
    this.llama2RoleName = llama2RoleName;
  }

  /**
   * Name of the ZooKeeper service that will be used for auto-failover.
   * Only relevant when enabling Llama RM in HA mode (i.e., when two
   * Llama roles are being created). This argument may be omitted if
   * the ZooKeeper dependency for Impala is already configured.
   */
  @XmlElement
  public String getZkServiceName() {
    return zkServiceName;
  }

  public void setZkServiceName(String zkServiceName) {
    this.zkServiceName = zkServiceName;
  }

  /**
   * Skip the restart of Yarn, Impala, and their dependent services,
   * and don't deploy client configuration. Default is false (i.e.,
   * by default, the services are restarted and client configuration
   * is deployed).
   */
  @XmlElement
  public boolean getSkipRestart() {
    return skipRestart;
  }

  public void setSkipRestart(boolean skipRestart) {
    this.skipRestart = skipRestart;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("llama1HostId", llama1HostId)
        .add("llama1RoleName", llama1RoleName)
        .add("llama2HostId", llama2HostId)
        .add("llama2RoleName", llama2RoleName)
        .add("zkServiceName", zkServiceName)
        .add("skipRestart", skipRestart)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiEnableLlamaRmArguments that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(llama1HostId, that.getLlama1HostId()) &&
        Objects.equal(llama1RoleName, that.getLlama1RoleName()) &&
        Objects.equal(llama2HostId, that.getLlama2HostId()) &&
        Objects.equal(llama2RoleName, that.getLlama2RoleName()) &&
        Objects.equal(zkServiceName, that.getZkServiceName()) &&
        skipRestart == that.skipRestart);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(llama1HostId, llama1RoleName, llama2HostId,
        llama2RoleName, zkServiceName, skipRestart);
  }
}
