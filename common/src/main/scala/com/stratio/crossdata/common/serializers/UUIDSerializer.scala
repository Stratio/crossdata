/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.serializers

import java.util.UUID

import org.json4s._

object UUIDSerializer extends Serializer[UUID] {
  private val UUIDClass = classOf[UUID]

  def deserialize(implicit format: Formats): PartialFunction[(TypeInfo, JValue), UUID] = {
    case (TypeInfo(UUIDClass, _), json) => json match {
      case JObject(JField("$uuid", JString(s)) :: Nil) => UUID.fromString(s)
      case x => throw new MappingException(s"Can't convert $x to UUID")
    }
  }

  def serialize(implicit format: Formats): PartialFunction[Any, JValue] = {
    case u: UUID => JObject(JField("$uuid", JString(u.toString)) :: Nil)
  }
}

