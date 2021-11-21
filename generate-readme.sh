#!/bin/bash
set -e
cd "$(dirname $0)"

HMCL_CURRENT_VERSION=`cat package.json | jq -M -r '.version'`
if [ -z `echo $HMCL_CURRENT_VERSION | grep "^[0-9]\.[0-9]\.[0-9]\{3\}\$"` ]; then exit 1; fi
sed "s/@@dev_version@@/$HMCL_CURRENT_VERSION/g" README.md.template > README.md