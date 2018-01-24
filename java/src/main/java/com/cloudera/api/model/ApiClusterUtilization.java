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
 * Utilization report information of a Cluster.
 */
@XmlRootElement(name = "clusterUtilization")
public class ApiClusterUtilization {

  private Double totalCpuCores;
  private Double avgCpuUtilization;
  private Double maxCpuUtilization;
  private Long maxCpuUtilizationTimestampMs;
  private Double avgCpuDailyPeak;
  private Double avgWorkloadCpu;
  private Double maxWorkloadCpu;
  private Long maxWorkloadCpuTimestampMs;
  private Double avgWorkloadCpuDailyPeak;

  private Double totalMemory;
  private Double avgMemoryUtilization;
  private Double maxMemoryUtilization;
  private Long maxMemoryUtilizationTimestampMs;
  private Double avgMemoryDailyPeak;
  private Double avgWorkloadMemory;
  private Double maxWorkloadMemory;
  private Long maxWorkloadMemoryTimestampMs;
  private Double avgWorkloadMemoryDailyPeak;
  private ApiTenantUtilizationList tenantUtilizations;

  private String errorMessage;

  public ApiClusterUtilization() {
    // For JAX-B
  }

  /**
   * Average number of CPU cores available in the cluster during the report window.
   */
  @XmlElement
  public Double getTotalCpuCores() {
    return totalCpuCores;
  }

  public void setTotalCpuCores(Double totalCpuCores) {
    this.totalCpuCores = totalCpuCores;
  }

  /**
   * Average CPU consumption for the entire cluster during the report window.
   * This includes consumption by user workloads in YARN and Impala, as well as
   * consumption by all services running in the cluster.
   */
  @XmlElement
  public Double getAvgCpuUtilization() {
    return avgCpuUtilization;
  }

  public void setAvgCpuUtilization(Double avgCpuUtilization) {
    this.avgCpuUtilization = avgCpuUtilization;
  }

  /**
   * Maximum CPU consumption for the entire cluster during the report window.
   * This includes consumption by user workloads in YARN and Impala, as well as
   * consumption by all services running in the cluster.
   */
  @XmlElement
  public Double getMaxCpuUtilization() {
    return maxCpuUtilization;
  }

  public void setMaxCpuUtilization(Double maxCpuUtilization) {
    this.maxCpuUtilization = maxCpuUtilization;
  }

  /**
   * Average daily peak CPU consumption for the entire cluster during the report
   * window. This includes consumption by user workloads in YARN and Impala, as
   * well as consumption by all services running in the cluster.
   */
  @XmlElement
  public Double getAvgCpuDailyPeak() {
    return avgCpuDailyPeak;
  }

  public void setAvgCpuDailyPeak(Double avgCpuDailyPeak) {
    this.avgCpuDailyPeak = avgCpuDailyPeak;
  }

  /**
   * Average CPU consumption by workloads that ran on the cluster during the
   * report window. This includes consumption by user workloads in YARN and
   * Impala.
   */
  @XmlElement
  public Double getAvgWorkloadCpu() {
    return avgWorkloadCpu;
  }

  public void setAvgWorkloadCpu(Double avgWorkloadCpu) {
    this.avgWorkloadCpu = avgWorkloadCpu;
  }

  /**
   * Maximum CPU consumption by workloads that ran on the cluster during the
   * report window. This includes consumption by user workloads in YARN and
   * Impala.
   */
  @XmlElement
  public Double getMaxWorkloadCpu() {
    return maxWorkloadCpu;
  }

  public void setMaxWorkloadCpu(Double maxWorkloadCpu) {
    this.maxWorkloadCpu = maxWorkloadCpu;
  }

  /**
   * Average daily peak CPU consumption by workloads that ran on the cluster
   * during the report window. This includes consumption by user workloads in
   * YARN and Impala.
   */
  @XmlElement
  public Double getAvgWorkloadCpuDailyPeak() {
    return avgWorkloadCpuDailyPeak;
  }

  public void setAvgWorkloadCpuDailyPeak(
      Double avgWorkloadCpuDailyPeak) {
    this.avgWorkloadCpuDailyPeak = avgWorkloadCpuDailyPeak;
  }

  /**
   * Average physical memory (in bytes) available in the cluster during the
   * report window. This includes consumption by user workloads in YARN and
   * Impala, as well as consumption by all services running in the cluster.
   */
  @XmlElement
  public Double getTotalMemory() {
    return totalMemory;
  }

  public void setTotalMemory(Double totalMemory) {
    this.totalMemory = totalMemory;
  }

  /**
   * Average memory consumption (as percentage of total memory) for the entire
   * cluster during the report window. This includes consumption by user
   * workloads in YARN and Impala, as well as consumption by all services
   * running in the cluster.
   */
  @XmlElement
  public Double getAvgMemoryUtilization() {
    return avgMemoryUtilization;
  }

  public void setAvgMemoryUtilization(Double avgMemoryUtilization) {
    this.avgMemoryUtilization = avgMemoryUtilization;
  }

  /**
   * Maximum memory consumption (as percentage of total memory) for the entire
   * cluster during the report window. This includes consumption by user
   * workloads in YARN and Impala, as well as consumption by all services
   * running in the cluster.
   */
  @XmlElement
  public Double getMaxMemoryUtilization() {
    return maxMemoryUtilization;
  }

  public void setMaxMemoryUtilization(Double maxMemoryUtilization) {
    this.maxMemoryUtilization = maxMemoryUtilization;
  }

