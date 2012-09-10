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

import java.util.Date;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * A counter in an HDFS replication job.
 */
@XmlRootElement(name = "hdfsReplicationCounter")
public class ApiHdfsReplicationCounter {

  private String group;
  private String name;
  private Long value;

  public ApiHdfsReplicationCounter() {
    // For JAX-B.
  }

  public ApiHdfsReplicationCounter(String group, String name, Long value) {
    this.group = group;
    this.name = name;
    this.value = value;
  }

  @XmlElement
  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  @XmlElement
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @XmlElement
  public Long getValue() {
    return value;
  }

  public void setValue(Long value) {
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    ApiHdfsReplicationCounter that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(group, that.getGroup()) &&
        Objects.equal(name, that.getName()) &&
        Objects.equal(value, that.getValue()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(group, name, value);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("group", group)
        .add("name", name)
        .add("value", value)
        .toString();
  }

}
