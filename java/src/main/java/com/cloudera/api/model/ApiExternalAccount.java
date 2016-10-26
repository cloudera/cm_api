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

import com.google.common.base.Objects;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents an instantiation of an external account type, referencing a supported
 * external account type, via the typeName field, along with suitable configuration to
 * access an external resource of the provided type.
 *
 * The typeName field must match the name of an external account type.
 */
@XmlRootElement(name="externalAccount")
public class ApiExternalAccount {
  private String name;
  private String displayName;
  private Date createdTime;
  private Date lastModifiedTime;
  private String typeName;
  private ApiConfigList accountConfigs;

  public ApiExternalAccount() {
    // For JAX-B
  }

  /**
   * Represents the intial name of the account; used to uniquely identify this account.
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Represents a modifiable label to identify this account for user-visible purposes.
   */
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Represents the time of creation for this account.
   */
  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createTime) {
    this.createdTime = createTime;
  }

  /**
   * Represents the last modification time for this account.
   */
  public Date getLastModifiedTime() {
    return lastModifiedTime;
  }

  public void setLastModifiedTime(Date lastModifiedTime) {
    this.lastModifiedTime = lastModifiedTime;
  }

  /**
   * Represents the Type ID of a supported external account type.
   * The type represented by this field dictates which configuration options must be defined
   * for this account.
   */
  public String getTypeName() {
    return typeName;
  }

  public void setTypeName(String typeName) {
    this.typeName = typeName;
  }

  /**
   * Represents the account configuration for this account.
   *
   * When an account is retrieved from the server,
   * the configs returned must match allowed configuration for the type of this account.
   *
   * When specified for creation of a new account or for the update of an existing account,
   * this field must include every required configuration parameter specified in the type's definition,
   * with the account configuration's value field specified to represent the specific
   * configuration desired for this account.
   */
  public ApiConfigList getAccountConfigs() {
    return accountConfigs;
  }

  public void setAccountConfigs(ApiConfigList accountConfigs) {
    this.accountConfigs = accountConfigs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ApiExternalAccount that = (ApiExternalAccount) o;
    return Objects.equal(name, that.name) &&
        Objects.equal(displayName, that.displayName) &&
        Objects.equal(createdTime, that.createdTime) &&
        Objects.equal(lastModifiedTime, that.lastModifiedTime) &&
        Objects.equal(typeName, that.typeName) &&
        Objects.equal(accountConfigs, that.accountConfigs);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, displayName, createdTime, lastModifiedTime, typeName, accountConfigs);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("name", name)
        .add("typeName", typeName)
        .add("createdTime", createdTime)
        .add("lastModifiedTime", lastModifiedTime)
        .add("displayName", displayName)
        .add("accountConfigs", accountConfigs)
        .toString();
  }
}