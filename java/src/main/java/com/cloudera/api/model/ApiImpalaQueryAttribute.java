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

import com.google.common.base.Preconditions;

/**
 * Metadata about an Impala query attribute.
 */
@XmlRootElement(name = "impalaQueryAttribute")
public class ApiImpalaQueryAttribute {

  private String name;
  private String type;
  private String displayName;
  private boolean supportsHistograms;
  private String description;

  public ApiImpalaQueryAttribute() {
    // For JAX-B
  }

  public ApiImpalaQueryAttribute(
      final String name,
      final String type,
      final String displayName,
      final boolean supportsHistograms,
      final String description) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(type);
    Preconditions.checkNotNull(displayName);
    Preconditions.checkNotNull(description);
    this.name = name;
    this.type = type;
    this.displayName = displayName;
    this.supportsHistograms = supportsHistograms;
    this.description = description;
  }

  /**
   * The name of the attribute. This name can be used in filters, for example
   * 'user' could be used in the filter 'user = root'.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The type of the attribute. Valid types are STRING, NUMBER, BOOLEAN, BYTES,
   * MILLISECONDS, BYTES_PER_SECOND, BYTE_SECONDS.
   */
  @XmlElement
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * The display name for the attribute.
   */
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Whether the Service Monitor can generate a histogram of the distribution
   * of the attribute across queries.
   */
  @XmlElement
  public boolean getSupportsHistograms() {
    return supportsHistograms;
  }

  public void setSupportsHistograms(boolean supportsHistograms) {
    this.supportsHistograms = supportsHistograms;
  }

  /**
   * The description of the attribute.
   */
  @XmlElement
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
