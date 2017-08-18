/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver

import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.FieldMetadata
import com.stratio.crossdata.driver.test.Utils._
import org.apache.spark.sql.types._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FlattenedTablesIT extends MongoWithSharedContext {

  val configWithFlattening = new DriverConf().setFlattenTables(true)

  driverFactories foreach { case (factory, description) =>

    implicit val ctx = DriverTestContext(factory, Some(configWithFlattening))
    val factoryDesc = s" $description"

    "The Driver" should " List table's description with nested and array fields flattened" + factoryDesc in {
      assumeCrossdataUpAndRunning

      withDriverDo { flattenedDriver =>

        //Experimentation
        val result: Seq[FieldMetadata] = flattenedDriver.describeTable(Some(Database), Collection)

        //Expectations
        result should contain(new FieldMetadata("address.zip", IntegerType))
        result should contain(new FieldMetadata("account.details.bank", StringType))
        result should contain(new FieldMetadata("account.details.bank", StringType))
        result should contain(new FieldMetadata("grades.FP", DoubleType))

      }
    }

    it should " List table's description with nested fields Not flattened" + factoryDesc in {
      assumeCrossdataUpAndRunning

      withDriverDo { flattenedDriver =>

        //Experimentation
        val result: Seq[FieldMetadata] = flattenedDriver.describeTable(Some(Database), Collection)

        //Expectations
        val addressType = StructType(Seq(StructField("street", StringType), StructField("city", StringType), StructField("zip", IntegerType)))
        val detailAccount = StructType(Seq(StructField("bank", StringType), StructField("office", IntegerType)))
        val accountType = StructType(Seq(StructField("number", IntegerType), StructField("details", detailAccount)))

        result should contain(new FieldMetadata("address", addressType))
        result should contain(new FieldMetadata("account", accountType))

      } (DriverTestContext(factory, Some(new DriverConf().setFlattenTables(false))))
    }


    it should " Query with Flattened Fields" + factoryDesc in {
      assumeCrossdataUpAndRunning

      withDriverDo { flattenedDriver =>

        //Experimentation
        val result = flattenedDriver.sql(s"SELECT address.street from $Database.$Collection").resultSet

        //Expectations
        result.head.toSeq(0).toString should fullyMatch regex "[0-9]+th Avenue"

      }
    }

    it should " Query with Flattened Fields On Filters" + factoryDesc in {
      assumeCrossdataUpAndRunning

      withDriverDo { flattenedDriver =>

        //Experimentation
        val result = flattenedDriver.sql(s"SELECT description FROM $Database.$Collection WHERE address.street = '5th Avenue'").resultSet

        //Expectations
        result.head.toSeq(0).toString should be equals "description5"

      }
    }

  }

}


