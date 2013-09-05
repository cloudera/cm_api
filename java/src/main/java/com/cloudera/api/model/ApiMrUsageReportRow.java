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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "mrUsageReportRow")
public class ApiMrUsageReportRow {
  private String timePeriod;
  private String user;
  private String group;
  private long cpuSec;
  private long memoryBytes;
  private long jobCount;
  private long taskCount;
  private long durationSec;

  public ApiMrUsageReportRow() {
  }

  public ApiMrUsageReportRow(String timePeriod, String user, String group,
                             long cpuSec, long memoryBytes, long jobCount,
                             long taskCount, long durationSec) {
    this.timePeriod = timePeriod;
    this.user = user;
    this.group = group;
    this.cpuSec = cpuSec;
    this.memoryBytes = memoryBytes;
    this.jobCount = jobCount;
    this.taskCount = taskCount;
    this.durationSec = durationSec;
  }

  /**
   * The time period over which this report is generated.
   */
  @XmlElement
  public String getTimePeriod() {
    return timePeriod;
  }

  public void setTimePeriod(String timePeriod) {
    this.timePeriod = timePeriod;
  }

  /**
   * The user being reported.
   */
  @XmlElement
  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  /**
   * The group this user belongs to.
   */
  @XmlElement
  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * Amount of CPU time (in seconds) taken up this user's MapReduce
   * jobs.
   */
  @XmlElement
  public long getCpuSec() {
    return cpuSec;
  }

  public void setCpuSec(long cpuSec) {
    this.cpuSec = cpuSec;
  }

  /**
   * The sum of physical memory used (collected as a snapshot) by this user's
   * MapReduce jobs.
   */
  @XmlElement
  public long getMemoryBytes() {
    return memoryBytes;
  }

  public void setMemoryBytes(long memoryBytes) {
    this.memoryBytes = memoryBytes;
  }

  /**
   * Number of jobs.
   */
  @XmlElement
  public long getJobCount() {
    return jobCount;
  }

  public void setJobCount(long jobCount) {
    this.jobCount = jobCount;
  }

  /**
   * Number of tasks.
   */
  @XmlElement
  public long getTaskCount() {
    return taskCount;
  }

  public void setTaskCount(long taskCount) {
    this.taskCount = taskCount;
  }

  /**
   * Total duration of this user's MapReduce jobs.
   */
  @XmlElement
  public long getDurationSec() {
    return durationSec;
  }

  public void setDurationSec(long durationSec) {
    this.durationSec = durationSec;
  }
}
