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
package com.stratio.crossdata.driver

import java.nio.file.Paths

import com.stratio.crossdata.driver.test.Utils
import com.stratio.datasource.mongodb.config.MongodbConfig
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class DriverDdlIT extends MongoWithSharedContext {

  import Utils._

  it should "allow to import tables" in {

    withDriverDo { driver =>
      val mongoImportOptions = Map(
        MongodbConfig.Host -> s"$MongoHost:$MongoPort",
        MongodbConfig.Database -> Database,
        MongodbConfig.Collection -> Collection
      )
      driver.importTables("mongodb", mongoImportOptions).resultSet.head.getSeq(0) shouldBe Seq(Database, Collection)
    }
  }

  it should "allow to create tables" in {

    withDriverDo { driver =>

      val crtTableResult = driver.createTable(
        name = "crtTable",
        dataSourceProvider = "org.apache.spark.sql.json",
        schema = None,
        options = Map("path" -> Paths.get(getClass.getResource("/tabletest.json").toURI).toString),
        isTemporary = true).resultSet

      driver.listTables() should contain("crtTable", None)
    }
  }

  it should "allow to drop tables" in {
    withDriverDo { driver =>

      driver.sql(
        s"CREATE TEMPORARY TABLE jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
      ).waitForResult()

      driver.dropTable("jsonTable3").waitForResult()

      driver.listTables() should not contain("jsonTable3", None)
    }
  }

  it should "allow to drop all tables" in {
    withDriverDo { driver =>

      driver.sql(
        s"CREATE TEMPORARY TABLE jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
      ).waitForResult()

      driver.dropAllTables().waitForResult()

      driver.listTables() should not contain("jsonTable3", None)
    }
  }

}