/**
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

import com.mongodb.casbah.Imports.{MongoClient, MongoCollection}
import com.mongodb.{MongoCredential, ServerAddress}
import com.stratio.datasource.mongodb.client.MongodbClientFactory
import com.stratio.datasource.util.Config
import com.stratio.datasource.mongodb.reader.MongodbReadException
import com.stratio.datasource.mongodb.config.{ MongodbConfig, MongodbCredentials, MongodbSSLOptions}

import scala.language.reflectiveCalls
import scala.util.Try


object MongodbConnection {

  import MongodbConfig._

  // TODO avoid openning a connection per query

  def withClientDo[T](hosts: List[String])(code: MongoClient => T): T = {

    val mongoClient: Try[MongoClient] = Try {
      MongoClient(hosts.map(new ServerAddress(_)))
    }.recover {
      case throwable =>
        throw MongodbReadException(throwable.getMessage, throwable)
    }

    try{
      code(mongoClient.get)
    } finally{
      mongoClient.foreach(_.close())
    }
  }


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

    val hosts: List[ServerAddress] = config[List[String]](Host).map(add => new ServerAddress(add))

    val credentials: List[MongoCredential] =
    config.getOrElse[List[MongodbCredentials]](Credentials, DefaultCredentials).map{
      case MongodbCredentials(user,database,password) =>
        MongoCredential.createCredential(user,database,password)
    }

    val ssloptions: Option[MongodbSSLOptions] = config.get[MongodbSSLOptions](SSLOptions)

      val mongoClient: Try[MongoClient] = Try {
         MongodbClientFactory.getClient(hosts, credentials, ssloptions, config.properties).clientConnection
      }recover{
        case throwable =>
          throw MongodbReadException(throwable.getMessage, throwable)
      }
      mongoClient.get
  }
}
