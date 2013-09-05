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
 * A parcelRef references a parcel. Each parcel is identified by its "parcelName"
 * and "parcelVersion", and the "clusterName" of the cluster that is using it.
 * To operate on the parcel object, use the API with the those fields as
 * parameters.
 */
@XmlRootElement(name = "parcelRef")
public class ApiParcelRef {

  private String clusterName;
  private String parcelName;
  private String parcelVersion;

  public ApiParcelRef() {
    // For JAX-B
  }

  public ApiParcelRef(String clusterName, String parcelName, String parcelVersion) {
    this.clusterName = clusterName;
    this.parcelName = parcelName;
    this.parcelVersion = parcelVersion;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("clusterName", clusterName)
        .add("serviceName", parcelName)
        .add("roleName", parcelVersion)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcelRef that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(clusterName, that.clusterName) &&
        Objects.equal(parcelName, that.parcelName) &&
        Objects.equal(parcelVersion, that.parcelVersion));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(parcelVersion, parcelName, clusterName);
  }

  /**
   * The name of the cluster that the parcel is used by.
   */
  @XmlElement
  public String getClusterName() {
    return clusterName;
  }

  public void setClusterName(String clusterName) {
    this.clusterName = clusterName;
  }

  /**
   * The name of the parcel.
   */
  @XmlElement
  public String getParcelName() {
    return parcelName;
  }

  public void setParcelName(String parcelName) {
    this.parcelName = parcelName;
  }

  /**
   * The version of the parcel.
   */
  @XmlElement
  public String getParcelVersion() {
    return parcelVersion;
  }

  public void setParcelVersion(String parcelVersion) {
    this.parcelVersion = parcelVersion;
  }

}
