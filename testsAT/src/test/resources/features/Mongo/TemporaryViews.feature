Feature: VIEWS

  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest AS SELECT * FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest AS SELECT * FROM tabletest'
    Then I execute 'SELECT * FROM viewTest'
    And The result has to have '10' rows ignoring the order:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date   |
      |    0          |name_0         | 10.2          |true           |2000-01-01   |
      |    1          |name_1         | 11.2          |true           |2001-01-01   |
      |    2          |name_2         | 12.2          |true           |2002-01-01   |
      |    3          |name_3         | 13.2          |true           |2003-01-01   |
      |    4          |name_4         | 14.2          |true           |2004-01-01   |
      |    5          |name_5         | 15.2          |true           |2005-01-01   |
      |    6          |name_6         | 16.2          |true           |2006-01-01   |
      |    7          |name_7         | 17.2          |true           |2007-01-01   |
      |    8          |name_8         | 18.2          |true           |2008-01-01   |
      |    9          |name_9         | 19.2          |true           |2009-01-01   |


  Scenario: [CROSSDATA-188] CREATE TEMPORARY VIEW viewTest AS SELECT ident FROM tabletest;
    When I execute 'CREATE TEMPORARY VIEW viewTest1 AS SELECT ident FROM tabletest'
    Then I execute 'SELECT * FROM viewTest1'
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