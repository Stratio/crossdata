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
package org.apache.spark.sql.crossdata.execution.auth

import java.nio.file.Paths
import java.util.UUID

import com.stratio.crossdata.security._
import org.apache.spark.sql.catalyst.plans.logical.LogicalPlan
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SaveMode}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.util.Try

@RunWith(classOf[JUnitRunner])
class AuthDirectivesExtractorIT extends SharedXDContextTest {


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

    df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$usersTable").saveAsTable(usersTable)
    df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$locationTable").saveAsTable(locationTable)
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
    authDirectivesExtractor.extractResourcesAndActions(unionTempTablePlan) should contain allOf (
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

    Seq(createTablePlan, createTempTablePlan).foreach{ createPlan =>
      authDirectivesExtractor.extractResourcesAndActions(createPlan) should have length 1
      authDirectivesExtractor.extractResourcesAndActions(createPlan) should contain (Resource(crossdataInstances, CatalogResource, catalogIdentifier), Write)
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


  private def composeTableResourceName(catalogIdentifier: String, tableName: String) = Seq(catalogIdentifier, tableName) mkString "."
}
