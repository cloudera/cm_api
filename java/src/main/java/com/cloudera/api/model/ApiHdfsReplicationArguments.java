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

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Replication arguments for HDFS.
 */
@XmlRootElement(name = "hdfsReplicationArguments")
public class ApiHdfsReplicationArguments {
  private ApiServiceRef sourceService;
  private String sourcePath;
  private String destinationPath;
  private String mapreduceServiceName;
  private String schedulerPoolName;
  private String userName;
  private Integer numMaps;
  private boolean dryRun;
  private Integer bandwidthPerMap;
  private boolean abortOnError;
  private boolean removeMissingFiles;
  private boolean preserveReplicationCount;
  private boolean preserveBlockSize;
  private boolean preservePermissions;
  private String logPath;
  private boolean skipChecksumChecks;
  private Boolean skipTrash;
  private ReplicationStrategy replicationStrategy;
  private Boolean preserveXAttrs;
  private List<String> exclusionFilters;

  /**
   * The strategy for distributing the file replication tasks among the mappers
   * of the MR job associated with a replication.
   */
  public enum ReplicationStrategy {
    /**
     * Distributes file replication tasks among the mappers up front, trying to
     * achieve a uniform distribution based on the file sizes.
     */
    STATIC,
    /**
     * Distributes file replication tasks in small sets to the mappers, and
     * as each mapper is done processing its set of tasks, it picks up and
     * processes the next unallocated set of tasks.
     */
    DYNAMIC
  };

  // For JAX-B
  public ApiHdfsReplicationArguments() {
  }

  public ApiHdfsReplicationArguments(ApiServiceRef sourceService,
      String sourcePath, String destinationPath, String mapreduceServiceName,
      Integer numMaps, String userName) {
    this.sourceService = sourceService;
    this.sourcePath = sourcePath;
    this.destinationPath = destinationPath;
    this.mapreduceServiceName = mapreduceServiceName;
    this.numMaps = numMaps;
    this.userName = userName;
  }

  /** The service to replicate from. */
  @XmlElement
  public ApiServiceRef getSourceService() {
    return sourceService;
  }

  public void setSourceService(ApiServiceRef sourceService) {
    this.sourceService = sourceService;
  }

  /** The path to replicate. */
  @XmlElement
  public String getSourcePath() {
    return sourcePath;
  }

  public void setSourcePath(String path) {
    this.sourcePath = path;
  }

  /** The destination to replicate to. */
  @XmlElement
  public String getDestinationPath() {
    return destinationPath;
  }

  public void setDestinationPath(String path) {
    this.destinationPath = path;
  }

  /** The mapreduce service to use for the replication job. */
  @XmlElement
  public String getMapreduceServiceName() {
    return mapreduceServiceName;
  }

  public void setMapreduceServiceName(String name) {
    this.mapreduceServiceName = name;
  }

  /**
   * Name of the scheduler pool to use when submitting the MapReduce job.
   * Currently supports the capacity and fair schedulers. The option is
   * ignored if a different scheduler is configured.
   */
  @XmlElement
  public String getSchedulerPoolName() {
    return schedulerPoolName;
  }

  public void setSchedulerPoolName(String schedulerPoolName) {
    this.schedulerPoolName = schedulerPoolName;
  }

  /**
   * The user which will execute the MapReduce job. Required if running with
   * Kerberos enabled.
   */
  @XmlElement
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /** The number of mappers to use for the mapreduce replication job. */
  @XmlElement
  public Integer getNumMaps() {
    return numMaps;
  }

  public void setNumMaps(Integer numMaps) {
    this.numMaps = numMaps;
  }

  /** Whether to perform a dry run. Defaults to false. */
  @XmlElement
  public boolean isDryRun() {
    return dryRun;
  }

