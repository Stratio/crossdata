@PrepareCasandraEnvironment
@CleanCasandraEnvironment
Feature: Select Cassandra Feature
  In order to test the XDContext fordware to Spark
  As a Spark User
  I want to execute Select with joins

  Background:
    Given a DATASOURCE_HOST in the System Variable "CassandraHost"


  Scenario Outline: Basic Join Select
    Given a table "students" with the provider "com.stratio.crossdata.sql.sources.cassandra" and options "keyspace 'highschool', table 'students',  cluster 'Test Cluster',  pushdown 'true',  spark_cassandra_connection_host '%DATASOURCE_HOST'"
    And a table "class" with the provider "com.stratio.crossdata.sql.sources.cassandra" and options "keyspace 'highschool', table 'class',  cluster 'Test Cluster',  pushdown 'true',  spark_cassandra_connection_host '%DATASOURCE_HOST'"
    When I query <query>
    Then the xdContext return <result> rows;

  Examples:
    |query                                                            |result |
    |"SELECT students.name, class.class_name FROM students inner join class ON (students.id = class.student_id)"                    |  10   |
