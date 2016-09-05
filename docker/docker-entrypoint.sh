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

#Marathon support
if [ -z ${MARATHON_APP_ID} ]; then
    AKKAIP=akka.tcp:\/\/CrossdataServerCluster@${HOST}:13420
    #TODO: Test instead of XD_SEED : CROSSDATA_SERVER_AKKA_CLUSTER_SEED_NODES
    if [ -z ${XD_SEED} ]; then
     sed -i "s|<member>127.0.0.1</member>|<member>${HOST}</member>|" /etc/sds/crossdata/server/hazelcast.xml
     sed -i "s|#crossdata-server.akka.cluster.seed-nodes =.*|crossdata-server.akka.cluster.seed-nodes = [\"${AKKAIP}\"]|" /etc/sds/crossdata/server/server-application.conf
    else
     SEED_IP=akka.tcp:\/\/CrossdataServerCluster@${XD_SEED}:13420
     sed -i "s|#crossdata-server.akka.cluster.seed-nodes =.*|crossdata-server.akka.cluster.seed-nodes = [\"${SEED_IP}\",\"${AKKAIP}\"]|" /etc/sds/crossdata/server/server-application.conf
     sed -i "s|<member>127.0.0.1</member>|<member>${XD_SEED}</member>|" /etc/sds/crossdata/server/hazelcast.xml
    fi

    #TODO: Check environment vars for hostname and bind hostname & ports
    sed -i "s|#crossdata-server.akka.remote.netty.tcp.hostname.*|crossdata-server.akka.remote.netty.tcp.hostname = \"${HOST}\"|" /etc/sds/crossdata/server/server-application.conf
    sed -i "s|#crossdata-server.akka.remote.netty.tcp.bind-hostname.*|crossdata-server.akka.remote.netty.tcp.bind-hostname = \"${HOST}\"|" /etc/sds/crossdata/server/server-application.conf
    if [ -z ${XD_SEED} ]; then
        sed -i "s|crossdata-driver.config.cluster.hosts.*|crossdata-driver.config.cluster.hosts = [\"${HOST}:13420\"]|" /etc/sds/crossdata/shell/driver-application.conf
    else
        sed -i "s|crossdata-driver.config.cluster.hosts.*|crossdata-driver.config.cluster.hosts = [\"${HOST}:13420\", \"${XD_SEED}\"]|" /etc/sds/crossdata/shell/driver-application.conf
    fi
else
    #XD_EXTERNAL_IP Must be specified
    if [ -z ${XD_EXTERNAL_IP} ] || [ -z ${MARATHON_APP_LABEL_HAPROXY_0_PORT} ]; then
        echo ERROR: Env var XD_EXTERNAL_IP and label HAPROXY_0_PORT must be provided using Marathon&Haproxy 1>&2
        exit 1 # terminate and indicate error
    else
        #Memory
        RAM_AVAIL=$(echo $MARATHON_APP_RESOURCE_MEM | cut -d "." -f1)
        CROSSDATA_JAVA_OPT="-Xmx${RAM_AVAIL}m -Xms${RAM_AVAIL}m"
        sed -i "s|# CROSSDATA_LIB|#CROSSDATA_JAVA_OPTS\nCROSSDATA_JAVA_OPTS=\"${CROSSDATA_JAVA_OPT}\"\n# CROSSDATA_LIB|" /etc/sds/crossdata/server/crossdata-env.sh

        #Hostname and port of haproxy
        HAPROXY_FINAL_ROUTE=${XD_EXTERNAL_IP}:${MARATHON_APP_LABEL_HAPROXY_0_PORT}
        sed -i "s|#crossdata-server.akka.remote.netty.tcp.hostname.*|crossdata-server.akka.remote.netty.tcp.hostname = \"${XD_EXTERNAL_IP}\"|" /etc/sds/crossdata/server/server-application.conf
        sed -i "s|#crossdata-server.akka.remote.netty.tcp.port.*|crossdata-server.akka.remote.netty.tcp.port = \"${MARATHON_APP_LABEL_HAPROXY_0_PORT}\"|" /etc/sds/crossdata/server/server-application.conf
        sed -i "s|#crossdata-server.akka.cluster.seed-nodes =.*|crossdata-server.akka.cluster.seed-nodes = [\"akka.tcp:\/\/CrossdataServerCluster@${HAPROXY_FINAL_ROUTE}\"]|" /etc/sds/crossdata/server/server-application.conf

        #Bind address for local
        sed -i "s|#crossdata-server.akka.remote.netty.tcp.bind-hostname.*|crossdata-server.akka.remote.netty.tcp.bind-hostname = \"${HOST}\"|" /etc/sds/crossdata/server/server-application.conf
        sed -i "s|#crossdata-server.akka.remote.netty.tcp.bind-port.*|crossdata-server.akka.remote.netty.tcp.bind-port = \"13420\"|" /etc/sds/crossdata/server/server-application.conf

        #Hazelcast TODO check
        #sed -i "s|<member>127.0.0.1</member>|<member>${XD_SEED}</member>|" /etc/sds/crossdata/server/hazelcast.xml

        #Driver
        sed -i "s|crossdata-driver.config.cluster.hosts.*|crossdata-driver.config.cluster.hosts = [\"${HAPROXY_FINAL_ROUTE}\"]|" /etc/sds/crossdata/shell/driver-application.conf
    fi
fi

#TODO spark external check
sed -i "s|local\[.\]|${SPARK_MASTER:=local\[2\]}|" /etc/sds/crossdata/server/server-application.conf
sed -i "s|crossdata-server.config.spark.driver.memory.*|crossdata-server.config.spark.driver.memory = ${XD_DRIVER_MEMORY:=512M}|" /etc/sds/crossdata/server/server-application.conf
sed -i "s|crossdata-server.config.spark.executor.memory.*|crossdata-server.config.spark.executor.memory = ${XD_EXECUTOR_MEMORY:=512M}|" /etc/sds/crossdata/server/server-application.conf
sed -i "s|crossdata-server.config.spark.cores.max.*|crossdata-server.config.spark.cores.max = ${XD_CORES:=4}|" /etc/sds/crossdata/server/server-application.conf

/etc/init.d/crossdata start

tail -F /var/log/sds/crossdata/crossdata.log