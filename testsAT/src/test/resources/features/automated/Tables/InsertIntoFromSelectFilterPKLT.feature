Feature: Insert Into from select with filter_pk_lt tests

  Scenario: INSERT INTO catalogTest.testTable(id, name, age, phone, salary, reten, new) SELECT * FROM catalogTest.tableTest WHERE id < 5;
    When I execute a query: 'CREATE TABLE catalogTest.testTableInsert ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogtest.testTableInsert' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTableInsert(id, name, age, phone, salary, reten, new) SELECT * FROM catalogTest.tableTest WHERE id < 5;'
    Then the result has not errors: 'false'
    When I execute a query: 'SELECT * FROM catalogTest.testTableInsert;'
    Then the result has to be:
      | catalogTest.testTableInsert.id-id-Integer | catalogTest.testTableInsert.name-name-String | catalogTest.testTableInsert.age-age-Integer | catalogTest.testTableInsert.phone-phone-BigInteger | catalogTest.testTableInsert.salary-salary-Double | catalogTest.testTableInsert.reten-reten-Float | catalogTest.testTableInsert.new-new-Boolean |
      | 1                                         | name_1                                       | 10                                          | 10000000                                           | 1111.11                                          | 11.11                                         | true                                        |
      | 2                                         | name_2                                       | 20                                          | 20000000                                           | 2222.22                                          | 12.11                                         | false                                       |
      | 3                                         | name_3                                       | 30                                          | 30000000                                           | 3333.33                                          | 13.11                                         | true                                        |
      | 4                                         | name_4                                       | 40                                          | 40000000                                           | 4444.44                                          | 14.11                                         | false                                       |

  Scenario: INSERT INTO catalogTest.testTableInsert1(id, name) SELECT id, name FROM catalogTest.tableTest WHERE id < 5;
    When I execute a query: 'CREATE TABLE catalogTest.testTableInsert1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogtest.testTableInsert1' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTableInsert1(id, name) SELECT id, name FROM catalogTest.tableTest WHERE id < 5;'
    Then the result has not errors: 'false'
    When I execute a query: 'SELECT id, name FROM catalogTest.testTableInsert1;'
    Then the result has to be:
      | catalogTest.testTableInsert1.id-id-Integer | catalogTest.testTableInsert1.name-name-String |
      | 1                                          | name_1                                        |
      | 2                                          | name_2                                        |
      | 3                                          | name_3                                        |
      | 4                                          | name_4                                        |

  Scenario: INSERT INTO catalogTest.testTableInsert11(name, id) SELECT name, id FROM catalogTest.tableTest WHERE id < 5;
    When I execute a query: 'CREATE TABLE catalogTest.testTableInsert11 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogtest.testTableInsert11' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTableInsert11(name, id) SELECT name, id FROM catalogTest.tableTest WHERE id < 5;'
    Then the result has not errors: 'false'
    When I execute a query: 'SELECT id, name FROM catalogTest.testTableInsert11;'
    Then the result has to be:
      | catalogTest.testTableInsert11.id-id-Integer | catalogTest.testTableInsert11.name-name-String |
      | 1                                           | name_1                                         |
      | 2                                           | name_2                                         |
      | 3                                           | name_3                                         |
      | 4                                           | name_4                                         |

  Scenario: INSERT INTO catalogTest.testTableInsert2(id, name, age) SELECT id, name FROM catalogTest.tableTest WHERE id < 5;
    When I execute a query: 'CREATE TABLE catalogTest.testTableInsert2 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogtest.testTableInsert2' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTableInsert2(id, name, age) SELECT id, name FROM catalogTest.tableTest WHERE id < 5;'
    Then an exception 'IS' thrown

  Scenario: INSERT INTO catalogTest.testTableInsert3(id, name) SELECT id, name, age FROM catalogTest.tableTest WHERE id < 5;
    When I execute a query: 'CREATE TABLE catalogTest.testTableInsert3 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogtest.testTableInsert3' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTableInsert3(id, name) SELECT id, name, age FROM catalogTest.tableTest WHERE id < 5;'
    Then an exception 'IS' thrown

  Scenario: INSERT INTO catalogTest.testTableInsert4(id, name) SELECT id, age FROM catalogTest.tableTest WHERE id < 5;
    When I execute a query: 'CREATE TABLE catalogTest.testTableInsert4 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogtest.testTableInsert4' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTableInsert4(id, name) SELECT id, age FROM catalogTest.tableTest WHERE id < 5;'
    Then an exception 'IS' thrown

  Scenario: INSERT INTO catalogTest.testTableInsert5(name) SELECT name FROM catalogTest.tableTest WHERE id < 5;
    When I execute a query: 'CREATE TABLE catalogTest.testTableInsert5 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogtest.testTableInsert5' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTableInsert5(name) SELECT name FROM catalogTest.tableTest WHERE id < 5;'
    Then an exception 'IS' thrown
