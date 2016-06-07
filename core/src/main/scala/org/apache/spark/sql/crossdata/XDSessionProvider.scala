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
package org.apache.spark.sql.crossdata

import java.util.UUID

import com.typesafe.config.Config
import org.apache.spark.SparkContext

import scala.util.{Failure, Try}

object XDSessionProvider {
  type SessionID = UUID
}

abstract class XDSessionProvider(
                                  @transient protected val sc: SparkContext,
                                  protected val commonConfig: Option[Config] = None
                                  ) {

  import XDSessionProvider._

  def session(sessionID: SessionID): Try[XDSession]


  def newSession(sessionID: SessionID): Try[XDSession]

  def closeSession(sessionID: SessionID): Try[Unit]

}


// TODO use simplexdSharedContext in the XDSession builder by default
// TODO SessionProvider(use delegateMethods) instead of error??
class SimpleSessionProvider(
                           @transient override val sc: SparkContext,
                           commonConfig: Option[Config] = None
                           ) extends XDSessionProvider(sc,commonConfig){

  import XDSessionProvider._

  private val errorMessage =
    "A distributed context must be used to manage XDServer sessions. Please, use SparkSessions instead"

  override def newSession(sessionID: SessionID): Try[XDSession] = Failure(new UnsupportedOperationException(errorMessage))

  override def closeSession(sessionID: SessionID): Try[Unit] = Failure(new UnsupportedOperationException(errorMessage))

  override def session(sessionID: SessionID): Try[XDSession] = Failure(new UnsupportedOperationException(errorMessage))
}

