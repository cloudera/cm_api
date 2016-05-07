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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class holding additional metadata to the ApiTimeSeriesAggregateStatistics
 * class that applies specifically to cross-entity aggregate metrics.
 */
@XmlRootElement(name = "timeSeriesCrossEntityMetadata")
public class ApiTimeSeriesCrossEntityMetadata {

  private String maxEntityDisplayName;
  private String maxEntityName;
  private String minEntityDisplayName;
  private String minEntityName;
  private Double numEntities;

  /**
   * The display name of the entity that had the maximum value for the
   * cross-entity aggregate metric.
   */
  @XmlElement
  public String getMaxEntityDisplayName() {
    return maxEntityDisplayName;
  }

  public void setMaxEntityDisplayName(String maxEntityDisplayName) {
    this.maxEntityDisplayName = maxEntityDisplayName;
  }

  /**
   * The name of the entity that had the maximum value for the
   * cross-entity aggregate metric.
   * <p>
   * Available since API v11.
   */
  @XmlElement
  public String getMaxEntityName() {
    return maxEntityName;
  }

  public void setMaxEntityName(String maxEntityName) {
    this.maxEntityName = maxEntityName;
  }

  /**
   * The display name of the entity that had the minimum value for the
   * cross-entity aggregate metric.
   */
  @XmlElement
  public String getMinEntityDisplayName() {
    return minEntityDisplayName;
  }

  public void setMinEntityDisplayName(String minEntityDisplayName) {
    this.minEntityDisplayName = minEntityDisplayName;
  }

  /**
   * The name of the entity that had the minimum value for the
   * cross-entity aggregate metric.
   * <p>
   * Available since API v11.
   */
  @XmlElement
  public String getMinEntityName() {
    return minEntityName;
  }

  public void setMinEntityName(String minEntityName) {
    this.minEntityName = minEntityName;
  }

  /**
   * The number of entities covered by this point. For a raw cross-entity point
   * this number is exact. For a rollup point this number is an average, since
   * the number of entities being aggregated can change over the aggregation
   * period.
   */
  @XmlElement
  public double getNumEntities() {
    return numEntities;
  }

  public void setNumEntities(double numEntities) {
    this.numEntities = numEntities;
  }

}
