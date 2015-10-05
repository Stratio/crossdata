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
package cassandra

import com.datastax.driver.core.{TableMetadata, KeyspaceMetadata, Cluster, Session}
import cucumber.api.Scenario
import cucumber.api.scala.{EN, ScalaDsl}

sealed trait DefaultConstants {
  val ClusterName = "Test Cluster"
  val Catalog = "highschool"
  val Table = "students"
  val Table2 = "class"

  val CassandraHost = Option(System.getenv("CassandraHost")).getOrElse("127.0.0.1")

  val SourceProvider = "com.stratio.crossdata.connector.cassandra"
  // Cassandra provider => org.apache.spark.sql.cassandra
}

class CassandraTestUtils extends ScalaDsl with EN with DefaultConstants {

  Before("@PrepareCasandraEnvironment") { scenario: Scenario =>
    val (cluster, session) = createSession()
    buildTable(session)
    closeSession(cluster, session)
  }

  After("@CleanCasandraEnvironment") { scenario: Scenario =>
    val (cluster, session) = createSession()
    cleanEnvironment(cluster, session)
    closeSession(cluster, session)
  }

  private def cleanEnvironment(cluster: Cluster, session: Session) = {
    try {
      cleanData(session)
    } finally {
      closeSession(cluster, session)
    }
  }

  private def createSession(): (Cluster, Session) = {
    val cluster = Cluster.builder().addContactPoint(CassandraHost).build()
    (cluster, cluster.connect())
  }

  private def buildTable(session: Session): Unit = {

    session.execute(s"CREATE KEYSPACE IF NOT EXISTS $Catalog WITH replication = " +
      s"{'class':'SimpleStrategy', 'replication_factor':1}  AND durable_writes = true;")

    session.execute(s"CREATE TABLE IF NOT EXISTS $Catalog.$Table " +
      s"(id int PRIMARY KEY, age int,comment text, enrolled boolean, name text)")

    session.execute(s"CREATE TABLE IF NOT EXISTS $Catalog.$Table2 " +
      s"(class_id int PRIMARY KEY, student_id int, class_name text)")

    for (a <- 1 to 10) {
      session.execute("INSERT INTO " + Catalog + "." + Table + " (id, age, comment, enrolled, name) VALUES " +
        "(" + a + ", " + (10 + a) + ", 'Coment " + a + "', " + (a % 2 == 0) + ", 'Name " + a + "')")

      session.execute("INSERT INTO " + Catalog + "." + Table2 + " (class_id, student_id, class_name) VALUES " +
        "(" + a + ", " + a + ", 'Class Name " + a + "')")
    }
  }

  private def cleanData(session: Session): Unit = {
    session.execute(s"DROP KEYSPACE $Catalog")
  }

  private def closeSession(cluster: Cluster, session: Session): Unit = {
    session.close()
    cluster.close()
  }
}
