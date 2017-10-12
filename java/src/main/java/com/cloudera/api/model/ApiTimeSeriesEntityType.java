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
 * Describe a time series entity type and attributes associated with
 * this entity type.
 * <p>
 * Available since API v11.
 */
@XmlRootElement(name = "timeSeriesEntityType")
public class ApiTimeSeriesEntityType {
  private String name;
  private String category;
  private String nameForCrossEntityAggregateMetrics;
  private String displayName;
  private String description;
  private List<String> immutableAttributeNames;
  private List<String> mutableAttributeNames;
  private List<String> entityNameFormat;
  private String entityDisplayNameFormat;
  private List<String> parentMetricEntityTypeNames;

  public ApiTimeSeriesEntityType() {
    // For JAX-B
  }

  /**
   * Returns the name of the entity type. This name uniquely identifies this
   * entity type.
   */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the category of the entity type.
   */
  @XmlElement
  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  /**
   * Returns the string to use to pluralize the name of the entity for cross
   * entity aggregate metrics.
   */
  @XmlElement
  public String getNameForCrossEntityAggregateMetrics() {
    return nameForCrossEntityAggregateMetrics;
  }

  public void setNameForCrossEntityAggregateMetrics(String value) {
    this.nameForCrossEntityAggregateMetrics = value;
  }

  /**
   * Returns the display name of the entity type.
   */
  @XmlElement
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Returns the description of the entity type.
   */
  @XmlElement
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the list of immutable attributes for this entity type. Immutable
   * attributes values for an entity may not change over its lifetime.
   */
  @XmlElement
  public List<String> getImmutableAttributeNames() {
    return immutableAttributeNames;
  }

  public void setImmutableAttributeNames(List<String> value) {
    this.immutableAttributeNames = value;
  }

  /**
   * Returns the list of mutable attributes for this entity type. Mutable
   * attributes for an entity may change over its lifetime.
   */
  @XmlElement
  public List<String> getMutableAttributeNames() {
    return mutableAttributeNames;
  }

  public void setMutableAttributeNames(List<String> value) {
    this.mutableAttributeNames = value;
  }

  /**
   * Returns a list of attribute names that will be used to construct entity
   * names for entities of this type. The attributes named here must be immutable
   * attributes of this type or a parent type.
   */
  @XmlElement
  public List<String> getEntityNameFormat() {
    return entityNameFormat;
  }

  public void setEntityNameFormat(List<String> value) {
    this.entityNameFormat = value;
  }

  /**
   * Returns a format string that will be used to construct the display name of
   * entities of this type. If this returns null the entity name would be used
   * as the display name.
   *
   * The entity attribute values are used to replace $attribute name portions of
   * this format string. For example, an entity with roleType "DATANODE" and
   * hostname "foo.com" will have a display name "DATANODE (foo.com)" if the
   * format is "$roleType ($hostname)".
   */
  @XmlElement
  public String getEntityDisplayNameFormat() {
    return entityDisplayNameFormat;
  }

  public void setEntityDisplayNameFormat(String entityDisplayNameFormat) {
    this.entityDisplayNameFormat = entityDisplayNameFormat;
  }

  /**
   * Returns a list of metric entity type names which are parents of this
   * metric entity type. A metric entity type inherits the attributes of
   * its ancestors. For example a role metric entity type has its service as a
   * parent. A service metric entity type has a cluster as a parent. The role
   * type inherits its cluster name attribute through its service parent. Only
   * parent ancestors should be returned here. In the example given, only the
   * service metric entity type should be specified in the parent list.
   */
  @XmlElement
  public List<String> getParentMetricEntityTypeNames() {
    return parentMetricEntityTypeNames;
  }

  public void setParentMetricEntityTypeNames(List<String> value) {
    this.parentMetricEntityTypeNames = value;
  }
}