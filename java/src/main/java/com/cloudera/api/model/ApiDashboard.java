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

import com.google.common.base.Preconditions;

/**
 * A dashboard definition. Dashboards are composed of tsquery-based charts.
 */
@XmlRootElement(name = "apiDashboard")
public class ApiDashboard {

  private String name;
  private String json;

  public ApiDashboard() {
    // For JAX-B
  }

  public ApiDashboard(String name, String json) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(json);
    this.name = name;
    this.json = json;
  }

  /**
   * Returns the dashboard name.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the json structure for the dashboard. This should be treated as
   * an opaque blob.
   */
  @XmlElement
  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }
}