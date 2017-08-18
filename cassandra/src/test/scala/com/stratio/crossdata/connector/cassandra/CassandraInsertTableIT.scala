package com.stratio.crossdata.connector.cassandra

import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CassandraInsertTableIT extends CassandraInsertCollection {

  it should "insert a row using INSERT INTO table VALUES in Cassandra" in {
    _xdContext.sql(s"INSERT INTO $Table VALUES (20, 25, [(x -> 3)], ['proof'], 'proof description', true, (x->[1]), (a->2), 'Eve' )").collect() should be(Row(1) :: Nil)

    //EXPECTATION
    val results = sql(s"select * from $Table where id=20").collect()

    results should have length 1
    results should contain
    Row(20, 25, "proof description", true, "Eve",
      Seq("proof"), Map("a" -> "2"), List(Map("x" -> "1", "y" -> "1"),
        Map("z" -> "1")), Map("x" -> List("1", "2"), "y" -> List("3", "4")))
  }

  it should "insert a row using INSERT INTO table(schema) VALUES in Cassandra" in {
    _xdContext.sql(s"INSERT INTO $Table(id, age, name) VALUES (21, 25, 'Peter')").collect() should be(Row(1) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age, enrolled from $Table where id=21").collect()

    results should have length 1
    results should contain
    Row(21, 25, "Peter")
  }

  it should "insert multiple rows using INSERT INTO table VALUES in Cassandra" in {
    val query =
      s"""|INSERT INTO $Table VALUES
          |(22, 25, [(x -> 1)], [4,5], 'proof description', true, (x->[1,5]), (x -> 1), 'John' ),
          |(23, 1, [(x -> 7, y->8)], [1,2,3], 'other description', false, (x->[1]), (key -> value), 'James' ),
          |(24, 33, [(x -> 3)], [true,true], 'other fun description', false, (x->[1,9]), (z->1, a-> 2), 'July' )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be(Row(3) :: Nil)

    //EXPECTATION
    val results = sql(s"select id,age,comment,enrolled,name,array_test,map_test,array_map,map_array from $Table where id=22 or id=23 or id=24").collect()

    results should have length 3
    results should contain allOf(

      Row(22, 25, "proof description", true, "John", Seq("4", "5"),
        Map("x" -> "1"), Seq(Map("x" -> "1")), Map("x" -> Seq("1", "5"))),

      Row(23, 1, "other description", false, "James", Seq("1", "2", "3"),
        Map("key" -> "value"), Seq(Map("x" -> "7", "y" -> "8")), Map("x" -> Seq("1"))),

      Row(24, 33, "other fun description", false, "July", Seq("true", "true"),
        Map("z" -> "1", "a" -> "2"), Seq(Map("x" -> "3")), Map("x" -> Seq("1", "9")))
      )
  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in Cassandra" in {
    _xdContext.sql(s"INSERT INTO $Table(id, age, name, enrolled) VALUES ( 25, 50, 'Samantha', true),( 26, 1, 'Charlie', false)").collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age, name, enrolled from $Table where id=25 or id=26").collect()

    results should have length 2
    results should contain allOf(
      Row(25, 50, "Samantha", true),
      Row(26, 1, "Charlie", false)
      )

  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Arrays in Cassandra" in {
    val query =
      s"""|INSERT INTO $Table (id, age, name, enrolled, array_test) VALUES
          |(27, 55, 'Jules', true, [true, false]),
          |(28, 12, 'Martha', false, ['test1,t', 'test2'])
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age, name, enrolled, array_test from $Table where id=27 or id=28").collect()

    results should have length 2
    results should contain allOf(
      Row(27, 55, "Jules", true, Seq("true", "false")),
      Row(28, 12, "Martha", false, Seq("test1,t", "test2"))
      )
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Map in Cassandra" in {
    val query =
      s"""|INSERT INTO $Table (id, age, name, enrolled, map_test) VALUES
          |( 29, 12, 'Albert', true, (x->1, y->2, z->3) ),
          |( 30, 20, 'Alfred', false, (xa->1, ya->2, za->3,d -> 5) )
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age, name, enrolled, map_test from $Table where id=29 or id=30").collect()

    results should have length 2
    results should contain allOf(
      Row(29, 12, "Albert", true, Map("x" -> "1", "y" -> "2", "z" -> "3")),
      Row(30, 20, "Alfred", false, Map("xa" -> "1", "ya" -> "2", "za" -> "3", "d" -> "5"))
      )
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Array of Maps in Cassandra" in {
    val query =
      s"""|INSERT INTO $Table (id, age,name, enrolled, array_map) VALUES
          |(31, 1, 'Nikolai', true, [(x -> 3), (z -> 1)] ),
          |(32, 14, 'Ludwig', false, [(x -> 1, y-> 1), (z -> 1)] )
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age,name, enrolled, array_map from $Table where id=31 or id=32").collect()

    results should have length 2
    results should contain allOf(
      Row(31, 1, "Nikolai", true, Seq(Map("x" -> "3"), Map("z" -> "1"))),
      Row(32, 14, "Ludwig", false, Seq(Map("x" -> "1", "y" -> "1"), Map("z" -> "1")))
      )
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Map of Array in Cassandra" in {
    val query =
      s"""|INSERT INTO $Table (id, age,name, enrolled, map_array) VALUES
          |(33, 13, 'Svletana', true, ( x->[1], y-> [3,4] ) ),
          |(34, 17, 'Wolfang', false, ( x->[1,2], y-> [3] ) )
       """.stripMargin
    _xdContext.sql(query).collect() should be(Row(2) :: Nil)

    //EXPECTATION
    val results = sql(s"select id, age,name, enrolled, map_array from $Table where id=33 or id=34").collect()

    results should have length 2
    results should contain allOf(
      Row(33, 13, "Svletana", true, Map("x" -> Seq("1"), "y" -> Seq("3", "4"))),
      Row(34, 17, "Wolfang", false, Map("x" -> Seq("1", "2"), "y" -> Seq("3")))
      )
  }

}
