/*
 * Stratio Meta
 *
 * Copyright (c) 2014, Stratio, All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 */

package com.stratio.meta.server.actors

import akka.actor.{Actor, Props}
import com.stratio.meta.core.metadata.MetadataManager
import org.apache.log4j.Logger
import com.stratio.meta.common.result.CommandResult
import com.stratio.meta.common.ask.Command
import com.stratio.meta.core.api.APIManager

object APIActor{
  def props(metadata: APIManager): Props = Props(new APIActor(metadata))
}

class APIActor(metadata: APIManager) extends Actor with TimeTracker {
  val log =Logger.getLogger(classOf[APIActor])
  override val timerName= this.getClass.getName

  def receive = {
    case cmd:Command => {
      val timer = initTimer()
      sender ! metadata.processRequest(cmd)
      finishTimer(timer)
    }
    case _ => {
      sender ! CommandResult.createFailCommanResult("Unsupported command: ");
    }
  }
}