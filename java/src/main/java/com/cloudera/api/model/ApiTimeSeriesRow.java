//Licensed to Cloudera, Inc. under one
//or more contributor license agreements.  See the NOTICE file
//distributed with this work for additional information
//regarding copyright ownership.  Cloudera, Inc. licenses this file
//to you under the Apache License, Version 2.0 (the
//"License"); you may not use this file except in compliance
//with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
package com.cloudera.api.model;

import java.util.Date;

import com.google.common.base.Preconditions;

/**
 * A time series data point.
 */
public class ApiTimeSeriesRow {

  private String entityName;
  private String metricName;
  private Date timestamp;
  private double value;

  public ApiTimeSeriesRow() {
  }

  public ApiTimeSeriesRow(
      final String entityName,
      final String metricName,
      final Date timestamp,
      final double value) {
    Preconditions.checkNotNull(entityName);
    Preconditions.checkNotNull(metricName);
    Preconditions.checkNotNull(timestamp);
    this.entityName = entityName;
    this.metricName = metricName;
    this.timestamp = timestamp;
    this.value = value;
  }

  /** The name of the entity for this time series data point. */
  public String getEntityName() {
    return entityName;
  }

  public void setEntityName(String entityName) {
    this.entityName = entityName;
  }

  /** The name of the metric for this time series data point. */
  public String getMetricName() {
    return metricName;
  }

  public void setMetricName(String metricName) {
    this.metricName = metricName;
  }

  /** The timestamp for this time series data point. */
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  /** The value for this time series data point. */
  public double getValue() {
    return value;
  }

  public void setValue(double value) {
    this.value = value;
  }
}