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

.PHONY: help
help:
	@echo 'The build targets are:'
	@echo '  dist		: Create a source distribution tarball'

.PHONY: test
test:
	@echo 'Running tests...'
	PYTHONPATH=src python -m unittest discover src/cm_api_tests

.PHONY: dist
dist: test
	git rev-parse HEAD > .git-hash
	python setup.py sdist

.PHONY: clean
clean:
	rm -rf *.egg-info
	rm -rf dist
	rm -rf build
	find . -name *.py[co] -exec rm -f {} \;

.PHONY: clean
clean:
	epydoc -v --html  -o doc -n python-cm_api `find . -name "*.py"`
