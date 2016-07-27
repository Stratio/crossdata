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

import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.metadata.FieldMetadata
import com.stratio.crossdata.driver.test.Utils._
import org.apache.spark.sql.types._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FlattenedTablesIT extends MongoWithSharedContext {

  implicit val configWithFlattening: Option[DriverConf] = Some(new DriverConf().setFlattenTables(true))

  "The Driver" should " List table's description with nested and array fields flattened" in {
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

  it should " List table's description with nested fields Not flattened" in {
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

    } (Some(new DriverConf().setFlattenTables(false)))
  }


  it should " Query with Flattened Fields" in {
    assumeCrossdataUpAndRunning

    withDriverDo { flattenedDriver =>

      //Experimentation
      val result = flattenedDriver.sql(s"SELECT address.street from $Database.$Collection").resultSet

      //Expectations
      result.head.toSeq(0).toString should fullyMatch regex "[0-9]+th Avenue"

    }
  }

  it should " Query with Flattened Fields On Filters" in {
    assumeCrossdataUpAndRunning

    withDriverDo { flattenedDriver =>

      //Experimentation
      val result = flattenedDriver.sql(s"SELECT description FROM $Database.$Collection WHERE address.street = '5th Avenue'").resultSet

      //Expectations
      result.head.toSeq(0).toString should be equals "description5"

    }
  }

}


