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

import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This object provides a complete view of the usage of parcels in a given
 * cluster - particularly which parcels are in use for which roles.
 */
@XmlRootElement(name = "parcelUsage")
public class ApiParcelUsage {

  private Set<ApiParcelUsageRack> racks;
  private Set<ApiParcelUsageParcel> parcels;

  public ApiParcelUsage() {
    // For JAX-B
  }

  /**
   * The racks that contain hosts that are part of this cluster.
   */
  @XmlElement
  public Set<ApiParcelUsageRack> getRacks() {
    return racks;
  }

  public void setRacks(Set<ApiParcelUsageRack> racks) {
    this.racks = racks;
  }

  /**
   * The parcel's that are activated and/or in-use on this cluster.
   */
  @XmlElement
  public Set<ApiParcelUsageParcel> getParcels() {
    return parcels;
  }

  public void setParcels(Set<ApiParcelUsageParcel> parcels) {
    this.parcels = parcels;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("racks", racks)
        .add("parcels", parcels)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcelUsage that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(racks, that.racks) &&
        Objects.equal(parcels, that.parcels));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(racks, parcels);
  }
}
