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
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A process represents a unix process to be managed by the Cloudera
 * Manager agents. A process can be a daemon, e.g. if it is associated
 * with a running role. It can also be a one-off process which is expected
 * to start, run and finish.
 */
@XmlRootElement(name="process")
public class ApiProcess {

  private List<String> configFiles;

  public ApiProcess() {
    // For JAX-B
  }
  
  /**
   * List of config files supplied to the process.
   */
  @XmlElement
  public List<String> getConfigFiles() {
    return configFiles;
  }

  public void setConfigFiles(List<String> configFiles) {
    this.configFiles = configFiles;
  }
  
  @Override
  public String toString() {
    return Objects.toStringHelper(this)
              .add("configFiles", configFiles)
              .toString();
  }
  
  @Override
  public int hashCode() {
    return Objects.hashCode(configFiles);
  }
  
  @Override
  public boolean equals(Object o) {
    ApiProcess that = ApiUtils.baseEquals(this, o);
    return this == that || (that != null &&
        Objects.equal(configFiles, that.configFiles));
  }
}
