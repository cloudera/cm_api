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
import com.google.common.base.Preconditions;
import org.apache.cxf.common.util.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "hiveCloudReplicationArguments")
public class ApiHiveCloudReplicationArguments extends ApiHiveReplicationArguments {

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

  /**
   * path at which data will be restored or backed up.
   */
  private String cloudRootPath;

  /**
   * This will decide how cloud replication will take place
   */
  public enum ReplicationOption {
    // This is used for backup as well as restore operation,
    // Use this, when you want to store/get only metadata to/from cloud
    METADATA_ONLY,

    // This is used for backup as well as restore operation,
    // Use this, when you want to store/get complete metadata/data to/from cloud
    METADATA_AND_DATA,

    // This is used for restore operation only,
    // Use this, when you want to get metadata and hive entities are pointing to
    // data on cloud.
    KEEP_DATA_IN_CLOUD
  }

  /**
   * Decides the type of hive cloud replication.
   */
  private ReplicationOption replicationOption;

  public ApiHiveCloudReplicationArguments() {
    // For JAX-B
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

  public String getCloudRootPath() {
    return cloudRootPath;
  }

  public void setCloudRootPath(String cloudRootPath) {
    this.cloudRootPath = cloudRootPath;
  }

  public ReplicationOption getReplicationOption() {
    return replicationOption;
  }

  public void setReplicationOption(ReplicationOption replicationOption) {
    this.replicationOption = replicationOption;
  }

  @Override
  public String toString() {
    Objects.ToStringHelper stringHelper = super.toStringHelper();
    if (!StringUtils.isEmpty(sourceAccount)) {
      stringHelper.add("sourceAccount", sourceAccount);
    } else if (!StringUtils.isEmpty(destinationAccount)) {
      stringHelper.add("destinationAccount", destinationAccount);
    }

    stringHelper.add("cloudRootPath", cloudRootPath);
    stringHelper.add("replicationOption", replicationOption);
    return stringHelper.toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHiveCloudReplicationArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        super.equals(other) &&
        Objects.equal(sourceAccount, other.sourceAccount) &&
        Objects.equal(destinationAccount, other.destinationAccount) &&
        Objects.equal(cloudRootPath, other.cloudRootPath) &&
        Objects.equal(replicationOption, other.replicationOption));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(super.hashCode(), sourceAccount, destinationAccount,
        cloudRootPath, replicationOption);
  }
}
