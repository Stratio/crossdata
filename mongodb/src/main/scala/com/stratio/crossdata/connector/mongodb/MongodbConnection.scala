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
package com.stratio.crossdata.connector.mongodb

import com.mongodb.casbah.Imports._
import com.mongodb.{MongoCredential, ServerAddress}
import com.stratio.datasource.mongodb.client.MongodbClientActor.ClientResponse
import com.stratio.datasource.mongodb.client.MongodbClientFactory
import com.stratio.datasource.mongodb.config.{MongodbConfig, MongodbCredentials, MongodbSSLOptions}
import com.stratio.datasource.util.Config

import scala.language.reflectiveCalls


object MongodbConnection {

  import MongodbConfig._

  // TODO refactor datasource
  def withClientDo[T](config: Config)(code: MongoClient => T): T = {

    val connectionsTime = config.get[String](MongodbConfig.ConnectionsTime).map(_.toLong)

    val clientResponse = openClient(config)

    val mongoClientOpt = Option(clientResponse.clientConnection)
    val mongoClientKey = clientResponse.key
    val mongoClient = mongoClientOpt.getOrElse(throw new RuntimeException("Error while getting mongo client"))

    try {
      code(mongoClient)
    } finally {
      MongodbClientFactory.setFreeConnectionByKey(mongoClientKey, connectionsTime)
      MongodbClientFactory.closeByKey(mongoClientKey)
    }

  }

  def withCollectionDo[T](config: Config)(code: MongoCollection => T): T = {
    val databaseName: String = config(MongodbConfig.Database)
    val collectionName: String = config(MongodbConfig.Collection)

    withClientDo[T](config) { mClient =>
      code(mClient(databaseName)(collectionName))
    }
  }

  private def openClient(config: Config): ClientResponse = {

    val hosts: List[ServerAddress] = config[List[String]](Host).map(add => new ServerAddress(add))
    val credentials = config.getOrElse[List[MongodbCredentials]](MongodbConfig.Credentials, MongodbConfig.DefaultCredentials).map {
      case MongodbCredentials(user, database, password) =>
        MongoCredential.createCredential(user, database, password)
    }
    val sslOptions = config.get[MongodbSSLOptions](MongodbConfig.SSLOptions)
    val clientOptions = config.properties.filterKeys(_.contains(MongodbConfig.ListMongoClientOptions))

    MongodbClientFactory.getClient(hosts, credentials, sslOptions, clientOptions)
  }

}