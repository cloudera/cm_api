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

import org.apache.cxf.common.util.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DataViewTest {

  @Test
  public void testDataView() {
    assertEquals("Unexpected default value for null",
                 DataView.SUMMARY, DataView.fromString(null));
    for (DataView view: DataView.values()) {
      String upperCase = view.toString();
      String lowerCase = upperCase.toLowerCase();
      String mixedCase = StringUtils.capitalize(lowerCase);
      assertEquals("Uppercase value failure",
                   view, DataView.fromString(upperCase));
      assertEquals("Lowercase value failure",
                   view, DataView.fromString(lowerCase));
      assertEquals("Mixedcase value failure",
                   view, DataView.fromString(mixedCase));
    }
    try {
      DataView.fromString("Mississippi");
      fail("Illegal view did not throw an exception");
    } catch (IllegalArgumentException e) {
      // expected
    }
  }
}
