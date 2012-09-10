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

public class DateTimeUtil {
  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      ISODateTimeFormat.dateTimeParser();
  private static final PeriodFormatter PERIOD_FORMATTER =
      ISOPeriodFormat.standard();
  public static final String NOW_KEYWORD = "now";

  public static Instant newInstantFromString(String value) {
    if (value.equalsIgnoreCase(NOW_KEYWORD)) {
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

  private DateTimeUtil() { }

}

