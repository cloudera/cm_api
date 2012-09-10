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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Arguments used when enabling HDFS automatic failover.
 */
@XmlRootElement(name = "hdfsFailoverArgs")
public class ApiHdfsFailoverArguments {

  private String nameservice;
  private ApiServiceRef zooKeeperService;
  private String activeControllerName;
  private String standByControllerName;

  /** Nameservice for which to enable automatic failover. */
  @XmlElement
  public String getNameservice() {
    return nameservice;
  }

  public void setNameservice(String nameservice) {
    this.nameservice = nameservice;
  }

  /** The ZooKeeper service to use. */
  @XmlElement
  public ApiServiceRef getZooKeeperService() {
    return zooKeeperService;
  }

  public void setZooKeeperService(ApiServiceRef zooKeeperService) {
    this.zooKeeperService = zooKeeperService;
  }

  /** Name of the failover controller to create for the active NameNode. */
  @XmlElement
  public String getActiveFCName() {
    return activeControllerName;
  }

  public void setActiveFCName(String activeControllerName) {
    this.activeControllerName = activeControllerName;
  }

  /** Name of the failover controller to create for the stand-by NameNode. */
  @XmlElement
  public String getStandByFCName() {
    return standByControllerName;
  }

  public void setStandByFCName(String standByControllerName) {
    this.standByControllerName = standByControllerName;
  }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("nameservice", nameservice)
                  .add("zooKeeperService", zooKeeperService)
                  .add("activeControllerName", activeControllerName)
                  .add("standByControllerName", standByControllerName)
                  .toString();
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ApiHdfsFailoverArguments other = (ApiHdfsFailoverArguments) o;
    return Objects.equal(nameservice, other.getNameservice()) &&
        Objects.equal(zooKeeperService, other.getZooKeeperService()) &&
        Objects.equal(activeControllerName, other.getActiveFCName()) &&
        Objects.equal(standByControllerName, other.getStandByFCName());
  }

  public int hashCode() {
    return Objects.hashCode(nameservice, zooKeeperService, activeControllerName,
        standByControllerName);
  }

}
