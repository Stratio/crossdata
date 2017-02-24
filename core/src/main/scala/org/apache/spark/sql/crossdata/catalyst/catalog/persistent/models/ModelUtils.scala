package org.apache.spark.sql.crossdata.catalyst.catalog.persistent.models

import org.apache.spark.sql.crossdata.serializers.CrossdataSerializer
import org.json4s.jackson.Serialization._

object ModelUtils extends CrossdataSerializer {

  def modelToJsonString[T <: AnyRef](model: T) : String = writePretty(model)
}