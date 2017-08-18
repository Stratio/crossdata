/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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

