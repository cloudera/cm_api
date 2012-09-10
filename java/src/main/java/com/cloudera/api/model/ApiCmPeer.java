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
 * The <i>username</i> and <i>password</i> properties are only used when
 * creating peers. They should be the credentials of a user with administrator
 * privileges on the remote Cloudera Manager being linked. These credentials are
 * not stored; they're just used to create the peer.
 * <p>
 * When retrieving peer information, neither of the above fields are populated.
 */
@XmlRootElement(name = "cmPeer")
public class ApiCmPeer {

  private String name;
  private String url;
  private String username;
  private String password;

  /** The name of the link. */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /** The URL of the remote CM instance. */
  @XmlElement
  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  /** The remote admin username, for setting up the link. */
  @XmlElement
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  /** The remote admin password, for setting up the link. */
  @XmlElement
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("url", url)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiCmPeer other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(name, other.getName()) &&
        Objects.equal(url, other.getUrl()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name, url);
  }

}
