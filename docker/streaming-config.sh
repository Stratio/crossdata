#!/bin/bash -xe

sed -i "s|//crossdata-core.streaming*|crossdata-core.streaming|" /etc/sds/crossdata/server/core-application.conf
export crossdata_core_catalog_zookeeper_connectionString=$1
export crossdata_core_streaming_receiver_zk_connection=$2
export crossdata_core_streaming_receiver_kafka_connection=$3
export crossdata_core_streaming_spark_master=$4