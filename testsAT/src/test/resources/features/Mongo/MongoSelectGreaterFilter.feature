Feature: MongoSelectGreaterFilter

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE ident > 8;
    When I execute 'SELECT * FROM tabletest WHERE ident > 8'
    Then The result has to have '1' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |

  Scenario: [MONGO NATIVE] SELECT * FROM tabletest WHERE ident > 9;
    When I execute 'SELECT * FROM tabletest WHERE ident > 9'
    Then The result has to have '0' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |


  Scenario: [MONGO NATIVE] SELECT ident AS identificador FROM tabletest WHERE ident > 8;
    When I execute 'SELECT ident AS identificador FROM tabletest WHERE ident > 8'
    Then The result has to have '1' rows:
      | identificador-integer |
      |    9                  |


  Scenario: [MONGO NATIVE] SELECT name AS nombre FROM tabletest WHERE name > 'name_8';
    When I execute 'SELECT name AS nombre FROM tabletest WHERE name > 'name_8''
    Then The result has to have '1' rows:
      | nombre-string |
      |    name_9     |

  Scenario: [MONGO NATIVE] SELECT money FROM tabletest WHERE money > 18.2;
    When I execute 'SELECT money FROM tabletest WHERE money > 18.2'
    Then The result has to have '1' rows:
      | money-double  |
      | 19.2          |

  Scenario: [MONGO NATIVE] SELECT money FROM tabletest WHERE money > 19.199;
    When I execute 'SELECT money FROM tabletest WHERE money > 19.199'
    Then The result has to have '1' rows:
      | money-double  |
      | 19.2          |

  Scenario: [MONGO NATIVE] SELECT new FROM tabletest WHERE new > false;
    When I execute 'SELECT new FROM tabletest WHERE new > false'
    Then The result has to have '10' rows:
      |  new-boolean  |
      |  true         |
      |  true         |
      |  true         |
      |  true         |
      |  true         |
      |  true         |
      |  true         |
      |  true         |
      |  true         |
      |  true         |


  Scenario: [CROSSDATA-79,CROSSDATA-81 : MONGO NATIVE] SELECT date FROM tabletest WHERE date > '2009-08-09';
    When I execute 'SELECT date FROM tabletest WHERE date > '2009-08-09''
    Then The result has to have '1' rows:
      | date-date   |
      |  2009-09-09 |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT * FROM tablearray WHERE names[0] > 'names_00';
    When I execute 'SELECT * FROM tablearray WHERE names[0] > 'names_00''
    Then The result has to have '9' rows:
      | ident-integer | names-array<string>   |

      |    1          | names_01,names_11,names_21,names_31,names_41   |
      |    2          | names_02,names_12,names_22,names_32,names_42   |
      |    3          | names_03,names_13,names_23,names_33,names_43   |
      |    4          | names_04,names_14,names_24,names_34,names_44   |
      |    5          | names_05,names_15,names_25,names_35,names_45   |
      |    6          | names_06,names_16,names_26,names_36,names_46   |
      |    7          | names_07,names_17,names_27,names_37,names_47   |
      |    8          | names_08,names_18,names_28,names_38,names_48   |
      |    9          | names_09,names_19,names_29,names_39,names_49   |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT ident, names[0] FROM tablearray WHERE names[0] > 'names_00';
    When I execute 'SELECT ident, names[0] FROM tablearray WHERE names[0]  > 'names_00''
    Then The result has to have '9' rows:
      | ident-integer | _c1-string   |
      |    1          | names_01     |
      |    2          | names_02     |
      |    3          | names_03     |
      |    4          | names_04     |
      |    5          | names_05     |
      |    6          | names_06     |
      |    7          | names_07     |
      |    8          | names_08     |
      |    9          | names_09     |


  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT ident, names[0] as nombre FROM tablearray WHERE nombre > 'names_00';
    When I execute 'SELECT ident, names[0] as nombre FROM tablearray WHERE names[0]  > 'names_00''
    Then The result has to have '9' rows:
      | ident-integer | nombre-string   |
      |    1          | names_01        |
      |    2          | names_02        |
      |    3          | names_03        |
      |    4          | names_04        |
      |    5          | names_05        |
      |    6          | names_06        |
      |    7          | names_07        |
      |    8          | names_08        |
      |    9          | names_09        |