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
 * Version information of Cloudera Manager itself.
 */
@XmlRootElement(name = "cmVersionInfo")
public class ApiVersionInfo {

  private String version;
  private boolean isSnapshot;
  private String buildUser;
  private String buildTimestamp;
  private String gitHash;

  public ApiVersionInfo() {
    /* JAX-B */
  }

  public String toString() {
    return Objects.toStringHelper(this)
        .add("version", version)
        .add("isSnapshot", isSnapshot)
        .add("buildUser", buildUser)
        .add("buildTimestamp", buildTimestamp)
        .add("gitHash", gitHash)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiVersionInfo that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(version, that.version) &&
        Objects.equal(isSnapshot, that.isSnapshot) &&
        Objects.equal(buildUser, that.buildUser) &&
        Objects.equal(buildTimestamp, that.buildTimestamp) &&
        Objects.equal(gitHash, that.gitHash));
  }

  @Override
  public int hashCode() {
    return Objects
        .hashCode(version, isSnapshot, buildUser, buildTimestamp, gitHash);
  }

  /** Version. */
  @XmlElement
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  /** Whether this build is a development snapshot. */
  @XmlElement
  public boolean isSnapshot() {
    return isSnapshot;
  }

  public void setSnapshot(boolean isSnapshot) {
    this.isSnapshot = isSnapshot;
  }

  /** The user performing the build. */
  @XmlElement
  public String getBuildUser() {
    return buildUser;
  }

  public void setBuildUser(String buildUser) {
    this.buildUser = buildUser;
  }

  /** Build timestamp. */
  @XmlElement
  public String getBuildTimestamp() {
    return buildTimestamp;
  }

  public void setBuildTimestamp(String buildTimestamp) {
    this.buildTimestamp = buildTimestamp;
  }

  /** Source control management hash. */
  @XmlElement
  public String getGitHash() {
    return gitHash;
  }

  public void setGitHash(String gitHash) {
    this.gitHash = gitHash;
  }

}
