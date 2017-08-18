package com.stratio.crossdata.server

import com.stratio.crossdata.server.config.ServerConfig
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.JavaConversions._


object CrossdataApplication extends App {

  val mapConfig = {
    val (keys, values) = args.toList.partition(_.startsWith("--"))
    val argsConfig = keys.map(_.replace("--", "")).zip(values).toMap
    ConfigFactory.parseMap(argsConfig)
  }

  val params = mapConfig match {
    case m: Config if !m.isEmpty => Some(m)
    case _ => None
  }

  val crossdataServer = new CrossdataServer(new ServerConfig(params))
  crossdataServer.start()

}