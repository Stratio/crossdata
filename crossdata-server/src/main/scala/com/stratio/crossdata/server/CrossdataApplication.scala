/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.server

import org.apache.log4j.Logger

import scala.annotation.tailrec

object CrossdataApplication extends App {

  lazy val logger = Logger.getLogger(getClass)

  val crossdataServer: CrossdataServer = new CrossdataServer


  /**
   * This method make a command loop.
   * @return  nothing.
   * */
  @tailrec
  private def commandLoop(): Unit = {
    Console.readLine() match {
      case "quit" | "exit" => sys.exit()
      case _ =>
    }
    commandLoop()
  }

  logger.info("Initializing Crossdata Server.")
  crossdataServer.init(null)
  logger.info("Starting Crossdata Server.")
  crossdataServer.start()
  logger.info("Crossdata Server is ready.")
  commandLoop()
  crossdataServer.stop()
  crossdataServer.destroy()
}
