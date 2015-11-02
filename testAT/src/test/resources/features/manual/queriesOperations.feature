@ignore @unimplemented
Feature: Test crossdata shell queries operations

  Background:
  	Given I run command ATTACH CLUSTER 'testCluster' ON DATASTORE 'Cassandra' WITH OPTIONS "{'Hosts': '[${HOSTS}]', 'Port': ${PORT}};""
	And I run command ADD CONNECTOR ${CONNECTOR_MANIFEST_PATH}'
	And I run command
	
  # PROJECT
  	Scenario: Retrieve columns
  	
  	Scenario: Retrieve non-existing columns
  	
  	Scenario: Retrieve no columns
  	
  
  # SELECT_OPERATOR
  	Scenario: Retrieve columns
  	
  	Scenario: Retrieve non-existing columns
  	
  	Scenario: Retrieve no columns
	
	
  # SELECT_WINDOW
  	Scenario: 
  
  # SELECT LIMIT
  	Scenario: Select with negative limit
  	
  	Scenario: Select with valid limit
  
  # SELECT_<join_type>_JOIN
  	Scenario: Select with invalid join type
  	
  	Scenario: Select with valid join type
  
  # SELECT_<join_type>_JOIN_PARTIAL_RESULTS
    Scenario: Select with invalid join type
  	
  	Scenario: Select with valid join type
  	
  # SELECT_ORDER_BY
  	Scenario: Non-existing column
  	
  	Scenario: Existing column
  
  # SELECT_GROUP_BY
  	Scenario: Non-existing column
  
  	Scenario: Existing column
  	
  # SELECT_CASE_WHEN
  	Scenario: Select
  
  # SELECT_FUNCTIONS
  	Scenario: Select
  
  # SELECT_SUBQUERY
  	Scenario: Select
  
  # FILTER_<column_type>_<relationship>
  
    Scenario: Filter
  
  	Scenario: Filter from non-existing table
  
  	Scenario: Filter with non-existing column_type
  
  	Scenario: Filter with non-existing relation
  
  	Scenario: Filter with empty column_type
  	
  	Scenario: Filter with empty relation