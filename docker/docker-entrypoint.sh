#!/bin/bash -xe

DOCKER_HOST="hostname -f"
if [[ "$(hostname -f)" =~ \. ]]; then
  export DOCKER_HOST="$(hostname -f)"
else
  export DOCKER_HOST="$(hostname -i)"
fi

####################################################
## XD Catalog
####################################################
source catalog-config.sh $XD_CATALOG $XD_CATALOG_PREFIX


####################################################
## Streaming
####################################################
 if [ "$XD_MODE" == "Streaming" ]; then
    source streaming-config.sh ${XD_ZOOKEEPER_CONNECTION_STRING} ${XD_ZOOKEEPER_CONNECTION_STRING} ${XD_KAFKA_CONNECTION_STRING} ${SPARK_MASTER}
 fi

####################################################
## Crossdata Config
####################################################
source crossdata-config.sh
case "$SERVER_MODE" in
 "shell") # This mode will launch a crossdata shell instead of a crossdata server
    if [ "$SHELL_SERVERADDR" -a "$SHELL_SERVERPORT" ]; then
        source shell-config.sh
        /opt/sds/crossdata/bin/crossdata-shell $OPTIONS
    else
        echo "A crossdata server address should be provided in shell mode"
        exit -1
    fi
    ;;
 "debug") # In this mode, crossdata will be launched as a service within the docker container.
    /etc/init.d/crossdata start
    tail -F /var/log/sds/crossdata/crossdata.log
    ;;
 *) # Default mode: Crossdata server run as a docker application
    /opt/sds/crossdata/bin/server.sh
    ;;
esac



