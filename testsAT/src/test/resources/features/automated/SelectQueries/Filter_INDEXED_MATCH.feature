Feature: Select query with filter indexed match(Filter FULL_TEXT)
	
	Scenario: SELECT * FROM catalogTest.tableTest WHERE name MATCH '*';
		When I execute a query: 'SELECT name FROM catalogTest.tableTest WHERE name MATCH '*';'
		Then the result has to be contained:
		|catalogTest.tableTest.name-name-String	|
		|		name_1				 			|	
		|		name_2				 			|	
		|		name_3				 			|
		|		name_4				 			|
		|		name_5				 			|
		|		name_6				 			|
		|		name_7				 			|
		|		name_8				 			|
		|		name_9				 			|
		|		name_10				 			|
				
Scenario: SELECT * FROM catalogTest.tableTest WHERE name MATCH '*1';
		When I execute a query: 'SELECT name FROM catalogTest.tableTest WHERE name MATCH '*1';'
		Then the result has to be contained:
		|catalogTest.tableTest.name-name-String	|
		|		name_1				 			|	
Scenario: SELECT * FROM catalogTest.tableTest WHERE name MATCH '*1*';
		When I execute a query: 'SELECT name FROM catalogTest.tableTest WHERE name MATCH '*1*';'
		Then the result has to be contained:
		|catalogTest.tableTest.name-name-String	|
		|		name_1				 			|	
		|		name_10				 			|	

Scenario: SELECT * FROM catalogTest.tableTest WHERE name MATCH 'name?5';
		When I execute a query: 'SELECT name FROM catalogTest.tableTest WHERE name MATCH 'name?5';'
		Then the result has to be contained:
		|catalogTest.tableTest.name-name-String	|
		|		name_5				 			|	

		
		Scenario: SELECT * FROM catalogTest.tableTest WHERE name MATCH 'name?4' AND name MATCH '*4';
		When I execute a query: 'SELECT name FROM catalogTest.tableTest WHERE name MATCH 'name?4' AND name MATCH '*4';'
		Then the result has to be contained:
		|catalogTest.tableTest.name-name-String	|
		|		name_4				 			|	