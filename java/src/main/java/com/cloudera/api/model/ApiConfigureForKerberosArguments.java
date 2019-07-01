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
 * Arguments used to configure a cluster for Kerberos.
 */
@XmlRootElement(name="configureForKerberosArgs")
public class ApiConfigureForKerberosArguments {
  private Long datanodeTransceiverPort;
  private Long datanodeWebPort;

  /**
   * The HDFS DataNode transceiver port to use. This will be applied to all DataNode
   * role configuration groups. If not specified, this will default to 1004.
   */
  @XmlElement
  public Long getDatanodeTransceiverPort() {
    return datanodeTransceiverPort;
  }

  public void setDatanodeTransceiverPort(Long datanodeTransceiverPort) {
    this.datanodeTransceiverPort = datanodeTransceiverPort;
  }

  /**
   * The HDFS DataNode web port to use.  This will be applied to all DataNode
   * role configuration groups. If not specified, this will default to 1006.
   */
  @XmlElement
  public Long getDatanodeWebPort() {
    return datanodeWebPort;
  }

  public void setDatanodeWebPort(Long datanodeWebPort) {
    this.datanodeWebPort = datanodeWebPort;
  }

  @Override
  public String toString() {
    return Objects.toStringHelper(this)
        .add("datanodeTransceiverPort", datanodeTransceiverPort)
        .add("datanodeWebPort", datanodeWebPort)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    ApiConfigureForKerberosArguments other = ApiUtils.baseEquals(this, o);
    return this == other || (other != null &&
        Objects.equal(this.datanodeTransceiverPort, other.datanodeTransceiverPort) &&
        Objects.equal(this.datanodeWebPort, other.datanodeWebPort));
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(datanodeTransceiverPort, datanodeWebPort);
  }
}
