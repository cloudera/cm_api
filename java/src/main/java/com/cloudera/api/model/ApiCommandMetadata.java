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
 * Provides metadata information about a command.
 */
@XmlRootElement(name = "commandMetadata")
public class ApiCommandMetadata {

  private String name;
  private String argSchema;

  public ApiCommandMetadata(String name, String argSchema) {
    this.name = name;
    this.argSchema = argSchema;
  }

  public ApiCommandMetadata(String name) {
    this(name, null);
  }

  public ApiCommandMetadata() {
    // For JAX-B
  }

  /**
   * The name of of the command.
   */
  @XmlElement(name = "name")
  public String getName() { return name; }

  public void setName(String name) { this.name = name; }

  /**
   * The command arguments schema.
   *
   * This is in the form of json schema and
   * describes the structure of the command
   * arguments. If null, the command does not
   * take arguments.
   */
  @XmlElement(name = "argSchema")
  public String getArgSchema() { return argSchema; }

  public void setArgSchema(String argSchema) { this.argSchema = argSchema; }

  public String toString() {
    return Objects.toStringHelper(this)
                  .add("name", name)
                  .add("argSchema", argSchema)
                  .toString();
  }

  public boolean equals(Object o) {
    ApiCommandMetadata other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(name, other.name) &&
        Objects.equal(argSchema, other.argSchema));
  }

  public int hashCode() {
    return Objects.hashCode(name, argSchema);
  }
}
