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
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * Detailed information about a Hive replication job.
 */
@XmlRootElement(name = "hiveReplicationResult")
public class ApiHiveReplicationResult {

  private List<ApiHiveTable> tables;
  private List<ApiHiveReplicationError> errors;
  private ApiHdfsReplicationResult dataReplicationResult;
  private boolean dryRun;

  public ApiHiveReplicationResult() {
    // For JAX-B
  }

  /** The list of tables successfully replicated. */
  @XmlElementWrapper
  public List<ApiHiveTable> getTables() {
    return tables;
  }

  public void setTables(List<ApiHiveTable> tables) {
    this.tables = tables;
  }

  /** List of errors encountered during replication. */
  @XmlElementWrapper
  public List<ApiHiveReplicationError> getErrors() {
    return errors;
  }

  public void setErrors(List<ApiHiveReplicationError> errors) {
    this.errors = errors;
  }

  /** Result of table data replication, if performed. */
  @XmlElement
  public ApiHdfsReplicationResult getDataReplicationResult() {
    return dataReplicationResult;
  }

  public void setDataReplicationResult(ApiHdfsReplicationResult dataReplicationResult) {
    this.dataReplicationResult = dataReplicationResult;
  }

  /** Whether this was a dry run. */
  @XmlElement
  public boolean isDryRun() {
    return dryRun;
  }

  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  @Override
  public boolean equals(Object o) {
    ApiHiveReplicationResult that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(tables, that.getTables()) &&
        Objects.equal(errors, that.getErrors()) &&
        dryRun == that.isDryRun());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(tables, errors, dryRun);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("tables", tables)
        .add("errors", errors)
        .add("dryRun", dryRun)
        .toString();
  }

}
