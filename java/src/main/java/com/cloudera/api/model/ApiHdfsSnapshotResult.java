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

/** Detailed information about an HDFS snapshot command. */
@XmlRootElement(name = "hdfsSnapshotResult")
public class ApiHdfsSnapshotResult {
  // TODO: For now, the list based information is populated only in full view.
  // But given the scaling issues we have had with replication, should consider
  // if separate API with paging support should be added for them.

  private Integer processedPathCount;
  private List<String> processedPaths;
  private Integer unprocessedPathCount;
  private List<String> unprocessedPaths;
  private Integer createdSnapshotCount;
  private List<ApiHdfsSnapshot> createdSnapshots;
  private Integer deletedSnapshotCount;
  private List<ApiHdfsSnapshot> deletedSnapshots;
  private Integer creationErrorCount;
  private List<ApiHdfsSnapshotError> creationErrors;
  private Integer deletionErrorCount;
  private List<ApiHdfsSnapshotError> deletionErrors;

  /** Number of processed paths. */
  @XmlElement
  public Integer getProcessedPathCount() {
    return processedPathCount;
  }

  public void setProcessedPathCount(Integer processedPathCount) {
    this.processedPathCount = processedPathCount;
  }

  /**
   * The list of processed paths.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<String> getProcessedPaths() {
    return processedPaths;
  }

  public void setProcessedPaths(List<String> paths) {
    this.processedPaths = paths;
  }

  /** Number of unprocessed paths. */
  @XmlElement
  public Integer getUnprocessedPathCount() {
    return unprocessedPathCount;
  }

  public void setUnprocessedPathCount(Integer unprocessedPathCount) {
    this.unprocessedPathCount = unprocessedPathCount;
  }

  /**
   * The list of unprocessed paths. Note that paths that are currently being
   * processed will also be included in this list.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<String> getUnprocessedPaths() {
    return unprocessedPaths;
  }

  public void setUnprocessedPaths(List<String> paths) {
    this.unprocessedPaths = paths;
  }

  /** Number of snapshots created. */
  @XmlElement
  public Integer getCreatedSnapshotCount() {
    return createdSnapshotCount;
  }

  public void setCreatedSnapshotCount(Integer createdSnapshotCount) {
    this.createdSnapshotCount = createdSnapshotCount;
  }

  /**
   * List of snapshots created.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<ApiHdfsSnapshot> getCreatedSnapshots() {
    return createdSnapshots;
  }

  public void setCreatedSnapshots(List<ApiHdfsSnapshot> createdSnapshots) {
    this.createdSnapshots = createdSnapshots;
  }

  /** Number of snapshots deleted. */
  @XmlElement
  public Integer getDeletedSnapshotCount() {
    return deletedSnapshotCount;
  }

  public void setDeletedSnapshotCount(Integer deletedSnapshotCount) {
    this.deletedSnapshotCount = deletedSnapshotCount;
  }

  /**
   * List of snapshots deleted.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<ApiHdfsSnapshot> getDeletedSnapshots() {
    return deletedSnapshots;
  }

  public void setDeletedSnapshots(List<ApiHdfsSnapshot> deletedSnapshots) {
    this.deletedSnapshots = deletedSnapshots;
  }

  /** Number of errors detected when creating snapshots. */
  @XmlElement
  public Integer getCreationErrorCount() {
    return creationErrorCount;
  }

  public void setCreationErrorCount(Integer creationErrorCount) {
    this.creationErrorCount = creationErrorCount;
  }

  /**
   * List of errors encountered when creating snapshots.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<ApiHdfsSnapshotError> getCreationErrors() {
    return creationErrors;
  }

  public void setCreationErrors(List<ApiHdfsSnapshotError> creationErrors) {
    this.creationErrors = creationErrors;
  }

  /** Number of errors detected when deleting snapshots. */
  @XmlElement
  public Integer getDeletionErrorCount() {
    return deletionErrorCount;
  }

  public void setDeletionErrorCount(Integer deletionErrorCount) {
    this.deletionErrorCount = deletionErrorCount;
  }

  /**
   * List of errors encountered when deleting snapshots.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<ApiHdfsSnapshotError> getDeletionErrors() {
    return deletionErrors;
  }

  public void setDeletionErrors(List<ApiHdfsSnapshotError> deletionErrors) {
    this.deletionErrors = deletionErrors;
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsSnapshotResult that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(processedPathCount, that.getProcessedPathCount()) &&
        Objects.equal(processedPaths, that.getProcessedPaths()) &&
        Objects.equal(unprocessedPathCount, that.getUnprocessedPathCount()) &&
        Objects.equal(unprocessedPaths, that.getUnprocessedPaths()) &&
        Objects.equal(createdSnapshotCount, that.getCreatedSnapshotCount()) &&
        Objects.equal(createdSnapshots, that.getCreatedSnapshots()) &&
        Objects.equal(deletedSnapshotCount, that.getDeletedSnapshotCount()) &&
        Objects.equal(deletedSnapshots, that.getDeletedSnapshots()) &&
        Objects.equal(creationErrorCount, that.getCreationErrorCount()) &&
        Objects.equal(creationErrors, that.getCreationErrors()) &&
        Objects.equal(deletionErrorCount, that.getDeletionErrorCount()) &&
        Objects.equal(deletionErrors, that.getDeletionErrors()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(processedPathCount, processedPaths,
        unprocessedPathCount, unprocessedPaths, createdSnapshotCount,
        createdSnapshots, deletedSnapshotCount, deletedSnapshots,
        creationErrorCount, creationErrors, deletionErrorCount,
        deletionErrors);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("processedPathCount", processedPathCount)
        .add("processedPaths", processedPaths)
        .add("unprocessedPathCount", unprocessedPathCount)
        .add("unprocessedPaths", unprocessedPaths)
        .add("createdSnapshotCount", createdSnapshotCount)
        .add("createdSnapshots", createdSnapshots)
        .add("deletedSnapshotCount", deletedSnapshotCount)
        .add("deletedSnapshots", deletedSnapshots)
        .add("creationErrorCount", creationErrorCount)
        .add("creationErrors", creationErrors)
        .add("deletionErrorCount", deletionErrorCount)
        .add("deletionErrors", deletionErrors)
        .toString();
  }
}
