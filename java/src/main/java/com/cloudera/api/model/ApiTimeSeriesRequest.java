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

import static com.cloudera.api.Parameters.ROLLUP_DEFAULT;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

import javax.ws.rs.core.MediaType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Request object containing information needed for querying timeseries data.
 *
 * Available since API v11.
 */
@XmlRootElement(name = "timeSeriesQuery")
public class ApiTimeSeriesRequest {

  String query;
  String from;
  String to;
  String contentType;
  String desiredRollup;
  Boolean mustUseDesiredRollup;

  public ApiTimeSeriesRequest(String query, String from, String to) {
    this(query,
         from,
         to,
         MediaType.APPLICATION_JSON,
         ROLLUP_DEFAULT,
         false);
  }

  public ApiTimeSeriesRequest(
      String query,
      String from,
      String to,
      String contentType,
      String desiredRollup,
      Boolean mustUseDesiredRollup) {
    this.query = query;
    this.from = from;
    this.to = to;
    this.contentType = contentType;
    this.desiredRollup = desiredRollup;
    this.mustUseDesiredRollup = mustUseDesiredRollup;
  }

  public ApiTimeSeriesRequest() {
    this.contentType = MediaType.APPLICATION_JSON;
    this.desiredRollup = ROLLUP_DEFAULT;
    this.mustUseDesiredRollup = false;
  }

  /**
   * tsquery to run against the CM time-series data store.
   * Please see the <a href="http://tiny.cloudera.com/cm_tsquery">
   * tsquery language documentation</a>.<p/>
   */
  @XmlElement
  public String getQuery() {
    return query;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  /**
   * Start of the period to query in ISO 8601 format (defaults to 5 minutes
   * before the end of the period).
   */
  @XmlElement
  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  /**
   * End of the period to query in ISO 8601 format (defaults to current time).
   */
  @XmlElement
  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  /**
   * contentType to return the response in. The content types "application/json"
   * and "text/csv" are supported. This defaults to "application/json". If
   * "text/csv" is specified then we return one row per time series data point,
   * and we don't return any of the metadata.
   */
  @XmlElement
  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Aggregate rollup level desired for the response data.
   * Valid values are RAW, TEN_MINUTELY, HOURLY, SIX_HOURLY, DAILY, and WEEKLY.
   * Note that if the mustUseDesiredRollup parameter is not set, then the
   * monitoring server can decide to return a different rollup level.
   */
  @XmlElement
  public String getDesiredRollup() {
    return desiredRollup;
  }

  public void setDesiredRollup(String desiredRollup) {
    this.desiredRollup = desiredRollup;
  }

  /**
   * If set to true, then the tsquery will return data with the desired
   * aggregate rollup level.
   */
  @XmlElement
  public Boolean getMustUseDesiredRollup() {
    return mustUseDesiredRollup;
  }

  public void setMustUseDesiredRollup(Boolean mustUseDesiredRollup) {
    this.mustUseDesiredRollup = mustUseDesiredRollup;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("query", query)
                  .add("from", from)
                  .add("to", to)
                  .add("contentType", contentType)
                  .add("desiredRollup", desiredRollup)
                  .add("mustUseDesiredRollup", mustUseDesiredRollup)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiTimeSeriesRequest other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(query, other.getQuery()) &&
        Objects.equal(from, other.getFrom()) &&
        Objects.equal(to, other.getTo()) &&
        Objects.equal(contentType, other.getContentType()) &&
        Objects.equal(desiredRollup, other.getDesiredRollup()) &&
        Objects.equal(mustUseDesiredRollup, other.getMustUseDesiredRollup()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        query,
        from,
        to,
        contentType,
        desiredRollup,
        mustUseDesiredRollup);
  }
}
