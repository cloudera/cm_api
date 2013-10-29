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

/**
 * Provides information about an HDFS nameservice.
 * <p>
 * Nameservices can be either a stand-alone NameNode, a NameNode paired with
 * a SecondaryNameNode, or a high-availability pair formed by an active and
 * a stand-by NameNode.
 * <p>
 * The following fields are only available in the object's full view:
 * <ul>
 *   <li>healthSummary</li>
 *   <li>healthChecks</li>
 * </ul>
 */
@XmlRootElement(name = "nameservice")
public class ApiNameservice {

  private String name;
  private ApiRoleRef active;
  private ApiRoleRef activeFailoverController;
  private ApiRoleRef standBy;
  private ApiRoleRef standByFailoverController;
  private ApiRoleRef secondary;
  private List<String> mountPoints;
  private ApiHealthSummary healthSummary;
  private List<ApiHealthCheck> healthChecks;

  public ApiNameservice() {
    // For JAX-B
  }

  public ApiNameservice(String name, ApiRoleRef active, ApiRoleRef activeFC,
      ApiRoleRef standBy, ApiRoleRef standByFC, ApiRoleRef secondary,
      List<String> mountPoints) {
    this.name = name;
    this.active = active;
    this.activeFailoverController = activeFC;
    this.standBy = standBy;
    this.standByFailoverController = standByFC;
    this.secondary = secondary;
    this.mountPoints = mountPoints;
  }

  /** Name of the nameservice. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** Reference to the active NameNode. */
  @XmlElement
  public ApiRoleRef getActive() {
    return active;
  }

  public void setActive(ApiRoleRef active) {
    this.active = active;
  }

  /** Reference to the active NameNode's failover controller, if configured. */
  @XmlElement
  public ApiRoleRef getActiveFailoverController() {
    return activeFailoverController;
  }

  public void setActiveFailoverController(ApiRoleRef activeFailoverController) {
    this.activeFailoverController = activeFailoverController;
  }

  /** Reference to the stand-by NameNode. */
  @XmlElement
  public ApiRoleRef getStandBy() {
    return standBy;
  }

  public void setStandBy(ApiRoleRef standBy) {
    this.standBy = standBy;
  }

  /** Reference to the stand-by NameNode's failover controller, if configured. */
  @XmlElement
  public ApiRoleRef getStandByFailoverController() {
    return standByFailoverController;
  }

  public void setStandByFailoverController(
      ApiRoleRef standByFailoverController) {
    this.standByFailoverController = standByFailoverController;
  }

  /** Reference to the SecondaryNameNode. */
  @XmlElement
  public ApiRoleRef getSecondary() {
    return secondary;
  }

  public void setSecondary(ApiRoleRef secondary) {
    this.secondary = secondary;
  }

  /** Mount points assigned to this nameservice in a federation. */
  @XmlElementWrapper(name = "mountPoints")
  public List<String> getMountPoints() {
    return mountPoints;
  }

  public void setMountPoints(List<String> mountPoints) {
    this.mountPoints = mountPoints;
  }

  /**
   * Requires "full" view. The high-level health status of this nameservice.
   */
  @XmlElement
  public ApiHealthSummary getHealthSummary() {
    return healthSummary;
  }

  public void setHealthSummary(ApiHealthSummary healthSummary) {
    this.healthSummary = healthSummary;
  }

  /**
   * Requires "full" view. List of health checks performed on the nameservice.
   */
  @XmlElementWrapper
  public List<ApiHealthCheck> getHealthChecks() {
    return healthChecks;
  }

  public void setHealthChecks(List<ApiHealthCheck> healthChecks) {
    this.healthChecks = healthChecks;
  }

}
