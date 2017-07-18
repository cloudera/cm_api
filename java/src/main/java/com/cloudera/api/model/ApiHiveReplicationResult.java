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

  private String phase;
  private Integer tableCount;
  private List<ApiHiveTable> tables;
  private Integer impalaUDFCount;
  private List<ApiImpalaUDF> impalaUDFs;
  private Integer hiveUDFCount;
  private List<ApiHiveUDF> hiveUDFs;
  private Integer errorCount;
  private List<ApiHiveReplicationError> errors;
  private ApiHdfsReplicationResult dataReplicationResult;
  private boolean dryRun;
  private String runAsUser;

  public ApiHiveReplicationResult() {
    // For JAX-B
  }

  /**
   * Phase the replication is in.
   * <p/>
   * If the replication job is still active, this will contain a string
   * describing the current phase. This will be one of: EXPORT, DATA or
   * IMPORT, for, respectively, exporting the source metastore information,
   * replicating table data (if configured), and importing metastore
   * information in the target.
   * <p/>
   * This value will not be present if the replication is not active.
   * <p/>
   * Available since API v4.
   */
  @XmlElement
  public String getPhase() {
    return phase;
  }

  public void setPhase(String phase) {
    this.phase = phase;
  }

  /**
   * Number of tables that were successfully replicated. Available since API v4.
   */
  @XmlElement
  public Integer getTableCount() {
    return tableCount;
  }

  public void setTableCount(Integer tableCount) {
    this.tableCount = tableCount;
  }

  /**
   * The list of tables successfully replicated.
   * <p/>
   * Since API v4, this is only available in the full view.
   */
  @XmlElementWrapper
  public List<ApiHiveTable> getTables() {
    return tables;
  }

  public void setTables(List<ApiHiveTable> tables) {
    this.tables = tables;
  }

  /**
   * Number of impala UDFs that were successfully replicated. Available since
   * API v6.
   */
  @XmlElement
  public Integer getImpalaUDFCount() {
    return impalaUDFCount;
  }

  public void setImpalaUDFCount(Integer impalaUDFCount) {
    this.impalaUDFCount = impalaUDFCount;
  }

  /**
   * Number of hive UDFs that were successfully replicated. Available since
   * API v14.
   */
  @XmlElement
  public Integer getHiveUDFCount() {
    return hiveUDFCount;
  }

  public void setHiveUDFCount(Integer hiveUDFCount) {
    this.hiveUDFCount = hiveUDFCount;
  }

  /**
   * The list of Impala UDFs successfully replicated. Available since API v6
   * in the full view.
   */
  @XmlElementWrapper
  public List<ApiImpalaUDF> getImpalaUDFs() {
    return impalaUDFs;
  }

  public void setImpalaUDFs(List<ApiImpalaUDF> impalaUDFs) {
    this.impalaUDFs = impalaUDFs;
  }


  /**
   * The list of Impala UDFs successfully replicated. Available since API v6
   * in the full view.
   */
  @XmlElementWrapper
  public List<ApiHiveUDF> getHiveUDFs() {
    return hiveUDFs;
  }

  public void setHiveUDFs(List<ApiHiveUDF> hiveUDFs) {
    this.hiveUDFs = hiveUDFs;
  }

  /**
   * Number of errors detected during replication job.
   * Available since API v4.
   */
  @XmlElement
  public Integer getErrorCount() {
    return errorCount;
  }

  public void setErrorCount(Integer errorCount) {
    this.errorCount = errorCount;
  }

  /**
   * List of errors encountered during replication.
   * <p/>
   * Since API v4, this is only available in the full view.
   */
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

  /**
   * Name of the of proxy user, if any.
   * Available since API v11.
   */
  @XmlElement
  public String getRunAsUser() {
    return runAsUser;
  }

  public void setRunAsUser(String runAsUser) {
    this.runAsUser = runAsUser;
  }

  @Override
  public boolean equals(Object o) {
    ApiHiveReplicationResult that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(phase, that.getPhase()) &&
        Objects.equal(tables, that.getTables()) &&
        Objects.equal(impalaUDFs, that.getImpalaUDFs()) &&
        Objects.equal(hiveUDFs, that.getHiveUDFs()) &&
        Objects.equal(errors, that.getErrors()) &&
        dryRun == that.isDryRun() &&
        Objects.equal(runAsUser, that.getRunAsUser()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(phase, tables, impalaUDFs, hiveUDFs, errors, dryRun,
        runAsUser);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("phase", phase)
        .add("tables", tables)
        .add("impalaUDFs", impalaUDFs)
        .add("hiveUDFs", hiveUDFs)
        .add("errors", errors)
        .add("dryRun", dryRun)
        .add("runAsUser", runAsUser)
        .toString();
  }

}
