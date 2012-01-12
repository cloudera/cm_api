# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.

__docformat__ = "epytext"

import cm_api.resource

ECHO_PATH = "/echo"

def echo(client, message):
  """Have the server echo our message back."""
  return cm_api.resource.Resource(ECHO_PATH, client).get(message=message)
