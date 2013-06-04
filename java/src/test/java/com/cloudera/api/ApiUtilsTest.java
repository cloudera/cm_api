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

import java.io.IOException;
import java.util.Date;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Test;
import static org.junit.Assert.*;

public class ApiUtilsTest {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      ISODateTimeFormat.dateTimeParser();

  private static final ApiObjectMapper MAPPER = new ApiObjectMapper();

  /**
   * Test that date inputs are parsed correctly.
   */
  @Test
  public void testDateTimeInputParsing() throws IOException {
    assertDateConversions("1980-01-23T12:34:56.012Z",
                          "1980-01-23T12:34:56.012Z");
    assertDateConversions("1980-01-23T12:34:56.12Z",
                          "1980-01-23T12:34:56.120Z");
    assertDateConversions("1980-01-23T12:34:56.2Z",
                          "1980-01-23T12:34:56.200Z");
    assertDateConversions("1980-01-23T12:34:56.123+01:00",
                          "1980-01-23T11:34:56.123Z");
    assertDateConversions("1980-01-23T12:34:56Z",
                          "1980-01-23T12:34:56.000Z");
    assertDateConversions("1980-01-23T12:34Z",
                          "1980-01-23T12:34:00.000Z");
  }

  private void assertDateConversions(String inputStr, String isoRepr)
      throws IOException {
    String jsonRepr = String.format("\"%s\"", isoRepr);

    // This is what we usually call on API date input param
    Date input = ApiUtils.newDateFromString(inputStr);
    // This is what CXF calls when serializing to JSON
    String output = MAPPER.writeValueAsString(input);
    assertEquals(jsonRepr, output);
  }

  @Test
  public void testBaseEquals() {
    assertNotNull(ApiUtils.baseEquals(this, new ApiUtilsTest()));
    assertNull(ApiUtils.baseEquals(this, new Object()));
  }

  @Test
  public void testDateTimeConversion() throws IOException {
    // That the time ends in 053 is important. We want this to be
    // converted to HH:MM:SS.053, not HH:MM:SS.53
    long millis = 1334313693053L;

    String dateStr = MAPPER.readValue(
        MAPPER.writeValueAsString(new Date(millis)),
        String.class);
    Date date = ApiUtils.newDateFromString(dateStr);
    assertEquals(millis, date.getTime());
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBadLimit() {
    ApiUtils.checkOffsetAndLimit(0, 0);
  }

  @Test(expected=IllegalArgumentException.class)
  public void testBadOffset() {
    ApiUtils.checkOffsetAndLimit(-1, 1);
  }

}
