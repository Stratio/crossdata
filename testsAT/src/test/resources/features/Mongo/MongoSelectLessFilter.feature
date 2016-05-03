Feature: MongoSelectLessFilter

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE ident < 1;
    When I execute 'SELECT * FROM tabletest WHERE ident < 1'
    Then The result has to have '1' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         |  2000-1-1 |

  Scenario: [MONGO NATIVE] SELECT * FROM tabletest WHERE ident < 0;
    When I execute 'SELECT * FROM tabletest WHERE ident < 0'
    Then The result has to have '0' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |


  Scenario: [MONGO NATIVE] SELECT ident AS identificador FROM tabletest WHERE ident < 1;
    When I execute 'SELECT ident AS identificador FROM tabletest WHERE ident < 1'
    Then The result has to have '1' rows:
      | identificador-integer |
      |    0                  |


  Scenario: [MONGO NATIVE] SELECT name AS nombre FROM tabletest WHERE name < 'name_1';
    When I execute 'SELECT name AS nombre FROM tabletest WHERE name < 'name_1''
    Then The result has to have '1' rows:
      | nombre-string |
      |    name_0     |

  Scenario: [MONGO NATIVE] SELECT money FROM tabletest WHERE money < 11.2;
    When I execute 'SELECT money FROM tabletest WHERE money < 11.2'
    Then The result has to have '1' rows:
      | money-double  |
      | 10.2          |

  Scenario: [MONGO NATIVE] SELECT money FROM tabletest WHERE money < 10.201;
    When I execute 'SELECT money FROM tabletest WHERE money < 10.201'
    Then The result has to have '1' rows:
      | money-double  |
      | 10.2          |

  Scenario: [MONGO NATIVE] SELECT new FROM tabletest WHERE new < false;
    When I execute 'SELECT new FROM tabletest WHERE new < false'
    Then The result has to have '0' rows:
      |  new-boolean  |

  Scenario: [CROSSDATA-79,CROSSDATA-81 : MONGO NATIVE] SELECT date FROM tabletest WHERE date < '2000-1-2';
    When I execute 'SELECT date FROM tabletest WHERE date < '2000-1-2''
    Then The result has to have '1' rows:
      | date-date  |
      |  2000-1-1 |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT * FROM tablearray WHERE names[0] < 'names_08';
    When I execute 'SELECT * FROM tablearray WHERE names[0] < 'names_08''
    Then The result has to have '8' rows:
      | ident-integer | names-array<string>   |
      |    0          | names_00,names_10,names_20,names_30,names_40   |
      |    1          | names_01,names_11,names_21,names_31,names_41   |
      |    2          | names_02,names_12,names_22,names_32,names_42   |
      |    3          | names_03,names_13,names_23,names_33,names_43   |
      |    4          | names_04,names_14,names_24,names_34,names_44   |
      |    5          | names_05,names_15,names_25,names_35,names_45   |
      |    6          | names_06,names_16,names_26,names_36,names_46   |
      |    7          | names_07,names_17,names_27,names_37,names_47   |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT ident, names[0] FROM tablearray WHERE names[0] < 'names_08';
    When I execute 'SELECT ident, names[0] FROM tablearray WHERE names[0]  < 'names_08''
    Then The result has to have '8' rows:
      | ident-integer | _c1-string   |
      |    0          | names_00     |
      |    1          | names_01     |
      |    2          | names_02     |
      |    3          | names_03     |
      |    4          | names_04     |
      |    5          | names_05     |
      |    6          | names_06     |
      |    7          | names_07     |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT ident, names[0] as nombre FROM tablearray WHERE nombre < 'names_08';
    When I execute 'SELECT ident, names[0] as nombre FROM tablearray WHERE names[0]  < 'names_08''
    Then The result has to have '8' rows:
      | ident-integer | nombre-string   |
      |    0          | names_00        |
      |    1          | names_01        |
      |    2          | names_02        |
      |    3          | names_03        |
      |    4          | names_04        |
      |    5          | names_05        |
      |    6          | names_06        |
      |    7          | names_07        |

