@ignore @unimplemented
Feature: Test crossdata shell storage operations

  Background:
  	Given I run command ATTACH CLUSTER 'testCluster' ON DATASTORE 'Cassandra' WITH OPTIONS "{'Hosts': '[${HOSTS}]', 'Port': ${PORT}};""
	And I run command ADD CONNECTOR ${CONNECTOR_MANIFEST_PATH}'
	And I run command

  # INSERT
  	Scenario: Insert row in non-existing table
  
  	Scenario: Insert row
  
  	Scenario: Insert empty row
  
  	Scenario: Insert row with less columns than expected
  
  	Scenario: Insert row with more columns than expected
  
  	
  # INSERT_IF_NOT_EXISTS
  	Scenario: Insert row in non-existing table
  
  	Scenario: Insert non-existing row
  
  	Scenario: Insert existing row 
  
  	Scenario: Insert empty row
  
  	Scenario: Insert row with less columns than expected
  
  	Scenario: Insert row with more columns than expected
  
  
  # DELETE_<column_type>_<relationship>
  	Scenario: Delete row
  
  	Scenario: Delete row from non-existing table
  
  	Scenario: Delete row with non-existing column_type
  
  	Scenario: Delete row with non-existing relation
  
  	Scenario: Delete row with empty column_type
  
  	Scenario: Delete row with empty relation
  
  
  # UPDATE_<column_type>_<relationship>
    Scenario: Update row
  
  	Scenario: Update row from non-existing table
  
  	Scenario: Update row with non-existing column_type
  
  	Scenario: Update row with non-existing relation
  
  	Scenario: Update row with empty column_type
  	
  	Scenario: Update row with empty relation
  
  
  # TRUNCATE_TABLE
  	Scenario: Truncate table
  	
  	Scenario: Truncate non-existing table
