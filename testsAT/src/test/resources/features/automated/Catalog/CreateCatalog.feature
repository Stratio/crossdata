Feature: CATALOG OPERATIONS
		Scenario Outline: New catalogs
	 	When I execute a CREATE_CATALOG query with this options: '<IfNotExists>' , '<CatalogName>' and '<Properties>'
	 	Then an exception 'IS NOT' thrown	 
	 	Then The result of create catalog query is '<Result>'
	 	And The catalog '<CatalogName>' has to be droped 
	 	Then an exception 'IS NOT' thrown	
	 	Examples:
	 	|	IfNotExists	|	CatalogName		|	Properties		|	Result	|
		|		false	|	example			|	NULL			|	example	|
		|		true	|	example			| 	NULL			|	example	|
		
		Scenario: Try to create the same catalog two times(IF NOT EXISTS).
		When I execute a CREATE_CATALOG query with this options: 'true' , 'example' and 'NULL'
	 	Then an exception 'IS NOT' thrown	 
	 	Then The result of create catalog query is 'example'
	 	When I execute a CREATE_CATALOG query with this options: 'false' , 'example' and 'NULL'
	 	Then an exception 'IS' thrown	 
		And The catalog 'example' has to be droped 
	 	Then an exception 'IS NOT' thrown
		
	Scenario: CROSSDATA-309-Try to create the same catalog two times(IF NOT EXISTS).
		When I execute a CREATE_CATALOG query with this options: 'true' , 'example' and 'NULL'
	 	Then an exception 'IS NOT' thrown	 
	 	Then The result of create catalog query is 'example'
	 	When I execute a CREATE_CATALOG query with this options: 'true' , 'example' and 'NULL'
	 	Then an exception 'IS NOT' thrown	 
	 	Then The result of create catalog query is 'example'
		And The catalog 'example' has to be droped 
	 	Then an exception 'IS NOT' thrown	
		
		
		Scenario: Try to drop a non existing catalog
		 When The catalog 'notExistsCatalog' has to be droped 
	 	 Then an exception 'IS NOT' thrown	
	 	 
	 	Scenario: (CROSSDATA-308)- Try to drop a non existing catalog
		 When The catalog if exists 'notExistsCatalog' has to be droped 
	 	 Then an exception 'IS' thrown	 
	 	