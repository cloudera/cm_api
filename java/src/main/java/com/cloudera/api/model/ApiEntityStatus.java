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
 * The single value used by the Cloudera Manager UI to represent the status of
 * the entity. It is computed from a variety of other entity-specific states,
 * not all values apply to all entities. For example, STARTING/STOPPING do not
 * apply to a host.
 */
public enum ApiEntityStatus {
  // There is not enough information to determine the entity status.
  UNKNOWN,
  // The entity in question does not have a entity status. For example, it is
  // not something that can be running and it cannot have health.
  NONE,
  // The entity in question is not running, as expected.
  STOPPED,
  // The entity in question is not running, but it is expected to be running.
  DOWN,
  // The entity in question is running, but we do not have enough information
  // to determine its health.
  UNKNOWN_HEALTH,
  // The entity in question is running, but all of its health checks are
  // disabled.
  DISABLED_HEALTH,
  // The entity in question is running with concerning health.
  CONCERNING_HEALTH,
  // The entity in question is running with bad health.
  BAD_HEALTH,
  // The entity in question is running with good health.
  GOOD_HEALTH,
  // The entity in question is starting.
  STARTING,
  // The entity in question is stopping.
  STOPPING,
  // The application is in historical mode, and the entity in question does not
  // have historical monitoring support.
  HISTORY_NOT_AVAILABLE;
}
