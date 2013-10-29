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
 * Represents MapReduce2 information for a YARN application.
 */
@XmlRootElement(name = "mr2AppInformation")
public class ApiMr2AppInformation {

  private String jobState;
  
  public ApiMr2AppInformation() {
    // For JAX-B
  }
  
  public ApiMr2AppInformation(String jobState) {
    this.jobState = jobState;
  }

  /** 
   * The state of the job. This is only set on completed jobs. This can
   * take on the following values: "NEW", "INITED", "RUNNING", "SUCCEEDED",
   * "FAILED", "KILLED", "ERROR".
   */
  @XmlElement
  public String getJobState() {
    return jobState;
  }

  public void setJobState(String jobState) {
    this.jobState = jobState;
  }
}
