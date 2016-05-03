Feature: VIEWS

  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest AS SELECT * FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest AS SELECT * FROM tabletest'
    Then I execute 'SELECT * FROM viewTest'
    Then an exception 'IS NOT' thrown
    And The result has to have '10' rows ignoring the order:
      |ident-integer|date-timestamp     |money-double |name-string| new-boolean |
      |    5        |2005-05-05 00:00:00| 15.2        |name_5     |true         |
      |    1        |2001-01-01 00:00:00| 11.2        |name_1     |true         |
      |    8        |2008-08-08 00:00:00| 18.2        |name_8     |true         |
      |    0        |1999-11-30 00:00:00| 10.2        |name_0     |true         |
      |    2        |2002-02-02 00:00:00| 12.2        |name_2     |true         |
      |    4        |2004-04-04 00:00:00| 14.2        |name_4     |true         |
      |    7        |2007-07-07 00:00:00| 17.2        |name_7     |true         |
      |    6        |2006-06-06 00:00:00| 16.2        |name_6     |true         |
      |    9        |2009-09-09 00:00:00| 19.2        |name_9     |true         |
      |    3        |2003-03-03 00:00:00| 13.2        |name_3     |true         |

  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest AS SELECT ident FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest1 AS SELECT ident FROM tabletest'
    Then I execute 'SELECT * FROM viewTest1'
    Then an exception 'IS NOT' thrown
    And The result has to have '10' rows ignoring the order:
      |ident-integer|
      |    5        |
      |    1        |
      |    8        |
      |    0        |
      |    2        |
      |    4        |
      |    7        |
      |    6        |
      |    9        |
      |    3        |

  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest2 AS SELECT ident as identificador FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest2 AS SELECT ident as identificador FROM tabletest'
    Then I execute 'SELECT * FROM viewTest2'
    Then an exception 'IS NOT' thrown
    And The result has to have '10' rows ignoring the order:
      |identificador-integer|
      |    5                |
      |    1                |
      |    8                |
      |    0                |
      |    2                |
      |    4                |
      |    7                |
      |    6                |
      |    9                |
      |    3                |

  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest3 AS SELECT name as nombre FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest3 AS SELECT name as nombre FROM tabletest'
    Then I execute 'SELECT * FROM viewTest3'
    Then an exception 'IS NOT' thrown
    And The result has to have '10' rows ignoring the order:
      | nombre-string   |
      | name_0        |
      | name_1        |
      | name_2        |
      | name_3        |
      | name_4        |
      | name_5        |
      | name_6        |
      | name_7        |
      | name_8        |
      | name_9        |


  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest3 AS SELECT name as nombre FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest33 AS SELECT name as nombre FROM tabletest'
    Then I execute 'SELECT nombre as fullname FROM viewTest33'
    Then an exception 'IS NOT' thrown
    And The result has to have '10' rows ignoring the order:
      | fullname-string   |
      | name_0        |
      | name_1        |
      | name_2        |
      | name_3        |
      | name_4        |
      | name_5        |
      | name_6        |
      | name_7        |
      | name_8        |
      | name_9        |

  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest4 AS SELECT ident, name FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest4 AS SELECT ident, name FROM tabletest'
    Then I execute 'SELECT * FROM viewTest4'
    Then an exception 'IS NOT' thrown
    And The result has to have '10' rows ignoring the order:
      | ident-integer | name-string   |
      |    0          | name_0        |
      |    1          | name_1        |
      |    2          | name_2        |
      |    3          | name_3        |
      |    4          | name_4        |
      |    5          | name_5        |
      |    6          | name_6        |
      |    7          | name_7        |
      |    8          | name_8        |
      |    9          | name_9        |

  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest4 AS SELECT ident, name FROM tab1;
    When I execute 'CREATE TEMPORARY VIEW viewTest5 AS SELECT name FROM tab1'
    Then an exception 'IS NOT' thrown
    Then I execute 'SELECT * FROM viewTest5'
    Then an exception 'IS NOT' thrown
    And The result has to have '1' rows ignoring the order:
      | name-string   |
      | name_0        |