package org.apache.spark.sql.crossdata

import java.util.UUID

import org.apache.spark.{Logging, SparkContext}
import com.typesafe.config.Config
import org.apache.spark.sql.crossdata.catalog.XDCatalog

/**
  *
  [[XDSession]], as with Spark 2.0, SparkSession will be the Crossdata entry point for SQL interfaces. It wraps and
  implements [[XDContext]]. Overriding those methods & attributes which vary among sessions and keeping
  common ones in the delegated [[XDContext]].

  Resource initialization is avoided through attribute initialization laziness.
 */
class XDSession (
                          @transient override val sc: SparkContext,
                          session: UUID,
                          userConfig: Option[Config] = None
                        ) extends XDContext(sc) with Logging {

  // xdContext will host common Crossdata context entities
  private val xdContext: XDContext = XDContext.getOrCreate(sc, userConfig) //Delegated XDContext


  //TODO: Use catalog for this session instead fix one
  override protected[sql] lazy val catalog: XDCatalog = xdContext.catalog


}