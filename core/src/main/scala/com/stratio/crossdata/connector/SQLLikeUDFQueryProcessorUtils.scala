package com.stratio.crossdata.connector

import java.sql.Timestamp

import org.apache.spark.sql.catalyst.expressions.Attribute
import org.apache.spark.sql.catalyst.expressions.AttributeReference
import org.apache.spark.sql.catalyst.expressions.Literal
import org.apache.spark.sql.crossdata.catalyst.NativeUDF
import org.apache.spark.sql.types.DataTypes

object SQLLikeUDFQueryProcessorUtils {
  trait ContextWithUDFs {
    val udfs: Map[String, NativeUDF]
  }
}

trait SQLLikeUDFQueryProcessorUtils {
  self: SQLLikeQueryProcessorUtils =>


  import SQLLikeUDFQueryProcessorUtils.ContextWithUDFs

  override type ProcessingContext <: ContextWithUDFs

  override def quoteString(in: Any)(implicit context: ProcessingContext): String = in match {
    case s @ (_:String | _: Timestamp) => s"'$s'"
    case a: Attribute => expandAttribute(a.toString)
    case other => other.toString
  }

  // UDFs are string references in both filters and projects => lookup in udfsMap
  def expandAttribute(att: String)(implicit context: ProcessingContext): String = {
    implicit val udfs = context.asInstanceOf[SQLLikeUDFQueryProcessorUtils#ProcessingContext].udfs
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
