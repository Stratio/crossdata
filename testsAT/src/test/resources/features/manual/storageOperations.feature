@ignore @unimplemented
Feature: Test crossdata shell storage operations

  Background:
	Given I run the shell command "ATTACH CLUSTER testCluster ON DATASTORE Cassandra WITH OPTIONS {'Hosts': '[172.31.13.46,172.31.15.233,172.31.5.190]', 'Port': 9042};"
	Then I expect a 'Cluster attached successfully' message
	Given I run the shell command "ATTACH CONNECTOR CassandraConnector TO testCluster WITH OPTIONS {'DefaultLimit': '1000'};"
	Then I expect a 'Connected to cluster successfully' message

  # INSERT
  Scenario: Insert empty table
  	Given I run the shell command "INSERT INTO VALUES(2,'pepe');"
    Then I expect a 'Parser exception' message

  Scenario: Insert row in non-existing table
  	Given I run the shell command "INSERT INTO invalidTable VALUES(2,'pepe');"
    Then I expect a 'Parser exception' message

  Scenario: Insert row with no values
	Given I run the shell command "INSERT INTO myTable(id,name) VALUES;"
    Then I expect a 'Parser exception' message

  Scenario: Insert row with less columns than expected
	Given I run the shell command "INSERT INTO myTable(id,name) VALUES(1);"
    Then I expect a 'Error recognized: Unknown parser error: Number of columns and number of values differ' message

  Scenario: Insert row with less columns than expected
	Given I run the shell command "INSERT INTO myTable(id) VALUES(1,'pepe');"
	Then I expect a 'Error recognized: Unknown parser error: Number of columns and number of values differ' message
  
  Scenario: Insert row with more columns than expected
	Given I run the shell command "INSERT INTO myTable(id,name,value) VALUES(1,'pepe','value1');"
	Then I expect a 'bugFestCatalog.myTable.name doesn't match data type' message

  Scenario: Insert row
	Given I run the shell command "INSERT INTO myTable(id,name) VALUES(2,'paco');"
	Then I expect a 'STORED successfully' message

  Scenario: [CROSSDATA-104] Insert row IF NOT EXISTS
	Given I run the shell command "INSERT INTO myTable(id,name) VALUES(2,'pacopaco') IF NOT EXISTS;"
	Then I expect a 'Error Message' messag
  
  
  # DELETE_<column_type>_<relationship>
  Scenario: Delete row
	Given I run the shell command "DELETE FROM myTable WHERE id = 4;"
	Then I expect a 'STORED successfully' message

  Scenario: Delete row with empty table
	Given I run the shell command "DELETE FROM WHERE id = 3;"
	Then I expect a 'Parser exception' message
  
  Scenario: Delete row from non-existing table
  	Given I run the shell command "DELETE FROM invalidTable WHERE id = 3;"
	Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message

  Scenario: Delete row with non-existing column_type
	Given I run the shell command "DELETE FROM myTable WHERE invalidColumn = 3;"
	Then I expect a 'There is no any attached connector supporting: [DELETE_NON_INDEXED_EQ]' message
  
  Scenario: Delete row with non-existing relation
	Given I run the shell command "DELETE FROM myTable WHERE id > 2;"
	Then I expect a 'There is no any attached connector supporting: [DELETE_PK_GT]' message
  
  Scenario: [CROSSDATA-105] Delete column
	Given I run the shell command "DELETE name FROM myTable;"
	Then I expect a 'OK' message
  
  Scenario: Delete row with empty relation
  	Given I run the shell command "DELETE FROM myTable WHERE id > 2;"
	Then I expect a 'Parser exception' message
  
  # UPDATE_<column_type>_<relationship>
  Scenario: Update row
  	Given I run the shell command "UPDATE myTable SET name = 'jose' WHERE id = 1;"
	Then I expect a 'STORED successfully' message

  Scenario: Update row from empty table
  	Given I run the shell command "UPDATE SET name = 'jose' WHERE id = 1;"
	Then I expect a 'Parser exception' message

  Scenario: Update row from non-existing table
  	Given I run the shell command "UPDATE invalidTable SET name = 'jose' WHERE id = 1;"
	Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message

  Scenario: Update row with non-existing column
	Given I run the shell command "UPDATE myTable SET invalidColumn = 'jose' WHERE id = 1;"
	Then I expect a 'Unknown identifier invalidColumn' message

  Scenario: Update row with empty column
	Given I run the shell command "UPDATE myTable SET WHERE id = 1;"
	Then I expect a 'Parser exception' message
  
  Scenario: Update row with empty where
  	Given I run the shell command "UPDATE myTable SET name = 'jose' WHERE;"
	Then I expect a 'There is no any attached connector supporting:[UPDATE_NO_FILTERS]' message

  # TRUNCATE_TABLE
  Scenario: Truncate empty table
	Given I run the shell command "TRUNCATE;"
	Then I expect a 'Parser exception' message

  Scenario: Truncate non-existing table
	  Given I run the shell command "TRUNCATE invalidTable;"
	  Then I expect a '[bugFestCatalog.invalidTable]  doesn't exist yet' message

  Scenario: Truncate table
	Given I run the shell command "TRUNCATE myTable;"
	Then I expect a 'STORED successfully' message