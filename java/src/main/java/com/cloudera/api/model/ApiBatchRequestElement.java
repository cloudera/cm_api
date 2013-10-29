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
 * A single element of a batch request, often part of a list with other
 * elements.
 */
@XmlRootElement(name = "batchRequestElement")
public class ApiBatchRequestElement {

  public static enum HTTPMethod {
    GET,
    POST,
    PUT,
    DELETE,
  }

  private HTTPMethod method;
  private String url;
  private Object body;
  private String contentType;
  private String acceptType;

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("method", method)
        .add("url", url)
        .add("body", body)
        .add("contentType", contentType)
        .add("acceptType", acceptType)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(method, url, body, contentType, acceptType);
  }

  @Override
  public boolean equals(Object obj) {
    ApiBatchRequestElement other = ApiUtils.baseEquals(this, obj);
    return this == other || (other != null &&
        Objects.equal(method, other.method) &&
        Objects.equal(url, other.url) &&
        Objects.equal(body, other.body) &&
        Objects.equal(contentType, other.contentType) &&
        Objects.equal(acceptType, other.acceptType));
  }

  /**
   * The type of request (e.g. POST, GET, etc.).
   */
  @XmlElement
  public HTTPMethod getMethod() {
    return method;
  }

  public void setMethod(HTTPMethod method) {
    this.method = method;
  }

  /**
   * The URL of the request. Must not have a scheme, host, or port. The path
   * should be prefixed with "/api/", and should include path and query
   * parameters.
   */
  @XmlElement
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Optional body of the request. Must be serialized in accordance with
   * {@link #getContentType()}. For application/json, use
   * {@link com.cloudera.api.ApiObjectMapper}.
   */
  @XmlElement
  public Object getBody() {
    return body;
  }

  public void setBody(Object body) {
    this.body = body;
  }


  /**
   * Content-Type header of the request element. If unset, the element will be
   * treated as if the wildcard type had been specified unless it has a body,
   * in which case it will fall back to application/json.
   */
  @XmlElement
  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Accept header of the request element. The response body (if it exists)
   * will be in this representation. If unset, the element will be treated as
   * if the wildcard type had been requested.
   */
  @XmlElement
  public String getAcceptType() {
    return acceptType;
  }

  public void setAcceptType(String acceptType) {
    this.acceptType = acceptType;
  }
}
