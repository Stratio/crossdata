Crossdata Configuration
************************

Crossdata has the following requirements:
 - `MySQL <http://dev.mysql.com/downloads/mysql/>`_ to persist metadata in the Crossdata Catalog.
 - `Mongo Provider <https://github.com/Stratio/spark-mongodb>`_ to get the features of Mongo into Spark.

Once the requirements are installed it's necessary to set up the server and driver configuration files to allow
crossdata runs in a distributed environments. In other case, it is possible to use Crossdata with a default values,
and it will run in localhost mode.

Server Configuration for Distributed Environment
=================================================

To configure the servers in a distributed environment it is needed to create a server-application.conf file in every
crossdata instance, with the following params:

 - crossdata-server.akka.remote.netty.tcp.hostname: This is the IP of the local machine where Crossdata server instance is running.

 - crossdata-server.akka.remote.netty.tcp.port: The Crossdata server port, default is set in 13420.

 - crossdata-server.akka.cluster.seed-nodes: In this parameter it is needed to set all the IPs and Ports of all Crossdata servers. The format of that is ["akka.tcp://CrossdataServerCluster@IP_1:port_1", "akka.tcp://CrossdataServerCluster@IP_2:port_2", ... , "akka.tcp://CrossdataServerCluster@IP_n:port_n"]


This is an example of server-application.conf file::

    crossdata-server.akka.remote.netty.tcp.hostname = "10.90.0.101"
    crossdata-server.akka.remote.netty.tcp.port = 13420
    crossdata-server.akka.cluster.seed-nodes = ["akka.tcp://CrossdataServerCluster@10.90.0.101:13420",
    "akka.tcp://CrossdataServerCluster@10.90.0.102:13420","akka.tcp://CrossdataServerCluster@10.90.0.103:13420"]


Driver configuration
======================
To start a crossdata client is needed to create the configuration file driver-application.conf. In any other case the driver tries to connect to a localhost server of Crossdata.
This file must contain:

 - crossdata-driver.config.cluster.name: The name of the Crossdata cluster.

 - crossdata-driver.config.cluster.hosts: A list of Crossdata servers where the client can connect. The format is like [ip_1:port_1, ip_2:port_2, ...,ip_n:port_n]

 - crossdata-driver.config.retry.times: Retry connections.

 - crossdata-driver.config.retry.duration: Time of retry connections.

 - crossdata-driver.config.cluster.actor: Name of the cluster actor.

This is an example of driver-application file::

    crossdata-driver.config.cluster.name = "CrossdataServerCluster"
    crossdata-driver.config.cluster.actor = "crossdata-server"
    crossdata-driver.config.cluster.hosts = ["10.90.0.101:13420","10.90.0.102:13420","10.90.0.103:13420"]
    crossdata-driver.config.retry.times = 3
    crossdata-driver.config.retry.duration = 30s



Catalog Configuration
======================
To allow crossdata stores metadata from different data sources is necessary to have a Crossdata Catalog. For this case crossdata uses a JDBC connection with a relational database (currently MySQL database). It is possible to set up some parameters to specify the requirements of the catalog in file core/src/main/resources/application.conf:

 - crossdata.catalog.class: The Crossdata class that controls the catalog.

 - crossdata.catalog.caseSensitive = Set if the catalog database is case sensitive.

 - crossdata.catalog.mysql.type: Type of Relational Database used in the catalog.

 - crossdata.catalog.mysql.ip: Ip address to the database.

 - crossdata.catalog.mysql.ip: Port number to the database.

 - crossdata.catalog.mysql.driver: JDBC Driver class.

 - crossdata.catalog.mysql.db.name: Database name in the database catalog.

 - crossdata.catalog.mysql.db.tableName: Table name where the metadata will be stored in the database catalog.

 - crossdata.catalog.mysql.db.user: Allowed user to connect using JDBC to the catalog database.

 - crossdata.catalog.mysql.db.pass: Password to connect using JDBC to the catalog database.

This is an example of the configuration file::

    crossdata.catalog.class = "org.apache.spark.sql.crossdata.catalog.MySQLCatalog"

    crossdata.catalog.caseSensitive = true

    #MySQL parameters
    crossdata.catalog.mysql.type = "mysql"
    crossdata.catalog.mysql.ip = "127.0.0.1"
    crossdata.catalog.mysql.port = "3306"
    crossdata.catalog.mysql.driver = "com.mysql.jdbc.Driver"
    crossdata.catalog.mysql.db.name = "crossdata"
    crossdata.catalog.mysql.db.persistTable = "crossdataTables"
    crossdata.catalog.mysql.db.user = "root"
    crossdata.catalog.mysql.db.pass = ""
