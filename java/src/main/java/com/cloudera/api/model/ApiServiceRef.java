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
 * A serviceRef references a service. It is identified by the "serviceName"
 * and "clusterName", the name of the cluster which the service belongs to.
 * To operate on the service object, use the API with those fields as
 * parameters.
 */
@XmlRootElement(name = "serviceRef")
public class ApiServiceRef {

  private String clusterName;
  private String serviceName;

  public ApiServiceRef() {
    // For JAX-B
  }

  public ApiServiceRef(String clusterName, String serviceName) {
    this.clusterName = clusterName;
    this.serviceName = serviceName;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("clusterName", clusterName)
                  .add("serviceName", serviceName)
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

    ApiServiceRef that = (ApiServiceRef) o;
    return Objects.equal(clusterName, that.clusterName) &&
        Objects.equal(serviceName, that.serviceName);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(clusterName, serviceName);
  }

  /** The enclosing cluster for this service. */
  @XmlElement
  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  /** The service name. */
  @XmlElement
  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

}
