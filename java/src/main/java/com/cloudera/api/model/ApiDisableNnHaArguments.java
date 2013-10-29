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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * Arguments used for Disable NameNode High Availability command.
 */
@XmlRootElement(name="hdfsDisableNnHaArgs")
public class ApiDisableNnHaArguments {
  
  private String activeNnName;
  private String snnHostId;
  private String snnName;
  private List<String> snnCheckpointDirList;

  /**
   * Name of the NamdeNode role that is going to be active after
   * High Availability is disabled.
   */
  @XmlElement
  public String getActiveNnName() {
    return activeNnName;
  }

  public void setActiveNnName(String activeNnName) {
    this.activeNnName = activeNnName;
  }

  /** Id of the host where the new SecondaryNameNode will be created. */
  @XmlElement
  public String getSnnHostId() {
    return snnHostId;
  }

  public void setSnnHostId(String snnHostId) {
    this.snnHostId = snnHostId;
  }

  /** List of directories used for checkpointing by the new SecondaryNameNode. */
  @XmlElement
  public List<String> getSnnCheckpointDirList() {
    return snnCheckpointDirList;
  }

  public void setSnnCheckpointDirList(List<String> snnCheckpointDirList) {
    this.snnCheckpointDirList = snnCheckpointDirList;
  }

  /** Name of the new SecondaryNameNode role (Optional). */
  @XmlElement
  public String getSnnName() {
    return snnName;
  }

  public void setSnnName(String snnName) {
    this.snnName = snnName;
  }
  
  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("activeNnName", activeNnName)
        .add("snnHostId", snnHostId)
        .add("snnName", snnName)
        .add("snnCheckpointDirList", snnCheckpointDirList)
        .toString();
  }
  
  @Override
  public boolean equals(Object o) {
    ApiDisableNnHaArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(activeNnName, other.activeNnName) &&
        Objects.equal(snnHostId, other.snnHostId) &&
        Objects.equal(snnName, other.snnName) &&
        Objects.equal(snnCheckpointDirList, other.snnCheckpointDirList));
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(activeNnName, snnHostId, snnName, snnCheckpointDirList);
  }
}
