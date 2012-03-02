# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

from cm_api.endpoints.types import config_to_json, json_to_config


CONFIG_PATH = "/clusters/%s/services/%s/roleTypes/%s/config"


class ApiRoleType(object):
  """
  The role type object allows you to set configuration at the role type level.
  """
  def __init__(self, resource_root, service, role_type):
    self._service = service
    self._role_type = role_type
    self._resource_root = resource_root

  def _get_resource_root(self):
    return self._resource_root

  @property
  def _config_path(self):
    return CONFIG_PATH % (self._service.clusterRef.clusterName,
                          self._service.name,
                          self._role_type)

  def get_config(self, view=None):
    """
    Retrieve the role type's configuration.

    The 'summary' view contains strings as the dictionary values. The full
    view contains ApiConfig instances as the values.

    @param view: View to materialize ('full' or 'summary')
    @return Dictionary with configuration data.
    """
    params = view and dict(view=view) or None
    resp = self._get_resource_root().get(self._config_path,
                                         params=params)
    return json_to_config(resp)

  def update_config(self, config):
    """
    Update the role's configuration.

    @param config Dictionary with configuration to update.
    @return Dictionary with updated configuration.
    """
    resp = self._get_resource_root().put(self._config_path,
                                         data=config_to_json(config))
    return json_to_config(resp)
