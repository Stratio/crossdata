Feature: [CROSSDATA-378]Drop Tables

  Background:
    Given Drop the spark tables

  Scenario: Drop a persisted cassandra table in the catalog
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'tabletest',keyspace 'databasetest',cluster '${CASSANDRA_CLUSTER}',pushdown "true",spark_cassandra_connection_host '${CASSANDRA_HOST}')'
    Then an exception 'IS NOT' thrown
    Then I execute 'DROP TABLE newTable'
    Then an exception 'IS NOT' thrown
    And Drop the spark tables

  Scenario: Drop a temporary cassandra table
    Given I execute 'CREATE TEMPORARY TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'tabletest',keyspace 'databasetest',cluster '${CASSANDRA_CLUSTER}',pushdown "true",spark_cassandra_connection_host '${CASSANDRA_HOST}')'
    Then an exception 'IS NOT' thrown
    Then I execute 'DROP TABLE newTable'
    Then an exception 'IS NOT' thrown
    And Drop the spark tables

  Scenario: Drop a table that not exists
    Then I execute 'DROP TABLE newTable'
    Then an exception 'IS' thrown
    And Drop the spark tables