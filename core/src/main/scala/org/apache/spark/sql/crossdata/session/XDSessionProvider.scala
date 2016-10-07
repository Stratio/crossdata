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

import java.lang.reflect.Constructor
import java.util.UUID

import com.stratio.crossdata.security.CrossdataSecurityManager
import com.typesafe.config.{Config, ConfigException, ConfigFactory}
import org.apache.log4j.Logger
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.{XDSQLConf, XDSession}
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDPersistentCatalog, XDStreamingCatalog, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.catalog.temporary.HashmapCatalog
import org.apache.spark.sql.crossdata.catalog.utils.CatalogUtils
import org.apache.spark.sql.crossdata.config.CoreConfig
import org.apache.spark.sql.crossdata.config.CoreConfig._

import scala.collection.mutable
import scala.util.{Failure, Success, Try}

object XDSessionProvider {
  type SessionID = UUID
}

// TODO It should share some of the XDContext fields. It will be possible when Spark 2.0 is released
// TODO sessionProvider should be threadSafe
abstract class XDSessionProvider(
                                  @transient val sc: SparkContext,
                                  protected val userCoreConfig: Config
                                ) extends CoreConfig {

  import XDSessionProvider._

  lazy val logger = Logger.getLogger(getClass)

  protected lazy val finalCoreConfig = userCoreConfig.withFallback(config)

  protected lazy val catalogConfig = Try(finalCoreConfig.getConfig(CoreConfig.CatalogConfigKey)).recover {
    case exception: ConfigException =>
      logger.debug(exception.getMessage, exception)
      ConfigFactory.empty()
  } get

  //NOTE: DO NEVER KEEP THE RETURNED REFERENCE FOR SEVERAL USES!
  def session(sessionID: SessionID): Try[XDSession]

  def newSession(sessionID: SessionID, userId: String): Try[XDSession]

  def closeSession(sessionID: SessionID): Try[Unit]

  /**
    * Close the underlying connections.
    *
    * It is called when the crossdata server is stopped, so if a session provider needs to open an external connection
    * it should be closed here.
    */
  def close(): Unit = {
    securityManager.foreach { secManager =>
      secManager.stop()
    }
  }

  @transient
  protected lazy val securityManager: Option[CrossdataSecurityManager] = {
    val securityManager = Try(finalCoreConfig.getString(SecurityClassConfigKey)).map { securityManagerClassName =>
      val securityManagerClass = Class.forName(securityManagerClassName)
      val constr: Constructor[_] = securityManagerClass.getConstructor()
      val secManager = constr.newInstance().asInstanceOf[CrossdataSecurityManager]
      secManager.start()
      secManager
    } toOption

    if (securityManager.isEmpty) {
      logger.warn("Authorization is not enabled, configure a security manager if needed")
    }
    securityManager
  }

}

/**
  * Session provider which store session info locally, so it can't be used when deploying several crossdata server
  */
class BasicSessionProvider(
                            @transient override val sc: SparkContext,
                            userCoreConfig: Config
                          ) extends XDSessionProvider(sc, userCoreConfig) {

  import XDSessionProvider._

  override lazy val logger = Logger.getLogger(classOf[BasicSessionProvider])

  // TODO Update DOC => user can set spark sql properties by adding crossdata-core.config.spark.<spark_option>=<option_value>
  private lazy val sqlConf: SQLConf = configToSparkSQL(finalCoreConfig, new SQLConf)

  @transient
  protected lazy val externalCatalog: XDPersistentCatalog = CatalogUtils.externalCatalog(sqlConf, catalogConfig)

  @transient
  protected lazy val streamingCatalog: Option[XDStreamingCatalog] = CatalogUtils.streamingCatalog(sqlConf, finalCoreConfig)

  private val sharedState = new XDSharedState(sc, sqlConf, externalCatalog, streamingCatalog, securityManager)

  private val sessionIDToSQLProps: mutable.Map[SessionID, SQLConf] = mutable.Map.empty
  private val sessionIDToTempCatalog: mutable.Map[SessionID, XDTemporaryCatalog] = mutable.Map.empty

  private val errorMessage =
    "A distributed context must be used to manage XDServer sessions. Please, use SparkSessions instead"

  override def newSession(sessionID: SessionID, userId: String): Try[XDSession] =
    Try {

      sharedState.sqlConf.setConfString(XDSQLConf.UserIdPropertyKey, userId)

      val tempCatalog = new HashmapCatalog(sqlConf)

      sessionIDToTempCatalog.put(sessionID, tempCatalog)
      sessionIDToSQLProps.put(sessionID, sharedState.sqlConf)

      buildSession(sharedState.sqlConf, tempCatalog, Some(userCoreConfig))
    }

  override def closeSession(sessionID: SessionID): Try[Unit] = {
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
    super.close()
    sessionIDToSQLProps.clear
    sessionIDToTempCatalog.clear
  }

  private def buildSession(sqlConf: XDSQLConf, xDTemporaryCatalog: XDTemporaryCatalog, userConfig: Option[Config] = None): XDSession = {
    val sessionState = new XDSessionState(sqlConf, xDTemporaryCatalog :: Nil)
    new XDSession(sharedState, sessionState, userConfig)
  }
}
