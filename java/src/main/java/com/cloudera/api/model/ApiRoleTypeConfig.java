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

import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Role type configuration information.
 */
@XmlRootElement(name = "roleTypeConfig")
public class ApiRoleTypeConfig extends ApiConfigList {

  private String roleType;

  /** The role type. */
  @XmlElement
  public String getRoleType() {
    return roleType;
  }

  public void setRoleType(String roleType) {
    this.roleType = roleType;
  }

  public boolean equals(Object other) {
    if (!super.equals(other)) {
      return false;
    }

    ApiRoleTypeConfig that = (ApiRoleTypeConfig) other;
    return Objects.equal(roleType, that.getRoleType());
  }

  public int hashCode() {
    return 31 * super.hashCode() + Objects.hashCode(roleType);
  }

}
