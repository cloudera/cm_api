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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Statistics related to one time series aggregate data point. It is
 * available from v6 for data points containing aggregate data. It includes
 * further statistics about the data point. An aggregate can be across
 * entities (e.g., fd_open_across_datanodes), over time (e.g., a daily point
 * for the fd_open metric for a specific DataNode), or both (e.g., a daily
 * point for the fd_open_across_datanodes metric). If the data point is for
 * non-aggregate date this will return null.
 */
@XmlRootElement(name = "timeSeriesAggregateStatistics")
public class ApiTimeSeriesAggregateStatistics {

  private Date sampleTime;
  private double sampleValue;
  private long count;
  private double min;
  private Date minTime;
  private double max;
  private Date maxTime;
  private double mean;
  private double stdDev;
  private ApiTimeSeriesCrossEntityMetadata crossEntityMetadata;

  /**
   * The timestamp of the sample data point.
   */
  @XmlElement
  public Date getSampleTime() {
    return sampleTime;
  }

  public void setSampleTime(Date sampleTime) {
    this.sampleTime = sampleTime;
  }

  /**
   * The sample data point value representing an actual sample value picked
   * from the underlying data that is being aggregated.
   */
  @XmlElement
  public double getSampleValue() {
    return sampleValue;
  }

  public void setSampleValue(double sampleValue) {
    this.sampleValue = sampleValue;
  }

  /**
   * The number of individual data points aggregated in this data point.
   */
  @XmlElement
  public long getCount() {
    return count;
  }

  public void setCount(long count) {
    this.count = count;
  }

  /**
   * This minimum value encountered while producing this aggregate data point.
   * If this is a cross-time aggregate then this is the minimum value
   * encountered during the aggregation period. If this is a cross-entity
   * aggregate then this is the minimum value encountered across all entities.
   * If this is a cross-time, cross-entity aggregate, then this is the minimum
   * value for any entity across the aggregation period.
   */
  @XmlElement
  public double getMin() {
    return min;
  }

  public void setMin(double min) {
    this.min = min;
  }

  /**
   * The timestamp of the minimum data point.
   */
  @XmlElement
  public Date getMinTime() {
    return minTime;
  }

  public void setMinTime(Date minTime) {
    this.minTime = minTime;
  }

  /**
   * This maximum value encountered while producing this aggregate data point.
   * If this is a cross-time aggregate then this is the maximum value
   * encountered during the aggregation period. If this is a cross-entity
   * aggregate then this is the maximum value encountered across all entities.
   * If this is a cross-time, cross-entity aggregate, then this is the maximum
   * value for any entity across the aggregation period.
   */
  @XmlElement
  public double getMax() {
    return max;
  }

  public void setMax(double max) {
    this.max = max;
  }

  /**
   * The timestamp of the maximum data point.
   */
  @XmlElement
  public Date getMaxTime() {
    return maxTime;
  }

  public void setMaxTime(Date maxTime) {
    this.maxTime = maxTime;
  }

  /**
   * The mean of the values of all data-points for this aggregate data point.
   */
  @XmlElement
  public double getMean() {
    return mean;
  }

  public void setMean(double mean) {
    this.mean = mean;
  }

  /**
   * The standard deviation of the values of all data-points for this aggregate
   * data point.
   */
  @XmlElement
  public double getStdDev() {
    return stdDev;
  }

  public void setStdDev(double stdDev) {
    this.stdDev = stdDev;
  }

  /**
   * If the data-point is for a cross entity aggregate (e.g.,
   * fd_open_across_datanodes) returns the cross entity metadata, null otherwise.
   */
  @XmlElement
  public ApiTimeSeriesCrossEntityMetadata getCrossEntityMetadata() {
    return crossEntityMetadata;
  }

  public void setCrossEntityMetadata(
      ApiTimeSeriesCrossEntityMetadata crossEntityMetadata) {
    this.crossEntityMetadata = crossEntityMetadata;
  }

}
