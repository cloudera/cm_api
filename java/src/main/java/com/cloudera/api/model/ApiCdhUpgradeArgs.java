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
  private String cdhPackageVersion;
  private ApiRollingUpgradeClusterArgs rollingRestartArgs;

  /**
   * If using parcels, the full version of an already distributed
   * parcel for the next major CDH version. Default is null, which
   * indicates this is a package upgrade. Example versions are:
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
   * If using packages, the full version of the CDH packages being upgraded to,
   * such as "5.1.2". These packages must already be installed on the cluster
   * before running the upgrade command. For backwards compatibility, if
   * "5.0.0" is specified here, then the upgrade command will relax validation
   * of installed packages to match v6 behavior, only checking major version.
   * <p>
   * Introduced in v9. Has no effect in older API versions, which assume
   * "5.0.0"
   */
  @XmlElement
  public String getCdhPackageVersion() {
    return cdhPackageVersion;
  }

  public void setCdhPackageVersion(String cdhPackageVersion) {
    this.cdhPackageVersion = cdhPackageVersion;
  }

  /**
   * If provided and rolling restart is available, will perform
   * rolling restart with the requested arguments. If provided and
   * rolling restart is not available, errors. If omitted, will do a
   * regular restart.
   * <p>
   * Introduced in v9. Has no effect in older API versions, which must
   * always do a hard restart.
   */
  @XmlElement
  public ApiRollingUpgradeClusterArgs getRollingRestartArgs() {
    return rollingRestartArgs;
  }

  public void setRollingRestartArgs(ApiRollingUpgradeClusterArgs rollingRestartArgs) {
    this.rollingRestartArgs = rollingRestartArgs;
  }

  /**
   * Not used starting in v9 - Client config is always deployed as part of
   * upgrade. For older versions, determines whether client configuration
   * should be deployed as part of upgrade. Default is true.
   */
  @Deprecated
  @XmlElement
  public Boolean getDeployClientConfig() {
    return deployClientConfig;
  }

  public void setDeployClientConfig(Boolean deployClientConfig) {
    this.deployClientConfig = deployClientConfig;
  }

  /**
   * Not used starting in v9 - All servies are always started as part of
   * upgrade. For older versions, determines whether all services should be
   * started should be deployed as part of upgrade. Default is true.
   */
  @Deprecated
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
        .add("cdhParcelVersion", cdhParcelVersion)
        .add("cdhPackageVersion", cdhPackageVersion)
        .add("rollingRestartArgs", rollingRestartArgs)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiCdhUpgradeArgs other = ApiUtils.baseEquals(this, o);
    return this == other
        || (other != null
            && Objects.equal(deployClientConfig, other.deployClientConfig)
            && Objects.equal(startAllServices, other.startAllServices)
            && Objects.equal(cdhParcelVersion, other.cdhParcelVersion)
            && Objects.equal(cdhPackageVersion, other.cdhPackageVersion)
            && Objects.equal(rollingRestartArgs, other.rollingRestartArgs));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        deployClientConfig,
        startAllServices,
        cdhParcelVersion,
        cdhPackageVersion,
        rollingRestartArgs);
  }
}
