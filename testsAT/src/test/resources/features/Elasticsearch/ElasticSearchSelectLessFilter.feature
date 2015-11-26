Feature: ElasticSearchSelectLessFilter

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT * FROM tabletest WHERE ident < 1;
    When I execute 'SELECT * FROM tabletest WHERE ident < 1'
    Then The result has to have '1' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-date  |
      |    0          | name_0        | 10.2          |  true         | 1999-11-30 |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT * FROM tabletest WHERE ident < 0;
    When I execute 'SELECT * FROM tabletest WHERE ident < 0'
    Then The result has to have '0' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-date  |


  Scenario: [CROSSDATA-18: ES NATIVE] SELECT ident AS identificador FROM tabletest WHERE ident < 1;
    When I execute 'SELECT ident AS identificador FROM tabletest WHERE ident < 1'
    Then The result has to have '1' rows:
      | identificador-long |
      |    0                  |


  Scenario: [MONGO NATIVE] SELECT name AS nombre FROM tabletest WHERE name < 'name_1';
    When I execute 'SELECT name AS nombre FROM tabletest WHERE name < 'name_1''
    Then The result has to have '1' rows:
      | nombre-string |
      |    name_0     |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT money FROM tabletest WHERE money < 11.2;
    When I execute 'SELECT money FROM tabletest WHERE money < 11.2'
    Then The result has to have '1' rows:
      | money-double  |
      | 10.2          |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT money FROM tabletest WHERE money < 10.201;
    When I execute 'SELECT money FROM tabletest WHERE money < 10.201'
    Then The result has to have '1' rows:
      | money-double  |
      | 10.2          |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT new FROM tabletest WHERE new < false;
    When I execute 'SELECT new FROM tabletest WHERE new < false'
    Then The result has to have '0' rows:
      |  new-boolean  |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT date FROM tabletest WHERE date < '1999-12-10';
    When I execute 'SELECT date FROM tabletest WHERE date < '1999-12-10''
    Then The result has to have '1' rows:
      | date-date  |
      | 1999-11-30 |
