//  Copyright (c) 2016 Cloudera, Inc. All rights reserved.
package com.cloudera.api.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cloudera.api.ApiUtils;
import com.google.common.base.Objects;

/**
 * An hive UDF identifier.
 */
@XmlRootElement(name = "hiveUDF")
public class ApiHiveUDF {

  private String database;
  private String signature;

  public ApiHiveUDF() {
    // For JAX-B
  }

  public ApiHiveUDF(String database, String signature) {
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
    ApiHiveUDF that = ApiUtils.baseEquals(this, o);
    return (this == that) || (that != null &&
        Objects.equal(database, that.getDatabase()) &&
        Objects.equal(signature, that.getSignature()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(database, signature);
  }

}
