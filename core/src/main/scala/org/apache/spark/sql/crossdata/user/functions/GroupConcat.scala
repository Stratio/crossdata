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
package org.apache.spark.sql.crossdata.user.functions

import org.apache.spark.sql.expressions.MutableAggregationBuffer
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

class GroupConcat(val separator: String) extends UserDefinedAggregateFunction {
  def inputSchema: StructType = StructType(StructField("value", StringType) :: Nil)

  def bufferSchema: StructType = StructType(StructField("total", StringType) :: Nil)

  def dataType: DataType = StringType

  def deterministic: Boolean = true

  def initialize(buffer: MutableAggregationBuffer): Unit = {
  }

  def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
    if(buffer.isNullAt(0)){
      buffer(0) = input.getAs[String](0)
    } else {
      buffer(0) = buffer.getAs[String](0).concat(separator).concat(input.getAs[String](0))
    }
  }

  def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
    if(buffer1.isNullAt(0)){
      buffer1(0) = buffer2.getAs[String](0)
    } else {
      buffer1(0) = buffer1.getAs[String](0).concat(separator).concat(buffer2.getAs[String](0))
    }
  }

  def evaluate(buffer: Row): Any = {
    buffer.getAs[String](0)
  }
}
