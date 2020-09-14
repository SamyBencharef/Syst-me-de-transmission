#!/bin/bash
find ./tests -name "\*.java" \ | sed -e "s/\.java//" -e "s/\//./g" \
    | xargs java org.junit.runner.JUnitCore
