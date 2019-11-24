/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.serializers

import org.json4s.{CustomSerializer, Extraction}
import org.json4s.JsonAST.JArray

import scala.collection.immutable.SortedSet

class SortedSetSerializer[T : Manifest : Ordering] extends CustomSerializer[collection.immutable.SortedSet[T]](
  formats =>
  (
    {
      case arr: JArray =>
        implicit val _ = formats
        SortedSet(arr.extract[Array[T]]:_*)
    },
    {
      case ss: SortedSet[T] =>
        implicit val _ = formats
        Extraction.decompose(ss.toSeq)
    }
  )
)
