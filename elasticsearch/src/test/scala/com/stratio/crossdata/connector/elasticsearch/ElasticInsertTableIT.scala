/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.stratio.crossdata.connector.elasticsearch

import com.sksamuel.elastic4s.ElasticDsl._
import org.apache.spark.sql.Row

class ElasticInsertTableIT extends ElasticInsertCollection {

  it should "insert a row using INSERT INTO table VALUES in ElasticSearch" in {
    val query = s"""|INSERT INTO $Type VALUES (20, 25, 'proof description', true, 'Eve', false, '2015-01-01' ,
                      |1200.00, 1463646640046, ['proof'], (a->2))""".stripMargin

    _xdContext.sql(query).collect() should be (Row(1)::Nil)
  }

  it should "insert a row using INSERT INTO table(schema) VALUES in ElasticSearch" in {
    _xdContext.sql(s"INSERT INTO $Type(age, name, enrolled) VALUES ( 25, 'Peter', true)").collect() should be (Row(1)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table VALUES in ElasticSearch" in {
    val query = s"""|INSERT INTO $Type VALUES
                    |(21, 25, 'proof description', true, 'John', false, '2015-01-01' , 1200.00, 1463626640046, [4,5], (x -> 1)) ,
                    |(22, 1, 'other description', false, 'James', true, '2015-01-05' , 1400.00, 1461646640046, [1,2,3], (key -> value)) ,
                    |(23, 33, 'other fun description', false, 'July', false, '2015-01-08' , 1400.00, 1463046640046, [true,true], (z->1, a-> 2) )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be (Row(3)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in ElasticSearch" in {
    _xdContext.sql(s"INSERT INTO $Type (age, name, enrolled) VALUES ( 50, 'Samantha', true),( 1, 'Charlie', false)").collect() should be (Row(2)::Nil)
  }


  it should "insert rows using INSERT INTO table(schema) VALUES with Arrays in ElasticSearch" in {
    val query = s"""|INSERT INTO $Type (age, name, enrolled, array_test) VALUES
                    |( 55, 'Jules', true, [true, false]),
                    |( 12, 'Martha', false, ['test1,t', 'test2'])
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

  it should "insert rows using INSERT INTO table(schema) VALUES with Map in ElasticSearch" in {
    val query = s"""|INSERT INTO $Type (age, name, enrolled, map_test) VALUES
                    |( 12, 'Albert', true, (x->1, y->2, z->3) ),
                    |( 20, 'Alfred', false, (xa->1, ya->2, za->3,d -> 5) )
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }

  /*it should "insert rows using INSERT INTO table(schema) VALUES with Map and Arrays in MongoDb" in {
    val query = s"""|INSERT INTO $Collection (age,name, enrolled, map_test) VALUES
                    |( 1, 'Nikolai', true, (x->[1,2], y->2, z->3) ),
                    |( 14, 'Ludwig', false, (xa->1, ya->[5, 7, 8], za->3,d -> 5) )
       """.stripMargin
    _xdContext.sql(query).collect() should be (Row(2)::Nil)
  }*/

}
