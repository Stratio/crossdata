package org.apache.zeppelin.crossdata

import java.util.Properties
import java.util.concurrent.TimeUnit

import com.stratio.crossdata.common.result.{ErrorSQLResult, SQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.driver.Driver
import com.stratio.crossdata.driver.config.DriverConf
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType
import org.apache.zeppelin.interpreter.Interpreter.FormType
import org.apache.zeppelin.interpreter.{Interpreter, InterpreterContext, InterpreterResult}
import org.apache.zeppelin.scheduler.{Scheduler, SchedulerFactory}

import scala.collection.JavaConversions._
import scala.concurrent.duration.{Duration, FiniteDuration}

object CrossdataInterpreter {
  private val CROSSDATA_SEEDS_PROPERTY: String = "crossdata.seeds"
  private val CROSSDATA_HTTP_CONNECTION: String = "crossdata.http"
  private val CROSSDATA_DEFAULT_LIMIT: String = "crossdata.defaultLimit"
  private val CROSSDATA_TIMEOUT_SEC: String = "crossdata.timeoutSeconds"
}

class CrossdataInterpreter(property: Properties) extends Interpreter(property) {
  private var driver: Driver = _

  def open() = {
    val seeds: java.util.List[String] = (getProperty(CrossdataInterpreter.CROSSDATA_SEEDS_PROPERTY).split(",")).toList
    val driverConf = new DriverConf

    driver = if(getProperty(CrossdataInterpreter.CROSSDATA_HTTP_CONNECTION).toBoolean){
      seeds(0).split(":") match { case Array(host, port) => driverConf.setHttpHostAndPort(host, port.toInt) }
      Driver.http.newSession(driverConf)
    } else {
      Driver.newSession(seeds, driverConf)
    }

  }

  def close() = {
    driver.closeSession
  }

  def interpret(sql: String, context: InterpreterContext): InterpreterResult = {

    val secondsTimeout: Long = (getProperty(CrossdataInterpreter.CROSSDATA_TIMEOUT_SEC)).toLong

    val timeout: Duration = if (secondsTimeout <= 0) Duration.Inf else new FiniteDuration(secondsTimeout, TimeUnit.SECONDS)

    driver.sql(sql).waitForResult(timeout) match {
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

  def cancel(context: InterpreterContext) = {
    // TODO do nothing
  }

  def getFormType: Interpreter.FormType = FormType.SIMPLE

  def getProgress(context: InterpreterContext): Int = 0

  override def getScheduler: Scheduler =
    SchedulerFactory.singleton.createOrGetParallelScheduler(s"interpreter_${this.hashCode}", 10)
}

