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

try:
  import json
except ImportError:
  import simplejson as json

def deserialize(raw_data, cls):
  """
  Deserializes raw JSON data into an instance of cls.

  The data is deserialized, serialized again using the class's to_json_dict()
  implementation, and deserialized again, to make sure both from_json_dict()
  and to_json_dict() are working.
  """
  instance = cls.from_json_dict(json.loads(raw_data), None)
  return cls.from_json_dict(instance.to_json_dict(), None)
