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

@XmlRootElement(name = "rollingUpgradeClusterArgs")
public class ApiRollingUpgradeClusterArgs {

  private Integer slaveBatchSize;
  private Integer sleepSeconds;
  private Integer slaveFailCountThreshold;

  /**
   * Number of slave roles to restart at a time.
   * Must be greater than zero. Default is 1.
   *
   * Please note that for HDFS, this number should be less than
   * the replication factor (default 3) to ensure data availability
   * during rolling restart.
   */
  @XmlElement
  public Integer getSlaveBatchSize() {
    return slaveBatchSize;
  }

  public void setSlaveBatchSize(int slaveBatchSize) {
    this.slaveBatchSize = slaveBatchSize;
  }

  /**
   * Number of seconds to sleep between restarts of slave role batches.
   *
   * Must be greater than or equal to 0. Default is 0.
   */
  @XmlElement
  public Integer getSleepSeconds() {
    return sleepSeconds;
  }

  public void setSleepSeconds(int sleepSeconds) {
    this.sleepSeconds = sleepSeconds;
  }

  /**
   * The threshold for number of slave batches that are allowed to fail
   * to restart before the entire command is considered failed.
   *
   * Must be greather than or equal to 0. Default is 0.
   * <p>
   * This argument is for ADVANCED users only.
   * </p>
   */
  @XmlElement
  public Integer getSlaveFailCountThreshold() {
    return slaveFailCountThreshold;
  }

  public void setSlaveFailCountThreshold(int slaveFailCountThreshold) {
    this.slaveFailCountThreshold = slaveFailCountThreshold;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("slaveBatchSize", slaveBatchSize)
                  .add("slaveFailCountThreshold", slaveFailCountThreshold)
                  .add("sleepSeconds", sleepSeconds)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRollingUpgradeClusterArgs other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(slaveBatchSize, other.slaveBatchSize) &&
        Objects.equal(slaveFailCountThreshold, other.slaveFailCountThreshold) &&
        Objects.equal(sleepSeconds, other.sleepSeconds));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(slaveBatchSize, slaveFailCountThreshold, sleepSeconds);
  }
}
