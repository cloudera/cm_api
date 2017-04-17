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
import com.google.common.base.Preconditions;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a Yarn application
 */
@XmlRootElement(name = "yarnApplication")
public class ApiYarnApplication {

  private String applicationId;


  private String name;
  private Date startTime;
  private Date endTime;
  private String user;
  private String pool;
  private String state;
  private Double progress;
  private ApiMr2AppInformation mr2AppInfo;
  private Map<String, String> attributes;
  private List<String> applicationTags;
  private Long allocatedMemorySeconds;
  private Long allocatedVcoreSeconds;
  private Integer allocatedMB;
  private Integer allocatedVCores;
  private Integer runningContainers;

  private Double containerUsedMemorySeconds;
  private Double containerUsedMemoryMax;
  private Double containerUsedCpuSeconds;
  private Double containerUsedVcoreSeconds;
  private Double containerAllocatedMemorySeconds;
  private Double containerAllocatedVcoreSeconds;

  public ApiYarnApplication() {
    // For JAX-B
  }

  public ApiYarnApplication(
      String applicationId,
      String name,
      Date startTime,
      Date endTime,
      String user,
      String pool,
      String state,
      Double progress,
      ApiMr2AppInformation mr2AppInfo,
      Map<String, String> attributes,
      List<String> applicationTags,
      Long allocatedMemorySeconds,
      Long allocatedVcoreSeconds,
      Integer allocatedMB,
      Integer allocatedVCores,
      Integer runningContainers,
      Double containerUsedMemorySeconds,
      Double containerUsedMemoryMax,
      Double containerUsedCpuSeconds,
      Double containerUsedVcoreSeconds,
      Double containerAllocatedMemorySeconds,
      Double containerAllocatedVcoreSeconds) {
    Preconditions.checkNotNull(applicationId);
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(startTime);
    Preconditions.checkNotNull(user);
    Preconditions.checkNotNull(attributes);
    this.applicationId = applicationId;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.user = user;
    this.pool = pool;
    this.state = state;
    this.progress = progress;
    this.attributes = attributes;
    this.mr2AppInfo = mr2AppInfo;
    this.applicationTags = applicationTags;
    this.allocatedMemorySeconds = allocatedMemorySeconds;
    this.allocatedVcoreSeconds = allocatedVcoreSeconds;
    this.allocatedMB = allocatedMB;
    this.allocatedVCores = allocatedVCores;
    this.runningContainers = runningContainers;
    this.containerUsedMemorySeconds = containerUsedMemorySeconds;
    this.containerUsedMemoryMax = containerUsedMemoryMax;
    this.containerUsedCpuSeconds = containerUsedCpuSeconds;
    this.containerUsedVcoreSeconds = containerUsedVcoreSeconds;
    this.containerAllocatedMemorySeconds = containerAllocatedMemorySeconds;
    this.containerAllocatedVcoreSeconds = containerAllocatedVcoreSeconds;
  }

  /**
   The sum of memory in MB allocated to the application's running containers
   Available since v12.
   */
  @XmlElement
  public Integer getAllocatedMB() {
    return allocatedMB;
  }

  public void setAllocatedMB(Integer allocatedMB) {
    this.allocatedMB = allocatedMB;
  }

  /**
   The sum of virtual cores allocated to the application's running containers
   Available since v12.
   */
  @XmlElement
  public Integer getAllocatedVCores() {
    return allocatedVCores;
  }

  public void setAllocatedVCores(Integer allocatedVCores) {
    this.allocatedVCores = allocatedVCores;
  }

  /**
   The number of containers currently running for the application
   Available since v12.
   */
  @XmlElement
  public Integer getRunningContainers() {
    return runningContainers;
  }

  public void setRunningContainers(Integer runningContainers) {
    this.runningContainers = runningContainers;
  }

  /** List of YARN application tags. Available since v12. */
  @XmlElement
  public List<String> getApplicationTags() {
    return applicationTags;
  }

  public void setApplicationTags(List<String> applicationTags) {
    this.applicationTags = applicationTags;
  }

  /**
   Allocated memory to the application in units of mb-secs.
   Available since v12.
   */
  @XmlElement
  public Long getAllocatedMemorySeconds() {
    return allocatedMemorySeconds;
  }

  public void setAllocatedMemorySeconds(Long allocatedMemorySeconds) {
    this.allocatedMemorySeconds = allocatedMemorySeconds;
  }

  /** Allocated vcore-secs to the application. Available since v12. */
  @XmlElement
  public Long getAllocatedVcoreSeconds() {
    return allocatedVcoreSeconds;
  }

  public void setAllocatedVcoreSeconds(Long allocatedVcoreSeconds) {
    this.allocatedVcoreSeconds = allocatedVcoreSeconds;
  }

  /** The application id. */
  @XmlElement
  public String getApplicationId() {
    return applicationId;
  }

  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  /** The name of the application. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** The time the application was submitted. */
  @XmlElement
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /**
   * The time the application finished. If the application hasn't finished
   * this will return null.
   */
  @XmlElement
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /** The user who submitted the application. */
  @XmlElement
  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  /** The pool the application was submitted to. */
  @XmlElement
  public String getPool() {
    return pool;
  }

