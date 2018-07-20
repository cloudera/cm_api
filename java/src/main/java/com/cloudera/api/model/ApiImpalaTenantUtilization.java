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
 * Utilization report information of a tenant of Impala application.
 */
@XmlRootElement(name = "impalaTenantUtilization")
public class ApiImpalaTenantUtilization {
  private String tenantName;
  private Double totalQueries;
  private Double successfulQueries;
  private Double oomQueries;
  private Double timeOutQueries;
  private Double rejectedQueries;
  private Double avgWaitTimeInQueue;
  private Long peakAllocationTimestampMS;
  private Double maxAllocatedMemory;
  private Double maxAllocatedMemoryPercentage;
  private Double utilizedAtMaxAllocated;
  private Double utilizedAtMaxAllocatedPercentage;
  private Long peakUsageTimestampMS;
  private Double maxUtilizedMemory;
  private Double maxUtilizedMemoryPercentage;
  private Double allocatedAtMaxUtilized;
  private Double allocatedAtMaxUtilizedPercentage;
  private ApiImpalaUtilizationHistogram distributionAllocatedByImpalaDaemon;
  private ApiImpalaUtilizationHistogram distributionUtilizedByImpalaDaemon;
  private Double avgSpilledMemory;
  private Double maxSpilledMemory;

