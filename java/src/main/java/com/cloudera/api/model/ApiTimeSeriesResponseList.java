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

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "timeSeriesResponseList")
public class ApiTimeSeriesResponseList extends ApiListBase<ApiTimeSeriesResponse> {

  public ApiTimeSeriesResponseList() {
  }

  public ApiTimeSeriesResponseList(List<ApiTimeSeriesResponse> responses) {
    super(responses);
  }

  /** The list of responses for this query response list. */
  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  public List<ApiTimeSeriesResponse> getResponses() {
    return values;
  }
}