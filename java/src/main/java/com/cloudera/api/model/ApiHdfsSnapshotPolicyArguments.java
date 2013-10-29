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

import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/** HDFS specific snapshot policy arguments. */
@XmlRootElement(name = "hdfsSnapshotPolicyArguments")
public class ApiHdfsSnapshotPolicyArguments {
  /** Patterns/globs of paths to snapshot. */
  private List<String> pathPatterns;

  public ApiHdfsSnapshotPolicyArguments() {
    // For JAX-B
  }

  /**
   * @param pathPatterns The path patterns specifying the paths. Paths matching
   * any of them will be eligible for snapshot creation.
   */
  public ApiHdfsSnapshotPolicyArguments(List<String> pathPatterns) {
    this.pathPatterns = pathPatterns;
  }

  /**
   * The path patterns specifying the paths. Paths matching any of them will be
   * eligible for snapshot creation.
   * <p/>
   * The pattern matching characters that can be specific are those supported
   * by HDFS. please see the documentation for HDFS globs for more details.
   */
  @XmlElementWrapper
  public List<String> getPathPatterns() {
    return pathPatterns;
  }

  public void setPathPatterns(List<String> pathPatterns) {
    this.pathPatterns = pathPatterns;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("pathPatterns", pathPatterns)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsSnapshotPolicyArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(pathPatterns, other.getPathPatterns()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(pathPatterns);
  }
}
