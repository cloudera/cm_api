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

// TODO: get rid of the following annotation when we upgrade to Jackson 1.9.
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Model for a configuration parameter.
 *
 * When an entry's <i>value</i> property is not available, it means the
 * entry is not configured. This means that the default value for the entry,
 * if any, will be used. Setting a value to <i>null</i> also can be used
 * to unset any previously set value for the parameter, reverting to
 * the default value (if any).
 */
@XmlRootElement(name = "config")
@XmlType(propOrder = { "name", "value", "required", "defaultValue",
                        "displayName", "description", "relatedName", "validationState",
                        "validationMessage" })
public class ApiConfig {

  private String name;
  private String value;
  private String defaultValue;
  private String displayName;
  private String description;
  private String relatedName;
  private Boolean required;
  private ValidationState validationState;
  private String validationMessage;

  public static enum ValidationState {
    OK,
    WARNING,
    ERROR,
  }

  /**
   * @param name          The name of the parameter
   * @param value         The value of the attribute
   * @param required      Whether the configuration value is required.
   * @param defaultValue  The default value, if any.
   * @param displayName   Name of the attribute as displayed in the UI.
   * @param description   Description of the configuration.
   * @param relatedName     Related config Alternative information about the parameter.
   * @param validationState   State of the parameter's validation.
   * @param validationMessage Message describing any validation issues.
   */
  public ApiConfig(String name, String value, Boolean required,
      String defaultValue, String displayName, String description,
      String relatedName, ValidationState validationState, String validationMessage) {
    this.name = name;
    this.value = value;
    this.required = required;
    this.defaultValue = defaultValue;
    this.displayName = displayName;
    this.description = description;
    this.relatedName = relatedName;
    this.validationState = validationState;
    this.validationMessage = validationMessage;
  }

  public ApiConfig(String name, String value) {
    this();
    this.name = name;
    this.value = value;
  }

  public ApiConfig() {
    this.required = null;
    this.defaultValue = null;
    this.displayName = null;
    this.description = null;
    this.relatedName = null;
    this.validationState = null;
    this.validationMessage = null;
  }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("value", value)
                  .add("required", required)
                  .add("default", defaultValue)
                  .add("displayName", displayName)
                  .add("description", description)
                  .add("relatedName", relatedName)
                  .toString();
  }

  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ApiConfig other = (ApiConfig) o;
    return Objects.equal(name, other.name) &&
        Objects.equal(value, other.value) &&
        Objects.equal(required, other.required) &&
        Objects.equal(defaultValue, other.defaultValue) &&
        Objects.equal(displayName, other.displayName) &&
        Objects.equal(description, other.description);
  }

  public int hashCode() {
    return Objects.hashCode(name, value, required, defaultValue,
        displayName, description);
  }

  /**
   * Readonly. Requires "full" view. The default value.
   */
  @XmlElement(name = "default")
  @JsonProperty(value = "default")
  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  /**
   * Readonly. Requires "full" view. A textual description of the parameter.
   */
  @XmlElement
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Readonly. Requires "full" view. A user-friendly name of the parameters,
   * as would have been shown in the web UI.
   */
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Readonly. Requires "full" view. If applicable, contains the related
   * configuration variable used by the source project.
   */
  @XmlElement
  public String getRelatedName() {
    return relatedName;
  }

  public void setRelatedName(String relatedName) {
    this.relatedName = relatedName;
  }

  /**
   * Readonly. The canonical name that identifies this configuration parameter.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The user-defined value. When absent, the default value (if any)
   * will be used.
   */
  @XmlElement
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  /**
   * Readonly. Requires "full" view.
   * Whether this configuration is required for the object. If
   * any required configuration is not set, operations on the object may not
   * work.
   */
  @XmlElement
  @JsonProperty(value = "required")
  public Boolean getRequired() {
    return required;
  }

  public void setRequired(Boolean required) {
    this.required = required;
  }

  /**
   * Readonly. Requires "full" view.
   * State of the configuration parameter after validation.
   */
  @XmlElement
  public ValidationState getValidationState() {
    return validationState;
  }

  public void setValidationState(ValidationState validationState) {
    this.validationState = validationState;
  }

  /**
   * Readonly. Requires "full" view.
   * A message explaining the parameter's validation state.
   */
  @XmlElement
  public String getValidationMessage() {
    return validationMessage;
  }

  public void setValidationMessage(String validationMessage) {
    this.validationMessage = validationMessage;
  }
}
