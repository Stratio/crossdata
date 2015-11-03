Feature: Select with funciont concat

  Scenario: SELECT concat(id) FROM catalogTest.tableTest;
    When I execute a query: 'SELECT concat(id) FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.concat-concat-String |
      | 1                                          |
      | 2                                          |
      | 3                                          |
      | 4                                          |
      | 5                                          |
      | 6                                          |
      | 7                                          |
      | 8                                          |
      | 9                                          |
      | 10                                         |

  Scenario: SELECT concat(name,id) as concatenacion FROM catalogTest.tableTest;
    When I execute a query: 'SELECT concat(name,id) as concatenacion FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.concatenacion-concatenacion-String |
      | name_11                                                  |
      | name_22                                                  |
      | name_33                                                  |
      | name_44                                                  |
      | name_55                                                  |
      | name_66                                                  |
      | name_77                                                  |
      | name_88                                                  |
      | name_99                                                  |
      | name_1010                                                |

  Scenario: SELECT concat(name,id) as concatenacion FROM catalogTest.tableTest;
    When I execute a query: 'SELECT concat(name,age) as concatenacion FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.concatenacion-concatenacion-String |
      | name_110                                                 |
      | name_220                                                 |
      | name_330                                                 |
      | name_440                                                 |
      | name_550                                                 |
      | name_660                                                 |
      | name_770                                                 |
      | name_880                                                 |
      | name_990                                                 |
      | name_101                                                 |

  Scenario: SELECT concat(name,id) as concatenacion FROM catalogTest.tableTest;
    When I execute a query: 'SELECT concat(name,age) as concatenacion , concat(name,id) AS nameid FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.concatenacion-concatenacion-String | catalogTest.tableTest.nameid-nameid-String |
      | name_110                                                 | name_11                                    |
      | name_220                                                 | name_22                                    |
      | name_330                                                 | name_33                                    |
      | name_440                                                 | name_44                                    |
      | name_550                                                 | name_55                                    |
      | name_660                                                 | name_66                                    |
      | name_770                                                 | name_77                                    |
      | name_880                                                 | name_88                                    |
      | name_990                                                 | name_99                                    |
      | name_101                                                 | name_1010                                  |

  Scenario: SELECT concat(name,id) as concatenacion FROM catalogTest.tableTest;
    When I execute a query: 'SELECT concat(name,'Hugo') as concatenacion FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.concatenacion-concatenacion-String |
      | name_1Hugo                                               |
      | name_2Hugo                                               |
      | name_3Hugo                                               |
      | name_4Hugo                                               |
      | name_5Hugo                                               |
      | name_6Hugo                                               |
      | name_7Hugo                                               |
      | name_8Hugo                                               |
      | name_9Hugo                                               |
      | name_10Hugo                                              |
      
      
      Scenario: SELECT concat(name,9999) as concatenacion FROM catalogTest.tableTest;
    When I execute a query: 'SELECT concat(name,9999) as concatenacion FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.concatenacion-concatenacion-String |
      | name_19999                                               |
      | name_29999                                               |
      | name_39999                                               |
      | name_49999                                               |
      | name_59999                                               |
      | name_69999                                               |
      | name_79999                                               |
      | name_89999                                               |
      | name_99999                                               |
      | name_109999                                              |
      
       Scenario: SELECT concat('Hugo',9999) as concatenacion FROM catalogTest.tableTest;
    When I execute a query: 'SELECT concat('Hugo',9999) as concatenacion FROM catalogTest.tableTest;'
    Then the result has to be:
      | catalogTest.tableTest.concatenacion-concatenacion-String |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                               |
      | Hugo9999                                              |
