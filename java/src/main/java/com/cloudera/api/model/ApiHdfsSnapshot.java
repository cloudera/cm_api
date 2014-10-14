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
import com.google.common.base.Preconditions;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * An HDFS snapshot descriptor.
 */
@XmlRootElement(name = "hdfsSnapshot")
public class ApiHdfsSnapshot {
  private String path;
  private String snapshotName;
  private String snapshotPath;
  private Date creationTime;

  // For JAX-B
  public ApiHdfsSnapshot() {
  }

  public ApiHdfsSnapshot(String snapshottedPath, String snapshotName,
      String snapshotPath) {
    this(snapshottedPath, snapshotName, snapshotPath, null);
  }

  public ApiHdfsSnapshot(String snapshottedPath, String snapshotName,
      String snapshotPath, Date creationTime) {
    validateSnapshotNameAndPath(snapshotName, snapshotPath);

    this.path = snapshottedPath;
    this.snapshotName = snapshotName;
    this.snapshotPath = snapshotPath;
    this.creationTime = creationTime;
  }

  /** Snapshotted path. */
  @XmlElement
  public String getPath() {
   return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  /** Snapshot name. */
  @XmlElement
  public String getSnapshotName() {
   return snapshotName;
  }

  public void setSnapshotName(String snapshotName) {
    validateSnapshotNameAndPath(snapshotName, snapshotPath);
    this.snapshotName = snapshotName;
  }

  /**
   * Read-only. Fully qualified path for the snapshot version of "path".
   * <p/>
   * For example, if a snapshot "s1" is present at "/a/.snapshot/s1, then the
   * snapshot path corresponding to "s1" for path "/a/b" will be
   * "/a/.snapshot/s1/b".
   */
  @XmlElement
  public String getSnapshotPath() {
   return snapshotPath;
  }

  public void setSnapshotPath(String snapshotPath) {
    validateSnapshotNameAndPath(snapshotName, snapshotPath);
    this.snapshotPath = snapshotPath;
  }

  /** Snapshot creation time. */
  @XmlElement
  public Date getCreationTime() {
   return creationTime;
  }

  public void setCreationTime(Date creationTime) {
    this.creationTime = creationTime;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("path", path)
        .add("snapshotName", snapshotName)
        .add("snapshotPath", snapshotPath)
        .add("creationTime", creationTime)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsSnapshot that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(path, that.getPath()) &&
        Objects.equal(snapshotName, that.getSnapshotName()) &&
        Objects.equal(snapshotPath, that.getSnapshotPath()) &&
        Objects.equal(creationTime, that.getCreationTime()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(path, snapshotName, snapshotPath, creationTime);
  }

  private static void validateSnapshotNameAndPath(String snapshotName,
      String snapshotPath) {
    if (snapshotName != null && snapshotPath != null) {
      Preconditions.checkArgument(
          snapshotPath.contains(
              "/" + ApiUtils.DOT_SNAPSHOT_DIR + "/" + snapshotName),
          "Passed in arguments snapshot name '%s' and snapshot path '%s' " +
              "does not match.",
          snapshotName, snapshotPath);
    }
  }
}
