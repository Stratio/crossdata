Feature: [CROSSDATA-41]Persistent catalog in MYSQL

  Background:
    Given I execute a jdbc select 'TRUNCATE TABLE crossdataTables'

  Scenario: Perist a simple mongo table in MYSQL
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Perist the same mongo table in MYSQL(With catalog)
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    Then an exception 'IS' thrown
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables


  Scenario: Perist a simple mongo table in MYSQL(With catalog)
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    Then I execute a jdbc select 'TRUNCATE TABLE crossdataTables'
    And Drop the spark tables

  Scenario: Perist a simple mongo table in MYSQL(With catalog) and other table(Without catalog)
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then The result has to be '2'
    And Drop the spark tables

  Scenario: Perist a simple cassandra table in MYSQL
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.cassandra' AND options='{"spark_cassandra_connection_host":"127.0.0.1","cluster":"newCluster","pushdown":"true","keyspace":"newCatalog","table":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And Drop the spark tables

  Scenario: Perist the same cassandra table in MYSQL(With catalog)
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    Then an exception 'IS' thrown
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.cassandra' AND options='{"spark_cassandra_connection_host":"127.0.0.1","cluster":"newCluster","pushdown":"true","keyspace":"newCatalog","table":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And Drop the spark tables

  Scenario: Perist a simple cassandra table in MYSQL(With catalog)
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.cassandra' AND options='{"spark_cassandra_connection_host":"127.0.0.1","cluster":"newCluster","pushdown":"true","keyspace":"newCatalog","table":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And Drop the spark tables


  Scenario: Perist a simple cassandra table in MYSQL(With catalog) and other table(Without catalog)
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.cassandra' AND options='{"spark_cassandra_connection_host":"127.0.0.1","cluster":"newCluster","pushdown":"true","keyspace":"newCatalog","table":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.cassandra' AND options='{"spark_cassandra_connection_host":"127.0.0.1","cluster":"newCluster","pushdown":"true","keyspace":"newCatalog","table":"newTable"}''
    Then The result has to be '2'
    And Drop the spark tables

  Scenario: Perist a simple cassandra table in MYSQL
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    Given I execute 'CREATE TEMPORARY TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.cassandra' AND options='{"spark_cassandra_connection_host":"127.0.0.1","cluster":"newCluster","pushdown":"true","keyspace":"newCatalog","table":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And Drop the spark tables


  Scenario: Perist a simple cassandra table in MYSQL
    Given I execute 'CREATE TEMPORARY TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.cassandra OPTIONS (table 'newTable',keyspace 'newCatalog',cluster 'newCluster',pushdown "true",spark_cassandra_connection_host '127.0.0.1')'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.cassandra' AND options='{"spark_cassandra_connection_host":"127.0.0.1","cluster":"newCluster","pushdown":"true","keyspace":"newCatalog","table":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And Drop the spark tables

  Scenario: Perist a simple mongo table in MYSQL(Without catalog) and execute a select after
    Given I execute 'CREATE TABLE tabletest(ident int, name string, money double, new boolean, date date) USING com.stratio.crossdata.connector.mongodb  OPTIONS (host '127.0.0.1:27017', database 'databasetest',collection 'tabletest')'
    When I execute 'SELECT * FROM tabletest'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |
    And Drop the spark tables

  Scenario: Perist a simple mongo table in MYSQL(Without catalog) and execute a select after(diferent table names)
    Given I execute 'CREATE TABLE newTable (ident int, name string, money double, new boolean, date date) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'databasetest',collection 'tabletest')'
    When I execute 'SELECT * FROM newTable'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |
    And Drop the spark tables

  Scenario: Perist a simple mongo table in MYSQL(Without catalog) and execute a select after(with XD catalog equals to mongo db database)
    Given I execute 'CREATE TABLE databasetest.tabletest (ident int, name string, money double, new boolean, date date) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'databasetest',collection 'tabletest')'
    When I execute 'SELECT * FROM databasetest.tabletest'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |
    And Drop the spark tables

  Scenario: Perist a simple mongo table in MYSQL(Without catalog) and execute a select after(with XD catalog equals to mongo db database)
    Given I execute 'CREATE TABLE databasetest.newTable (ident int, name string, money double, new boolean, date date) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'databasetest',collection 'tabletest')'
    When I execute 'SELECT * FROM databasetest.newTable'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |
    And Drop the spark tables

  Scenario: Perist a simple mongo table in MYSQL(Without catalog) and execute a select after(with XD catalog equals to mongo db database)
    Given I execute 'CREATE TABLE database.newTable (ident int, name string, money double, new boolean, date date) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'databasetest',collection 'tabletest')'
    When I execute 'SELECT * FROM database.newTable'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |
    And Drop the spark tables

  Scenario: Perist a simple mongo table in MYSQL(Without catalog) and execute a select after(with XD catalog equals to mongo db database)
    Given I execute 'CREATE TABLE database.newTable (ident int, name string, money double, new boolean, date date) USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'databasetest',collection 'tabletest')'
    When I execute 'SELECT * FROM database.newTable'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |
    And Drop the spark tables