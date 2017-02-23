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
usage: aws.py [-h] -H HOSTNAME [-p PORT] [-u USERNAME] [--password PASSWORD]
              [--api-version API_VERSION] [--tls] [-c] [-t CATEGORY_NAME]
              [-n TYPE_NAME] [--prune CREDENTIAL_NAME] [--version]

A utility to interact with AWS using Cloudera Manager.

optional arguments:
  -h, --help            show this help message and exit
  -H HOSTNAME, --hostname HOSTNAME
                        The hostname of the Cloudera Manager server.
  -p PORT               The port of the Cloudera Manager server. Defaults to
                        7180 (http) or 7183 (https).
  -u USERNAME, --username USERNAME
                        Login name.
  --password PASSWORD   Login password.
  --api-version API_VERSION
                        API version to be used. Defaults to 16.
  --tls                 Whether to use tls (https).
  -c, --show-categories
                        Prints a list of supported external account category
                        names. For example, "AWS" is a supported external
                        account category name.
  -t CATEGORY_NAME, --show-types CATEGORY_NAME
                        Prints a list of supported external account type names
                        for the given CATEGORY_NAME. For example,
                        "AWS_ACCESS_KEY_AUTH" is a supported external account
                        type name for external account category "AWS".
  -n TYPE_NAME, --show-credentials TYPE_NAME
                        Prints a list of available credential names for the
                        given TYPE_NAME.
  --prune CREDENTIAL_NAME
                        Runs S3Guard prune command on external account
                        associated with the given CREDENTIAL_NAME.
  --version             show program's version number and exit
