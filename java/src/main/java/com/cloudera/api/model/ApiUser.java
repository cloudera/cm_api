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
import com.google.common.collect.Sets;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * This is the model for user information in the API.
 * <p>
 * Note that any method that returns user information will not contain any
 * password information. The password property is only used when creating
 * or updating users.
 */
@XmlRootElement(name = "user")
public class ApiUser {

  private String name;
  private String password;
  private Set<String> roles;
  private String pwHash;
  private Long pwSalt;
  private Boolean pwLogin;

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("roles", roles)
                  .toString();
  }

  public boolean equals(Object o) {
    ApiUser other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(name, other.getName()) &&
        Objects.equal(roles, other.getRoles()));
  }

  public int hashCode() {
    int hashCode = 0;
    if (name != null) {
      hashCode += name.hashCode();
    }
    if (roles != null) {
      for (String role : roles) {
        hashCode += role.hashCode();
      }
    }
    return hashCode;
  }

  /** The username, which is unique within a Cloudera Manager installation. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the user password.
   * <p>
   * Passwords are not returned when querying user information, so this
   * property will always be empty when reading information from a server.
   *
   * @return The user's password (may be null).
   */
  @XmlElement
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user password.
   * <p>
   * When creating a user or updating an existing user, this property should
   * contain the plain text user password to be set.
   *
   * @param password Plain text user password.
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * A list of roles this user belongs to.
   * <p>
   * In Cloudera Express, possible values are:
   * <ul>
   * <li><b>ROLE_ADMIN</b></li>
   * <li><b>ROLE_USER</b></li>
   * </ul>
   * In Cloudera Enterprise Datahub Edition, additional possible values are:
   * <ul>
   * <li><b>ROLE_LIMITED</b>: Added in Cloudera Manager 5.0</li>
   * <li><b>ROLE_OPERATOR</b>: Added in Cloudera Manager 5.1</li>
   * <li><b>ROLE_CONFIGURATOR</b>: Added in Cloudera Manager 5.1</li>
   * <li><b>ROLE_CLUSTER_ADMIN</b>: Added in Cloudera Manager 5.2</li>
   * <li><b>ROLE_BDR_ADMIN</b>: Added in Cloudera Manager 5.2</li>
   * <li><b>ROLE_NAVIGATOR_ADMIN</b>: Added in Cloudera Manager 5.2</li>
   * <li><b>ROLE_USER_ADMIN</b>: Added in Cloudera Manager 5.2</li>
   * </ul>
   * An empty list implies ROLE_USER.
   * <p>
   * Note that although this interface provides a list of roles, a user should
   * only be assigned a single role at a time.
   */
  @XmlElementWrapper(name = "roles")
  public Set<String> getRoles() {
    return roles;
  }

  public void setRoles(Set<String> roles) {
    if (roles != null) {
      this.roles = Sets.newHashSet(roles);
    } else {
      this.roles = null;
    }
  }

  public void addRole(String role) {
    if (roles == null) {
      roles = Sets.newHashSet();
    }
    roles.add(role);
  }

  /**
   * NOTE: Only available in the "export" view
   * @return The password hash for the user
   */
  @XmlElement
  public String getPwHash() {
    return pwHash;
  }

  public void setPwHash(String pwHash) {
    this.pwHash = pwHash;
  }

  /**
   * NOTE: Only available in the "export" view
   * @return The password salt for the user
   */
  @XmlElement
  public Long getPwSalt() {
    return pwSalt;
  }

  public void setPwSalt(Long pwSalt) {
    this.pwSalt = pwSalt;
  }

  /**
   * NOTE: Only available in the "export" view
   * @return Whether the user is authenticated using the local password info
   */
  @XmlElement
  public Boolean getPwLogin() {
    return pwLogin;
  }

  public void setPwLogin(Boolean pwLogin) {
    this.pwLogin = pwLogin;
  }
}
