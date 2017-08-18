/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.server.config

import com.typesafe.config.Config


object NumberActorConfig {
  val DefaultServerExecutorInstances = 5
  val ServerActorInstancesMin = "config.akka.number.server-actor-min"
  val ServerActorInstancesMax = "config.akka.number.server-actor-max"
}

trait NumberActorConfig {

  import NumberActorConfig.ServerActorInstancesMin
  import NumberActorConfig.ServerActorInstancesMax
  import NumberActorConfig.DefaultServerExecutorInstances
  lazy val minServerActorInstances: Int = Option(config.getString(ServerActorInstancesMin)).map(_.toInt).getOrElse(DefaultServerExecutorInstances)
  lazy val maxServerActorInstances: Int = Option(config.getString(ServerActorInstancesMax)).map(_.toInt).getOrElse(minServerActorInstances*2)
  def config: Config

}