  public ApiImpalaTenantUtilization() {
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
   * Total number of queries submitted to Impala.
   */
  @XmlElement
  public Double getTotalQueries() {
    return totalQueries;
  }

  public void setTotalQueries(
      Double totalQueries) {
    this.totalQueries = totalQueries;
  }

  /**
   * Number of queries that finished successfully.
   */
  @XmlElement
  public Double getSuccessfulQueries() {
    return successfulQueries;
  }

  public void setSuccessfulQueries(
      Double successfulQueries) {
    this.successfulQueries = successfulQueries;
  }

  /**
   * Number of queries that failed due to insufficient memory.
   */
  @XmlElement
  public Double getOomQueries() {
    return oomQueries;
  }

  public void setOomQueries(
      Double oomQueries) {
    this.oomQueries = oomQueries;
  }

  /**
   * Number of queries that timed out while waiting for resources in a pool.
   */
  @XmlElement
  public Double getTimeOutQueries() {
    return timeOutQueries;
  }

  public void setTimeOutQueries(
      Double timeOutQueries) {
    this.timeOutQueries = timeOutQueries;
  }

  /**
   * Number of queries that were rejected by Impala because the pool was full.
   */
  @XmlElement
  public Double getRejectedQueries() {
    return rejectedQueries;
  }

  public void setRejectedQueries(
      Double rejectedQueries) {
    this.rejectedQueries = rejectedQueries;
  }

  /**
   * Average time, in milliseconds, spent by a query in an Impala pool while waiting for resources.
   */
  @XmlElement
  public Double getAvgWaitTimeInQueue() {
    return avgWaitTimeInQueue;
  }

  public void setAvgWaitTimeInQueue(
      Double avgWaitTimeInQueue) {
    this.avgWaitTimeInQueue = avgWaitTimeInQueue;
  }

  /**
   * The time when Impala reserved the maximum amount of memory for queries.
   */
  @XmlElement
  public Long getPeakAllocationTimestampMS() {
    return peakAllocationTimestampMS;
  }

  public void setPeakAllocationTimestampMS(
      Long peakAllocationTimestampMS) {
    this.peakAllocationTimestampMS = peakAllocationTimestampMS;
  }

  /**
   * The maximum memory (in bytes) that was reserved by Impala for executing queries.
   */
  @XmlElement
  public Double getMaxAllocatedMemory() {
    return maxAllocatedMemory;
  }

  public void setMaxAllocatedMemory(
      Double maxAllocatedMemory) {
    this.maxAllocatedMemory = maxAllocatedMemory;
  }

  /**
   * The maximum percentage of memory that was reserved by Impala for executing queries.
   */
  @XmlElement
  public Double getMaxAllocatedMemoryPercentage() {
    return maxAllocatedMemoryPercentage;
  }

  public void setMaxAllocatedMemoryPercentage(
      Double maxAllocatedMemoryPercentage) {
    this.maxAllocatedMemoryPercentage = maxAllocatedMemoryPercentage;
  }

  /**
   * The amount of memory (in bytes) used by Impala for running queries at the
   * time when maximum memory was reserved.
   */
  @XmlElement
  public Double getUtilizedAtMaxAllocated() {
    return utilizedAtMaxAllocated;
  }

  public void setUtilizedAtMaxAllocated(
      Double utilizedAtMaxAllocated) {
    this.utilizedAtMaxAllocated = utilizedAtMaxAllocated;
  }

  /**
   * The percentage of memory used by Impala for running queries at the
   * time when maximum memory was reserved.
   */
  @XmlElement
  public Double getUtilizedAtMaxAllocatedPercentage() {
    return utilizedAtMaxAllocatedPercentage;
  }

  public void setUtilizedAtMaxAllocatedPercentage(
      Double utilizedAtMaxAllocatedPercentage) {
    this.utilizedAtMaxAllocatedPercentage = utilizedAtMaxAllocatedPercentage;
  }

  /**
   * The time when Impala used the maximum amount of memory for queries.
   */
  @XmlElement
  public Long getPeakUsageTimestampMS() {
    return peakUsageTimestampMS;
  }

  public void setPeakUsageTimestampMS(
      Long peakUsageTimestampMS) {
    this.peakUsageTimestampMS = peakUsageTimestampMS;
  }

  /**
   * The maximum memory (in bytes) that was used by Impala for executing queries.
   */
  @XmlElement
  public Double getMaxUtilizedMemory() {
    return maxUtilizedMemory;
  }

  public void setMaxUtilizedMemory(
      Double maxUtilizedMemory) {
    this.maxUtilizedMemory = maxUtilizedMemory;
  }

  /**
   * The maximum percentage of memory that was used by Impala for executing queries.
   */
  @XmlElement
  public Double getMaxUtilizedMemoryPercentage() {
    return maxUtilizedMemoryPercentage;
  }

  public void setMaxUtilizedMemoryPercentage(
      Double maxUtilizedMemoryPercentage) {
    this.maxUtilizedMemoryPercentage = maxUtilizedMemoryPercentage;
  }

  /**
   * The amount of memory (in bytes) reserved by Impala at the time when it was using the
   * maximum memory for executing queries.
   */
  @XmlElement
  public Double getAllocatedAtMaxUtilized() {
    return allocatedAtMaxUtilized;
  }

  public void setAllocatedAtMaxUtilized(
      Double allocatedAtMaxUtilized) {
    this.allocatedAtMaxUtilized = allocatedAtMaxUtilized;
  }

  /**
   * The percentage of memory reserved by Impala at the time when it was using the
   * maximum memory for executing queries.
   */
  @XmlElement
  public Double getAllocatedAtMaxUtilizedPercentage() {
    return allocatedAtMaxUtilizedPercentage;
  }

  public void setAllocatedAtMaxUtilizedPercentage(
      Double allocatedAtMaxUtilizedPercentage) {
    this.allocatedAtMaxUtilizedPercentage = allocatedAtMaxUtilizedPercentage;
  }

  /**
   * Distribution of memory used per Impala daemon for executing queries at
   * the time Impala used the maximum memory.
   */
  @XmlElement
  public ApiImpalaUtilizationHistogram getDistributionUtilizedByImpalaDaemon() {
    return distributionUtilizedByImpalaDaemon;
  }

  public void setDistributionUtilizedByImpalaDaemon(ApiImpalaUtilizationHistogram distributionUtilizedByImpalad) {
    this.distributionUtilizedByImpalaDaemon = distributionUtilizedByImpalad;
  }

  /**
   * Distribution of memory reserved per Impala daemon for executing queries at
   * the time Impala used the maximum memory.
   */
  @XmlElement
  public ApiImpalaUtilizationHistogram getDistributionAllocatedByImpalaDaemon() {
    return distributionAllocatedByImpalaDaemon;
  }

  public void setDistributionAllocatedByImpalaDaemon(ApiImpalaUtilizationHistogram distributionAllocatedByImpalad) {
    this.distributionAllocatedByImpalaDaemon = distributionAllocatedByImpalad;
  }

  /**
   * Average spill per query.
   */
  @XmlElement
  public Double getAvgSpilledMemory() {
    return avgSpilledMemory;
  }

  public void setAvgSpilledMemory(
      Double avgSpilledMemory) {
    this.avgSpilledMemory = avgSpilledMemory;
  }

  /**
   * Maximum spill per query.
   */
  @XmlElement
  public Double getMaxSpilledMemory() {
    return maxSpilledMemory;
  }

  public void setMaxSpilledMemory(
      Double maxSpilledMemory) {
    this.maxSpilledMemory = maxSpilledMemory;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("tenantName", tenantName)
        .add("totalQueries", totalQueries)
        .add("successfulQueries", successfulQueries)
        .add("oomQueries", oomQueries)
        .add("timeOutQueries", timeOutQueries)
        .add("rejectedQueries", rejectedQueries)
        .add("avgWaitTimeInQueue", avgWaitTimeInQueue)
        .add("peakAllocationTimestampMS", peakAllocationTimestampMS)
        .add("maxAllocatedMemory", maxAllocatedMemory)
        .add("maxAllocatedMemoryPercentage", maxAllocatedMemoryPercentage)
        .add("utilizedAtMaxAllocated", utilizedAtMaxAllocated)
        .add("utilizedAtMaxAllocatedPercentage", utilizedAtMaxAllocatedPercentage)
        .add("peakUsageTimestampMS", peakUsageTimestampMS)
        .add("maxUtilizedMemory", maxUtilizedMemory)
        .add("maxUtilizedMemoryPercentage", maxUtilizedMemoryPercentage)
        .add("allocatedAtMaxUtilized", allocatedAtMaxUtilized)
        .add("allocatedAtMaxUtilizedPercentage", allocatedAtMaxUtilizedPercentage)
        .add("distributionAllocatedByImpalad", distributionAllocatedByImpalaDaemon)
        .add("distributionUtilizedByImpalad", distributionUtilizedByImpalaDaemon)
        .add("avgSpilledMemory", avgSpilledMemory)
        .add("maxSpilledMemory", maxSpilledMemory)
        .toString();
  }
}
