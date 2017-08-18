package org.apache.spark.sql.crossdata.execution.datasources

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.XDContext
import org.apache.spark.sql.crossdata.catalyst.parser.XDDdlParser
import org.apache.spark.sql.crossdata.catalyst.streaming._
import org.apache.spark.sql.crossdata.config.StreamingConfig
import org.apache.spark.sql.crossdata.models.EphemeralQueryModel
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.mock.MockitoSugar

import scala.util.Try


@RunWith(classOf[JUnitRunner])
class StreamingDdlParserSpec extends BaseXDTest with StreamingDDLTestConstants with MockitoSugar {

  val xdContext = mock[XDContext]
  val parser = new XDDdlParser(_ => null, xdContext)


  // EPHEMERAL TABLE
  "StreamingDDLParser" should "parse a create ephemeral table" in {
    val logicalPlan =
      parser.parse(s"CREATE EPHEMERAL TABLE $EphemeralTableName (id STRING) OPTIONS (kafka.options.opKey 'value')")
    logicalPlan shouldBe CreateEphemeralTable(EphemeralTableIdentifier, Some(EphemeralTableSchema),  Map("kafka.options.opKey" -> "value"))
  }

  it should "parse a create ephemeral table without schema" in {
    val logicalPlan =
      parser.parse(s"CREATE EPHEMERAL TABLE $EphemeralTableName OPTIONS (kafka.options.opKey 'value')")
    logicalPlan shouldBe CreateEphemeralTable(EphemeralTableIdentifier, None,  Map("kafka.options.opKey" -> "value"))
  }

  it should "parse a describe ephemeral table" in {
    val logicalPlan = parser.parse(s"DESCRIBE EPHEMERAL TABLE $EphemeralTableName")
    logicalPlan shouldBe DescribeEphemeralTable(EphemeralTableIdentifier)
  }

  it should "parse a show ephemeral tables" in {
    val logicalPlan = parser.parse(s"SHOW EPHEMERAL TABLES")
    logicalPlan shouldBe ShowEphemeralTables
  }

  it should "parse a drop ephemeral table" in {
    val logicalPlan = parser.parse(s"DROP EPHEMERAL TABLE $EphemeralTableName")
    logicalPlan shouldBe DropEphemeralTable(EphemeralTableIdentifier)
  }

  it should "parse a drop all ephemeral tables" in {
    val logicalPlan = parser.parse(s"DROP ALL EPHEMERAL TABLES")
    logicalPlan shouldBe DropAllEphemeralTables
  }


  // STATUS
  it should "parse a show ephemeral status" in {
    val logicalPlan = parser.parse(s"SHOW EPHEMERAL STATUS IN $EphemeralTableName")
    logicalPlan shouldBe ShowEphemeralStatus(EphemeralTableIdentifier)
  }
  it should "parse a show all ephemeral statuses" in {
    val logicalPlan = parser.parse(s"SHOW EPHEMERAL STATUSES")
    logicalPlan shouldBe ShowAllEphemeralStatuses
  }

  // START AND STOP PROCESS
  it should "parse a start process" in {
    val logicalPlan = parser.parse(s"START $EphemeralTableName")
    logicalPlan shouldBe StartProcess(EphemeralTableIdentifier.unquotedString)

  }
  it should "parse a stop process" in {
    val logicalPlan = parser.parse(s"STOP $EphemeralTableName")
    logicalPlan shouldBe StopProcess(EphemeralTableIdentifier.unquotedString)
  }

  // QUERIES
  it should "parse a show ephemeral queries" in {
    val logicalPlan = parser.parse(s"SHOW EPHEMERAL QUERIES")
    logicalPlan shouldBe ShowEphemeralQueries(None)
  }


  it should "parse a show ephemeral queries with a specific table" in {
    val logicalPlan = parser.parse(s"SHOW EPHEMERAL QUERIES IN $EphemeralTableName")
    logicalPlan shouldBe ShowEphemeralQueries(Some(EphemeralTableIdentifier.unquotedString))
  }

  it should "fail parsing an add query statement without window" in {
    an [Exception] should be thrownBy parser.parse(s"ADD $Sql AS topic")
  }

  it should "parse a drop ephemeral query" in {
    val logicalPlan = parser.parse(s"DROP EPHEMERAL QUERY $QueryName")
    logicalPlan shouldBe DropEphemeralQuery(QueryName)

  }


  it should "parse a drop all ephemeral queries" in {
    val logicalPlan = parser.parse(s"DROP ALL EPHEMERAL QUERIES")
    DropAllEphemeralQueries()

  }

  it should "parse a drop all ephemeral queries in a specific table" in {
    val logicalPlan = parser.parse(s"DROP ALL EPHEMERAL QUERIES IN $EphemeralTableName")
    DropAllEphemeralQueries(Some(EphemeralTableName))

  }


}

trait StreamingDDLTestConstants {
  // Ephemeral table
  val EphemeralTableName = "epheTab"
  val EphemeralTableIdentifier = TableIdentifier(EphemeralTableName)
  val EphemeralTableSchema = StructType(Seq(StructField("id", StringType)))
  val QueryName = "qName"
  val Sql = s"SELECT * FROM $EphemeralTableName"
  val Window = 5
  val MandatoryTableOptions: Map[String, String] = {
    val KafkaGroupId = "xd1"
    val KafkaTopic = "ephtable"
    val KafkaNumPartitions = 1
    Map(
      "receiver.kafka.topic" -> s"$KafkaTopic:$KafkaNumPartitions",
      "receiver.kafka.groupId" -> KafkaGroupId
    )
  }
  val EphemeralTable =  StreamingConfig.createEphemeralTableModel(EphemeralTableName, MandatoryTableOptions)

  val EphemeralQuery = EphemeralQueryModel(EphemeralTableName, Sql, QueryName, Window)

  val ZookeeperStreamingConnectionKey = "streaming.catalog.zookeeper.connectionString"
  val ZookeeperConnection: Option[String] =
    Try(ConfigFactory.load().getString(ZookeeperStreamingConnectionKey)).toOption
}
