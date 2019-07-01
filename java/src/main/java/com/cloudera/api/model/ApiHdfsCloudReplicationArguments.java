// Copyright (c) 2016 Cloudera, Inc. All rights reserved.
package com.cloudera.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import org.apache.cxf.common.util.StringUtils;

@XmlRootElement(name = "hdfsCloudReplicationArguments")
public class ApiHdfsCloudReplicationArguments extends ApiHdfsReplicationArguments {

  /**
   * Source Account during replication. If this is non-null,
   * destinationAccount should be null
   */
  private String sourceAccount;

  /**
   * destination Account during replication. If this is non-null,
   * sourceAccount should be null
   */
  private String destinationAccount;

  // For JAX-B
  public ApiHdfsCloudReplicationArguments() {
  }

  public ApiHdfsCloudReplicationArguments(ApiServiceRef sourceService,
      String sourcePath, String destinationPath,
      String mapreduceServiceName, Integer numMaps, String userName,
      String sourceAccount, String destinationAccount) {
    super(sourceService, sourcePath, destinationPath, mapreduceServiceName,
        numMaps, userName);
    this.sourceAccount = sourceAccount;
    this.destinationAccount = destinationAccount;
  }

  @XmlElement
  public String getSourceAccount() {
    return sourceAccount;
  }

  public void setSourceAccount(String sourceAccount) {
    this.sourceAccount = sourceAccount;
  }

  @XmlElement
  public String getDestinationAccount() {
    return destinationAccount;
  }

  public void setDestinationAccount(String destinationAccount) {
    this.destinationAccount = destinationAccount;
  }

  @Override
  public String toString() {
    if (!StringUtils.isEmpty(sourceAccount)) {
      return super.toStringHelper()
          .add("sourceAccount", sourceAccount)
          .toString();
    } else if (!StringUtils.isEmpty(destinationAccount)) {
      return super.toStringHelper()
          .add("destinationAccount", destinationAccount)
          .toString();
    }

    Preconditions.checkState(false, "Both sourceAccount and destinationAccount are null " +
        "in ApiHdfsCloudReplicationArguments");
    return null;
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsCloudReplicationArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        super.equals(other) &&
        Objects.equal(sourceAccount, other.sourceAccount) &&
        Objects.equal(destinationAccount, other.destinationAccount));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), sourceAccount, destinationAccount);
  }
}
