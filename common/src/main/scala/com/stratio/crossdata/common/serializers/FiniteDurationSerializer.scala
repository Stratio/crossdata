/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
