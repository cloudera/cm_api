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
 * Arguments used for Rolling Upgrade command.
 */
@XmlRootElement(name="rollingUpgradeServicesArgs")
public class ApiRollingUpgradeServicesArgs {

  private Integer slaveBatchSize;
  private Integer sleepSeconds;
  private Integer slaveFailCountThreshold;
  private String upgradeFromCdhVersion;
  private String upgradeToCdhVersion;
  private List<String> upgradeServiceNames;

  /**
   * Current CDH Version of the services. Example versions are:
   * "5.1.0", "5.2.2" or "5.4.0"
   */
  @XmlElement
  public String getUpgradeFromCdhVersion() {
    return upgradeFromCdhVersion;
  }

  public void setUpgradeFromCdhVersion(String upgradeFromCdhVersion) {
    this.upgradeFromCdhVersion = upgradeFromCdhVersion;
  }

  /**
   * Target CDH Version for the services. The CDH version should already
   * be present and activated on the nodes. Example versions are:
   * "5.1.0", "5.2.2" or "5.4.0"
   */
  @XmlElement
  public String getUpgradeToCdhVersion() {
    return upgradeToCdhVersion;
  }

  public void setUpgradeToCdhVersion(String upgradeToCdhVersion) {
    this.upgradeToCdhVersion = upgradeToCdhVersion;
  }

  /**
   * Number of hosts with slave roles to upgrade at a time.
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
   *
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
   *
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
   * List of services to upgrade.
   * Only the services that support rolling upgrade should be included.
   */
  @XmlElement
  public List<String> getUpgradeServiceNames() {
    return upgradeServiceNames;
  }

  public void setUpgradeServiceNames(List<String> upgradeServiceNames) {
    this.upgradeServiceNames = upgradeServiceNames;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("slaveBatchSize", slaveBatchSize)
                  .add("slaveFailCountThreshold", slaveFailCountThreshold)
                  .add("sleepSeconds", sleepSeconds)
                  .add("upgradeFromCdhVersion", upgradeFromCdhVersion)
                  .add("upgradeToCdhVersion", upgradeToCdhVersion)
                  .add("upgradeServiceNames", upgradeServiceNames)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRollingUpgradeServicesArgs other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(slaveBatchSize, other.slaveBatchSize) &&
        Objects.equal(slaveFailCountThreshold, other.slaveFailCountThreshold) &&
        Objects.equal(sleepSeconds, other.sleepSeconds) &&
        Objects.equal(upgradeFromCdhVersion, other.upgradeFromCdhVersion) &&
        Objects.equal(upgradeToCdhVersion, other.upgradeToCdhVersion) &&
        Objects.equal(upgradeServiceNames, other.upgradeServiceNames));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(slaveBatchSize, slaveFailCountThreshold, sleepSeconds,
        upgradeFromCdhVersion, upgradeToCdhVersion, upgradeServiceNames);
  }
}
