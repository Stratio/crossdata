Feature: MongoSelectAnd
  Scenario: [CROSSDATA-162] SELECT * FROM tabletest WHERE ident NOT BETWEEN 10 AND 15;
    When I execute 'SELECT * FROM tabletest WHERE ident NOT BETWEEN 10 AND 15'
    Then The spark result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 2000-1-1 |
      |    1          | name_1        | 11.2          |  true         | 2001-1-1|
      |    2          | name_2        | 12.2          |  true         | 2002-1-1 |
      |    3          | name_3        | 13.2          |  true         | 2003-1-1 |
      |    4          | name_4        | 14.2          |  true         | 2004-1-1 |
      |    5          | name_5        | 15.2          |  true         | 2005-1-1 |
      |    6          | name_6        | 16.2          |  true         | 2006-1-1 |
      |    7          | name_7        | 17.2          |  true         | 2007-1-1 |
      |    8          | name_8        | 18.2          |  true         | 2008-1-1 |
      |    9          | name_9        | 19.2          |  true         | 2009-1-1 |

  Scenario: [CROSSDATA-162] SELECT * FROM tabletest WHERE ident NOT BETWEEN 0 AND 10;
    When I execute 'SELECT * FROM tabletest WHERE ident NOT BETWEEN 0 AND 10'
    Then The spark result has to have '0' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |


  Scenario: [CROSSDATA-162] SELECT * FROM tabletest WHERE ident NOT BETWEEN 5 AND 6;
    When I execute 'SELECT * FROM tabletest WHERE ident NOT BETWEEN 5 AND 6'
    Then The spark result has to have '8' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 2000-1-1 |
      |    1          | name_1        | 11.2          |  true         | 2001-1-1|
      |    2          | name_2        | 12.2          |  true         | 2002-1-1 |
      |    3          | name_3        | 13.2          |  true         | 2003-1-1 |
      |    4          | name_4        | 14.2          |  true         | 2004-1-1 |
      |    7          | name_7        | 17.2          |  true         | 2007-1-1 |
      |    8          | name_8        | 18.2          |  true         | 2008-1-1 |
      |    9          | name_9        | 19.2          |  true         | 2009-1-1 |


  Scenario: [CROSSDATA-162] SELECT * FROM tabletest WHERE ident NOT BETWEEN 5 AND 5;
    When I execute 'SELECT * FROM tabletest WHERE ident NOT BETWEEN 5 AND 5'
    Then The spark result has to have '9' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 2000-1-1   |
      |    1          | name_1        | 11.2          |  true         | 2001-1-1   |
      |    2          | name_2        | 12.2          |  true         | 2002-1-1   |
      |    3          | name_3        | 13.2          |  true         | 2003-1-1   |
      |    4          | name_4        | 14.2          |  true         | 2004-1-1   |
      |    6          | name_6        | 16.2          |  true         | 2006-1-1   |
      |    7          | name_7        | 17.2          |  true         | 2007-1-1   |
      |    8          | name_8        | 18.2          |  true         | 2008-1-1   |
      |    9          | name_9        | 19.2          |  true         | 2009-1-1   |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT ident, names[0] as nombre FROM tablearray WHERE names[0] NOT BETWEEN 'name_03' AND 'name_09';
    When I execute 'SELECT ident, names[0] as nombre FROM tablearray WHERE names[0]  NOT BETWEEN 'name_03' AND 'name_09''
    Then The spark result has to have '3' rows:
      | ident-integer | nombre-string   |
      |    0          | names_00        |
      |    1          | names_01        |
      |    2          | names_02        |
