Feature: VIEWS

  Scenario: [CROSSDATA-237] CREATE VIEW viewTest AS SELECT ident,name,money,new,date FROM tabletest;
    When I execute 'CREATE VIEW viewTest AS SELECT ident,name,money,new,date FROM tabletest'
    Then I execute 'SELECT ident,name,money,new,date FROM viewTest'
    Then an exception 'IS NOT' thrown
    And The result has to have '10' rows ignoring the order:
      |ident-integer|  name-string|money-double| new-boolean| date-timestamp|
      |    3|name_3| 13.2|true|2003-03-03 01:00:0|
      |    8|name_8| 18.2|true|2008-08-08 02:00:0|
      |    4|name_4| 14.2|true|2004-04-04 02:00:0|
      |    0|name_0| 10.2|true|1999-11-30 01:00:0|
      |    5|name_5| 15.2|true|2005-05-05 02:00:0|
      |    1|name_1| 11.2|true|2001-01-01 01:00:0|
      |    6|name_6| 16.2|true|2006-06-06 02:00:0|
      |    2|name_2| 12.2|true|2002-02-02 01:00:0|
      |    7|name_7| 17.2|true|2007-07-07 02:00:0|
      |    9|name_9| 19.2|true|2009-09-09 02:00:0|


  Scenario: [CROSSDATA-237] CREATE VIEW viewTest AS SELECT ident FROM tabletest;
    When I execute 'CREATE VIEW viewTest1 AS SELECT ident FROM tabletest'
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

  Scenario: [CROSSDATA-237] CREATE VIEW viewTest2 AS SELECT ident as identificador FROM tabletest;
    When I execute 'CREATE VIEW viewTest2 AS SELECT ident as identificador FROM tabletest'
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

  Scenario: [CROSSDATA-237] CREATE VIEW viewTest3 AS SELECT name as nombre FROM tabletest;
    When I execute 'CREATE VIEW viewTest3 AS SELECT name as nombre FROM tabletest'
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


  Scenario: [CROSSDATA-237] CREATE VIEW viewTest3 AS SELECT name as nombre FROM tabletest;
    When I execute 'CREATE VIEW viewTest33 AS SELECT name as nombre FROM tabletest'
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

  Scenario: [CROSSDATA-237]CREATE VIEW viewTest4 AS SELECT ident, name FROM tabletest;
    When I execute 'CREATE VIEW viewTest4 AS SELECT ident, name FROM tabletest'
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

  Scenario: [CROSSDATA-237]CREATE VIEW viewTest4 AS SELECT ident, name FROM tabletest;
    When I execute 'CREATE VIEW viewTest5 AS SELECT ident, name FROM tab1'
    Then an exception 'IS' thrown
