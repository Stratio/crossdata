Feature: Connection exceptions Test statements
	
	Scenario: Statements that produce an exception
	
	 	When I disconnect the driver from the server
	 	And I execute a simple query: 'SELECT * FROM tab_1;'
	 	Then an exception 'IS' thrown with class 'ConnectionException'
