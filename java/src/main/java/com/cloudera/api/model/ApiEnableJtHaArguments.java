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
 * Arguments used for enable JT HA command.
 */
@XmlRootElement(name="enableJtHaArgs")
public class ApiEnableJtHaArguments {

  private String newJtHostId;
  private boolean forceInitZNode = true;
  private String zkServiceName;

  /** Id of host on which second JobTracker role will be added. */
  @XmlElement
  public String getNewJtHostId() {
    return newJtHostId;
  }

  public void setNewJtHostId(String newJtHostId) {
    this.newJtHostId = newJtHostId;
  }

  /** Initialize the ZNode even if it already exists. This can happen if
   * JobTracker HA was enabled before and then disabled. Disable operation
   * doesn't delete this ZNode. Defaults to true.*/
  @XmlElement
  public boolean getForceInitZNode() {
    return forceInitZNode;
  }

  public void setForceInitZNode(boolean forceInitZNode) {
    this.forceInitZNode = forceInitZNode;
  }

  /**
   * Name of the ZooKeeper service that will be used for auto-failover.
   * This is an optional parameter if the MapReduce to ZooKeeper dependency is
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
                  .add("newJtHostId", newJtHostId)
                  .add("forceInitZnode", forceInitZNode)
                  .add("zkServiceName", zkServiceName)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiEnableJtHaArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(newJtHostId, other.getNewJtHostId()) &&
        Objects.equal(zkServiceName, other.getZkServiceName()) &&
        (forceInitZNode == other.getForceInitZNode()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(newJtHostId, forceInitZNode, zkServiceName);
  }

}
