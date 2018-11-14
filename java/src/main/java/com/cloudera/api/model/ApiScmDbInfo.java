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

/**
 * Cloudera Manager server's database information
 */
@XmlRootElement(name = "scmDbInfo")
public class ApiScmDbInfo {

  /**
   * Enum for Cloudera Manager DB type.
   * Note that DERBY and SQLITE3 are not supported DBs
   */
  public static enum ScmDbType {
    /** mysql db */
    MYSQL("mysql"),
    /** postgresql db */
    POSTGRESQL("postgresql"),
    /** hsql db, in memory version */
    HSQL("mem"),
    /** oracle db */
    ORACLE("oracle"),
    /** derby db, not supported */
    DERBY("derby"),
    /** sqlite3 db, not supported */
    SQLITE3("sqlite3");

    private String name;

    ScmDbType(String name) {
      this.name = name;
    }

    public static ScmDbType convert(String str) {
      for (ScmDbType scmDbType : ScmDbType.values()) {
        if (scmDbType.name.equals(str)) {
          return scmDbType;
        }
      }
      return null;
    }
  }

  private boolean scmUsingEmbeddedDb;
  private ScmDbType scmDbType;

  public ApiScmDbInfo() {
  }

  /** Cloudera Manager server's db type */
  @XmlElement
  public ScmDbType getScmDbType() {
    return scmDbType;
  }

  public void setScmDbType(ScmDbType scmDbType) {
    this.scmDbType = scmDbType;
  }

  /** Whether Cloudera Manager server is using embedded DB */
  @XmlElement
  public boolean getEmbeddedDbUsed() {
    return scmUsingEmbeddedDb;
  }

  public void setEmbeddedDbUsed(boolean scmUsingEmbeddedDb) {
    this.scmUsingEmbeddedDb = scmUsingEmbeddedDb;
  }

}