  public void setPool(String pool) {
    this.pool = pool;
  }

  /**
   * The progress, as a percentage, the application has made. This is only
   * set if the application is currently executing.
   */
  @XmlElement
  public Double getProgress() {
    return progress;
  }

  public void setProgress(Double progress) {
    this.progress = progress;
  }

  /**
   * A map of additional application attributes which is generated by
   * Cloudera Manager. For example MR2 job counters are exposed as key/value
   * pairs here. For more details see the Cloudera Manager documentation.
   */
  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  public ApiMr2AppInformation getMr2AppInformation() {
    return mr2AppInfo;
  }

  public void setMr2AppInformation(ApiMr2AppInformation mr2AppInfo) {
    this.mr2AppInfo = mr2AppInfo;
  }

  @XmlElement
  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  /**
   * Actual memory (in MB-secs) used by containers launched by the YARN application.
   * Computed by running a MapReduce job from Cloudera Service Monitor to
   * aggregate YARN usage metrics.
   * Available since v12.
   */
  @XmlElement
  public Double getContainerUsedMemorySeconds() {
    return containerUsedMemorySeconds;
  }

  public void setContainerUsedMemorySeconds(Double containerUsedMemorySeconds) {
    this.containerUsedMemorySeconds = containerUsedMemorySeconds;
  }

  /**
   * Maximum memory used by containers launched by the YARN application.
   * Computed by running a MapReduce job from Cloudera Service Monitor to
   * aggregate YARN usage metrics
   * Available since v16
   */
  @XmlElement
  public Double getContainerUsedMemoryMax() {
    return containerUsedMemoryMax;
  }

  public void setContainerUsedMemoryMax(Double containerUsedMemoryMax) {
    this.containerUsedMemoryMax = containerUsedMemoryMax;
  }

  /**
   * Actual CPU (in percent-secs) used by containers launched by the YARN application.
   * Computed by running a MapReduce job from Cloudera Service Monitor to
   * aggregate YARN usage metrics.
   * Available since v12.
   */
  @XmlElement
  public Double getContainerUsedCpuSeconds() {
    return containerUsedCpuSeconds;
  }

  public void setContainerUsedCpuSeconds(Double containerUsedCpuSeconds) {
    this.containerUsedCpuSeconds = containerUsedCpuSeconds;
  }

  /**
   * Actual VCore-secs used by containers launched by the YARN application.
   * Computed by running a MapReduce job from Cloudera Service Monitor to
   * aggregate YARN usage metrics.
   * Available since v12.
   */
  @XmlElement
  public Double getContainerUsedVcoreSeconds() {
    return containerUsedVcoreSeconds;
  }

  public void setContainerUsedVcoreSeconds(Double containerUsedVcoreSeconds) {
    this.containerUsedVcoreSeconds = containerUsedVcoreSeconds;
  }

  /**
   * Total memory (in mb-secs) allocated to containers launched by the YARN application.
   * Computed by running a MapReduce job from Cloudera Service Monitor to
   * aggregate YARN usage metrics.
   * Available since v12.
   */
  @XmlElement
  public Double getContainerAllocatedMemorySeconds() {
    return containerAllocatedMemorySeconds;
  }

  public void setContainerAllocatedMemorySeconds(
      Double containerAllocatedMemorySeconds) {
    this.containerAllocatedMemorySeconds = containerAllocatedMemorySeconds;
  }

  /**
   * Total vcore-secs allocated to containers launched by the YARN application.
   * Computed by running a MapReduce job from Cloudera Service Monitor to
   * aggregate YARN usage metrics.
   * Available since v12.
   */
  @XmlElement
  public Double getContainerAllocatedVcoreSeconds() {
    return containerAllocatedVcoreSeconds;
  }

  public void setContainerAllocatedVcoreSeconds(
      Double containerAllocatedVcoreSeconds) {
    this.containerAllocatedVcoreSeconds = containerAllocatedVcoreSeconds;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("name", name)
        .add("startTime", startTime)
        .add("endTime", endTime)
        .add("user", user)
        .add("pool", pool)
        .add("state", state)
        .add("progress", progress)
        .add("attributes", attributes)
        .add("mr2AppInfo", mr2AppInfo)
        .add("applicationTags", applicationTags)
        .add("allocatedMemorySeconds", allocatedMemorySeconds)
        .add("allocatedVcoreSeconds", allocatedVcoreSeconds)
        .add("allocatedMB", allocatedMB)
        .add("allocatedVCores", allocatedVCores)
        .add("runningContainers", runningContainers)
        .add("containerUsedMemorySeconds", containerUsedMemorySeconds)
        .add("containerUsedMemoryMax", containerUsedMemoryMax)
        .add("containerUsedCpuSeconds", containerUsedCpuSeconds)
        .add("containerUsedVcoreSeconds", containerUsedVcoreSeconds)
        .add("containerAllocatedMemorySeconds", containerAllocatedMemorySeconds)
        .add("containerAllocatedVcoreSeconds", containerAllocatedVcoreSeconds)
        .toString();
  }
}
