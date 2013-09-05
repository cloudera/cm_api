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

import com.google.common.base.Joiner;

/**
 * Enum for different aggregation time periods.
 */
public enum ApiTimeAggregation {
  HOURLY,
  DAILY,
  WEEKLY;

  private static String SUPPORTED_AGGREGATIONS =
      Joiner.on(", ").join(values()).toLowerCase();

  public static ApiTimeAggregation fromString(String s) {
    if (s == null) {
      // Would be illegal anyway. Take care of that below.
      s = "";
    }

    try {
      return valueOf(s.toUpperCase());
    } catch (IllegalArgumentException t) {
      throw new IllegalArgumentException(
          String.format("Illegal time aggregation '%s'. Supported values: %s",
                        s, SUPPORTED_AGGREGATIONS));
    }
  }
}
