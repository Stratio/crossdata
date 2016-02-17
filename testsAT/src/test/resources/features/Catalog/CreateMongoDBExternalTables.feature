Feature: [CROSSDATA-251]Create External tables(MONGODB)
  Background:
  Given Drop the spark tables

  Scenario: [MONGODB EXTERNAL TABLE : CROSSDATA-270]Create a MongoDB collection(Not exists Database)
    Given I drop a mongoDB database 'externaldatabase'
    #  When I execute 'CREATE EXTERNAL TABLE externaldatabase.externalcollection (id INT, name STRING) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'externaldatabase', collection 'externalcollection')'
    When I execute 'CREATE EXTERNAL TABLE externaldatabase.externalcollection (id INT, name STRING) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'externaldatabase', collection 'externalcollection')'
    Then an exception 'IS NOT' thrown
    And The collection 'externalcollection' exists in mongo database 'externaldatabase'

  Scenario: [MONGODB EXTERNAL TABLE : CROSSDATA-270]Create a MongoDB table(Exists Database)
    Given I drop a mongoDB database 'externaldatabase'
    Given I create a mongoDB database 'externaldatabase'
#  When I execute 'CREATE EXTERNAL TABLE externaldatabase.externalcollection (id INT, name STRING) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'externaldatabase', collection 'externalcollection')'
    When I execute 'CREATE EXTERNAL TABLE externaldatabase.externalcollection (id INT, name STRING) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'externaldatabase', collection 'externalcollection')'
    Then an exception 'IS NOT' thrown
    And The collection 'externalcollection' exists in mongo database 'externaldatabase'


  Scenario: [MONGODB EXTERNAL TABLE : CROSSDATA-270]Create a MongoDB collection(exists keyspace and exists table)
    Given I drop a mongoDB database 'externaldatabase'
    Given I create a mongoDB collection 'externalcollection' over database 'externaldatabase'
    #  When I execute 'CREATE EXTERNAL TABLE externaldatabase.externalcollection (id INT, name STRING) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '${MONGO_HOST}:${MONGO_PORT}', database 'externaldatabase', collection 'externalcollection')'
    When I execute 'CREATE EXTERNAL TABLE externaldatabase.externalcollection (id INT, name STRING) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'externaldatabase', collection 'externalcollection')'
    Then an exception 'IS' thrown

  Scenario Outline: [MONGODB EXTERNAL TABLE : CROSSDATA-270]Create a MongoDB collection(exists database)
    Given I drop a mongoDB database 'externaldatabase'
    Given I create a mongoDB database 'externaldatabase'
    When I execute 'CREATE EXTERNAL TABLE externaldatabase.externalcollection (id INT, name <type>) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'externaldatabase', collection 'externalcollection')'
    Then an exception 'IS NOT' thrown
    And The collection 'externalcollection' exists in mongo database 'externaldatabase'
    Examples:
      | type                  |
      | INT                   |
      | LONG                  |
      | FLOAT                 |
      | DOUBLE                |
      | DECIMAL               |
      | STRING                |
      | BINARY                |
      | BOOLEAN               |
      | TIMESTAMP             |
      | ARRAY<STRING>         |
      | MAP<INT, INT>         |