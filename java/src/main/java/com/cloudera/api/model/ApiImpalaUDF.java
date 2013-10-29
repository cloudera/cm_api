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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * An impala UDF identifier.
 */
@XmlRootElement(name = "impalaUDF")
public class ApiImpalaUDF {

  private String database;
  private String signature;

  public ApiImpalaUDF() {
    // For JAX-B
  }

  public ApiImpalaUDF(String database, String signature) {
    this.database = database;
    this.signature = signature;
  }

  /** Name of the database to which this UDF belongs. */
  @XmlElement
  public String getDatabase() {
    return database;
  }

  public void setDatabase(String database) {
    this.database = database;
  }

  /** UDF signature, includes the UDF name and parameter types. */
  @XmlElement
  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("database", database)
        .add("signature", signature)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiImpalaUDF that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(database, that.getDatabase()) &&
        Objects.equal(signature, that.getSignature()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(database, signature);
  }

}
