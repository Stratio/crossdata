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

package com.stratio.crossdata.util.akka

/**
  * [[KeepAlive]] object provides the tools to easily implement a dead man switch
  * (https://en.wikipedia.org/wiki/Dead_man%27s_switch) for actors. This dead switch is not dependant on
  * low-level details, neither on the actor reference or address within an ActorSystem, remoting or akka clustering
  * mechanisms.
  *
  * Each monitored actor provides a customized id whereby the [[KeepAlive.KeepAliveMaster]] or controller identifies it.
  * Any akka actor can make use of the stackable modificator [[KeepAlive.LiveMan]] trait to automatically become
  * a monitored actor.
  *
  * The monitor is itself an actor which notifies heartbeat losses by the means of [[KeepAlive.KeepAliveMaster.HeartbeatLost]]
  * events.
  *
  */
package object KeepAlive