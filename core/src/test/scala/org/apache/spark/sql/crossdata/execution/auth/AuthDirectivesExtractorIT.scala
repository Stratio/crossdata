/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.execution.auth

import java.nio.file.Paths
import java.util.UUID

import com.stratio.crossdata.security._
import org.apache.spark.sql.catalyst.expressions.{BitwiseAnd, Expression}
import org.apache.spark.sql.crossdata.catalyst.execution.{DropAllTables, DropExternalTable, DropTable, DropView}
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SaveMode}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class AuthDirectivesExtractorIT extends SharedXDContextTest {

  val tmpCrossdata = "/tmp/crossdata-test"
  // TODO constant
  val crossdataInstances = Seq("crossdata01", "crossdata02")
  val catalogIdentifier = "mstrCatalog"
  val usersTable = "usersper"
  val locationTable = "locat"


  protected override def beforeAll(): Unit = {
    super.beforeAll()

    val df = xdContext.createDataFrame(
      xdContext.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType)))
    )

    Try(xdContext.sql("DROP ALL TABLES"))

    df.write.format("json").mode(SaveMode.Overwrite).option("path", s"$tmpCrossdata/$usersTable").saveAsTable(usersTable)
    df.write.format("json").mode(SaveMode.Overwrite).option("path", s"$tmpCrossdata/$locationTable").saveAsTable(locationTable)
  }

  "AuthDirectives" should "return a tuple (TableResource,READ) action resource when reading tables" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    val selectTempTablePlan = xdContext.sql(s"SELECT * FROM $usersTable").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(selectTempTablePlan) should have length 1
    authDirectivesExtractor.extractResourcesAndActions(selectTempTablePlan) should contain(
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, usersTable)), Read)
    )
  }

  it should "return as many table resources as tables involved in a query" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    val unionTempTablePlan = xdContext.sql(s"SELECT * FROM $locationTable UNION SELECT * FROM $usersTable").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(unionTempTablePlan) should have length 2
    authDirectivesExtractor.extractResourcesAndActions(unionTempTablePlan) should contain allOf(
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, usersTable)), Read),
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, locationTable)), Read)
      )

    val joinTempTablePlan = xdContext.sql(s"SELECT * FROM $locationTable JOIN $usersTable").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(joinTempTablePlan) should have length 2
    authDirectivesExtractor.extractResourcesAndActions(joinTempTablePlan) should contain allOf(
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, usersTable)), Read),
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, locationTable)), Read)
      )
  }

  it should "return the right permissions when creating tables" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)

    val createTablePlan = xdContext.sql(s"CREATE TABLE tabName USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/foo.json").toURI).toString}')").queryExecution.logical
    val createTempTablePlan = xdContext.sql(s"CREATE TEMPORARY TABLE tabNameTemp USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/foo.json").toURI).toString}')").queryExecution.logical

    Seq(createTablePlan, createTempTablePlan).foreach { createPlan =>
      authDirectivesExtractor.extractResourcesAndActions(createPlan) should have length 1
      authDirectivesExtractor.extractResourcesAndActions(createPlan) should contain(Resource(crossdataInstances, CatalogResource, catalogIdentifier), Write)
    }

  }

  it should "return the right permissions when creating views" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    val tableName = "tabName"

    val createViewPlan = xdContext.sql(s"CREATE VIEW viewName AS SELECT * FROM $tableName").queryExecution.logical
    val createTempViewPlan = xdContext.sql(s"CREATE TEMPORARY VIEW viewTempName AS SELECT * FROM $tableName").queryExecution.logical

    Seq(createViewPlan, createTempViewPlan).foreach { createPlan =>
      authDirectivesExtractor.extractResourcesAndActions(createPlan) should have length 2
      authDirectivesExtractor.extractResourcesAndActions(createViewPlan) should contain allOf(
        (Resource(crossdataInstances, CatalogResource, catalogIdentifier), Write),
        (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, tableName)), Read)
        )
    }
  }

  it should "return the right permissions when creating tables as select" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)

    val createAsSelectPlan = xdContext.sql(s"CREATE TABLE extTabName USING org.apache.spark.sql.json OPTIONS (path '/tmp/crossdatatest-${UUID.randomUUID()}.json') AS SELECT * FROM $usersTable").queryExecution.logical

    Seq(createAsSelectPlan).foreach { createPlan =>
      authDirectivesExtractor.extractResourcesAndActions(createPlan) should have length 3
      authDirectivesExtractor.extractResourcesAndActions(createPlan) should contain allOf(
        (Resource(crossdataInstances, CatalogResource, catalogIdentifier), Write),
        (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, usersTable)), Read),
        (Resource(crossdataInstances, DatastoreResource, Resource.AllResourceName), Write)
        )
    }
  }


  it should "extract the expected permissions when inserting tables" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    val insertIntoSelectPlan = xdContext.sql(s"INSERT INTO TABLE $locationTable SELECT * FROM $usersTable").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(insertIntoSelectPlan) should have length 3
    authDirectivesExtractor.extractResourcesAndActions(insertIntoSelectPlan) should contain allOf(
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, usersTable)), Read),
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, locationTable)), Write),
      (Resource(crossdataInstances, DatastoreResource, "*"), Write)
      )
  }

  it should "extract the expected permissions when dropping tables" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    val emptyTableName = "emptytab"
    val emptyDF = xdContext.emptyDataFrame
    emptyDF.write.format("json").mode(SaveMode.Overwrite).option("path", s"$tmpCrossdata/toDropTable").saveAsTable(emptyTableName)

    val dropTablePlan = xdContext.sql(s"DROP TABLE $emptyTableName").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(dropTablePlan) should have length 2
    authDirectivesExtractor.extractResourcesAndActions(dropTablePlan) should contain allOf(
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, emptyTableName)), Drop),
      (Resource(crossdataInstances, CatalogResource, catalogIdentifier), Write)
      )
  }

  it should "not authorize streaming plans" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    val streamingPlan = xdContext.sql(s"SHOW EPHEMERAL TABLES").queryExecution.logical

    an[Exception] shouldBe thrownBy {
      authDirectivesExtractor.extractResourcesAndActions(streamingPlan)
    }
  }

  it should "not authorize insecure plans" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)
    val insecureSQLs = Seq(
      s"ADD JAR ${Paths.get(getClass.getResource("/TestAddApp.jar").toURI()).toString}",
      s"ADD APP '${Paths.get(getClass.getResource("/TestAddApp.jar").toURI()).toString}' AS jj WITH aaa.bbb.ccc"
    )

    insecureSQLs.foreach { insecureSQL =>
      val insecurePlan = xdContext.sql(insecureSQL).queryExecution.logical
      an[Exception] shouldBe thrownBy {
        authDirectivesExtractor.extractResourcesAndActions(insecurePlan)
      }
    }
  }



  it should "extract the expected permissions when requesting metadata" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)

    val showTablePlan = xdContext.sql("SHOW TABLES").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(showTablePlan) should have length 1
    authDirectivesExtractor.extractResourcesAndActions(showTablePlan) should contain(
      (Resource(crossdataInstances, CatalogResource, catalogIdentifier), Describe)
    )

    val describeTablePlan = xdContext.sql(s"DESCRIBE $usersTable").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(describeTablePlan) should have length 1

    authDirectivesExtractor.extractResourcesAndActions(describeTablePlan) should contain(
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, usersTable)), Describe)
    )
  }


  it should "extract the expected permissions when executing cache operations" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier)

    val cacheTablePlans = Seq(
      s"CACHE TABLE $locationTable",
      s"UNCACHE TABLE $locationTable",
      s"REFRESH TABLE $locationTable",
      "CLEAR CACHE"
    ).map( cacheSQL =>
      xdContext.sql(s"CACHE TABLE $locationTable").queryExecution.logical
    )

    cacheTablePlans.foreach{ cacheTablePlan =>
      authDirectivesExtractor.extractResourcesAndActions(cacheTablePlan) should have length 1
      authDirectivesExtractor.extractResourcesAndActions(cacheTablePlan) should contain(
        (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, locationTable)), Cache)
      )
    }

    val cacheTableAsSelectPlan = xdContext.sql(s"CACHE TABLE cacheTableGüei AS SELECT * FROM $locationTable").queryExecution.logical
    authDirectivesExtractor.extractResourcesAndActions(cacheTableAsSelectPlan) should have length 3
    authDirectivesExtractor.extractResourcesAndActions(cacheTableAsSelectPlan) should contain allOf(
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, locationTable)), Read),
      (Resource(crossdataInstances, TableResource, composeTableResourceName(catalogIdentifier, "*")), Cache),
      (Resource(crossdataInstances, CatalogResource, catalogIdentifier), Write)
      )

  }


  private def composeTableResourceName(catalogIdentifier: String, tableName: String) = Seq(catalogIdentifier, tableName) mkString "."
}
