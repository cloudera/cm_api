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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Arguments used for collecting diagnostics data for Yarn applications
 */
@XmlRootElement(name = "yarnApplicationDiagnosticsCollectionArgs")
public class ApiYarnApplicationDiagnosticsCollectionArgs {
  private List<String> applicationIds;
  private String ticketNumber;
  private String comments;

  /**
   * Id's of the applications whose diagnostics data has to be collected
   */
  @XmlElement
  public List<String> getApplicationIds() {
    return applicationIds;
  }

  public void setApplicationIds(List<String> applicationIds) {
    this.applicationIds = applicationIds;
  }

  /**
   * Ticket Number of the Cloudera Support Ticket
   */
  @XmlElement
  public String getTicketNumber() {
    return ticketNumber;
  }

  public void setTicketNumber(String ticketNumber) {
    this.ticketNumber = ticketNumber;
  }

  /**
   * Comments to add to the support bundle
   */
  @XmlElement
  public String getComments() {
    return comments;
  }

  public void setComments(String comments) {
    this.comments = comments;
  }
}
