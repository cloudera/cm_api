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

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * Information about a replication command.
 * <p/>
 * This object holds all the information a regular ApiCommand object provides,
 * and adds specific information about the results of a replication command.
 * <p/>
 * Depending on the type of the service where the replication was run, a
 * different result property will be populated.
 */
@XmlRootElement(name = "replicationCommand")
public class ApiReplicationCommand extends ApiCommand {

  private ApiHdfsReplicationResult hdfsResult;
  private ApiHiveReplicationResult hiveResult;

  public ApiReplicationCommand() {
    // For JAX-B
  }

  /** Results for replication commands on HDFS services. */
  @XmlElement
  public ApiHdfsReplicationResult getHdfsResult() {
    return hdfsResult;
  }

  public void setHdfsResult(ApiHdfsReplicationResult hdfsResult) {
    this.hdfsResult = hdfsResult;
  }

  /** Results for replication commands on Hive services. */
  @XmlElement
  public ApiHiveReplicationResult getHiveResult() {
    return hiveResult;
  }

  public void setHiveResult(ApiHiveReplicationResult hiveResult) {
    this.hiveResult = hiveResult;
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) {
      return false;
    }
    ApiReplicationCommand that = (ApiReplicationCommand) o;
    return Objects.equal(hdfsResult, that.getHdfsResult()) &&
        Objects.equal(hiveResult, that.getHiveResult());
  }

  @Override
  public int hashCode() {
    return 31 * super.hashCode() + Objects.hashCode(hdfsResult, hiveResult);
  }

  @Override
  public String toString() {
    return super.toStringHelper()
        .add("hdfsResult", hdfsResult)
        .add("hiveResult", hiveResult)
        .toString();
  }

}
