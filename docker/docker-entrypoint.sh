#!/bin/bash -xe
if [ "x${XD_CATALOG}x" != "xx" ]; then
 sed -i "s|#crossdata-core.catalog.class.*|crossdata-core.catalog.class = \"org.apache.spark.sql.crossdata.catalog.${XD_CATALOG}Catalog\"|" /etc/sds/crossdata/server/core-application.conf
 if [ "${XD_CATALOG}" == "MySQL" ]; then
   sed -i "s|#crossdata-core.catalog.jdbc.driver.*|crossdata-core.catalog.jdbc.driver = \"org.mariadb.jdbc.Driver\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.url.*|crossdata-core.catalog.jdbc.url = \"${XD_CATALOG_HOST}:3306\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.name = \"crossdata\"|crossdata-core.catalog.jdbc.db.name = \"${XD_CATALOG_DB_NAME}\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.table = \"crossdataTables\"|crossdata-core.catalog.jdbc.db.table = \"${XD_CATALOG_DB_TABLE}\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.user = \"root\"|crossdata-core.catalog.jdbc.db.user = \"${XD_CATALOG_DB_USER}\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.pass = \"\"|crossdata-core.catalog.jdbc.db.pass = \"${XD_CATALOG_DB_PASS}\"|" /etc/sds/crossdata/server/core-application.conf
 fi
 if [ "${XD_CATALOG}" == "PostgreSQL" ]; then
   sed -i "s|#crossdata-core.catalog.jdbc.driver.*|crossdata-core.catalog.jdbc.driver = \"org.mariadb.jdbc.Driver\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.url.*|crossdata-core.catalog.jdbc.url = \"${XD_CATALOG_HOST}:3306\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.name = \"crossdata\"|crossdata-core.catalog.jdbc.db.name = \"${XD_CATALOG_DB_NAME}\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.table = \"crossdataTables\"|crossdata-core.catalog.jdbc.db.table = \"${XD_CATALOG_DB_TABLE}\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.user = \"root\"|crossdata-core.catalog.jdbc.db.user = \"${XD_CATALOG_DB_USER}\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.jdbc.db.pass = \"\"|crossdata-core.catalog.jdbc.db.pass = \"${XD_CATALOG_DB_PASS}\"|" /etc/sds/crossdata/server/core-application.conf
 fi
 if [ "${XD_CATALOG}" == "Zookeeper" ]; then
   sed -i "s|#crossdata-core.catalog.zookeeper.connectionString = \"localhost:2181\"|crossdata-core.catalog.zookeeper.connectionString = \"${XD_CATALOG_ZOOKEEPER_CONNECTION_STRING:=localhost:2181}\"|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.zookeeper.connectionTimeout = 15000|crossdata-core.catalog.zookeeper.connectionTimeout = ${XD_CATALOG_ZOOKEEPER_CONNECTION_TIMEOUT:=15000}|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.zookeeper.sessionTimeout = 60000|crossdata-core.catalog.zookeeper.sessionTimeout = ${XD_CATALOG_ZOOKEEPER_SESSION_TIMEOUT:=60000}|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.zookeeper.retryAttempts = 5|crossdata-core.catalog.zookeeper.retryAttempts = ${XD_CATALOG_ZOOKEEPER_RETRY_ATTEMPS:=5}|" /etc/sds/crossdata/server/core-application.conf
   sed -i "s|#crossdata-core.catalog.zookeeper.retryInterval = 10000|crossdata-core.catalog.zookeeper.retryInterval = ${XD_CATALOG_ZOOKEEPER_RETRY_INTERVAL:=10000}|" /etc/sds/crossdata/server/core-application.conf
 fi
