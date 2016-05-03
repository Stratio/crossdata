Feature: [CROSSDATA-86, CROSSDATA-167]Import tables from persistence

  Background:
    Given I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple mongo table in MYSQL
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_PORT}:${MONGO_HOST}',schema_samplingRatio  '0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple mongo table in MYSQL
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_PORT}:${MONGO_HOST}',schema_samplingRatio  '0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    #Then The result has to have '8' rows
    #  |tableIdentifier-array| ignored-boolean |
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple cassandra table in MYSQL
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (cluster "${CASSANDRA_CLUSTER}", spark_cassandra_connection_host '${CASSANDRA_HOST}')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '6'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: [CROSSDATA-86,CROSSDATA-189]Import a simple cassandra table and the same table from Mongo
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (cluster "${CASSANDRA_CLUSTER}", spark_cassandra_connection_host '${CASSANDRA_HOST}')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_PORT}:${MONGO_HOST}',schema_samplingRatio  '0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    #Then an exception 'IS' thrown
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: [CROSSDATA-86,CROSSDATA-189] Import a simple Mongo table and the same table from cassandra
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.mongodb OPTIONS (host ''${MONGO_PORT}:${MONGO_HOST}',schema_samplingRatio  '0.1')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.cassandra OPTIONS (cluster "${CASSANDRA_CLUSTER}", spark_cassandra_connection_host '${CASSANDRA_HOST}')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple ES table
    Given I execute 'IMPORT TABLES USING com.stratio.crossdata.connector.elasticsearch OPTIONS (resource 'databasetest/tabletest', es.nodes '${ES_NODES}', es.port '${ES_PORT}', es.nativePort '${ES_NATIVE_PORT}', es.cluster '${ES_CLUSTER}')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.elasticsearch''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables