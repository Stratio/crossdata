package org.apache.spark.sql.crossdata.catalyst

import com.stratio.common.utils.components.logger.impl.SparkLoggerComponent
import com.stratio.crossdata.connector.FunctionInventory
import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.expressions.{Expression, ExpressionInfo}

import scala.util.Try

private[crossdata] class XDFunctionRegistry(sparkFunctionRegistry: FunctionRegistry, functionInventoryServices: Seq[FunctionInventory])
  extends FunctionRegistry with SparkLoggerComponent {

  import FunctionInventory.qualifyUDF

  @throws[AnalysisException]("If function does not exist")
  override def lookupFunction(name: String, children: Seq[Expression]): Expression =
    Try(sparkFunctionRegistry.lookupFunction(name, children)).getOrElse {

      val datasourceCandidates: Seq[(Expression, String)] = functionInventoryServices.flatMap { fi =>
        Try(
          (sparkFunctionRegistry.lookupFunction(qualifyUDF(fi.shortName(), name), children), fi.shortName())
        ).toOption
      }

      datasourceCandidates match {
        case Seq() => missingFunction(name)
        case Seq((expression, dsname)) => logInfo(s"NativeUDF $name has been resolved to ${qualifyUDF(dsname, name)}"); expression
        case multipleDC => duplicateFunction(name, multipleDC.map(_._2))
      }
    }

  override def registerFunction(name: String, info: ExpressionInfo, builder: FunctionBuilder): Unit =
    sparkFunctionRegistry.registerFunction(name, info, builder)

  override def lookupFunction(name: String): Option[ExpressionInfo] =
    sparkFunctionRegistry.lookupFunction(name)

  override def listFunction(): Seq[String] =
    sparkFunctionRegistry.listFunction()

  private def missingFunction(name: String) =
    throw new AnalysisException(s"Undefined function $name")

  private def duplicateFunction(name: String, datasources: Seq[String]) =
    throw new AnalysisException(s"Unable to resolve udf $name. You must qualify it: use one of ${datasources.map(qualifyUDF(_, name).mkString(", "))}")
}