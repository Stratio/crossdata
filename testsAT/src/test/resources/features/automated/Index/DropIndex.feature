Feature: CROSSDATA-151 => Drop Index queries

  Scenario Outline: Drop index of all types
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<QueriesCreate>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: '<QueriesDrop>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | QueriesCreate                                                                                                               | QueriesDrop                                                |
      | CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);   | DROP INDEX catalogTests.indextable.age_default_index_test; |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age); | DROP INDEX catalogTests.indextable.age_default_index_test; |

  Scenario Outline: Drop index of all types(IF EXISTS)
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<QueriesCreate>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: '<QueriesDrop>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | QueriesCreate                                                                                                               | QueriesDrop                                                          |
      | CREATE DEFAULT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age);   | DROP INDEX IF EXISTS catalogTests.indextable.age_default_index_test; |
      | CREATE FULL_TEXT INDEX catalogTest.indextable.age_default_index_test ON catalogTest.indextable(catalogTest.indextable.age); | DROP INDEX IF EXISTS catalogTests.indextable.age_default_index_test; |

  Scenario Outline: Drop index of all types that not exists
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<QueriesDrop>'
    Then an exception 'IS' thrown
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | QueriesDrop                                                |
      | DROP INDEX catalogTests.indextable.age_default_index_test; |
      | DROP INDEX catalogTests.indextable.age_default_index_test; |

  Scenario Outline: Drop index of all types that not exists(IF EXISTS)
    When I execute a query: 'CREATE TABLE IF NOT EXISTS catalogTest.indextable ON CLUSTER ClusterTest(id int, name text, age int, phone BIGINT, salary DOUBLE, reten FLOAT, new BOOLEAN, PRIMARY KEY(id));'
    Then the result has not errors: 'false'
    When I execute a query: '<QueriesDrop>'
    Then an exception 'IS NOT' thrown
    Then the result has not errors: 'false'
    When I execute a query: 'DROP TABLE catalogTest.indextable;'
    Then the result has not errors: 'false'

    Examples: 
      | QueriesDrop                                                          |
      | DROP INDEX IF EXISTS catalogTests.indextable.age_default_index_test; |
      | DROP INDEX IF EXISTS catalogTests.indextable.age_default_index_test; |
