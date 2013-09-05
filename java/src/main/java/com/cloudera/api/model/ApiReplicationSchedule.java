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
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.base.Objects;

/**
 * A replication job schedule.
 * <p/>
 * Replication jobs have service-specific arguments. This object has methods
 * to retrieve arguments for all supported types of replication, but only
 * one argument type is allowed to be set; the backend will check that the
 * provided argument matches the service type where the replication is
 * being scheduled.
 * <p/>
 * The replication job's arguments should match the underlying service. Refer
 * to each property's documentation to find out which properties correspond to
 * which services.
 */
@XmlRootElement(name = "replicationSchedule")
public class ApiReplicationSchedule extends ApiSchedule<ApiReplicationCommand> {

  private ApiHdfsReplicationArguments hdfsArguments;
  private ApiHiveReplicationArguments hiveArguments;

  public ApiReplicationSchedule() {
    // For JAX-B.
  }

  public ApiReplicationSchedule(Long id, Date startTime, Date endTime,
      long interval, ApiScheduleInterval intervalUnit, boolean paused) {
    super(id, startTime, endTime, interval, intervalUnit, paused);
  }

  /** Arguments for HDFS replication commands. */
  @XmlElement
  public ApiHdfsReplicationArguments getHdfsArguments() {
    return hdfsArguments;
  }

  public void setHdfsArguments(ApiHdfsReplicationArguments hdfsArguments) {
    this.hdfsArguments = hdfsArguments;
  }

  /** Arguments for Hive replication commands. */
  @XmlElement
  public ApiHiveReplicationArguments getHiveArguments() {
    return hiveArguments;
  }

  public void setHiveArguments(ApiHiveReplicationArguments hiveArguments) {
    this.hiveArguments = hiveArguments;
  }

  /** List of active and/or finished commands for this schedule. */
  @XmlElementWrapper
  @Override
  public List<ApiReplicationCommand> getHistory() {
    return super.getHistory();
  }

  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) {
      return false;
    }

    ApiReplicationSchedule that = (ApiReplicationSchedule) o;
    return Objects.equal(hdfsArguments, that.getHdfsArguments()) &&
        Objects.equal(hiveArguments, that.getHiveArguments());
  }

  @Override
  public int hashCode() {
    return 31 * super.hashCode() + Objects.hashCode(hdfsArguments, hiveArguments);
  }

  @Override
  public String toString() {
    return super.toStringHelper()
      .add("hdfsArguments", hdfsArguments)
      .add("hiveArguments", hiveArguments)
      .toString();
  }

}
