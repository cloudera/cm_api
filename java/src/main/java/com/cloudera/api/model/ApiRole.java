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
import com.google.common.collect.Lists;

import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

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
  private HaStatus haStatus;
  private Boolean maintenanceMode;
  private List<ApiEntityType> maintenanceOwners;
  private ApiCommissionState commissionState;
  private ApiConfigList config;

  public enum HaStatus {
    ACTIVE,
    STANDBY,
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
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ApiRole that = (ApiRole) o;
    return Objects.equal(hostRef, that.hostRef) &&
        Objects.equal(name, that.name) &&
        Objects.equal(serviceRef, that.serviceRef);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, serviceRef, hostRef, config);
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
   */
  @XmlElement
  @JsonProperty(value = "configStale")
  public Boolean isConfigStale() {
    return configStale;
  }

  public void setConfigStale(Boolean configStale) {
    this.configStale = configStale;
  }

  /**
   * Readonly. The list of health checks of this service.
   */
  @XmlElement
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
  @XmlElementWrapper
  @XmlElement(name = "maintenanceOwner")
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
  
}
