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
 * Arguments used for the HDFS disable HA command.
 */
@XmlRootElement(name="hdfsDisableHaArgs")
public class ApiHdfsDisableHaArguments {

  private String activeName;
  private String secondaryName;

  private boolean startDependentServices = true;
  private boolean deployClientConfigs = true;

  private boolean disableQuorumStorage = false;

  /** Name of the the NameNode to be kept. */
  @XmlElement
  public String getActiveName() {
    return activeName;
  }

  public void setActiveName(String activeName) {
    this.activeName = activeName;
  }

  /** Name of the SecondaryNamenode to associate with the active NameNode. */
  @XmlElement
  public String getSecondaryName() {
    return secondaryName;
  }

  public void setSecondaryName(String secondaryName) {
    this.secondaryName = secondaryName;
  }

  /** Whether to re-start dependent services. Defaults to true. */
  @XmlElement
  public boolean getStartDependentServices() {
    return startDependentServices;
  }

  public void setStartDependentServices(boolean start) {
    this.startDependentServices = start;
  }

  /** Whether to re-deploy client configurations. Defaults to true. */
  @XmlElement
  public boolean getDeployClientConfigs() {
    return deployClientConfigs;
  }

  public void setDeployClientConfigs(boolean deploy) {
    this.deployClientConfigs = deploy;
  }

  /** Whether to disable Quorum-based Storage. Defaults to false.
   *
   *  Available since API v2. */
  public boolean isDisableQuorumStorage() {
    return disableQuorumStorage;
  }

  public void setDisableQuorumStorage(boolean disableQuorumStorage) {
    this.disableQuorumStorage = disableQuorumStorage;
  }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("activeName", activeName)
                  .add("secondaryName", secondaryName)
                  .add("startDependentServices", startDependentServices)
                  .add("deployClientConfigs", deployClientConfigs)
                  .add("disableQuorumStorage", disableQuorumStorage)
                  .toString();
  }

  public boolean equals(Object o) {
    ApiHdfsDisableHaArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(activeName, other.getActiveName()) &&
        Objects.equal(secondaryName, other.getSecondaryName()) &&
        (startDependentServices == other.getStartDependentServices()) &&
        (deployClientConfigs == other.getDeployClientConfigs()) &&
        (disableQuorumStorage == other.isDisableQuorumStorage()));
  }

  public int hashCode() {
    return Objects.hashCode(activeName, secondaryName, startDependentServices,
        deployClientConfigs, disableQuorumStorage);
  }

}
