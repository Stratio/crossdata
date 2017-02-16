package org.apache.spark.sql.crossdata.serializers

import org.json4s._

trait CrossdataSerializer {

  implicit val json4sJacksonFormats: Formats =
    DefaultFormats

}