"""
import argparse
import getpass
import logging
import sys

from cm_api.api_client import ApiResource
from cm_api.endpoints.external_accounts import *

# Configuration
DEFAULT_HTTP_PORT = 7180
DEFAULT_HTTPS_PORT = 7183
MINIMUM_SUPPORTED_API_VERSION = 16

# Constants
COMMA_WITH_SPACE = ", "

# Global API object
api = None


def list_supported_categories():
  """
  Prints a list of supported external account category names.
  For example, "AWS" is a supported external account category name.
  """
  categories = get_supported_categories(api)
  category_names = [category.name for category in categories]
  print ("Supported account categories by name: {0}".format(
    COMMA_WITH_SPACE.join(map(str, category_names))))


def list_supported_types(category_name):
  """
  Prints a list of supported external account type names for the given
  category_name. For example, "AWS_ACCESS_KEY_AUTH" is a supported external
  account type name for external account category "AWS".
  """
  types = get_supported_types(api, category_name)
  type_names = [type.name for type in types]
  print ("Supported account types by name for '{0}': [{1}]".format(
    category_name, COMMA_WITH_SPACE.join(map(str, type_names))))


def list_credentials_by_name(type_name):
  """
  Prints a list of available credential names for the given type_name.
  """
  accounts = get_all_external_accounts(api, type_name)
  account_names = [account.name for account in accounts]
  print ("List of credential names for '{0}': [{1}]".format(
    type_name, COMMA_WITH_SPACE.join(map(str, account_names))))


def call_s3guard_prune(credential_name):
  """
  Runs S3Guard prune command on external account associated with the
  given credential_name.
  """  # Get the AWS credential account associated with the credential
  account = get_external_account(api, credential_name)
  # Invoke the prune command for the account by its name
  cmd = account.external_account_cmd_by_name('S3GuardPrune')
  print ("Issued '{0}' command with id '{1}'".format(cmd.name, cmd.id))
  print ("Waiting for command {0} to finish...".format(cmd.id))
  cmd = cmd.wait()
  print ("Command succeeded: {0}".format(cmd.success))


def setup_logging(level):
  """
  Sets up the logging for the script. The default logging level
  is set to INFO.
  """
  logging.basicConfig()
  logging.getLogger().setLevel(level)


def initialize_api(args):
  """
  Initializes the global API instance using the given arguments.
  @param args: arguments provided to the script.
  """
  global api
  api = ApiResource(server_host=args.hostname, server_port=args.port,
                    username=args.username, password=args.password,
                    version=args.api_version, use_tls=args.use_tls)


def validate_api_compatibility(args):
  """
  Validates the API version.
  @param args: arguments provided to the script.
  """
  if args.api_version and args.api_version < MINIMUM_SUPPORTED_API_VERSION:
    print("ERROR: Given API version: {0}. Minimum supported API version: {1}"
          .format(args.api_version, MINIMUM_SUPPORTED_API_VERSION))


def get_login_credentials(args):
  """
    Gets the login credentials from the user, if not specified while invoking
    the script.
    @param args: arguments provided to the script.
    """
  if not args.username:
    args.username = raw_input("Enter Username: ")
  if not args.password:
    args.password = getpass.getpass("Enter Password: ")


def main():
  """
  The "main" entry that controls the flow of the script based
  on the provided arguments.
  """
  setup_logging(logging.INFO)

  # Parse arguments
  parser = argparse.ArgumentParser(
    description="A utility to interact with AWS using Cloudera Manager.")
  parser.add_argument('-H', '--hostname', action='store', dest='hostname',
                      required=True,
                      help='The hostname of the Cloudera Manager server.')
  parser.add_argument('-p', action='store', dest='port', type=int,
                      help='The port of the Cloudera Manager server. Defaults '
                           'to 7180 (http) or 7183 (https).')
  parser.add_argument('-u', '--username', action='store', dest='username',
                      help='Login name.')
  parser.add_argument('--password', action='store', dest='password',
                      help='Login password.')
  parser.add_argument('--api-version', action='store', dest='api_version',
                      type=int,
                      default=MINIMUM_SUPPORTED_API_VERSION,
                      help='API version to be used. Defaults to {0}.'.format(
                        MINIMUM_SUPPORTED_API_VERSION))
  parser.add_argument('--tls', action='store_const', dest='use_tls',
                      const=True, default=False,
                      help='Whether to use tls (https).')
  parser.add_argument('-c', '--show-categories', action='store_true',
                      default=False, dest='show_categories',
                      help='Prints a list of supported external account '
                           'category names. For example, "AWS" is a supported '
                           'external account category name.')
  parser.add_argument('-t', '--show-types', action='store',
                      dest='category_name',
                      help='Prints a list of supported external account type '
                           'names for the given CATEGORY_NAME. For example, '
                           '"AWS_ACCESS_KEY_AUTH" is a supported external '
                           'account type name for external account category '
                           '"AWS".')
  parser.add_argument('-n', '--show-credentials', action='store',
                      dest='type_name',
                      help='Prints a list of available credential names for '
                           'the given TYPE_NAME.')
  parser.add_argument('--prune', action='store', dest='credential_name',
                      help='Runs S3Guard prune command on external account '
                           'associated with the given CREDENTIAL_NAME.')
  parser.add_argument('--version', action='version', version='%(prog)s 1.0')
  args = parser.parse_args()

  # Use the default port if required.
  if not args.port:
    if args.use_tls:
      args.port = DEFAULT_HTTPS_PORT
    else:
      args.port = DEFAULT_HTTP_PORT

  validate_api_compatibility(args)
  get_login_credentials(args)
  initialize_api(args)

  # Perform the AWS operation based on the input arguments.
  if args.show_categories:
    list_supported_categories()
  elif args.category_name:
    list_supported_types(args.category_name)
  elif args.type_name:
    list_credentials_by_name(args.type_name)
  elif args.credential_name:
    call_s3guard_prune(args.credential_name)
  else:
    print ("ERROR: No arguments given to perform any AWS operation.")
    parser.print_help()
    sys.exit(1)


"""
The "main" entry.
"""
if __name__ == "__main__":
  sys.exit(main())
