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
package com.cloudera.api;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * View object for errors. We expose the error message and messages of
 * any nested throwables.
 */
@XmlRootElement(name = "error")
@XmlType(propOrder = {"message", "causes"})
public class ApiErrorMessage {

  private String message;
  private List<String> causes;

  /**
   * Empty constructor, for JAX-B.
   */
  public ApiErrorMessage() {
    this.message = null;
    this.causes = null;
  }

  /**
   * Constructs a new ApiErrorMessage that provides information about
   * the given error. Its message and any underlying causes are saved.
   *
   * @param t The error being wrapped.
   */
  public ApiErrorMessage(Throwable t) {
    this.message = t.getMessage();
    List<String> causeMessages = Lists.newLinkedList();
    if (t.getCause() != null) {
      do {
        t = t.getCause();
        String errorMessage = t.getMessage();
        if (errorMessage != null) {
          causeMessages.add(errorMessage);
        }
      } while (t.getCause() != null);
    }
    if (!causeMessages.isEmpty()) {
      causes = causeMessages;
    } else {
      causes = null;
    }
  }

  /**
   * Constructs a new ApiErrorMessage with the given error message, and no
   * causes.
   *
   * @param message The error message.
   */
  public ApiErrorMessage(String message) {
    this.message = message;
    this.causes = null;
  }

  public String toString() {
    return Objects.toStringHelper(this).addValue(message).toString();
  }

  @XmlElement
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @XmlElementWrapper
  public List<String> getCauses() {
    return causes;
  }

  public void setCauses(List<String> causes) {
    this.causes = causes;
  }

}
