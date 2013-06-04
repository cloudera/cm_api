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

@XmlRootElement(name = "hdfsUsageReportRow")
public class ApiHdfsUsageReportRow {
  private Date date;
  private String user;
  private long size;
  private long rawSize;
  private long numFiles;

  public ApiHdfsUsageReportRow() {
  }

  public ApiHdfsUsageReportRow(Date date, String user, long size, long rawSize,
                               long numFiles) {
    this.date = date;
    this.user = user;
    this.size = size;
    this.rawSize = rawSize;
    this.numFiles = numFiles;
  }

  /**
   * The date of the report row data.
   */
  @XmlElement
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
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
   * Total size (in bytes) of the files owned by this user. This does not
   * include replication in HDFS.
   */
  @XmlElement
  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  /**
   * Total size (in bytes) of all the replicas of all the files owned by
   * this user.
   */
  @XmlElement
  public long getRawSize() {
    return rawSize;
  }

  public void setRawSize(long rawSize) {
    this.rawSize = rawSize;
  }

  /**
   * Number of files owned by this user.
   */
  @XmlElement
  public long getNumFiles() {
    return numFiles;
  }

  public void setNumFiles(long numFiles) {
    this.numFiles = numFiles;
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsUsageReportRow other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(date, other.getDate()) &&
        Objects.equal(user, other.getUser()) &&
        Objects.equal(numFiles, other.getNumFiles()) &&
        Objects.equal(rawSize, other.getRawSize()) &&
        Objects.equal(size, other.getSize()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(date, user, size, rawSize, numFiles);
  }
}
