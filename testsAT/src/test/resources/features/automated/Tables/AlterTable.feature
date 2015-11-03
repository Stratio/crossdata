Feature: Alter table tests

  Scenario Outline: Alter table and Add action over a table(All types)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: '<Queries>'
    Then the column 'catalogTest.testtable.testcolumn' of type '<Type>' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                   | Type    |
      | ALTER TABLE catalogTest.testtable ADD testcolumn INT;     | Integer |
      | ALTER TABLE catalogTest.testtable ADD testcolumn TEXT;    | String  |
      | ALTER TABLE catalogTest.testtable ADD testcolumn BIGINT;  | Long    |
      | ALTER TABLE catalogTest.testtable ADD testcolumn DOUBLE;  | Double  |
      | ALTER TABLE catalogTest.testtable ADD testcolumn FLOAT;   | Float   |
      | ALTER TABLE catalogTest.testtable ADD testcolumn BOOLEAN; | Boolean |

  Scenario: Alter table and Add a column that exists(of the same type)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'ALTER TABLE catalogTest.testtable ADD name TEXT;'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario Outline: Alter table and Drop action over a table(All types)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: '<Queries>'
    Then the column '<Column>' of type '<Type>' has to exists: 'false'
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                        | Type    | Column                       |
      | ALTER TABLE catalogTest.testtable DROP name;   | String  | catalogTest.testtable.name   |
      | ALTER TABLE catalogTest.testtable DROP age;    | Integer | catalogTest.testtable.age    |
      | ALTER TABLE catalogTest.testtable DROP phone;  | Long    | catalogTest.testtable.phone  |
      | ALTER TABLE catalogTest.testtable DROP salary; | Double  | catalogTest.testtable.salary |
      | ALTER TABLE catalogTest.testtable DROP reten;  | Float   | catalogTest.testtable.reten  |
      | ALTER TABLE catalogTest.testtable DROP new;    | Boolean | catalogTest.testtable.new    |

  Scenario: Alter table and drop action trying to drop de PK_column
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'ALTER TABLE catalogTest.testtable DROP id;'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: Alter table and drop action trying to drop a column that not exists
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'ALTER TABLE catalogTest.testtable DROP notexists;'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario Outline: (CROSSDATA-353/148) Alter table and Alter action
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: '<Queries>'
    Then an exception 'IS NOT' thrown
    Then the column '<Column>' of type '<Type>' has to exists: 'true'
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                    | Type    | Column                       |
      | ALTER TABLE catalogTest.testtable ALTER age TYPE TEXT;     | String  | catalogtest.testtable.age    |
      | ALTER TABLE catalogTest.testtable ALTER phone TYPE TEXT;   | String  | catalogTest.testtable.phone  |
      | ALTER TABLE catalogTest.testtable ALTER salary TYPE TEXT;  | String  | catalogTest.testtable.salary |
      | ALTER TABLE catalogTest.testtable ALTER reten TYPE TEXT;   | String  | catalogTest.testtable.reten  |
      | ALTER TABLE catalogTest.testtable ALTER new TYPE TEXT;     | String  | catalogTest.testtable.new    |
      | ALTER TABLE catalogTest.testtable ALTER age TYPE BIGINT;   | Long    | catalogtest.testtable.age    |
      | ALTER TABLE catalogTest.testtable ALTER age TYPE DOUBLE;   | Double  | catalogtest.testtable.age    |
      | ALTER TABLE catalogTest.testtable ALTER age TYPE FLOAT;    | Float   | catalogtest.testtable.age    |
      | ALTER TABLE catalogTest.testtable ALTER phone TYPE INT;    | Integer | catalogTest.testtable.phone  |
      | ALTER TABLE catalogTest.testtable ALTER phone TYPE DOUBLE; | Double  | catalogTest.testtable.phone  |
      | ALTER TABLE catalogTest.testtable ALTER phone TYPE FLOAT;  | Float   | catalogTest.testtable.phone  |
      | ALTER TABLE catalogTest.testtable ALTER salary TYPE FLOAT; | Float   | catalogTest.testtable.salary |
      | ALTER TABLE catalogTest.testtable ALTER reten TYPE DOUBLE; | Double  | catalogTest.testtable.reten  |

  Scenario: Alter table and alter action, casting incompatible types(TEXT TO INT)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name text, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'ALTER TABLE catalogTest.testtable ALTER name TYPE INT;'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: Alter table and alter action, casting incompatible types(BOOLEAN TO INT)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name BOOLEAN, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'ALTER TABLE catalogTest.testtable ALTER name TYPE INT;'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: Alter table and alter action, casting incompatible types(DOUBLE TO INT)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name DOUBLE, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'ALTER TABLE catalogTest.testtable ALTER name TYPE INT;'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'

  Scenario: Alter table and alter action, casting incompatible types(FLOAT TO INT)
    When I execute a query: 'CREATE TABLE catalogTest.testtable ON CLUSTER ClusterTest(id int, name FLOAT, PRIMARY KEY(id));'
    Then the table 'catalogTest.testtable' has to exists: 'true'
    When I execute a query: 'ALTER TABLE catalogTest.testtable ALTER name TYPE INT;'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.testtable;'
    Then the table 'catalogTest.testtable' has to exists: 'false'
    Then the result has not errors: 'false'
