package org.apache.spark.sql.crossdata.models

case class ConnectionHostModel(zkConnection: Seq[ConnectionModel], kafkaConnection: Seq[ConnectionModel]){

  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}