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

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Arguments used for Cluster Restart command.
 *
 * Since V11:
 * If both restartOnlyStaleServices and restartServiceNames are specified,
 * a service must be specified in restartServiceNames and also be stale,
 * in order to be restarted.
 */
@XmlRootElement(name="restartClusterArgs")
public class ApiRestartClusterArgs {

  private Boolean restartOnlyStaleServices;
  private Boolean redeployClientConfiguration;
  private List<String> restartServiceNames;

  /**
   * Only restart services that have stale configuration and their dependent
   * services.
   */
  @XmlElement
  public Boolean getRestartOnlyStaleServices() {
    return restartOnlyStaleServices;
  }

  public void setRestartOnlyStaleServices(Boolean restartOnlyStaleServices) {
    this.restartOnlyStaleServices = restartOnlyStaleServices;
  }

  /**
   * Re-deploy client configuration for all services in the cluster.
   */
  @XmlElement
  public Boolean getRedeployClientConfiguration() {
    return redeployClientConfiguration;
  }

  public void setRedeployClientConfiguration(Boolean redeployClientConfiguration) {
    this.redeployClientConfiguration = redeployClientConfiguration;
  }

  /**
   * Only restart services that are specified and their dependent services.
   * Available since V11.
   */
  @XmlElement
  public List<String> getRestartServiceNames() {
    return restartServiceNames;
  }

  public void setRestartServiceNames(List<String> restartServiceNames) {
    this.restartServiceNames = restartServiceNames;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("restartOnlyStaleServices", restartOnlyStaleServices)
        .add("redeployClientConfiguration", redeployClientConfiguration)
        .add("restartServiceNames", restartServiceNames)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiRestartClusterArgs other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(restartOnlyStaleServices, other.restartOnlyStaleServices)) &&
        Objects.equal(redeployClientConfiguration, other.redeployClientConfiguration) &&
        Objects.equal(restartServiceNames, other.restartServiceNames);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(restartOnlyStaleServices, redeployClientConfiguration,
        restartServiceNames);
  }
}
