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
 * The list of all the attributes that are applicable to Impala queries.
 */
@XmlRootElement(name = "impalaQueryAttributeList")
public class ApiImpalaQueryAttributeList
    extends ApiListBase<ApiImpalaQueryAttribute>{

  public ApiImpalaQueryAttributeList() {
    // For JAX-B
  }

  public ApiImpalaQueryAttributeList(List<ApiImpalaQueryAttribute> attributes) {
    super(attributes);
  }

  /**
   * @return The list of attributes.
   */
  @XmlElementWrapper(name = ApiListBase.ITEMS_ATTR)
  public List<ApiImpalaQueryAttribute> getAttributes() {
    return values;
  }


  /**
   * Sets the list of attributes.
   */
  public void setAttributes(List<ApiImpalaQueryAttribute> values) {
    this.values = values;
  }
}