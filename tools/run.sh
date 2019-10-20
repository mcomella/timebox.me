#!/bin/bash

set -e # exit on error.

BASEDIR=$(dirname $0)
java -jar $BASEDIR/timebox.jar $@
