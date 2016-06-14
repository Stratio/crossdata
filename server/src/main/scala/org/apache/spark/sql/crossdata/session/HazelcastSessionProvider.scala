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

import java.util

import com.hazelcast.config.{Config => HazelcastConfig}
import com.hazelcast.core.Hazelcast
import com.stratio.crossdata.server.config.ServerConfig
import com.typesafe.config.Config
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.catalog.inmemory.HashmapCatalog
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog
import org.apache.spark.sql.crossdata.{XDSession, XDSessionProvider, XDSessionState, XDSharedState}

import scala.collection.JavaConversions._
import scala.util.Try

object HazelcastSessionProvider {

  // val SessionListId = "sessions"
  val SqlConfMapId = "sqlconfmap"

}

class HazelcastSessionProvider(
                                @transient sc: SparkContext,
                                userConfig: Config
                                ) extends XDSessionProvider(sc, Option(userConfig)) {

  import HazelcastSessionProvider._
  import ServerConfig._

  private val sharedState = new XDSharedState(sc, Option(userConfig))

  // TODO manage config
  private val cfg = new HazelcastConfig
  private val hInstance = Hazelcast.newHazelcastInstance(cfg)
  // TODO scalaWrapper // scalaHazel
  // TODO snapshot from (list(session) + addlistener)??
  private val confMap: java.util.Map[SessionID, Map[String,String]] = hInstance.getMap(SqlConfMapId)
  private val catalogMap: java.util.Map[SessionID, XDTemporaryCatalog] = new util.HashMap() // TODO do it as above (hazelcastInstance)

  /*def sessions: Seq[SessionID] = {
    hInstance.getList[SessionID](SessionList).asScala
    // TODO internalMap time-aware to clean unclosedSessions
    // TODO copy (read-only??)
  } // TODO if user do sessions put => hazelcast is not updated
*/

  // TODO try vs future
  override def newSession(sessionID: SessionID): Try[XDSession] = {//try handle failures
    Try {
      val sqlConf = sqlPropsToSQLConf(sparkSQLProps)
      val tempCatalog = new HashmapCatalog(sqlConf)
      val session = buildSession(sqlConf, tempCatalog)
      confMap.put(sessionID, sparkSQLProps) // TODO try TODO read SQLConf from config => base for every session
      catalogMap.put(sessionID, tempCatalog)
      // TODO addSessionsToAllMap && recieveSpecificOptions
      session
    }
  }

  override def closeSession(sessionID: SessionID): Try[Unit] = Try {
    confMap.remove(sessionID)
    catalogMap.remove(sessionID)
  }    // TODO closeSession && removeFromAllCatalogs


  // TODO take advantage of common utils pattern?
  override def session(sessionID: SessionID): Try[XDSession] = Try {
    val catMap =  catalogMap.get(sessionID)
    buildSession(sqlPropsToSQLConf(confMap.get(sessionID)), catalogMap.get(sessionID)) // TODO try
  }

  override def close(): Unit = {
    catalogMap.clear()
    confMap.clear()
    hInstance.shutdown()
  }


  private def buildSession(sqlConf: SQLConf, xDTemporaryCatalog: XDTemporaryCatalog): XDSession = {
    val sessionState = new XDSessionState(sqlConf, xDTemporaryCatalog) // TODO replace with HazelcastCatalog
    new XDSession(sharedState, sessionState)
  }

  private lazy val sparkSQLProps: Map[String,String] ={
    commonConfig.map{ conf =>
      conf.entrySet()
        .map(e => (e.getKey, e.getValue.unwrapped().toString))
        .toMap
        .filterKeys(_.startsWith(SparkSqlConfigPrefix))
        .map(e => (e._1.replace("config.", ""), e._2))
    }.getOrElse(Map.empty)
  }

  // TODO make it implicit?
  private def sqlPropsToSQLConf(sparkSQLProps: Map[String, String]): SQLConf = {
    val sqlConf = new SQLConf
    sparkSQLProps.foreach{ case (key, value) =>
      sqlConf.setConfString(key, value)
    }
    sqlConf
  }
}
