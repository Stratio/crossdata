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

import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDCatalogCommon, XDStreamingCatalog}
import org.apache.spark.sql.crossdata.security.api.CrossdataSecurityManager



final class XDSharedState(
                           @transient val sc: SparkContext,
                           val sqlConf: SQLConf,
                           val externalCatalog: XDCatalogCommon,
                           val streamingCatalog: Option[XDStreamingCatalog],
                           @transient val securityManager: Option[CrossdataSecurityManager]
                         )
