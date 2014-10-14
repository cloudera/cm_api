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

/** Detailed information about an HBase snapshot command. */
@XmlRootElement(name = "hbaseSnapshotResult")
public class ApiHBaseSnapshotResult {
  // TODO: For now, the list based information is populated only in full view.
  // But given the scaling issues we have had with replication, should consider
  // if separate API with paging support should be added for them.

  private Integer processedTableCount;
  private List<String> processedTables;
  private Integer unprocessedTableCount;
  private List<String> unprocessedTables;
  private Integer createdSnapshotCount;
  private List<ApiHBaseSnapshot> createdSnapshots;
  private Integer deletedSnapshotCount;
  private List<ApiHBaseSnapshot> deletedSnapshots;
  private Integer creationErrorCount;
  private List<ApiHBaseSnapshotError> creationErrors;
  private Integer deletionErrorCount;
  private List<ApiHBaseSnapshotError> deletionErrors;

  /** Number of processed tables. */
  @XmlElement
  public Integer getProcessedTableCount() {
    return processedTableCount;
  }

  public void setProcessedTableCount(Integer processedTableCount) {
    this.processedTableCount = processedTableCount;
  }

  /**
   * The list of processed tables.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<String> getProcessedTables() {
    return processedTables;
  }

  public void setProcessedTables(List<String> tables) {
    this.processedTables = tables;
  }

  /** Number of unprocessed tables. */
  @XmlElement
  public Integer getUnprocessedTableCount() {
    return unprocessedTableCount;
  }

  public void setUnprocessedTableCount(Integer unprocessedTableCount) {
    this.unprocessedTableCount = unprocessedTableCount;
  }

  /**
   * The list of unprocessed tables. Note that tables that are currently being
   * processed will also be included in this list.
   * <p/>
   * This is only available in the full view.
   */
  @XmlElementWrapper
  public List<String> getUnprocessedTables() {
    return unprocessedTables;
  }

  public void setUnprocessedTables(List<String> tables) {
    this.unprocessedTables = tables;
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
  public List<ApiHBaseSnapshot> getCreatedSnapshots() {
    return createdSnapshots;
  }

  public void setCreatedSnapshots(List<ApiHBaseSnapshot> createdSnapshots) {
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
  public List<ApiHBaseSnapshot> getDeletedSnapshots() {
    return deletedSnapshots;
  }

  public void setDeletedSnapshots(List<ApiHBaseSnapshot> deletedSnapshots) {
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
  public List<ApiHBaseSnapshotError> getCreationErrors() {
    return creationErrors;
  }

  public void setCreationErrors(List<ApiHBaseSnapshotError> creationErrors) {
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
  public List<ApiHBaseSnapshotError> getDeletionErrors() {
    return deletionErrors;
  }

  public void setDeletionErrors(List<ApiHBaseSnapshotError> deletionErrors) {
    this.deletionErrors = deletionErrors;
  }

  @Override
  public boolean equals(Object o) {
    ApiHBaseSnapshotResult that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(processedTableCount, that.getProcessedTableCount()) &&
        Objects.equal(processedTables, that.getProcessedTables()) &&
        Objects.equal(unprocessedTableCount, that.getUnprocessedTableCount()) &&
        Objects.equal(unprocessedTables, that.getUnprocessedTables()) &&
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
    return Objects.hashCode(processedTableCount, processedTables,
        unprocessedTableCount, unprocessedTables, createdSnapshotCount,
        createdSnapshots, deletedSnapshotCount, deletedSnapshots,
        creationErrorCount, creationErrors, deletionErrorCount,
        deletionErrors);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("processedTableCount", processedTableCount)
        .add("processedTables", processedTables)
        .add("unprocessedTableCount", unprocessedTableCount)
        .add("unprocessedTables", unprocessedTables)
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
