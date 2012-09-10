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

public enum ApiEventCategory {
  /**
   * The category of this event is unknown
   */
  UNKNOWN,
  /**
   * This event was generate during a health check
   */
  HEALTH_EVENT,
  /**
   * This event was sent because a specific log message was detected
   */
  LOG_EVENT,
  /**
   * This event was caused by an audit event e.g. service restart,
   * role reconfiguration, etc
   */
  AUDIT_EVENT,
  /**
   * This event was triggered by an activity event
   * e.g. activity entered shuffle phase, stopped, preempted, etc
   */
  ACTIVITY_EVENT;

  private static final String VALID_CATS = Joiner.on(", ").join(
      Collections2.filter(Arrays.<ApiEventCategory>asList(values()),
          new Predicate<ApiEventCategory>() {
            @Override
            public boolean apply(ApiEventCategory input) {
              return input != UNKNOWN;
            }
      }));

  /**
   * @param category String value of a category.
   * @return The category enum. Note that {@link #UNKNOWN} is not valid.
   */
  public static ApiEventCategory fromString(String category) {
    try {
      ApiEventCategory res = ApiEventCategory.valueOf(category.toUpperCase());
      if (res == UNKNOWN) {
        throw new IllegalArgumentException();
      }
      return res;
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(category +
          " is not a valid event category. Supported category values: " +
          VALID_CATS);
    } catch (NullPointerException e) {
      throw new IllegalArgumentException("Category cannot be null.");
    }
  }
}
