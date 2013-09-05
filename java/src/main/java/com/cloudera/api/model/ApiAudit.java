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
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

/**
 * Models audit events from both CM and CM managed services like HDFS, HBase
 * and Hive. Audits for CM managed services are retrieved from Cloudera Navigator
 * server.
 */
@XmlRootElement(name = "audit")
@XmlType(
    propOrder = {"timestamp", "service", "username", "impersonator",
        "ipAddress", "command", "resource", "operationText", "allowed"})
public class ApiAudit {
  private String service;
  private String username;
  private String impersonator;
  private String command;
  private String ipAddress;
  private String resource;
  private boolean allowed;
  private long timestamp;
  private String operationText;

  public ApiAudit() {
    // For JAX-B
  }

  public ApiAudit(String service, String username,
                  String impersonator, String command,
                  String ipAddress, String resource, boolean allowed,
                  Long timestamp, String operationText) {
    this.service = service;
    this.username = username;
    this.impersonator = impersonator;
    this.command = command;
    this.ipAddress = ipAddress;
    this.resource = resource;
    this.allowed = allowed;
    this.timestamp = timestamp;
    this.operationText = operationText;
  }

  @Override
  public boolean equals(Object o) {
    ApiAudit that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        allowed == that.allowed &&
        timestamp == that.timestamp &&
        Objects.equal(username, that.username) &&
        Objects.equal(impersonator, that.impersonator) &&
        Objects.equal(service, that.service) &&
        Objects.equal(command, that.command) &&
        Objects.equal(ipAddress, that.ipAddress) &&
        Objects.equal(resource, that.resource) &&
        Objects.equal(operationText, that.operationText));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(timestamp, username, impersonator, service,
        command, ipAddress, resource, allowed, operationText);
  }

  /**
   * When the audit event was captured.
   */
  @XmlElement
  public Date getTimestamp() {
    return ApiUtils.newDateFromMillis(timestamp);
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  /**
   * Service name associated with this audit.
   */
  @XmlElement
  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  /**
   * The user who performed this operation.
   */
  @XmlElement
  public String getUsername() {
    return username;
  }

  public void setUseruame(String userName) {
    this.username = userName;
  }

  /**
   * The impersonating user (or the proxy user) who submitted this operation.
   * This is usually applicable when using services like Oozie or Hue, who
   * can be configured to impersonate other users and submit jobs.
   */
  @XmlElement
  public String getImpersonator() {
    return impersonator;
  }

  public void setImpersonator(String impersonator) {
    this.impersonator = impersonator;
  }

  /**
   * The IP address that the client connected from.
   */
  @XmlElement
  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  /**
   * The command/operation that was requested.
   */
  @XmlElement
  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  /**
   * The resource that the operation was performed on.
   */
  @XmlElement
  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  /**
   * Whether the operation was allowed or denied by the authorization system.
   */
  @XmlElement
  public boolean getAllowed() {
    return allowed;
  }

  public void setAllowed(boolean allowed) {
    this.allowed = allowed;
  }

  /**
   * The full text of the requested operation. E.g. the full Hive query.
   * <p>
   * Available since API v5.
   */
  @XmlElement
  public String getOperationText() {
    return operationText;
  }

  public void setOperationText(String operationText) {
    this.operationText = operationText;
  }
}
