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
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public enum ApiActivityStatus {
  /**
   * The status of this activity is not known
   */
  UNKNOWN,
  /**
   * This activity has been submitted but not started
   */
  SUBMITTED,
  /**
   * This activity is currently running
   */
  STARTED,
  /**
   * This activity was started but is currently suspended
   */
  SUSPENDED,
  /**
   * This activity has failed
   */
  FAILED,
  /**
   * This activity has been killed
   */
  KILLED,
  /**
   * This activity succeeded.
   */
  SUCCEEDED,
  
  /**
   * This activity was assumed to finish successfully.
   */
  ASSUMED_SUCCEEDED;

  private static String VALID_STATUS_VALUES = Joiner.on(", ").join(
      Iterables.filter(Lists.newArrayList(values()),
                       new Predicate<ApiActivityStatus>() {
                         @Override
                         public boolean apply(ApiActivityStatus input) {
                           return input != UNKNOWN;
                         }
                       }));

  /**
   * @param s String representation of a status.
   *          Note that UNKNOWN is not a valid input.
   * @return ApiActivityStatus
   */
  public static ApiActivityStatus fromString(String s) {
    if (s == null) {
      return null;
    }
    try {
      ApiActivityStatus res = valueOf(s.toUpperCase());
      if (res == UNKNOWN) {
        throw new IllegalArgumentException();
      }
      return res;
    } catch (IllegalArgumentException t) {
      throw new IllegalArgumentException(
          String.format("Illegal status='%s'. Supported status values: %s",
                        s, VALID_STATUS_VALUES));
    }
  }
}
