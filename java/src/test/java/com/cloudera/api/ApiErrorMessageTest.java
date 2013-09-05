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

import com.cloudera.api.model.ApiModelTest;
import org.junit.Test;

import java.io.IOException;

public class ApiErrorMessageTest {

  @Test
  public void testCauseWithNoMessage() throws IOException {
    Throwable causeWithNoMessage = new Throwable();
    Throwable t = new Throwable("To err is human...", causeWithNoMessage);
    ApiErrorMessage message = new ApiErrorMessage(t);
    // Make sure there is no "causes" only "message"
    ApiModelTest.checkJsonProperties(message, "message");
  }

  @Test
  public void testCauseWithMessage() throws IOException {
    Throwable causeWithMessage = new Throwable(".. to forgive divine.");
    Throwable t = new Throwable("To err is human...", causeWithMessage);
    ApiErrorMessage message = new ApiErrorMessage(t);
    ApiModelTest.checkJsonProperties(message, "message", "causes");
  }

}
