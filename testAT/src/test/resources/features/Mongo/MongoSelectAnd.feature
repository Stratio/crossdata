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