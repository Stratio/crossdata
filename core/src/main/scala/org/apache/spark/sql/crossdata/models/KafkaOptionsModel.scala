package org.apache.spark.sql.crossdata.models

case class KafkaOptionsModel(connection: ConnectionHostModel,
                             topics: Seq[TopicModel],
                             groupId: String,
                             partitionOutput: Option[String] = None,
                             additionalOptions: Map[String, String] = Map.empty,
                             storageLevel: String = "MEMORY_AND_DISK_SER") {

  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}