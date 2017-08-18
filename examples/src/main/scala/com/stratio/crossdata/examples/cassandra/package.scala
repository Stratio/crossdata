package com.stratio.crossdata.examples

import com.datastax.driver.core.{Session, Cluster}
import com.stratio.crossdata.examples.cassandra.CassandraExample._

package object cassandra {

  trait CassandraDefaultConstants {
    val ClusterName = "Test Cluster"
    val Catalog = "highschool"
    val Table = "students"
    val CassandraHost = "127.0.0.1"
    val SourceProvider = "com.stratio.crossdata.connector.cassandra"// Cassandra provider => org.apache.spark.sql.cassandra
  }

  def prepareEnvironment(): (Cluster, Session) = {
    val (cluster, session) = createSession()
    buildTable(session)
    (cluster, session)
  }

  def cleanEnvironment(cluster: Cluster, session: Session) = {
    cleanData(session)
    closeSession(cluster, session)
  }

  private def createSession(): (Cluster, Session) = {
    val cluster = Cluster.builder().addContactPoint(CassandraHost).build()
    (cluster, cluster.connect())
  }

  private def buildTable(session: Session): Unit = {

    session.execute(s"CREATE KEYSPACE $Catalog WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}  AND durable_writes = true;")
    session.execute(s"CREATE TABLE $Catalog.$Table (id int PRIMARY KEY, age int, comment text, enrolled boolean, name text)")

    for (a <- 1 to 10) {
      session.execute("INSERT INTO " + Catalog + "." + Table + " (id, age, comment, enrolled, name) VALUES " +
        "(" + a + ", " + (10 + a) + ", 'Comment " + a + "', " + (a % 2 == 0) + ", 'Name " + a + "')")
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
