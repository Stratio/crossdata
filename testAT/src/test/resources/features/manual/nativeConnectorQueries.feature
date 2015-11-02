@ignore @unimplemented
Feature: Test

  Scenario: Test operations for native Cassandra Connector
  	# Attach cluster
    When I execute a ATTACH CLUSTER query for 'TestCluster', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
    Then The result of ATTACH CLUSTER query is 'false'
    
    # Add native connector
    When I execute a ADD_CONNECTOR query with this options: 'CassandraConnector.xml'
	Then The result of add new connector query is 'false'
	
	# Attach native connector
	When I execute a ATTACH CONNECTOR query  for a connector:
		|	Connector		|	ConnectorName		| 	ClusterName			| 	Options						|
	 	|	Cassandra		|	CassandraConnector	|	TestCluster	|	{'DefaultLimit': '1000'}	|
	Then The result of ATTACH CONNECTOR query is 'false'
	
	# Perform query with non-valid command
	When I execute a query: 'SELECT * FROM tableTest ORDER BY age;'
	Then the result has to be:
	
	# Perform query with valid command
	When I execute a query: 'SELECT * FROM tableTest'
	Then the result has to be:
		| catalogTest.tableTest.id-id-Integer | catalogTest.tableTest.name-name-String | catalogTest.tableTest.age-age-Integer | catalogTest.tableTest.phone-phone-BigInteger | catalogTest.tableTest.salary-salary-Double | catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
    	| 1                                   | name_1                                 | 10                                    | 10000000                                     | 1111.11                                    | 11.11                                   | true                                  |
    	| 2                                   | name_2                                 | 20                                    | 20000000                                     | 2222.22                                    | 12.11                                   | false                                 |
      