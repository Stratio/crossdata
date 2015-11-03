Feature: ATTACH CONNECTOR feature

Scenario Outline: Attach connector. One per connector.
		#Given I have to execute the reset metadata query
	 	When I execute a ADD_DATASTORE query with this options: '<Path_Datastore>'
	 	Then The result of add new datastore query is '<Result>'
	 	When I execute a ATTACH CLUSTER query  for a connector:
	 		|	DatastoreName	|	ClusterName		| 	DatastoreName	| 	Options									|
	 		|	Cassandra		|	CassandraCluster|	Cassandra		|	{'Hosts': '[127.0.0.1]', 'Port': 9042}	|
	 		#|	Mongo			|	MongoCluster	|	Mongo			|	{'Hosts': '127.0.0.1', 'Port': 27017}	|
	 		#|	Elastic			|	ElasticCluster	|	elasticsearch	|	{'Hosts': '127.0.0.1', 'Port': 9300}	|
	 		#|	Streaming		|	StreamingCluster|	Streaming		|	{'KafkaServer': '127.0.0.1', 'KafkaPort': 9093, 'zooKeeperServer' : '127.0.0.1', 'zooKeeperPort' : 2222}	|
	 	Then The result of ATTACH CLUSTER query is 'false' 
	 	When I execute a ADD_CONNECTOR query with this options: '<Path_Connector>'
	 	Then The result of add new connector query is '<Result>'
	 	When I execute a ATTACH CONNECTOR query  for a connector:
	 		|	Connector		|	ConnectorName		| 	ClusterName			| 	Options						|
	 		|	Cassandra		|	CassandraConnector	|	CassandraCluster	|	{'DefaultLimit': '1000'}	|
	 	#	|	Mongo			|	MongoConnector		|	Mongo				|	NULL						|
	 	#	|	Elastic			|	elasticsearch		|	elasticsearch		|	{'node_type' : 	'Node'}		|
	 	#	|	Streaming		|	StreamingConnector	|	Streaming			|	NULL	|
	 	Then The result of ATTACH CONNECTOR query is 'false'
	 	Examples:
	 	|	Path_Datastore				|	Path_Connector				|	Result		| 
		|	CassandraDataStore.xml		|	CassandraConnector.xml		|	false		|
		#|	ElasticSearchDataStore.xml	|	ElasticConnector.xml		|	false		|
		#|	MongoDataStore.xml			|	MongoConnector.xml			|	false		|
		#|	StreamingDataStore.xml		|	StreamingConnector.xml		|	false		|
		
