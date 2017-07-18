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
import com.google.common.collect.Lists;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

/**
 * A service (such as HDFS, MapReduce, HBase) runs in a cluster. It has roles,
 * which are the actual entities (NameNode, DataNodes, etc.) that perform the
 * service's functions.
 *
 * <h3>HDFS services and health checks</h3>
 *
 * In CDH4, HDFS services may not present any health checks. This will happen
 * if the service has more than one nameservice configured. In those cases,
 * the health information will be available by fetching information about the
 * nameservices instead.
 * <p>
 * The health summary is still available, and reflects a service-wide summary.
 */
@XmlRootElement(name = "service")
public class ApiService {

  private String name;
  private String type;
  private ApiClusterRef clusterRef;
  private String serviceUrl;
  private String roleInstancesUrl;
  private ApiServiceState serviceState;
  private ApiHealthSummary healthSummary;
  private List<ApiHealthCheck> healthChecks;
  private Boolean configStale;
  private ApiConfigStalenessStatus configStalenessStatus;
  private ApiConfigStalenessStatus clientConfigStalenessStatus;
  private Boolean maintenanceMode;
  private List<ApiEntityType> maintenanceOwners;
  private ApiServiceConfig config;
  private List<ApiRole> roles;
  private String displayName;
  private List<ApiRoleConfigGroup> roleConfigGroups;
  private List<ApiReplicationSchedule> replicationSchedules;
  private List<ApiSnapshotPolicy> snapshotPolicies;
  private ApiEntityStatus entityStatus;

  public ApiService() {
    // For JAX-B
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("type", type)
                  .add("clusterRef", clusterRef)
                  .add("serviceState", serviceState)
                  .add("healthSummary", healthSummary)
                  .add("config", config)
                  .add("roles", roles)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiService that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(name, that.name) &&
        Objects.equal(type, that.type) &&
        Objects.equal(clusterRef, that.clusterRef));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, type, clusterRef);
  }

  /**
   * The name of the service.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The type of the service, e.g. HDFS, MAPREDUCE, HBASE.
   */
  @XmlElement
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Readonly. A reference to the enclosing cluster.
   */
  @XmlElement
  public ApiClusterRef getClusterRef() {
    return clusterRef;
  }

  public void setClusterRef(ApiClusterRef clusterRef) {
    this.clusterRef = clusterRef;
  }

  /**
   * Readonly. The configured run state of this service.
   * Whether it's running, etc.
   */
  @XmlElement
  public ApiServiceState getServiceState() {
    return serviceState;
  }

  public void setServiceState(ApiServiceState serviceState) {
    this.serviceState = serviceState;
  }

  /**
   * Readonly. The high-level health status of this service.
   */
  @XmlElement
  public ApiHealthSummary getHealthSummary() {
    return healthSummary;
  }

  public void setHealthSummary(ApiHealthSummary healthSummary) {
    this.healthSummary = healthSummary;
  }

  /**
   * Readonly. Expresses whether the service configuration is stale.
   *
   * @deprecated Use configStalenessStatus instead which exposes more staleness
   *             state.
   *             Deprecated since V6.
   */
  @Deprecated
  @XmlElement
  public Boolean getConfigStale() {
    return configStale;
  }

  public void setConfigStale(Boolean configStale) {
    this.configStale = configStale;
  }

  /**
   * Readonly. Expresses the service's configuration staleness status which is
   * based on the staleness status of its roles.
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
   * Readonly. Expresses the service's client configuration staleness status
   * which is marked as stale if any of the service's hosts have missing client
   * configurations or if any of the deployed client configurations are stale.
   * Available since API v6.
   */
  @XmlElement
  public ApiConfigStalenessStatus getClientConfigStalenessStatus() {
    return clientConfigStalenessStatus;
  }

  public void setClientConfigStalenessStatus(ApiConfigStalenessStatus clientConfigStalenessStatus) {
    this.clientConfigStalenessStatus = clientConfigStalenessStatus;
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

  /**
   * Readonly. Link into the Cloudera Manager web UI for this specific service.
   */
  @XmlElement
  public String getServiceUrl() {
    return serviceUrl;
  }

  public void setServiceUrl(String serviceUrl) {
    this.serviceUrl = serviceUrl;
  }

  /**
   * Readonly. Link into the Cloudera Manager web UI for role instances table for
   * this specific service.
   * Available since API v11.
   */
  @XmlElement
  public String getRoleInstancesUrl() {
    return roleInstancesUrl;
  }

  public void setRoleInstancesUrl(String roleInstancesUrl) {
    this.roleInstancesUrl = roleInstancesUrl;
  }

  /**
   * Readonly. Whether the service is in maintenance mode.
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
   * Readonly. The list of objects that trigger this service to be in
   * maintenance mode.
   * Available since API v2.
   */
  @XmlElementWrapper(name = "maintenanceOwners")
  public List<ApiEntityType> getMaintenanceOwners() {
    return maintenanceOwners;
  }

  public void setMaintenanceOwners(List<ApiEntityType> maintenanceOwners) {
    this.maintenanceOwners = (null == maintenanceOwners)
                               ? new ArrayList<ApiEntityType>()
                               : Lists.newArrayList(maintenanceOwners);
  }

  /** Configuration of the service being created. Optional. */
  @XmlElement
  public ApiServiceConfig getConfig() {
    return config;
  }

  public void setConfig(ApiServiceConfig config) {
    this.config = config;
  }

  /** The list of service roles. Optional. */
  @XmlElementWrapper(name = "roles")
  public List<ApiRole> getRoles() {
    return roles;
  }

  public void setRoles(List<ApiRole> roles) {
    this.roles = roles;
  }

  /**
   * The display name for the service that is shown in the UI.
   * Available since API v2.
   */
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * The list of role configuration groups in this service. Optional.
   * Available since API v3.
   */
  @XmlElementWrapper
  public List<ApiRoleConfigGroup> getRoleConfigGroups() {
    return roleConfigGroups;
  }

  public void setRoleConfigGroups(List<ApiRoleConfigGroup> roleConfigGroups) {
    this.roleConfigGroups = roleConfigGroups;
  }

  /**
   * The list of replication schedules for this service. Optional.
   * Available since API v6.
   */
  @XmlElementWrapper
  public List<ApiReplicationSchedule> getReplicationSchedules() {
    return replicationSchedules;
  }

  public void setReplicationSchedules(List<ApiReplicationSchedule> replicationSchedules) {
    this.replicationSchedules = replicationSchedules;
  }

  /**
   * The list of snapshot policies for this service. Optional.
   * Available since API v6.
   */
  @XmlElementWrapper
  public List<ApiSnapshotPolicy> getSnapshotPolicies() {
    return snapshotPolicies;
  }

  public void setSnapshotPolicies(List<ApiSnapshotPolicy> snapshotPolicies) {
    this.snapshotPolicies = snapshotPolicies;
  }

  /**
   * Readonly. The entity status for this service.
   * Available since API v11.
   */
  @XmlElement
  public ApiEntityStatus getEntityStatus() {
    return entityStatus;
  }

  public void setEntityStatus(ApiEntityStatus entityStatus) {
    this.entityStatus = entityStatus;
  }
}
