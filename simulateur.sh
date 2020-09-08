#!/bin/bash
SCRIPTPATH="$( cd "$(dirname "$0")" >/dev/null 2>&1 ; pwd -P )"
cd $SCRIPTPATH
echo "ATTENTION : il faut que le fichier simulateur.java ait été compilé !" 
java -cp ./bin Simulateur $@
