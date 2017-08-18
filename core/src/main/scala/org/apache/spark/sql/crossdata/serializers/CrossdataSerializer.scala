package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common.serializers.CrossdataCommonSerializer
import org.apache.spark.sql.crossdata.models.{EphemeralExecutionStatus, EphemeralOutputFormat}
import org.json4s._
import org.json4s.ext.EnumNameSerializer


trait CrossdataSerializer  {

  object commonSerializers extends CrossdataCommonSerializer

  implicit val json4sJacksonFormats: Formats = commonSerializers.json4sJacksonFormats +
      new EnumNameSerializer(EphemeralExecutionStatus) +
      new EnumNameSerializer(EphemeralOutputFormat)

}

