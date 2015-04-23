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

@XmlRootElement(name="migrateRolesArgs")
public class ApiMigrateRolesArguments {

  private List<String> roleNamesToMigrate;
  private String destinationHostId;
  private boolean clearStaleRoleData;

  /**
   * The list of role names to migrate.
   */
  @XmlElement
  public List<String> getRoleNamesToMigrate() {
    return roleNamesToMigrate;
  }

  public void setRoleNamesToMigrate(List<String> roleNamesToMigrate) {
    this.roleNamesToMigrate = roleNamesToMigrate;
  }

  /**
   * The ID of the host to which the roles should be migrated.
   */
  @XmlElement
  public String getDestinationHostId() {
    return destinationHostId;
  }

  public void setDestinationHostId(String destinationHostId) {
    this.destinationHostId = destinationHostId;
  }

  /**
   * Delete existing stale role data, if any. For example, when migrating
   * a NameNode, if the destination host has stale data in the NameNode data
   * directories (possibly because a NameNode role was previously located
   * there), this stale data will be deleted before migrating the role.
   */
  @XmlElement
  public boolean getClearStaleRoleData() {
    return clearStaleRoleData;
  }

  public void setClearStaleRoleData(boolean clearStaleRoleData) {
    this.clearStaleRoleData = clearStaleRoleData;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("roleNamesToMigrate", roleNamesToMigrate)
        .add("destinationHostId", destinationHostId)
        .add("clearStaleRoleData", clearStaleRoleData)
        .toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(
        roleNamesToMigrate,
        destinationHostId,
        clearStaleRoleData);
  }

  @Override
  public boolean equals(Object o) {
    ApiMigrateRolesArguments that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null
        && Objects.equal(this.roleNamesToMigrate, that.roleNamesToMigrate)
        && Objects.equal(this.destinationHostId, that.destinationHostId)
        && Objects.equal(this.clearStaleRoleData, that.clearStaleRoleData));
  }
}
