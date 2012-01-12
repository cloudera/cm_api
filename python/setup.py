#! /usr/bin/env python
# Copyright (c) 2012 Cloudera, Inc. All rights reserved.

from setuptools import setup, find_packages

from sys import version_info
if version_info[:2] > (2, 5):
    install_requires = []
else:
    install_requires = ['simplejson >= 2.0.0']

setup(
  name = 'cm_api',
  version = '1.0',      # Compatible with API v1
  packages = find_packages('src'),
  package_dir = {'cm_api': 'src/cm_api'},

  # Project uses simplejson, so ensure that it gets installed or upgraded
  # on the target machine
  install_requires = install_requires,

  author = 'Cloudera, Inc.',
  description = 'Cloudera Manager API client',
  license = 'Proprietary',
  url = 'http://www.cloudera.com/',
)
