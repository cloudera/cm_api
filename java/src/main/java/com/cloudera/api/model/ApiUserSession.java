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

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This is the model for interactive user session information in the API.
 * <p>
 * A user may have more than one active session. Each such session will have
 * its own session object.
 */
@XmlRootElement(name = "userSession")
public class ApiUserSession {

  private String name;
  private String remoteAddr;
  private Date lastRequest;

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("remoteAddr", remoteAddr)
                  .add("expiration", lastRequest.toString())
                  .toString();
  }

  public boolean equals(Object o) {
    ApiUserSession other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(name, other.getName()) &&
        Objects.equal(remoteAddr, other.getRemoteAddr()) &&
        Objects.equal(lastRequest, other.getLastRequest()));
  }

  public int hashCode() {
    return Objects.hashCode(name, remoteAddr, lastRequest);
  }

  /**
   * The username associated with the session.
   * <p>
   * This will be the same value shown to the logged in user in the UI, which
   * will normally be the same value they typed when logging in, but it is
   * possible that in certain external authentication scenarios, it will differ
   * from that value.
   *
   * @return the username
   * */
  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * The remote IP address for the session.
   * <p>
   * This will be the remote IP address for the last request made as part of
   * this session. It is not guaranteed to be the same IP address as was
   * previously used, or the address used to initiate the session.
   *
   * @return The remote IP address.
   */
  @XmlElement
  public String getRemoteAddr() {
    return remoteAddr;
  }

  public void setRemoteAddr(String remoteAddr) {
    this.remoteAddr = remoteAddr;
  }

  /**
   * The date and time of the last request received as part of this session.
   * <p>
   * This will be returned in ISO 8601 format from the REST API.
   *
   * @return The date/time.
   */
  @XmlElement
  public Date getLastRequest() {
    return lastRequest;
  }

  public void setLastRequest(Date lastRequest) {
    this.lastRequest = lastRequest;
  }
}
