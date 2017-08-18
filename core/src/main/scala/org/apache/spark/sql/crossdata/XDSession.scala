package org.apache.spark.sql.crossdata

import com.stratio.crossdata.security.CrossdataSecurityManager
import com.typesafe.config.Config
import org.apache.spark.Logging
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon
import org.apache.spark.sql.crossdata.catalog.{CatalogChain, XDCatalog}
import org.apache.spark.sql.crossdata.session.{XDSessionState, XDSharedState}

object XDSession {
  // TODO Spark2.0. It will be the main entryPoint, so we should add a XDSession builder to make it easier to work with.
}

/**
  *
  * [[XDSession]], as with Spark 2.0, SparkSession will be the Crossdata entry point for SQL interfaces. It wraps and
  * implements [[XDContext]]. Overriding those methods & attributes which vary among sessions and keeping
  * common ones in the delegated [[XDContext]].
  *
  * Resource initialization is avoided through attribute initialization laziness.
  */
class XDSession(
                 @transient private val xdSharedState: XDSharedState,
                 @transient private val xdSessionState: XDSessionState,
                 @transient private val userCoreConfig: Option[Config] = None
               )
  extends XDContext(xdSharedState.sc, userCoreConfig) with Logging {

  @transient
  override protected[sql] lazy val catalog: XDCatalog = {
    val catalogs: Seq[XDCatalogCommon] = (xdSessionState.temporaryCatalogs :+ xdSharedState.externalCatalog) ++ xdSharedState.streamingCatalog.toSeq
    CatalogChain(catalogs: _*)(this)
  }

  @transient
  override protected[sql] lazy val conf: SQLConf = xdSessionState.sqlConf.enableCacheInvalidation(false)

  @transient
  override protected[crossdata] lazy val securityManager: Option[CrossdataSecurityManager] = xdSharedState.securityManager

  xdSessionState.sqlConf.enableCacheInvalidation(true)

}
