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
 * A clusterRef references a cluster. To operate on the cluster object,
 * use the cluster API with the clusterName as the parameter.
 */
@XmlRootElement(name = "clusterRef")
public class ApiClusterRef {

  private String clusterName;

  public ApiClusterRef() {
    // For JAX-B
  }

  public ApiClusterRef(String clusterName) {
    this.clusterName = clusterName;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("clusterName", clusterName)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ApiClusterRef that = (ApiClusterRef) o;
    return Objects.equal(clusterName, that.clusterName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(clusterName);
  }

  /**
   * The name of the cluster, which uniquely identifies it in a CM installation.
   */
  @XmlElement
  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }
}
