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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Details of cluster template
 */

@XmlRootElement(name = "clusterTemplateInstantiator")
@JsonInclude(Include.NON_EMPTY)
public class ApiClusterTemplateInstantiator {

  /**
   * Cluster name
   */
  private String clusterName;
  /**
   * All the hosts that are part of that cluster
   */
  private List<ApiClusterTemplateHostInfo> hosts = Lists.newArrayList();
  /**
   * All the variables the are referred by the cluster template
   */
  private List<ApiClusterTemplateVariable> variables = Lists.newArrayList();
  /**
   * All the role config group informations for non-base RCGs.
   */
  private List<ApiClusterTemplateRoleConfigGroupInfo> roleConfigGroups = Lists.newArrayList();

  public String getClusterName() {
    return this.clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  public List<ApiClusterTemplateHostInfo> getHosts() {
    return this.hosts;
  }

  public void setHosts(List<ApiClusterTemplateHostInfo> hosts) {
    this.hosts = hosts;
  }

  public List<ApiClusterTemplateVariable> getVariables() {
    return this.variables;
  }

  public void setVariables(
      List<ApiClusterTemplateVariable> variables) {
    this.variables = variables;
  }

  public List<ApiClusterTemplateRoleConfigGroupInfo> getRoleConfigGroups() {
    return roleConfigGroups;
  }

  public void setRoleConfigGroups(
      List<ApiClusterTemplateRoleConfigGroupInfo> roleConfigGroups) {
    this.roleConfigGroups = roleConfigGroups;
  }

}
