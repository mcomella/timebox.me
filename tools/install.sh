#!/bin/bash

# Installs the app: this will overwrite any files at the destination.

set -e # exit on error.

# These are absolute paths.
BASEDIR=$(cd "$(dirname "$0")"; pwd) # abs path, via https://stackoverflow.com/a/5756763
RUN_SCRIPT=$BASEDIR/run.sh
PROJ_DIR=$BASEDIR/../

cd $PROJ_DIR
./gradlew -q jar # must run from proj dir.

mkdir -p ~/bin
cp $RUN_SCRIPT ~/bin/timebox
cp $PROJ_DIR/build/libs/timebox.jar ~/bin

echo "Successfully installed timebox & timebox.jar to ~/bin/timebox."
