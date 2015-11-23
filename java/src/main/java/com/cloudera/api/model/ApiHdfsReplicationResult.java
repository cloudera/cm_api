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

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Detailed information about an HDFS replication job.
 */
@XmlRootElement(name = "hdfsReplicationResult")
public class ApiHdfsReplicationResult {

  private int progress;
  private List<ApiHdfsReplicationCounter> counters;
  private long numBytesDryRun;
  private long numFilesDryRun;
  private long numFilesExpected;
  private long numBytesExpected;
  private long numFilesCopied;
  private long numBytesCopied;
  private long numFilesSkipped;
  private long numBytesSkipped;
  private long numFilesDeleted;
  private long numFilesCopyFailed;
  private long numBytesCopyFailed;
  private String setupError;
  private String jobId;
  private String jobDetailsUri;
  private boolean dryRun;
  private List<String> snapshottedDirs;
  private List<String> failedFiles;
  private String runAsUser;

  /** The file copy progress percentage. */
  @XmlElement
  public int getProgress() {
    return progress;
  }

  public void setProgress(int progress) {
    this.progress = progress;
  }

  /**
   * The counters collected from the replication job.
   * <p/>
   * Starting with API v4, the full list of counters is only available in the
   * full view.
   */
  @XmlElementWrapper
  public List<ApiHdfsReplicationCounter> getCounters() {
    return counters;
  }

  public void setCounters(List<ApiHdfsReplicationCounter> counters) {
    this.counters = counters;
  }

  /** The number of files found to copy. */
  @XmlElement
  public long getNumFilesDryRun() {
    return numFilesDryRun;
  }

  public void setNumFilesDryRun(long numFilesDryRun) {
    this.numFilesDryRun = numFilesDryRun;
  }

  /** The number of bytes found to copy. */
  @XmlElement
  public long getNumBytesDryRun() {
    return numBytesDryRun;
  }

  public void setNumBytesDryRun(long numBytesDryRun) {
    this.numBytesDryRun = numBytesDryRun;
  }

  /** The number of files expected to be copied. */
  @XmlElement
  public long getNumFilesExpected() {
    return numFilesExpected;
  }

  public void setNumFilesExpected(long numFiledExpected) {
    this.numFilesExpected = numFiledExpected;
  }

  /** The number of bytes expected to be copied. */
  @XmlElement
  public long getNumBytesExpected() {
    return numBytesExpected;
  }

  public void setNumBytesExpected(long numBytesExpected) {
    this.numBytesExpected = numBytesExpected;
  }

  /** The number of files actually copied. */
  @XmlElement
  public long getNumFilesCopied() {
    return numFilesCopied;
  }

  public void setNumFilesCopied(long numFiledCopied) {
    this.numFilesCopied = numFiledCopied;
  }

  /** The number of bytes actually copied. */
  @XmlElement
  public long getNumBytesCopied() {
    return numBytesCopied;
  }

  public void setNumBytesCopied(long numBytesCopied) {
    this.numBytesCopied = numBytesCopied;
  }

  /**
   * The number of files that were unchanged and thus skipped during
   * copying.
   */
  @XmlElement
  public long getNumFilesSkipped() {
    return numFilesSkipped;
  }

  public void setNumFilesSkipped(long numFilesSkipped) {
    this.numFilesSkipped = numFilesSkipped;
  }

  /** The aggregate number of bytes in the skipped files. */
  @XmlElement
  public long getNumBytesSkipped() {
    return numBytesSkipped;
  }

  public void setNumBytesSkipped(long numBytesSkipped) {
    this.numBytesSkipped = numBytesSkipped;
  }

  /**
   * The number of files deleted since they were present at destination, but
   * missing from source.
   */
  @XmlElement
  public long getNumFilesDeleted() {
    return numFilesDeleted;
  }

  public void setNumFilesDeleted(long numFilesDeleted) {
    this.numFilesDeleted = numFilesDeleted;
  }

  /** The number of files for which copy failed. */
  @XmlElement
  public long getNumFilesCopyFailed() {
    return numFilesCopyFailed;
  }

  public void setNumFilesCopyFailed(long numFilesCopyFailed) {
    this.numFilesCopyFailed = numFilesCopyFailed;
  }

  /** The aggregate number of bytes in the files for which copy failed. */
  @XmlElement
  public long getNumBytesCopyFailed() {
    return numBytesCopyFailed;
  }

  public void setNumBytesCopyFailed(long numBytesCopyFailed) {
    this.numBytesCopyFailed = numBytesCopyFailed;
  }

  // TODO: The number of file deletions that failed are not tracked currently
  // in the custom distcp. Property needs to be added once available.
  //
  // Should also consider whether the number of bytes deleted should be tracked
  // by the custom distcp and exposed here.

  /** The error that happened during job setup, if any. */
  @XmlElement
  public String getSetupError() {
    return setupError;
  }

  public void setSetupError(String setupError) {
    this.setupError = setupError;
  }

  /**
   * Read-only. The MapReduce job ID for the replication job.
   * Available since API v4.
   * <p/>
   * This can be used to query information about the replication job from the
   * MapReduce server where it was executed. Refer to the "/activities"
   * resource for services for further details.
   */
  public String getJobId() {
    return jobId;
  }

  public void setJobId(String jobId) {
    this.jobId = jobId;
  }

  /**
   * Read-only. The URI (relative to the CM server's root) where to find the
   * Activity Monitor page for the job. Available since API v4.
   */
  public String getJobDetailsUri() {
    return jobDetailsUri;
  }

  public void setJobDetailsUri(String jobDetailsUri) {
    this.jobDetailsUri = jobDetailsUri;
  }

  /** Whether this was a dry run. */
  @XmlElement
  public boolean isDryRun() {
    return dryRun;
  }

  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  /**
   * The list of directories for which snapshots were taken and used as part of
   * this replication.
   */
  @XmlElement
  public List<String> getSnapshottedDirs() {
    return snapshottedDirs;
  }

  public void setSnapshottedDirs(List<String> snapshottedDirs) {
    this.snapshottedDirs = snapshottedDirs;
  }

  /**
   * returns run-as user name.
   * Available since API v11.
   */
  public String getRunAsUser() {
    return runAsUser;
  }

  public void setRunAsUser(String runAsUser) {
    this.runAsUser = runAsUser;
  }

  /**
   * The list of files that failed during replication.
   * Available since API v11.
   */
  public List<String> getFailedFiles() {
    return failedFiles;
  }

  public void setFailedFiles(List<String> failedFiles) {
    this.failedFiles = failedFiles;
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsReplicationResult that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(progress, that.getProgress()) &&
        Objects.equal(counters, that.getCounters()) &&
        Objects.equal(setupError, that.getSetupError()) &&
        dryRun == that.isDryRun() &&
        Objects.equal(snapshottedDirs, that.getSnapshottedDirs())) &&
        Objects.equal(failedFiles, that.getFailedFiles()) &&
        Objects.equal(runAsUser, that.getRunAsUser());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(progress, counters, setupError, dryRun,
        snapshottedDirs, failedFiles, runAsUser);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("progress", progress)
        .add("counters", counters)
        .add("setupError", setupError)
        .add("dryRun", dryRun)
        .add("snapshottedDirs", snapshottedDirs)
        .add("failedFiles", failedFiles)
        .add("runAsUser", runAsUser)
        .toString();
  }

}
