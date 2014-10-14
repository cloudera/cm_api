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

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Metadata for a time series.
 */
@XmlRootElement(name = "timeSeriesMetaData")
public class ApiTimeSeriesMetadata {
  private String metricName;
  private String entityName;
  private Date startTime;
  private Date endTime;
  private Map<String, String> attributes;
  private List<String> unitNumerators;
  private List<String> unitDenominators;
  private String expression;
  private String alias;
  private Long metricCollectionFrequencyMs;
  private String rollupUsed;

  public ApiTimeSeriesMetadata() {
    // For JAX-B
  }

  /** The metric name for the time series. */
  @XmlElement
  public String getMetricName() {
    return metricName;
  }

  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  /**
   * The display name for the entity associated with this time series.
   * For example, if this was a time series for an HDFS service the entity name
   * might be something like "My HDFS Service". If it was for a host it might
   * be something like "myhost.mysite.com".
   */
  @XmlElement
  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  /** The start time for the time series. */
  @XmlElement
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /** The end time for the time series. */
  @XmlElement
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /**
   * The attributes for the time series. Note that the entityName entry in
   * this map is not the same as the entityName field in this
   * ApiTimeSeriesMetadata. The entityName entry in this map is a unique
   * identifier for the entity and not the name displayed in the UI.
   *
   * For example, if this was a time series for the YARN Job History Server
   * the entityName entry in this map might be something like
   * "yarn-JOBHISTORY-6bd17ceb1489aae93fef4c867350d0dd"
   **/
  @XmlElement
  public Map<String, String> getAttributes() {
    return attributes;
  }

  public void setAttributes(Map<String, String> attributes) {
    this.attributes = attributes;
  }

  /** The numerators of the units for the time series. */
  @XmlElement
  public List<String> getUnitNumerators() {
    return unitNumerators;
  }

  public void setUnitNumerators(List<String> unitNumerators) {
    this.unitNumerators = unitNumerators;
  }

  /** The denominators of the units for the time series. */
  @XmlElement
  public List<String> getUnitDenominators() {
    return unitDenominators;
  }

  public void setUnitDenominators(List<String> unitDenominators) {
    this.unitDenominators = unitDenominators;
  }

  /** The tsquery expression that could be used to extract just this stream. */
  @XmlElement
  public String getExpression() {
    return expression;
  }

  public void setExpression(String expression) {
    this.expression = expression;
  }

  /**
   * The alias for this stream's metric. Aliases correspond to use of the 'as'
   * keyword in the tsquery.
   */
  @XmlElement
  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  /**
   * The minimum frequency at which the underlying metric for this stream is
   * collected. Note that this can be null if the stream returns irregularly
   * sampled data.
   */
  @XmlElement
  public Long getMetricCollectionFrequencyMs() {
    return metricCollectionFrequencyMs;
  }

  public void setMetricCollectionFrequencyMs(Long metricCollectionFrequencyMs) {
    this.metricCollectionFrequencyMs = metricCollectionFrequencyMs;
  }

  /**
   * The aggregate rollup for the returned data. This can be TEN_MINUTELY,
   * HOURLY, SIX_HOURLY, DAILY, or WEEKLY.
   */
  @XmlElement
  public String getRollupUsed() {
    return rollupUsed;
  }

  public void setRollupUsed(String rollupUsed) {
    this.rollupUsed = rollupUsed;
  }
}