Getting started
****************

Prerequisites
==============
Crossdata can be installed in a single computer as a proof of concept, or can be installed in a cluster.
The minimum requisites are the same as `Spark requisites <http://spark.apache.org/docs/latest/hardware-provisioning.html>_`.

It is necessary too, to install `Apache Maven 3<https://maven.apache.org/>_` due to the build script use it to get
all dependencies of Crossdata and `Mongo Provider <https://github.com/Stratio/spark-mongodb>_` to get the features of Mongo into Spark.

Finally, it is necessary to have a minimum knowledge on Apache Spark, specifically in `SparkSQL Grammar<https://spark.apache.org/docs/1.5.1/sql-programming-guide.html>_`.
It could be useful take a look to `Crossdata reference guide<6_reference_guide.rst>_` before start with Crossdata.

Download
=========
Crossdata can be downloaded from `Stratio Crossdata Github repository <https://github.com/Stratio/Crossdata>_` or directly using Stratio Platform.

Build
======
There are different ways to install Crossdata depending of your necessities.
One way is using maven::

    > mvn clean package -Ppackage

and it could be packaged with hive dependencies too::

    > mvn clean package -Ppackage -Phive

You can build a Spark Distribution with Crossdata libraries running the make-distribution-crossdata script::

    > cd scripts
    > ./make-distribution-crossdata.sh

This will build Spark with the following options:
    - Spark-1.5.1/hadoop2.6
    - Crossdata shell
    - And all available Crossdata providers

For others options run ./make-distribution-crossdata.sh --help

Install
========
Actually it is not available any rpm or deb installation packages. It is possible to use the build script to start
using Crossdata.

Configure
==========
Please see the `Configuration section <3_configuration.rst>_`.

Running Crossdata standalone
=============================
Crossdata can be used in a standalone mode as it happened with Apache Spark. It can be useful for testing purposes or
simple computation tasks. This kind of use, includes Cassandra and Mongo datasources.

To run Crossdata in a standalone mode, it's necessary to install Crossdata using the make-distribution-crossdata
script first.
Once Crossdata is installed, just run this::

    > bin/stratio-xd-shell --cassandra --mongodb

This example register the existent table "students" of the Cassandra keyspace "highschool" that has a few rows inserted. Once the table is registered then execute a query by the native way::

    >xdContext.sql("CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra
            OPTIONS (keyspace 'highschool', table 'students', cluster 'students', pushdown 'true',
            spark_cassandra_connection_host '127.0.0.1')")
    >xdContext.sql("SELECT * FROM students").collect()


Running Crossdata as a client-server service
=============================================
Crossdata has a Scala/Java API driver to allow to make queries programmatically on your own projects. Before do it,
please see the `Configuration document <3_configuration.rst>_`

This kind of execution is necessary when Crossdata need to be connected with an ODBC or an external software that
uses the Crossdata Driver.

Run Crossdata Server using maven::

    > mvn exec:java -pl crossdata-server -Dexec.mainClass="com.stratio.crossdata.server.CrossdataApplication"

Or it is possible to start using the server jar generated previously and server configuration file::

    > java -cp crossdata-server-<version>.jar com.stratio.crossdata.server.CrossdataApplication -Dcrossdata-server.external.config.filename=[path]/server-application.conf

Now that Crossdata server is running you can use the Crossdata driver importing the jar in your own project.

Next Steps
===========
Now Crossdata is running and it is possible to start executing different queries. Please see `Using Crossdata
Section<4_using_crossdata>_` to know how exactly use Crossdata.

