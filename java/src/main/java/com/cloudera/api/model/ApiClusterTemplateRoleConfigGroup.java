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
 * Role config group info.
 */
@JsonInclude(Include.NON_EMPTY)
public class ApiClusterTemplateRoleConfigGroup {
  /**
   * The reference name of the role config.
   */
  private String refName;
  /**
   * Role type
   */
  private String roleType;
  /**
   * If true then it is the base config group for that role. There can only be
   * one base role config group for a given role type.
   */
  @JsonInclude(Include.NON_DEFAULT)
  private boolean isBase;
  /**
   * Role config group display name
   */
  private String displayName;
  /**
   * List of configurations
   */
  private List<ApiClusterTemplateConfig> configs = Lists.newArrayList();

  public String getRefName() {
    return this.refName;
  }

  public void setRefName(String refName) {
    this.refName = refName;
  }

  public String getRoleType() {
    return this.roleType;
  }

  public void setRoleType(String roleType) {
    this.roleType = roleType;
  }

  public boolean isBase() {
    return this.isBase;
  }

  public void setBase(boolean isBase) {
    this.isBase = isBase;
  }

  public String getDisplayName() {
    return this.displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public List<ApiClusterTemplateConfig> getConfigs() {
    return this.configs;
  }

  public void setConfigs(List<ApiClusterTemplateConfig> configs) {
    this.configs = configs;
  }

}