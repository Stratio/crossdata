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
package org.apache.spark.sql.crossdata.authorizer

import java.util.UUID

import com.stratio.crossdata.test.BaseXDTest
import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.crossdata.XDSQLConf
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, XDSessionProvider}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, Row, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.junit.JUnitRunner
import org.apache.spark.sql.crossdata.authorizer.SecurityManagerTestConstants._

import scala.util.{Random, Try}

@RunWith(classOf[JUnitRunner])
class XDAuthorizationIT extends BaseXDTest with BeforeAndAfterAll {

  var _sparkContext: SparkContext = _

  val simplePersistentTable = "simpletable"

  "CrossdataSecurityManager" should "reject an unknown user" in {
    val userRandom = Random.nextString(8)
    val sessionWithSMallowingAnyResource = createNewBasicProvider(classOf[SMAllowingAnyResource].getName).newSession(UUID.randomUUID(), userRandom).get
    val df: DataFrame = sessionWithSMallowingAnyResource.createDataFrame(sessionWithSMallowingAnyResource.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))

    the [Exception] thrownBy {
      df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$simplePersistentTable").saveAsTable(simplePersistentTable)
    } should have message "Operation not authorized"

  }

  it should "reject streaming plans" in {

    val sessionWithSMallowingAnyResource = createNewBasicProvider(classOf[SMAllowingAnyResource].getName).newSession(UUID.randomUUID(), XDUser).get

    val exception = the [Exception] thrownBy sessionWithSMallowingAnyResource.sql("START PROCESS")
    exception.getMessage should startWith ("Unauthorized")
  }

  it should "reject set user plans" in {

    val sessionWithSMallowingAnyResource = createNewBasicProvider(classOf[SMAllowingAnyResource].getName).newSession(UUID.randomUUID(), XDUser).get

    Try(
      sessionWithSMallowingAnyResource.sql("SET a=b")
    ).isSuccess shouldBe true

    val exception = the [Exception] thrownBy sessionWithSMallowingAnyResource.sql(s"SET ${XDSQLConf.UserIdPropertyKey}=newUser")
    exception.getMessage should startWith ("Unauthorized")
  }


  "A SMAllowingAnyResource" should "authorize any plan requiring authorization" in {

    val sessionWithSMallowingAnyResource = createNewBasicProvider(classOf[SMAllowingAnyResource].getName).newSession(UUID.randomUUID(), XDUser).get
    val df: DataFrame = sessionWithSMallowingAnyResource.createDataFrame(sessionWithSMallowingAnyResource.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))

    Try (
      df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$simplePersistentTable").saveAsTable(simplePersistentTable)
    ).isSuccess shouldBe true

    Try (
    sessionWithSMallowingAnyResource.sql(s"SELECT * FROM $simplePersistentTable").collect()
    ).isSuccess shouldBe true
  }


  "A SMDenyingAnyResource" should "not authorize plans requiring authorization" in {

    val sessionWithSDenyingAnyResource = createNewBasicProvider(classOf[SMDenyingAnyResource].getName).newSession(UUID.randomUUID(), XDUser).get
    val df: DataFrame = sessionWithSDenyingAnyResource.createDataFrame(sessionWithSDenyingAnyResource.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))


    the [Exception] thrownBy {
      df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$simplePersistentTable").saveAsTable(simplePersistentTable)
    } should have message "Operation not authorized"

    the [Exception] thrownBy {
      sessionWithSDenyingAnyResource.sql(s"SELECT * FROM $simplePersistentTable").collect()
    } should have message "Operation not authorized"

  }

  it should "execute plans which do not need authorization" in {
    val sessionWithSMDenyingAnyResource = createNewBasicProvider(classOf[SMDenyingAnyResource].getName).newSession(UUID.randomUUID(), XDUser).get
    Try (
      sessionWithSMDenyingAnyResource.sql("SHOW FUNCTIONS").collect()
    ).isSuccess shouldBe true
  }

  "A SMAllowingWriteCatalog" should "accept catalog writes and reject select statements" in {

    val sessionWithSMallowingWriteCatalog = createNewBasicProvider(classOf[SMAllowingWriteCatalogAndDatastore].getName).newSession(UUID.randomUUID(), XDUser).get
    val df: DataFrame = sessionWithSMallowingWriteCatalog.createDataFrame(sessionWithSMallowingWriteCatalog.sparkContext.parallelize((1 to 5).map(i => Row(s"val_$i"))), StructType(Array(StructField("id", StringType))))

    Try {
      df.write.format("json").mode(SaveMode.Overwrite).option("path", s"/tmp/$simplePersistentTable").saveAsTable(simplePersistentTable)
    }.isSuccess shouldBe true


    the[Exception] thrownBy {
      sessionWithSMallowingWriteCatalog.sql(s"SELECT * FROM $simplePersistentTable").collect()
    } should have message "Operation not authorized"

  }

  override protected def beforeAll(): Unit = {
    _sparkContext = new SparkContext(
      "local[2]",
      "test-xdsession",
      new SparkConf().set("spark.cores.max", "2").set("spark.sql.testkey", "true").set("spark.sql.shuffle.partitions", "3")
    )
  }

  override protected def afterAll(): Unit = {
    _sparkContext.stop()
  }

  private def createNewBasicProvider(securityManagerClass: String = CoreConfig.DefaultSecurityManager): XDSessionProvider = {
    val securityManagerConfigString = s"${CoreConfig.SecurityClassConfigKey}=$securityManagerClass"
    val catalogIdentifierConfigString = s"${CoreConfig.CatalogPrefixConfigKey}=${SecurityManagerTestConstants.CatalogIdentifier}"

    val config = ConfigFactory.parseString(securityManagerConfigString).withFallback{
      ConfigFactory.parseString(catalogIdentifierConfigString)
    }
    new BasicSessionProvider(_sparkContext, config)
  }

}
