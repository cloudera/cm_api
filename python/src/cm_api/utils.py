# Licensed to Cloudera, Inc. under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  Cloudera, Inc. licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

import datetime

def api_time_to_datetime(s):
  """
  Convert a time string received from the API into a datetime object.
  @param s: A time string received from the API, e.g. "2012-02-18T01:01:03.234Z"
  @return: A datetime object.
  """
  return datetime.datetime.strptime(s, "%Y-%m-%dT%H:%M:%S.%fZ")
