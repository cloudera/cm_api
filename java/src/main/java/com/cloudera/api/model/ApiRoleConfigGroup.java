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
 * A role config group contains roles of the same role type
 * sharing the same configuration. While each role has to belong
 * to a group, a role config group may be empty.
 * 
 * There exists a default role config group for each role type.
 * Default groups cannot be removed nor created. 
 * 
 * The name of a role config group is unique and cannot be changed.
 * 
 * The configuration of individual roles may be overridden on 
 * role level.
 */
@XmlRootElement(name = "roleConfigGroup")
public class ApiRoleConfigGroup {

  private String name;
  private String displayName;
  private String roleType;
  private boolean base;
  private ApiServiceRef serviceRef;
  private ApiConfigList config;

  public ApiRoleConfigGroup() {
    // For JAX-B
  }
  
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("displayName", displayName)
                  .add("roleType", roleType)
                  .add("base", base)
                  .add("serviceRef", serviceRef)
                  .add("config", config)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRoleConfigGroup that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(name, that.name) &&
        Objects.equal(roleType, that.roleType) &&
        Objects.equal(base, that.base) &&
        Objects.equal(serviceRef, that.serviceRef));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, roleType, base, serviceRef);
  }

  /**
   * Readonly. The unique name of this role config group.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Readonly. The type of the roles in this group.
   */
  @XmlElement
  public String getRoleType() {
    return roleType;
  }

  public void setRoleType(String roleType) {
    this.roleType = roleType;
  }

  /**
   * Readonly. Indicates whether this is a base group.
   */
  @XmlElement
  public boolean isBase() {
    return base;
  }

  public void setBase(boolean base) {
    this.base = base;
  }

  /**
   * The configuration for this group. Optional.
   */
  @XmlElement
  public ApiConfigList getConfig() {
    return config;
  }

  public void setConfig(ApiConfigList config) {
    this.config = config;
  }

  /**
   * The display name of this group.
   */
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Readonly. The service reference (service name and cluster name)
   * of this group. 
   */
  @XmlElement
  public ApiServiceRef getServiceRef() {
    return serviceRef;
  }

  public void setServiceRef(ApiServiceRef serviceRef) {
    this.serviceRef = serviceRef;
  }

}