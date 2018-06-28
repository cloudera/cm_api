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
 * Arguments used for enable Sentry HA command.
 */
@XmlRootElement(name = "enableSentryHaArgs")
public class ApiEnableSentryHaArgs {
  private String newSentryHostId;
  private String newSentryRoleName;
  private String zkServiceName;
  private ApiSimpleRollingRestartClusterArgs rrcArgs;

  /**
   * Id of host on which new Sentry Server role will be added.
   */
  @XmlElement
  public String getNewSentryHostId() {
    return newSentryHostId;
  }

  public void setNewSentryHostId(String newSentryHostId) {
    this.newSentryHostId = newSentryHostId;
  }

  /**
   * Name of the new Sentry Server role to be created. This is an
   * optional argument.
   */
  @XmlElement
  public String getNewSentryRoleName() {
    return newSentryRoleName;
  }

  public void setNewSentryRoleName(String newSentryRoleName) {
    this.newSentryRoleName = newSentryRoleName;
  }

  /**
   * Name of the ZooKeeper service that will be used for Sentry HA.
   * This is an optional parameter if the Sentry to ZooKeeper dependency is
   * already set in CM.
   */
  @XmlElement
  public String getZkServiceName() {
    return zkServiceName;
  }

  public void setZkServiceName(String zkServiceName) {
    this.zkServiceName = zkServiceName;
  }

  public ApiSimpleRollingRestartClusterArgs getRrcArgs() {
    return rrcArgs;
  }

  public void setRrcArgs(
      ApiSimpleRollingRestartClusterArgs rrcArgs) {
    this.rrcArgs = rrcArgs;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("newSentryHostId", newSentryHostId)
        .add("newSentryRoleName", newSentryRoleName)
        .add("zkServiceName", zkServiceName)
        .add("rrcArgs", rrcArgs)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiEnableSentryHaArgs that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(newSentryHostId, that.getNewSentryHostId()) &&
        Objects.equal(newSentryRoleName, that.getNewSentryRoleName()) &&
        Objects.equal(zkServiceName, that.getZkServiceName()) &&
        Objects.equal(rrcArgs, that.getRrcArgs()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(newSentryHostId, newSentryRoleName,
        zkServiceName, rrcArgs);
  }
}
