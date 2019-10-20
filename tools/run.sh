#!/bin/bash

# A run script to be used only after the installation process.

set -e # exit on error.

BASEDIR=$(dirname $0)
java -jar $BASEDIR/timebox.jar $@
