#!/bin/bash
set -e

PROJECT_ROOT_DIR=`realpath "$(dirname $0)/.."`

javac -encoding UTF-8 -d "$PROJECT_ROOT_DIR/out" -cp "$PROJECT_ROOT_DIR/scripts" "$PROJECT_ROOT_DIR/scripts/$1.java"
java -cp "$PROJECT_ROOT_DIR/out" $1