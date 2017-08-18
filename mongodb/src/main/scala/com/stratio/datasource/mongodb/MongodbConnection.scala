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
    MongodbClientFactory.getClient(config.hosts, config.credentials, config.sslOptions, config.clientOptions)


}