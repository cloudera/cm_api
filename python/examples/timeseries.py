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
Given a query and an optional time range, show all timeseries
data that matches the given query in the given time range.

Usage: %s [options] query

Options:
-f <from_time>        From time for the query
Should be in the format "YYYY-mm-ddTHH:MM".
Defaults to 5 minutes before to_time if not specified.
-t <to_time>           To time for the query.
Should be in the format "YYYY-mm-ddTHH:MM".
Defaults to now if not specified.

Example: 
The query "select cpu_percent" 
returns
<ApiTimeSeriesResponse>
  query: select cpu_percent
  timeSeries:
    metadata:
      metricName: cpu_percent
      entityName: localhost
      startTime: 2013-05-09 18:49:52.488000
      endTime: 2013-05-09 18:54:52.488000
      unitNumerators: [percent]
      attributes: {category: HOST, entityName: localhost,
                   hostId: localhost, rackId: default,
                   hostname: localhost}
    data:
      timestamp: 2013-05-09 18:50:38 value: 3.0 type: SAMPLE
      timestamp: 2013-05-09 18:51:38 value: 3.4 type: SAMPLE
      timestamp: 2013-05-09 18:52:38 value: 4.4 type: SAMPLE
      timestamp: 2013-05-09 18:53:38 value: 4.5 type: SAMPLE
      timestamp: 2013-05-09 18:54:38 value: 6.0 type: SAMPLE
"""

import getopt
import inspect
import logging
import sys
import textwrap
from datetime import datetime
from datetime import timedelta 

from cm_api.api_client import ApiResource

#
# Customize these constants for your Cloudera Manager.
#
CM_HOST = 'localhost'
CM_USER = 'admin'
CM_PASSWD = 'admin'
CM_USE_TLS = False

LOG = logging.getLogger(__name__)

class TimeSeriesQuery(object):
  """
  """
  def __init__(self):
    self._api = ApiResource(CM_HOST, username=CM_USER, password=CM_PASSWD, use_tls=CM_USE_TLS)

  def query(self, query, from_time, to_time):
    return self._api.query_timeseries(query, from_time, to_time)

def do_print(response):
  print "<ApiTimeSeriesResponse>"
  print "  query: %s" % (response.timeSeriesQuery)
  if response.warnings:
    print "  warnings: %s" % (response.warnings)
  if response.errors:
    print "  errors: %s" % (response.errors)
  if response.timeSeries:
    print "  timeSeries:"
    for ts in response.timeSeries:
      metadata = ts.metadata
      print "    metadata:"
      print "      metricName: %s" % (metadata.metricName)
      print "      entityName: %s" % (metadata.entityName)
      print "      startTime: %s" % (metadata.startTime)
      print "      endTime: %s" % (metadata.endTime)
      if metadata.unitNumerators:
        print "      unitNumerators: %s" % (metadata.unitNumerators)
      if metadata.unitDenominators: 
        print "      unitDenominators: %s" % (metadata.unitDenominators)
      if metadata.attributes:
        print "      attributes: %s" % (metadata.attributes)
      print "    data:"
      for data in ts.data:
        print "      timestamp: %s value: %s type: %s" % \
               (data.timestamp, data.value, data.type)

def do_query(query, from_time, to_time):
  tsquery = TimeSeriesQuery()
  for response in tsquery.query(query, from_time, to_time):
    do_print(response)

def usage():
  doc = inspect.getmodule(usage).__doc__
  print >>sys.stderr, textwrap.dedent(doc % (sys.argv[0],))

def setup_logging(level):
  logging.basicConfig()
  logging.getLogger().setLevel(level)

def main(argv):
  setup_logging(logging.INFO)

  from_time = None
  to_time = None

  # Argument parsing
  try:
    opts, args = getopt.getopt(argv[1:], "hf:t:")
  except getopt.GetoptError, err:
    print >>sys.stderr, err
    usage()
    return -1

  for option, val in opts:
    if option == '-h':
      usage()
      return -1
    elif option == '-f':
      try:
        print val
        from_time = datetime.strptime(val, "%Y-%m-%dT%H:%M")
        from_time = from_time.isoformat()
      except:
        print >>sys.stderr, "Unable to parse the from time:"
        usage()
        return -1
    elif option == '-t':
      try:
        to_time = datetime.strptime(val, "%Y-%m-%dT%H:%M")
        to_time = to_time.isoformat()
      except:
        print >>sys.stderr, "Unable to parse the to time:"
        usage()
        return -1
    else:
      print >>sys.stderr, "Unknown flag:", option
      usage()
      return -1

  if args:
    # Do work
    do_query(args[0], from_time, to_time)
    return 0
  else:
    print >>sys.stderr, "No query:"
    usage()
    return -1

#
# The "main" entry
#
if __name__ == '__main__':
  sys.exit(main(sys.argv))
