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
 * Arguments used for disable Sentry HA API call.
 */
@XmlRootElement(name = "disableSentryHaArgs")
public class ApiDisableSentryHaArgs {
  private String activeName;

  /** Name of the single role that will remain active after HA is disabled. */
  @XmlElement
  public String getActiveName() {
    return activeName;
  }

  public void setActiveName(String activeName) {
    this.activeName = activeName;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("activeName", activeName)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiDisableSentryHaArgs that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(activeName, that.getActiveName()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(activeName);
  }

}
