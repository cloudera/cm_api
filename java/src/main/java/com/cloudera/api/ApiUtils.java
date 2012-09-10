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

import org.joda.time.Instant;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;

import java.util.Date;

/**
 * A collection of utility methods used by API code.
 */
public final class ApiUtils {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      ISODateTimeFormat.dateTimeParser();
  private static final PeriodFormatter PERIOD_FORMATTER =
      ISOPeriodFormat.standard();

  public static Instant newInstantFromString(String value) {
    if (value.equalsIgnoreCase(Parameters.DATE_TIME_NOW)) {
      return new Instant();
    }
    return new Instant(DATE_TIME_FORMATTER.parseMillis(value));
  }

  public static Date newDateFromString(String value) {
    return new Date(newInstantFromString(value).getMillis());
  }

  public static Period newPeriodFromString(String value) {
    return PERIOD_FORMATTER.parsePeriod(value);
  }

  /**
   * Checks whether the <i>other</i> object has the same class as the given
   * object, and casts it to the given object's type.
   * <p>
   * The pattern for using this in an implementation of equals() would be:
   * <blockquote><tt>
   *   MyType that = ApiUtils.baseEquals(this, other);
   *   return this == that || (that != null &&
   *       Objects.equal(...) &&
   *       Objects.equal(...));
   * </tt></blockquote>
   *
   * @param object The object being compared.
   * @param other The object being compared against.
   * @return <i>other</i>, if it's not null and has the same class as
   *         <i>object</i>; null otherwise.
   */
  public static <T> T baseEquals(T object, Object other) {
    if (object == other ||
        (other != null && object.getClass() == other.getClass())) {
      return (T) other;
    }
    return null;
  }

  private ApiUtils() { }

}

