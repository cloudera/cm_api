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

import com.google.common.base.Objects;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

// TODO: get rid of the following annotation when we upgrade to Jackson 1.9.
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Information about the Cloudera Manager license.
 */
@XmlRootElement(name = "license")
public class ApiLicense {

  private String owner;
  private String uuid;
  private Date expiration;

  public ApiLicense() {
    // For JAX-B.
    this(null, null, null);
  }

  public ApiLicense(String owner, String uuid, Date expiration) {
    this.owner = owner;
    this.uuid = uuid;
    this.expiration = expiration;
  }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("owner", owner)
                  .add("uuid", uuid)
                  .add("expiration", expiration)
                  .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ApiLicense other = (ApiLicense) o;
    return Objects.equal(owner, other.owner) &&
        Objects.equal(uuid, other.uuid) &&
        Objects.equal(expiration, other.expiration);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(owner, uuid, expiration);
  }

  /** The owner (organization name) of the license. */
  @XmlElement
  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  /** A UUID of this license. */
  @XmlElement
  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  /** The expiration date. */
  @XmlElement
  public Date getExpiration() {
    return expiration;
  }

  public void setExpiration(Date expiration) {
    this.expiration = expiration;
  }
}
