package org.apache.spark.sql.crossdata.models

case class ConnectionModel(host: String, port: Int){

  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}
