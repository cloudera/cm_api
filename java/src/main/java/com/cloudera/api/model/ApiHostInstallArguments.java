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
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Arguments to perform installation on one
 * or more hosts
 */
@XmlRootElement(name = "hostInstallArgs")
public class ApiHostInstallArguments {
  private List<String> hostNames;
  private Integer sshPort;
  private String userName;
  private String password;
  private String privateKey;
  private String passphrase;
  private Integer parallelInstallCount;
  private String cmRepoUrl;
  private String gpgKeyCustomUrl;

  public ApiHostInstallArguments() {
    // For JAX-B
  }

  public String toString() {
    return Objects.toStringHelper(this)
        .add("hostNames", hostNames)
        .add("sshPort", sshPort)
        .add("userName", userName)
        .add("password", password)
        .add("privateKey", privateKey)
        .add("passphrase", passphrase)
        .add("parallelInstallCount", parallelInstallCount)
        .add("cmRepoUrl", cmRepoUrl)
        .add("gpgKeyCustomUrl", gpgKeyCustomUrl)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiHostInstallArguments that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(hostNames, that.getHostNames()) &&
        Objects.equal(sshPort, that.getSshPort()) &&
        Objects.equal(userName, that.getUserName()) &&
        Objects.equal(password, that.getPassword()) &&
        Objects.equal(privateKey, that.getPrivateKey()) &&
        Objects.equal(passphrase, that.getPassphrase()) &&
        Objects.equal(parallelInstallCount, that.getParallelInstallCount()) &&
        Objects.equal(cmRepoUrl, that.getCmRepoUrl()) &&
        Objects.equal(gpgKeyCustomUrl, that.getGpgKeyCustomUrl()));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(hostNames, userName, password,
        privateKey, passphrase, gpgKeyCustomUrl);
  }

  /**
   * List of hosts to configure for use with Cloudera Manager.
   * A host may be specified by a hostname (FQDN) or an
   * IP address.
   */
  @XmlElementWrapper(name = "hostNames")
  public List<String> getHostNames() {
    return hostNames;
  }

  public void setHostNames(List<String> hostNames) {
    this.hostNames = hostNames;
  }

  /**
   * SSH port. If unset, defaults to 22.
   */
  @XmlElement
  public Integer getSshPort() {
    return sshPort;
  }

  public void setSshPort(Integer sshPort) {
    this.sshPort = sshPort;
  }

  /**
   * The username used to authenticate with the hosts.
   * Root access to your hosts is required to install Cloudera packages.
   * The installer will connect to your hosts via SSH and log in either
   * directly as root or as another user with password-less sudo
   * privileges to become root.
   */
  @XmlElement
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  /**
   * The password used to authenticate with the hosts.
   * Specify either this or a private key.
   * For password-less login, use an empty string as
   * password.
   */
  @XmlElement
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * The private key to authenticate with the hosts.
   * Specify either this or a password.
   */
  @XmlElement
  public String getPrivateKey() {
    return privateKey;
  }

  public void setPrivateKey(String privateKey) {
    this.privateKey = privateKey;
  }

  /**
   * The passphrase associated with the private key
   * used to authenticate with the hosts (optional).
   */
  @XmlElement
  public String getPassphrase() {
    return passphrase;
  }

  public void setPassphrase(String passphrase) {
    this.passphrase = passphrase;
  }

  /**
   * Number of simultaneous installations.
   * Defaults to 10. Running a large number of
   * installations at once can consume large
   * amounts of network bandwidth and other system
   * resources.
   */
  @XmlElement
  public Integer getParallelInstallCount() {
    return parallelInstallCount;
  }

  public void setParallelInstallCount(Integer parallelInstallCount) {
    this.parallelInstallCount = parallelInstallCount;
  }

  /**
   * The Cloudera Manager repository URL to use (optional).
   * Example for SLES, Redhat or other RPM based distributions:
   * http://archive.cloudera.com/cdh4/redhat/5/x86_64/cdh/4/
   * Example for Ubuntu or other Debian based distributions:
   * "deb http://archive.cloudera.com/cdh4/ubuntu/lucid/amd64/cdh/ lucid-cdh4 contrib"
   */
  @XmlElement
  public String getCmRepoUrl() {
    return cmRepoUrl;
  }

  public void setCmRepoUrl(String cmRepoUrl) {
    this.cmRepoUrl = cmRepoUrl;
  }

  /**
   * The Cloudera Manager public GPG key (optional).
   * Example for SLES, Redhat or other RPM based distributions:
   * http://archive.cloudera.com/redhat/cdh/RPM-GPG-KEY-cloudera
   * Example for Ubuntu or other Debian based distributions:
   * http://archive.cloudera.com/debian/archive.key
   */
  @XmlElement
  public String getGpgKeyCustomUrl() {
    return gpgKeyCustomUrl;
  }

  public void setGpgKeyCustomUrl(String gpgKeyCustomUrl) {
    this.gpgKeyCustomUrl = gpgKeyCustomUrl;
  }
}