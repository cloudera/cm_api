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

/**
 * This class encapsulates the data limits that we want to put on ApiReplicationSchedule to
 * avoid potential OOME. It is currently used when archiving replication history.
 * Available since v11.
 */
public class ApiReplicationScheduleDataLimits {
  private final int maxCommandsPerSchedule;
  private final int maxTablesPerResult;
  private final int maxErrorsPerResult;

  public ApiReplicationScheduleDataLimits(int maxCommandsPerSchedule, int maxTablesPerResult,
      int maxErrorsPerResult) {
    this.maxCommandsPerSchedule = maxCommandsPerSchedule;
    this.maxTablesPerResult = maxTablesPerResult;
    this.maxErrorsPerResult = maxErrorsPerResult;
  }

  /**
   * @return Max number of ApiReplicationCommands per ApiReplicationSchedule.
   */
  public int getMaxCommandsPerSchedule() {
    return maxCommandsPerSchedule;
  }

  /**
   * @return Max number of ApiHiveTable per ApiHiveReplicationResult.
   */
  public int getMaxTablesPerResult() {
    return maxTablesPerResult;
  }

  /**
   * @return Max number of ApiHiveReplicationError per ApiHiveReplicationResult.
   */
  public int getMaxErrorsPerResult() {
    return maxErrorsPerResult;
  }
}
