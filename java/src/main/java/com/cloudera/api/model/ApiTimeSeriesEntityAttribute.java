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
 * A time series entity attribute represents a possible attribute of a time
 * series entity type monitored by the Cloudera Management Services.
 * <p>
 * Available since API v11.
 */
@XmlRootElement(name = "timeSeriesEntityAttribute")
public class ApiTimeSeriesEntityAttribute {
  private String name;
  private String displayName;
  private String description;
  private boolean isValueCaseSensitive;

  public ApiTimeSeriesEntityAttribute() {
    // For JAX-B
  }

  public ApiTimeSeriesEntityAttribute(
      String name,
      String displayName,
      String description,
      boolean isValueCaseSensitive) {
    this.name = name;
    this.displayName = displayName;
    this.description = description;
    this.isValueCaseSensitive = isValueCaseSensitive;
  }

  /**
   * Name of the of the attribute.
   * This name uniquely identifies this attribute.
   **/
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Display name of the attribute.
   **/
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Description of the attribute.
   **/
  @XmlElement
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns whether to treat attribute values as case-sensitive.
   **/
  @XmlElement
  public boolean getIsValueCaseSensitive() {
    return isValueCaseSensitive;
  }

  public void setIsValueCaseSensitive(boolean isValueCaseSensitive) {
    this.isValueCaseSensitive = isValueCaseSensitive;
  }
}