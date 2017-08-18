package org.apache.spark.sql.crossdata.session

import com.stratio.crossdata.security.CrossdataSecurityManager
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDCatalogCommon, XDStreamingCatalog}


final class XDSharedState(
                           @transient val sc: SparkContext,
                           val sqlConf: SQLConf,
                           val externalCatalog: XDCatalogCommon,
                           val streamingCatalog: Option[XDStreamingCatalog],
                           @transient val securityManager: Option[CrossdataSecurityManager]
                         )
