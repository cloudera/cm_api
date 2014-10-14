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
import com.cloudera.api.model.ApiHBaseSnapshot.Storage;
import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A HBase snapshot operation error.
 */
@XmlRootElement(name = "hbaseSnapshotError")
public class ApiHBaseSnapshotError {
  private String tableName;
  private String snapshotName;
  private Storage storage;
  private String error;

  public ApiHBaseSnapshotError() {
    // For JAX-B
  }

  public ApiHBaseSnapshotError(String tableName, String snapshotName,
      String error) {
    this(tableName, snapshotName, null /* no storage specified */, error);
  }

  public ApiHBaseSnapshotError(String tableName, String snapshotName,
      Storage storage, String error) {
    this.tableName = tableName;
    this.snapshotName = snapshotName;
    this.storage = storage;
    this.error = error;
  }

  /** Name of the table. */
  @XmlElement
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /** Name of the snapshot. */
  @XmlElement
  public String getSnapshotName() {
    return snapshotName;
  }

  public void setSnapshotName(String snapshotName) {
    this.snapshotName = snapshotName;
  }

  /** The location of the snapshot. */
  @XmlElement
  public Storage getStorage() {
    return storage;
  }

  public void setStorage(Storage storage) {
    this.storage = storage;
  }

  /** Description of the error. */
  @XmlElement
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("tableName", tableName)
        .add("snapshotName", snapshotName)
        .add("storage", storage)
        .add("error", error)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHBaseSnapshotError that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(tableName, that.getTableName()) &&
        Objects.equal(snapshotName, that.getSnapshotName()) &&
        Objects.equal(storage, that.getStorage()) &&
        Objects.equal(error, that.getError()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tableName, snapshotName, storage, error);
  }
}
