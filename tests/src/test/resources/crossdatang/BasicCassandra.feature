@PrepareCasandraEnvironment
@CleanCasandraEnvironment
Feature: Select Cassandra Feature
  In order to test XDContext connection to Cassandra
  As a Spark User
  I want to execute Select functions

    Background:
      Given a DATASOURCE_HOST in the System Variable "CassandraHost"

    Scenario Outline: Basic Select
      Given a table "students" with the provider "com.stratio.crossdata.connector.cassandra" and options "keyspace 'highschool', table 'students',  cluster 'Test Cluster',  pushdown 'true',  spark_cassandra_connection_host '%DATASOURCE_HOST'"
      When I query <query>
      Then the xdContext return <result> rows;

      Examples:
      |query                                                            |result |
      |"SELECT comment as b FROM students WHERE id = 1"                 |  1    |
      |"SELECT * FROM students"                                         |  10   |
      |"SELECT comment as b FROM students WHERE id IN(1,2,3,4) limit 2" |  2    |
      |"SELECT comment as b FROM students WHERE comment = 1 AND id = 5" |  0    |
