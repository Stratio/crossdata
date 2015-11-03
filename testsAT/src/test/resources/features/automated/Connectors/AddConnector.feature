Feature: ADD_CONNECTOR feature
	
	Scenario Outline: Add news connector definiton for the file <Path>
		#Given I have to execute the reset metadata query
	 	When I execute a ADD_CONNECTOR query with this options: '<Path>'
	 	Then The result of add new connector query is '<Result>'
	   #When I want to know the description of a connector '<ConnectorName>'
       #Then I expect to recieve the string: '<Path>'
	 	When I execute a DROP_CONNECTOR query of the datastore '<ConnectorName>'
	 	Then The result of drop a connector is '<Result>'
	 	
	 	Examples:
	 	
	 	|	Path						|	Result	| 	ConnectorName		|
		|	CassandraConnector.xml		|	false	|	CassandraConnector	|
		|	ElasticConnector.xml		|	false	|	elasticsearchconnector		|
		|	MongoConnector.xml			|	false	|	MongoConnector		|
		|	StreamingConnector.xml		|	false	|	StreamingConnector	|
		|	DeepConnector.xml			|	false	|	DeepConnector		|

	
	Scenario: Add a datastore from a xml file bad formed
		When I execute a ADD_CONNECTOR query with this options: 'FileBadFormedConnector.xml'
		Then an exception 'IS' thrown with class 'ManifestException'	

	Scenario: Add a the same connector two times
		When I execute a ADD_CONNECTOR query with this options: 'CassandraConnector.xml'
		Then The result of add new connector query is 'false'
		When I execute a ADD_CONNECTOR query with this options: 'CassandraConnector.xml'
		Then The result of add new connector query is 'true'
		When I execute a DROP_CONNECTOR query of the datastore 'CassandraConnector'
	 	Then The result of drop a connector is 'false'
	
	Scenario: Add a connector from a file that not is an xml and is well formed.
		When I execute a ADD_CONNECTOR query with this options: 'FileWellFormedConnector'
		Then The result of add new connector query is 'false'
		When I execute a DROP_CONNECTOR query of the datastore 'CassandraConnector'
	 	Then The result of drop a connector is 'false'
	
	Scenario: Add a connector from a xml file with incorrect data
		When I execute a ADD_CONNECTOR query with this options: 'BadConnector.xml'
		Then an exception 'IS' thrown
	
	Scenario: Droping a connector that not exists
		When I execute a DROP_CONNECTOR query of the datastore 'CassandraConnector'
 		Then The result of drop a connector is 'true'
 		
 	