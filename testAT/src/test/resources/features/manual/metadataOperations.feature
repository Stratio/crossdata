@ignore @unimplemented
Feature: Test crossdata shell metadata operations

  Background:
  	Given I run command ATTACH CLUSTER 'testCluster' ON DATASTORE 'Cassandra' WITH OPTIONS "{'Hosts': '[${HOSTS}]', 'Port': ${PORT}};""
	And I run command ADD CONNECTOR ${CONNECTOR_MANIFEST_PATH}'
	And I run command

  # CREATE CATALOG
  Scenario: Create catalog
  	Given I run command 'myCatalog'
  	Then result is 'Catalog created'
  
  Scenario: Create same catalog
  	Given I create catalog 'myCatalog'
  	Then result is 'Catalog already exists'
  	
  Scenario: Create catalog with empty name
  
  Scenario: Create catalog with name too long
  
  # ALTER CATALOG
  Scenario: Alter non-existing catalog
  
  Scenario: Alter catalog
  
  
  # DROP CATALOG
  Scenario: Drop non-existing catalog
  
  Scenario: Drop catalog
  
  
  # CREATE TABLE
  Scenario: Create table
  
  Scenario: Create same table
  
  
  # ALTER TABLE
  Scenario: Alter non-existing table
  
  Scenario: Alter table
  

  # DROP TABLE
  Scenario: Drop non-existing table
  
  Scenario: Drop table
  
  
  # CREATE INDEX
  Scenario: Create index with non-existing column
  
  Scenario: Create index
  
  
  # DROP INDEX
  Scenario: Drop non-existing index
  
  Scenario: Drop index