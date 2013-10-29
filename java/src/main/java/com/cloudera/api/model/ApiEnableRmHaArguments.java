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
 * Arguments used for enable RM HA command.
 */
@XmlRootElement(name="enableRmHaArgs")
public class ApiEnableRmHaArguments {
  
  private String newRmHostId;
  private String newRmRoleName;
  private String zkServiceName;
  
  /** Id of host on which second ResourceManager role will be added. */
  @XmlElement
  public String getNewRmHostId() {
    return newRmHostId;
  }
  
  public void setNewRmHostId(String newRmHostId) {
    this.newRmHostId = newRmHostId;
  }
  
  /** Name of the second ResourceManager role to be created (Optional) */
  @XmlElement
  public String getNewRmRoleName() {
    return newRmRoleName;
  }
  
  public void setNewRmRoleName(String newRmRoleName) {
    this.newRmRoleName = newRmRoleName;
  }

  /**
   * Name of the ZooKeeper service that will be used for auto-failover.
   * This is an optional parameter if the Yarn to ZooKeeper dependency is
   * already set in CM.
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
                  .add("newRmHostId", newRmHostId)
                  .add("zkServiceName", zkServiceName)
                  .add("newRmRoleName", newRmRoleName)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiEnableRmHaArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(newRmHostId, other.getNewRmHostId()) &&
        Objects.equal(zkServiceName, other.getZkServiceName()) &&
        Objects.equal(newRmRoleName, other.getNewRmRoleName()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(newRmHostId, zkServiceName, newRmRoleName);
  }
}
