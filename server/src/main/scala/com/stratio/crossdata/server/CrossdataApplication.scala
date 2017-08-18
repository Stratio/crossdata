/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
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