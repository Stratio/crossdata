# Changelog

Only listing significant user-visible, not internal code cleanups and minor bug fixes. 

## 1.6.0 (upcoming)

* Elasticsearch: support import tables without specifying a particular resource

## 1.5.0 (August 2016)

* Global index: use of inverted indices for speeding-up queries
* Downgrade to Elasticsearch 2.0.2
* Session management
* Multitenant architecture: isolate metadata (catalog and configuration)
* Multiple driver's instances per JVM

## 1.4.0 (July 2016)

* Upgrade to Elasticsearch 2.3

## 1.3.0 (June 2016)

* Upgrade to Spark 1.6.1
* Support for INSERT INTO
* Support for ADD JAR
* Enabled Scala cross builds for Scala 2.10 and Scala 2.11.
* Encryption for the communication between client and server.
* Added method to check communication between client ans server.
* Upgrade Stratio spark-mongodb to 0.11.2
* Upgrade Datastax spark-cassandra-connector to 1.6.0
* Upgrade Elasticsearch-hadoop to 2.3
* Improved communication between client and server.
* Native jobs available in SparkUI
* Improved native access to MongoDB

## 1.2.2 (May 2016)
* Upgrade spark-mongodb to 0.11.2
* Security Manager added
* Deploying uber-jars in Maven

## 1.2.1 (April 2016)
* Deploy mode parameter for Spark-Submit configuration added
* Bux fixing in package creation

## 1.2.0 (March 2016)
* StreamSQL: Streaming capabilities from a SQL language including insert into select (Batch + Streaming)
* Improved asynchronous driver API
* Create external tables
* Cancellable jobs
* Zookeeper catalog
* CSV datasource added
* Avro datasource added
* Session identifiers in queries
* Zeppelin interpreter added
* Resolution of conflictive names with aliases preference

## 1.1.0 (February 2016)
* Flattener of subdocuments for relational interfaces 
* Flattener of collections for relational interfaces
* Persistent views (not materialized)
* Improvements in the aliases resolution of Spark through the Crossdata Context
* Improvements in the stability of the Crossdata Servers
* Usage of environment variables to configure Crossdata

## 1.0.2 (January 2016)
* Downgrade postgres version to 9.2
* Permgem space increased
* Performance improvements

## 1.0.1 (January 2016)
* Fix server script

## 1.0.0 (December 2015)

* Architecture migrated to Spark Catalyst.
* Upgraded Spark 1.5.2
* Mix data from different datastores: Cassandra, MongoDB and Elasticsearch.
* Native access to datastores: Cassandra, MongoDB and Elasticsearch.
* Java/Scala Driver API for external projects like ODBC/JDBC.
* Improved and extended SparkContext: XDContext.
* Pluggable and persistent catalog.
* Metadata discovery.
* Temporary views.
* P2P architecture: High availability and scalability.
* Query builder.