  public void setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
  }

  /**
   * The maximum bandwidth (in MB) per mapper in the mapreduce replication
   * job.
   */
  @XmlElement
  public Integer getBandwidthPerMap() {
    return bandwidthPerMap;
  }

  public void setBandwidthPerMap(Integer bandwidthPerMap) {
    this.bandwidthPerMap = bandwidthPerMap;
  }

  /** Whether to abort on a replication failure. Defaults to false. */
  @XmlElement
  public boolean getAbortOnError() {
    return abortOnError;
  }

  public void setAbortOnError(boolean abortOnError) {
    this.abortOnError = abortOnError;
  }

  /**
   * Whether to delete destination files that are missing in source. Defaults
   * to false.
   */
  @XmlElement
  public boolean getRemoveMissingFiles() {
    return removeMissingFiles;
  }

  public void setRemoveMissingFiles(boolean removeMissingFiles) {
    this.removeMissingFiles = removeMissingFiles;
  }

  /** Whether to preserve the HDFS replication count. Defaults to false. */
  @XmlElement
  public boolean getPreserveReplicationCount() {
    return preserveReplicationCount;
  }

  public void setPreserveReplicationCount(boolean preserveReplicationCount) {
    this.preserveReplicationCount = preserveReplicationCount;
  }

  /** Whether to preserve the HDFS block size. Defaults to false. */
  @XmlElement
  public boolean getPreserveBlockSize() {
    return preserveBlockSize;
  }

  public void setPreserveBlockSize(boolean preserveBlockSize) {
    this.preserveBlockSize = preserveBlockSize;
  }

  /**
   * Whether to preserve the HDFS owner, group and permissions. Defaults to
   * false.
   * Starting from V10, it also preserves ACLs. Defaults to null (no preserve).
   * ACLs is preserved if both clusters enable ACL support, and replication
   * ignores any ACL related failures.
   */
  @XmlElement
  public boolean getPreservePermissions() {
    return preservePermissions;
  }

  public void setPreservePermissions(boolean preservePermissions) {
    this.preservePermissions = preservePermissions;
  }

  /** The HDFS path where the replication log files should be written to. */
  @XmlElement
  public String getLogPath() {
    return logPath;
  }

  public void setLogPath(String logPath) {
    this.logPath = logPath;
  }

  /**
   * Whether to skip checksum based file validation/comparison during
   * replication. Defaults to false.
   */
  @XmlElement
  public boolean getSkipChecksumChecks() {
    return skipChecksumChecks;
  }

  public void setSkipChecksumChecks(boolean skipChecksumChecks) {
    this.skipChecksumChecks = skipChecksumChecks;
  }

  /**
   * Whether to permanently delete destination files that are missing in source.
   * Defaults to null.
   */
  @XmlElement
  public Boolean getSkipTrash() {
    return skipTrash;
  }

  public void setSkipTrash(Boolean skipTrash) {
    this.skipTrash = skipTrash;
  }

  /**
   * The strategy for distributing the file replication tasks among the mappers
   * of the MR job associated with a replication. Default is
   * {@link ReplicationStrategy#STATIC}.
   */
  @XmlElement
  public ReplicationStrategy getReplicationStrategy() {
    return replicationStrategy;
  }

  public void setReplicationStrategy(ReplicationStrategy replicationStrategy) {
    this.replicationStrategy = replicationStrategy;
  }

  /**
   * Whether to preserve XAttrs, default to false
   * This is introduced in V10. To preserve XAttrs, both CDH versions
   * should be >= 5.2. Replication fails if either cluster does not support
   * XAttrs.
   */
  @XmlElement
  public Boolean getPreserveXAttrs() {
    return preserveXAttrs;
  }

  public void setPreserveXAttrs(Boolean preserveXAttrs) {
    this.preserveXAttrs = preserveXAttrs;
  }

  /**
   * Specify regular expression strings to match full paths of files and directories
   * matching source paths and exclude them from the replication. Optional.
   * Available since V11.
   * @return exclusion paths, if set; null if no exclusion paths are specified.
   */
  @XmlElement
  public List<String> getExclusionFilters() {
    return exclusionFilters;
  }

  public void setExclusionFilters(List<String> exclusionFilters) {
    this.exclusionFilters = exclusionFilters;
  }

  protected Objects.ToStringHelper toStringHelper() {
    return Objects.toStringHelper(this)
        .add("sourceService", sourceService)
        .add("sourcePath", sourcePath)
        .add("destinationPath", destinationPath)
        .add("mapreduceServiceName", mapreduceServiceName)
        .add("schedulerPoolName", schedulerPoolName)
        .add("numMaps", numMaps)
        .add("dryRun", dryRun)
        .add("bandwidthPerMap", bandwidthPerMap)
        .add("abortOnError", abortOnError)
        .add ("removeMissingFiles", removeMissingFiles)
        .add("preserveReplicationCount", preserveReplicationCount)
        .add("preserveBlockSize", preserveBlockSize)
        .add("preservePermissions", preservePermissions)
        .add("logPath", logPath)
        .add("skipChecksumChecks", skipChecksumChecks)
        .add("skipTrash", skipTrash)
        .add("replicationStrategy", replicationStrategy)
        .add("preserveXAttrs", preserveXAttrs)
        .add("exclusionFilters", exclusionFilters);
  }

  @Override
  public String toString() {
    return toStringHelper().toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsReplicationArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(sourceService, other.getSourceService()) &&
        Objects.equal(sourcePath, other.getSourcePath()) &&
        Objects.equal(destinationPath, other.getDestinationPath()) &&
        Objects.equal(mapreduceServiceName, other.getMapreduceServiceName()) &&
        Objects.equal(schedulerPoolName, other.getSchedulerPoolName()) &&
        Objects.equal(numMaps, other.getNumMaps()) &&
        dryRun == other.isDryRun() &&
        Objects.equal(bandwidthPerMap, other.getBandwidthPerMap()) &&
        abortOnError == other.getAbortOnError() &&
        removeMissingFiles == other.getRemoveMissingFiles() &&
        preserveReplicationCount == other.getPreserveReplicationCount() &&
        preserveBlockSize == other.getPreserveBlockSize() &&
        preservePermissions == other.getPreservePermissions() &&
        Objects.equal(logPath, other.getLogPath()) &&
        skipChecksumChecks == other.getSkipChecksumChecks() &&
        Objects.equal(skipTrash, other.getSkipTrash()) &&
        Objects.equal(replicationStrategy, other.getReplicationStrategy()) &&
        Objects.equal(preserveXAttrs, other.getPreserveXAttrs())) &&
        Objects.equal(exclusionFilters, other.getExclusionFilters());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(sourceService, sourcePath, destinationPath,
        mapreduceServiceName, schedulerPoolName, numMaps, dryRun,
        bandwidthPerMap, abortOnError, removeMissingFiles,
        preserveReplicationCount, preserveBlockSize, preservePermissions,
        logPath, skipChecksumChecks, skipTrash, replicationStrategy,
        preserveXAttrs, exclusionFilters);
  }
}
