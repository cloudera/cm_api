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

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The response contains a list of queries and warnings.
 */
@XmlRootElement(name = "impalaQueryResponse")
public class ApiImpalaQueryResponse {

  public static final String WARNINGS_ATTR = "warnings";
  
  private List<ApiImpalaQuery> queries;
  private List<String> warnings;
  
  public ApiImpalaQueryResponse() {
    // For JAX-B
  }
  
  public ApiImpalaQueryResponse(List<ApiImpalaQuery> queries, List<String> warnings) {
    this.queries = queries;
    this.warnings = warnings;
  }
  
  /** The list of queries for this response. */
  @XmlElementWrapper()
  public List<ApiImpalaQuery> getQueries() {
    return queries;
  }
  
  public void setQueries(List<ApiImpalaQuery> queries) {
    this.queries = queries;
  }
  
  /** This list of warnings for this response. */
  @XmlElementWrapper(name = WARNINGS_ATTR)
  public List<String> getWarnings() {
    return warnings;
  }
  
  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }
}