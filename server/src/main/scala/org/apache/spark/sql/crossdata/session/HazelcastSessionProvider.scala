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

import com.hazelcast.config.{GroupConfig, XmlConfigBuilder, Config => HazelcastConfig}
import com.hazelcast.core.Hazelcast
import com.typesafe.config.Config
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog
import org.apache.spark.sql.crossdata.{XDSession, XDSessionProvider, XDSessionState, XDSharedState}

import scala.util.{Failure, Success, Try}

object HazelcastSessionProvider {

  val SqlConfMapId = "sqlconfmap"
  val HazelcastCatalogMapId = "hazelcatalogmap"

  // TODO this is not the right place
  def checkNotNull[T]: T => Try[T] =
    a => Option(a).map(Success(_)).getOrElse(Failure(new RuntimeException(s"Map not found")))

}

class HazelcastSessionProvider( @transient sc: SparkContext,
                                userConfig: Config
                                ) extends XDSessionProvider(sc, Option(userConfig)) {

  import HazelcastSessionProvider._
  import XDSharedState._

  private val sharedState = new XDSharedState(sc, Option(userConfig))// TODO add sqlConf

  // TODO manage config
  private val hInstance = {
    val xmlConfig = new XmlConfigBuilder().build()
    xmlConfig.setGroupConfig(new GroupConfig(scala.util.Properties.scalaPropOrElse("version.number", "unknown")))
    // TODO it should only use Hazelcast.newHazelcastInstance() which internally create a xmlConfig
    Hazelcast.newHazelcastInstance(xmlConfig)
  }

  // TODO scalaWrapper // scalaHazel
  // TODO snapshot from (list(session) + addlistener)?? (
  private val sessionSQLProps: java.util.Map[SessionID, Map[String,String]] = hInstance.getMap(SqlConfMapId)
  private val sessionTempCatalogs: SessionManager[SessionID, Seq[XDTemporaryCatalog]] = new HazelcastSessionCatalogManager(hInstance, sharedState.sqlConf)
  // TODO addSessionsToAllMap && recieveSpecificOptions
  override def newSession(sessionID: SessionID): Try[XDSession] = // TODO try vs future
    Try {
      val tempCatalogs = sessionTempCatalogs.addSession(sessionID)
      sessionSQLProps.put(sessionID, sharedState.sparkSQLProps) // TODO Imap and set

      buildSession(sharedState.sqlConf, tempCatalogs)
    }

  // TODO closeSession && removeFromAllCatalogs
  override def closeSession(sessionID: SessionID): Try[Unit] =
    for {
      _ <- checkNotNull(sessionSQLProps.remove(sessionID))
      _ <- sessionTempCatalogs.removeSession(sessionID)
    } yield ()


  // TODO take advantage of common utils pattern?
  override def session(sessionID: SessionID): Try[XDSession] =
    for {
      tempCatalogMap <- sessionTempCatalogs.getSession(sessionID)
      configMap <- checkNotNull(sessionSQLProps.get(sessionID))
      sess <- Try(buildSession(sqlPropsToSQLConf(configMap), tempCatalogMap))
    } yield {
      sess
    }


  override def close(): Unit = {
    sessionTempCatalogs.clearAllSessions()
    sessionSQLProps.clear()
    hInstance.shutdown()
  }


  private def buildSession(sqlConf: SQLConf, xDTemporaryCatalogs: Seq[XDTemporaryCatalog]): XDSession = {
    val sessionState = new XDSessionState(sqlConf, xDTemporaryCatalogs)
    new XDSession(sharedState, sessionState)
  }

}
