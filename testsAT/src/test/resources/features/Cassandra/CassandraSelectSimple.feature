Feature: CassandraSelectSimple
  Scenario: [CASSANDRA NATIVE] SELECT * FROM tabletest;
    When I execute 'SELECT * FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
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

  Scenario: [CASSANDRA NATIVE] SELECT ident FROM tabletest;
    When I execute 'SELECT ident FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | ident-integer |
      |    0          |
      |    1          |
      |    2          |
      |    3          |
      |    4          |
      |    5          |
      |    6          |
      |    7          |
      |    8          |
      |    9          |

  Scenario: [CASSANDRA NATIVE ALIAS INTEGER COLUMN] SELECT ident AS identificador FROM tabletest;
    When I execute 'SELECT ident AS identificador FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | identificador-integer |
      |    0                  |
      |    1                  |
      |    2                  |
      |    3                  |
      |    4                  |
      |    5                  |
      |    6                  |
      |    7                  |
      |    8                  |
      |    9                  |

  Scenario: [CASSANDRA NATIVE ALIAS STRING COLUMN] SELECT name AS nombre FROM tabletest;
    When I execute 'SELECT name as nombre FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
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
  Scenario: [CASSANDRA NATIVE] SELECT ident,name FROM tabletest;
    When I execute 'SELECT ident, name FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
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

  Scenario: [CASSANDRA NATIVE] SELECT name,ident FROM tabletest;
    When I execute 'SELECT name, ident FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | name-string | ident-integer   |
      |    name_0   | 0               |
      |    name_1   | 1               |
      |    name_2   | 2               |
      |    name_3   | 3               |
      |    name_4   | 4               |
      |    name_5   | 5               |
      |    name_6   | 6               |
      |    name_7   | 7               |
      |    name_8   | 8               |
      |    name_9   | 9               |

  Scenario: [CASSANDRA NATIVE] SELECT ident AS identificador,name AS nombre FROM tabletest;
    When I execute 'SELECT ident AS identificador,name AS nombre FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | identificador-integer | nombre-string   |
      |    0                  | name_0          |
      |    1                  | name_1          |
      |    2                  | name_2          |
      |    3                  | name_3          |
      |    4                  | name_4          |
      |    5                  | name_5          |
      |    6                  | name_6          |
      |    7                  | name_7          |
      |    8                  | name_8          |
      |    9                  | name_9          |

  Scenario: [CASSANDRA NATIVE] SELECT name, money,ident FROM tabletest;
    When I execute 'SELECT name, money,ident FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | name-string | money-double |ident-integer   |
      |    name_0   | 10.2         |0               |
      |    name_1   | 11.2         |1               |
      |    name_2   | 12.2         |2               |
      |    name_3   | 13.2         |3               |
      |    name_4   | 14.2         |4               |
      |    name_5   | 15.2         |5               |
      |    name_6   | 16.2         |6               |
      |    name_7   | 17.2         |7               |
      |    name_8   | 18.2         |8               |
      |    name_9   | 19.2         |9               |


  Scenario: [CASSANDRA NATIVE] SELECT ident,name,money FROM tabletest;
    When I execute 'SELECT ident,name,money FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | ident-integer | name-string   | money-double  |
      |    0          | name_0        | 10.2          |
      |    1          | name_1        | 11.2          |
      |    2          | name_2        | 12.2          |
      |    3          | name_3        | 13.2          |
      |    4          | name_4        | 14.2          |
      |    5          | name_5        | 15.2          |
      |    6          | name_6        | 16.2          |
      |    7          | name_7        | 17.2          |
      |    8          | name_8        | 18.2          |
      |    9          | name_9        | 19.2          |

  Scenario: [CASSANDRA NATIVE] SELECT ident,name,money,new FROM tabletest;
    When I execute 'SELECT ident,name,money,new FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | ident-integer | name-string   | money-double  |  new-boolean  |
      |    0          | name_0        | 10.2          |  true         |
      |    1          | name_1        | 11.2          |  true         |
      |    2          | name_2        | 12.2          |  true         |
      |    3          | name_3        | 13.2          |  true         |
      |    4          | name_4        | 14.2          |  true         |
      |    5          | name_5        | 15.2          |  true         |
      |    6          | name_6        | 16.2          |  true         |
      |    7          | name_7        | 17.2          |  true         |
      |    8          | name_8        | 18.2          |  true         |
      |    9          | name_9        | 19.2          |  true         |


  Scenario: [CASSANDRA NATIVE] SELECT SELECT name,money,ident,new new FROM tabletest;
    When I execute 'SELECT name,money,ident,new FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | name-string | money-double |ident-integer   |new-boolean  |
      |    name_0   | 10.2         |0               |true         |
      |    name_1   | 11.2         |1               |true         |
      |    name_2   | 12.2         |2               |true         |
      |    name_3   | 13.2         |3               |true         |
      |    name_4   | 14.2         |4               |true         |
      |    name_5   | 15.2         |5               |true         |
      |    name_6   | 16.2         |6               |true         |
      |    name_7   | 17.2         |7               |true         |
      |    name_8   | 18.2         |8               |true         |
      |    name_9   | 19.2         |9               |true         |

  Scenario: [CASSANDRA : MONGO NATIVE] SELECT ident,name,money,new,date FROM tabletest;
    When I execute 'SELECT ident,name,money,new,date FROM tabletest'
    Then The result has to have '10' rows ignoring the order:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-timestamp      |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 00:00:00 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 00:00:00 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 00:00:00 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 00:00:00 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 00:00:00 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 00:00:00 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 00:00:00 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 00:00:00 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 00:00:00 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 00:00:00 |