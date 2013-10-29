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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A metric represents a specific metric monitored by the Cloudera Management
 * Services, and a list of values matching a user query.
 * <p>
 * These fields are available only in the "full" view:
 * <ul>
 *   <li>displayName</li>
 *   <li>description</li>
 * </ul>
 */
@XmlRootElement(name = "metric")
public class ApiMetric {
  private String name;
  private String context;
  private String unit;
  private String displayName;
  private String description;
  private List<ApiMetricData> data;

  public ApiMetric() {
    // For JAX-B
  }

  public ApiMetric(String name, String context, String unit,
      List<ApiMetricData> data,
      String displayName, String description) {
    this.name = name;
    this.context = context;
    this.unit = unit;
    this.displayName = displayName;
    this.description = description;
    this.data = data;
  }

  /** Name of the metric. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** Context the metric is associated with. */
  @XmlElement
  public String getContext() {
    return context;
  }

  public void setContext(String context) {
    this.context = context;
  }

  /** Unit of the metric values. */
  @XmlElement
  public String getUnit() {
    return unit;
  }

  public void setUnit(String unit) {
    this.unit = unit;
  }

  /** List of readings retrieved from the monitors. */
  @XmlElementWrapper
  public List<ApiMetricData> getData() {
    return data;
  }

  public void setData(List<ApiMetricData> data) {
    this.data = data;
  }

  /** Requires "full" view. User-friendly display name for the metric. */
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /** Requires "full" view. Description of the metric. */
  @XmlElement
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
