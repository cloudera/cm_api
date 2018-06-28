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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.Objects;

import java.util.Arrays;
import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Provides detailed information about a submitted command.
 *
 * <p>There are two types of commands: synchronous and asynchronous.
 * Synchronous commands complete immediately, and their results are passed
 * back in the returned command object after the execution of an API call.
 * Outside of that returned object, there is no way to check the result of a
 * synchronous command.</p>
 *
 * <p>Asynchronous commands have unique non-negative IDs. They may still be
 * running when the API call returns. Clients can check the status of such
 * commands using the API.</p>
 */
@XmlRootElement(name = "command")
@XmlType(propOrder = {
    "id", "name", "startTime", "endTime",
    "active", "success", "resultMessage", "resultDataUrl",
    "clusterRef", "serviceRef", "roleRef", "hostRef", "parent", "children",
    "canRetry"
})
public class ApiCommand {

  private Long id;
  private String name;
  private Date startTime;
  private Date endTime;
  private boolean active;
  private Boolean success;
  private String resultMessage;
  private String resultDataUrl;

  private ApiClusterRef clusterRef;
  private ApiServiceRef serviceRef;
  private ApiRoleRef roleRef;
  private ApiHostRef hostRef;
  private ApiCommandList children;
  private ApiCommand parent;
  private Boolean canRetry;

  public ApiCommand() {
    // For JAX-B
    this(null, null, null, null, false, null, null, null, null, null, null, null,
         null, null, null);
  }

  public ApiCommand(Long id, String name, Date startTime, Date endTime,
                    boolean active, Boolean success, String resultMessage,
                    String resultDataUrl, ApiClusterRef clusterRef,
                    ApiServiceRef serviceRef,
                    ApiRoleRef roleRef, ApiHostRef hostRef,
                    ApiCommandList children, ApiCommand parent,
                    Boolean canRetry) {
    this.id = id;
    this.name = name;
    this.startTime = startTime;
    this.endTime = endTime;
    this.active = active;
    this.success = success;
    this.resultMessage = resultMessage;
    this.resultDataUrl = resultDataUrl;
    this.clusterRef = clusterRef;
    this.serviceRef = serviceRef;
    this.roleRef = roleRef;
    this.hostRef = hostRef;
    this.children = children;
    this.parent = parent;
    this.canRetry = canRetry;
  }

  @Override
  public String toString() {
    return toStringHelper().toString();
  }

  protected Objects.ToStringHelper toStringHelper() {
    return Objects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("startTime", startTime)
        .add("endTime", endTime)
        .add("active", active)
        .add("success", success)
        .add("resultMessage", resultMessage)
        .add("serviceRef", serviceRef)
        .add("roleRef", roleRef)
        .add("hostRef", hostRef)
        .add("parent", parent);
  }

  /**
   * The command ID.
   */
  @XmlElement
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  /** The command name. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** The start time. */
  @XmlElement
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  /** The end time, if the command is finished. */
  @XmlElement
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }

  /** Whether the command is currently active. */
  @XmlElement
  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  /** If the command is finished, whether it was successful. */
  @XmlElement
  public Boolean getSuccess() {
    return success;
  }

  public void setSuccess(Boolean success) {
    this.success = success;
  }

  /** If the command is finished, the result message. */
  @XmlElement
  public String getResultMessage() {
    return resultMessage;
  }

  public void setResultMessage(String resultMessage) {
    this.resultMessage = resultMessage;
  }

  /** URL to the command's downloadable result data, if any exists. */
  @XmlElement
  public String getResultDataUrl() {
    return resultDataUrl;
  }

  public void setResultDataUrl(String resultDataUrl) {
    this.resultDataUrl = resultDataUrl;
  }

  /** Reference to the cluster (for cluster commands only). */
  @XmlElement
  public ApiClusterRef getClusterRef() {
    return clusterRef;
  }

  public void setClusterRef(ApiClusterRef clusterRef) {
    this.clusterRef = clusterRef;
  }

  /** Reference to the service (for service commands only). */
  @XmlElement
  public ApiServiceRef getServiceRef() {
    return serviceRef;
  }

  public void setServiceRef(ApiServiceRef serviceRef) {
    this.serviceRef = serviceRef;
  }

  /** Reference to the role (for role commands only). */
  @XmlElement
  public ApiRoleRef getRoleRef() {
    return roleRef;
  }

  public void setRoleRef(ApiRoleRef roleRef) {
    this.roleRef = roleRef;
  }

  /** Reference to the host (for host commands only). */
  @XmlElement
  public ApiHostRef getHostRef() {
    return hostRef;
  }

  public void setHostRef(ApiHostRef hostRef) {
    this.hostRef = hostRef;
  }

  /** Reference to the parent command, if any. */
  @XmlElement
  public ApiCommand getParent() {
    return parent;
  }

  public void setParent(ApiCommand parent) {
    this.parent = parent;
  }

  /**
   * List of child commands. Only available in the full view.
   * <p>
   * The list contains only the summary view of the children.
   */
  @XmlElement
  public ApiCommandList getChildren() {
    return children;
  }

  public void setChildren(ApiCommandList children) {
    this.children = children;
  }

  /**
   * Available since V11
   *
   * @deprecated Use {@link ApiCommand#getCanRetry()} instead
   */
  @Deprecated
  @JsonIgnore
  public Boolean isCanRetry() {
    return this.canRetry;
  }

  /**
   * If the command can be retried. Available since V11
   */
  @XmlElement
  public Boolean getCanRetry() {
    return this.canRetry;
  }

  public void setCanRetry(Boolean canRetry) {
    this.canRetry = canRetry;
  }

  @Override
  public boolean equals(Object o) {
    ApiCommand that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(id, that.getId()) &&
        Objects.equal(name, that.getName()) &&
        Objects.equal(startTime, that.getStartTime()) &&
        Objects.equal(endTime, that.getEndTime()) &&
        Objects.equal(active, that.isActive()) &&
        Objects.equal(success, that.getSuccess()) &&
        Objects.equal(resultMessage, that.getResultMessage()) &&
        Objects.equal(resultDataUrl, that.getResultDataUrl()) &&
        Objects.equal(serviceRef, that.getServiceRef()) &&
        Objects.equal(roleRef, that.getRoleRef()) &&
        Objects.equal(hostRef, that.getHostRef()) &&
        Objects.equal(clusterRef, that.getClusterRef()) &&
        Objects.equal(parent, that.getParent()) &&
        Objects.equal(canRetry, that.getCanRetry()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id, name, startTime, endTime, active, success,
        resultMessage, resultDataUrl, serviceRef, roleRef, hostRef, clusterRef,
        parent, canRetry);
  }

}
