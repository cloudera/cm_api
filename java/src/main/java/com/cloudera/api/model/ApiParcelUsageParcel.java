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
 * This object is used to represent a parcel within an ApiParcelUsage.
 */
@XmlRootElement(name = "parcelUsageParcel")
public class ApiParcelUsageParcel {
  private ApiParcelRef parcelRef;
  private int processCount;
  private boolean activated;

  public ApiParcelUsageParcel () {
    // For JAX-B
  }

  /**
   * Reference to the corresponding Parcel object.
   */
  @XmlElement
  public ApiParcelRef getParcelRef() {
    return parcelRef;
  }

  public void setParcelRef(ApiParcelRef parcelRef) {
    this.parcelRef = parcelRef;
  }

  /**
   * How many running processes on the cluster are using the parcel.
   */
  @XmlElement
  public int getProcessCount() {
    return processCount;
  }

  public void setProcessCount(int processCount) {
    this.processCount = processCount;
  }

  /**
   * Is this parcel currently activated on the cluster.
   */
  @XmlElement
  public boolean isActivated() {
    return activated;
  }

  public void setActivated(boolean activated) {
    this.activated = activated;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("parcelRef", parcelRef)
        .add("processCount", processCount)
        .add("activated", activated)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcelUsageParcel that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(parcelRef, that.parcelRef) &&
        Objects.equal(processCount, that.processCount) &&
        Objects.equal(activated, that.activated));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(parcelRef, processCount, activated);
  }
}