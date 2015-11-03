Feature: Create Table tests

  Scenario: (CROSSDATA-199)Create a simple table with one PK
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario Outline: Create a simple table with more  than one PK
    When I execute a query: '<Queries>'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable1' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable1;'
    Then the table 'catalogTest.testtable1' has to exists: 'false'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                                                                                                                                                           |
      | CREATE TABLE catalogTest.testtable1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name));                             |
      | CREATE TABLE catalogTest.testtable1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age));                         |
      | CREATE TABLE catalogTest.testtable1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone));                   |
      | CREATE TABLE catalogTest.testtable1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone,salary));            |
      | CREATE TABLE catalogTest.testtable1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone,salary,reten));      |
      | CREATE TABLE catalogTest.testtable1 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone,salary,reten, new)); |

  Scenario: (CROSSDATA-626)Create a table with a PK column that not exists
    When I execute a query: 'CREATE TABLE catalogTest.testtable2 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(notExits));'
    Then an exception 'IS' thrown
    Then the table 'catalogTest.testtable2' has to exists: 'false'

  Scenario: Create a simple table with one PK(IF NOT EXISTS)
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.testtable3 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable3' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable3;'
    Then the table 'catalogTest.testtable3' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario Outline: Create a simple table with more than one PK(IF NOT EXISTS)
    When I execute a query: '<Queries>'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable4' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable4;'
    Then the table 'catalogTest.testtable4' has to exists: 'false'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                                                                                                                                                                         |
      | CREATE TABLE IF NOT EXISTS catalogTest.testtable4 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name));                             |
      | CREATE TABLE IF NOT EXISTS catalogTest.testtable4 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age));                         |
      | CREATE TABLE IF NOT EXISTS catalogTest.testtable4 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone));                   |
      | CREATE TABLE IF NOT EXISTS catalogTest.testtable4 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone,salary));            |
      | CREATE TABLE IF NOT EXISTS catalogTest.testtable4 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone,salary,reten));      |
      | CREATE TABLE IF NOT EXISTS catalogTest.testtable4 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, name,age,phone,salary,reten, new)); |

  Scenario:(CROSSDATA-626) Create a table with a PK column that not exists(IF NOT EXISTS)
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.testtable5 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(notExits));'
    Then an exception 'IS' thrown
    Then the table 'catalogTest.testtable5' has to exists: 'false'

  Scenario: Create the same table two times
    When I execute a query: 'CREATE TABLE catalogTest.testtable6 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable6' has to exists: 'true'
    When I execute a query: 'CREATE TABLE catalogTest.testtable6 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable6;'
    Then the table 'catalogTest.testtable6' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: (CROSSDATA-349)Create the same table two times(The second table with IF NOT EXISTS)
    When I execute a query: 'CREATE TABLE catalogTest.testtable7 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable7' has to exists: 'true'
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.testtable7 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable7' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable7;'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable7' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: (CROSSDATA-349) Create the same table two times(IF NOT EXISTS)
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.testtable8 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable8' has to exists: 'true'
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.testtable8 ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable8' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable8;'
    Then an exception 'IS NOT' thrown
    Then the table 'catalogTest.testtable8' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: Create a table without columns
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest();'
    Then an exception 'IS' thrown

  Scenario: Create a table without PK
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN);'
    Then an exception 'IS' thrown

  Scenario: Create a table over cluster that not exists
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER notExistsCluster (id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS' thrown

  Scenario: Create a table over catalog that not exists
    When I execute a query: 'CREATE TABLE notExists.testtable ON CLUSTER notExistsCluster (id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS' thrown

  Scenario: (CROSSDATA-378)Create a table with two columns with the same name of different type
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, id text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS' thrown

  Scenario: Create a table with two columns with the same name and type
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, id int, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS' thrown

  Scenario: (CROSSDATA-628)Create a table with a column that has a type that not exists
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name notExists, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then an exception 'IS' thrown

  Scenario: Create a table with a PK duplicated
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, id));'
    Then an exception 'IS' thrown

  Scenario: Create a table with a column of type List
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone LIST, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id, id));'
    Then an exception 'IS' thrown
