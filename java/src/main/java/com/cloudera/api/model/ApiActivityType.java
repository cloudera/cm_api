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

public enum ApiActivityType {
  /**
   * The activity type is unknown
   */
  UNKNOWN,
  /**
   * The corresponding activity is an Oozie workflow
   */
  OOZIE,
  /**
   * The corresponding activity is a Pig job
   */
  PIG,
  /**
   * The corresponding activity a Hive job
   */
  HIVE,
  /**
   * The corresponding activity is a Map-Reduce job
   */
  MR,
  /**
   * The corresponding activity is a Hadoop streaming job
   */
  STREAMING;

  private static String VALID_VALUES = Joiner.on(", ").join(
      Iterables.filter(Lists.newArrayList(values()),
                       new Predicate<ApiActivityType>() {
                         @Override
                         public boolean apply(ApiActivityType input) {
                           return input != UNKNOWN;
                         }
                       }));

  /**
   * @param str String representation of an activity type.
   *            Note that UNKNOWN is not a valid input.
   * @return ApiActivityType
   */
  public static ApiActivityType fromString(String str) {
    if (str == null) {
      return null;
    }
    try {
      ApiActivityType res = valueOf(str.toUpperCase());
      if (res == UNKNOWN) {
        throw new IllegalArgumentException();
      }
      return res;
    } catch (IllegalArgumentException iae) {
      throw new IllegalArgumentException(
          String.format("Illegal type='%s'. Supported type values: %s",
                        str, VALID_VALUES));
    }

  }
}
