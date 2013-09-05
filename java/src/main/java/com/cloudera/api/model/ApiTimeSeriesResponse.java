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

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

/**
 * The time series response for a time series query.
 */
@XmlRootElement(name = "timeSeriesResponse")
public class ApiTimeSeriesResponse {
  private List<ApiTimeSeries> timeSeries;
  private List<String> warnings;
  private String timeSeriesQuery;

  public ApiTimeSeriesResponse() {
  }

  public ApiTimeSeriesResponse(List<ApiTimeSeries> timeSeries,
                               List<String> warnings,
                               String timeSeriesQuery) {
    this.timeSeries = timeSeries;
    this.warnings = warnings;
    this.timeSeriesQuery = timeSeriesQuery;
  }

  /** The time series data for this single query response. */
  @XmlElementWrapper
  public List<ApiTimeSeries> getTimeSeries() {
    return timeSeries;
  }
  
  public void setTimeSeries(List<ApiTimeSeries> timeSeries) {
    this.timeSeries = timeSeries;
  }
  
  /** The warnings for this single query response. */
  @XmlElementWrapper
  public List<String> getWarnings() {
    return warnings;
  }
  
  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }
  
  /** The query for this single query response. */
  @XmlElementWrapper
  public String getTimeSeriesQuery() {
    return timeSeriesQuery;
  }
  
  public void setTimeSeriesQuery(String timeSeriesQuery) {
    this.timeSeriesQuery = timeSeriesQuery;
  }
}