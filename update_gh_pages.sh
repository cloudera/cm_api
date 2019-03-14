#!/bin/bash

# Script to update gh-pages branch that backs https://cloudera.github.io/cm_api site
set -eu

if [ $# -ne 2 ];
then
  echo "Insufficient arguments to gh-pages update script"
  echo "Usage: update_gh_pages.sh <api_version> <cm_version>"
  echo "Example: update_gh_pages.sh 32 6.2.0"
  exit 1
fi

api_version=$1
cm_version=$2

if ! [[ "$api_version" =~ ^[0-9]+$ ]];
then
  echo "API version $api_version doesn't look valid"
  exit 2
fi

if ! [[ "$cm_version" =~ ^[0-9]+.[0-9]+.[0-9]+$ ]];
then
  echo "CM version $cm_version doesn't look valid"
  exit 3
fi

echo "Updating gh-pages docs to API version $api_version and CM version $cm_version"
echo "Updating _config.yml file"

sed -i "s/latest_api_version:.*/latest_api_version:      $api_version/g" _config.yml
sed -i "s/latest_cm_version:.*/latest_cm_version:       $cm_version/g" _config.yml

echo "Updating docs/releases.md file"

search="<tbody>"
replace="<tbody>\n\
      <tr>\n\
          <td>v$api_version</td>\n\
          <td>Cloudera Manager $cm_version</td>\n\
          <td><a href=\"https://archive.cloudera.com/cm6/$cm_version/generic/jar/cm_api/apidocs/index.html\">API docs</a>,\n\
              <a href=\"https://archive.cloudera.com/cm6/$cm_version/generic/jar/cm_api/swagger-html-sdk-docs/java/README.html\">Java SDK Docs</a>,\n\
              <a href=\"https://archive.cloudera.com/cm6/$cm_version/generic/jar/cm_api/swagger-html-sdk-docs/python/README.html\">Python SDK Docs</a>\n\
          </td>\n\
      </tr>"

sed -i "s|$search|$replace|g" docs/releases.md

git add _config.yml docs/releases.md
git commit -m "[API] Update gh-pages for CM version $cm_version, API version $api_version"
git show HEAD

echo "====================="
echo "Verify the generated links with local jekyll server 'jekyll serve --watch --baseurl=/cm_api --host=0.0.0.0'"

