Feature: [CROSSDATA-86, CROSSDATA-167]Import tables from persistence

  Background:
    Given I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple mongo table in MYSQL
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017',schema_samplingRatio  '0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple mongo table in MYSQL
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017',schema_samplingRatio  '0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then The result has to have '8' rows
      |tableIdentifier-string|
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple cassandra table in MYSQL
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (cluster "Test Cluster", spark_cassandra_connection_host '127.0.0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '6'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple cassandra table and the same table from Mongo
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (cluster "Test Cluster", spark_cassandra_connection_host '127.0.0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017',schema_samplingRatio  '0.1')'
    Then an exception 'IS' thrown
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple Mongo table and the same table from cassandra
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017',schema_samplingRatio  '0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (cluster "Test Cluster", spark_cassandra_connection_host '127.0.0.1')'
    Then an exception 'IS' thrown
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables