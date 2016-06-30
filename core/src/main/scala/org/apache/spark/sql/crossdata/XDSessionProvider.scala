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

// TODO It should share some of the XDContext fields. It will be possible when Spark 2.0 is released
// TODO Crossdata server should create new session and close session when certain events happen

//TODO sessionProvider should be threadSafe
abstract class XDSessionProvider(
                                  @transient val sc: SparkContext,
                                  protected val commonConfig: Option[Config] = None
                                  ) {

  import XDSessionProvider._


  //NOTE: DO NEVER KEEP THE RETURNED REFERENCE FOR SEVERAL USES!
  def session(sessionID: SessionID): Try[XDSession]

  def newSession(sessionID: SessionID): Try[XDSession]

  def closeSession(sessionID: SessionID): Try[Unit]

  /**
    * Close the underlying connections.
    *
    * It is called when the crossdata server is stopped, so if a session provider needs to open an external connection
    * it should be closed here.
    */
  def close(): Unit

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

  override def close(): Unit = ()
}

