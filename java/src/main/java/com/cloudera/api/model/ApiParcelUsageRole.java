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
 * This object is used to represent a role within an ApiParcelUsage.
 */
@XmlRootElement(name = "parcelUsageRole")
public class ApiParcelUsageRole {
  private ApiRoleRef roleRef;
  private Set<ApiParcelRef> parcelRefs;

  public ApiParcelUsageRole() {
    // For JAX-B
  }

  /**
   * A reference to the corresponding Role object.
   */
  @XmlElement
  public ApiRoleRef getRoleRef() {
    return roleRef;
  }

  public void setRoleRef(ApiRoleRef roleRef) {
    this.roleRef = roleRef;
  }

  /**
   * A collection of references to the parcels being used by the role.
   */
  @XmlElement
  public Set<ApiParcelRef> getParcelRefs() {
    return parcelRefs;
  }

  public void setParcelRefs(Set<ApiParcelRef> parcelRefs) {
    this.parcelRefs = parcelRefs;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("roleRef", roleRef)
        .add("parcelRefs", parcelRefs)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcelUsageRole that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(roleRef, that.roleRef) &&
        Objects.equal(parcelRefs, that.parcelRefs));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(roleRef, parcelRefs);
  }
}