Feature: CROSSDATA-162 => Create Index queries

  Scenario Outline: Create a defaul index over all datatypes.
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<Queries>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                                                                                         |
      | CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);       |
      | CREATE DEFAULT INDEX catalogTest.indextable.name_default_index_test ON catalogTest.indextable(catalogTest.indextable.name);     |
      | CREATE DEFAULT INDEX catalogTest.indextable.phone_default_index_test ON catalogTest.indextable(catalogTest.indextable.phone);   |
      | CREATE DEFAULT INDEX catalogTest.indextable.salary_default_index_test ON catalogTest.indextable(catalogTest.indextable.salary); |
      | CREATE DEFAULT INDEX catalogTest.indextable.reten_default_index_test ON catalogTest.indextable(catalogTest.indextable.reten);   |
      | CREATE DEFAULT INDEX catalogTest.indextable.new_default_index_test ON catalogTest.indextable(catalogTest.indextable.new);       |

  Scenario Outline: Create a full_text index over all datatypes.
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<Queries>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                                                                                           |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);       |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.name_default_index_test ON catalogTest.indextable(catalogTest.indextable.name);     |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.phone_default_index_test ON catalogTest.indextable(catalogTest.indextable.phone);   |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.salary_default_index_test ON catalogTest.indextable(catalogTest.indextable.salary); |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.reten_default_index_test ON catalogTest.indextable(catalogTest.indextable.reten);   |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.new_default_index_test ON catalogTest.indextable(catalogTest.indextable.new);       |

  Scenario Outline: (CROSSDATA-328) Create a full_text over two columns or more.
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<Queries>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                                                                                                                                                                         |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age,catalogTest.indextable.name);                                                         |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age,catalogTest.indextable.name,catalogTest.indextable.phone);                            |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age,catalogTest.indextable.name,catalogTest.indextable.phone,catalogTest.indextable.new); |

  Scenario: Create a defaul index over two columns.
    When I execute a query: 'CREATE TABLE catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age,catalogTest.indextable.new);'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

  Scenario Outline: Create the same index over the same column(multicolumns index)
    When I execute a query: 'CREATE TABLE catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<Queries>'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | Queries                                                                                                                                                |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age,catalogTest.indextable.age); |
      | CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age,catalogTest.indextable.age);  |

  Scenario: Create the same index two times
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);'
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

  Scenario: Create two index of different type, but with the same name
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);'
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

  Scenario: Create two full_text index in the same table
    When I execute a query: 'CREATE TABLE catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE FULL_TEXT INDEX catalogTest.indextable.name_default_index_test ON catalogTest.indextable(catalogTest.indextable.name);  '
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

  Scenario: Create a index over column that not exists
    When I execute a query: 'CREATE TABLE catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: 'CREATE FULL_TEXT INDEX catalogTest.indextable.id_default_index ON catalogTest.indextable(catalogTest.indextable.notExists);'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

  Scenario: Create a index over table that not exists
    When I execute a query: 'CREATE FULL_TEXT INDEX catalogTest.notExists.id_default_index_test ON catalogTest.indextable(catalogTest.notExists.notExists);'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'
