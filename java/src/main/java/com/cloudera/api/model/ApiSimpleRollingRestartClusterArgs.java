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
 * Basic arguments used for Rolling Restart Cluster commands.
 */
@XmlRootElement(name = "simpleRollingRestartClusterArgs")
public class ApiSimpleRollingRestartClusterArgs {

  private Integer slaveBatchSize;
  private Integer sleepSeconds;
  private Integer slaveFailCountThreshold;

  /**
   * Number of hosts with slave roles to restart at a time.
   * Must be greater than zero. Default is 1.
   */
  @XmlElement
  public Integer getSlaveBatchSize() {
    return slaveBatchSize;
  }

  public void setSlaveBatchSize(Integer slaveBatchSize) {
    this.slaveBatchSize = slaveBatchSize;
  }

  /**
   * Number of seconds to sleep between restarts of slave host batches.
   * <p>
   * Must be greater than or equal to 0. Default is 0.
   */
  @XmlElement
  public Integer getSleepSeconds() {
    return sleepSeconds;
  }

  public void setSleepSeconds(Integer sleepSeconds) {
    this.sleepSeconds = sleepSeconds;
  }

  /**
   * The threshold for number of slave host batches that are allowed to fail
   * to restart before the entire command is considered failed.
   * <p>
   * Must be greater than or equal to 0. Default is 0.
   * <p>
   * This argument is for ADVANCED users only.
   * </p>
   */
  @XmlElement
  public Integer getSlaveFailCountThreshold() {
    return slaveFailCountThreshold;
  }

  public void setSlaveFailCountThreshold(Integer slaveFailCountThreshold) {
    this.slaveFailCountThreshold = slaveFailCountThreshold;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("slaveBatchSize", slaveBatchSize)
        .add("sleepSeconds", sleepSeconds)
        .add("slaveFailCountThreshold", slaveFailCountThreshold)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiSimpleRollingRestartClusterArgs that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(slaveBatchSize, that.slaveBatchSize) &&
        Objects.equal(sleepSeconds, that.sleepSeconds) &&
        Objects.equal(slaveFailCountThreshold, that.slaveFailCountThreshold));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(slaveBatchSize, sleepSeconds,
        slaveFailCountThreshold);
  }
}
