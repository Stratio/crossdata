#!/bin/bash

# Stratio Meta
#
# Copyright (c) 2014, Stratio, All rights reserved.
#
# This library is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public
# License as published by the Free Software Foundation; either
# version 3.0 of the License, or (at your option) any later version.
#
# This library is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
# Lesser General Public License for more details.
#
# You should have received a copy of the GNU Lesser General Public
#  License along with this library.
#
# Stratio Meta CCM Test

CCM_REPO=https://github.com/Stratio/ccm
TMPDIR=/tmp/stratiometa-ccm
CURRENTDIR=$(pwd)
CASSANDRA_BRANCH=release-2.0.5
CASSANDRA_BASE_VERSION=2.0.5
STRATIO_CASSANDRA_REPO="git@github.com:Stratio/stratio-cassandra.git"
CCM_DIR=$(which ccm)

if [ "$(ccm status | cut -f2 -d" " | tail -1)" = "UP" ] || [ "$(ccm status | cut -f2 -d" " | tail -1)" = "DOWN" ] ; then
	echo "CCM is already active" 
	exit
fi

mkdir ${TMPDIR}
echo " Installing pyYaml"
sudo apt-get install python-setuptools
sudo easy_install pyYaml

echo " Checking ccm installation ... "
unset $CASSANDRA_HOME

if [ -z "$CCM_DIR" ]; then
	echo " Installing ccm ... "
	cd $(TMPDIR)
	git clone ${CCM_REPO}
	cd ccm
	sudo ./setup.py install
	sudo updatedb
	cd .. && sudo rm -rf ccm
else
	echo " CCM is already installed"
fi

echo " Cloning Stratio-Cassandra repository ..."

if [ ! -d ~/.ccm/repository ]; then
  mkdir ~/.ccm/repository
fi

cd ~/.ccm/repository
if [ ! -d ${CASSANDRA_BASE_VERSION} ]; then
	git clone ${STRATIO_CASSANDRA_REPO}
	mv -f stratio-cassandra/ ${CASSANDRA_BASE_VERSION}
fi
cd ${CASSANDRA_BASE_VERSION}
git checkout ${CASSANDRA_BRANCH}
ant build
cd ..

echo " Initializing ccm ..."

ccm create testCluster --cassandra-version ${CASSANDRA_BASE_VERSION} --nodes 2
ccm updateconf
ccm start

#Delete temporary files
rm -rf ${TMPDIR}

#Go to inital directory
cd ${CURRENTDIR}
