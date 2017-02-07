#!/bin/bash

. commons.sh

echo " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
echo " >>> NOTE: .deb PACKAGE MUST BE UP-TO-DATE! >>> "
echo " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> "
echo " "

echo " >>> Executing script for generation Crossdata $XD_VERSION Docker"
echo " "

debFile="../../dist/target/2.11/stratio-crossdata-mesosphere-scala211-$XD_VERSION.all.deb"

if [[ !  -f $debFile ]]; then
    echo "$debFile must be created previously"
    exit 1
fi

mkdir dockerfiles

echo " >>> Copying $debFile"
cp $debFile dockerfiles

echo " >>> Copying docker scripts"
cp ../../docker/* dockerfiles

echo " >>> Building Crossdata $XD_VERSION Docker"
docker build --build-arg PKG=$XD_VERSION -t crossdata-enterprise:$XD_VERSION .
echo " >>> Crossdata $XD_VERSION Docker generated"

echo " >>> Cleaning some stuff..."
rm -rf dockerfiles

echo " >>> Start Crossdata Docker with the Gosec security Manager: docker run [OPTIONS] crossdata-enterprise[:TAG] [COMMAND]"
echo " >>> Start Crossdata Docker with the default security Manager: docker run [OPTIONS] crossdata-enterprise[:TAG] [COMMAND] skipSecManager"
