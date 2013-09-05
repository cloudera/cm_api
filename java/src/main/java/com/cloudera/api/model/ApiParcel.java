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
 * A Parcel encapsulate a specific product and version. For example,
 * (CDH 4.1). A parcel is downloaded, distributed to all the machines of a
 * cluster and then allowed to be activated.
 * <p>>
 * The available parcels are determined by which cluster they will be running on.
 * For example, a SLES parcel won't show up for a RHEL cluster.
 * </p>
 */
@XmlRootElement(name = "parcel")
public class ApiParcel {

  private String product;
  private String version;
  private String stage;
  private ApiParcelState state;
  private ApiClusterRef clusterRef;

  public ApiParcel() {
    // For JAX-B
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("product", product)
                  .add("version", version)
                  .add("stage", stage)
                  .add("clusterRef", clusterRef)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiParcel that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(product, that.product) &&
        Objects.equal(version, that.version) &&
        Objects.equal(clusterRef, that.clusterRef));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(product, version, clusterRef);
  }

  /**
   * The name of the product, e.g. CDH, Impala
   */
  @XmlElement
  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  /**
   * The version of the product, e.g. 1.1.0, 2.3.0.
   */
  @XmlElement
  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  /**
   * Returns the current stage of the parcel.
   * <p>
   * There are a number of stages a parcel can be in. There are two types of
   * stages - stable and transient. A parcel is in a transient stage when it is
   * transitioning between two stable stages. The stages are listed below with
   * some additional information.
   *
   * <ul>
   *   <li><b>AVAILABLE_REMOTELY</b>: Stable stage - the parcel can be
   *   downloaded to the server.</li>
   *   <li><b>DOWNLOADING</b>: Transient stage - the parcel is in the process of being
   *   downloaded to the server.</li>
   *   <li><b>DOWNLOADED</b>: Stable stage - the parcel is downloaded and
   *   ready to be distributed or removed from the server.</li>
   *   <li><b>DISTRIBUTING</b>: Transient stage - the parcel is being sent to all
   *   the hosts in the cluster.</li>
   *   <li><b>DISTRIBUTED</b>: Stable stage - the parcel is on all the hosts in
   *   the cluster. The parcel can now be activated, or removed from all the hosts.</li>
   *   <li><b>UNDISTRIBUTING</b>: Transient stage - the parcel is being removed
   *   from all the hosts in the cluster>/li>
   *   <li><b>ACTIVATED</b>: Steady stage - the parcel is set to active on every host
   *   in the cluster. If desired, a parcel can be deactivated from this stage.</li>
   * </ul>
   */
  @XmlElement
  public String getStage() {
    return stage;
  }

  public void setStage(String stage) {
    this.stage = stage;
  }

  /**
   * The state of the parcel. This shows the progress of state transitions
   * and if there were any errors.
   */
  @XmlElement
  public ApiParcelState getState() {
    return state;
  }

  public void setState(ApiParcelState state) {
    this.state = state;
  }

  /**
   * Readonly. A reference to the enclosing cluster.
   */
  @XmlElement
  public ApiClusterRef getClusterRef() {
    return clusterRef;
  }

  public void setClusterRef(ApiClusterRef clusterRef) {
    this.clusterRef = clusterRef;
  }
}
