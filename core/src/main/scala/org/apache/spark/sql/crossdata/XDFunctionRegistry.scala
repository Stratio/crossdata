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
package org.apache.spark.sql.crossdata

import com.stratio.crossdata.connector.FunctionInventory
import org.apache.spark.Logging
import org.apache.spark.sql.AnalysisException
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.expressions.Expression
import org.apache.spark.sql.catalyst.expressions.ExpressionInfo

import scala.util.Try

private[crossdata] class XDFunctionRegistry(sparkFunctionRegistry: FunctionRegistry, functionInventoryServices: Seq[FunctionInventory])
  extends FunctionRegistry with Logging {

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