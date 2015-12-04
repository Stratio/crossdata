/**
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

import com.stratio.crossdata.common.metadata.FieldMetadata
import org.apache.spark.sql.types.{IntegerType, StringType}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FlattenedTablesIT extends MongoWithSharedContext {

  "The Driver" should " The table description with nested fields flattened" in {
    assumeCrossdataUpAndRunning

    val flattenedDriver = Driver(true)

    //Experimentation
    val result:Seq[FieldMetadata] = flattenedDriver.describeTable(Some(Database), Collection)

    //Expectations
    result should contain (new FieldMetadata("addres.street", StringType))
    result should contain (new FieldMetadata("addres.zip", IntegerType))
    result should contain (new FieldMetadata("account.details.bank", StringType))
  }
}


