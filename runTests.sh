#!/bin/bash
SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
cd $SCRIPTPATH
find ./tests -name "\*.java" \ | sed -e "s/\.java//" -e "s/\//./g" \
    | xargs java org.junit.runner.JUnitCore
