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
package com.stratio.crossdata.driver.shell

import java.io._

import com.stratio.crossdata.common.crossdata
import com.stratio.crossdata.common.result.ErrorSQLResult
import com.stratio.crossdata.driver.Driver
import jline.console.history.FileHistory
import jline.console.{ConsoleReader, UserInterruptException}
import org.apache.log4j.Logger

import scala.collection.JavaConversions._
import scala.util.{Failure, Try}

object BasicShell extends App {

  val logger = Logger.getLogger(getClass)

  val HistoryPath = Some(System.getProperty("user.home")).getOrElse(".").concat("/.crossdata/")
  val HistoryFile = "history.txt"
  val PersistentHistory = new File(HistoryPath.concat(HistoryFile))

  require(args.length <= 4, "usage --user username --http true/false(default)")

  val user = (args.toList: @unchecked) match {
    case Nil => "xd_shell"
    case "--user" :: username :: Nil => username
  }
  val http = (args.toList: @unchecked) match {
    case Nil => false
    case "--connection" :: bool :: Nil => bool.toBoolean
  }

  val password = "" // TODO read the password

  private def createHistoryDirectory(historyPath: String): Boolean = {
    val historyPathFile = new File(historyPath)
    !historyPathFile.exists && historyPathFile.mkdirs
  }

  createHistoryDirectory(HistoryPath)


  private def getLine(reader: ConsoleReader): Option[String] =
    Try(reader.readLine).recoverWith {
      case uie: UserInterruptException =>
        close(reader)
        Failure(uie)
    } toOption


  private def checkEnd(line: Option[String]): Boolean =
    line.isEmpty || {
      val trimmedLine = line.get
      trimmedLine.equalsIgnoreCase("exit") || trimmedLine.equalsIgnoreCase("quit")
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

    val driver = if(http){
      Driver.http.newSession(user, password)
    } else {
      Driver.newSession(user, password)
    }

    console.println()
    console.println("+-----------------+-------------------------+---------------------------+")
    console.println(s"| CROSSDATA ${crossdata.CrossdataVersion} | Powered by Apache Spark | Easy access to big things |")
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

        val sqlResponse = driver.sql(line.get)
        val result = sqlResponse.waitForResult()
        console.println(s"Result for query ID: ${sqlResponse.id}")
        if (result.hasError) {
          console.println("ERROR")
          console.println(result.asInstanceOf[ErrorSQLResult].message)
        } else {
          console.println("SUCCESS")
          result.prettyResult.foreach(l => console.println(l))
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
