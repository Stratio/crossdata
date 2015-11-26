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
package com.stratio.crossdata.connector

import org.apache.spark.sql.catalyst.expressions.{Attribute, Literal, AttributeReference}
import org.apache.spark.sql.crossdata.execution.NativeUDF
import org.apache.spark.sql.types.DataTypes

object UDFQueryProcessorUtils {
  trait ContextWithUDFs {
    val udfs: Map[String, NativeUDF]
  }
}

trait UDFQueryProcessorUtils {
  self: QueryProcessorUtils =>


  import UDFQueryProcessorUtils._

  override type ProcessingContext <: ContextWithUDFs

  override def quoteString(in: Any)(implicit context: ProcessingContext): String = in match {
    case s: String => s"'$s'"
    case a: Attribute => expandAttribute(a.toString)
    case other => other.toString
  }

  // UDFs are string references in both filters and projects => lookup in udfsMap
  def expandAttribute(att: String)(implicit context: ProcessingContext): String = {
    implicit val udfs = context.asInstanceOf[UDFQueryProcessorUtils#ProcessingContext].udfs
    udfs get(att) map { udf =>
      val actualParams = udf.children.collect { //TODO: Add type checker (maybe not here)
        case at: AttributeReference if(udfs contains at.toString) => expandAttribute(at.toString)
        case at: AttributeReference => at.name
        case lit @ Literal(_, DataTypes.StringType) => quoteString(lit.toString)
        case lit: Literal => lit.toString
      } mkString ","
      s"${udf.name}($actualParams)"
    } getOrElse att.split("#").head.trim // TODO: Try a more sophisticated way...
  }

}
