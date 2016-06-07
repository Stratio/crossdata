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
package org.apache.spark.sql.crossdata.session

import com.hazelcast.config.{Config => HazelcastConfig}
import com.hazelcast.core.Hazelcast
import com.typesafe.config.Config
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.{XDSession, XDSessionProvider, XDSessionState, XDSharedState}

import scala.util.Try

object HazelcastSessionProvider {

  // val SessionListId = "sessions"
  val SqlConfMapId = "sqlconfmap"

}

class HazelcastSessionProvider(
                                @transient sc: SparkContext,
                                userConfig: Option[Config] = None
                                ) extends XDSessionProvider(sc, userConfig) {

  import HazelcastSessionProvider._

  private val sharedState = new XDSharedState(sc, userConfig)

  // TODO manage config
  private val cfg = new HazelcastConfig
  private val hInstance = Hazelcast.newHazelcastInstance(cfg)

  // TODO scalaWrapper // scalaHazel
  // TODO snapshot from (list(session) + addlistener)??
  private val catalogMap: java.util.Map[SessionID, SQLConf] = hInstance.getMap(SqlConfMapId)

  /*def sessions: Seq[SessionID] = {
    hInstance.getList[SessionID](SessionList).asScala
    // TODO internalMap time-aware to clean unclosedSessions
    // TODO copy (read-only??)
  } // TODO if user do sessions put => hazelcast is not updated
*/

  // TODO try vs future
  override def newSession(sessionID: SessionID): Try[XDSession] = {
    catalogMap.put(sessionID, new SQLConf) // TODO try TODO read SQLConf from config => base for every session
    session(sessionID) // TODO eventConsistency
    // TODO addSessionsToAllMap && recieveSpecificOptions
  }

  override def closeSession(sessionID: SessionID): Try[Unit] = Try {
    catalogMap.remove(sessionID)
  }    // TODO closeSession && removeFromAllCatalogs


  // TODO take advantage of common utils pattern?
  override def session(sessionID: SessionID): Try[XDSession] = Try {
    val sessionState = new XDSessionState(catalogMap.get(sessionID))
    new XDSession(sharedState, sessionState)
  }
}
