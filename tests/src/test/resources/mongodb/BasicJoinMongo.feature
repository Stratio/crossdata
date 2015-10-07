@PrepareMongoDBEnvironment
@CleanMongoDBEnvironment
Feature: Select Cassandra Feature
  In order to test the XDContext fordware to Spark
  As a Spark User
  I want to execute Select with joins

  Background:
    Given a DATASOURCE_HOST in the System Variable "MongoDBHost"


  Scenario Outline: Basic Join Select
    Given a table "students" with the provider "com.stratio.crossdata.connector.mongodb" and options " host '%DATASOURCE_HOST:27017', database 'highschool', collection 'students'"
    And a table "class" with the provider "com.stratio.crossdata.connector.mongodb" and options " host '%DATASOURCE_HOST:27017', database 'highschool', collection 'class'"
    When I query <query>
    Then the xdContext return <result> rows;

  Examples:
    |query                                                            |result |
    |"SELECT students.name, class.class_name FROM students inner join class ON (students._id = class.student_id)"              |  10   |
    |"SELECT students.name, class.class_name FROM students inner join class ON (students._id = class.student_id) where students._id = 1" |  1   |
