// Copyright (c) 2016 Cloudera, Inc. All rights reserved.
package com.cloudera.api.model;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * The state of Hive/HDFS Replication.
 */
public class ApiReplicationState {
  private Boolean incrementalExportEnabled;

  public ApiReplicationState() {
    this(false);
  }

  public ApiReplicationState(Boolean incrementalExportEnabled) {
    this.incrementalExportEnabled = incrementalExportEnabled;
  }

  /**
   *
   * returns if incremental export is enabled for the given Hive service.
   * Not applicable for HDFS service.
   */
  public Boolean getIncrementalExportEnabled() {
    return incrementalExportEnabled;
  }

  public void setIncrementalExportEnabled(Boolean incrementalExportEnabled) {
    this.incrementalExportEnabled = incrementalExportEnabled;
  }

  @Override
  public String toString() {
    return toStringHelper().toString();
  }

  protected Objects.ToStringHelper toStringHelper() {
    return Objects.toStringHelper(this)
        .add("incrementalExportEnabled", incrementalExportEnabled);
  }

  @Override
  public boolean equals(Object o) {
    ApiReplicationState that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(incrementalExportEnabled, that.getIncrementalExportEnabled()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(incrementalExportEnabled);
  }
}
