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

import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Information about snapshot commands.
 * <p/>
 * This object holds all the information a regular ApiCommand object provides,
 * and adds specific information about the results of a snapshot command.
 * <p/>
 * Depending on the type of the service where the snapshot command was run, a
 * different result property will be populated.
 */
@XmlRootElement(name = "snapshotCommand")
public class ApiSnapshotCommand extends ApiCommand {
  private ApiHBaseSnapshotResult hbaseResult;
  private ApiHdfsSnapshotResult hdfsResult;

  /** Results for snapshot commands on HBase services. */
  @XmlElement
  public ApiHBaseSnapshotResult getHBaseResult() {
    return hbaseResult;
  }

  public void setHBaseResult(ApiHBaseSnapshotResult hbaseResult) {
    this.hbaseResult = hbaseResult;
  }

  /** Results for snapshot commands on Hdfs services. */
  @XmlElement
  public ApiHdfsSnapshotResult getHdfsResult() {
    return hdfsResult;
  }

  public void setHdfsResult(ApiHdfsSnapshotResult hdfsResult) {
    this.hdfsResult = hdfsResult;
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) {
      return false;
    }
    ApiSnapshotCommand that = (ApiSnapshotCommand) o;
    return Objects.equal(hbaseResult, that.getHBaseResult()) &&
        Objects.equal(hdfsResult, that.getHdfsResult());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), hbaseResult, hdfsResult);
  }

  @Override
  public String toString() {
    return super.toStringHelper()
        .add("hbaseResult", hbaseResult)
        .add("hdfsResult", hdfsResult)
        .toString();
  }
}
