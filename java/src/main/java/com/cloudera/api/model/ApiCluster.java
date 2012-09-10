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
 * (e.g. CDH3 or CDH4).
 */
@XmlRootElement(name = "cluster")
@XmlType(propOrder = {"name", "version",
    "maintenanceMode", "maintenanceOwners", "services"})
public class ApiCluster {

  private String name;
  private ApiClusterVersion version;
  private Boolean maintenanceMode;
  private List<ApiEntityType> maintenanceOwners;
  private List<ApiService> services;

  public ApiCluster() {
    // For JAX-B
  }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("version", version)
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

  /** The name of the cluster */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

}
