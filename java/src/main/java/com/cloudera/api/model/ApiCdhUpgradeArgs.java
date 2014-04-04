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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Arguments used for the CDH Upgrade command.
 */
@XmlRootElement(name = "cdhUpgradeArgs")
public class ApiCdhUpgradeArgs {

  private Boolean deployClientConfig = true;
  private Boolean startAllServices = true;
  private String cdhParcelVersion;

  /**
   * If using parcels, the full version of an already distributed parcel for the
   * next major CDH version. Default is None. Example versions are:
   * '5.0.0-1.cdh5.0.0.p0.11' or '5.0.2-1.cdh5.0.2.p0.32'
   */
  @XmlElement
  public String getCdhParcelVersion() {
    return cdhParcelVersion;
  }

  public void setCdhParcelVersion(String cdhParcelVersion) {
    this.cdhParcelVersion = cdhParcelVersion;
  }

  /**
   * Whether to deploy client configurations after the upgrade. Default is True.
   */
  @XmlElement
  public Boolean getDeployClientConfig() {
    return deployClientConfig;
  }

  public void setDeployClientConfig(Boolean deployClientConfig) {
    this.deployClientConfig = deployClientConfig;
  }

  /**
   * Whether to start all services after the upgrade. Default is True.
   */
  @XmlElement
  public Boolean getStartAllServices() {
    return startAllServices;
  }

  public void setStartAllServices(Boolean startAllServices) {
    this.startAllServices = startAllServices;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("deployClientConfig", deployClientConfig)
        .add("startAllServices", startAllServices)
        .add("cdhParcelVersion", cdhParcelVersion).toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiCdhUpgradeArgs other = ApiUtils.baseEquals(this, o);
    return this == other
        || (other != null
            && Objects.equal(deployClientConfig, other.deployClientConfig)
            && Objects.equal(startAllServices, other.startAllServices) && Objects
              .equal(cdhParcelVersion, other.cdhParcelVersion));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        deployClientConfig,
        startAllServices,
        cdhParcelVersion);
  }
}
