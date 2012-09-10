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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * Arguments used for Rolling Restart commands.
 */
@XmlRootElement(name="rollingRestartArgs")
public class ApiRollingRestartArgs {

  private Integer slaveBatchSize;
  private Integer sleepSeconds;
  private Integer slaveFailCountThreshold;
  private Boolean staleConfigsOnly;
  private Boolean unUpgradedOnly;
  private List<String> restartRoleTypes;
  private List<String> restartRoleNames;
  
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
  
  /** Restart roles with stale configs only. */
  @XmlElement
  public Boolean getStaleConfigsOnly() {
    return staleConfigsOnly;
  }
  
  public void setStaleConfigsOnly(boolean staleConfigsOnly) {
    this.staleConfigsOnly = staleConfigsOnly;
  }
  
  /** Restart roles that haven't been upgraded yet. */
  @XmlElement
  public Boolean getUnUpgradedOnly() {
    return unUpgradedOnly;
  }
  
  public void setUnUpgradedOnly(boolean unUpgradedOnly) {
    this.unUpgradedOnly = unUpgradedOnly;
  }
  
  /** 
   * Role types to restart. If not specified, all startable roles are restarted. 
   * 
   * Both role types and role names should not be specified.
   */
  @XmlElement
  public List<String> getRestartRoleTypes() {
    return restartRoleTypes;
  }
  
  public void setRestartRoleTypes(List<String> restartRoleTypes) {
    this.restartRoleTypes = restartRoleTypes;
  }
  
  /** 
   * List of specific roles to restart.
   * If none are specified, then all roles of specified role types are restarted.
   * 
   * Both role types and role names should not be specified.
   */
  @XmlElement
  public List<String> getRestartRoleNames() {
    return restartRoleNames;
  }
  
  public void setRestartRoleNames(List<String> restartRoleNames) {
    this.restartRoleNames = restartRoleNames;
  }
  
  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("slaveBatchSize", slaveBatchSize)
                  .add("slaveFailCountThreshold", slaveFailCountThreshold)
                  .add("staleConfigsOnly", staleConfigsOnly)
                  .add("unUpgradedOnly", unUpgradedOnly)
                  .add("restartRoleTypes", restartRoleTypes)
                  .add("restartRoleNames", restartRoleNames)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRollingRestartArgs other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(slaveBatchSize, other.slaveBatchSize) &&
        Objects.equal(slaveFailCountThreshold, other.slaveFailCountThreshold) &&
        Objects.equal(staleConfigsOnly, other.staleConfigsOnly) &&
        Objects.equal(unUpgradedOnly, other.unUpgradedOnly) &&
        Objects.equal(restartRoleTypes, other.restartRoleTypes) &&
        Objects.equal(restartRoleNames, other.restartRoleNames));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(slaveBatchSize, slaveFailCountThreshold,
        staleConfigsOnly, unUpgradedOnly, restartRoleTypes, restartRoleNames);
  }
}
