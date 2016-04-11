# Changelog

Only listing significant user-visible, not internal code cleanups and minor bug fixes. 

## 1.2.1 (April 2016)
* Deploy parameter for Spark-Submit configuration added
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
