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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

/**
 * A role represents a specific entity that participate in a service. Examples
 * are JobTrackers, DataNodes, HBase Masters. Each role is assigned a host
 * where it runs on.
 */
@XmlRootElement(name = "role")
public class ApiRole {

  private String name;
  private String type;
  private ApiServiceRef serviceRef;
  private ApiHostRef hostRef;
  private String roleUrl;
  private ApiRoleState roleState;
  private ApiHealthSummary healthSummary;
  private List<ApiHealthCheck> healthChecks;
  private Boolean configStale;
  private ApiConfigStalenessStatus configStalenessStatus;
  private HaStatus haStatus;
  private Boolean maintenanceMode;
  private List<ApiEntityType> maintenanceOwners;
  private ApiCommissionState commissionState;
  private ApiConfigList config;
  private ApiRoleConfigGroupRef roleConfigGroupRef;
  private ZooKeeperServerMode zooKeeperServerMode;

  public enum HaStatus {
    ACTIVE,
    STANDBY,
    UNKNOWN,
  }

  /**
   * The state of the Zookeeper server.
   */
  public enum ZooKeeperServerMode {
    /**
     * The ZooKeeper Server is a Standalone server.
     */
    STANDALONE,
    /**
     * The ZooKeeper Server is a Follower in a set of replicas.
     */
    REPLICATED_FOLLOWER,
    /**
     * The ZooKeeper Server is a Leader in a set of replicas.
     */
    REPLICATED_LEADER,
    /**
     * The ZooKeeper Server is going through Leader election in a set of replicas.
     */
    REPLICATED_LEADER_ELECTION,
    /**
     * The ZooKeeper Server is an Observer in a set of replicas.
     */
    REPLICATED_OBSERVER,
    /**
     * The status of the ZooKeeper Server could not be determined.
     */
    UNKNOWN,
  }

  public ApiRole() {
    // For JAX-B
  }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("type", type)
                  .add("serviceRef", serviceRef)
                  .add("hostRef", hostRef)
                  .add("roleState", roleState)
                  .add("healthSummary", healthSummary)
                  .add("healthChecks", healthChecks)
                  .add("commissionState", commissionState)
                  .add("roleConfigGroupRef", roleConfigGroupRef)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRole that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(hostRef, that.hostRef) &&
        Objects.equal(name, that.name) &&
        Objects.equal(serviceRef, that.serviceRef) &&
        Objects.equal(roleConfigGroupRef, that.roleConfigGroupRef));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, serviceRef, hostRef, roleConfigGroupRef);
  }

  /**
   * The name of the role.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The type of the role, e.g. NAMENODE, DATANODE, TASKTRACKER.
   */
  @XmlElement
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * A reference to the host where this role runs.
   */
  @XmlElement
  public ApiHostRef getHostRef() {
    return hostRef;
  }

  public void setHostRef(ApiHostRef hostRef) {
    this.hostRef = hostRef;
  }

  /**
   * Readonly. A reference to the parent service.
   */
  @XmlElement
  public ApiServiceRef getServiceRef() {
    return serviceRef;
  }

  public void setServiceRef(ApiServiceRef serviceRef) {
    this.serviceRef = serviceRef;
  }

  /**
   * Readonly. The configured run state of this role. Whether it's running, etc.
   */
  @XmlElement
  public ApiRoleState getRoleState() {
    return roleState;
  }

  public void setRoleState(ApiRoleState roleState) {
    this.roleState = roleState;
  }

  /**
   * Readonly. The commission state of this role.
   * Available since API v2.
   */
  @XmlElement
  public ApiCommissionState getCommissionState() {
    return commissionState;
  }

  public void setCommissionState(ApiCommissionState commissionState) {
    this.commissionState = commissionState;
  }

  /**
   * Readonly. The high-level health status of this role.
   */
  @XmlElement
  public ApiHealthSummary getHealthSummary() {
    return healthSummary;
  }

  public void setHealthSummary(ApiHealthSummary healthSummary) {
    this.healthSummary = healthSummary;
  }

  /**
   * Readonly. Expresses whether the role configuration is stale.
   *
   * @deprecated Use configStalenessStatus instead which exposes more staleness
   *             state.
   *             Deprecated since V6.
   */
  @Deprecated
  @XmlElement
  @JsonProperty(value = "configStale")
  public Boolean getConfigStale() {
    return configStale;
  }

  public void setConfigStale(Boolean configStale) {
    this.configStale = configStale;
  }

  /**
   * Readonly. Expresses the role's configuration staleness status.
   * Available since API v6.
   */
  @XmlElement
  public ApiConfigStalenessStatus getConfigStalenessStatus() {
    return configStalenessStatus;
  }

  public void setConfigStalenessStatus(ApiConfigStalenessStatus configStalenessStatus) {
    this.configStalenessStatus = configStalenessStatus;
  }

  /**
   * Readonly. The list of health checks of this service.
   */
  @XmlElementWrapper
  public List<ApiHealthCheck> getHealthChecks() {
    return healthChecks;
  }

  public void setHealthChecks(List<ApiHealthCheck> healthChecks) {
    this.healthChecks = healthChecks;
  }

  /** Readonly. The HA status of this role. */
  @XmlElement
  public HaStatus getHaStatus() {
    return haStatus;
  }

  public void setHaStatus(HaStatus haStatus) {
    this.haStatus = haStatus;
  }

  /**
   * Readonly.
   * Link into the Cloudera Manager web UI for this specific role.
   */
  @XmlElement
  public String getRoleUrl() {
    return roleUrl;
  }

  public void setRoleUrl(String roleUrl) {
    this.roleUrl = roleUrl;
  }

  /**
   * Readonly. Whether the role is in maintenance mode.
   * Available since API v2.
   */
  @XmlElement
  public Boolean getMaintenanceMode() {
    return maintenanceMode;
  }

  public void setMaintenanceMode(Boolean maintenanceMode) {
    this.maintenanceMode = maintenanceMode;
  }

  /**
   * Readonly. The list of objects that trigger this role to be in
   * maintenance mode.
   * Available since API v2.
   */
  @XmlElementWrapper(name = "maintenanceOwners")
  public List<ApiEntityType> getMaintenanceOwners() {
    return maintenanceOwners;
  }

  public void setMaintenanceOwners(List<ApiEntityType> maintenanceOwners) {
    this.maintenanceOwners = Lists.newArrayList(maintenanceOwners);
  }

  /**
   * The role configuration. Optional.
   */
  @XmlElement
  public ApiConfigList getConfig() {
    return config;
  }

  public void setConfig(ApiConfigList config) {
    this.config = config;
  }

  /**
   * Readonly. The reference to the role configuration group of this role.
   * Available since API v3.
   */
  @XmlElement
  public ApiRoleConfigGroupRef getRoleConfigGroupRef() {
    return roleConfigGroupRef;
  }

  public void setRoleConfigGroupRef(ApiRoleConfigGroupRef roleConfigGroupRef) {
    this.roleConfigGroupRef = roleConfigGroupRef;
  }

  /**
   * Readonly. The ZooKeeper server mode for this role. Note that for
   * non-ZooKeeper Server roles this will be null.
   * Available since API v6.
   */
  @XmlElement
  public ZooKeeperServerMode getZooKeeperServerMode() {
    return zooKeeperServerMode;
  }

  public void setZooKeeperServerMode(ZooKeeperServerMode zooKeeperServerMode) {
    this.zooKeeperServerMode = zooKeeperServerMode;
  }
}