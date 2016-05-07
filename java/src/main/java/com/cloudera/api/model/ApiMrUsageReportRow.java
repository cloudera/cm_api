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
  private Long cpuSec;
  private Long memoryBytes;
  private Long jobCount;
  private Long taskCount;
  private Long durationSec;
  private Long failedMaps;
  private Long totalMaps;
  private Long failedReduces;
  private Long totalReduces;
  private Long mapInputBytes;
  private Long mapOutputBytes;
  private Long hdfsBytesRead;
  private Long hdfsBytesWritten;
  private Long localBytesRead;
  private Long localBytesWritten;
  private Long dataLocalMaps;
  private Long rackLocalMaps;

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

  public ApiMrUsageReportRow(String timePeriod, String user, String group,
      long cpuSec, long memoryBytes, long jobCount, long taskCount,
      long durationSec, long failedMaps, long totalMaps, long failedReduces,
      long totalReduces, long mapInputBytes, long mapOutputBytes,
      long hdfsBytesRead, long hdfsBytesWritten, long localBytesRead,
      long localBytesWritten, long dataLocalMaps, long rackLocalMaps) {
    this(timePeriod, user, group, cpuSec, memoryBytes, jobCount, taskCount,
        durationSec);
    this.failedMaps = failedMaps;
    this.totalMaps = totalMaps;
    this.failedReduces = failedReduces;
    this.totalReduces = totalReduces;
    this.mapInputBytes = mapInputBytes;
    this.mapOutputBytes = mapOutputBytes;
    this.hdfsBytesRead = hdfsBytesRead;
    this.hdfsBytesWritten = hdfsBytesWritten;
    this.localBytesRead = localBytesRead;
    this.localBytesWritten = localBytesWritten;
    this.dataLocalMaps = dataLocalMaps;
    this.rackLocalMaps = rackLocalMaps;
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
  public Long getCpuSec() {
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
  public Long getMemoryBytes() {
    return memoryBytes;
  }

  public void setMemoryBytes(long memoryBytes) {
    this.memoryBytes = memoryBytes;
  }

  /**
   * Number of jobs.
   */
  @XmlElement
  public Long getJobCount() {
    return jobCount;
  }

  public void setJobCount(long jobCount) {
    this.jobCount = jobCount;
  }

  /**
   * Number of tasks.
   */
  @XmlElement
  public Long getTaskCount() {
    return taskCount;
  }

  public void setTaskCount(long taskCount) {
    this.taskCount = taskCount;
  }

  /**
   * Total duration of this user's MapReduce jobs.
   */
  @XmlElement
  public Long getDurationSec() {
    return durationSec;
  }

  public void setDurationSec(long durationSec) {
    this.durationSec = durationSec;
  }

  /**
   * Failed maps of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getFailedMaps() {
    return failedMaps;
  }

  public void setFailedMaps(long failedMaps) {
    this.failedMaps = failedMaps;
  }

  /**
   * Total maps of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getTotalMaps() {
    return totalMaps;
  }

  public void setTotalMaps(long totalMaps) {
    this.totalMaps = totalMaps;
  }

  /**
   * Failed reduces of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getFailedReduces() {
    return failedReduces;
  }

  public void setFailedReduces(long failedReduces) {
    this.failedReduces = failedReduces;
  }

  /**
   * Total reduces of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getTotalReduces() {
    return totalReduces;
  }

  public void setTotalReduces(long totalReduces) {
    this.totalReduces = totalReduces;
  }

  /**
   * Map input bytes of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getMapInputBytes() {
    return mapInputBytes;
  }

  public void setMapInputBytes(long mapInputBytes) {
    this.mapInputBytes = mapInputBytes;
  }

  /**
   * Map output bytes of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getMapOutputBytes() {
    return mapOutputBytes;
  }

  public void setMapOutputBytes(long mapOutputBytes) {
    this.mapOutputBytes = mapOutputBytes;
  }

  /**
   * HDFS bytes read of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getHdfsBytesRead() {
    return hdfsBytesRead;
  }

  public void setHdfsBytesRead(long hdfsBytesRead) {
    this.hdfsBytesRead = hdfsBytesRead;
  }

  /**
   * HDFS bytes written of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getHdfsBytesWritten() {
    return hdfsBytesWritten;
  }

  public void setHdfsBytesWritten(long hdfsBytesWritten) {
    this.hdfsBytesWritten = hdfsBytesWritten;
  }

  /**
   * Local bytes read of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getLocalBytesRead() {
    return localBytesRead;
  }

  public void setLocalBytesRead(long localBytesRead) {
    this.localBytesRead = localBytesRead;
  }

  /**
   * Local bytes written of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getLocalBytesWritten() {
    return localBytesWritten;
  }

  public void setLocalBytesWritten(long localBytesWritten) {
    this.localBytesWritten = localBytesWritten;
  }

  /**
   * Data local maps of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getDataLocalMaps() {
    return dataLocalMaps;
  }

  public void setDataLocalMaps(long dataLocalMaps) {
    this.dataLocalMaps = dataLocalMaps;
  }

  /**
   * Rack local maps of this user's MapReduce jobs.
   * Available since v11.
   */
  @XmlElement
  public Long getRackLocalMaps() {
    return rackLocalMaps;
  }

  public void setRackLocalMaps(long rackLocalMaps) {
    this.rackLocalMaps = rackLocalMaps;
  }
}
