Feature: Insert Into tests

  Scenario: (CROSSDATA-373)Simple Insert Into
    When I execute a query: 'CREATE TABLE catalogTest.testTable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testTable' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTable(id, name, age, phone, salary, reten, new) VALUES (1, 'name_1', 10, -1000000000, 1111, 11, true);'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTable(id, name, age, phone, salary, reten, new) VALUES (2, 'name_2', 0, 10000000, 1111.1, 11.22, false);'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTable(id, name, age, phone, salary, reten, new) VALUES (3, 'name_3', -1, 10000000, 1111.12, 11.23, true);'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTable(id, name, age, phone, salary, reten, new) VALUES (4, 'name_4', 100, 10000000, 1111.13, 11.44, false);'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTable(id, name, age, phone, salary, reten, new) VALUES (5, 'name_5', -3, 10000000, 1111.14, 11.55, true);'
    Then the result has not errors: 'false'
    When I execute a query: 'SELECT * FROM catalogTest.testTable;'
    Then the result has to be:
      | catalogTest.testTable.id-id-Integer | catalogTest.testTable.name-name-String | catalogTest.testTable.age-age-Integer | catalogTest.testTable.phone-phone-BigInteger | catalogTest.testTable.salary-salary-Double | catalogTest.testTable.reten-reten-Float | catalogTest.testTable.new-new-Boolean |
      | 1                                   | name_1                                 | 10                                    | -1000000000                                  | 1111                                       | 11                                      | true                                  |
      | 2                                   | name_2                                 | 0                                     | 10000000                                     | 1111.1                                     | 11.22                                   | false                                 |
      | 3                                   | name_3                                 | -1                                    | 10000000                                     | 1111.12                                    | 11.23                                   | true                                  |
      | 4                                   | name_4                                 | 100                                   | 10000000                                     | 1111.13                                    | 11.44                                   | false                                 |
      | 5                                   | name_5                                 | -3                                    | 10000000                                     | 1111.14                                    | 11.55                                   | true                                  |

  Scenario: Simple Insert Into(Different order)
    When I execute a query: 'CREATE TABLE catalogTest.testTable1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testTable1' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTable1(new , id,  age, name, phone, salary, reten) VALUES (true, 1,  10, 'name_1', -1000000000, 1111.11, 11.11);'
    Then the result has not errors: 'false'
    When I execute a query: 'SELECT * FROM catalogTest.testTable1;'
    Then the result has to be:
      | catalogTest.testTable1.id-id-Integer | catalogTest.testTable1.name-name-String | catalogTest.testTable1.age-age-Integer | catalogTest.testTable1.phone-phone-BigInteger | catalogTest.testTable1.salary-salary-Double | catalogTest.testTable1.reten-reten-Float | catalogTest.testTable1.new-new-Boolean |
      | 1                                    | name_1                                  | 10                                     | -1000000000                                   | 1111.11                                     | 11.11                                    | true                                   |

  Scenario: Simple Insert Into(Different order)
    When I execute a query: 'CREATE TABLE catalogTest.testTable2 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testTable2' has to exists: 'true'
    Then the result has not errors: 'false'
    When I execute a query: 'INSERT INTO catalogTest.testTable2(new , id,  age, name, phone, salary, reten) VALUES ('badData', 1,  10, 'name_1', -1000000000, 1111.11, 11.11);'
    Then an exception 'IS' thrown

  Scenario: Simple Insert Into in a table that not exists
    When I execute a query: 'INSERT INTO catalogTest.notexists(new , id,  age, name, phone, salary, reten) VALUES ('badData', 1,  10, 'name_1', -1000000000, 1111, 11);'
    Then an exception 'IS' thrown

  Scenario: Simple Insert Into in a catalog that not exists
    When I execute a query: 'INSERT INTO notexists.notexists(new , id,  age, name, phone, salary, reten) VALUES ('badData', 1,  10, 'name_1', -1000000000, 1111, 11);'
    Then an exception 'IS' thrown
