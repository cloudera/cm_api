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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Information about a Cloudera Manager peer instance.
 * <p>
 * The requirement and usage of <i>username</i> and <i>password</i> properties
 * are dependent on the <i>clouderaManagerCreatedUser</i> flag.
 * <p>
 * When creating peers, if 'clouderaManagerCreatedUser' is true, the
 * username/password should be the credentials of a user with administrator
 * privileges on the remote Cloudera Manager. These credentials are not stored,
 * they are used to connect to the peer and create a user in that peer. The
 * newly created user is stored and used for communication with that peer.
 * If 'clouderaManagerCreatedUser' is false, which is not applicable to
 * REPLICATION peer type, the username/password to the remote Cloudera Manager
 * are directly stored and used for all communications with that peer.
 * <p>
 * When updating peers, if 'clouderaManagerCreatedUser' is true and
 * username/password are set, a new remote user will be created. If
 * 'clouderaManagerCreatedUser' is false and username/password are set, the
 * stored username/password will be updated.
 */
@XmlRootElement(name = "cmPeer")
public class ApiCmPeer {

  private String name;
  private ApiCmPeerType type;
  private String url;
  private String username;
  private String password;
  private Boolean clouderaManagerCreatedUser;

  /** The name of the remote CM instance. Immutable during update.*/
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The type of the remote CM instance. Immutable during update.
   *
   * Available since API v11.
   **/
  @XmlElement
  public ApiCmPeerType getType() {
    return type;
  }

  public void setType(ApiCmPeerType type) {
    this.type = type;
  }

  /** The URL of the remote CM instance. Mutable during update.*/
  @XmlElement
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * When creating peers, if 'clouderaManagerCreatedUser' is true, this should be
   * the remote admin username for creating a user in remote Cloudera Manager. The
   * created remote user will then be stored in the local Cloudera Manager DB and
   * used in later communication. If 'clouderaManagerCreatedUser' is false, which
   * is not applicable to REPLICATION peer type, Cloudera Manager will store this
   * username in the local DB directly and use it together with 'password' for
   * communication.
   *
   * Mutable during update.
   * When set during update, if 'clouderaManagerCreatedUser' is true, a new user
   * in remote Cloudera Manager is created, the newly created remote user will be
   * stored in the local DB. An attempt to delete the previously created remote
   * user will be made; If 'clouderaManagerCreatedUser' is false, the
   * username/password in the local DB will be updated.
   **/
  @XmlElement
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * When creating peers, if 'clouderaManagerCreatedUser' is true, this should be
   * the remote admin password for creating a user in remote Cloudera Manager. The
   * created remote user will then be stored in the local Cloudera Manager DB and
   * used in later communication. If 'clouderaManagerCreatedUser' is false, which
   * is not applicable to REPLICATION peer type, Cloudera Manager will store this
   * password in the local DB directly and use it together with 'username' for
   * communication.
   *
   * Mutable during update.
   * When set during update, if 'clouderaManagerCreatedUser' is true, a new user
   * in remote Cloudera Manager is created, the newly created remote user will be
   * stored in the local DB. An attempt to delete the previously created remote
   * user will be made; If 'clouderaManagerCreatedUser' is false, the
   * username/password in the local DB will be updated.
   **/
  @XmlElement
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * If true, Cloudera Manager creates a remote user using the given
   * username/password and stores the created user in local DB for use in later
   * communication. Cloudera Manager will also try to delete the created remote
   * user when deleting such peers.
   *
   * If false, Cloudera Manager will store the provided username/password in
   * the local DB and use them in later communication. 'false' value on this
   * field is not applicable to REPLICATION peer type.
   *
   * Available since API v11.
   *
   * Immutable during update. Should not be set when updating peers.
   **/
  @XmlElement
  public Boolean getClouderaManagerCreatedUser() {
    return clouderaManagerCreatedUser;
  }

  public void setClouderaManagerCreatedUser(Boolean clouderaManagerCreatedUser) {
    this.clouderaManagerCreatedUser = clouderaManagerCreatedUser;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("url", url)
                  .add("type", type)
                  .add("clouderaManagerCreatedUser", clouderaManagerCreatedUser)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiCmPeer other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(name, other.getName()) &&
        Objects.equal(url, other.getUrl()) &&
        Objects.equal(type, other.getType()) &&
        Objects.equal(clouderaManagerCreatedUser,
                      other.getClouderaManagerCreatedUser()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, url, type, clouderaManagerCreatedUser);
  }
}
