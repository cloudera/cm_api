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
        "ipAddress", "command", "resource", "allowed"})
public class ApiAudit {
  private String service;
  private String userName;
  private String impersonator;
  private String command;
  private String ipAddress;
  private String resource;
  private boolean allowed;
  private long timestamp;

  public ApiAudit() {
    // For JAX-B
  }

  public ApiAudit(String service, String userName, String impersonator,
      String command, String ipAddress, String resource, boolean allowed,
      Long timestamp) {
    this.service = service;
    this.userName = userName;
    this.impersonator = impersonator;
    this.command = command;
    this.ipAddress = ipAddress;
    this.resource = resource;
    this.allowed = allowed;
    this.timestamp = timestamp;
  }

  @Override
  public boolean equals(Object o) {
    ApiAudit that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        allowed == that.allowed &&
        timestamp == that.timestamp &&
        Objects.equal(userName, that.userName) &&
        Objects.equal(impersonator, that.impersonator) &&
        Objects.equal(service, that.service) &&
        Objects.equal(command, that.command) &&
        Objects.equal(ipAddress, that.ipAddress) &&
        Objects.equal(resource, that.resource));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(timestamp, userName, impersonator, service,
        command, ipAddress, resource, allowed);
  }

  @XmlElement
  public Date getTimestamp() {
    return ApiUtils.newDateFromMillis(timestamp);
  }

  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }

  @XmlElement
  public String getService() {
    return service;
  }

  public void setService(String service) {
    this.service = service;
  }

  @XmlElement
  public String getUsername() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @XmlElement
  public String getImpersonator() {
    return impersonator;
  }

  public void setImpersonator(String impersonator) {
    this.impersonator = impersonator;
  }

  @XmlElement
  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  @XmlElement
  public String getCommand() {
    return command;
  }

  public void setCommand(String command) {
    this.command = command;
  }

  @XmlElement
  public String getResource() {
    return resource;
  }

  public void setResource(String resource) {
    this.resource = resource;
  }

  @XmlElement
  public boolean getAllowed() {
    return allowed;
  }

  public void setAllowed(boolean allowed) {
    this.allowed = allowed;
  }

}