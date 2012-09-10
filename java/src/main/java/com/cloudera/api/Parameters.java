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

/**
 * Constants defining the common parameter names used in API methods.
 */
public final class Parameters {

  /* Parameter names. */
  static public final String CLUSTER_NAME = "clusterName";
  static public final String DATA_VIEW = "view";
  static public final String HOST_ID = "hostId";
  static public final String MESSAGE = "message";
  static public final String NAMESERVICE_NAME = "nameservice";
  static public final String ROLE_NAME = "roleName";
  static public final String SERVICE_NAME = "serviceName";
  static public final String USER_NAME = "userName";
  static public final String ACTIVITY_ID = "activityId";
  static public final String PEER_NAME = "peerName";

  /* Default values. */
  static public final String DATA_VIEW_DEFAULT = "summary";

  private Parameters() { }

}
