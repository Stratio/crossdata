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

class ElasticInsertTableIT extends ElasticWithSharedContext {

  //(id INT, age INT, description STRING, enrolled BOOLEAN, name STRING, optionalField BOOLEAN, birthday DATE, salary DOUBLE, ageInMilis LONG)


  it should "insert a row using INSERT INTO table VALUES in ElasticSearch" in {
    _xdContext.sql(s"INSERT INTO $Type VALUES (200, 25, 'proof description', true, 'pepe', false, '2015-01-01' , 1200.00, 1463646640046)").collect() should be (Row(1)::Nil)
  }

  it should "insert a row using INSERT INTO table(schema) VALUES in ElasticSearch" in {
    _xdContext.sql(s"INSERT INTO $Type(age,name, enrolled) VALUES ( 25, 'pepe', true)").collect() should be (Row(1)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table VALUES in ElasticSearch" in {
    val query = s"""|INSERT INTO $Type VALUES
                    |(200, 25, 'proof description', true, 'pepe123', false, '2015-01-01' , 1200.00, 1463626640046) ,
                    |(15, 1, 'other description', false, 'pepe23', true, '2015-01-05' , 1400.00, 1461646640046) ,
                    |(6, 33, 'other fun description', false, 'pepe123', false, '2015-01-08' , 1400.00, 1463046640046 )
       """.stripMargin
    val rows: Array[Row] = _xdContext.sql(query).collect()
    rows should be (Row(3)::Nil)
  }

  it should "insert multiple rows using INSERT INTO table(schema) VALUES in ElasticSearch" in {
    _xdContext.sql(s"INSERT INTO $Type (age,name, enrolled) VALUES ( 252, 'pepe2', true),( 1, 'asd', false)").collect() should be (Row(2)::Nil)
  }

}
