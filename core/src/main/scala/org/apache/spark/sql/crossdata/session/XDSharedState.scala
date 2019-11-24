/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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
