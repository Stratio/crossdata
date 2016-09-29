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

import java.util.UUID

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.XDSQLConf
import org.apache.spark.sql.crossdata.authorizer.SecurityManagerTestConstants._
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, XDSessionProvider}
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner

import scala.util.{Random, Try}

@RunWith(classOf[JUnitRunner])
class AuthDirectivesExtractorSpec extends SharedXDContextTest {


  val crossdataInstances = Seq("crossdata01", "crossdata02")
  val catalogIdentifier = "mstrCatalog"
  val usersTempTable = "tempusers"
  val usersPersTable = "persusers"
  val tempTables = Seq(usersTempTable)

  protected override def beforeAll(): Unit = {
    super.beforeAll()

    val df = xdContext.createDataFrame(
      xdContext.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType)))
    )
    df.registerTempTable(usersTempTable)

    df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$usersPersTable").saveAsTable(usersPersTable)
  }


  "AuthDirectives" should "return an empty seq when select temporary tables" in {

    val authDirectivesExtractor = new AuthDirectivesExtractor(crossdataInstances, catalogIdentifier, tempTables)
    val selectTempTablePlan = xdContext.sql(s"SELECT * FROM $usersTempTable").queryExecution.logical

    authDirectivesExtractor.extractResourcesAndActions(selectTempTablePlan) shouldBe empty
  }

}
