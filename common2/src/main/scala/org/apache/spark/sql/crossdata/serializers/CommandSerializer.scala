/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.spark.sql.crossdata.serializers

import com.stratio.crossdata.common._
import org.apache.spark.sql.crossdata.serializers.CommandSerializerHelper._
import org.json4s._

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
