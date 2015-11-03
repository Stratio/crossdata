Feature: Select query plus limit clause

	Scenario: SELECT * FROM catalogTest.tableTest LIMIT 1;
		When I execute a query: 'SELECT * FROM catalogTest.tableTest LIMIT 1;'
		Then the result has to be contained:
		|catalogTest.tableTest.id-id-Integer |catalogTest.tableTest.name-name-String |catalogTest.tableTest.age-age-Integer |catalogTest.tableTest.phone-phone-BigInteger |	catalogTest.tableTest.salary-salary-Double |  catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
		|1| name_1| 10| 10000000| 1111.11| 11.11| true  |
		|2| name_2| 20| 20000000| 2222.22| 12.11| false |
		|3| name_3| 30| 30000000| 3333.33| 13.11| true  |
		|4| name_4| 40| 40000000| 4444.44| 14.11| false |
		|5| name_5| 50| 50000000| 5555.55| 15.11| true  |
		|6| name_6| 60| 60000000| 6666.66| 16.11| false |
		|7| name_7| 70| 70000000| 7777.77| 17.11| true  |
		|8| name_8| 80| 80000000| 8888.88| 18.11| false |
		|9| name_9| 90| 90000000| 9999.99| 19.11| true  |
		|10| name_10|1| 10000000| 1111.11| 20.11| false |	
	
	Scenario: SELECT * FROM catalogTest.tableTest LIMIT 5;
		When I execute a query: 'SELECT * FROM catalogTest.tableTest LIMIT 5;'
		Then the result has to be contained:
		|catalogTest.tableTest.id-id-Integer |catalogTest.tableTest.name-name-String |catalogTest.tableTest.age-age-Integer |catalogTest.tableTest.phone-phone-BigInteger |	catalogTest.tableTest.salary-salary-Double |  catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
		|1| name_1| 10| 10000000| 1111.11| 11.11| true  |
		|2| name_2| 20| 20000000| 2222.22| 12.11| false |
		|3| name_3| 30| 30000000| 3333.33| 13.11| true  |
		|4| name_4| 40| 40000000| 4444.44| 14.11| false |
		|5| name_5| 50| 50000000| 5555.55| 15.11| true  |
		|6| name_6| 60| 60000000| 6666.66| 16.11| false |
		|7| name_7| 70| 70000000| 7777.77| 17.11| true  |
		|8| name_8| 80| 80000000| 8888.88| 18.11| false |
		|9| name_9| 90| 90000000| 9999.99| 19.11| true  |
		|10| name_10|1| 10000000| 1111.11| 20.11| false |	
	
	Scenario: SELECT * FROM catalogTest.tableTest LIMIT 10;
		When I execute a query: 'SELECT * FROM catalogTest.tableTest LIMIT 10;'
		Then the result has to be:
		|catalogTest.tableTest.id-id-Integer |catalogTest.tableTest.name-name-String |catalogTest.tableTest.age-age-Integer |catalogTest.tableTest.phone-phone-BigInteger |	catalogTest.tableTest.salary-salary-Double |  catalogTest.tableTest.reten-reten-Float | catalogTest.tableTest.new-new-Boolean |
		|1| name_1| 10| 10000000| 1111.11| 11.11| true  |
		|2| name_2| 20| 20000000| 2222.22| 12.11| false |
		|3| name_3| 30| 30000000| 3333.33| 13.11| true  |
		|4| name_4| 40| 40000000| 4444.44| 14.11| false |
		|5| name_5| 50| 50000000| 5555.55| 15.11| true  |
		|6| name_6| 60| 60000000| 6666.66| 16.11| false |
		|7| name_7| 70| 70000000| 7777.77| 17.11| true  |
		|8| name_8| 80| 80000000| 8888.88| 18.11| false |
		|9| name_9| 90| 90000000| 9999.99| 19.11| true  |
		|10| name_10|1| 10000000| 1111.11| 20.11| false |	
	
	Scenario:(CROSSDATA-662) SELECT * FROM catalogTest.tableTest LIMIT 0;
		When I execute a query: 'SELECT * FROM catalogTest.tableTest LIMIT 0;'
		Then an exception 'IS' thrown	
		
	Scenario: SELECT * FROM catalogTest.tableTest LIMIT 2.5;
		When I execute a query: 'SELECT * FROM catalogTest.tableTest LIMIT 2.5;'
		Then an exception 'IS' thrown	
		