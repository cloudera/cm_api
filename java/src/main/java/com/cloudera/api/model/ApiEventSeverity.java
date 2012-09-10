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

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

import java.util.Arrays;

public enum ApiEventSeverity {
  /**
   * The severity of this event is not known
   */
  UNKNOWN,
  /**
   * This event provides information about a state change.
   */
  INFORMATIONAL,
  /**
   * This event severity should be seen as concerning and may require
   * attention.
   */
  IMPORTANT,
  /**
   * This is the most severe event and signifies an important error
   * that requires immediate attention.
   */
  CRITICAL;

  private static final String VALID_SEVERITIES = Joiner.on(", ").join(
      Collections2.filter(Arrays.asList(values()),
          new Predicate<ApiEventSeverity>() {
            @Override
            public boolean apply(ApiEventSeverity input) {
              return input != UNKNOWN;
            }
      }));

  /**
   * @param severity String value of a severity.
   * @return The severity enum. Note that {@link #UNKNOWN} is not valid.
   */
  public static ApiEventSeverity fromString(String severity) {
    try {
      ApiEventSeverity res = ApiEventSeverity.valueOf(severity.toUpperCase());
      if (res == UNKNOWN) {
        throw new IllegalArgumentException();
      }
      return res;
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(severity +
        " is not a valid event severity. Support severity values: " +
        VALID_SEVERITIES);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Severity cannot be null.");
    }
  }
}
