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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A query details response.
 */
@XmlRootElement(name = "impalaQueryDetailsResponse")
public class ApiImpalaQueryDetailsResponse {

  private String details;

  public ApiImpalaQueryDetailsResponse() {
    // For JAX-B
  }

  public ApiImpalaQueryDetailsResponse(String details) {
    this.details = details;
  }
  
  /** 
   * The details for this query. Two formats are supported:
   * <ul>
   *   <li>
   *   'text': this is a text based, human readable representation of the
   *   Impala runtime profile.
   *   </li>
   *   <li>
   *   'thrift_encoded': this a compact-thrift, base64 encoded representation
   *   of the impala RuntimeProfile.thrift object. See the Impala documentation
   *   for more details.
   *   </li>
   * </ul>
   */
  @XmlElement
  public String getDetails() {
    return details;
  }
  
  public void setDetails(String details) {
    this.details = details;
  }
}
