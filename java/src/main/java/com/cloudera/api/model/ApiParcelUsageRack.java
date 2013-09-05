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

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This object is used to represent a rack within an ApiParcelUsage.
 */
@XmlRootElement(name = "parcelUsageRack")
public class ApiParcelUsageRack {
  private String rackId;
  private Set<ApiParcelUsageHost> hosts;

  public ApiParcelUsageRack() {
    // For JAX-B
  }

  /**
   * A collection of the hosts in the rack.
   */
  @XmlElement
  public Set<ApiParcelUsageHost> getHosts() {
    return hosts;
  }

  public void setHosts(Set<ApiParcelUsageHost> hosts) {
    this.hosts = hosts;
  }

  /**
   * The rack ID for the rack.
   */
  @XmlElement
  public String getRackId() {
    return rackId;
  }

  public void setRackId(String rackId) {
    this.rackId = rackId;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("rackId", rackId)
        .add("hosts", hosts)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcelUsageRack that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(rackId, that.rackId) &&
        Objects.equal(hosts, that.hosts));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(rackId, hosts);
  }
}