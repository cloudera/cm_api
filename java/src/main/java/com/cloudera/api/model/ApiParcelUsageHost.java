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
 * This object is used to represent a host within an ApiParcelUsage.
 */
@XmlRootElement(name = "parcelUsageHost")
public class ApiParcelUsageHost {
  private ApiHostRef hostRef;
  private Set<ApiParcelUsageRole> roles;

  public ApiParcelUsageHost() {
    // For JAX-B
  }

  /**
   * A reference to the corresponding Host object.
   */
  @XmlElement
  public ApiHostRef getHostRef() {
    return hostRef;
  }

  public void setHostRef(ApiHostRef hostRef) {
    this.hostRef = hostRef;
  }

  /**
   * A collection of the roles present on the host.
   */
  @XmlElement
  public Set<ApiParcelUsageRole> getRoles() {
    return roles;
  }

  public void setRoles(Set<ApiParcelUsageRole> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("hostRef", hostRef)
        .add("roles", roles)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcelUsageHost that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(hostRef, that.hostRef) &&
        Objects.equal(roles, that.roles));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(hostRef, roles);
  }
}