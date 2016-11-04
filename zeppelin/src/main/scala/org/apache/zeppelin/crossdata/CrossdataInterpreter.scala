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
package org.apache.zeppelin.crossdata

import com.stratio.crossdata.common.result.ErrorSQLResult
import com.stratio.crossdata.common.result.SQLResult
import com.stratio.crossdata.common.result.SuccessfulSQLResult
import com.stratio.crossdata.driver.JavaDriver
import com.stratio.crossdata.driver.config.DriverConf
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.apache.zeppelin.interpreter.Interpreter
import org.apache.zeppelin.interpreter.InterpreterContext
import org.apache.zeppelin.interpreter.InterpreterResult
import org.apache.zeppelin.scheduler.Scheduler
import org.apache.zeppelin.scheduler.SchedulerFactory

import scala.concurrent.duration.Duration
import scala.concurrent.duration.FiniteDuration
import java.util.Properties
import java.util.concurrent.TimeUnit

import org.apache.zeppelin.interpreter.Interpreter.FormType

import scala.collection.JavaConversions._

object CrossdataInterpreter {
  private val CROSSDATA_SEEDS_PROPERTY: String = "crossdata.seeds"
  private val CROSSDATA_DEFAULT_LIMIT: String = "crossdata.defaultLimit"
  private val CROSSDATA_TIMEOUT_SEC: String = "crossdata.timeoutSeconds"
}

class CrossdataInterpreter(property: Properties) extends Interpreter(property) {
  private var driver: JavaDriver = null

  def open() {
    val seeds: java.util.List[String] = (getProperty(CrossdataInterpreter.CROSSDATA_SEEDS_PROPERTY).split(",")).toList
    driver = new JavaDriver(new DriverConf().setClusterContactPoint(seeds))
  }

  def close() {
    driver.closeSession()
  }

  def interpret(sql: String, context: InterpreterContext): InterpreterResult = {
    val secondsTimeout: Long = (getProperty(CrossdataInterpreter.CROSSDATA_TIMEOUT_SEC)).toLong
    val timeout: Duration = if (secondsTimeout <= 0) Duration.Inf
    else new FiniteDuration(secondsTimeout, TimeUnit.SECONDS)
    val sqlResult: SQLResult = driver.sql(sql, timeout)
    if (sqlResult.hasError && classOf[ErrorSQLResult].isInstance(sqlResult)) new InterpreterResult(InterpreterResult.Code.ERROR, classOf[ErrorSQLResult].cast(sqlResult).message)
    else if (classOf[SuccessfulSQLResult].isInstance(sqlResult)) {
      val schema: StructType = classOf[SuccessfulSQLResult].cast(sqlResult).schema
      val resultSet: Array[Row] = sqlResult.resultSet
      if (resultSet.length <= 0) new InterpreterResult(InterpreterResult.Code.SUCCESS, "%text EMPTY result")
      else new InterpreterResult(InterpreterResult.Code.SUCCESS, resultToZeppelinMsg(resultSet, schema))
    }
    else new InterpreterResult(InterpreterResult.Code.ERROR, "Unexpected result: " + sqlResult.toString)
  }

  private def resultToZeppelinMsg(resultSet: Array[Row], schema: StructType): String = {
    val defaultLimit: Int = getProperty(CrossdataInterpreter.CROSSDATA_DEFAULT_LIMIT).toInt
    val msg: StringBuilder = new StringBuilder
    // Add columns names
    var resultsHeader: String = ""
    for (colName <- schema.fieldNames) {
      if (resultsHeader.isEmpty) resultsHeader = colName
      else resultsHeader += "\t" + colName
    }
    msg.append(resultsHeader).append(System.lineSeparator)
    //Add rows
    // ArrayType, BinaryType, BooleanType, ByteType, DecimalType, DoubleType, DynamicType,
    // FloatType, FractionalType, IntegerType, IntegralType, LongType, MapType, NativeType,
    // NullType, NumericType, ShortType, StringType, StructType
    val resultLength: Int = Math.min(defaultLimit, resultSet.length)
    val numFields: Int = schema.fieldNames.length
    var r: Int = 0
    while (r < resultLength) {
      {
        val row: Row = resultSet(r)
        var i: Int = 0
        while (i < numFields) {
          {
            if (!row.isNullAt(i)) msg.append(row.apply(i).toString)
            else msg.append("null")
            if (i != numFields - 1) msg.append("\t")
          }
          {
            i += 1; i - 1
          }
        }
        msg.append(System.lineSeparator)
      }
      {
        r += 1; r - 1
      }
    }
    if (resultSet.length > defaultLimit) {
      // TODO use default limit -> Improve driver API
      msg.append(System.lineSeparator).append("<font color=red>Results are limited by ").append(defaultLimit).append(".</font>")
    }
    msg.append(System.lineSeparator)
    "%table " + msg.toString
  }

  def cancel(context: InterpreterContext) {
    // TODO do nothing
  }

  def getFormType: Interpreter.FormType = FormType.SIMPLE

  def getProgress(context: InterpreterContext): Int = 0

  override def getScheduler: Scheduler = SchedulerFactory.singleton.createOrGetParallelScheduler("interpreter_" + this.hashCode, 10)
}

