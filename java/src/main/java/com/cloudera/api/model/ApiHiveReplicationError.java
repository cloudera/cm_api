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
 * A Hive replication error.
 */
@XmlRootElement(name = "hiveReplicationError")
public class ApiHiveReplicationError {

  private String database;
  private String tableName;
  private String error;

  public ApiHiveReplicationError() {
    // For JAX-B
  }

  public ApiHiveReplicationError(String database, String tableName,
      String error) {
    this.database = database;
    this.tableName = tableName;
    this.error = error;
  }

  /** Name of the database. */
  @XmlElement
  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  /** Name of the table. */
  @XmlElement
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  /** Description of the error. */
  @XmlElement
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("database", database)
        .add("tableName", tableName)
        .add("error", error)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHiveReplicationError that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(database, that.getDatabase()) &&
        Objects.equal(tableName, that.getTableName()) &&
        Objects.equal(error, that.getError()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(database, tableName, error);
  }

}
