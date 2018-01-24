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
 * Utilization report information of a Yarn application service.
 */
@XmlRootElement(name = "yarnUtilization")
public class ApiYarnUtilization {
  private Double avgCpuUtilization;
  private Double maxCpuUtilization;
  private Double avgCpuDailyPeak;
  private Long maxCpuUtilizationTimestampMs;
  private Double avgCpuUtilizationPercentage;
  private Double maxCpuUtilizationPercentage;
  private Double avgCpuDailyPeakPercentage;

  private Double avgMemoryUtilization;
  private Double maxMemoryUtilization;
  private Double avgMemoryDailyPeak;
  private Long maxMemoryUtilizationTimestampMs;
  private Double avgMemoryUtilizationPercentage;
  private Double maxMemoryUtilizationPercentage;
  private Double avgMemoryDailyPeakPercentage;
  private ApiYarnTenantUtilizationList tenantUtilizations;
  private String errorMessage;

  public ApiYarnUtilization() {
    // For JAX-B
  }

  /**
   * Average number of VCores used by YARN applications during the report
   * window.
   */
  @XmlElement
  public Double getAvgCpuUtilization() {
    return avgCpuUtilization;
  }

  public void setAvgCpuUtilization(Double avgCpuUtilization) {
    this.avgCpuUtilization = avgCpuUtilization;
  }

  /**
   * Maximum number of VCores used by YARN applications during the report
   * window.
   */
  @XmlElement
  public Double getMaxCpuUtilization() {
    return maxCpuUtilization;
  }

  public void setMaxCpuUtilization(Double maxCpuUtilization) {
    this.maxCpuUtilization = maxCpuUtilization;
  }

  /**
   * Average daily peak VCores used by YARN applications during the report
   * window. The number is computed by first finding the maximum resource
   * consumption per day and then taking their mean.
   */
  @XmlElement
  public Double getAvgCpuDailyPeak() {
    return avgCpuDailyPeak;
  }

  public void setAvgCpuDailyPeak(Double avgCpuDailyPeak) {
    this.avgCpuDailyPeak = avgCpuDailyPeak;
  }

  /**
   * Timestamp corresponds to maximum number of VCores used by YARN applications
   * during the report window.
   */
  @XmlElement
  public Long getMaxCpuUtilizationTimestampMs() {
    return maxCpuUtilizationTimestampMs;
  }

  public void setMaxCpuUtilizationTimestampMs(
      Long maxCpuUtilizationTimestampMs) {
    this.maxCpuUtilizationTimestampMs = maxCpuUtilizationTimestampMs;
  }

  /**
   * Average percentage of VCores used by YARN applications during the report
   * window.
   */
  @XmlElement
  public Double getAvgCpuUtilizationPercentage() {
    return avgCpuUtilizationPercentage;
  }

  public void setAvgCpuUtilizationPercentage(
      Double avgCpuUtilizationPercentage) {
    this.avgCpuUtilizationPercentage = avgCpuUtilizationPercentage;
  }

  /**
   * Maximum percentage of VCores used by YARN applications during the report
   * window.
   */
  @XmlElement
  public Double getMaxCpuUtilizationPercentage() {
    return maxCpuUtilizationPercentage;
  }

  public void setMaxCpuUtilizationPercentage(
      Double maxCpuUtilizationPercentage) {
    this.maxCpuUtilizationPercentage = maxCpuUtilizationPercentage;
  }

  /**
   * Average daily peak percentage of VCores used by YARN applications during
   * the report window.
   */
  @XmlElement
  public Double getAvgCpuDailyPeakPercentage() {
    return avgCpuDailyPeakPercentage;
  }

  public void setAvgCpuDailyPeakPercentage(Double avgCpuDailyPeakPercentage) {
    this.avgCpuDailyPeakPercentage = avgCpuDailyPeakPercentage;
  }

  /**
   * Average memory used by YARN applications during the report window.
   */
  @XmlElement
  public Double getAvgMemoryUtilization() {
    return avgMemoryUtilization;
  }

  public void setAvgMemoryUtilization(Double avgMemoryUtilization) {
    this.avgMemoryUtilization = avgMemoryUtilization;
  }

