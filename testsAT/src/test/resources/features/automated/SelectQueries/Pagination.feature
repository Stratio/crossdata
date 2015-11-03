Feature: Basic Select with pagination

  Scenario: SELECT * FROM paginationTable;(PAGINATION)
		When I execute a async query: 'SELECT * FROM catalogTest.paginationTable;'
		Then I have to recieve '3' results