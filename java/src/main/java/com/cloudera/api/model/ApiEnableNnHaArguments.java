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
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.ImmutableSet;

/**
 * Arguments used for Enable NameNode High Availability command.
 */
@XmlRootElement(name="enableNnHaArgs")
public class ApiEnableNnHaArguments {

  private String activeNnName;
  private String standbyNnName;
  private String standbyNnHostId;
  private List<String> standbyNameDirList;
  private String nameservice;
  private String qjName;

  private String activeFcName;
  private String standbyFcName;
  private String zkServiceName;
  private Set<ApiJournalNodeArguments> jns = ImmutableSet.<ApiJournalNodeArguments>of();

  private boolean forceInitZNode = true;
  private boolean clearExistingStandbyNameDirs = true;
  private boolean clearExistingJnEditsDir = true;

  /** Name of the NameNode role that is going to be made Highly Available. */
  @XmlElement
  public String getActiveNnName() {
    return activeNnName;
  }

  public void setActiveNnName(String activeNnName) {
    this.activeNnName = activeNnName;
  }

  /**
   * Name of the new Standby NameNode role that will be created during the
   * command (Optional).
   */
  @XmlElement
  public String getStandbyNnName() {
    return standbyNnName;
  }

  public void setStandbyNnName(String standbyNnName) {
    this.standbyNnName = standbyNnName;
  }

  /** Id of the host on which new Standby NameNode will be created. */
  @XmlElement
  public String getStandbyNnHostId() {
    return standbyNnHostId;
  }

  public void setStandbyNnHostId(String standbyNnHostId) {
    this.standbyNnHostId = standbyNnHostId;
  }

  /**
   * List of directories for the new Standby NameNode.
   * If not provided then it will use same dirs as Active NameNode.
   */
  @XmlElement
  public List<String> getStandbyNameDirList() {
    return standbyNameDirList;
  }

  public void setStandbyNameDirList(List<String> standbyNameDirList) {
    this.standbyNameDirList = standbyNameDirList;
  }

  /**
   * Nameservice to be used while enabling Highly Available. 
   * It must be specified if Active NameNode isn't configured with it. 
   * If Active NameNode is already configured, then this need not
   * be specified. However, if it is still specified, it must match
   * the existing config for the Active NameNode.
   */
  @XmlElement
  public String getNameservice() {
    return nameservice;
  }

  public void setNameservice(String nameservice) {
    this.nameservice = nameservice;
  }

  /**
   * Name of the journal located on each JournalNodes' filesystem.
   * This can be optionally provided if the config hasn't already been set for the 
   * Active NameNode.
   * If this isn't provided and Active NameNode doesn't also have the config,
   * then nameservice is used by default. If Active NameNode already has this configured,
   * then it much match the existing config.
   */
  @XmlElement
  public String getQjName() {
    return qjName;
  }

  public void setQjName(String qjName) {
    this.qjName = qjName;
  }

  /**
   * Name of the FailoverController role to be created on
   * Active NameNode's host (Optional).
   */
  @XmlElement
  public String getActiveFcName() {
    return activeFcName;
  }

  public void setActiveFcName(String activeFcName) {
    this.activeFcName = activeFcName;
  }

  /**
   * Name of the FailoverController role to be created on 
   * Standby NameNode's host (Optional).
   */
  @XmlElement
  public String getStandbyFcName() {
    return standbyFcName;
  }

  public void setStandbyFcName(String standbyFcName) {
    this.standbyFcName = standbyFcName;
  }

  /**
   * Name of the ZooKeeper service to be used for Auto-Failover.
   * This MUST be provided if HDFS doesn't have a ZooKeeper dependency.
   * If the dependency is already set, then this should be the name of the same
   * ZooKeeper service, but can also be omitted in that case.
   */
  @XmlElement
  public String getZkServiceName() {
    return zkServiceName;
  }

  public void setZkServiceName(String zkServiceName) {
    this.zkServiceName = zkServiceName;
  }

  /**
   * Arguments for the JournalNodes to be created during the command.
   * Must be provided only if JournalNodes don't exist already in HDFS.
   */
  @XmlElement
  public Set<ApiJournalNodeArguments> getJns() {
    return jns;
  }

  public void setJns(Set<ApiJournalNodeArguments> jns) {
    this.jns = jns;
  }

  /**
   * Boolean indicating if the ZNode should be force initialized if it is
   * already present. Useful while re-enabling High Availability. (Default: TRUE)
   */
  @XmlElement
  public boolean isForceInitZNode() {
    return forceInitZNode;
  }

  public void setForceInitZNode(boolean forceInitZNode) {
    this.forceInitZNode = forceInitZNode;
  }

  /**
   * Boolean indicating if the existing name directories for Standby NameNode
   * should be cleared during the workflow.
   * Useful while re-enabling High Availability. (Default: TRUE)
   */
  @XmlElement
  public boolean isClearExistingStandbyNameDirs() {
    return clearExistingStandbyNameDirs;
  }

  public void setClearExistingStandbyNameDirs(boolean clearExistingStandbyNameDirs) {
    this.clearExistingStandbyNameDirs = clearExistingStandbyNameDirs;
  }

  /**
   * Boolean indicating if the existing edits directories for the JournalNodes
   * for the specified nameservice should be cleared during the workflow.
   * Useful while re-enabling High Availability. (Default: TRUE)
   */
  @XmlElement
  public boolean isClearExistingJnEditsDir() {
    return clearExistingJnEditsDir;
  }

  public void setClearExistingJnEditsDir(boolean clearExistingJnEditsDir) {
    this.clearExistingJnEditsDir = clearExistingJnEditsDir;
  }
}
