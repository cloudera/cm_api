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

__docformat__ = "epytext"


ECHO_PATH = "/tools/echo"
ECHO_ERROR_PATH = "/tools/echoError"

def echo(root_resource, message):
  """Have the server echo our message back."""
  params = dict(message=message)
  return root_resource.get(ECHO_PATH, params)

def echo_error(root_resource, message):
  """Generate an error, but we get to set the error message."""
  params = dict(message=message)
  return root_resource.get(ECHO_ERROR_PATH, params)
