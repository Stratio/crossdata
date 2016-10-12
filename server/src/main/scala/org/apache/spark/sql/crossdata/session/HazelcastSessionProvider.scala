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

import com.hazelcast.config.{XmlConfigBuilder, Config => HzConfig}
import com.hazelcast.core.Hazelcast
import com.stratio.crossdata.util.CacheInvalidator
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata._
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDPersistentCatalog, XDStreamingCatalog, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.catalog.utils.CatalogUtils
import org.apache.spark.sql.crossdata.config.CoreConfig._
import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID

import scala.util.{Failure, Success, Try}

object HazelcastSessionProvider {

  val SqlConfMapId = "sqlconfmap"
  val HazelcastCatalogMapId = "hazelcatalogmap"
  val HazelcastConfigMapId = "hazelconfigmap"

  def checkNotNull[T]: T => Try[T] =
    a => Option(a).map(Success(_)).getOrElse(Failure(new RuntimeException(s"Map not found")))

}

class HazelcastSessionProvider(sc: SparkContext,
                               serverConfig: Config,
                               userCoreConfig: Config = ConfigFactory.empty(), // TODO this config should be coreConfig instead of userCoreConfig
                               hzConfig: HzConfig = new XmlConfigBuilder().build()
                              ) extends XDSessionProvider(sc, userCoreConfig) {

  import HazelcastSessionProvider._

  private val sessionsCache: collection.mutable.Map[SessionID, XDSession] =
    new collection.mutable.HashMap[SessionID, XDSession] with collection.mutable.SynchronizedMap[SessionID, XDSession] // TODO insecure map

  private val sessionsCacheInvalidator =
    (sessionId: Option[SessionID]) => Some {
      new CacheInvalidator {
        override def invalidateCache: Unit = sessionId match {
          case None => sessionsCache clear()
          case Some(sid) => sessionsCache -= sid
        }
      }
    }

  override lazy val logger = Logger.getLogger(classOf[HazelcastSessionProvider])

  private lazy val sqlConf: SQLConf = configToSparkSQL(serverConfig, new SQLConf) // TODO Crossdata 1.8+ user server config should be replaced with user core config

  @transient
  protected lazy val externalCatalog: XDPersistentCatalog = CatalogUtils.externalCatalog(sqlConf, catalogConfig)

  @transient
  protected lazy val streamingCatalog: Option[XDStreamingCatalog] = CatalogUtils.streamingCatalog(sqlConf, finalCoreConfig)

  private val sharedState = new XDSharedState(sc, sqlConf, externalCatalog, streamingCatalog, securityManager)

  protected val hInstance = Hazelcast.newHazelcastInstance(hzConfig)

  protected val sessionIDToSQLProps = new HazelcastSessionConfigManager(hInstance, sessionsCacheInvalidator)
  protected val sessionIDToTempCatalogs = new HazelcastSessionCatalogManager(
    hInstance,
    sharedState.sqlConf,
    sessionsCacheInvalidator
  )

  def getHzMembers = hInstance.getCluster.getMembers

  def gelLocalMember = hInstance.getCluster.getLocalMember

  def getClusterState = hInstance.getCluster

  override def newSession(sessionID: SessionID, userId: String): Try[XDSession] =
    Try {
      val tempCatalogs = sessionIDToTempCatalogs.newResource(sessionID)
      // Add the user to the shared map
      sharedState.sqlConf.setConfString(XDSQLConf.UserIdPropertyKey, userId)
      val xdsqlConf = sessionIDToSQLProps.newResource(sessionID, Some(sharedState.sqlConf))

      val session = buildSession(sessionID, xdsqlConf, tempCatalogs, Some(userCoreConfig))
      sessionsCache += sessionID -> session

      session
    }

  override def closeSession(sessionID: SessionID): Try[Unit] =
    for {
      _ <- checkNotNull(sessionIDToSQLProps.deleteSessionResource(sessionID))
      _ <- sessionIDToTempCatalogs.deleteSessionResource(sessionID)
    } yield {
      sessionsCache -= sessionID
    }


  // TODO take advantage of common utils pattern?
  override def session(sessionID: SessionID): Try[XDSession] = {
    sessionsCache.get(sessionID).map(Success(_)).getOrElse {
      for {
        tempCatalogMap <- sessionIDToTempCatalogs.getResource(sessionID)
        configMap <- sessionIDToSQLProps.getResource(sessionID)
        sess <- Try(buildSession(sessionID, configMap, tempCatalogMap))
      } yield {
        sess
      }
    }
  }


  override def close(): Unit = {
    super.close()
    hInstance.shutdown()
  }


  private def buildSession(
                            sessionID: SessionID,
                            sqlConf: XDSQLConf,
                            xDTemporaryCatalogs: Seq[XDTemporaryCatalog],
                            coreConfig: Option[Config] = None): XDSession = {
    val sessionState = new XDSessionState(sqlConf, xDTemporaryCatalogs)
    new XDSession(sharedState, sessionState, coreConfig)
  }

}
