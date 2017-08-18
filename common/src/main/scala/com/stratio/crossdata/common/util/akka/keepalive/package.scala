/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.common.util.akka

import com.stratio.crossdata.common.util.akka.keepalive.{KeepAliveMaster, LiveMan}

/**
  * [[com.stratio.crossdata.common.util.akka.keepalive]] object provides the tools to easily implement a dead man switch
  * (https://en.wikipedia.org/wiki/Dead_man%27s_switch) for actors. This dead switch is not dependant on
  * low-level details, neither on the actor reference or address within an ActorSystem, remoting or akka clustering
  * mechanisms.
  *
  * Each monitored actor provides a customized id whereby the [[KeepAliveMaster]] or controller identifies it.
  * Any akka actor can make use of the stackable modificator [[LiveMan]] trait to automatically become
  * a monitored actor.
  *
  * The monitor is itself an actor which notifies heartbeat losses by the means of [[keepalive.KeepAliveMaster.HeartbeatLost]]
  * events.
  *
  */
package object keepalive