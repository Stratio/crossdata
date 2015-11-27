Feature: [CROSSDATA-166]Drop table

  Background:
    Given I execute a jdbc select 'TRUNCATE TABLE crossdataTables'

  Scenario: Drop a simple mongo table in MYSQL
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And I execute 'DROP TABLE newTable'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '0'

  Scenario: Drop the same mongo table in MYSQL(With catalog)
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    Then an exception 'IS' thrown
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And I execute 'DROP TABLE newCatalog.newTable'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '0'


  Scenario: Drop a simple mongo table in MYSQL(With catalog)
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    And I execute 'DROP TABLE newCatalog.newTable'
    When I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '0'

  Scenario: Drop a simple mongo table in MYSQL(With catalog) and other table(Without catalog)
    Given I execute 'CREATE TABLE newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    Given I execute 'CREATE TABLE newCatalog.newTable USING com.stratio.crossdata.connector.mongodb OPTIONS (host '127.0.0.1:27017', database 'any',collection 'newTable')'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE db='newCatalog' AND tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then an exception 'IS NOT' thrown
    Then The result has to be '1'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then The result has to be '2'
    And I execute 'DROP TABLE newCatalog.newTable'
    And I execute 'DROP TABLE newTable'
    When  I execute a jdbc select 'SELECT count(*) FROM crossdataTables WHERE tableName='newTable' AND tableSchema ='{"type":"struct","fields":[]}' AND datasource='com.stratio.crossdata.connector.mongodb' AND options='{"host":"127.0.0.1:27017","database":"any","collection":"newTable"}''
    Then The result has to be '0'
