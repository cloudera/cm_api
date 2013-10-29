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
 * A batch response, comprised of one or more response elements.
 */
@XmlRootElement(name = "batchResponse")
public class ApiBatchResponse extends ApiListBase<ApiBatchResponseElement> {
  private Boolean success;

  public ApiBatchResponse() {

  }

  public ApiBatchResponse(List<ApiBatchResponseElement> elements) {
    super(elements);
  }

  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  public List<ApiBatchResponseElement> getElements() {
    return values;
  }

  public void setElements(List<ApiBatchResponseElement> elements) {
    this.values = elements;
  }

  /**
   * Read-only. True if every response element's
   * {@link ApiBatchResponseElement#getStatusCode()} is in the range [200, 300),
   * false otherwise.
   */
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }
}
