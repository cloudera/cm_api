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
 * Replication arguments for Hive services.
 */
@XmlRootElement(name = "hiveReplicationArguments")
public class ApiHiveReplicationArguments {

  private ApiServiceRef sourceService;
  private List<ApiHiveTable> tableFilters;
  private String exportDir;
  private boolean force;

  private boolean replicateData;
  private ApiHdfsReplicationArguments hdfsArguments;

  private Boolean replicateImpalaMetadata;

  private boolean dryRun;

  public ApiHiveReplicationArguments() {
    // For JAX-B
  }

  /** The service to replicate from. */
  @XmlElement
  public ApiServiceRef getSourceService() {
    return sourceService;
  }

  public void setSourceService(ApiServiceRef sourceService) {
    this.sourceService = sourceService;
  }

  /**
   * Filters for tables to include in the replication. Optional. If not
   * provided, include all tables in all databases.
   */
  @XmlElementWrapper
  public List<ApiHiveTable> getTableFilters() {
    return tableFilters;
  }

  public void setTableFilters(List<ApiHiveTable> tableFilters) {
    this.tableFilters = tableFilters;
  }

  /**
   * Directory, in the HDFS service where the target Hive service's data is
   * stored, where the export file will be saved. Optional. If not provided,
   * Cloudera Manager will pick a directory for storing the data.
   */
  @XmlElement
  public String getExportDir() {
    return exportDir;
  }

  public void setExportDir(String exportDir) {
    this.exportDir = exportDir;
  }

  /** Whether to force overwriting of mismatched tables. */
  @XmlElement
  public boolean getForce() {
    return force;
  }

  public void setForce(boolean force) {
    this.force = force;
  }

  /**
   * Whether to replicate table data stored in HDFS.
   * <p/>
   * If set, the "hdfsArguments" property must be set to configure the
   * HDFS replication job.
   */
  @XmlElement
  public boolean getReplicateData() {
    return replicateData;
  }

  public void setReplicateData(boolean replicateData) {
    this.replicateData = replicateData;
  }

  /**
   * Arguments for the HDFS replication job.
   * <p/>
   * This must be provided when choosing to replicate table data stored in HDFS.
   * The "sourceService", "sourcePath" and "dryRun" properties of the HDFS
   * arguments are ignored; their values are derived from the Hive replication's
   * information.
   * <p/>
   * The "destinationPath" property is used slightly differently from the
   * usual HDFS replication jobs. It is used to map the root path of the
   * source service into the target service. It may be omitted, in which
   * case the source and target paths will match.
   * <p/>
   * Example: if the destination path is set to "/new_root", a "/foo/bar" path
   * in the source will be stored in "/new_root/foo/bar" in the target.
   */
  @XmlElement
  public ApiHdfsReplicationArguments getHdfsArguments() {
    return hdfsArguments;
  }

  public void setHdfsArguments(ApiHdfsReplicationArguments hdfsArguments) {
    this.hdfsArguments = hdfsArguments;
  }

  /**
   * Whether to replicate the impala metadata. (i.e. the metadata for impala
   * UDFs and their corresponding binaries in HDFS).
   */
  @XmlElement
  public Boolean getReplicateImpalaMetadata() {
    return replicateImpalaMetadata;
  }

  public void setReplicateImpalaMetadata(Boolean replicateImpalaMetadata) {
    this.replicateImpalaMetadata = replicateImpalaMetadata;
  }

  /** Whether to perform a dry run. Defaults to false. */
  @XmlElement
  public boolean isDryRun() {
    return dryRun;
  }

  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  protected Objects.ToStringHelper toStringHelper() {
    return Objects.toStringHelper(this)
        .add("sourceService", sourceService)
        .add("tableFilters", tableFilters)
        .add("exportDir", exportDir)
        .add("force", force)
        .add("replicateHdfs", replicateData)
        .add("hdfsArguments", hdfsArguments)
        .add("replicateImpalaMetadata", replicateImpalaMetadata)
        .add("dryRun", dryRun);
  }

  @Override
  public String toString() {
    return toStringHelper().toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHiveReplicationArguments that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(sourceService, that.getSourceService()) &&
        Objects.equal(tableFilters, that.getTableFilters()) &&
        Objects.equal(exportDir, that.getExportDir()) &&
        force == that.getForce() &&
        replicateData == that.getReplicateData() &&
        Objects.equal(hdfsArguments, that.getHdfsArguments()) &&
        Objects.equal(replicateImpalaMetadata,
            that.getReplicateImpalaMetadata()) &&
        dryRun == that.isDryRun());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(sourceService, tableFilters, exportDir, force,
        replicateData, hdfsArguments, replicateImpalaMetadata, dryRun);
  }

}
