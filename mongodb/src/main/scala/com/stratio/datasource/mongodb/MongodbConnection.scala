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
package com.stratio.datasource.mongodb

import com.mongodb.casbah.Imports._
import com.stratio.datasource.mongodb.client.MongodbClientFactory
import com.stratio.datasource.mongodb.config.{MongodbConfig, MongodbConfigReader}
import com.stratio.datasource.util.{Config, using}

object MongodbConnection {

  import MongodbConfigReader._

  def withClientDo[T](config: Config)(code: MongoClient => T): T = {
    using(openClient(config)) {
      code
    }
  }

  def withCollectionDo[T](config: Config)(code: MongoCollection => T): T = {
    val databaseName: String = config(MongodbConfig.Database)
    val collectionName: String = config(MongodbConfig.Collection)

    withClientDo[T](config) { mClient =>
      code(mClient(databaseName)(collectionName))
    }
  }

  private def openClient(config: Config): MongoClient =
    MongodbClientFactory
      .getClient(config.hosts, config.credentials, config.sslOptions, config.clientOptions)

}
