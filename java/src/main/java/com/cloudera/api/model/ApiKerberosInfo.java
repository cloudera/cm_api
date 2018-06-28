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

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Kerberos information of a Cluster or Cloudera Manager.
 */
@XmlRootElement(name = "kerberosInfo")
public class ApiKerberosInfo {

  private boolean isKerberized;
  private String kdcType;
  private String kerberosRealm;
  private String kdcHost;
  private String adminHost;
  private List<String> domain;

  public ApiKerberosInfo() {
    /* JAX-B */
  }

  public ApiKerberosInfo(boolean isKerberized) {
    this.isKerberized = isKerberized;
  }

  /*
   * Whether the cluster is configured with Kerberos
   */
  @XmlElement
  public boolean isKerberized() {
    return isKerberized;
  }

  public void setKerberized(boolean isKerberized) {
    this.isKerberized = isKerberized;
  }

  /*
   * KDC Type (MIT/AD Kerberos), it will be null if the cluster is not
   * configured with Kerberos
   */
  @XmlElement
  public String getKdcType() {
    return kdcType;
  }

  public void setKdcType(String kdcType) {
    this.kdcType = kdcType;
  }

  /*
   * Kerberos Realm, it will be null if the cluster is not configured with
   * Kerberos
   */
  @XmlElement
  public String getKerberosRealm() {
    return kerberosRealm;
  }

  public void setKerberosRealm(String kerberosRealm) {
    this.kerberosRealm = kerberosRealm;
  }

  /*
   * Kerberos KDC Host, it will be null if the cluster is not configured with
   * Kerberos
   */
  @XmlElement
  public String getKdcHost() {
    return kdcHost;
  }

  public void setKdcHost(String kdcHost) {
    this.kdcHost = kdcHost;
  }

  /*
   * Kerberos Admin Server Host, it will be null if the cluster is not configured with
   * Kerberos
   */
  @XmlElement
  public String getAdminHost() {
    return adminHost;
  }

  public void setAdminHost(String adminHost) {
    this.adminHost = adminHost;
  }

  /*
   * Kerberos Domain, it will be null if the cluster is not configured with
   * Kerberos
   */
  @XmlElement
  public List<String> getDomain() {
    return domain;
  }

  public void setDomain(List<String> domain) {
    this.domain = domain;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("isKerberized", isKerberized)
        .add("kdcType", kdcType)
        .add("kerberosRealm", kerberosRealm)
        .add("kdcHost", kdcHost)
        .add("adminHost", adminHost)
        .toString();
  }
}
