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

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A time series represents a stream of data points. 
 * Each data point contains a time and a value.
 * Time series are returned by executing a tsquery.
 */
@XmlRootElement(name = "timeSeries")
public class ApiTimeSeries {
  private ApiTimeSeriesMetadata metadata;
  private List<ApiTimeSeriesData> data;

  public ApiTimeSeries() {
    // For JAX-B
  }

  public ApiTimeSeries(ApiTimeSeriesMetadata metadata, 
                       List<ApiTimeSeriesData> data) {
    this.metadata = metadata;
    this.data = data;
  }
  
  @Override
  public boolean equals(Object o) {
    ApiTimeSeries that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(metadata, that.metadata) &&
        Objects.equal(data, that.data));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(metadata, data);
  }

  /** Metadata for the metric. */
  @XmlElement
  public ApiTimeSeriesMetadata getMetadata() {
    return metadata;
  }

  public void setMetadata(ApiTimeSeriesMetadata metadata) {
    this.metadata = metadata;
  }

  /** List of metric data points. */
  @XmlElement
  public List<ApiTimeSeriesData> getData() {
    return data;
  }

  public void setData(List<ApiTimeSeriesData> data) {
    this.data = data;
  }
}
