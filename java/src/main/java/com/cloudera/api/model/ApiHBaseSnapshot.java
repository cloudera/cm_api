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

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An HBase snapshot descriptor.
 */
@XmlRootElement(name = "hbaseSnapshot")
public class ApiHBaseSnapshot {
  private String snapshotName;
  private String tableName;
  private Date creationTime;
  private Storage storage;

  /*
   * The location where a snapshot is stored.
   */
  public enum Storage {
    /**
     * The snapshot is in storage managed by the HBase service.
     */
    LOCAL,
    /**
     * The snapshot is in Amazon S3.
     */
    REMOTE_S3
  }

  // For JAX-B
  public ApiHBaseSnapshot() {
  }

  public ApiHBaseSnapshot(String snapshotName, String tableName) {
    this(snapshotName, tableName, null);
  }

  public ApiHBaseSnapshot(String snapshotName, String tableName,
      Date creationTime) {
    this(snapshotName, tableName, creationTime, null);
  }

  public ApiHBaseSnapshot(String snapshotName, String tableName,
      Date creationTime, Storage storage) {
    this.snapshotName = snapshotName;
    this.tableName = tableName;
    this.creationTime = creationTime;
    this.storage = storage;
  }

  /** Snapshot name. */
  @XmlElement
  public String getSnapshotName() {
   return snapshotName;
  }

  public void setSnapshotName(String snapshotName) {
    this.snapshotName = snapshotName;
  }

  /** Name of the table this snapshot is for. */
  @XmlElement
  public String getTableName() {
   return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /** Snapshot creation time. */
  @XmlElement
  public Date getCreationTime() {
   return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  /** The location where a snapshot is stored. */
  @XmlElement
  public Storage getStorage() {
   return storage;
  }

  public void setStorage(Storage storage) {
    this.storage = storage;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("snapshotName", snapshotName)
        .add("tableName", tableName)
        .add("creationTime", creationTime)
        .add("storage", storage)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHBaseSnapshot that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(snapshotName, that.getSnapshotName()) &&
        Objects.equal(tableName, that.getTableName()) &&
        Objects.equal(creationTime, that.getCreationTime()) &&
        Objects.equal(storage, that.storage));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(snapshotName, tableName, creationTime, storage);
  }
}
