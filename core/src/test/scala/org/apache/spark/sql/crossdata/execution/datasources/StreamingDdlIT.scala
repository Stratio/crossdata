/*package org.apache.spark.sql.crossdata.execution.datasources

import java.util.UUID

import com.typesafe.config.{Config, ConfigFactory, ConfigValueFactory}
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.models.EphemeralExecutionStatus
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class StreamingDdlIT extends SharedXDContextTest with StreamingDDLTestConstants{


  override val catalogConfig: Option[Config] = {
    val zkResourceConfig =
      Try(ConfigFactory.load("core-reference.conf").getConfig(CoreConfig.ParentConfigName)).toOption

    ZookeeperConnection.fold(zkResourceConfig) { connectionString =>
      zkResourceConfig.flatMap(resourceConfig =>
        Option(resourceConfig.withValue(ZookeeperStreamingConnectionKey, ConfigValueFactory.fromAnyRef(connectionString))))
    }
  }

  /**
   * Parser tests
   */
  "StreamingDDLParser" should "parse an add ephemeral query" in {
    val logicalPlan = xdContext.ddlParser.parse(s"ADD $Sql WITH WINDOW $Window SEC AS $QueryName")
    logicalPlan shouldBe AddEphemeralQuery(EphemeralTableName, Sql, QueryName, Window)
  }

  "StreamingDDLParser" should "parse a join query with window" in {
    val complexQuery = s"""
                      |SELECT name FROM $EphemeralTableName INNER JOIN $EphemeralTableName
                      |ON $EphemeralTableName.id = $EphemeralTableName.id
                      |WITH WINDOW 10 SECS AS joinTopic""".stripMargin
    val logicalPlan = xdContext.ddlParser.parse(s"ADD $Sql WITH WINDOW $Window SEC AS $QueryName")
    logicalPlan shouldBe AddEphemeralQuery(EphemeralTableName, Sql, QueryName, Window)
  }


  it should "parse an add ephemeral query ommiting the add" in {
    val logicalPlan = xdContext.ddlParser.parse(s"$Sql WITH WINDOW 5 SEC AS $QueryName")
    logicalPlan shouldBe AddEphemeralQuery(EphemeralTableName, Sql, QueryName, Window)
  }

  it should "parse an add ephemeral query ommiting the alias" in {
    val logicalPlan = xdContext.ddlParser.parse(s"ADD $Sql WITH WINDOW $Window SEC")
    logicalPlan shouldBe a[AddEphemeralQuery]
    logicalPlan.asInstanceOf[AddEphemeralQuery].copy(alias = "alias") shouldBe AddEphemeralQuery(EphemeralTableName, Sql, "alias", Window)

  }

  it should "recognize sec, secs and seconds within the window clause" in {
    val logicalPlan = xdContext.ddlParser.parse(s"$Sql WITH WINDOW 5 SEC AS $QueryName")
    logicalPlan shouldBe AddEphemeralQuery(EphemeralTableName, Sql, QueryName, Window)
    logicalPlan shouldBe xdContext.ddlParser.parse(s"$Sql WITH WINDOW 5 SECS AS $QueryName")
    logicalPlan shouldBe xdContext.ddlParser.parse(s"$Sql WITH WINDOW 5 SECONDS AS $QueryName")
  }

  /**
   * Command tests
   */
  "StreamingCommand" should "allow to describe ephemeral tables" in {

    val ephTableIdentifier = TableIdentifier(EphemeralTableName, Some("db"))
    val ephTableName = ephTableIdentifier.unquotedString
    val ephTable = EphemeralTable.copy(name = ephTableName, schema = Option(EphemeralTableSchema))
    try {
      val tableCreated = xdContext.streamingCatalog.foreach(_.createEphemeralTable(ephTable))
      val describeResult = DescribeEphemeralTable(ephTableIdentifier).run(xdContext)
      describeResult should have length 1
      describeResult.head should have length 1
      val describeString = describeResult.head.getString(0)
      describeString should include ("name")
      describeString should include (ephTableName)
      describeString should include ("options")
      describeString should include ("atomicWindow")
      describeString should include ("checkpointDirectory")
      describeString should include ("sparkOptions")
      describeString should include ("schema")
    } catch {
      case t: Throwable =>
        fail(t)
    } finally {
      xdContext.streamingCatalog.foreach(_.dropEphemeralTable(ephTableName))
    }
  }


  it should "allow to show ephemeral tables" in {
    val ephTableIdentifier = TableIdentifier(EphemeralTableName+UUID.randomUUID())
    val ephTableName = ephTableIdentifier.unquotedString
    val ephTable = EphemeralTable.copy(name = ephTableName)
    try {
      val tableCreated = xdContext.streamingCatalog.foreach(_.createEphemeralTable(ephTable))
      val showTablesResult = ShowEphemeralTables.run(xdContext)
      showTablesResult should have length 2
      val showTablesFilteredResult = showTablesResult.filter(r => r.getString(0) == ephTableName)
      showTablesFilteredResult should have length 1
      showTablesFilteredResult.head should have length 3

      showTablesFilteredResult.head.toSeq match {
        case Seq(name: String, status: String, atomicWindow: Int) =>
          name shouldBe ephTableName
          status shouldBe "NotStarted"
          atomicWindow shouldBe Window
        case _ => fail("expected name: string, status: string and atomicWindow: int")
      }
    } catch {
      case t: Throwable =>
        fail(t)
    } finally {
      xdContext.streamingCatalog.foreach(_.dropEphemeralTable(ephTableName))
    }
  }

  it should "throw an exception when creating an ephemeral table without options" in {

    val ephTableIdentifier = TableIdentifier(EphemeralTableName+UUID.randomUUID())
    val ephTableName = ephTableIdentifier.unquotedString
    val ephTable = EphemeralTable.copy(name = ephTableName)

    an [Exception] should be thrownBy {
      CreateEphemeralTable(ephTableIdentifier, ephTable.schema, Map.empty).run(xdContext)
    }
  }


  it should "allow to create ephemeral tables" in {
    val ephTableIdentifier = TableIdentifier(EphemeralTableName + UUID.randomUUID())
    val ephTableName = ephTableIdentifier.unquotedString
    val ephTable = EphemeralTable.copy(name = ephTableName)

    try {
      val createResult = CreateEphemeralTable(ephTableIdentifier, ephTable.schema, MandatoryTableOptions).run(xdContext)
      xdContext.streamingCatalog.exists( _.existsEphemeralTable(ephTableName)) shouldBe true
      createResult should have length 1
      createResult.head should have length 1
      val describeString = createResult.head.getString(0)
      describeString should include("name")
      describeString should include(ephTableName)
      describeString should include("options")
      describeString should include("atomicWindow")
      describeString should include("checkpointDirectory")
      describeString should include("sparkOptions")
    } catch {
      case t: Throwable =>
        fail(t)
    } finally {
      xdContext.streamingCatalog.foreach(_.dropEphemeralTable(ephTableName))
    }

  }

  it should "allow to drop an ephemeral table" in {

    val ephTableIdentifier = TableIdentifier(EphemeralTableName + UUID.randomUUID())
    val ephTableName = ephTableIdentifier.unquotedString
    val ephTable = EphemeralTable.copy(name = ephTableName)

    try {
      CreateEphemeralTable(ephTableIdentifier, ephTable.schema, MandatoryTableOptions).run(xdContext)
      xdContext.streamingCatalog.exists( _.existsEphemeralTable(ephTableName)) shouldBe true

      val dropResult = DropEphemeralTable(ephTableIdentifier).run(xdContext)
      xdContext.streamingCatalog.exists( _.existsEphemeralTable(ephTableName)) shouldBe false

      dropResult should have length 1
      dropResult.head should have length 1
      dropResult.head.getString(0) shouldBe ephTableName
    } catch {
      case t: Throwable =>
        xdContext.streamingCatalog.foreach(_.dropEphemeralTable(ephTableName))
        fail(t)
    }

  }

  it should "allow to drop all ephemeral tables" in {

    val ephTableIdentifier = TableIdentifier(EphemeralTableName + UUID.randomUUID())
    val ephTableName = ephTableIdentifier.unquotedString
    val ephTable = EphemeralTable.copy(name = ephTableName)
    val previousTables = xdContext.streamingCatalog.map( _.getAllEphemeralTables)

    try {

      CreateEphemeralTable(ephTableIdentifier, ephTable.schema, MandatoryTableOptions).run(xdContext)
      val dropResult = DropAllEphemeralTables.run(xdContext)
      // The catalog should be empty
      xdContext.streamingCatalog.exists ( _.getAllEphemeralTables.isEmpty) shouldBe true

      dropResult should have length (1 + previousTables.size)
      dropResult.head should have length 1

    } catch {
      case t: Throwable =>
        xdContext.streamingCatalog.foreach(_.dropEphemeralTable(ephTableName))
        fail(t)
    } finally {
      // Restoring ephemeral tables
      xdContext.streamingCatalog.foreach{ streamingCatalog =>
        previousTables.foreach{ pTables =>
          pTables.foreach(streamingCatalog.createEphemeralTable)
        }
      }
    }

  }

  it should "allow to show the ephemeral table status" in {

    val showStatusResult = ShowEphemeralStatus(EphemeralTableIdentifier).run(xdContext)

    showStatusResult should have length 1
    showStatusResult.head should have length 1

    showStatusResult.head.getString(0) shouldBe EphemeralExecutionStatus.NotStarted.toString

  }

  it should "allow to show the status of all ephemeral tables" in {

    val showAllResult = ShowAllEphemeralStatuses.run(xdContext)

    showAllResult should have length 1
    showAllResult.head should have length 2

    showAllResult.head.toSeq match {
      case Seq(name: String, status: String) =>
        name shouldBe EphemeralTableName
        status shouldBe EphemeralExecutionStatus.NotStarted.toString
      case _ => fail("expected name: string, status: string")
    }

  }

  it should "fail when starting the process associated to an ephemeral table which doesn't exist" in {
    an [Exception] should be thrownBy{
      StartProcess("asdf").run(xdContext)
    }
  }

  it should "fail when stopping a non-started process" in {
    the [Exception] thrownBy{
      StopProcess(EphemeralTableIdentifier.unquotedString).run(xdContext)
    } should have message s"Cannot stop process. $EphemeralTableName status is NotStarted"
  }

  // QUERIES

  it should "allow to show queries associated to ephemeral tables" in {
    ShowEphemeralQueries().run(xdContext) should have length 0
    try {
      xdContext.streamingCatalog.foreach(_.createEphemeralQuery(EphemeralQuery))
      val showQueriesResult = ShowEphemeralQueries(Option(EphemeralTableName)).run(xdContext)
      showQueriesResult should have length 1
      showQueriesResult.head should have length 1
      val showQueryJson = showQueriesResult.head.getString(0)

      showQueryJson should include (s"""ephemeralTableName" : "$EphemeralTableName""")
      showQueryJson should include (s"""sql" : "$Sql"""")
      showQueryJson should include (s"""alias" : "$QueryName"""")
      showQueryJson should include (s"""window" : $Window""")
    } catch {
      case t: Throwable =>
        fail(t)
    } finally {
      xdContext.streamingCatalog.foreach(_.dropEphemeralQuery(QueryName))
    }
  }

  it should "allow to add queries to an ephemeral table" in {

    xdContext.streamingCatalog.exists( _.existsEphemeralQuery(QueryName)) shouldBe false

    try {
      val addQueryResult = AddEphemeralQuery(
        EphemeralQuery.ephemeralTableName,
        EphemeralQuery.sql,
        EphemeralQuery.alias,
        EphemeralQuery.window
      ).run(xdContext)

      xdContext.streamingCatalog.exists( _.existsEphemeralQuery(QueryName)) shouldBe true

      addQueryResult should have length 1
      addQueryResult.head should have length 1
      val addQueryJson = addQueryResult.head.getString(0)

      addQueryJson should include (s"""ephemeralTableName" : "$EphemeralTableName""")
      addQueryJson should include (s"""sql" : "$Sql"""")
      addQueryJson should include (s"""alias" : "$QueryName"""")
      addQueryJson should include (s"""window" : $Window""")
    } catch {
      case t: Throwable =>
        fail(t)
    } finally {
      xdContext.streamingCatalog.foreach(_.dropEphemeralQuery(QueryName))
    }
  }

  it should "allow to drop a query associated to an ephemeral table" in {

    try {
      xdContext.streamingCatalog.foreach(_.createEphemeralQuery(EphemeralQuery))
      xdContext.streamingCatalog.exists(_.existsEphemeralQuery(QueryName)) shouldBe true

      val dropResult = DropEphemeralQuery(QueryName).run(xdContext)
      xdContext.streamingCatalog.exists(_.existsEphemeralQuery(QueryName)) shouldBe false

      dropResult should have length 1
      dropResult.head should have length 1
      dropResult.head.getString(0) shouldBe QueryName
    } catch {
      case t: Throwable =>
        xdContext.streamingCatalog.foreach(_.dropEphemeralQuery(QueryName))
        fail(t)
    }
  }


  it should "allow to drop all ephemeral queries in a specific table" in {

    val previousQueries = xdContext.streamingCatalog.map(_.getAllEphemeralQueries)

    try {
      xdContext.streamingCatalog.foreach(_.createEphemeralQuery(EphemeralQuery))
      xdContext.streamingCatalog.exists(_.existsEphemeralQuery(QueryName)) shouldBe true

      val randomTable = UUID.randomUUID().toString
      val dResult = DropAllEphemeralQueries(Some(randomTable)).run(xdContext)
      dResult should have length 0
      xdContext.streamingCatalog.exists(_.existsEphemeralQuery(QueryName)) shouldBe true

      val dropResult = DropAllEphemeralQueries(Some(EphemeralTableName)).run(xdContext)
      xdContext.streamingCatalog.exists(_.existsEphemeralQuery(QueryName)) shouldBe false

      dropResult should have length 1
      dropResult.head should have length 1
      dropResult.head.getString(0) shouldBe QueryName

    } catch {
      case t: Throwable =>
        xdContext.streamingCatalog.foreach(_.dropEphemeralQuery(QueryName))
        fail(t)
    } finally {
      // Restoring ephemeral tables
      xdContext.streamingCatalog.foreach { streamingCatalog =>
        previousQueries.foreach { pQueries =>
          pQueries.foreach(streamingCatalog.createEphemeralQuery)
        }
      }
    }
  }


  protected override def beforeAll(): Unit = {
    super.beforeAll()
    xdContext.streamingCatalog.foreach(_.createEphemeralTable(EphemeralTable))

  }

  /**
   * Stop the underlying [[org.apache.spark.SparkContext]], if any.
   */
  protected override def afterAll(): Unit = {
    xdContext.streamingCatalog.foreach(_.dropAllEphemeralTables())
    super.afterAll()
  }
}
*/