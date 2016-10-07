Feature: VIEWS

  @ignore @tillfixed(CROSSDATA-237)
  Scenario: [CROSSDATA-237] CREATE VIEW viewTest AS SELECT * FROM tabletest;
    When I execute 'CREATE VIEW viewTest AS SELECT * FROM tabletest'
    Then I execute 'SELECT ident, date, money, name, new FROM viewTest'
    And The result has to have '10' rows ignoring the order:
      |ident-long|date-timestamp     |money-double |name-string| new-boolean |
      |    5        |2005-05-04 22:00:00| 15.2        |name_5     |true         |
      |    1        |2000-12-31 23:00:00| 11.2        |name_1     |true         |
      |    8        |2008-08-07 22:00:00| 18.2        |name_8     |true         |
      |    0        |1999-11-29 23:00:00| 10.2        |name_0     |true         |
      |    2        |2002-02-01 23:00:00| 12.2        |name_2     |true         |
      |    4        |2004-04-03 22:00:00| 14.2        |name_4     |true         |
      |    7        |2007-07-06 22:00:00| 17.2        |name_7     |true         |
      |    6        |2006-06-05 22:00:00| 16.2        |name_6     |true         |
      |    9        |2009-09-08 22:00:00| 19.2        |name_9     |true         |
      |    3        |2003-03-02 23:00:00| 13.2        |name_3     |true         |

  Scenario: [CROSSDATA-237] CREATE VIEW viewTest AS SELECT ident FROM tabletest;
    When I execute 'CREATE VIEW viewTest1 AS SELECT ident FROM tabletest'
    Then I execute 'SELECT * FROM viewTest1'
    And The result has to have '10' rows ignoring the order:
      |ident-long|
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

  Scenario: [CROSSDATA-237] CREATE VIEW viewTest2 AS SELECT ident as identificador FROM tabletest;
    When I execute 'CREATE VIEW viewTest2 AS SELECT ident as identificador FROM tabletest'
    Then I execute 'SELECT * FROM viewTest2'
    And The result has to have '10' rows ignoring the order:
      |identificador-long|
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

  Scenario: [CROSSDATA-237] CREATE VIEW viewTest3 AS SELECT name as nombre FROM tabletest;
    When I execute 'CREATE VIEW viewTest3 AS SELECT name as nombre FROM tabletest'
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


  Scenario: [CROSSDATA-237] CREATE VIEW viewTest3 AS SELECT name as nombre FROM tabletest;
    When I execute 'CREATE VIEW viewTest33 AS SELECT name as nombre FROM tabletest'
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

  Scenario: [CROSSDATA-237]CREATE VIEW viewTest4 AS SELECT ident, name FROM tabletest;
    When I execute 'CREATE VIEW viewTest4 AS SELECT ident, name FROM tabletest'
    Then I execute 'SELECT * FROM viewTest4'
    And The result has to have '10' rows ignoring the order:
      | ident-long | name-string   |
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