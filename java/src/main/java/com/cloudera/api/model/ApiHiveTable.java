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

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * A Hive table identifier.
 */
@XmlRootElement(name = "hiveTable")
public class ApiHiveTable {

  private String database;
  private String tableName;

  public ApiHiveTable() {
    // For JAX-B
  }

  public ApiHiveTable(String database, String tableName) {
    this.database = database;
    this.tableName = tableName;
  }

  /** Name of the database to which this table belongs. */
  @XmlElement
  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  /**
   * Name of the table. When used as input for a replication job, this can
   * be a regular expression that matches several table names. Refer to the
   * Hive documentation for the syntax of regular expressions.
   */
  @XmlElement
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("database", database)
        .add("tableName", tableName)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHiveTable that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(database, that.getDatabase()) &&
        Objects.equal(tableName, that.getTableName()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(database, tableName);
  }

}
