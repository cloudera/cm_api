#! /usr/bin/env python
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
import os.path

from setuptools import setup, find_packages

from sys import version_info, platform

if version_info[:2] > (2, 5):
    install_requires = []
else:
    install_requires = ['simplejson >= 2.0.0']

# Python 2.6 and below requires argparse
if version_info[:2] < (2, 7):
    install_requires += ['argparse']

# Mac does not come default with readline, this is needed for autocomplete
# in the cmps shell
if platform == 'darwin':
    install_requires += ['readline']

# Optional PySocks support
extras_require = dict(Socks=['PySocks >= 1.5.0'])
setup_dir = os.path.split(__file__)[0]
if setup_dir == '':
  setup_dir = '.';
base_dir = os.path.relpath(os.path.normpath(setup_dir), os.getcwd())
src_dir = os.path.normpath(os.path.join(base_dir, 'src'))

setup(
  name = 'cm_api',
  version = '14.0.0',    # Compatible with API v14 (CM 5.9)
  packages = find_packages(src_dir, exclude=['cm_api_tests']),
  package_dir = {'': src_dir },
  zip_safe = True,

  # Project uses simplejson, so ensure that it gets installed or upgraded
  # on the target machine
  install_requires = install_requires,
  extras_require = extras_require,

  author = 'Cloudera, Inc.',
  author_email = 'scm-users@cloudera.org',
  description = 'Cloudera Manager API client',
  long_desc = 'cm_api is a Python client to the Cloudera Manager REST API',
  license = 'Apache License 2.0',
  url = 'http://cloudera.github.com/cm_api/',
  classifiers = [
        "Development Status :: 5 - Production/Stable",
        "Operating System :: OS Independent",
        "Programming Language :: Python",
        "Programming Language :: Python :: 2.6",
        "Programming Language :: Python :: 2.7",
  ],
  entry_points = { 'console_scripts': [ 'cmps = cm_shell.cmps:main', ]}
)
