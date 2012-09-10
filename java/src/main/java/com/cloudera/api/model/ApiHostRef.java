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

/**
 * A reference to a host.
 */
public class ApiHostRef {

  private String hostId;

  public ApiHostRef() {
    // For JAX-B
    this(null);
  }

  public ApiHostRef(String hostId) {
    this.hostId = hostId;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
                  .add("hostId", hostId)
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

    ApiHostRef that = (ApiHostRef) o;

    if (hostId != null ? !hostId.equals(that.hostId) : that.hostId != null) {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return hostId != null ? hostId.hashCode() : 0;
  }

  /** The unique host ID. */
  public String getHostId() {
    return hostId;
  }

  public void setHostId(String hostId) {
    this.hostId = hostId;
  }

}
