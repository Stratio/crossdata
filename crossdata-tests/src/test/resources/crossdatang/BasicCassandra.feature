@PrepareCasandraEnvironment
@CleanCasandraEnvironment
Feature: Select Cassandra Feature
  In order to test XDContext connection to Cassandra
  As a Spark User
  I want to execute Select functions

  Scenario: Basic Select
    Given a XDContext instance with this createTable query "CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra OPTIONS ( keyspace 'highschool', table 'students',  cluster 'Test Cluster',  pushdown 'true',  spark_cassandra_connection_host '127.0.0.1')"
    When I query "SELECT comment as b FROM students WHERE id = 1"
    Then the xdContext return 1 rows;

  Scenario: Basic Select *
    Given a XDContext instance with this createTable query "CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra OPTIONS ( keyspace 'highschool', table 'students',  cluster 'Test Cluster',  pushdown 'true',  spark_cassandra_connection_host '127.0.0.1')"
    When I query "SELECT * FROM students"
    Then the xdContext return 10 rows;

  Scenario: Basic Select Where In
    Given a XDContext instance with this createTable query "CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra OPTIONS ( keyspace 'highschool', table 'students',  cluster 'Test Cluster',  pushdown 'true',  spark_cassandra_connection_host '127.0.0.1')"
    When I query "SELECT comment as b FROM students WHERE id IN(1,2,3,4) limit 2"
    Then the xdContext return 2 rows;

  Scenario: Basic Select Where with to clause
    Given a XDContext instance with this createTable query "CREATE TEMPORARY TABLE students USING com.stratio.crossdata.sql.sources.cassandra OPTIONS ( keyspace 'highschool', table 'students',  cluster 'Test Cluster',  pushdown 'true',  spark_cassandra_connection_host '127.0.0.1')"
    When I query "SELECT comment as b FROM students WHERE comment = 1 AND id = 5"
    Then the xdContext return 1 rows;
