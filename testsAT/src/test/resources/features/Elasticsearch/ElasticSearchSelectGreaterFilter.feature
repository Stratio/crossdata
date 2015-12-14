Feature: ElasticSearchSelectGreaterFilter

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT * FROM tabletest WHERE ident > 8;
    When I execute 'SELECT * FROM tabletest WHERE ident > 8'
    Then The result has to have '1' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-date  |
      |    9          | name_9        | 19.2          |  true         | 2009-09-09 |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT * FROM tabletest WHERE ident > 9;
    When I execute 'SELECT * FROM tabletest WHERE ident > 9'
    Then The result has to have '0' rows:
      | ident-long | name-string   | money-double  |  new-boolean  | date-date  |


  Scenario: [CROSSDATA-18: ES NATIVE] SELECT ident AS identificador FROM tabletest WHERE ident > 8;
    When I execute 'SELECT ident AS identificador FROM tabletest WHERE ident > 8'
    Then The result has to have '1' rows:
      | identificador-long |
      |    9                  |


  Scenario: [CROSSDATA-18: ES NATIVE] SELECT name AS nombre FROM tabletest WHERE name > 'name_8';
    When I execute 'SELECT name AS nombre FROM tabletest WHERE name > 'name_8''
    Then The result has to have '1' rows:
      | nombre-string |
      |    name_9     |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT money FROM tabletest WHERE money > 18.2;
    When I execute 'SELECT money FROM tabletest WHERE money > 18.2'
    Then The result has to have '1' rows:
      | money-double  |
      | 19.2          |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT money FROM tabletest WHERE money > 19.199;
    When I execute 'SELECT money FROM tabletest WHERE money > 19.199'
    Then The result has to have '1' rows:
      | money-double  |
      | 19.2          |

  Scenario: [CROSSDATA-18: ES NATIVE] SELECT new FROM tabletest WHERE new > false;
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


  Scenario: [CROSSDATA-18: ES NATIVE] SELECT date FROM tabletest WHERE date > '2009-08-09';
    When I execute 'SELECT date FROM tabletest WHERE date > '2009-08-09''
    Then The result has to have '1' rows:
      | date-date   |
      |  2009-09-09 |
