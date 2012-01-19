# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.

__docformat__ = "epytext"

import cm_api.resource

ECHO_PATH = "/tools/echo"
ECHO_ERROR_PATH = "/tools/echoError"

def echo(client, message):
  """Have the server echo our message back."""
  return cm_api.resource.Resource(ECHO_PATH, client).get(message=message)

def echo_error(client, message):
  """Generate an error, but we get to set the error message."""
  return cm_api.resource.Resource(ECHO_ERROR_PATH, client).get(message=message)
