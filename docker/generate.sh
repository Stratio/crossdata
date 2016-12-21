#!/bin/bash

. commons.sh

changedir "../"

echo ">>> DOCKER GENERATION: Installing Crossdata before generating $XD_VERSION"

mvn clean install -DskipUTs -DskipITs

echo ">>> DOCKER GENERATION: Generating $XD_VERSION package"

mvn package -Ppackage -DskipUTs -DskipITs

echo ">>> DOCKER GENERATION: Moving $XD_VERSION to docker directory"

cp dist/target/2.11/stratio-crossdata-mesosphere-scala211-$XD_VERSION.all.deb docker/

changedir "docker"

echo ">>> DOCKER GENERATION: Executing Dockerfile using $XD_VERSION"

docker build --build-arg PKG=$XD_VERSION -t crossdata-enterprise:$XD_VERSION .

echo ">>> DOCKER GENERATION: Docker generated using $XD_VERSION"

