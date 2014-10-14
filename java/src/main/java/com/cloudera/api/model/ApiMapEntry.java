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
import javax.xml.bind.annotation.XmlType;

/**
 * Models a map entry, with a key and a value. By forming a list of these
 * entries you can have the equivalent of Map&lt;String, String&gt; (since
 * JAX-B doesn't support maps).
 */
@XmlRootElement(name = "mapEntry")
@XmlType(propOrder = {"key", "value"})
public class ApiMapEntry {
  private String key;
  private String value;

  public ApiMapEntry() {
    // For JAX-B
  }

  public ApiMapEntry(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @Override
  public boolean equals(Object o) {
    ApiMapEntry that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(key, that.key) &&
        Objects.equal(value, that.value));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(key, value);
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("key", key)
        .add("value", value)
        .toString();
  }

  @XmlElement
  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  @XmlElement
  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }
}
