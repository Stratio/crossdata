/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver.shell

import java.io._
import java.util.UUID

import com.stratio.crossdata.common.crossdata
import com.stratio.crossdata.common.result.{ErrorSQLResult, SQLResponse, SQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.driver.Driver
import jline.console.history.FileHistory
import jline.console.{ConsoleReader, UserInterruptException}
import org.apache.log4j.Logger

import scala.annotation.tailrec
import scala.collection.JavaConversions._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object BasicShell extends App {

  val logger = Logger.getLogger(getClass)

  val HistoryPath = Some(System.getProperty("user.home")).getOrElse(".").concat("/.crossdata/")
  val HistoryFile = "history.txt"
  val PersistentHistory = new File(HistoryPath.concat(HistoryFile))

  require(args.length <= 8, "usage --user username [--timeout 120] [--tcp] [--async] [--query \"show tables\"")

  val argsReader = new ShellArgsReader(args.toList)
  val options = argsReader.options

  val user = options.getOrElse("user", "xd_shell").toString
  val tcp = options.get("tcp") collect { case bool: Boolean => bool } getOrElse false
  val asyncEnabled = options.get("async") collect { case bool: Boolean => bool } getOrElse false

  val timeout: Duration = options.get("timeout") map {
    case x: Int => x seconds
    case _ => Duration.Inf
  } getOrElse Duration.Inf

  val password = "" // TODO read the password

  val query: Option[String] =
    options get ("query") map { str => str.toString }

  logger.info(s"user: $user tcp enabled: ${tcp.toString} timeout(seconds): $timeout")

  createHistoryDirectory(HistoryPath)
  val console = new ConsoleReader()
  initialize(console)
  runConsole(console)

  private def createHistoryDirectory(historyPath: String): Boolean = {
    val historyPathFile = new File(historyPath)
    !historyPathFile.exists && historyPathFile.mkdirs
  }

  private def getLine(reader: ConsoleReader): Option[String] =
    Try(reader.readLine).recoverWith {
      case uie: UserInterruptException =>
        close(reader)
        Failure(uie)
    } toOption


  private def checkEnd(line: Option[String]): Boolean =
    line.exists { l =>
      val trimmedLine = l.trim
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

  private def loadHistory(console: ConsoleReader): Unit = {
    if (PersistentHistory.exists) {
      logger.info("Loading history...")
      console.setHistory(new FileHistory(PersistentHistory))
    } else {
      logger.info("No previous history found")
    }
  }

  private def initialize(console: ConsoleReader) = {
    console.setHandleUserInterrupt(true)
    console.setExpandEvents(false)
    console.setPrompt("CROSSDATA> ")
    loadHistory(console)
  }

  private def printFrame(statements: Seq[String]) = {
    def mkOuterLine(statements: Seq[String]): String = {
      val spacesLength = 2
      statements.map(str => "-"*(str.length+spacesLength)).mkString("+", "+", "+")
    }

    val outerLine: String = mkOuterLine(statements)

    console.println(outerLine)
    console.println(statements.mkString("| ", " | ", " |"))
    console.println(outerLine)
  }

  private def runConsole(console: ConsoleReader): Unit = {

    val driver = if (tcp) {
      Driver.newSession(user, password)
    } else {
      Driver.http.newSession(user, password)
    }

    console.println()
    printFrame(Seq(s"CROSSDATA ${crossdata.CrossdataVersion}", "Powered by Apache Spark", "Easy access to big things"))
    console.println()
    console.flush

    def executeQuery(query: String): SQLResponse =
      driver.sql(query)

    def executeQueryAsync(query: String): SQLResponse = {
      val sqlResponse = executeQuery(query)

      sqlResponse.sqlResult onComplete {
        case Success(sqlResult) =>
          printResult(sqlResponse.id, sqlResult)
        case Failure(throwable) =>
          printError(sqlResponse.id, throwable)
      }

      sqlResponse
    }

    def executeQuerySync(query: String): SQLResponse = {
      val sqlResponse = executeQuery(query)
      printResult(sqlResponse.id, sqlResponse.waitForResult(timeout))
      sqlResponse
    }

    def execute(query: String): SQLResponse =
     if (asyncEnabled) {
        executeQueryAsync(query)
      } else {
        executeQuerySync(query)
      }

    @tailrec
    def shellLoop(query: Option[String], continue: Boolean): Unit = { //Option for readLine?? Is a bit strange

      if(checkEnd(query)){
        close(console)
        System.exit(0)
      } else {
        //WARN: Using async shell and --query option will generate strange behaviours

        val validQuery = query.filter(_.trim.nonEmpty)

        if (continue) {
          validQuery.foreach(execute)
          shellLoop(getLine(console), continue = true)
        } else {

          if (validQuery.isEmpty) {
            close(console)
            System.exit(-1)
          } else {
            validQuery.foreach{ q =>
              execute(q).sqlResult onComplete {
                case Success(_: SuccessfulSQLResult) =>
                  close(console)
                  System.exit(0)
                case Success(_: ErrorSQLResult) | Failure(_) =>
                  close(console)
                  System.exit(-1)
              }
            }
          }
        }
      }
    }

    shellLoop(query, query.isEmpty)
  }

  private def printResult(responseId: UUID, sResult: SQLResult) : Unit = {
    console.println(s"Result for query ID: $responseId")
    sResult match {
        case result @ SuccessfulSQLResult(sqlResult, _) =>
          console.println("SUCCESS")
          result.prettyResult.foreach(console.println)
          console.flush()
        case ErrorSQLResult(message, _) =>
          console.println("ERROR")
          console.println(message)
          console.flush()
    }
  }

  private def printError(responseId: UUID, throwable: Throwable) = {
    console.println(s"Unexpected error while processing the query with id $responseId: ${throwable.getMessage}")
    console.flush()
  }

}
