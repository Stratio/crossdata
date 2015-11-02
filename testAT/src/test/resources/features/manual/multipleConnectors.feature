@ignore @unimplemented
Feature: Test

  Scenario: Test attaching/detaching conectors
	# Attach cluster
    When I execute a ATTACH CLUSTER query for 'TestCluster2', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
    Then The result of ATTACH CLUSTER query is 'false'
    
    # Add native connector
    When I execute a ADD_CONNECTOR query with this options: 'SparkSQLConnector.xml'
	Then The result of add new connector query is 'false'
	
	# Attach native connector
	When I execute a ATTACH CONNECTOR query  for a connector:
		|	Connector		|	ConnectorName		| 	ClusterName			| 	Options						|
	 	|	Cassandra		|	SparkSQLConnector	|	TestCluster2		|	{'DefaultLimit': '1000'}	|
	Then The result of ATTACH CONNECTOR query is 'false'
	
	# Kill connector
	Given I kill connector
	When I execute a ATTACH CONNECTOR query  for a connector:
		|	Connector		|	ConnectorName		| 	ClusterName			| 	Options						|
	 	|	Cassandra		|	SparkSQLConnector	|	TestCluster2		|	{'DefaultLimit': '1000'}	|
	And I run command I execute a query: 'SELECT * FROM tableTest ORDER BY age;'
	Then the result has to be:
		# VALID RESULT
		