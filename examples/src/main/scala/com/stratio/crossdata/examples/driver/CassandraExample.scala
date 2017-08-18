package com.stratio.crossdata.examples.driver

import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.examples.cassandra._

/**
 * Driver example - Cassandra
 */

sealed trait DefaultConstants {
  val ClusterName = "Test Cluster"
  val Catalog = "highschool"
  val Table = "students"
  val CassandraHost = "127.0.0.1"
  val SourceProvider = "cassandra"
  // Cassandra provider => org.apache.spark.sql.cassandra
  val CassandraOptions = Map(
    "cluster" -> ClusterName,
    "spark_cassandra_connection_host" -> CassandraHost
  )
}

object DriverExample extends App with DefaultConstants {

  val (cluster, session) = prepareEnvironment()

  var driver: Option[Driver] = None

  val driverConf = new DriverConf().
    setFlattenTables(false).
    setTunnelTimeout(30).
    setClusterContactPoint("127.0.0.1:13420", "127.0.0.1:13425")

  try {

    driver = Option(Driver.newSession(driverConf))

    for {
      xdDriver <- driver
    } {
      xdDriver.importTables(SourceProvider, CassandraOptions).waitForResult() match {
        case result: SuccessfulSQLResult => println("Successful importation")
        case error: ErrorSQLResult => sys.error(error.message)
      }
      xdDriver.listTables().foreach(println(_))
      xdDriver.show(s"SELECT * FROM $Catalog.$Table")
    }

  } finally {
    driver.foreach(_.closeSession())
    Driver.shutdown()
    cleanEnvironment(cluster, session)
  }

}
