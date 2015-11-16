@ignore @unimplemented
Feature: Test delegate operations in mongoDB

  Scenario: Attach cluster and connectors
    Given I run the shell command "ATTACH CLUSTER testCluster ON DATASTORE Mongo WITH OPTIONS {'Hosts': '[172.31.13.46,172.31.15.233,172.31.5.190]', 'Port': '[27017,27017,27017]'};"
    Then I expect a 'Cluster attached successfully' message
    Given I run the shell command "ATTACH CONNECTOR MongoConnector TO testCluster WITH OPTIONS {};"
    Then I expect a 'Connected to cluster successfully' message
    Given I run the shell command "ATTACH CONNECTOR SparkSQLConnector TO testCluster WITH OPTIONS {'DefaultLimit': '1000'};"
    Then I expect a 'Connected to cluster successfully' message

  Scenario: Create tables
    Given I run the shell command "CREATE TABLE myTable ON CLUSTER testCluster (id int primary key, name text);"
    Then I expect a 'TABLE created successfully' message
    Given I run the shell command "INSERT INTO myTable (id, name) VALUES (1, 'Hugo');"
    Then I expect a 'STORED successfully' message
    Given I run the shell command "INSERT INTO myTable (id, name) VALUES (2, 'Tony');"
    Then I expect a 'STORED successfully' message

   Scenario: [CROSSDATA-126] SELECT with JOIN and aliases
     Given I run the shell command "SELECT myTable.id, myTable2.id as id FROM myTable JOIN myTable2 ON myTable.id = myTable2.id;"
     Then the result has to be:
       | id | id |
       | 1  | 1  |
       | 2  | 2  |

  Scenario: [CROSSDATA-129 CROSSDATA-116] SELECT_CASE_WHEN
    Given I run the shell command "SELECT "id", CASE WHEN "id" < 2 THEN 'Hugo' WHEN "id" > 1 THEN 'Tony' ELSE 'Pepe' END FROM myTable;"
    Then the result has to be:
       | id |       |
       | 1  | Hugo  |
       | 2  | Tony  |

    Scenario: [CROSSDATA-130] SELECT NOT BETWEEN IN PRIMARY KEY
      Given I run the shell command "select * from myTable where id not between 3 and 4;"
      Then the result has to be:
       | id |  name |
       | 1  | Hugo  |
       | 2  | Tony  |

    Scenario: [CROSSDATA-131] SELECT NOT LIKE IN NOT PRIMARY KEY
      Given I run the shell command "select * from myTable where name not like 'Hugo';"
      Then the result has to be:
        | id |  name |
        | 2  | Tony  |

    Scenario: [CROSSDATA-133] SELECT NOT IN
      Given I run the shell command "select * from myTable where id not in [3,4];"
      Then the result has to be:
        | id |  name |
        | 1  | Hugo  |
        | 2  | Tony  |

    Scenario: [CROSSDATA-134] SELECT SUM NOT IN
      Given I run the shell command "ALTER TABLE myTable ADD amount INT"
      When I run the shell command "SELECT sum(amount) FROM myTable WHERE id NOT IN [1];"
      Then the result has to be:
        | sum |
        |  2  |

    Scenario: [CROSSDATA-135] SELECT SUM DISTINCT
      When I run the shell command "SELECT sum(amount) FROM myTable WHERE id <> 2;"
      Then the result has to be:
        | sum |
        |  1  |

     Scenario: [CROSSDATA-136] SELECT SUM NOT BETWEEN
      When I run the shell command "SELECT sum(amount) FROM myTable WHERE id NOT BETWEEN 0 AND 1;"
      Then the result has to be:
        | sum |
        |  2  |