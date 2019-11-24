/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver

import java.nio.file.Paths

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.testkit.scaladsl.TestSink
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.JavaTableName
import com.stratio.crossdata.driver.test.Utils._
import org.apache.spark.sql.Row
import org.junit.runner.RunWith
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class JavaDriverIT extends EndToEndTest with ScalaFutures{

  driverFactories foreach { case (factory, description) =>

    implicit val ctx = DriverTestContext(factory)

    "JavaDriver (with default options)" should s"get a list of tables $description" in {

      assumeCrossdataUpAndRunning()
      withJavaDriverDo { javaDriver =>

        javaDriver.sql(
          s"CREATE TABLE db.jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI()).toString}')"
        )
        javaDriver.sql(
          s"CREATE TABLE jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI()).toString}')"
        )

        javaDriver.listTables() should contain allOf(new JavaTableName("jsonTable3", "db"), new JavaTableName("jsonTable3", ""))
      }
    }

    "JavaDriver (specifying serverHost, and flattened value)" should s"return a list of tables $description" in {

      assumeCrossdataUpAndRunning()

      withJavaDriverDo { javaDriver =>

        javaDriver.sql(
          s"CREATE TABLE db.jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI()).toString}')"
        )
        javaDriver.sql(
          s"CREATE TABLE jsonTable3 USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI()).toString}')"
        )

        javaDriver.listTables() should contain allOf(new JavaTableName("jsonTable3", "db"), new JavaTableName("jsonTable3", ""))
      } (DriverTestContext(factory, Some(new DriverConf().setFlattenTables(true))))

    }

  }

  Seq(Driver.http -> "through HTTP") foreach { case (factory, description) =>

    implicit val ctx = DriverTestContext(factory)
    val factoryDesc = s" $description"

    it should "return a SuccessfulQueryResult when executing a select *" + factoryDesc in {
      assumeCrossdataUpAndRunning()
      withJavaDriverDo { driver =>

        driver.sql(s"CREATE TEMPORARY TABLE jsonTable USING org.apache.spark.sql.json OPTIONS (path '${Paths.get(getClass.getResource("/tabletest.json").toURI).toString}')")

        val sqlStreamedResult = driver.sqlStreamSource("SELECT * FROM jsonTable")

        val javadslSource = sqlStreamedResult.javaRowsSource
        sqlStreamedResult.schema.fieldNames should contain allOf("id", "title")
        implicit val _: ActorSystem =  ActorSystem()
        javadslSource.runWith(TestSink.probe[Row](ActorSystem()), ActorMaterializer())
            .requestNext(Row(1, "Crossdata"))
            .requestNext(Row(2, "Fuse"))
            .request(1).expectComplete()

        }
      }
  }

}
