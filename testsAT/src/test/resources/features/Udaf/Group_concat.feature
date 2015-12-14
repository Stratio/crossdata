Feature: UDAF - GROUP CONCAT


  Scenario: [CROSSDATA-178] SELECT group_concat(name) FROM tabletest;
    When I execute 'SELECT group_concat(name) FROM tabletest'
    Then The spark result has to have '1' rows:
      | _c0-string                                                                      |
      | name_0, name_1, name_2, name_3, name_4, name_5, name_6, name_7, name_8, name_9  |

  Scenario: [CROSSDATA-178] SELECT group_concat(ident) FROM tabletest;
    When I execute 'SELECT group_concat(ident) FROM tabletest'
    Then The spark result has to have '1' rows:
      | _c0-string                    |
      | 0, 1, 2, 3, 4, 5, 6, 7, 8, 9  |

  Scenario: [CROSSDATA-178] SELECT group_concat(money) FROM tabletest;
    When I execute 'SELECT group_concat(money) FROM tabletest'
    Then The spark result has to have '1' rows:
      | _c0-string                    |
      | 10.2, 11.2, 12.2, 13.2, 14.2, 15.2, 16.2, 17.2, 18.2, 19.2  |

  Scenario: [CROSSDATA-178] SELECT group_concat(true) FROM tabletest;
    When I execute 'SELECT group_concat(true) FROM tabletest'
    Then The spark result has to have '1' rows:
      | _c0-string                    |
      | true, true, true, true, true, true, true, true, true, true |

  Scenario: [CROSSDATA-178] SELECT group_concat(date) FROM tabletest;
    When I execute 'SELECT group_concat(date) FROM tabletest'
    Then The spark result has to have '1' rows:
      | _c0-string                    |
      | 1999-11-30, 2001-01-01, 2002-02-02, 2003-03-03, 2004-04-04, 2005-05-05, 2006-06-06, 2007-07-07, 2008-08-08, 2009-09-09 |

  Scenario: [CROSSDATA-178] SELECT group_concat(ident) FROM tabletest;
    When I execute 'SELECT group_concat(ident) FROM tabletest WHERE ident < 3'
    Then The spark result has to have '1' rows:
      | _c0-string  |
      | 0, 1, 2     |

  Scenario: [CROSSDATA-178] SELECT group_concat(name) FROM tabletest;
    When I execute 'SELECT new, group_concat(name) FROM tabletest GROUP BY new'
    Then The spark result has to have '1' rows:
      | new-boolean| _c1-string                                                                      |
      |    true    | name_0, name_1, name_2, name_3, name_4, name_5, name_6, name_7, name_8, name_9  |

