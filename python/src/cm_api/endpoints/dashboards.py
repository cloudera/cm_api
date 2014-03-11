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

from cm_api.endpoints.types import *

__docformat__ = "epytext"

DASHBOARDS_PATH = "/timeseries/dashboards"

def _get_dashboard_path(dashboard_name):
  return DASHBOARDS_PATH + "/%s" % (dashboard_name)

def create_dashboards(resource_root, dashboard_list):
  """
  Creates the list of dashboards. If any of the dashboards already exist
  this whole command will fail and no dashboards will be created.
  @since: API v6
  @return: The list of dashboards created.
  """
  return call(resource_root.post, DASHBOARDS_PATH, ApiDashboard, \
      ret_is_list=True, data=dashboard_list)

def get_dashboards(resource_root):
  """
  Returns the list of all dashboards.
  @since: API v6
  @return: A list of API dashboard objects.
  """
  return call(resource_root.get, DASHBOARDS_PATH, ApiDashboard, \
     ret_is_list=True)

def get_dashboard(resource_root, dashboard_name):
  """
  Returns a dashboard definition for the specified name. This dashboard
  can be imported with the createDashboards API.
  @since: API v6
  @return: An API dasbhboard object.
  """
  return call(resource_root.get, _get_dashboard_path(dashboard_name), \
      ApiDashboard)

def delete_dashboard(resource_root, dashboard_name):
  """
  Deletes a dashboard.
  @since: API v6
  @return: The deleted dashboard.
  """
  return call(resource_root.delete, _get_dashboard_path(dashboard_name), \
      ApiDashboard)

class ApiDashboard(BaseApiResource):
  _ATTRIBUTES = {
    'name'  : None,
    'json'  : None
  }

  def __init__(self, resource_root, name=None, json=None):
    BaseApiObject.init(self, resource_root, locals())

  def __str__(self):
    return "<ApiDashboard>: %s" % (self.name)

  def _path(self):
    return _get_dashboard_path(self.name)
