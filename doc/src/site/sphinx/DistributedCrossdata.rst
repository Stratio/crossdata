Distributed Crossdata Configuration
***********************************
If you want to install Crossdata in several machines in a distributed environment, you need to change some params in
the configuration files in all Crossdata installations.

There are two configuration files in every Crossdata instance:

    - server-application.conf: In this file you can configure all params relatives to crossdata server.

    - driver-application.conf: Here you can set the parameters of the driver that can access to crossdata-server.


Servers configuration
=====================
To configure the servers in a distributed environment it is needed to access to conf directory in every crossdata
instance, and set the following params:

 - crossdata-server.akka.remote.netty.tcp.hostname: This is the IP of the local machine where is this crossdata instance.

 - crossdata-server.akka.remote.netty.tcp.port: The crossdata server port, default set in 13420.

 - crossdata-server.akka.cluster.seed-nodes: In this parameter it is needed to set all the IPs and Ports of all Crossdata servers. The format of that is ["akka.tcp://CrossdataServerCluster@IP_1:port_1", "akka.tcp://CrossdataServerCluster@IP_2:port_2", ... , "akka.tcp://CrossdataServerCluster@IP_n:port_n"]

 - crossdata-server.config.grid.listen-address: The local ip of the grid where metadata information is added by crossdata. This IP can be the same as the crossdata-server.akka.remote.netty.tcp.hostname.

 - crossdata-server.config.grid.contact-hosts: In this parameter it is needed to set all grid instance of all grids of the distributed environment. The format is [ip_1. ip_2, ..., ip_n]

 - crossdata-server.config.grid.persistence-path: This parameter is the local path where crossdata store all needed metadata.

 This is an example of the server parameters configuration::

    crossdata-server.akka.remote.netty.tcp.hostname = "10.90.0.101"
    crossdata-server.akka.remote.netty.tcp.port = 13420
    crossdata-server.akka.cluster.seed-nodes = ["akka.tcp://CrossdataServerCluster@10.90.0.101:13420",
    "akka.tcp://CrossdataServerCluster@10.90.0.102:13420","akka.tcp://CrossdataServerCluster@10.90.0.103:13420"]

    crossdata-server.config.grid.listen-address = "10.90.0.101"
    crossdata-server.config.grid.contact-hosts = ["10.90.0.101","10.90.0.102","10.90.0.103"]
    crossdata-server.config.grid.persistence-path = "/tmp/crossdata-store"

Drivers configuration
=====================
To start a crossdata client is needed to set the configuration file driver-application.conf of the conf directory.
This file contains:

 - crossdata-driver.config.cluster.name: The name of the crossdata cluster.

 - crossdata-driver.config.cluster.hosts: A list of crossdata servers where the client can connect. The format is like [ip_1:port_1, ip_2:port_2, ...,ip_n:port_n]

 - crossdata-driver.config.retry.times: Retry connections.

 - crossdata-driver.config.retry.duration: Time of retry connections.

 - crossdata-driver.config.cluster.actor: Name of the cluster actor.

 This is an example of driver-application file::

    crossdata-driver.config.cluster.name = "CrossdataServerCluster"
    crossdata-driver.config.cluster.actor = "crossdata-server"
    crossdata-driver.config.cluster.hosts = ["10.90.0.101:13420","10.90.0.102:13420","10.90.0.103:13420"]
    crossdata-driver.config.retry.times = 3
    crossdata-driver.config.retry.duration = 30s

About connectors in a distributed environment
=============================================
Every connector has a connector-application.conf that allow to configure it in a distributed environment. This file
is in the conf directory of every connector.

The params of this file are:

 - crossdata-connector.akka.cluster.seed-nodes: All the IPs and Ports of all Crossdata servers. The format of that is  ["akka.tcp://CrossdataServerCluster@IP_1:port_1","akka.tcp://CrossdataServerCluster@IP_2:port_2", ... , "akka.tcp://CrossdataServerCluster@IP_n:port_n"]

 - crossdata-connector.akka.remote.netty.tcp.hostname = Local IP where the connector.

 - crossdata-connector.akka.remote.netty.tcp.port: Local port, use 0 for autoselection.

 - crossdata-connector.config.akka.number.connector-actor: Number of threads receiving incoming queries.

  This is an example of connector-application file::

    crossdata-connector.akka.cluster.seed-nodes = ["akka.tcp://CrossdataServerCluster@10.90.0.101:13420",
    "akka.tcp://CrossdataServerCluster@10.90.0.102:13420","akka.tcp://CrossdataServerCluster@10.90.0.103:13420"]
    crossdata-connector.akka.remote.netty.tcp.hostname = "10.90.0.102"
    crossdata-connector.akka.remote.netty.tcp.port = 0
    crossdata-connector.config.akka.number.connector-actor = 5



