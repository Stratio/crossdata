@ignore @unimplemented
Feature: Test crossdata shell metadata operations

  Background:
    Given I run the shell command "ATTACH CLUSTER testCluster ON DATASTORE Cassandra WITH OPTIONS {'Hosts': '[172.31.13.46,172.31.15.233,172.31.5.190]', 'Port': 9042};"
    Then I expect a 'Cluster attached successfully' message
    Given I run the shell command "ATTACH CONNECTOR CassandraConnector TO testCluster WITH OPTIONS {'DefaultLimit': '1000'};"
    Then I expect a 'Connected to cluster successfully' message

  # CREATE CATALOG
  Scenario: Create catalog with empty name
    Given I run the shell command "CREATE CATALOG;"
    Then I expect a 'Parser exception' message

  Scenario: Create catalog
    Given I run the shell command "CREATE CATALOG bugFestCatalog;"
    Then I expect a 'CATALOG created successfully' message

  Scenario: Create same catalog
    Given I run the shell command "CREATE CATALOG bugFestCatalog;"
    Then I expect a '[bugFestCatalog] exists already' message
  
  # ALTER CATALOG
  Scenario: Alter catalog with empty WITH
    Given I run the shell command "ALTER CATALOG bugFestCatalog WITH;"
    Then I expect a 'Parser exception' message

  Scenario: [CROSSDATA-95] Alter catalog with invalid WITH attributes in json
    Given I run the shell command "ALTER CATALOG bugFestCatalog WITH {'key1': 'value1'};"
    Then I expect a 'Parser exception' message

  Scenario: Alter non-existing catalog
    Given I run the shell command "ALTER CATALOG invalidCatalog WITH {'key1': 'value1'};"
    Then I expect a '[invalidCatalog]  doesn't exist yet' message


  # ERROR IN CATALOG FLOW
  Scenario: [CROSSDATA-95] No error is reported when altering the catalog or creating the table
    Given I run shell command "CREATE CATALOG myCatalog;"
    Then I expect a 'CATALOG created successfully' message
    Given I run shell command "USE myCatalog;"
    Then I expect success
    Given I run shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id int primary key);"
    Then I expect a 'TABLE created successfully' message
    Given I run shell command "ALTER CATALOG myCatalog with {'key1': 'value1'};"
    Then I expect a 'line 1:52 no viable alternative at input '}' (... "myCatalog" WITH key1 = {[value1]})' message

  Scenario: [CROSSDATA-95] Error should be reported when altering catalog, not after creating the first table
    Given I run shell command "CREATE CATALOG myCatalog2;"
    Then I expect a 'CATALOG created successfully' message
    Given I run shell command "USE myCatalog2;"
    Then I expect success
    Given I run shell command "ALTER CATALOG myCatalog2 with {'key1': 'value1'};"
    Then I expect a 'line 1:52 no viable alternative at input '}' (... "myCatalog2" WITH key1 = {[value1]})' message

  # DROP CATALOG
  Scenario: Drop non-existing catalog
    Given I run the shell command "DROP CATALOG invalidCatalog;"
    Then I expect a '[invalidCatalog]  doesn't exist yet' message
  
  Scenario: Drop catalog
    Given I run the shell command "DROP CATALOG bugFestCatalog;"
    Then I expect a 'CATALOG dropped successfully' message
  
  # CREATE TABLE
  Scenario: Create table with empty cluster
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER;"
    Then I expect a 'Parser exception' message

  Scenario: Create table with invalid cluster
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER myCluster (id int primary key);"
    Then I expect a '[cluster.myCluster]  doesn't exist yet' message

  Scenario: Create table without primary key
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id int);"
    Then I expect a 'Parser exception' message

  Scenario: Create table without type
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id primary key);"
    Then I expect a 'Parser exception' message

  Scenario: Create table
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id int primary key, name varchar);"
    Then I expect a 'TABLE created successfully' message

  Scenario: Create same table
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id int primary key, name varchar);"
    Then I expect a '[bugFestCatalog.myTable] exists already' message
  
  # ALTER TABLE - DROP
  Scenario: Alter table empty
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id int primary key, name varchar);"
    Then I expect a 'TABLE created successfully' message
    When I run the shell command "ALTER TABLE DROP id;"
    Then I expect a 'Parser exception' message

  Scenario: Alter non-existing table
    When I run the shell command "ALTER TABLE invalidTable DROP id;"
    Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message

  Scenario: Alter table non-existing identifier
    When I run the shell command "ALTER TABLE myTable DROP invalidID;"
    Then I expect a '[bugFestCatalog.myTable.invalidID]  doesn't exist yet' message

  Scenario: Alter table existing identifier
    When I run the shell command "ALTER TABLE myTable DROP name;"
    Then I expect a 'OK' message

   # ALTER TABLE - ADD
  Scenario: Alter table empty
    When I run the shell command "ALTER TABLE ADD name varchar;"
    Then I expect a 'Parser exception' message

  Scenario:  Alter non-existing table
    When I run the shell command "ALTER TABLE invalidTable ADD name varchar;"
    Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message

  Scenario: Alter table empty identifier
    When I run the shell command "ALTER TABLE myTable ADD;"
    Then I expect a 'Parser Exception' message

  Scenario: Alter table empty identifier type
    When I run the shell command "ALTER TABLE myTable ADD name;"
    Then I expect a 'Parser Exception' message

  Scenario: Alter table
    When I run the shell command "ALTER TABLE myTable ADD name varchar;"
    Then I expect a 'OK' message

  # ALTER TABLE - ALTER
  Scenario: Alter table empty
    When I run the shell command "ALTER TABLE ALTER name TYPE varchar;"
    Then I expect a 'Parser exception' message

  Scenario:  Alter non-existing table
    When I run the shell command "ALTER TABLE invalidTable ADD name varchar;"
    Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message

  Scenario: Alter table empty identifier
    When I run the shell command "ALTER TABLE myTable ALTER TYPE varchar;"
    Then I expect a '[bugFestCatalog.myTable.TYPE]  doesn't exist yet' message

  Scenario: Alter table empty identifier type
    When I run the shell command "ALTER TABLE myTable ALTER name TYPE;"
    Then I expect a 'Parser Exception' message

  Scenario: Alter table invalid identifier type
    When I run the shell command "ALTER TABLE myTable ALTER name TYPE string;"
    Then I expect a 'Unknown type bugFestCatalog.native' message

  Scenario: [CROSSDATA-96] Alter table with options
    When I run the shell command "ALTER TABLE myTable WITH comment = 'myComment';"
    Then I expect a 'OK' message

  # DROP TABLE
  Scenario: Drop non-existing table
    Given I run shell command "DROP TABLE invalidTable;"
    Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message
    Given I run shell command "DROP TABLE IF EXISTS invalidTable;"
    Then I expect a 'Query has been ignored because: [bugFestCatalog.invalidTable] doesn't exist' message

  Scenario: Drop table
    Given I run shell command "DROP TABLE invalidTable;"
    Then I expect a 'TABLE dropped successfully' message
    Given I run shell command "DROP TABLE IF EXISTS invalidTable;"
    Then I expect a 'TABLE dropped successfully' message
  
  # CREATE INDEX
  Scenario: Create index with invalid index type
    Given I run shell command "CREATE INVALID INDEX;"
    Then I expect a 'Parser exception' message

  Scenario: Create index with empty name
    Given I run shell command "CREATE INDEX ON myTable(name);"
    Then I expect a 'Parser exception' message

  Scenario: Create index with empty table
    Given I run shell command "CREATE INDEX myIndex ON;"
    Then I expect a 'Parser exception' message

  Scenario: Create index with non-existing table
    Given I run shell command "CREATE INDEX myIndex ON invalidTable(name);"
    Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message

  Scenario: Create index with empty column
    Given I run shell command "CREATE INDEX myIndex ON myTable();"
    Then I expect a 'Parser exception' message

  Scenario: Create index with non-existing column
    Given I run shell command "CREATE INDEX myIndex ON myTable(invalidColumn);"
    Then I expect a '[bugFestCatalog.myTable.invalidColumn]  doesn't exist yet' message

  Scenario: [CROSSDATA-97] Create default index with more than one column
    Given I run shell command "CREATE INDEX myIndex ON myTable(id, name);"
    Then I expect a 'Parser exception' message

  Scenario: Create index
    Given I run shell command "CREATE INDEX myIndex ON myTable(name);"
    Then I expect a 'INDEX created successfully' message

  Scenario: [CROSSDATA-98] Create index with USING
    Given I run shell command "CREATE INDEX myIndex ON myTable(name) USING 'org.apache.cassandra.db.index.SecondaryIndex';"
    Then I expect a 'INDEX created successfully' message

   Scenario: [CROSSDATA-99] Create index with WITH OPTIONS
    Given I run shell command "CREATE INDEX myIndex ON myTable(name) WITH OPTIONS {};"
    Then I expect a 'INDEX created successfully' message

  # CREATE LUCENE INDEX
  Scenario: [CROSSDATA-101] Create lucen index
    Given I run shell command "CREATE LUCENE INDEX myIndex3 ON myTable(id,name);"
    Then I expect a 'INDEX created successfully' message

  # DROP INDEX
  Scenario: Drop empty index
    Given I run shell command "DROP INDEX;"
    Then I expect a 'Parser exception' message

  Scenario: Drop non-existing index
    Given I run shell command "DROP INDEX invalidIndex;"
    Then I expect a '' message

  Scenario: [CROSSDATA-100] Drop index
    Given I run shell command "DROP INDEX myTable.myIndex;"
    Then I expect a 'INDEX dropped successfully' message


