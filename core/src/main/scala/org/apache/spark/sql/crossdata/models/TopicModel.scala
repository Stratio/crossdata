package org.apache.spark.sql.crossdata.models

case class TopicModel(name: String, numPartitions: Int = TopicModel.DefaultNumPartitions) {

  def toStringPretty : String = ModelUtils.modelToJsonString(this)
}

object TopicModel {

  val DefaultNumPartitions = 1
}
