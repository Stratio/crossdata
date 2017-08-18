package com.stratio.crossdata.common.serializers


import _root_.akka.cluster.Member

import com.stratio.crossdata.common.serializers.akka.{AkkaClusterMemberSerializer, AkkaMemberStatusSerializer}
import org.apache.spark.sql.crossdata.serializers.StructTypeSerializer
import org.json4s._

trait CrossdataCommonSerializer {

  implicit val json4sJacksonFormats: Formats =
    DefaultFormats + SQLResultSerializer + UUIDSerializer +
      StructTypeSerializer + FiniteDurationSerializer + CommandSerializer + StreamedSuccessfulSQLResultSerializer +
        AkkaMemberStatusSerializer + AkkaClusterMemberSerializer + new SortedSetSerializer[Member]()

}

