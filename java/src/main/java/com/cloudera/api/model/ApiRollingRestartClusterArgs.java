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
 * Arguments used for Rolling Restart Cluster command.
 */
@XmlRootElement(name="rollingRestartClusterArgs")
public class ApiRollingRestartClusterArgs {

  private Integer slaveBatchSize;
  private Integer sleepSeconds;
  private Integer slaveFailCountThreshold;
  private Boolean staleConfigsOnly;
  private Boolean unUpgradedOnly;
  private Boolean redeployClientConfiguration;
  private ApiRolesToInclude rolesToInclude;
  private List<String> restartServiceNames;

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

  /**
   * Restart roles with stale configs only.
   */
  @XmlElement
  public Boolean getStaleConfigsOnly() {
    return staleConfigsOnly;
  }

  public void setStaleConfigsOnly(Boolean staleConfigsOnly) {
    this.staleConfigsOnly = staleConfigsOnly;
  }

  /**
   * Restart roles that haven't been upgraded yet.
   */
  @XmlElement
  public Boolean getUnUpgradedOnly() {
    return unUpgradedOnly;
  }

  public void setUnUpgradedOnly(Boolean unUpgradedOnly) {
    this.unUpgradedOnly = unUpgradedOnly;
  }

  /**
   * Re-deploy client configuration. Available since API v6.
   */
  @XmlElement
  public Boolean getRedeployClientConfiguration() {
    return redeployClientConfiguration;
  }

  public void setRedeployClientConfiguration(Boolean redeployClientConfiguration) {
    this.redeployClientConfiguration = redeployClientConfiguration;
  }

  /**
   * Role types to restart. Default is slave roles only.
   */
  @XmlElement
  public ApiRolesToInclude getRolesToInclude() {
    return rolesToInclude;
  }

  public void setRolesToInclude(ApiRolesToInclude rolesToInclude) {
    this.rolesToInclude = rolesToInclude;
  }

  /**
   * List of services to restart.
   */
  @XmlElement
  public List<String> getRestartServiceNames() {
    return restartServiceNames;
  }

  public void setRestartServiceNames(List<String> restartServiceNames) {
    this.restartServiceNames = restartServiceNames;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("slaveBatchSize", slaveBatchSize)
        .add("slaveFailCountThreshold", slaveFailCountThreshold)
        .add("sleepSeconds", sleepSeconds)
        .add("staleConfigsOnly", staleConfigsOnly)
        .add("unUpgradedOnly", unUpgradedOnly)
        .add("rolesToInclude", rolesToInclude)
        .add("restartServiceNames", restartServiceNames)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRollingRestartClusterArgs other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(slaveBatchSize, other.slaveBatchSize) &&
        Objects.equal(slaveFailCountThreshold, other.slaveFailCountThreshold) &&
        Objects.equal(sleepSeconds, other.sleepSeconds) &&
        Objects.equal(staleConfigsOnly, other.staleConfigsOnly) &&
        Objects.equal(unUpgradedOnly, other.unUpgradedOnly) &&
        Objects.equal(rolesToInclude, other.rolesToInclude) &&
        Objects.equal(restartServiceNames, other.restartServiceNames));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(slaveBatchSize, slaveFailCountThreshold, sleepSeconds,
        staleConfigsOnly, unUpgradedOnly, rolesToInclude, restartServiceNames);
  }
}
