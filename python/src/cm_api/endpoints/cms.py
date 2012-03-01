# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

try:
  import json
except ImportError:
  import simplejson as json

from cm_api.endpoints.types import config_to_api_list, config_to_json, \
    json_to_config, ApiList, BaseApiObject
from cm_api.endpoints.services import ApiService

class ApiLicense(BaseApiObject):
  """Model for a CM license."""
  RO_ATTR = ('owner', 'uuid', 'expiration')
  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

class ClouderaManager(BaseApiObject):
  """
  The Cloudera Manager instance.

  Provides access to CM configuration and services.
  """

  def __init__(self, resource_root):
    BaseApiObject.ctor_helper(**locals())

  def setup_cms(self, config = None, alert_pub_config = None,
      amon_config = None, event_server_config = None, host_mon_config = None,
      res_manager_config = None, smon_config = None):
    """
    Setup the Cloudera Management Services.

    @param config               Configuration of the management service.
    @param alert_pub_config     Configuration of the alert publisher.
    @param amon_config          Configuration of the activity monitor.
    @param event_server_config  Configuration of the event server.
    @param host_mon_config      Configuration of the host monitor.
    @param res_manager_config   Configuration of the resource manager.
    @param smon_config          Configuration of the service monitor.
    @return The management service instance.
    """
    dic = ApiService(self._get_resource_root(), None, None, None)
    dic = dic.to_json_dict()
    if config:
      dic['config'] = config_to_api_list(config)
    roles = [ ]

    def addRoleConfig(roleType, config):
      if config:
        roles.append({ 'roleType' : roleType,
                       'config' : config_to_api_list(config) })

    addRoleConfig('ACTIVITYMONITOR', amon_config)
    addRoleConfig('ALERTPUBLISHER', alert_pub_config)
    addRoleConfig('EVENTSERVER', event_server_config)
    addRoleConfig('HOSTMONITOR', host_mon_config)
    addRoleConfig('RESOURCEMANAGER', res_manager_config)
    addRoleConfig('SERVICEMONITOR', smon_config)

    if roles:
      dic['roleTypes'] = roles

    resp = self._get_resource_root().put('/cm/service',
        data = json.dumps(dic))
    return ApiService.from_json_dict(resp, self._get_resource_root())

  def get_service(self):
    """
    Return the Cloudera Management Services instance.

    @return An ApiService instance.
    """
    resp = self._get_resource_root().get('/cm/service')
    return ApiService.from_json_dict(resp, self._get_resource_root())

  def get_license(self):
    """
    Return information about the currently installed license.

    @return License information.
    """
    resp = self._get_resource_root().get('/cm/license')
    return ApiLicense.from_json_dict(resp, self._get_resource_root())

  def update_license(self):
    """
    Install a new license.

    TODO: not yet implemented.
    """
    raise NotImplementedError

  def get_config(self, view = None):
    """
    Retrieve the Cloudera Manager configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return Dictionary with configuration data.
    """
    resp = self._get_resource_root().get('/cm/config',
        params = view and dict(view=view) or None)
    return json_to_config(resp, view == 'full')

  def update_config(self, config):
    """
    Update the CM configuration.

    @param config Dictionary with configuration to update.
    @return Dictionary with updated configuration.
    """
    resp = self._get_resource_root().put('/cm/config',
        data = config_to_json(config))
    return json_to_config(resp, False)
