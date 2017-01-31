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
import javax.xml.bind.annotation.XmlType;

import java.util.List;

/**
 * A cluster represents a set of interdependent services running on a set
 * of hosts. All services on a given cluster are of the same software version
 * (e.g. CDH4 or CDH5).
 */
@XmlRootElement(name = "cluster")
@XmlType(propOrder = {"name", "displayName", "version", "fullVersion",
    "maintenanceMode", "maintenanceOwners", "services", "parcels", "clusterUrl",
    "hostsUrl", "entityStatus", "uuid"})
public class ApiCluster {

  private String name;
  private String displayName;
  private String uuid;
  private String clusterUrl;
  private String hostsUrl;
  private ApiClusterVersion version;
  private String fullVersion;
  private Boolean maintenanceMode;
  private List<ApiEntityType> maintenanceOwners;
  private List<ApiService> services;
  private List<ApiParcel> parcels;
  private ApiEntityStatus entityStatus;

  public ApiCluster() {
    // For JAX-B
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("version", version)
                  .add("fullVersion", fullVersion)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiCluster other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(name, other.name));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }

  /**
   * The name of the cluster.
   * <p>
   * Immutable since API v6.
   * <p>
   * Prior to API v6, will contain the display name of the cluster.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The display name of the cluster that is shown in the UI.
   * <p>
   * Available since API v6.
   */
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Readonly. The UUID of the cluster.
   * <p>
   * Available since API v15.
   */
  @XmlElement
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /**
   * Readonly. Link into the Cloudera Manager web UI for this specific cluster.
   * <p>
   * Available since API v10.
   */
  @XmlElement
  public String getClusterUrl() {
    return clusterUrl;
  }

  public void setClusterUrl(String clusterUrl) {
    this.clusterUrl = clusterUrl;
  }

  /**
   * Readonly. Link into the Cloudera Manager web UI for host table for this cluster.
   * <p>
   * Available since API v11.
   */
  @XmlElement
  public String getHostsUrl() {
    return hostsUrl;
  }

  public void setHostsUrl(String hostsUrl) {
    this.hostsUrl = hostsUrl;
  }

  /** The CDH version of the cluster. */
  @XmlElement
  public ApiClusterVersion getVersion() {
    return version;
  }

  public void setVersion(ApiClusterVersion version) {
    this.version = version;
  }

  /**
   * The full CDH version of the cluster. The expected format is three dot
   * separated version numbers, e.g. "4.2.1" or "5.0.0". The full version takes
   * precedence over the version field during cluster creation.
   * <p>
   * Available since API v6.
   */
  @XmlElement
  public String getFullVersion() {
    return fullVersion;
  }

  public void setFullVersion(String fullVersion) {
    this.fullVersion = fullVersion;
  }

  /**
   * Readonly. Whether the cluster is in maintenance mode.
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
   * Readonly. The list of objects that trigger this cluster to be in
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
   * Optional. Used during import/export of settings.
   */
  @XmlElementWrapper(name = "services")
  public List<ApiService> getServices() {
    return services;
  }

  public void setServices(List<ApiService> services) {
    this.services = services;
  }

  /**
   * Optional. Used during import/export of settings.
   * Available since API v4.
   */
  @XmlElementWrapper(name = "parcels")
  public List<ApiParcel> getParcels() {
    return parcels;
  }

  public void setParcels(List<ApiParcel> parcels) {
    this.parcels = parcels;
  }

  /**
   * Readonly. The entity status for this cluster.
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
