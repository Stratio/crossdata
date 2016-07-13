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
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.catalyst.{CatalystConf, SimpleCatalystConf}
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDPersistentCatalog, XDStreamingCatalog, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.catalog.temporary.HashmapCatalog
import org.apache.spark.sql.crossdata.catalog.utils.CatalogUtils
import org.apache.spark.sql.crossdata.config.CoreConfig

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object XDSessionProvider {
  type SessionID = UUID
}

// TODO It should share some of the XDContext fields. It will be possible when Spark 2.0 is released
// TODO sessionProvider should be threadSafe
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

/**
  * Session provider which store session info locally, so it can't be used when deploying several crossdata server
  */
class BasicSessionProvider(
                             @transient override val sc: SparkContext,
                             userConfig: Config
                           ) extends XDSessionProvider(sc, Option(userConfig)) with CoreConfig {

  import XDSessionProvider._

  override lazy val logger = Logger.getLogger(classOf[BasicSessionProvider])

  private lazy val catalogConfig = config.getConfig(CoreConfig.CatalogConfigKey)

  protected lazy val catalystConf: CatalystConf = {
    import CoreConfig.CaseSensitive
    val caseSensitive: Boolean = catalogConfig.getBoolean(CaseSensitive)
    new SimpleCatalystConf(caseSensitive)
  }

  @transient
  protected lazy val externalCatalog: XDPersistentCatalog = CatalogUtils.externalCatalog(catalystConf, catalogConfig)

  @transient
  protected lazy val streamingCatalog: Option[XDStreamingCatalog] = CatalogUtils.streamingCatalog(catalystConf, config)

  private val sharedState = new XDSharedState(sc, Option(userConfig), externalCatalog, streamingCatalog)

  private val sessionIDToSQLProps: mutable.Map[SessionID, SQLConf] = mutable.Map.empty
  private val sessionIDToTempCatalog: mutable.Map[SessionID, XDTemporaryCatalog] = mutable.Map.empty


  private val errorMessage =
    "A distributed context must be used to manage XDServer sessions. Please, use SparkSessions instead"

  override def newSession(sessionID: SessionID): Try[XDSession] =
    Try {
      val tempCatalog = new HashmapCatalog(catalystConf)

      sessionIDToTempCatalog.put(sessionID, tempCatalog)
      sessionIDToSQLProps.put(sessionID, sharedState.sqlConf)

      buildSession(sharedState.sqlConf, tempCatalog)
    }

  override def closeSession(sessionID: SessionID): Try[Unit] =
    {
      for {
        _ <- sessionIDToSQLProps.remove(sessionID)
        _ <- sessionIDToTempCatalog.remove(sessionID)
      } yield ()
    } map {
      Success(_)
    } getOrElse {
      Failure(new RuntimeException(s"Cannot close session with sessionId=$sessionID"))
    }

  override def session(sessionID: SessionID): Try[XDSession] = {
    for {
      sqlConf <- sessionIDToSQLProps.get(sessionID)
      tempCatalog <- sessionIDToTempCatalog.get(sessionID)
    } yield buildSession(sqlConf, tempCatalog)
  } map {
    Success(_)
  } getOrElse {
    Failure(new RuntimeException(s"Cannot recover session with sessionId=$sessionID"))
  }

  override def close(): Unit = {
    sessionIDToSQLProps.clear
    sessionIDToTempCatalog.clear
  }

  private def buildSession(sqlConf: SQLConf, xDTemporaryCatalog: XDTemporaryCatalog): XDSession = {
    val sessionState = new XDSessionState(sqlConf, xDTemporaryCatalog :: Nil)
    new XDSession(sharedState, sessionState)
  }
}
