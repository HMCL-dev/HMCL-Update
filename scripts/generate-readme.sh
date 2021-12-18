#!/bin/bash
set -e
cd "$(dirname $0)/.."

export HMCL_DEV_VERSION=`cat ./channels/dev/package.json | jq -M -r '.version'`
export HMCL_STABLE_VERSION=`cat ./channels/stable/package.json | jq -M -r '.version'`

java ./scripts/ReadMeTemplate.java