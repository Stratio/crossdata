package org.apache.spark.sql.crossdata.models

import org.apache.spark.sql.types.StructType

case class EphemeralTableModel(name: String,
                               options: EphemeralOptionsModel,
                               schema: Option[StructType] = None) {

  def toPrettyString : String = ModelUtils.modelToJsonString(this).replaceAll("\\\\\"","\"")

}
