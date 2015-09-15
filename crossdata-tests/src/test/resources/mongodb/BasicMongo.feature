@PrepareMongoDBEnvironment
@CleanMongoDBEnvironment
Feature: Select MongoDB Feature
  In order to test XDContext connection to MongoDB
  As a Spark User
  I want to execute Select functions

    Background:
      Given a DATASOURCE_HOST in the System Variable "MongoDBHost"

    Scenario Outline: Basic Select
      Given a table "students" with the provider "com.stratio.crossdata.sql.sources.mongodb" and options " host '%DATASOURCE_HOST:27017', database 'highschool', collection 'students'"
      When I query <query>
      Then the xdContext return <result> rows;

      Examples:
      |query                                                              |result |
      |"SELECT comment as b FROM students WHERE _id = 1"                  |  1    |
      |"SELECT * FROM students"                                           |  10   |
      |"SELECT comment as b FROM students WHERE _id IN(1,2,3,4) limit 2"  |  2    |
      |"SELECT comment as b FROM students WHERE comment = 1 AND _id = 5"  |  0    |
