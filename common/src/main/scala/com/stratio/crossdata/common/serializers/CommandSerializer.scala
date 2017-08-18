/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.serializers

import com.stratio.crossdata.common._
import org.json4s._
import CommandSerializerHelper._

import scala.reflect.ClassTag

private [serializers] object CommandSerializerHelper {

  case class CommandWithName[T <: Command : ClassTag](command: String, details: Option[T] = None)

  object CommandWithName {
    private def apply[T <: Command : ClassTag](details: Option[T]): CommandWithName[T] =
      CommandWithName(implicitly[ClassTag[T]].toString.split('.').last, details)
    def apply[T <: Command : ClassTag](details: T): CommandWithName[T] =
      CommandWithName(Some(details))
    def apply[T <: Command : ClassTag](): CommandWithName[T] =
      CommandWithName(None)
  }

  implicit val _ = DefaultFormats + UUIDSerializer + FiniteDurationSerializer

  val commandExtractor = Map[String, JValue => Command](
    "SQLCommand" -> { jSQLCommand => (jSQLCommand \ "details").extract[SQLCommand] },
    "CancelQueryExecution" -> { jSQLCommand => (jSQLCommand \ "details").extract[CancelQueryExecution] },
    "OpenSessionCommand" -> { jSQLCommand => (jSQLCommand \ "details").extract[OpenSessionCommand] },
    "CloseSessionCommand" -> { _.extract[CloseSessionCommand] },
    "ClusterStateCommand" -> { _.extract[ClusterStateCommand] }
  )

  val commandSerializer: PartialFunction[Any, JValue] = {
    case command: SQLCommand =>
      Extraction.decompose(CommandWithName(command))
    case command: OpenSessionCommand =>
      Extraction.decompose(CommandWithName(command))
    case _: CloseSessionCommand =>
      Extraction.decompose(CommandWithName[CloseSessionCommand])
    case _: ClusterStateCommand =>
      Extraction.decompose(CommandWithName[ClusterStateCommand])
    case cancellation: CancelQueryExecution =>
      Extraction.decompose(CommandWithName(cancellation))
  }

}

object CommandSerializer extends CustomSerializer[Command](
  format => (
    {
      case jCommand @ JObject(JField("command", JString(commandType))::_) =>
        commandExtractor(commandType)(jCommand)
    },
    commandSerializer
    )
)
