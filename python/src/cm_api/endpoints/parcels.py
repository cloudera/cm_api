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

PARCELS_PATH = "/clusters/%s/parcels"
PARCEL_PATH = "/clusters/%s/parcels/products/%s/versions/%s"

def get_parcel(resource_root, product, version, cluster_name="default"):
  """
  Lookup a parcel by name
  @param resource_root: The root Resource object.
  @param product: Parcel product name
  @param version: Parcel version
  @param cluster_name: Cluster name
  @return: An ApiService object
  """
  return _get_parcel(resource_root, PARCEL_PATH % (cluster_name, product, version))

def _get_parcel(resource_root, path):
  return call(resource_root.get, path, ApiParcel, api_version=3)

def get_all_parcels(resource_root, cluster_name="default", view=None):
  """
  Get all parcels
  @param resource_root: The root Resource object.
  @param cluster_name: Cluster name
  @return: A list of ApiParcel objects.
  @since: API v3
  """
  return call(resource_root.get, PARCELS_PATH % (cluster_name,),
      ApiParcel, True, params=view and dict(view=view) or None, api_version=3)

class ApiParcelState(BaseApiObject):
  """
  An object that represents the state of a parcel.
  """
  _ATTRIBUTES = {
      'progress'      : ROAttr(),
      'totalProgress' : ROAttr(),
      'count'         : ROAttr(),
      'totalCount'    : ROAttr(),
      'warnings'      : ROAttr(),
      'errors'        : ROAttr(),
    }

  def __init__(self, resource_root):
    BaseApiObject.init(self, resource_root)

  def __str__(self):
    return "<ApiParcelState>: (progress: %s) (totalProgress: %s) (count: %s) (totalCount: %s)" % (
        self.progress, self.totalProgress, self.count, self.totalCount)

class ApiParcel(BaseApiResource):
  """
  An object that represents a parcel and allows administrative operations.

  @since: API v3
  """
  _ATTRIBUTES = {
    'product'     : ROAttr(),
    'version'     : ROAttr(),
    'stage'       : ROAttr(),
    'state'       : ROAttr(ApiParcelState),
    'clusterRef'  : ROAttr(ApiClusterRef),
  }

  def __init__(self, resource_root):
    BaseApiObject.init(self, resource_root)

  def __str__(self):
    return "<ApiParcel>: %s-%s (stage: %s) (state: %s) (cluster: %s)" % (
        self.product, self.version, self.stage, self.state, self._get_cluster_name())

  def _api_version(self):
    return 3

  def _path(self):
    """
    Return the API path for this service.
    """
    return PARCEL_PATH % (self._get_cluster_name(), self.product, self.version)

  def _get_cluster_name(self):
    if self.clusterRef:
      return self.clusterRef.clusterName
    return None

  def start_download(self):
    """
    Start the download of the parcel

    @return: Reference to the completed command.
    """
    return self._cmd('startDownload')

  def cancel_download(self):
    """
    Cancels the parcel download. If the parcel is not
    currently downloading an exception is raised.

    @return: Reference to the completed command.
    """
    return self._cmd('cancelDownload')

  def remove_download(self):
    """
    Removes the downloaded parcel

    @return: Reference to the completed command.
    """
    return self._cmd('removeDownload')

  def start_distribution(self):
    """
    Start the distribution of the parcel to all hosts
    in the cluster.

    @return: Reference to the completed command.
    """
    return self._cmd('startDistribution')

  def cancel_distribution(self):
    """
    Cancels the parcel distrubution. If the parcel is not
    currently distributing an exception is raised.

    @return: Reference to the completed command
    """
    return self._cmd('cancelDistribution')

  def start_removal_of_distribution(self):
    """
    Start the removal of the distribution of the parcel
    from all the hosts in the cluster.

    @return: Reference to the completed command.
    """
    return self._cmd('startRemovalOfDistribution')

  def activate(self):
    """
    Activate the parcel on all the hosts in the cluster.

    @return: Reference to the completed command.
    """
    return self._cmd('activate')

  def deactivate(self):
    """
    Deactivates the parcel on all the hosts in the cluster.

    @return: Reference to the completed command.
    """
    return self._cmd('deactivate')

