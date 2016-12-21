#!/bin/bash

. commons.sh

if [ -z "$1" ]; then
    echo ">>> DOCKER GENERATION: Usage: ./generateWithGosec.sh <gosec-dyplon-branch> [skipXDDocker]"
	echo ">>> DOCKER GENERATION: Usage example: ./generateWithGosec.sh branch-0.4 skipXDDocker"
	exit 1
fi

if [ -z "$2" ]; then
	echo ">>> DOCKER GENERATION: Generating Crossdata $XD_VERSION docker without security"
	. generate.sh
	echo ">>> DOCKER GENERATION: Crossdata $XD_VERSION docker without security created"
fi	

echo ">>> DOCKER GENERATION: Cloning dyplon-gosec project"

if [ -d "gosec-dyplon" ]; then
    rm -rf gosec-dyplon
fi

git clone https://github.com/Stratio/gosec-dyplon.git

echo ">>> DOCKER GENERATION: dyplon-gosec cloned"

changedir "gosec-dyplon"

echo ">>> DOCKER GENERATION: checkout to branch $1"

git checkout $1

echo ">>> DOCKER GENERATION: Modifying Gosec plugin for Crossdata"

if [ -z "$2" ]; then
	#Modify Gosec Crossdata plugin Dockerfile to put $XD_VERSION and docker image created
	sed -i .tmp "s|FROM qa.stratio.com/stratio/crossdata-mesosphere-scala211:.*|FROM crossdata-enterprise:$XD_VERSION|" plugins/crossdata/Dockerfile
else 
	#Modify Gosec Crossdata plugin Dockerfile to put $XD_VERSION and docker image from qa.stratio.com
	sed -i .tmp "s|FROM qa.stratio.com/stratio/crossdata-mesosphere-scala211:.*|FROM qa.stratio.com/stratio/crossdata-mesosphere-scala211:$XD_VERSION|" plugins/crossdata/Dockerfile
fi

#Modify Crossdata pom to put $XD_VERSION
sed -i .tmp "s|<crossdata.version>.*|<crossdata.version>$XD_VERSION</crossdata.version>|" plugins/crossdata/pom.xml
sed -i .tmp "s|COPY plugins/crossdata/|COPY |" plugins/crossdata/Dockerfile
sed -i .tmp '7i\
RUN chmod +x /secured-docker-entrypoint.sh' plugins/crossdata/Dockerfile

#Get Crossdata plugin version from pom
tmp4=`grep -m1 "<version>" plugins/crossdata/pom.xml`
tmp5=${tmp4/<version>/}
tmp6=${tmp5/<\/version>/}
XD_GOSEC_VERSION=${tmp6// }

echo ">>> DOCKER GENERATION: Installing gosec-dyplon (including Crossdata $XD_GOSEC_VERSION)"

mvn clean install -DskipUTs -DskipITs

echo ">>> DOCKER GENERATION: gosec-dyplon installed"

changedir "plugins/crossdata"

echo ">>> DOCKER GENERATION: Generating docker $XD_VERSION with Gosec"

docker build --build-arg VERSION=$XD_GOSEC_VERSION -t crossdata-gosec:$XD_VERSION .

echo ">>> DOCKER GENERATION: Docker generated for $XD_VERSION with Gosec generated"

changedir "../../../"

rm -rf gosec-dyplon

