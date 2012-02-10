# Copyright (c) 2011-2012 Cloudera, Inc. All rights reserved.

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
