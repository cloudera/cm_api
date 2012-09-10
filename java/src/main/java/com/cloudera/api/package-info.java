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

/**
 * <p>
 * This document describes the Cloudera Manager REST API.
 * All requests and responses are presented in Javascript
 * Object Notation (JSON).
 * </p>
 * <p>
 * The API resources listed below follow standard
 * Create-Read-Update-Delete (CRUD) semantics where the HTTP request
 * path defines the entity to be acted on and the HTTP method expresses the
 * type of action to perform.
 * </p>
 * <table>
 *   <thead>
 *     <tr><th>HTTP Method</th><th>Operation</th></tr>
 *   </thead>
 *   <tbody>
 *     <tr><td>POST</td><td>Create entries</td></tr>
 *     <tr><td>GET</td><td>Read entries</td></tr>
 *     <tr><td>PUT</td><td>Update or edit entries</td></td></tr>
 *     <tr><td>DELETE</td><td>Delete entries</td></td></tr>
 *   </tbody>
 * </table>
 * <p>
 * All collections in the API use plural names, 'users', instead of
 * the singular, 'user'.  To address a specific user in the system,
 * expand the URL path to include the user identifier.  For example,
 * '/users/foo' identifies user 'foo' and '/users/bar' identifies user 'bar'.
 * </p>
 * <table>
 *   <thead>
 *   <tr>
 *     <th>Collection</th>
 *     <th>POST (create)</th>
 *     <th>GET (read)</th>
 *     <th>PUT (update)</th>
 *     <th>DELETE (delete)</th>
 *   </tr>
 *   </thead>
 *   <tbody>
 *   <tr>
 *     <td>/users</td>
 *     <td>Create a new user</td>
 *     <td>List all users in the system</td>
 *     <td>Bulk update all users</td>
 *     <td>Delete all users</td>
 *   </tr>
 *   <tr>
 *     <td>/users/foo</td>
 *     <td>error</td>
 *     <td>Read information about user 'foo'</td>
 *     <td>If user 'foo' exists, update their information; otherwise, error.</td>
 *     <td>Delete user 'foo'</td>
 *   </tr>
 *   </tbody>
 * </table>
 * <p>
 * Keep in mind that not all collections supports all operations.  For example,
 * events in the system are read-only; you cannot create new events with
 * the API.
 * </p>
 * <p>
 * You can list the entries in a collection using one of two views: 'summary'
 * or 'full'.  The default 'summary' view provides the core information about
 * each entry.  The 'full' view is more heavyweight and provides a fully
 * expanded view of each entry.  The view is controlled by a query parameter
 * called 'view' e.g. 'GET /users?view=full'.
 * </p>
 */
package com.cloudera.api;
