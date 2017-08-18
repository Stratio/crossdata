package org.apache.spark.sql.crossdata.models

case class EphemeralStatusModel(ephemeralTableName: String,
                                status: EphemeralExecutionStatus.Value,
                                startedTime: Option[Long] = None,
                                stoppedTime: Option[Long] = None) {

  def toPrettyString : String = ModelUtils.modelToJsonString(this)
}
