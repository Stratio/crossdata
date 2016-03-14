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
package com.stratio.crossdata.launcher

import org.apache.spark.Logging
import org.apache.spark.launcher.SparkLauncher

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success, Try}

class SparkJob(sparkLauncher: SparkLauncher)(implicit executionContext: ExecutionContext) extends Logging{

  def submit(): Unit =
    Future[(Int, Process)] {
      val sparkProcess = sparkLauncher.launch()
      (sparkProcess.waitFor(), sparkProcess)
    } onComplete {

      case Success((0, _)) =>
        logInfo("Spark process exited successfully")

      case Success((exitCode, sparkProcess)) =>
        logError(s"Spark process exited with code $exitCode")
        val errorLines = for {
          is <- Try(sparkProcess.getErrorStream)
          source = Source.fromInputStream(is)
        } yield source.getLines()
        errorLines.foreach { lines =>
          lines.foreach(line => logError(line))
        }

      case Failure(exception) =>
        logError(exception.getMessage)
        throw exception
    }

}
