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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Service information
 */
@JsonInclude(Include.NON_EMPTY)
public class ApiClusterTemplateService {
  /**
   * Reference name of the service. This could be referred by some
   * configuration.
   */
  private String refName;
  /**
   * Service type
   */
  private String serviceType;
  /**
   * Service level configuration
   */
  private List<ApiClusterTemplateConfig> serviceConfigs = Lists.newArrayList();
  /**
   * All role config groups for that service
   */
  private List<ApiClusterTemplateRoleConfigGroup> roleConfigGroups = Lists.newArrayList();
  /**
   * Service display name.
   */
  private String displayName;
  /**
   * List of roles for this service that are referred by some configuration.
   */
  private List<ApiClusterTemplateRole> roles = Lists.newArrayList();

  public String getRefName() {
    return this.refName;
  }

  public void setRefName(String refName) {
    this.refName = refName;
  }

  public String getServiceType() {
    return this.serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public List<ApiClusterTemplateConfig> getServiceConfigs() {
    return this.serviceConfigs;
  }

  public void setServiceConfigs(List<ApiClusterTemplateConfig> serviceConfigs) {
    this.serviceConfigs = serviceConfigs;
  }

  public List<ApiClusterTemplateRoleConfigGroup> getRoleConfigGroups() {
    return this.roleConfigGroups;
  }

  public void setRoleConfigGroups(List<ApiClusterTemplateRoleConfigGroup> roleConfigGroups) {
    this.roleConfigGroups = roleConfigGroups;
  }

  public List<ApiClusterTemplateRole> getRoles() {
    return this.roles;
  }

  public void setRoles(List<ApiClusterTemplateRole> roles) {
    this.roles = roles;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

}