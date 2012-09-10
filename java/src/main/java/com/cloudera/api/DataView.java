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

public enum DataView {
  SUMMARY,
  FULL,
  EXPORT,
  /** All passwords and other sensitive fields are marked as REDACTED. */
  EXPORT_REDACTED;

  // List of supported views
  private static String SUPPORTED_VIEWS =
      Joiner.on(", ").join(values()).toLowerCase();

  public static DataView fromString(String s) {
    if (s == null || s.isEmpty()) {
      return SUMMARY;
    }
    try {
      return valueOf(s.toUpperCase());
    } catch (IllegalArgumentException t) {
      throw new IllegalArgumentException(
          String.format("Illegal view='%s'. Supported views: %s",
                        s, SUPPORTED_VIEWS));
    }
  }
}
