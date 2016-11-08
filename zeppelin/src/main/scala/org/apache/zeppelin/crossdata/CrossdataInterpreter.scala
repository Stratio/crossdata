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
  private var driver: JavaDriver = _

  def open() {
    val seeds: java.util.List[String] = (getProperty(CrossdataInterpreter.CROSSDATA_SEEDS_PROPERTY).split(",")).toList
    driver = new JavaDriver(new DriverConf().setClusterContactPoint(seeds))
  }

  def close() {
    driver.closeSession
  }

  def interpret(sql: String, context: InterpreterContext): InterpreterResult = {

    val secondsTimeout: Long = (getProperty(CrossdataInterpreter.CROSSDATA_TIMEOUT_SEC)).toLong

    val timeout: Duration = if (secondsTimeout <= 0) Duration.Inf else new FiniteDuration(secondsTimeout, TimeUnit.SECONDS)

    driver.sql(sql, timeout) match {
      case er: ErrorSQLResult => new InterpreterResult(InterpreterResult.Code.ERROR, er.message)
      case esr: SuccessfulSQLResult if esr.resultSet.length <= 0 => new InterpreterResult(InterpreterResult.Code.SUCCESS, "%text EMPTY result")
      case sr: SuccessfulSQLResult if sr.resultSet.length > 0 => new InterpreterResult(InterpreterResult.Code.SUCCESS, resultToZeppelinMsg(sr))
      case other: SQLResult => new InterpreterResult(InterpreterResult.Code.ERROR, s"Unexpected result: ${other.toString}")
    }
  }

  private def resultToZeppelinMsg(sqlResult: SQLResult): String = {
    resultToZeppelinMsg(sqlResult.resultSet, sqlResult.schema)
  }

  private def resultToZeppelinMsg(resultSet: Array[Row], schema: StructType): String = {
    val defaultLimit: Int = getProperty(CrossdataInterpreter.CROSSDATA_DEFAULT_LIMIT).toInt
    val msg: StringBuilder = new StringBuilder
    // Add columns names

    msg.append(schema.fieldNames.mkString("\t")).append(System.lineSeparator)

    //Add rows
    // ArrayType, BinaryType, BooleanType, ByteType, DecimalType, DoubleType, DynamicType,
    // FloatType, FractionalType, IntegerType, IntegralType, LongType, MapType, NativeType,
    // NullType, NumericType, ShortType, StringType, StructType
    resultSet.take(Math.min(defaultLimit, resultSet.length)) foreach { r =>
      val arrayRow: Seq[String] = for (i <- 0 until r.length) yield {
        if(r.isNullAt(i)) "null" else r(i).toString
      }
      msg.append(s"${arrayRow.mkString("\t")}${System.lineSeparator}")
    }

    if (resultSet.length > defaultLimit) {
      // TODO use default limit -> Improve driver API
      msg.append(System.lineSeparator).append("<font color=red>Results are limited by ").append(defaultLimit).append(".</font>")
    }

    s"%table ${msg.append(System.lineSeparator).toString}"
  }

  def cancel(context: InterpreterContext) {
    // TODO do nothing
  }

  def getFormType: Interpreter.FormType = FormType.SIMPLE

  def getProgress(context: InterpreterContext): Int = 0

  override def getScheduler: Scheduler = SchedulerFactory.singleton.createOrGetParallelScheduler("interpreter_" + this.hashCode, 10)
}

