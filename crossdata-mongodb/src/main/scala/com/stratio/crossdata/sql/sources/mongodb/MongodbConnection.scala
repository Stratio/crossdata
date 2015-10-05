/*
 *  Licensed to STRATIO (C) under one or more contributor license agreements.
 *  See the NOTICE file distributed with this work for additional information
 *  regarding copyright ownership. The STRATIO (C) licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package com.stratio.crossdata.sql.sources.mongodb

import com.mongodb.MongoCredential
import com.mongodb.casbah.Imports._
import com.stratio.provider.Config
import com.stratio.provider.mongodb.reader.MongodbReadException
import com.stratio.provider.mongodb.{MongodbClientFactory, MongodbConfig, MongodbCredentials, MongodbSSLOptions}

import scala.language.reflectiveCalls
import scala.util.Try


object MongodbConnection {


  // TODO avoid openning a connection per query

  def withCollectionDo[T](config: Config)(code: MongoCollection => T): T = {

    val databaseName: String = config(MongodbConfig.Database)
    val collectionName: String = config(MongodbConfig.Collection)
    var client: Option[MongoClient] = None
    try{
      client = Some(openClient(config))
      code(client.get(databaseName)(collectionName))
    } finally{
      client.foreach(_.close())
    }
  }


  private def openClient(config: Config): MongoClient = {

    val hosts: List[ServerAddress] = config[List[String]](MongodbConfig.Host).map(add => new ServerAddress(add))

    val credentials: List[MongoCredential] =
    config[List[MongodbCredentials]](MongodbConfig.Credentials).map{
      case MongodbCredentials(user,database,password) =>
        MongoCredential.createCredential(user,database,password)
    }
    val ssloptions: Option[MongodbSSLOptions] = config.get[MongodbSSLOptions](MongodbConfig.SSLOptions)
    val readpreference: String = config[String](MongodbConfig.readPreference)

    val mongoClient: Try[MongoClient] = Try {
      MongodbClientFactory.createClient(
        hosts,
        config[List[MongodbCredentials]](MongodbConfig.Credentials).map {
          case MongodbCredentials(user, database, password) =>
            MongoCredential.createCredential(user, database, password)
        },
        config.get[MongodbSSLOptions](MongodbConfig.SSLOptions), config[String](MongodbConfig.readPreference))
    }.recover {
      case throwable =>
        throw MongodbReadException(throwable.getMessage, throwable)
    }
    mongoClient.get

  }
}
