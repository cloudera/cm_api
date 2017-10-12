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

import javax.xml.bind.annotation.XmlRootElement;

/**
 * A supported external account type.
 * An external account type represents an external authentication source that is used by
 * Cloudera Manager in its APIs to take suitable actions that require authentication to an
 * external service.
 *
 * An external account type is uniquely identified by a server-generated ID and identifies with
 * a category identifier: e.g. The "AWS" category has an account type "AWS_Access_Key_Authorization"
 *
 */
@XmlRootElement(name = "externalAccountType")
public class ApiExternalAccountType {

  private String name;
  private String categoryName;
  private String type;
  private String displayName;
  private String description;
  private ApiConfigList allowedAccountConfigs;

  public ApiExternalAccountType() {
    // For JAX-B
  }

  /**
   * Represents the immutable name for this account.
   */
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Represents the category of this account.
   */
  public String getCategoryName() {
    return categoryName;
  }

  public void setCategoryName(String categoryName) {
    this.categoryName = categoryName;
  }

  /**
   * Represents the type for this account.
   */
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  /**
   * Represents the localized display name for this account.
   */
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Represents the localized description for this account type.
   */
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Represents the list of allowed account configs.
   */
  public ApiConfigList getAllowedAccountConfigs() {
    return allowedAccountConfigs;
  }

  public void setAllowedAccountConfigs(ApiConfigList allowedAccountConfigs) {
    this.allowedAccountConfigs = allowedAccountConfigs;
  }
}