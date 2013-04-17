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
Given an alias_name, show all metrics schema that has the given
alias_name as an alias. If an alias_name is not provided, then
show all metrics schema.

Usage: %s [options]

Options:
-a <name>    alias name. Show only metric schema
whose aliases include the specified alias name.
Aliases usually occur when a metric is renamed or
reorganized. Multiple metrics can have the same alias
because the metrics come from different sources.
If not specified, the program will show all
metrics schema.

Example: 
The alias "-a drop_receive_network_interface_sum" 
returns
<ApiMetricSchema>
  name: drop_receive_network_interface_sum
  isCounter: True
  unitNumerator: packets
  aliases: [network_interface_drop_receive]
  sources:
    CLUSTER: [enterprise]
    HOST: [enterprise]
<ApiMetricSchema>
  name: drop_receive
  isCounter: True
  unitNumerator: packets
  aliases: [network_interface_drop_receive]
  sources:
    NETWORK_INTERFACE: [enterprise]

"""

import getopt
import inspect
import logging
import sys
import textwrap

from cm_api.api_client import ApiResource

#
# Customize these constants for your Cloudera Manager.
#
CM_HOST   = 'localhost'
CM_USER   = 'admin'
CM_PASSWD = 'admin'

LOG = logging.getLogger(__name__)

class MetricSchemas(object):
  """
  """
  def __init__(self):
    api = ApiResource(CM_HOST, username=CM_USER, password=CM_PASSWD)
    self._schemas = api.get_metric_schema()

  def get_schemas(self):
    return self._schemas

  def get_aliases(self, name):
    results = []
    for metric in self._schemas:
      if metric.aliases is not None:
        for alias in metric.aliases:
          if alias == name:
            results.append(metric)    
    return results;

def do_get_metrics():
  """
  Get schema for all metrics
  """
  metric_schemas = MetricSchemas()
  for metric in metric_schemas.get_schemas():
    do_print(metric)

def do_get_aliases(name):
  """
  Get aliases for given metric name
  """
  metric_schemas = MetricSchemas()
  aliases = metric_schemas.get_aliases(name)
  for alias in aliases:
    do_print(alias)

def do_print(metric):
  print "<ApiMetricSchema>"
  print "  name: %s" % metric.name
  print "  isCounter: %s" % metric.isCounter
  if metric.unitNumerator:
    print "  unitNumerator: %s" % metric.unitNumerator
  if metric.unitDenominator:
    print "  unitDenominator: %s" % metric.unitDenominator
  if metric.aliases:
    print "  aliases: %s" % map(str, metric.aliases)
  if metric.sources:
    print "  sources:"
    for (k,v) in metric.sources.items():
      print "    %s: %s" % (k, map(str, v))

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
    opts, args = getopt.getopt(argv[1:], "ha:")
  except getopt.GetoptError, err:
    print >>sys.stderr, err
    usage()
    return -1

  for option, val in opts:
    if option == '-h':
      usage()
      return -1
    elif option == '-a':
      do_get_aliases(val)
      return 0
    else:
      print >>sys.stderr, "Unknown flag:", option
      usage()
      return -1

  if args:
    print >>sys.stderr, "Unknown trailing argument:", args
    usage()
    return -1

  # Do work
  do_get_metrics()
  return 0


#
# The "main" entry
#
if __name__ == '__main__':
  sys.exit(main(sys.argv))
