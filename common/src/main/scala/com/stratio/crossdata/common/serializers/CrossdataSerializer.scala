/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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

