package org.apache.spark.sql.crossdata.launcher

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import org.apache.spark.launcher.SparkLauncher

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.{Failure, Success, Try}

class SparkJob(sparkLauncher: SparkLauncher)(implicit executionContext: ExecutionContext) extends SparkLoggerComponent{

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
