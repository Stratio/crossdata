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
package com.stratio.crossdata.driver.shell

import java.io._

import com.stratio.crossdata.common.SQLCommand
import com.stratio.crossdata.common.result.{ErrorResult, SuccessfulQueryResult}
import com.stratio.crossdata.driver.Driver
import jline.console.{UserInterruptException, ConsoleReader}
import jline.console.history.FileHistory
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.StructType

import scala.collection.JavaConversions._

object BasicShell extends App {

  val logger = Logger.getLogger(getClass)

  val HistoryPath = Some(System.getProperty("user.home")).getOrElse(".").concat("/.crossdata/")
  val HistoryFile = "history.txt"
  val PersistentHistory = new File(HistoryPath.concat(HistoryFile))

  private def createHistoryDirectory(historyPath: String): Boolean = {
    val historyPathFile = new File(historyPath)
    if(!historyPathFile.exists){
      historyPathFile.mkdirs()
    } else {
      false
    }
  }

  createHistoryDirectory(HistoryPath)

  /**
    * NOTE: This method is based on the method org.apache.spark.sql.DataFrame#showString from Apache Spark.
    *       For more information, go to http://spark.apache.org.
    * Compose the string representing rows for output
    */
  private def stringifyResult(resultSet: Array[Row], schema: StructType): Array[String] = {

    val sb = new StringBuilder

    val numCols = schema.fieldNames.length

    // For array values, replace Seq and Array with square brackets
    // For cells that are beyond 20 characters, replace it with the first 17 and "..."
    val rows: Seq[Seq[String]] = schema.fieldNames.toSeq +: resultSet.map { row =>
      row.toSeq.map { cell =>
        val str = cell match {
          case null => "null"
          case array: Array[_] => array.mkString("[", ", ", "]")
          case seq: Seq[_] => seq.mkString("[", ", ", "]")
          case _ => cell.toString
        }
        str
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

    sb.append(sep).toString.split("\n")
  }

  private def getLine(reader: ConsoleReader): Option[String] = {
    try {
      Option(reader.readLine)
    } catch {
      case  uie: UserInterruptException => {
        close(reader)
        None
      }
    }
  }

  private def checkEnd(line: Option[String]): Boolean = {
    if (line.isEmpty) {
      true
    } else {
      val trimmedLine = line.get
      if (trimmedLine.equalsIgnoreCase("exit") || trimmedLine.equalsIgnoreCase("quit")) {
        true
      } else {
        false
      }
    }
  }

  private def close(console: ConsoleReader): Unit = {
    logger.info("Saving history...")
    val pw = new PrintWriter(PersistentHistory)
    console.getHistory.foreach(l => pw.println(l.value))
    logger.info("Closing shell...")
    pw.close
    console.flush
  }

  def loadHistory(console: ConsoleReader): Unit = {
    if(PersistentHistory.exists){
      logger.info("Loading history...")
      console.setHistory(new FileHistory(PersistentHistory))
    } else {
      logger.info("No previous history found")
    }
  }

  val console = new ConsoleReader()

  def initialize(console: ConsoleReader) = {
    console.setHandleUserInterrupt(true)
    console.setExpandEvents(false)
    console.setPrompt("CROSSDATA> ")
    loadHistory(console)
  }


  initialize(console)

  private def runConsole(console: ConsoleReader): Unit = {
    val driver = Driver()

    Thread.sleep(1000)

    console.println()
    console.println("+-----------------+-------------------------+---------------------------+")
    console.println("| CROSSDATA 1.1.0 | Powered by Apache Spark | Easy access to big things |")
    console.println("+-----------------+-------------------------+---------------------------+")
    console.println()
    console.flush

    while (true) {
      val line = getLine(console)

      if (checkEnd(line)) {
        close(console)
        System.exit(0)
      }

      if (line.get.trim.nonEmpty) {
        val result = driver.syncQuery(SQLCommand(line.get))

        console.println(s"Result for query ID: ${result.queryId}")
        if (result.hasError) {
          console.println("ERROR")
          console.println(result.asInstanceOf[ErrorResult].message)
        } else {
          console.println("SUCCESS")
          stringifyResult(
            result.resultSet,
            result.asInstanceOf[SuccessfulQueryResult].schema).foreach(l => console.println(l))
        }
      }
      console.flush
    }
  }

  runConsole(console)

  sys addShutdownHook{
    close(console)
  }

}
