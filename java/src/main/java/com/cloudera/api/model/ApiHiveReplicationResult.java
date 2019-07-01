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
  private String runOnSourceAsUser;
  private boolean statsAvailable;
  private long dbProcessed;
  private long tableProcessed;
  private long partitionProcessed;
  private long functionProcessed;
  private long indexProcessed;
  private long statsProcessed;
  private long dbExpected;
  private long tableExpected;
  private long partitionExpected;
  private long functionExpected;
  private long indexExpected;
  private long statsExpected;

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

  /**
   * Name of the source proxy user, if any.
   * Available since API v18.
   */
  @XmlElement
  public String getRunOnSourceAsUser() {
    return runOnSourceAsUser;
  }

  public void setRunOnSourceAsUser(String runOnSourceAsUser) {
    this.runOnSourceAsUser = runOnSourceAsUser;
  }

  /**
   * Whether stats are available to display or not.
   * Available since API v19.
   */
  @XmlElement
  public boolean isStatsAvailable() {
    return statsAvailable;
  }

  public void setStatsAvailable(boolean statsAvailable) {
    this.statsAvailable = statsAvailable;
  }

  /**
   * Number of Db's Imported/Exported.
   * Available since API v19.
   */
  @XmlElement
  public long getDbProcessed() {
    return dbProcessed;
  }

  public void setDbProcessed(long dbProcessed) {
    this.dbProcessed = dbProcessed;
  }

  /**
   * Number of Tables Imported/Exported.
   * Available since API v19.
   */
  @XmlElement
  public long getTableProcessed() {
    return tableProcessed;
  }

  public void setTableProcessed(long tableProcessed) {
    this.tableProcessed = tableProcessed;
  }

  /**
   * Number of Partitions Imported/Exported.
   * Available since API v19.
   */
  @XmlElement
  public long getPartitionProcessed() {
    return partitionProcessed;
  }

  public void setPartitionProcessed(long partitionProcessed) {
    this.partitionProcessed = partitionProcessed;
  }

  /**
   * Number of Functions Imported/Exported.
   * Available since API v19.
   */
  @XmlElement
  public long getFunctionProcessed() {
    return functionProcessed;
  }

  public void setFunctionProcessed(long functionProcessed) {
    this.functionProcessed = functionProcessed;
  }

  /**
   * Number of Indexes Imported/Exported.
   * Available since API v19.
   */
  @XmlElement
  public long getIndexProcessed() {
    return indexProcessed;
  }

  public void setIndexProcessed(long indexProcessed) {
    this.indexProcessed = indexProcessed;
  }

  /**
   * Number of Table and Partitions Statistics Imported/Exported.
   * Available since API v19.
   */
  @XmlElement
  public long getStatsProcessed() {
    return statsProcessed;
  }

  public void setStatsProcessed(long statsProcessed) {
    this.statsProcessed = statsProcessed;
  }

  /**
   * Number of Db's Expected.
   * Available since API v19.
   */
  @XmlElement
  public long getDbExpected() {
    return dbExpected;
  }

  public void setDbExpected(long dbExpected) {
    this.dbExpected = dbExpected;
  }

  /**
   * Number of Tables Expected.
   * Available since API v19.
   */
  @XmlElement
  public long getTableExpected() {
    return tableExpected;
  }

  public void setTableExpected(long tableExpected) {
    this.tableExpected = tableExpected;
  }

  /**
   * Number of Partitions Expected.
   * Available since API v19.
   */
  @XmlElement
  public long getPartitionExpected() {
    return partitionExpected;
  }

  public void setPartitionExpected(long partitionExpected) {
    this.partitionExpected = partitionExpected;
  }

  /**
   * Number of Functions Expected.
   * Available since API v19.
   */
  @XmlElement
  public long getFunctionExpected() {
    return functionExpected;
  }

  public void setFunctionExpected(long functionExpected) {
    this.functionExpected = functionExpected;
  }

  /**
   * Number of Indexes Expected.
   * Available since API v19.
   */
  @XmlElement
  public long getIndexExpected() {
    return indexExpected;
  }

  public void setIndexExpected(long indexExpected) {
    this.indexExpected = indexExpected;
  }

  /**
   * Number of Table and Partition Statistics Expected.
   * Available since API v19.
   */
  @XmlElement
  public long getStatsExpected() {
    return statsExpected;
  }

  public void setStatsExpected(long statsExpected) {
    this.statsExpected = statsExpected;
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
        Objects.equal(runAsUser, that.getRunAsUser()) &&
        Objects.equal(runOnSourceAsUser, that.getRunOnSourceAsUser()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(phase, tables, impalaUDFs, hiveUDFs, errors, dryRun,
        runAsUser, runOnSourceAsUser);
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
        .add("runOnSourceAsUser", runOnSourceAsUser)
        .toString();
  }

}
