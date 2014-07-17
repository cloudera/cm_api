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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This objects represents a deployment including all clusters, hosts,
 * services, roles, etc in the system.  It can be used to save and restore
 * all settings.
 */
@XmlRootElement(name = "deployment")
public class ApiDeployment {
  private Date timestamp;
  private List<ApiCluster> clusters;
  private List<ApiHost> hosts;
  private List<ApiUser> users;
  private ApiVersionInfo versionInfo;
  private ApiService managementService;
  private ApiConfigList managerSettings;
  private ApiConfigList allHostsConfig;
  private List<ApiCmPeer> peers;
  private ApiHostTemplateList hostTemplates;

  public ApiDeployment() {
    // For JAX-B
  }

  public String toString() {
    return Objects.toStringHelper(this)
        .add("timestamp", timestamp)
        .add("clusters", clusters)
        .add("hosts", hosts)
        .add("versionInfo", versionInfo)
        .add("managementService", managementService)
        .add("managerSettings", managerSettings)
        .add("allHostsConfig", allHostsConfig)
        .add("peers", peers)
        .add("hostTemplates", hostTemplates)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiDeployment that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(timestamp, that.getTimestamp()) &&
        Objects.equal(clusters, that.getClusters()) &&
        Objects.equal(hosts, that.getHosts()) &&
        Objects.equal(versionInfo, that.getVersionInfo()) &&
        Objects.equal(managementService, that.getManagementService()) &&
        Objects.equal(managerSettings, that.getManagerSettings()) &&
        Objects.equal(allHostsConfig, that.getAllHostsConfig()) &&
        Objects.equal(peers, that.getPeers()) &&
        Objects.equal(hostTemplates, that.getHostTemplates()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(timestamp, clusters, hosts,
                            versionInfo, managementService,
                            allHostsConfig, peers, hostTemplates);
  }

  /**
   * Readonly. This timestamp is provided when you request a deployment and
   * is not required (or even read) when creating a deployment. This
   * timestamp is useful if you have multiple deployments saved and
   * want to determine which one to use as a restore point.
   */
  @XmlElement
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date date) {
    this.timestamp = date;
  }

  /**
   * List of clusters in the system including their services, roles and
   * complete config values.
   */
  @XmlElementWrapper(name = "clusters")
  public List<ApiCluster> getClusters() {
    return clusters;
  }

  public void setClusters(List<ApiCluster> clusters) {
    this.clusters = clusters;
  }

  /**
   * List of hosts in the system
   */
  @XmlElementWrapper(name = "hosts")
  public List<ApiHost> getHosts() {
    return hosts;
  }

  public void setHosts(List<ApiHost> hosts) {
    this.hosts = hosts;
  }

  /**
   * List of all users in the system
   */
  @XmlElementWrapper(name = "users")
  public List<ApiUser> getUsers() {
    return users;
  }

  public void setUsers(List<ApiUser> users) {
    this.users = users;
  }

  /**
   * Full version information about the running Cloudera Manager instance
   */
  @XmlElement
  public ApiVersionInfo getVersionInfo() {
    return versionInfo;
  }

  public void setVersionInfo(ApiVersionInfo versionInfo) {
    this.versionInfo = versionInfo;
  }

  /**
   * The full configuration of the Cloudera Manager management service
   * including all the management roles and their config values
   */
  @XmlElement
  public ApiService getManagementService() {
    return managementService;
  }

  public void setManagementService(ApiService managementService) {
    this.managementService = managementService;
  }

  /**
   * The full configuration of Cloudera Manager itself including licensing info
   */
  @XmlElement
  public ApiConfigList getManagerSettings() {
    return managerSettings;
  }

  public void setManagerSettings(ApiConfigList managerSettings) {
    this.managerSettings = managerSettings;
  }

  /**
   * Configuration parameters that apply to all hosts, unless overridden at
   * the host level. Available since API v3.
   */
  @XmlElement
  public ApiConfigList getAllHostsConfig() {
    return allHostsConfig;
  }

  public void setAllHostsConfig(ApiConfigList allHostsConfig) {
    this.allHostsConfig = allHostsConfig;
  }

  /**
   * The list of peers configured in Cloudera Manager.
   * Available since API v3.
   */
  @XmlElementWrapper
  public List<ApiCmPeer> getPeers() {
    return peers;
  }

  public void setPeers(List<ApiCmPeer> peers) {
    this.peers = peers;
  }

  /**
   * The list of all host templates in Cloudera Manager.
   */
  @XmlElement
  public ApiHostTemplateList getHostTemplates() {
    return hostTemplates;
  }

  public void setHostTemplates(ApiHostTemplateList hostTemplates) {
    this.hostTemplates = hostTemplates;
  }
}
