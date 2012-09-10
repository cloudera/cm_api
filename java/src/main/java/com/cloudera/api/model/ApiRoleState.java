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

// Modeled after RoleState

/**
 * Represents the configured run state of a role.
 */
public enum ApiRoleState {
  /** The role's run state was not captured at this past instant */
  HISTORY_NOT_AVAILABLE,
  /** The role's run state cannot be determined */
  UNKNOWN,
  /** The role is starting */
  STARTING,
  /** The role has started */
  STARTED,
  /** The role is executing a command */
  BUSY,
  /** The role is stopping */
  STOPPING,
  /** The role has stopped */
  STOPPED
}
