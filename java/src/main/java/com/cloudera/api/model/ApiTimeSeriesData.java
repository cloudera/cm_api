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
 * A single data point of time series data.
 */
@XmlRootElement(name = "timeSeriesData")
public class ApiTimeSeriesData {
  private Date timestamp;
  private double value;
  private String type;
  private ApiTimeSeriesAggregateStatistics aggregateStatistics;

  /** The timestamp for the time series data. */
  @XmlElement
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /** The value of the time series data. */
  @XmlElement
  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }

  /** The type of the time series data. */
  @XmlElement
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Available from v6 for data points containing aggregate data. It includes
   * further statistics about the data point. An aggregate can be across
   * entities (e.g., fd_open_across_datanodes), over time (e.g., a daily point
   * for the fd_open metric for a specific DataNode), or both (e.g., a daily
   * point for the fd_open_across_datanodes metric). If the data point is for
   * non-aggregate date this will return null.
   */
  @XmlElement
  public ApiTimeSeriesAggregateStatistics getAggregateStatistics() {
    return aggregateStatistics;
  }

  public void setAggregateStatistics(
      ApiTimeSeriesAggregateStatistics aggregateStatistics) {
    this.aggregateStatistics = aggregateStatistics;
  }
}