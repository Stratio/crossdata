/*
 * Licensed to STRATIO (C) under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.  The STRATIO (C) licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.stratio.crossdata.server.actors


import java.lang.management.ManagementFactory
import javax.management.{Attribute, AttributeList, MBeanServer, ObjectName}

import akka.actor.{Actor, ActorLogging, Props}
import com.stratio.crossdata.core.loadWatcher.LoadWatcherManager

import scala.concurrent.duration._

object LoadWatcherActor{
  def props(hostname:String): Props = Props(new LoadWatcherActor(hostname))
}

class LoadWatcherActor(hostname:String) extends Actor with ActorLogging {
  import context.dispatcher
  context.system.scheduler.schedule(5 seconds,5 seconds,self,"watchload")

  def watchLoad():Double={
    val mbs:MBeanServer= ManagementFactory.getPlatformMBeanServer()
    val name:ObjectName     = ObjectName.getInstance("java.lang:type=OperatingSystem")
    val list : AttributeList = mbs.getAttributes(name,Array[String]("ProcessCpuLoad") )

    if (list.isEmpty())return Double.NaN
    val value:Double   = list.get(0).asInstanceOf[Attribute].getValue().asInstanceOf[Double]
    if (value == -1.0) return Double.NaN

    ((value * 1000) / 10.0)
  }

  def receive = {
    //CLIENT MESSAGES
    case "watchload"=>
      LoadWatcherManager.MANAGER.createEntry(hostname,watchLoad(),true)
    case msg => {
      log.info(s"load watcher actor receives message: $msg and does not know what to do with it")
    }
  }
}
