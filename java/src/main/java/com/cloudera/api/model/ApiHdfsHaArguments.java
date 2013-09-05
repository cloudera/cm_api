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
 * Arguments used for HDFS HA commands.
 */
@XmlRootElement(name="hdfsHaArgs")
public class ApiHdfsHaArguments {

  private String activeName;
  private String annSharedEditsPath;

  private String standByName;
  private String sbnSharedEditsPath;

  private String nameservice;

  private boolean startDependentServices = true;
  private boolean deployClientConfigs = true;

  private boolean enableQuorumStorage = false;

  /** Name of the active NameNode. */
  @XmlElement
  public String getActiveName() {
    return activeName;
  }

  public void setActiveName(String activeName) {
    this.activeName = activeName;
  }

  /** Path to the shared edits directory on the active NameNode's host.
   *  Ignored if Quorum-based Storage is being enabled. */
  @XmlElement
  public String getActiveSharedEditsPath() {
    return annSharedEditsPath;
  }

  public void setActiveSharedEditsPath(String annSharedEditsPath) {
    this.annSharedEditsPath = annSharedEditsPath;
  }

  /** Name of the stand-by Namenode. */
  @XmlElement
  public String getStandByName() {
    return standByName;
  }

  public void setStandByName(String standByName) {
    this.standByName = standByName;
  }

  /** Path to the shared edits directory on the stand-by NameNode's host.
   *  Ignored if Quorum-based Storage is being enabled. */
  @XmlElement
  public String getStandBySharedEditsPath() {
    return sbnSharedEditsPath;
  }

  public void setStandBySharedEditsPath(String sbnSharedEditsPath) {
    this.sbnSharedEditsPath = sbnSharedEditsPath;
  }

  /** Nameservice that identifies the HA pair. */
  @XmlElement
  public String getNameservice() {
    return nameservice;
  }

  public void setNameservice(String nameservice) {
    this.nameservice = nameservice;
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

  /** Whether to enable Quorum-based Storage. Defaults to false.
   *
   *  Enabling Quorum-based Storage requires a minimum of three and
   *  an odd number of JournalNodes to be created and configured
   *  before enabling HDFS HA.
   *
   *  Available since API v2. */
  @XmlElement
  public boolean isEnableQuorumStorage() {
    return enableQuorumStorage;
  }

  public void setEnableQuorumStorage(boolean enableQuorumStorage) {
    this.enableQuorumStorage = enableQuorumStorage;
  }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("nameservice", nameservice)
                  .add("activeName", activeName)
                  .add("annSharedEditsPath", annSharedEditsPath)
                  .add("standByName", standByName)
                  .add("sbnSharedEditsPath", sbnSharedEditsPath)
                  .add("startDependentServices", startDependentServices)
                  .add("deployClientConfigs", deployClientConfigs)
                  .add("enableQuorumStorage", enableQuorumStorage)
                  .toString();
  }

  public boolean equals(Object o) {
    ApiHdfsHaArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(nameservice, other.getNameservice()) &&
        Objects.equal(activeName, other.getActiveName()) &&
        Objects.equal(annSharedEditsPath, other.getActiveSharedEditsPath()) &&
        Objects.equal(standByName, other.getStandByName()) &&
        Objects.equal(sbnSharedEditsPath, other.getStandBySharedEditsPath()) &&
        (startDependentServices == other.getStartDependentServices()) &&
        (deployClientConfigs == other.getDeployClientConfigs()) &&
        (enableQuorumStorage == other.isEnableQuorumStorage()));
  }

  public int hashCode() {
    return Objects.hashCode(nameservice, activeName, annSharedEditsPath,
        standByName, sbnSharedEditsPath, startDependentServices,
        deployClientConfigs, enableQuorumStorage);
  }

}
