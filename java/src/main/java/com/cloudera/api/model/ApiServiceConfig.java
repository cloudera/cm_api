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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Service and role type configuration.
 */
@XmlRootElement(name = "serviceConfig")
public class ApiServiceConfig extends ApiConfigList {

  private List<ApiRoleTypeConfig> roleTypeConfigs;

  /** 
   * List of role type configurations.
   * Only available up to API v2.
   */
  @XmlElement
  public List<ApiRoleTypeConfig> getRoleTypeConfigs() {
    return roleTypeConfigs;
  }

  public void setRoleTypeConfigs(List<ApiRoleTypeConfig> roleTypeConfigs) {
    this.roleTypeConfigs = roleTypeConfigs;
  }

  public boolean equals(Object other) {
    if (!super.equals(other)) {
      return false;
    }

    ApiServiceConfig that = (ApiServiceConfig) other;
    return Objects.equal(roleTypeConfigs, that.getRoleTypeConfigs());
  }

  public int hashCode() {
    return 31 * super.hashCode() + Objects.hashCode(roleTypeConfigs);
  }

}