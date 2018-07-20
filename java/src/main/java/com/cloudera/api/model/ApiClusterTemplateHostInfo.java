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
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * This contains information about the host or host range on which provided
 * host template will be applied.
 */
@JsonInclude(Include.NON_EMPTY)
public class ApiClusterTemplateHostInfo {
  /**
   * Host name
   */
  private String hostName;
  /**
   * Host range. Either this this or host name must be provided.
   */
  private String hostNameRange;
  /**
   * Rack Id
   */
  private String rackId;
  /**
   * Pointing to the host template reference in the cluster template.
   */
  private String hostTemplateRefName;
  /**
   * This will used to resolve the roles defined in the cluster template. This
   * roleRefName will be used to connect this host with that a role referrence
   * defined in cluster template.
   */
  private Set<String> roleRefNames = Sets.newHashSet();

  public String getHostName() {
    return this.hostName;
  }

  public ApiClusterTemplateHostInfo setHostName(String hostName) {
    this.hostName = hostName;
    return this;
  }

  public String getHostNameRange() {
    return this.hostNameRange;
  }

  public void setHostNameRange(String hostNameRange) {
    this.hostNameRange = hostNameRange;
  }

  public String getRackId() {
    return this.rackId;
  }

  public void setRackId(String rackId) {
    this.rackId = rackId;
  }

  public String getHostTemplateRefName() {
    return this.hostTemplateRefName;
  }

  public void setHostTemplateRefName(String hostTemplateRefName) {
    this.hostTemplateRefName = hostTemplateRefName;
  }

  public Set<String> getRoleRefNames() {
    return this.roleRefNames;
  }

  public void setRoleRefNames(Set<String> roleRefNames) {
    this.roleRefNames = roleRefNames;
  }

}