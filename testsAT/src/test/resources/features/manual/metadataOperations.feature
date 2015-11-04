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
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id int primary key);"
    Then I expect a 'TABLE created successfully' message

  Scenario: Create same table
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER bugFestCluster (id int primary key);"
    Then I expect a '[bugFestCatalog.myTable] exists already' message
  
  # ALTER TABLE
  Scenario: Alter non-existing table
  
  Scenario: Alter table
  

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
  Scenario: Create index with non-existing column
  
  Scenario: Create index
  
  
  # DROP INDEX
  Scenario: Drop non-existing index
  
  Scenario: Drop index