  /**
   * Maximum memory used by YARN applications during the report window.
   */
  @XmlElement
  public Double getMaxMemoryUtilization() {
    return maxMemoryUtilization;
  }

  public void setMaxMemoryUtilization(Double maxMemoryUtilization) {
    this.maxMemoryUtilization = maxMemoryUtilization;
  }

  /**
   * Average daily peak memory used by YARN applications during the report
   * window. The number is computed by first finding the maximum resource
   * consumption per day and then taking their mean.
   */
  @XmlElement
  public Double getAvgMemoryDailyPeak() {
    return avgMemoryDailyPeak;
  }

  public void setAvgMemoryDailyPeak(Double avgMemoryDailyPeak) {
    this.avgMemoryDailyPeak = avgMemoryDailyPeak;
  }

  /**
   * Timestamp corresponds to maximum memory used by YARN applications during
   * the report window.
   */
  @XmlElement
  public Long getMaxMemoryUtilizationTimestampMs() {
    return maxMemoryUtilizationTimestampMs;
  }

  public void setMaxMemoryUtilizationTimestampMs(
      Long maxMemoryUtilizationTimestampMs) {
    this.maxMemoryUtilizationTimestampMs = maxMemoryUtilizationTimestampMs;
  }

  /**
   * Average percentage memory used by YARN applications during the report window.
   */
  @XmlElement
  public Double getAvgMemoryUtilizationPercentage() {
    return avgMemoryUtilizationPercentage;
  }

  public void setAvgMemoryUtilizationPercentage(Double avgMemoryUtilizationPercentage) {
    this.avgMemoryUtilizationPercentage = avgMemoryUtilizationPercentage;
  }

  /**
   * Maximum percentage of memory used by YARN applications during the report window.
   */
  @XmlElement
  public Double getMaxMemoryUtilizationPercentage() {
    return maxMemoryUtilizationPercentage;
  }

  public void setMaxMemoryUtilizationPercentage(Double maxMemoryUtilizationPercentage) {
    this.maxMemoryUtilizationPercentage = maxMemoryUtilizationPercentage;
  }

  /**
   * Average daily peak percentage of memory used by YARN applications during the report
   * window.
   */
  @XmlElement
  public Double getAvgMemoryDailyPeakPercentage() {
    return avgMemoryDailyPeakPercentage;
  }

  public void setAvgMemoryDailyPeakPercentage(Double avgMemoryDailyPeakPercentage) {
    this.avgMemoryDailyPeakPercentage = avgMemoryDailyPeakPercentage;
  }

  /**
   * A list of tenant utilization reports.
   */
  @XmlElement
  public ApiYarnTenantUtilizationList getTenantUtilizations() {
    return tenantUtilizations;
  }

  public void setTenantUtilizations(
      ApiYarnTenantUtilizationList tenantUtilizations) {
    this.tenantUtilizations = tenantUtilizations;
  }

  /**
   * error message of utilization report.
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
        .add("avgCpuUtilization", avgCpuUtilization)
        .add("maxCpuUtilization", maxCpuUtilization)
        .add("avgCpuDailyPeak", avgCpuDailyPeak)
        .add("avgMemoryUtilization", avgMemoryUtilization)
        .add("maxMemoryUtilization", maxMemoryUtilization)
        .add("avgMemoryDailyPeak", avgMemoryDailyPeak)
        .add("maxCpuUtilizationTimestampMs", maxCpuUtilizationTimestampMs)
        .add("maxMemoryUtilizationTimestampMs", maxMemoryUtilizationTimestampMs)
        .add("avgCpuUtilizationPercentage", avgCpuUtilizationPercentage)
        .add("maxCpuUtilizationPercentage", maxCpuUtilizationPercentage)
        .add("avgCpuDailyPeakPercentage", avgCpuDailyPeakPercentage)
        .add("avgMemoryUtilizationPercentage", avgMemoryUtilizationPercentage)
        .add("maxMemoryUtilizationPercentage", maxMemoryUtilizationPercentage)
        .add("avgMemoryDailyPeakPercentage", avgMemoryDailyPeakPercentage)
        .add("tenantUtilizations", tenantUtilizations)
        .add("errorMessage", errorMessage)
        .toString();
  }
}
