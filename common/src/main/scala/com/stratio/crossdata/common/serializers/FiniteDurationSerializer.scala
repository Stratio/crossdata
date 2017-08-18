package com.stratio.crossdata.common.serializers

import org.json4s.{CustomSerializer, Extraction, Formats, JObject}

import scala.concurrent.duration._

private[serializers] case class ProtoDuration(duration_ms: Long)

object FiniteDurationSerializer extends CustomSerializer[FiniteDuration](formats =>
  (
    {
      case jduration: JObject =>
        implicit val _: Formats = formats
        jduration.extract[ProtoDuration].duration_ms milliseconds
    },
    {
      case d: FiniteDuration =>
        Extraction.decompose(ProtoDuration(d.toMillis))(formats)
    }
  )
)
