package com.stratio.crossdata.common.serializers.akka

import akka.cluster.MemberStatus
import com.stratio.crossdata.common.serializers.akka.AkkaMemberStatusSerializerHelper._
import org.json4s.JsonAST.{JField, JObject, JString}
import org.json4s.JsonDSL._
import org.json4s.{CustomSerializer, Extraction}

object AkkaMemberStatusSerializerHelper {

  import MemberStatus._

  //TODO: Use Scala reflection to dynamically load value cases.
  //def extractObjectTypeTag[T : TypeTag](x: T): TypeTag[T] = typeTag[T]
  val obj2str: Map[MemberStatus, String] = Seq(
    WeaklyUp, Up, Leaving, Exiting, Down, Removed
  ) map (x => x -> x.toString) toMap

  val str2obj: Map[String, MemberStatus] = obj2str map {
    case (k,v) => v -> k
  }

  val typeLabel = "MemberStatus"

}

object AkkaMemberStatusSerializer extends CustomSerializer[MemberStatus] ( formats =>
  (
    {
      case JObject(JField(typeLabel, JString(statusStr))::Nil) if str2obj contains statusStr =>
        str2obj(statusStr)
    },
    {
      case x: MemberStatus =>
        typeLabel -> Extraction.decompose(obj2str(x))(formats)
    }
  )
)
