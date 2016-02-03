Feature: MongoSelectSimple

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest;
    When I execute 'SELECT * FROM tabletest'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |

  Scenario: [MONGO NATIVE] SELECT ident FROM tabletest;
    When I execute 'SELECT ident FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [MONGO NATIVE ALIAS INTEGER COLUMN] SELECT ident AS identificador FROM tabletest;
    When I execute 'SELECT ident AS identificador FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [MONGO NATIVE ALIAS STRING COLUMN] SELECT name AS nombre FROM tabletest;
    When I execute 'SELECT name as nombre FROM tabletest'
    Then The result has to have '10' rows:
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
  Scenario: [MONGO NATIVE] SELECT ident,name FROM tabletest;
    When I execute 'SELECT ident, name FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [MONGO NATIVE] SELECT name,ident FROM tabletest;
    When I execute 'SELECT name, ident FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [MONGO NATIVE] SELECT ident AS identificador,name AS nombre FROM tabletest;
    When I execute 'SELECT ident AS identificador,name AS nombre FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [MONGO NATIVE] SELECT name, money,ident FROM tabletest;
    When I execute 'SELECT name, money,ident FROM tabletest'
    Then The result has to have '10' rows:
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


  Scenario: [MONGO NATIVE] SELECT ident,name,money FROM tabletest;
    When I execute 'SELECT ident,name,money FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [MONGO NATIVE] SELECT ident,name,money,new FROM tabletest;
    When I execute 'SELECT ident,name,money,new FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [MONGO NATIVE] SELECT SELECT name,money,ident,new new FROM tabletest;
    When I execute 'SELECT name,money,ident,new FROM tabletest'
    Then The result has to have '10' rows:
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

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT ident,name,money,new,date FROM tabletest;
    When I execute 'SELECT ident,name,money,new,date FROM tabletest'
    Then The result has to have '10' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    1          | name_1        | 11.2          |  true         | 2001-01-01 |
      |    2          | name_2        | 12.2          |  true         | 2002-02-02 |
      |    3          | name_3        | 13.2          |  true         | 2003-03-03 |
      |    4          | name_4        | 14.2          |  true         | 2004-04-04 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |
      |    6          | name_6        | 16.2          |  true         | 2006-06-06 |
      |    7          | name_7        | 17.2          |  true         | 2007-07-07 |
      |    8          | name_8        | 18.2          |  true         | 2008-08-08 |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT * FROM tablearray;
    When I execute 'SELECT * FROM tablearray'
    Then The result has to have '10' rows:
      | ident-integer | names-array<string>   |
      |    0          | names_00,names_10,names_20,names_30,names_40   |
      |    1          | names_01,names_11,names_21,names_31,names_41   |
      |    2          | names_02,names_12,names_22,names_32,names_42   |
      |    3          | names_03,names_13,names_23,names_33,names_43   |
      |    4          | names_04,names_14,names_24,names_34,names_44   |
      |    5          | names_05,names_15,names_25,names_35,names_45   |
      |    6          | names_06,names_16,names_26,names_36,names_46   |
      |    7          | names_07,names_17,names_27,names_37,names_47   |
      |    8          | names_08,names_18,names_28,names_38,names_48   |
      |    9          | names_09,names_19,names_29,names_39,names_49   |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT ident, names FROM tablearray;
    When I execute 'SELECT ident, names FROM tablearray'
    Then The result has to have '10' rows:
      | ident-integer | names-array<string>   |
      |    0          | names_00,names_10,names_20,names_30,names_40   |
      |    1          | names_01,names_11,names_21,names_31,names_41   |
      |    2          | names_02,names_12,names_22,names_32,names_42   |
      |    3          | names_03,names_13,names_23,names_33,names_43   |
      |    4          | names_04,names_14,names_24,names_34,names_44   |
      |    5          | names_05,names_15,names_25,names_35,names_45   |
      |    6          | names_06,names_16,names_26,names_36,names_46   |
      |    7          | names_07,names_17,names_27,names_37,names_47   |
      |    8          | names_08,names_18,names_28,names_38,names_48   |
      |    9          | names_09,names_19,names_29,names_39,names_49   |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT names FROM tablearray;
    When I execute 'SELECT  names FROM tablearray'
    Then The result has to have '10' rows:
      | names-array<string>   |
      | names_00,names_10,names_20,names_30,names_40   |
      | names_01,names_11,names_21,names_31,names_41   |
      | names_02,names_12,names_22,names_32,names_42   |
      | names_03,names_13,names_23,names_33,names_43   |
      | names_04,names_14,names_24,names_34,names_44   |
      | names_05,names_15,names_25,names_35,names_45   |
      | names_06,names_16,names_26,names_36,names_46   |
      | names_07,names_17,names_27,names_37,names_47   |
      | names_08,names_18,names_28,names_38,names_48   |
      | names_09,names_19,names_29,names_39,names_49   |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT names as array FROM tablearray;
    When I execute 'SELECT  names as array FROM tablearray'
    Then The result has to have '10' rows:
      | array-array<string>   |
      | names_00,names_10,names_20,names_30,names_40   |
      | names_01,names_11,names_21,names_31,names_41   |
      | names_02,names_12,names_22,names_32,names_42   |
      | names_03,names_13,names_23,names_33,names_43   |
      | names_04,names_14,names_24,names_34,names_44   |
      | names_05,names_15,names_25,names_35,names_45   |
      | names_06,names_16,names_26,names_36,names_46   |
      | names_07,names_17,names_27,names_37,names_47   |
      | names_08,names_18,names_28,names_38,names_48   |
      | names_09,names_19,names_29,names_39,names_49   |

  Scenario: [CROSSDATA-257] SELECT * FROM composetable;
    When I execute 'SELECT * FROM composetable'
    Then The flattened result has to have '2' rows:
      | ident-integer | person.name-string | person.daughter.name-string | person.daughter.age-integer |
      | 0             | Hugo               | Juan                        | 12                   |
      | 0             | Hugo               | Pepe                        | 13                   |

  Scenario: [CROSSDATA-257] SELECT * FROM composetable;
    When I execute 'SELECT ident, person.name, person.daughter[0].name FROM composetable'
    Then The flattened result has to have '1' rows:
      | ident-integer | person.name-string | _c2-string     |
      | 0             | Hugo               | Juan           |

  Scenario:[CROSSDATA-282] SELECT new AS alias, count(*) as count FROM tabletest GROUP BY alias
    When I execute 'SELECT new AS alias, count(*) as count FROM tabletest GROUP BY alias'
    Then The spark result has to have '1' rows:
      | alias-boolean | count-long |
      |true           | 10            |

