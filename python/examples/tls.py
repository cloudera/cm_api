#!/usr/bin/env python
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

"""
Lists all hosts managed by CM. Demonstrates connecting to CM via HTTPS using a
custom CA certificate in PEM format. Requires Python 2.7.9 or higher.

Usage: %s hostname username password ca_cert_file [options]

Options:
-h    Displays usage
"""

import getopt
import inspect
import logging
import ssl
import sys
import textwrap

from cm_api.api_client import ApiResource

def list_hosts(host, username, password, cafile):
  context = ssl.create_default_context(cafile=cafile)

  api = ApiResource(host, username=username, password=password, use_tls=True,
                    ssl_context=context)

  for h in api.get_all_hosts():
    print h.hostname

def usage():
  doc = inspect.getmodule(usage).__doc__
  print >>sys.stderr, textwrap.dedent(doc % (sys.argv[0],))

def setup_logging(level):
  logging.basicConfig()
  logging.getLogger().setLevel(level)

def main(argv):
  setup_logging(logging.INFO)

  # Argument parsing
  try:
    opts, args = getopt.getopt(argv[1:], "h")
  except getopt.GetoptError, err:
    print >>sys.stderr, err
    usage()
    return -1

  for option, val in opts:
    if option == '-h':
      usage()
      return -1
    else:
      print >>sys.stderr, "Unknown flag:", option
      usage()
      return -1

  if len(args) < 4:
    print >>sys.stderr, "Invalid number of arguments"
    usage()
    return -1

  # Do work
  list_hosts(*args)
  return 0


#
# The "main" entry
#
if __name__ == '__main__':
  sys.exit(main(sys.argv))
