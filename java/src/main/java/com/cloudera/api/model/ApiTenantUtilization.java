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
 * Utilization report information of a tenant.
 */
@XmlRootElement(name = "tenantUtilization")
public class ApiTenantUtilization {

  private String tenantName;
  private Double cpuUtilizationPercentage;
  private Double memoryUtilizationPercentage;

  public ApiTenantUtilization() {
    // For JAX-B
  }

  /**
   * Name of the tenant.
   */
  @XmlElement
  public String getTenantName() {
    return tenantName;
  }

  public void setTenantName(
      String tenantName) {
    this.tenantName = tenantName;
  }

  /**
   * Percentage of CPU resource used by workloads.
   */
  @XmlElement
  public Double getCpuUtilizationPercentage() {
    return cpuUtilizationPercentage;
  }

  public void setCpuUtilizationPercentage(
      Double cpuUtilizationPercentage) {
    this.cpuUtilizationPercentage = cpuUtilizationPercentage;
  }

  /**
   * Percentage of memory used by workloads.
   */
  @XmlElement
  public Double getMemoryUtilizationPercentage() {
    return memoryUtilizationPercentage;
  }

  public void setMemoryUtilizationPercentage(
      Double memoryUtilizationPercentage) {
    this.memoryUtilizationPercentage = memoryUtilizationPercentage;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("tenantName", tenantName)
        .add("cpuUtilizationPercentage", cpuUtilizationPercentage)
        .add("memoryUtilizationPercentage",
            memoryUtilizationPercentage)
        .toString();
  }
}
