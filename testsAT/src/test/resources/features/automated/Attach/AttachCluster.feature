Feature: Attach CLUSTER feature
	
	Scenario Outline: Attach cluster. One per datastore.
		Given I have to execute the reset metadata query
	 	When I execute a ADD_DATASTORE query with this options: '<Path>'
	 	Then The result of add new datastore query is '<Result>'
	 	When I execute a ATTACH CLUSTER query for '<ClusterName>', '<DatastoreName>' and '<Options>'
	 	Then The result of ATTACH CLUSTER query is '<Result>'  
	 	When I want to know the description of a cluster '<ClusterName>'
	 	Then The expected result of describe cluster is: '<ClusterName>','<DatastoreName>' and '<Options>'
	 	When I execute a DETACH_CLUSTER query over '<ClusterName>'
	 	Then The result of detach a cluster is '<Result>'
	 	When I execute a DROP_DATASTORE query of the datastore '<DatastoreName>'
	 	Then The result of drop a datastore is '<Result>'
	 	 	
	 	Examples:
	 	
	 	|	Path						|	Result	| ClusterName	|	DatastoreName	|	Options									|	Result	|
		|	CassandraDataStore.xml		|	false	|	TestCluster	|	Cassandra		|	{'Hosts': '[127.0.0.1]', 'Port': 9042}	|	false	|
		|	ElasticSearchDataStore.xml	|	false	|	TestCluster	|	elasticsearch	|	{'Hosts': '127.0.0.1', 'Native Ports': 9300, 'Restful Ports' :  9200, 'Cluster Name': 'Test'}	|	false	|
		|	MongoDataStore.xml			|	false	|	TestCluster	|	Mongo			|	{'Hosts': '127.0.0.1', 'Port': 27017}	|	false	|
		|	StreamingDataStore.xml		|	false	|	TestCluster	|	Streaming		|	{'KafkaServer': '127.0.0.1', 'KafkaPort': 9093, 'zooKeeperServer' : '127.0.0.1', 'zooKeeperPort' : 2222}	| false	|
	
	Scenario: Attach two clusters to the same datastore
		Given I have to execute the reset metadata query 
		When I execute a ADD_DATASTORE query with this options: 'CassandraDataStore.xml'
	 	Then The result of add new datastore query is 'false'
	 	When I execute a ATTACH CLUSTER query for 'TestCluster', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
	 	Then The result of ATTACH CLUSTER query is 'false' 
	 	When I execute a ATTACH CLUSTER query for 'TestClusterTwo', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
	 	Then The result of ATTACH CLUSTER query is 'false'
	 	When I execute a DETACH_CLUSTER query over 'TestCluster'
 	    Then The result of detach a cluster is 'false'
	 	When I execute a DETACH_CLUSTER query over 'TestClusterTwo'
 	    Then The result of detach a cluster is 'false'
	 	When I execute a DROP_DATASTORE query of the datastore 'Cassandra'
	 	Then The result of drop a datastore is 'false'
    @ignore
	Scenario: Attach the same clusters to different datastores
		Given I have to execute the reset metadata query 
		When I execute a ADD_DATASTORE query with this options: 'CassandraDataStore.xml'
	 	Then The result of add new datastore query is 'false'
	 	When I execute a ADD_DATASTORE query with this options: 'ElasticSearchDataStore.xml'
	 	Then The result of add new datastore query is 'false'
	 	When I execute a ATTACH CLUSTER query for 'TestCluster', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
	 	Then The result of ATTACH CLUSTER query is 'false' 
	 	When I execute a ATTACH CLUSTER query for 'TestCluster', 'elasticsearch' and '{'Hosts': '127.0.0.1', 'Port': 9300}'
	 	Then an exception 'IS' thrown
	 	When I execute a DETACH_CLUSTER query over 'TestCluster'
	 	Then The result of detach a cluster is 'false'
	 	When I execute a DROP_DATASTORE query of the datastore 'Cassandra'
	 	Then The result of drop a datastore is 'false'
	 	When I execute a DROP_DATASTORE query of the datastore 'elasticsearch'
	 	Then The result of drop a datastore is 'false'
		
	#Scenario: Attach cluster without datastore
	#	Given I have to execute the reset metadata query 
	#	When I execute a ATTACH CLUSTER query for 'TestCluster', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
	#	Then an exception 'IS' thrown
	
	Scenario: Attach cluster to a datastore with other options
		Given I have to execute the reset metadata query 
		When I execute a ADD_DATASTORE query with this options: 'CassandraDataStore.xml'
	 	Then The result of add new datastore query is 'false'
		When I execute a ATTACH CLUSTER query for 'TestCluster', 'Cassandra' and '{'KafkaServer': '127.0.0.1', 'KafkaPort': 9093, 'zooKeeperServer' : '127.0.0.1', 'zooKeeperPort' : 2222}'
		Then an exception 'IS' thrown
		When I execute a DROP_DATASTORE query of the datastore 'Cassandra'
	 	Then The result of drop a datastore is 'false'
	
	Scenario: Attach two clusters with the same name to the same datastore
		Given I have to execute the reset metadata query 
		When I execute a ADD_DATASTORE query with this options: 'CassandraDataStore.xml'
	 	Then The result of add new datastore query is 'false'
	 	When I execute a ATTACH CLUSTER query for 'TestCluster', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
	 	Then The result of ATTACH CLUSTER query is 'false' 
	 	When I execute a ATTACH CLUSTER query for 'TestCluster', 'Cassandra' and '{'Hosts': '[127.0.0.1]', 'Port': 9042}'
	 	Then an exception 'IS' thrown
	 	When I execute a DETACH_CLUSTER query over 'TestCluster'
	 	Then The result of detach a cluster is 'false'
	 	When I execute a DROP_DATASTORE query of the datastore 'Cassandra'
	 	Then The result of drop a datastore is 'false'
	 
