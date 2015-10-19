Getting started
****************

How to build Crossdata
=========================
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


Running Crossdata standalone
=============================
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
please see the `Configuration document <3_configuration.rst>`_

Run Crossdata Server using maven::

    > mvn exec:java -pl crossdata-server -Dexec.mainClass="com.stratio.crossdata.server.CrossdataApplication"

Or it is possible to start using the server jar generated previously and server configuration file::

    > java -cp crossdata-server-<version>.jar com.stratio.crossdata.server.CrossdataApplication -Dcrossdata-server.external.config.filename=[path]/server-application.conf

Now that Crossdata server is running you can use the Crossdata driver importing the jar in your own project.
