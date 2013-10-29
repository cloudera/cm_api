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

import com.google.common.base.Preconditions;

/**
 * The response contains a list of applications and warnings.
 */
@XmlRootElement(name = "yarnApplicationResponse")
public class ApiYarnApplicationResponse {

  public static final String APPLICATIONS_ATTR = "applications";
  public static final String WARNINGS_ATTR = "warnings";
  
  private List<ApiYarnApplication> applications;
  private List<String> warnings;
  
  public ApiYarnApplicationResponse() {
    // For JAX-B
  }
  
  public ApiYarnApplicationResponse(
      List<ApiYarnApplication> applications, List<String> warnings) {
    Preconditions.checkArgument(applications != null);
    Preconditions.checkArgument(warnings != null);
    this.applications = applications;
    this.warnings = warnings;
  }
  
  /** The list of applications for this response. */
  @XmlElementWrapper(name = APPLICATIONS_ATTR)
  public List<ApiYarnApplication> getApplications() {
    return applications;
  }
  
  public void setApplications(List<ApiYarnApplication> applications) {
    this.applications = applications;
  }
  
  /**
   * This list of warnings for this response.
   */
  @XmlElementWrapper(name = WARNINGS_ATTR)
  public List<String> getWarnings() {
    return warnings;
  }
  
  public void setWarnings(List<String> warnings) {
    this.warnings = warnings;
  }
}