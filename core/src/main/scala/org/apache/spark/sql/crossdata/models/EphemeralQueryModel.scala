package org.apache.spark.sql.crossdata.models

case class EphemeralQueryModel(ephemeralTableName: String,
                               sql: String,
                               alias : String,
                               window: Int = EphemeralOptionsModel.DefaultAtomicWindow,
                               options: Map[String, String] = Map.empty) {

  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}