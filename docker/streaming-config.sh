#!/bin/bash -xe

sed -i "s|//crossdata-core.streaming*|crossdata-core.streaming|" /etc/sds/crossdata/server/core-application.conf
crossdata_core_catalog_zookeeper_connectionString=$1
crossdata_core_streaming_receiver_zk_connection=$2
crossdata_core_streaming_receiver_kafka_connection=$3
crossdata_core_streaming_spark_master=$4