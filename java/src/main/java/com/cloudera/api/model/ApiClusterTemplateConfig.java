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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Config Details: The config can either have a value or ref or variable.
 */
public class ApiClusterTemplateConfig {
  /**
   * Config name
   */
  private String name;
  /**
   * Config value
   */
  @JsonInclude(Include.NON_NULL)
  private String value;
  /**
   * Name of the reference. If referring to a service then it will be replaced
   * with actual service name at import time. If referring to a role then it
   * will be replaced with the host name containing that role at import time.
   */
  @JsonInclude(Include.NON_EMPTY)
  private String ref;
  /**
   * Referring a variable. The variable value will be provided by the user at
   * import time. Variable name for this config. At import time the value of
   * this variable will be provided by the
   * {@link #ApiClusterTemplateInstantiator.Variable}
   */
  @JsonInclude(Include.NON_EMPTY)
  private String variable;
  /**
   * This indicates that the value was automatically configured.
   */
  @JsonInclude(Include.NON_DEFAULT)
  private boolean autoConfig;

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return this.value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getRef() {
    return this.ref;
  }

  public void setRef(String ref) {
    this.ref = ref;
  }

  public String getVariable() {
    return this.variable;
  }

  public void setVariable(String variable) {
    this.variable = variable;
  }

  public boolean isAutoConfig() {
    return this.autoConfig;
  }

  public void setAutoConfig(boolean autoConfig) {
    this.autoConfig = autoConfig;
  }

}