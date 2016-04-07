#!/bin/bash
find -type f -name derby.log -exec rm {} \;
find -type d -name sampledb -exec rm -r {} \;
