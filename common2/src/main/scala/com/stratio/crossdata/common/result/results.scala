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
package com.stratio.crossdata.common.result

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import org.apache.commons.lang3.StringUtils
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Success, Try}


trait Result {
  def hasError: Boolean
}

sealed trait SQLResult extends Result {

  // TODO: Avoid to expose methods that some of their implementation throw an exception
  def resultSet: Array[Row]

  def schema: StructType

  /**
   * NOTE: This method is based on the method org.apache.spark.sql.DataFrame#showString from Apache Spark.
   *       For more information, go to http://spark.apache.org.
   * Compose the string representing rows for output
   */
  def prettyResult: Array[String] = {

    val sb = new StringBuilder
    val numCols = schema.fieldNames.length

    // For array values, replace Seq and Array with square brackets
    // For cells that are beyond 20 characters, replace it with the first 17 and "..."
    val rows: Seq[Seq[String]] = schema.fieldNames.toSeq +: resultSet.map { row =>
      row.toSeq.map {
          case null => "null"
          case array: Array[_] => array.mkString("[", ", ", "]")
          case seq: Seq[_] => seq.mkString("[", ", ", "]")
          case cell => cell.toString
      }: Seq[String]
    }

    // Initialise the width of each column to a minimum value of '3'
    val colWidths = Array.fill(numCols)(3)

    // Compute the width of each column
    for (row <- rows) {
      for ((cell, i) <- row.zipWithIndex) {
        colWidths(i) = math.max(colWidths(i), cell.length)
      }
    }

    // Create SeparateLine
    val sep: String = colWidths.map("-" * _).addString(sb, "+", "+", "+\n").toString()

    // column names
    rows.head.zipWithIndex.map { case (cell, i) =>
      StringUtils.rightPad(cell, colWidths(i))
    }.addString(sb, "|", "|", "|\n")

    sb.append(sep)

    // data
    rows.tail.map {
      _.zipWithIndex.map { case (cell, i) =>
        StringUtils.rightPad(cell.toString, colWidths(i))
      }.addString(sb, "|", "|", "|\n")
    }

    sb.append(sep).toString().split("\n")
  }
}

case class SuccessfulSQLResult(resultSet: Array[Row], schema: StructType) extends SQLResult {
  val hasError = false
}

case class ErrorSQLResult(message: String, cause: Option[Throwable] = None) extends SQLResult {
  val hasError = true
  override lazy val resultSet = throw mkException
  override lazy val schema = throw mkException

  private def mkException: Exception =
    cause.map(throwable => new RuntimeException(message, throwable)).getOrElse(new RuntimeException(message))
}


trait StreamedSQLResult extends SQLResult {
  val rowsSource: Source[Row, NotUsed]
  val javaRowsSource: akka.stream.javadsl.Source[Row, NotUsed] = rowsSource.asJava

  override def resultSet: Array[Row] = {
    implicit val aSystem: ActorSystem = ActorSystem()
    implicit  val aMater: ActorMaterializer = ActorMaterializer()

    val sqlResult = rowsSource.runFold(List.empty[Row]) {
      case (acc: List[Row], row: Row) => row :: acc
      case _ => Nil
    }
    Await.result(sqlResult, Duration.Inf).reverse.toArray
  }
}

case class StreamedSuccessfulSQLResult(rowsSource: Source[Row, NotUsed], schema: StructType) extends StreamedSQLResult {
  val hasError: Boolean = false
}

case class StreamedErrorSQLResult(message: String, cause: Option[Throwable] = None) extends StreamedSQLResult {
  val hasError = true
  override lazy val rowsSource = throw mkException
  override lazy val schema = throw mkException

  private def mkException: Exception =
    cause.map(throwable => new RuntimeException(message, throwable)).getOrElse(new RuntimeException(message))
}


object InternalStreamedSuccessfulSQLResult {
  implicit def schema2streamed(schema: StructType): InternalStreamedSuccessfulSQLResult = StreamedSchema(schema)
  implicit def row2streamed(row: Row)(implicit providedSchema: StructType): InternalStreamedSuccessfulSQLResult =
    StreamedRow(row, Some(providedSchema))
}

sealed trait InternalStreamedSuccessfulSQLResult

case class StreamedSchema(schema: StructType) extends InternalStreamedSuccessfulSQLResult

case class StreamedRow(row: Row, providedSchema: Option[StructType] = None) extends InternalStreamedSuccessfulSQLResult