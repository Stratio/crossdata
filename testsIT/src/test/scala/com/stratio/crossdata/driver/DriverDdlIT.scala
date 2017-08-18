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

  driverFactories foreach { case (factory, description) =>

    implicit val ctx = DriverTestContext(factory)

    it should s"allow to import tables $description" in {

      withDriverDo { driver =>
        val mongoImportOptions = Map(
          MongodbConfig.Host -> s"$MongoHost:$MongoPort",
          MongodbConfig.Database -> Database,
          MongodbConfig.Collection -> Collection
        )
        driver.importTables("mongodb", mongoImportOptions).resultSet.head.getSeq(0) shouldBe Seq(Database, Collection)
      }
    }

    it should s"allow to create tables $description" in {

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

    it should s"allow to drop tables $description" in {
      withDriverDo { driver =>

        driver.sql(
          s"CREATE TEMPORARY TABLE jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
        ).waitForResult()

        driver.dropTable("jsonTable3").waitForResult()

        driver.listTables() should not contain("jsonTable3", None)
      }
    }

    it should s"allow to drop all tables $description" in {
      withDriverDo { driver =>

        driver.sql(
          s"CREATE TEMPORARY TABLE jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')"
        ).waitForResult()

        driver.dropAllTables().waitForResult()

        driver.listTables() should not contain("jsonTable3", None)
      }
    }

  }

}