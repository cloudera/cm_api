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
import com.google.common.collect.Lists;

// TODO: get rid of the following annotation when we upgrade to Jackson 1.9.
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * A list of commands.
 * <p>
 * This list is returned whenever commands are issued in bulk, and contains a
 * second list with information about errors issuing specific commands.
 */
@XmlRootElement(name = "bulkCommandList")
public class ApiBulkCommandList extends ApiCommandList {

  private List<String> errors;

  public ApiBulkCommandList() {
    this.errors = Lists.newArrayList();
  }

  /** Errors that occurred when issuing individual commands. */
  @XmlElementWrapper(name = "errors")
  @XmlElement(name = "error")
  @JsonProperty(value = "errors")
  public List<String> getErrors() {
    return errors;
  }

  public boolean equals(Object o) {
    if (!super.equals(o)) {
      return false;
    }

    ApiBulkCommandList that = (ApiBulkCommandList) o;
    return Objects.equal(errors, that.getErrors());
  }

  public int hashCode() {
    return 31 * super.hashCode() + Objects.hashCode(errors);
  }

}
