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
 * A roleRef references a role. Each role is identified by its "roleName",
 * the "serviceName" for the service it belongs to, and the "clusterName" in
 * which the service resides. To operate on the role object,
 * use the API with the those fields as parameters.
 */
@XmlRootElement(name = "roleRef")
public class ApiRoleRef {

  private String clusterName;
  private String serviceName;
  private String roleName;

  public ApiRoleRef() {
    // For JAX-B
  }

  public ApiRoleRef(String clusterName, String serviceName, String roleName) {
    this.clusterName = clusterName;
    this.serviceName = serviceName;
    this.roleName = roleName;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("clusterName", clusterName)
        .add("serviceName", serviceName)
        .add("roleName", roleName)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRoleRef that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(clusterName, that.clusterName) &&
        Objects.equal(serviceName, that.serviceName) &&
        Objects.equal(roleName, that.roleName));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(roleName, serviceName, clusterName);
  }

  @XmlElement
  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  @XmlElement
  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  @XmlElement
  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }

}