  /**
   * Average daily peak memory consumption (as percentage of total memory) for
   * the entire cluster during the report window. This includes consumption by
   * user workloads in YARN and Impala, as well as consumption by all services
   * running in the cluster.
   */
  @XmlElement
  public Double getAvgMemoryDailyPeak() {
    return avgMemoryDailyPeak;
  }

  public void setAvgMemoryDailyPeak(Double avgMemoryDailyPeak) {
    this.avgMemoryDailyPeak = avgMemoryDailyPeak;
  }

  /**
   * Average memory consumption (as percentage of total memory) by workloads
   * that ran on the cluster during the report window. This includes consumption
   * by user workloads in YARN and Impala.
   */
  @XmlElement
  public Double getAvgWorkloadMemory() {
    return avgWorkloadMemory;
  }

  public void setAvgWorkloadMemory(Double avgWorkloadMemory) {
    this.avgWorkloadMemory = avgWorkloadMemory;
  }

  /**
   * Maximum memory consumption (as percentage of total memory) by workloads
   * that ran on the cluster. This includes consumption by user workloads in
   * YARN and Impala
   */
  @XmlElement
  public Double getMaxWorkloadMemory() {
    return maxWorkloadMemory;
  }

  public void setMaxWorkloadMemory(Double maxWorkloadMemory) {
    this.maxWorkloadMemory = maxWorkloadMemory;
  }

  /**
   * Average daily peak memory consumption (as percentage of total memory) by
   * workloads that ran on the cluster during the report window. This includes
   * consumption by user workloads in YARN and Impala.
   */
  @XmlElement
  public Double getAvgWorkloadMemoryDailyPeak() {
    return avgWorkloadMemoryDailyPeak;
  }

  public void setAvgWorkloadMemoryDailyPeak(
      Double avgWorkloadMemoryDailyPeak) {
    this.avgWorkloadMemoryDailyPeak = avgWorkloadMemoryDailyPeak;
  }

  /**
   * A list of tenant utilization reports.
   */
  @XmlElement
  public ApiTenantUtilizationList getTenantUtilizations() {
    return tenantUtilizations;
  }

  public void setTenantUtilizations(
      ApiTenantUtilizationList tenantUtilizations) {
    this.tenantUtilizations = tenantUtilizations;
  }

  /**
   * Timestamp corresponding to maximum CPU utilization for the entire cluster
   * during the report window.
   */
  @XmlElement
  public Long getMaxCpuUtilizationTimestampMs() {
    return maxCpuUtilizationTimestampMs;
  }

  public void setMaxCpuUtilizationTimestampMs(Long maxCpuUtilizationTimestampMs) {
    this.maxCpuUtilizationTimestampMs = maxCpuUtilizationTimestampMs;
  }

  /**
   * Timestamp corresponding to maximum memory utilization for the entire
   * cluster during the report window.
   */
  @XmlElement
  public Long getMaxMemoryUtilizationTimestampMs() {
    return maxMemoryUtilizationTimestampMs;
  }

  public void setMaxMemoryUtilizationTimestampMs(Long maxMemoryUtilizationTimestampMs) {
    this.maxMemoryUtilizationTimestampMs = maxMemoryUtilizationTimestampMs;
  }

  /**
   * Timestamp corresponds to maximum CPU consumption by workloads that
   * ran on the cluster during the report window.
   */
  @XmlElement
  public Long getMaxWorkloadCpuTimestampMs() {
    return maxWorkloadCpuTimestampMs;
  }

  public void setMaxWorkloadCpuTimestampMs(Long maxWorkloadCpuTimestampMs) {
    this.maxWorkloadCpuTimestampMs = maxWorkloadCpuTimestampMs;
  }


  /**
   * Timestamp corresponds to maximum memory resource consumption by workloads that
   * ran on the cluster during the report window.
   */
  @XmlElement
  public Long getMaxWorkloadMemoryTimestampMs() {
    return maxWorkloadMemoryTimestampMs;
  }

  public void setMaxWorkloadMemoryTimestampMs(Long maxWorkloadMemoryTimestampMs) {
    this.maxWorkloadMemoryTimestampMs = maxWorkloadMemoryTimestampMs;
  }

  /**
   * Error message while generating utilization report.
   */
  @XmlElement
  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("totalCpuCores", totalCpuCores)
        .add("avgCpuUtilization", avgCpuUtilization)
        .add("maxCpuUtilization", maxCpuUtilization)
        .add("avgCpuDailyPeak", avgCpuDailyPeak)
        .add("avgWorkloadCpu", avgWorkloadCpu)
        .add("maxWorkloadCpu", maxWorkloadCpu)
        .add("avgWorkloadCpuDailyPeak", avgWorkloadCpuDailyPeak)
        .add("totalMemory", totalMemory)
        .add("avgMemoryUtilization", avgMemoryUtilization)
        .add("maxMemoryUtilization", maxMemoryUtilization)
        .add("avgMemoryDailyPeak", avgMemoryDailyPeak)
        .add("avgWorkloadMemory", avgWorkloadMemory)
        .add("maxWorkloadMemory", maxWorkloadMemory)
        .add("avgWorkloadMemoryDailyPeak", avgWorkloadMemoryDailyPeak)
        .add("maxWorkloadCpuTimestampMs", maxWorkloadCpuTimestampMs)
        .add("maxWorkloadMemoryTimestampMs", maxWorkloadMemoryTimestampMs)
        .add("maxCpuUtilizationTimestampMs", maxCpuUtilizationTimestampMs)
        .add("maxMemoryUtilizationTimestampMs", maxMemoryUtilizationTimestampMs)
        .add("tenantUtilizations", tenantUtilizations)
        .add("errorMessage", errorMessage)
        .toString();
  }
}
