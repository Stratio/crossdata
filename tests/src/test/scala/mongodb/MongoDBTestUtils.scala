/**
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
package mongodb

import com.datastax.driver.core.Session
import com.mongodb.casbah.Imports
import cucumber.api.Scenario
import cucumber.api.scala.{EN, ScalaDsl}

import com.mongodb.casbah.Imports._

sealed trait DefaultConstants {

  val Catalog = "highschool"
  val Table = "students"
  val Table2 = "class"

  val Host = Option(System.getenv("MongoHost")).getOrElse("127.0.0.1")

  val SourceProvider = "com.stratio.crossdata.connector.cassandra"
  // Cassandra provider => org.apache.spark.sql.cassandra
}

class MongoDBTestUtils extends ScalaDsl with EN with DefaultConstants{



  Before("@PrepareMongoDBEnvironment") { scenario: Scenario =>
    val (client, database) = getDatabase(Catalog)

    buildTable(database)

    closeSession(client, database)
  }

  After("@CleanMongoDBEnvironment") { scenario: Scenario =>
    val (client, database) = getDatabase(Catalog)
    cleanEnvironment(client, database)
    closeSession(client, database)
  }


  private def getDatabase(database:String): (MongoClient, MongoDB) = {
    val client =  MongoClient(Host, 27017)

    (client, client(database))
  }

  private def closeSession(client: MongoClient, db: MongoDB): Unit = {
      client.close()
  }

  private def buildTable(database: MongoDB): Unit = {

    val col1 = database(Table)
    val col2 = database(Table2)

    for (a <- 1 to 10) {
      col1.insert(MongoDBObject("_id" -> a, "age" -> (10+a), "comment" -> s"Coment $a", "enrolled" -> (a % 2 == 0), "name" -> s"Name $a" ))
      col2.insert(MongoDBObject("_id" -> a, "student_id" -> a, "class_name"-> s"Class Name $a"));
    }
  }

  private def cleanEnvironment(client: MongoClient, database: MongoDB): Unit ={
      database.dropDatabase()
  }
}
