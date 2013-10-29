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

import javax.xml.bind.annotation.*;

import com.google.common.base.Objects;

/**
 * Arguments used as part of ApiEnableNnHaArguments to
 * specify JournalNodes.
 */
@XmlRootElement(name="jnArgs")
public class ApiJournalNodeArguments {

  private String jnName;
  private String jnHostId;
  private String jnEditsDir;

  /** Name of new JournalNode to be created. (Optional) */
  @XmlElement
  public String getJnName() {
    return jnName;
  }

  public void setJnName(String jnName) {
    this.jnName = jnName;
  }

  /** ID of the host where the new JournalNode will be created. */
  @XmlElement
  public String getJnHostId() {
    return jnHostId;
  }

  public void setJnHostId(String jnHostId) {
    this.jnHostId = jnHostId;
  }

  /** 
   * Path to the JournalNode edits directory. Need not be specified
   * if it is already set at RoleConfigGroup level.
   */
  @XmlElement
  public String getJnEditsDir() {
    return jnEditsDir;
  }

  public void setJnEditsDir(String jnEditsDir) {
    this.jnEditsDir = jnEditsDir;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(jnName, jnHostId, jnEditsDir);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }

    if (o instanceof ApiJournalNodeArguments) {
      ApiJournalNodeArguments other = (ApiJournalNodeArguments) o;
      return Objects.equal(jnName, other.getJnName()) &&
             Objects.equal(jnHostId, other.getJnHostId()) &&
             Objects.equal(jnEditsDir, other.getJnEditsDir());
    }

    return false;
  }
  
  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("jnName", jnName)
        .add("jnHostId", jnHostId)
        .add("jnEditsDir", jnEditsDir)
        .toString();
  }
}
