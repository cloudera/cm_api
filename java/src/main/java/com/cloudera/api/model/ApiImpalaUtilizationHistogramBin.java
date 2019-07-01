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
 * Histogram bin of Impala utilization.
 */
@XmlRootElement(name = "impalaUtilizationHistogramBin")
public class ApiImpalaUtilizationHistogramBin {
  Double startPointInclusive;
  Double endPointExclusive;
  Long numberOfImpalaDaemons;

  public ApiImpalaUtilizationHistogramBin() {
    // For JAX-B
  }

  /**
   * start point (inclusive) of the histogram bin.
   */
  @XmlElement
  public Double getStartPointInclusive() {
    return startPointInclusive;
  }

  public void setStartPointInclusive(
      Double startPointInclusive) {
    this.startPointInclusive = startPointInclusive;
  }

  /**
   * end point (exclusive) of the histogram bin.
   */
  @XmlElement
  public Double getEndPointExclusive() {
    return endPointExclusive;
  }

  public void setEndPointExclusive(
      Double endPointExclusive) {
    this.endPointExclusive = endPointExclusive;
  }

  /**
   * Number of Impala daemons.
   */
  @XmlElement
  public Long getNumberOfImpalaDaemons() {
    return numberOfImpalaDaemons;
  }

  public void setNumberOfImpalaDaemons(
      Long numberOfImpalaDs) {
    this.numberOfImpalaDaemons = numberOfImpalaDs;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("startPointInclusive", startPointInclusive)
        .add("endPointExclusive", endPointExclusive)
        .add("numberOfImpalaDaemons", numberOfImpalaDaemons)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(startPointInclusive,
        endPointExclusive,
        numberOfImpalaDaemons);
  }

  @Override
  public boolean equals(Object obj) {
    ApiImpalaUtilizationHistogramBin that = ApiUtils.baseEquals(this, obj);
    return this == that || (that != null &&
        Objects.equal(startPointInclusive, that.startPointInclusive) &&
        Objects.equal(endPointExclusive, that.endPointExclusive) &&
        Objects.equal(numberOfImpalaDaemons, that.numberOfImpalaDaemons));
  }
}
