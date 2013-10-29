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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An HDFS snapshot operation error.
 */
@XmlRootElement(name = "hdfsSnapshotError")
public class ApiHdfsSnapshotError {
  private String path;
  private String snapshotName;
  private String error;

  public ApiHdfsSnapshotError() {
    // For JAX-B
  }

  public ApiHdfsSnapshotError(String path, String snapshotName,
      String error) {
    this.path = path;
    this.snapshotName = snapshotName;
    this.error = error;
  }

  /** Path for which the snapshot error occurred. */
  @XmlElement
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  /**
   * Name of snapshot for which error occurred.
   */
  public String getSnapshotName() {
    return snapshotName;
  }

  public void setSnapshotName(String snapshotName) {
    this.snapshotName = snapshotName;
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
        .add("path", path)
        .add("snapshotName", snapshotName)
        .add("error", error)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsSnapshotError that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(path, that.getPath()) &&
        Objects.equal(snapshotName, that.getSnapshotName()) &&
        Objects.equal(error, that.getError()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(path, snapshotName, error);
  }
}
