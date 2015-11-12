Getting started
***************

TODO

Prerequisites
=============

- **System requirements**: TODO

- **Other modules**:
    There are no strict requirements but in order to use external datasources you need to install them.
    You will need Apache Cassandra to run the examples described below.

- **Skills needed**: SQL knowledge

How to build Crossdata
======================
There are different ways to build Crossdata core and connectors depending of your necessities.

- **A) Using an external Spark installation**:

One way is using maven by running::

    > mvn clean package -Ppackage

and it could be packaged with hive dependencies too::

    > mvn clean package -Ppackage -Phive

This generates rpm and deb packages as well as several fat/uber jar:
    - Crossdata core
    - Mongo connector
    - Cassandra connector
    - Elasticsearch connector


- **B) Build a Spark distribution along with Crossdata**:

You can build a Spark Distribution with Crossdata libraries by running the make-distribution-crossdata script::

    > cd scripts
    > ./make-distribution-crossdata.sh

This will build Spark with the following options:
    - Spark-1.5.1/hadoop2.6
    - Crossdata shell
    - And all available Crossdata connectors

For others options run ./make-distribution-crossdata.sh --help


How to install Crossdata
========================

- **A) Using an external Spark installation**:

Copy the jars which you need to all spark nodes in your cluster $spark_home/lib
    - Crossdata core
    - Mongo connector
    - Cassandra connector
    - Elasticsearch connector

Use rpm and deb packages: TODO


- **B) Spark distribution along with Crossdata**:

Use rpm and deb packages: TODO
Download a prebuilt targz: TODO


Configure
=========

TODO
If you want to use Crossdata as a client-server service, please see the `Configuration document <3_configuration.rst>`_


Run Crossdata server (Crossdata as a client-server service)
===========================================================

If you don't need concurrent clients within a Spark cluster, skip this step.

Crossdata has a Scala/Java API driver to allow to make queries programmatically on your own projects. Before do it,
please see the `Configuration document <3_configuration.rst>`_

Run Crossdata Server using maven::

    > mvn exec:java -pl crossdata-server -Dexec.mainClass="com.stratio.crossdata.server.CrossdataApplication"

Or it is possible to start using the server jar generated previously and server configuration file::

    > java -cp crossdata-server-<version>.jar com.stratio.crossdata.server.CrossdataApplication -Dcrossdata-server.external.config.filename=[path]/server-application.conf

Now that Crossdata server is running you can use the Crossdata driver importing the jar in your own project.


Run Crossdata shell
===================

If you are using Crossdata core and connectors as a library, you have the following options:


a) Use the Crossdata shell if you haven't Spark installed previously.

To run Crossdata in this mode, it's necessary to install Crossdata using the make-distribution-crossdata script first.
Once Crossdata is installed, just run this::

    > bin/stratio-xd-shell --cassandra --mongodb

This example register the existent table "students" of the Cassandra keyspace "highschool" that has a few rows inserted. Once the table is registered then execute a query by the native way::

    >xdContext.sql("CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra
            OPTIONS (keyspace 'highschool', table 'students', cluster 'students', pushdown 'true',
            spark_cassandra_connection_host '127.0.0.1')")
    >xdContext.sql("SELECT * FROM students").collect()


b) Use the Spark shell (you should have added the jars described above manually)::

    > $spark_home/bin/spark-shell
    > val xdContext = new XDContext(sc)
    > xdContext.sql("CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra
            OPTIONS (keyspace 'highschool', table 'students', cluster 'students', pushdown 'true',
            spark_cassandra_connection_host '127.0.0.1')")
    > xdContext.sql("SELECT * FROM students").collect()

c) Use the Spark shell and indicate the path of the fat jars::

    > $spark_home/bin/spark-shell --jars $jar_paths
    > val xdContext = new XDContext(sc)
    > xdContext.sql("CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra
            OPTIONS (keyspace 'highschool', table 'students', cluster 'students', pushdown 'true',
            spark_cassandra_connection_host '127.0.0.1')")
    > xdContext.sql("SELECT * FROM students").collect()

The above options do not use a Spark cluster. In order to connect to a cluster, you must `run a Spark Cluster <http://spark.apache.org/docs/latest/spark-standalone.html>`_
and specify the master as a shell option (--master spark://IP:PORT)

Next Steps
==========

More information check out the `Reference guide <6_reference guide.rst>`_
Scala driver exaw the modules it depends on are installed and configured. For example, how would you configure GoSec as it pertains to Crossdata, its benefits, etc.mples in `github <https://github.com/Stratio/Crossdata/tree/master/examples/src/main/scala/com/stratio/crossdata/examples>`_