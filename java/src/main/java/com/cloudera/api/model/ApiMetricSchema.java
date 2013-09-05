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
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A metric schema represents the schema for a specific metric monitored by 
 * the Cloudera Management Services.
 */
@XmlRootElement(name = "metricSchema")
public class ApiMetricSchema {
  private String name;
  private boolean isCounter;
  private String unitNumerator;
  private String unitDenominator;
  private List<String> aliases;
  private Map<String, List<String>> sources;

  public ApiMetricSchema() {
    // For JAX-B
  }

  public ApiMetricSchema(String name,
      boolean isCounter,
      String unitNumerator, 
      String unitDenominator,
      List<String> aliases,
      Map<String, List<String>> sources) {
    this.name = name;
    this.isCounter = isCounter;
    this.unitNumerator = unitNumerator;
    this.unitDenominator = unitDenominator;
    this.aliases = aliases;
    this.sources = sources;
  }

  /** 
   * Name of the metric.
   * This name is guaranteed to be unique among the metrics. 
   **/
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  /** 
   * Is the metric a counter.
   * A counter tracks the total count since a process / host started.
   * The rate of change of a counter may often be more interesting than
   * the raw value of a counter.
   **/
  @XmlElement
  public boolean getIsCounter() {
    return isCounter;
  }

  public void setIsCounter(boolean isCounter) {
    this.isCounter = isCounter;
  }

  /** Numerator for the unit of the metric. */
  @XmlElement
  public String getUnitNumerator() {
    return unitNumerator;
  }

  public void setUnitNumerator(String unitNumerator) {
    this.unitNumerator = unitNumerator;
  }
  
  /** Denominator for the unit of the metric. */
  @XmlElement
  public String getUnitDenominator() {
    return unitDenominator;
  }

  public void setUnitDenominator(String unitDenominator) {
    this.unitDenominator = unitDenominator;
  }
  
  /** 
   * Aliases for the metric.
   * An alias is unique per metric (per source and version) but 
   * is not globally unique. Aliases usually refer to previous
   * names for the metric as metrics are renamed or replaced.
   **/
  @XmlElement
  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }
  
  /** 
   * Sources for the metric.
   * Each source entry contains the name of the source
   * and a list of versions for which this source is valid
   **/
  @XmlElement
  public Map<String, List<String>> getSources() {
    return sources;
  }

  public void setSources(Map<String, List<String>> sources) {
    this.sources = sources;
  }
}
