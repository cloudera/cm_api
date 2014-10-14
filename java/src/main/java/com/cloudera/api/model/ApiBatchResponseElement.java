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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A single element of a batch response, often part of a list with other
 * elements.
 */
@XmlRootElement(name = "batchResponseElement")
public class ApiBatchResponseElement {

  private int statusCode;
  private Object response;

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("statusCode", statusCode)
        .add("response", response)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(statusCode, response);
  }

  @Override
  public boolean equals(Object obj) {
    ApiBatchResponseElement other = ApiUtils.baseEquals(this, obj);
    return this == other || (other != null &&
        Objects.equal(statusCode, other.statusCode) &&
        Objects.equal(response, other.response));
  }

  /**
   * Read-only. The HTTP status code of the response.
   */
  @XmlElement
  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * Read-only. The (optional) serialized body of the response, in the
   * representation produced by the corresponding API endpoint, such as
   * application/json.
   */
  @XmlElement
  public Object getResponse() {
    return response;
  }

  public void setResponse(Object response) {
    this.response = response;
  }
}
