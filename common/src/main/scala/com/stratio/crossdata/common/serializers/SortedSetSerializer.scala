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
