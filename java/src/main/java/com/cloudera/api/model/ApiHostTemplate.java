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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A host template belongs to a cluster and contains a set of role config
 * groups for slave roles (such as DataNodes and TaskTrackers) from services
 * in the cluster. At most one role config group per role type can be present
 * in a host template. Host templates can be applied to fresh hosts (those with
 * no roles on them) in order to create a role for each of the role groups on
 * each host.
 */
@XmlRootElement(name = "hostTemplate")
public class ApiHostTemplate {
  private String name;
  private ApiClusterRef clusterRef;
  private List<ApiRoleConfigGroupRef> roleConfigGroupRefs;

  public ApiHostTemplate() {
    // For JAX-B
  }
  
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("clusterRef", clusterRef)
                  .add("roleConfigGroups", roleConfigGroupRefs)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHostTemplate that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(name, that.name) &&
        Objects.equal(clusterRef, that.clusterRef));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, clusterRef, roleConfigGroupRefs);
  }

  /**
   * The name of the host template. Unique across clusters.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Readonly. A reference to the cluster the host template belongs to.
   */
  @XmlElement
  public ApiClusterRef getClusterRef() {
    return clusterRef;
  }

  public void setClusterRef(ApiClusterRef clusterRef) {
    this.clusterRef = clusterRef;
  }

  /**
   * The role config groups belonging to this host tempalte.
   */
  @XmlElement
  public List<ApiRoleConfigGroupRef> getRoleConfigGroupRefs() {
    return roleConfigGroupRefs;
  }

  public void setRoleConfigGroups(List<ApiRoleConfigGroupRef> roleConfigGroupRefs) {
    this.roleConfigGroupRefs = roleConfigGroupRefs;
  }
}
