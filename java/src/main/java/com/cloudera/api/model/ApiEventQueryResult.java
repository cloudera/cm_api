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

import com.google.common.collect.Lists;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonProperty;

@XmlRootElement(name = "eventQueryResult")
public class ApiEventQueryResult extends ApiListBase<ApiEvent> {
  private long totalResults;

  public ApiEventQueryResult() {
    // For JAX-B
  }

  public ApiEventQueryResult(long totalResults, List<ApiEvent> events) {
    super(events);
    this.totalResults = totalResults;
  }

  public static ApiEventQueryResult newEmptyResult() {
    return new ApiEventQueryResult(0, Lists.<ApiEvent>newArrayList());
  }

  /**
   * The total number of matched results. Some are possibly not shown due
   * to pagination.
   */
  @XmlElement
  public long getTotalResults() {
    return totalResults;
  }

  public void setTotalResults(long totalResults) {
    this.totalResults = totalResults;
  }

  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  @XmlElement(name = "event")
  @JsonProperty(value = ApiListBase.ITEMS_ATTR)
  public List<ApiEvent> getEvents() {
    return values;
  }

  public void setEvents(List<ApiEvent> values) {
    this.values = values;
  }

}
