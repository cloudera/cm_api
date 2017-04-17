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
  static public final String PEER_TYPE = "type";
  static public final String SCHEDULE_ID = "scheduleId";
  static public final String PRODUCT = "product";
  static public final String VERSION = "version";
  static public final String ROLE_CONFIG_GROUP_NAME = "roleConfigGroupName";
  static public final String HOST_TEMPLATE_NAME = "hostTemplateName";
  static public final String CONFIG_FILE_NAME = "configFileName";
  static public final String COMMAND_ID = "commandId";
  static public final String POLICY_NAME = "policyName";
  static public final String PATH = "path";
  static public final String CONTENT_TYPE = "contentType";
  static public final String DESIRED_ROLLUP = "desiredRollup";
  static public final String MUST_USE_DESIRED_ROLLUP = "mustUseDesiredRollup";
  static public final String FILTER = "filter";
  static public final String COMMAND_NAME = "commandName";
  static public final String DIR_PATH = "directoryPath";
  static public final String EXTERNAL_ACCOUNT_NAME = "name";
  static public final String EXTERNAL_ACCOUNT_TYPE_NAME = "typeName";
  static public final String EXTERNAL_ACCOUNT_CATEGORY_NAME = "categoryName";
  static public final String DISPLAY_NAME = "displayName";

  /* Common query params. */
  static public final String FROM = "from";
  static public final String TO = "to";
  static public final String OFFSET = "offset";
  static public final String LIMIT = "limit";
  static public final String METRICS = "metrics";
  static public final String QUERY = "query";

  /* Default values. */
  static public final String DATA_VIEW_DEFAULT = "summary";
  static public final String DATA_VIEW_EXPORT = "export";
  static public final String DATA_VIEW_FULL = "full";
  static public final String DATE_TIME_NOW = "now";
  static public final String DAILY_AGGREGATION = "daily";
  static public final String FILTER_DEFAULT = "";
  static public final String ROLLUP_DEFAULT = "RAW";

  /* Common filtering properties. */
  static public final String HOSTNAME = "hostname";
  static public final String ROLE_TYPE = "type";

  private Parameters() { }

}
