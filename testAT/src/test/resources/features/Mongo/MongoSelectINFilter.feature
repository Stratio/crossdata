Feature: MongoSelectINFilter

  Scenario: [CROSSDATA-74 : MONGO NATIVE] SELECT * FROM tabletest WHERE ident IN (0,10,5,27);
    When I execute 'SELECT * FROM tabletest WHERE ident IN (0,10,5,27)'
    Then The result has to have '2' rows:
      | ident-integer | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |
      |    5          | name_5        | 15.2          |  true         | 2005-05-05 |

  Scenario: [MONGO NATIVE] SELECT ident FROM tabletest WHERE ident IN (0,10,5,27);
    When I execute 'SELECT ident FROM tabletest WHERE ident IN (0,10,5,27)'
    Then The result has to have '2' rows:
      | ident-integer |
      |    0          |
      |    5          |

  Scenario: [MONGO NATIVE] SELECT ident FROM tabletest WHERE ident IN (10,27);
    When I execute 'SELECT ident FROM tabletest WHERE ident IN (10,27)'
    Then The result has to have '0' rows:
      | ident-integer |

  Scenario: [MONGO NATIVE] SELECT name FROM tabletest WHERE name IN ('name_0','name_10','name_5','name_27');
    When I execute 'SELECT name FROM tabletest WHERE name IN ('name_0','name_10','name_5','name_27')'
    Then The result has to have '2' rows:
      | name-string |
      |    name_0   |
      |    name_5   |

  Scenario: [MONGO NATIVE] SELECT name FROM tabletest WHERE name IN  ('name_10','name_27');
    When I execute 'SELECT name FROM tabletest WHERE name IN  ('name_10','name_27')'
    Then The result has to have '0' rows:
      | name-string |

  Scenario: [MONGO NATIVE] SELECT money FROM tabletest WHERE money IN (10.2, 10.25, 15.2, 17.00);
    When I execute 'SELECT money FROM tabletest WHERE money IN (10.2, 10.25, 15.2, 17.00)'
    Then The result has to have '2' rows:
      | money-double  |
      |    10.2   |
      |    15.2   |

  Scenario: [MONGO NATIVE] SELECT money FROM tabletest WHERE money IN (10.201,15.201);
    When I execute 'SELECT money FROM tabletest WHERE money IN (10.201,15.201)'
    Then The result has to have '0' rows:
      | money-double  |

  Scenario: [MONGO NATIVE] SELECT new FROM tabletest WHERE new IN (true);
    When I execute 'SELECT new FROM tabletest WHERE new IN (true)'
    Then The result has to have '10' rows:
      | new-boolean |
      |true         |
      |true         |
      |true         |
      |true         |
      |true         |
      |true         |
      |true         |
      |true         |
      |true         |
      |true         |

  Scenario: [MONGO NATIVE] SELECT new FROM tabletest WHERE new IN (false);
    When I execute 'SELECT new FROM tabletest WHERE new IN (false)'
    Then The result has to have '0' rows:
      | new-boolean |

  Scenario: [MONGO NATIVE] SELECT date FROM tabletest WHERE date IN ('1999-11-30','1998-12-25','2005-05-05','2008-2-27');
    When I execute 'SELECT date FROM tabletest WHERE date IN ('1999-11-30','1998-12-25','2005-05-05','2008-2-27')'
    Then The result has to have '2' rows:
       | date-date  |
       | 1999-11-30 |
       | 2005-05-05 |

  Scenario: [MONGO NATIVE] SELECT date FROM tabletest WHERE date IN ('1998-12-25','2008-2-27');
    When I execute 'SELECT date FROM tabletest WHERE date IN ('1998-12-25','2008-2-27')'
    Then The result has to have '0' rows:
      | date-date  |