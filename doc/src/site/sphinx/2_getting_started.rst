===============
Getting started
===============

Compiling Crossdata

    > mvn clean install -Pcrossdata-all

If you prefer to install solely some connectors::

    > mvn clean install -Pcrossdata-cassandra

You can build a Spark Distribution with Crossdata libraries running the make-distribution-crossdata script:
    > cd crossdata-scripts
    > ./make-distribution-crossdata.sh

This will build Spark with the following options:
    - Crossdata with Cassandra support
    - Spark Version v1.5.0
    - Spark's Hadoop  Version 2.4.0
    - Yarn support
    - Hive integration for SparkSQL
     -Scala version 2.10

For others options run ./make-distribution-crossdata.sh --help

Using a Crossdata's Spark Distribution with cassandra support:
    > bin/stratio-xd-shell --cassandra

Then you can do:

    >xdContext.sql("CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra
            OPTIONS (keyspace \"highschool\", table \"students\", cluster \"students\", pushdown \"true\",
            spark_cassandra_connection_host \"127.0.0.1\")".stripMargin)
    >xdContext.sql("SELECT * FROM students").collect()


TODO:

See Crossdata examples.

How do I get started?
- What do I need before I start? System Requirements, What skills should I have, What should I know?
- Walk through an Example