fi
if [ "${XD_MODE}" == "Streaming" ]; then
 sed -i "s|//crossdata-core.streaming*|crossdata-core.streaming|" /etc/sds/crossdata/server/core-application.conf
 sed -i "s|crossdata-core.streaming.catalog.zookeeper.connectionString = \"localhost:2181\"|crossdata-core.streaming.catalog.zookeeper.connectionString =  \"${XD_ZOOKEEPER_CONNECTION_STRING}\"|" /etc/sds/crossdata/server/core-application.conf
 sed -i "s|crossdata-core.streaming.receiver.zk.connection = \"localhost:2181\"|crossdata-core.streaming.receiver.zk.connection =  \"${XD_ZOOKEEPER_CONNECTION_STRING}\"|" /etc/sds/crossdata/server/core-application.conf
 sed -i "s|crossdata-core.streaming.receiver.kafka.connection = \"localhost:9092\"|crossdata-core.streaming.receiver.kafka.connection =  \"${XD_KAFKA_CONNECTION_STRING}\"|" /etc/sds/crossdata/server/core-application.conf
 sed -i "s|crossdata-core.streaming.spark.master = \"spark://localhost:7077\"|crossdata-core.streaming.spark.master = \"${SPARK_MASTER}\" |" /etc/sds/crossdata/server/core-application.conf
fi
HOST="hostname -f"
if [[ "$(hostname -f)" =~ \. ]]; then
  HOST="$(hostname -f)"
else
  HOST="$(hostname -i)"
fi
AKKAIP=akka.tcp:\/\/CrossdataServerCluster@${HOST}:13420
if [ -z ${XD_SEED}]; then
 sed -i "s|<member>127.0.0.1</member>|<member>${HOST}</member>|" /etc/sds/crossdata/server/hazelcast.xml
 sed -i "s|crossdata-server.akka.cluster.seed-nodes =.*|crossdata-server.akka.cluster.seed-nodes = [\"${AKKAIP}\"]|" /etc/sds/crossdata/server/server-application.conf
else
 SEED_IP=akka.tcp:\/\/CrossdataServerCluster@${XD_SEED}:13420
 sed -i "s|crossdata-server.akka.cluster.seed-nodes =.*|crossdata-server.akka.cluster.seed-nodes = [\"${SEED_IP}\",\"${AKKAIP}\"]|" /etc/sds/crossdata/server/server-application.conf
 sed -i "s|<member>127.0.0.1</member>|<member>${XD_SEED}</member>|" /etc/sds/crossdata/server/hazelcast.xml
fi
sed -i "s|crossdata-server.akka.remote.netty.tcp.hostname.*|crossdata-server.akka.remote.netty.tcp.hostname = \"${HOST}\"|" /etc/sds/crossdata/server/server-application.conf
sed -i "s|local\[.\]|${SPARK_MASTER:=local\[2\]}|" /etc/sds/crossdata/server/server-application.conf
sed -i "s|crossdata-server.config.spark.driver.memory.*|crossdata-server.config.spark.driver.memory = ${XD_DRIVER_MEMORY:=512M}|" /etc/sds/crossdata/server/server-application.conf
sed -i "s|crossdata-server.config.spark.executor.memory.*|crossdata-server.config.spark.executor.memory = ${XD_EXECUTOR_MEMORY:=512M}|" /etc/sds/crossdata/server/server-application.conf
sed -i "s|crossdata-server.config.spark.cores.max.*|crossdata-server.config.spark.cores.max = ${XD_CORES:=4}|" /etc/sds/crossdata/server/server-application.conf

/etc/init.d/crossdata start
if [ -z ${XD_SEED}]; then
  sed -i "s|crossdata-driver.config.cluster.hosts.*|crossdata-driver.config.cluster.hosts = [\"${HOST}:13420\"]|" /etc/sds/crossdata/shell/driver-application.conf
else
 sed -i "s|crossdata-driver.config.cluster.hosts.*|crossdata-driver.config.cluster.hosts = [\"${HOST}:13420\", \"${XD_SEED}\"]|" /etc/sds/crossdata/shell/driver-application.conf
fi

tail -F /var/log/sds/crossdata/crossdata.log