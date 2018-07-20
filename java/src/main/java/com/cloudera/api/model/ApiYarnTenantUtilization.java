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
 * Utilization report information of a tenant of Yarn application.
 */
@XmlRootElement(name = "yarnTenantUtilization")
public class ApiYarnTenantUtilization {
  private String tenantName;
  private Double avgYarnCpuAllocation;
  private Double avgYarnCpuUtilization;
  private Double avgYarnCpuUnusedCapacity;
  private Double avgYarnCpuSteadyFairShare;
  private Double avgYarnPoolAllocatedCpuDuringContention;
  private Double avgYarnPoolFairShareCpuDuringContention;
  private Double avgYarnPoolSteadyFairShareCpuDuringContention;

  private Double avgYarnMemoryAllocation;
  private Double avgYarnMemoryUtilization;
  private Double avgYarnMemoryUnusedCapacity;
  private Double avgYarnMemorySteadyFairShare;
  private Double avgYarnPoolAllocatedMemoryDuringContention;
  private Double avgYarnPoolFairShareMemoryDuringContention;
  private Double avgYarnPoolSteadyFairShareMemoryDuringContention;

  private Double avgYarnContainerWaitRatio;

  public ApiYarnTenantUtilization() {
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
   * Average number of VCores allocated to YARN applications of the tenant.
   */
  @XmlElement
  public Double getAvgYarnCpuAllocation() {
    return avgYarnCpuAllocation;
  }

  public void setAvgYarnCpuAllocation(
      Double avgYarnCpuAllocation) {
    this.avgYarnCpuAllocation = avgYarnCpuAllocation;
  }

  /**
   * Average number of VCores used by YARN applications of the tenant.
   */
  @XmlElement
  public Double getAvgYarnCpuUtilization() {
    return avgYarnCpuUtilization;
  }

  public void setAvgYarnCpuUtilization(
      Double avgYarnCpuUtilization) {
    this.avgYarnCpuUtilization = avgYarnCpuUtilization;
  }

  /**
   * Average unused VCores of the tenant.
   */
  @XmlElement
  public Double getAvgYarnCpuUnusedCapacity() {
    return avgYarnCpuUnusedCapacity;
  }

  public void setAvgYarnCpuUnusedCapacity(
      Double avgYarnCpuUnusedCapacity) {
    this.avgYarnCpuUnusedCapacity = avgYarnCpuUnusedCapacity;
  }

  /**
   * Average steady fair share VCores.
   */
  @XmlElement
  public Double getAvgYarnCpuSteadyFairShare() {
    return avgYarnCpuSteadyFairShare;
  }

  public void setAvgYarnCpuSteadyFairShare(
      Double avgYarnCpuSteadyFairShare) {
    this.avgYarnCpuSteadyFairShare = avgYarnCpuSteadyFairShare;
  }

  /**
   * Average allocated Vcores with pending containers.
   */
  @XmlElement
  public Double getAvgYarnPoolAllocatedCpuDuringContention() {
    return avgYarnPoolAllocatedCpuDuringContention;
  }

  public void setAvgYarnPoolAllocatedCpuDuringContention(
      Double avgYarnPoolAllocatedCpuDuringContention) {
    this.avgYarnPoolAllocatedCpuDuringContention = avgYarnPoolAllocatedCpuDuringContention;
  }

  /**
   * Average fair share VCores with pending containers.
   */
  @XmlElement
  public Double getAvgYarnPoolFairShareCpuDuringContention() {
    return avgYarnPoolFairShareCpuDuringContention;
  }

  public void setAvgYarnPoolFairShareCpuDuringContention(
      Double avgYarnPoolFairShareCpuDuringContention) {
    this.avgYarnPoolFairShareCpuDuringContention = avgYarnPoolFairShareCpuDuringContention;
  }

  /**
   * Average steady fair share VCores with pending containers.
   */
  @XmlElement
  public Double getAvgYarnPoolSteadyFairShareCpuDuringContention() {
    return avgYarnPoolSteadyFairShareCpuDuringContention;
  }

  public void setAvgYarnPoolSteadyFairShareCpuDuringContention(
      Double avgYarnPoolSteadyFairShareCpuDuringContention) {
    this.avgYarnPoolSteadyFairShareCpuDuringContention = avgYarnPoolSteadyFairShareCpuDuringContention;
  }

  /**
   * Average percentage of pending containers for the pool during periods of
   * contention.
   */
  @XmlElement
  public Double getAvgYarnContainerWaitRatio() {
    return avgYarnContainerWaitRatio;
  }

  public void setAvgYarnContainerWaitRatio(
      Double avgYarnContainerWaitRatio) {
    this.avgYarnContainerWaitRatio = avgYarnContainerWaitRatio;
  }

  /**
   * Average memory allocated to YARN applications of the tenant.
   */
  @XmlElement
  public Double getAvgYarnMemoryAllocation() {
    return avgYarnMemoryAllocation;
  }

  public void setAvgYarnMemoryAllocation(
      Double avgYarnMemoryAllocation) {
    this.avgYarnMemoryAllocation = avgYarnMemoryAllocation;
  }

  /**
   * Average memory used by YARN applications of the tenant.
   */
  @XmlElement
  public Double getAvgYarnMemoryUtilization() {
    return avgYarnMemoryUtilization;
  }

  public void setAvgYarnMemoryUtilization(
      Double avgYarnMemoryUtilization) {
    this.avgYarnMemoryUtilization = avgYarnMemoryUtilization;
  }

  /**
   * Average unused memory of the tenant.
   */
  @XmlElement
  public Double getAvgYarnMemoryUnusedCapacity() {
    return avgYarnMemoryUnusedCapacity;
  }

  public void setAvgYarnMemoryUnusedCapacity(
      Double avgYarnMemoryUnusedCapacity) {
    this.avgYarnMemoryUnusedCapacity = avgYarnMemoryUnusedCapacity;
  }

  /**
   * Average steady fair share memory.
   */
  @XmlElement
  public Double getAvgYarnMemorySteadyFairShare() {
    return avgYarnMemorySteadyFairShare;
  }

  public void setAvgYarnMemorySteadyFairShare(
      Double avgYarnMemorySteadyFairShare) {
    this.avgYarnMemorySteadyFairShare = avgYarnMemorySteadyFairShare;
  }

  /**
   * Average allocated memory with pending containers.
   */
  @XmlElement
  public Double getAvgYarnPoolAllocatedMemoryDuringContention() {
    return avgYarnPoolAllocatedMemoryDuringContention;
  }

  public void setAvgYarnPoolAllocatedMemoryDuringContention(
      Double avgYarnPoolAllocatedMemoryDuringContention) {
    this.avgYarnPoolAllocatedMemoryDuringContention = avgYarnPoolAllocatedMemoryDuringContention;
  }

  /**
   * Average fair share memory with pending containers.
   */
  @XmlElement
  public Double getAvgYarnPoolFairShareMemoryDuringContention() {
    return avgYarnPoolFairShareMemoryDuringContention;
  }

  public void setAvgYarnPoolFairShareMemoryDuringContention(
      Double avgYarnPoolFairShareMemoryDuringContention) {
    this.avgYarnPoolFairShareMemoryDuringContention = avgYarnPoolFairShareMemoryDuringContention;
  }

  /**
   * Average steady fair share memory with pending containers.
   */
  @XmlElement
  public Double getAvgYarnPoolSteadyFairShareMemoryDuringContention() {
    return avgYarnPoolSteadyFairShareMemoryDuringContention;
  }

  public void setAvgYarnPoolSteadyFairShareMemoryDuringContention(
      Double avgYarnPoolSteadyFairShareMemoryDuringContention) {
    this.avgYarnPoolSteadyFairShareMemoryDuringContention = avgYarnPoolSteadyFairShareMemoryDuringContention;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("avgYarnCpuAllocation", avgYarnCpuAllocation)
        .add("avgYarnCpuUtilization", avgYarnCpuUtilization)
        .add("avgYarnCpuUnusedCapacity", avgYarnCpuUnusedCapacity)
        .add("avgYarnCpuSteadyFairShare", avgYarnCpuSteadyFairShare)
        .add("avgYarnPoolAllocatedCpuDuringContention",
            avgYarnPoolAllocatedCpuDuringContention)
        .add("avgYarnPoolFairShareCpuDuringContention",
            avgYarnPoolFairShareCpuDuringContention)
        .add("avgYarnPoolSteadyFairShareCpuDuringContention",
            avgYarnPoolSteadyFairShareCpuDuringContention)
        .add("avgYarnMemoryAllocation", avgYarnMemoryAllocation)
        .add("avgYarnMemoryUtilization", avgYarnMemoryUtilization)
        .add("avgYarnMemoryUnusedCapacity", avgYarnMemoryUnusedCapacity)
        .add("avgYarnMemorySteadyFairShare", avgYarnMemorySteadyFairShare)
        .add("avgYarnPoolAllocatedMemoryDuringContention",
            avgYarnPoolAllocatedMemoryDuringContention)
        .add("avgYarnPoolFairShareMemoryDuringContention",
            avgYarnPoolFairShareMemoryDuringContention)
        .add("avgYarnPoolSteadyFairShareMemoryDuringContention",
            avgYarnPoolSteadyFairShareMemoryDuringContention)
        .add("avgYarnContainerWaitRatio", avgYarnContainerWaitRatio)
        .toString();
  }
}
