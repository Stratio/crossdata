Feature: MongoSelectAnd
  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_0';
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_0''
    Then The result has to have '1' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_8';
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_8''
    Then The result has to have '0' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2;
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2'
    Then The result has to have '1' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND money = 10.2;
    When I execute 'SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND money = 10.2'
    Then The result has to have '1' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2 AND new = true;
    When I execute 'SELECT * FROM tabletest WHERE ident = 0 AND name='name_0' AND money = 10.2 AND new = true'
    Then The result has to have '1' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true);
    When I execute 'SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true)'
    Then The result has to have '1' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |

  Scenario: [MONGO NATIVE] SELECT * FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true);
    When I execute 'SELECT date FROM tabletest WHERE (ident = 0 AND name='name_0') AND (money = 10.2 AND new = true)'
    Then The result has to have '1' rows:
     | date-date  |
     | 1999-11-30 |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT * FROM tablearray WHERE names[0] = 'names_00' AND names[1] = 'names_10';
    When I execute 'SELECT * FROM tablearray WHERE names[0] = 'names_00' AND names[1] = 'names_10''
    Then The result has to have '1' rows:
      | ident-integer | names-array<string>   |
      |    0          | names_00,names_10,names_20,names_30,names_40   |

  Scenario: [CROSSDATA-74, CROSSDATA-201 : MONGO NATIVE] SELECT ident, names[0] as nombre FROM tablearray WHERE nombre = 'names_00' AND nombres = 'names_10';
    When I execute 'SELECT ident, names[0] as nombre, names[1] as nombres FROM tablearray WHERE nombre = 'names_00' AND nombres = 'names_10''
    Then The result has to have '1' rows:
      | ident-integer | nombre-string   | nombres-string |
      |    0          | names_00        | names_10       |