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
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.List;

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
    ApiDeployment that = (ApiDeployment)o;
    return Objects.equal(timestamp, that.timestamp) &&
        Objects.equal(clusters, that.clusters) &&
        Objects.equal(hosts, that.hosts) &&
        Objects.equal(versionInfo, that.versionInfo) &&
        Objects.equal(managementService, that.managementService);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(timestamp, clusters, hosts,
                            versionInfo, managementService);
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
  @XmlElement(name = "cluster")
  @JsonProperty(value = "clusters")
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
  @XmlElement(name = "host")
  @JsonProperty(value = "hosts")
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
  @XmlElement(name = "user")
  @JsonProperty(value = "users")
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
  public ApiService getManagementService() {
    return managementService;
  }

  public void setManagementService(ApiService managementService) {
    this.managementService = managementService;
  }
}
