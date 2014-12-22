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
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

/**
 * This is the model for a host in the system.
 */
@XmlRootElement(name = "host")
public class ApiHost {

  private String hostId;
  private String ipAddress;
  private String hostname;
  private String rackId;
  private String hostUrl;
  private Date lastHeartbeat;
  private ApiHealthSummary healthSummary;
  private List<ApiHealthCheck> healthChecks;
  private List<ApiRoleRef> roleRefs;
  private Boolean maintenanceMode;
  private List<ApiEntityType> maintenanceOwners;
  private ApiCommissionState commissionState;
  private ApiConfigList config;
  private Long numCores;
  private Long numPhysicalCores;
  private Long totalPhysMemBytes;

  public ApiHost() {
    // for JAX-B
  }

  /**
   * Copy constructor
   * @param host The host to copy
   */
  public ApiHost(ApiHost host) {
    Preconditions.checkNotNull(host, "Cannot copy a null host");
    this.hostId = host.getHostId();
    this.ipAddress = host.getIpAddress();
    this.hostname = host.getHostname();
    this.rackId = host.getRackId();
    this.hostUrl = host.getHostUrl();
    this.lastHeartbeat = host.getLastHeartbeat();
    this.healthSummary = host.getHealthSummary();
    this.healthChecks = host.getHealthChecks();
    List<ApiRoleRef> refs = host.getRoleRefs();
    if (refs != null) {
      this.roleRefs = Lists.newArrayList(refs);
    } else {
      this.roleRefs = Lists.newArrayList();
    }
    this.maintenanceMode = host.getMaintenanceMode();
    this.maintenanceOwners = host.getMaintenanceOwners();
    this.commissionState = host.getCommissionState();
    this.config = host.getConfig();
    this.numCores = host.getNumCores();
    this.numPhysicalCores = host.getNumPhysicalCores();
    this.totalPhysMemBytes = host.getTotalPhysMemBytes();
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("hostId", hostId)
                  .add("ipAddress", ipAddress)
                  .add("hostname", hostname)
                  .add("rackId", rackId)
                  .add("lastHeartbeat", lastHeartbeat)
                  .add("healthSummary", healthSummary)
                  .add("healthChecks", healthChecks)
                  .add("roleRefs", roleRefs)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHost that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(hostId, that.getHostId()));
  }

  @Override
  public int hashCode() {
    return hostId != null ? hostId.hashCode() : 0;
  }

  /**
   * A unique host identifier.
   * This is not the same as the hostname (FQDN). It is a distinct
   * value that remains the same even if the hostname changes.
   */
  @XmlElement
  public String getHostId() {
    return hostId;
  }

  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

  /** The host IP address. This field is not mutable after the initial creation. */
  @XmlElement
  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  /** The hostname. This field is not mutable after the initial creation. */
  @XmlElement
  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  /** The rack ID for this host. */
  @XmlElement
  public String getRackId() {
    return rackId;
  }

  public void setRackId(String rackId) {
    this.rackId = rackId;
  }

  /**
   * Readonly. Requires "full" view.
   * When the host agent sent the last heartbeat.
   */
  @XmlElement
  public Date getLastHeartbeat() {
    return lastHeartbeat;
  }

  public void setLastHeartbeat(Date lastHeartbeat) {
    this.lastHeartbeat = lastHeartbeat;
  }

  /**
   * Readonly. Requires "full" view.
   * The list of roles assigned to this host.
   */
  @XmlElementWrapper(name = "roleRefs")
  public List<ApiRoleRef> getRoleRefs() {
    return roleRefs;
  }

  public void setRoleRefs(List<ApiRoleRef> roleRefs) {
    this.roleRefs = roleRefs;
  }

  /**
   * Readonly. Requires "full" view.
   * The high-level health status of this host.
   */
  @XmlElement
  public ApiHealthSummary getHealthSummary() {
    return healthSummary;
  }

  public void setHealthSummary(ApiHealthSummary healthSummary) {
    this.healthSummary = healthSummary;
  }

  /**
   * Readonly. Requires "full" view.
   * The list of health checks performed on the host, with their results.
   */
  @XmlElementWrapper
  public List<ApiHealthCheck> getHealthChecks() {
    return healthChecks;
  }

  public void setHealthChecks(List<ApiHealthCheck> healthChecks) {
    this.healthChecks = healthChecks;
  }

  /**
   * Readonly.
   * A URL into the Cloudera Manager web UI for this specific host.
   */
  @XmlElement
  public String getHostUrl() {
    return hostUrl;
  }

  public void setHostUrl(String hostUrl) {
    this.hostUrl = hostUrl;
  }

  /**
   * Readonly. Whether the host is in maintenance mode.
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
   * Readonly. The list of objects that trigger this host to be in
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

  @XmlElement
  public ApiConfigList getConfig() {
    return config;
  }

  public void setConfig(ApiConfigList config) {
    this.config = config;
  }

  /**
   * Readonly. The number of logical CPU cores on this host. Only populated
   * after the host has heartbeated to the server.
   * Available since API v4.
   */
  @XmlElement
  public Long getNumCores() {
    return numCores;
  }

  public void setNumCores(Long numCores) {
    this.numCores = numCores;
  }

  /**
   * Readonly. The number of physical CPU cores on this host. Only populated
   * after the host has heartbeated to the server.
   * Available since API v9.
   */
  @XmlElement
  public Long getNumPhysicalCores() {
    return numPhysicalCores;
  }

  public void setNumPhysicalCores(Long numCores) {
    this.numPhysicalCores = numCores;
  }

  /**
   * Readonly. The amount of physical RAM on this host, in bytes. Only
   * populated after the host has heartbeated to the server.
   * Available since API v4.
   */
  @XmlElement
  public Long getTotalPhysMemBytes() {
    return totalPhysMemBytes;
  }

  public void setTotalPhysMemBytes(Long totalPhysMemBytes) {
    this.totalPhysMemBytes = totalPhysMemBytes;
  }
}
