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

/**
 * Role info: This will contain information related to a role referred by some
 * configuration. During import type this role must be materizalized.
 *
 */
@JsonInclude(Include.NON_EMPTY)
public class ApiClusterTemplateRole {
  /**
   * Role reference name
   */
  private String refName;
  /**
   * Role type
   */
  private String roleType;

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

}