#!/bin/bash
SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
cd $SCRIPTPATH
javadoc -encoding ISO-8859-1 ./src/*.java ./src/*/*.java -d ./docs/
