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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

@XmlRootElement(name="enableOozieHaArgs")
public class ApiEnableOozieHaArguments {

  private List<String> newOozieServerHostIds;
  private List<String> newOozieServerRoleNames;
  private String zkServiceName;
  private String loadBalancerHostPort;

  /** IDs of the hosts on which new Oozie Servers will be added. */
  @XmlElement
  public List<String> getNewOozieServerHostIds() {
    return newOozieServerHostIds;
  }

  public void setNewOozieServerHostIds(List<String> newOozieServerHostIds) {
    this.newOozieServerHostIds = newOozieServerHostIds;
  }

  /** 
   * Names of the new Oozie Servers. This is an optional argument,
   * but if provided, it should match the length of host IDs provided.
   */
  @XmlElement
  public List<String> getNewOozieServerRoleNames() {
    return newOozieServerRoleNames;
  }

  public void setNewOozieServerRoleNames(List<String> newOozieServerRoleNames) {
    this.newOozieServerRoleNames = newOozieServerRoleNames;
  }

  /**
   * Name of the ZooKeeper service that will be used for Oozie HA.
   * This is an optional parameter if the Oozie to ZooKeeper dependency is
   * already set in CM.
   */
  @XmlElement
  public String getZkServiceName() {
    return zkServiceName;
  }

  public void setZkServiceName(String zkServiceName) {
    this.zkServiceName = zkServiceName;
  }

  /** 
   * Address of the load balancer used for Oozie HA.
   * This is an optional parameter if this config is already set in CM.
   */
  @XmlElement
  public String getLoadBalancerHostPort() {
    return loadBalancerHostPort;
  }

  public void setLoadBalancerHostPort(String loadBalancerHostPort) {
    this.loadBalancerHostPort = loadBalancerHostPort;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        newOozieServerHostIds,
        newOozieServerRoleNames,
        zkServiceName,
        loadBalancerHostPort);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("newOozieServerHostIds", newOozieServerHostIds)
        .add("newOozieServerRoleNames", newOozieServerRoleNames)
        .add("zkServiceName", zkServiceName)
        .add("loadBalancerHostPort", loadBalancerHostPort)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiEnableOozieHaArguments that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null
        && Objects.equal(this.newOozieServerHostIds, that.getNewOozieServerHostIds())
        && Objects.equal(this.newOozieServerRoleNames, that.getNewOozieServerRoleNames())
        && Objects.equal(this.zkServiceName, that.getZkServiceName())
        && Objects.equal(this.loadBalancerHostPort, that.getLoadBalancerHostPort()));
  }
}
