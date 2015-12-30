Feature: [CROSSDATA-174]Import tables using api from persistence

  Background:
    Given I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple mongo table in catalog
    When I import tables using api for 'com.stratio.crossdata.connector.mongodb'
      |host                  | 127.0.0.1:27017 |
      |schema_samplingRatio  | 0.1             |
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.mongodb''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple cassandra table in MYSQL using api
    When I import tables using api for 'com.stratio.crossdata.connector.cassandra'
      |cluster                          | Test Cluster          |
      |spark_cassandra_connection_host  | 127.0.0.1            |
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND datasource='com.stratio.crossdata.connector.cassandra''
    Then an exception 'IS NOT' thrown
    Then The result has to be '6'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Import a simple ES table using api
    When I import tables using api for 'com.stratio.crossdata.connector.elasticsearch'
      |resource                           | databasetest/tabletest  |
      | es.nodes                          | 127.0.0.1               |
      |es.port                            | 9200                    |
      |es.nativePort                      | 9300                    |
      |es.cluster                         | elasticsearchHugo       |
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db = 'databasetest' AND tableName='tabletest' AND datasource='com.stratio.crossdata.connector.elasticsearch''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables