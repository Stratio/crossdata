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

/**
 * Created by carlos on 1/10/14.
 */


package com.stratio.meta2.server.mocks

import akka.actor.{Actor, ActorLogging, Props}


object State extends Enumeration {
  type state = Value
  val Started, Stopping, Stopped = Value
}

object MockConnectorManagerActor {
  def props(): Props = Props(new MockConnectorManagerActor())
}


class MockConnectorManagerActor() extends Actor with ActorLogging {


  // subscribe to cluster changes, re-subscribe when restart

  override def preStart(): Unit = {
    //#subscribe
    //Cluster(context.system).subscribe(self, classOf[MemberEvent])
    //cluster.subscribe(self, initialStateMode = InitialStateAsEvents, classOf[MemberEvent], classOf[UnreachableMember])
  }

  override def receive = {
    case _ =>{
      //sender ! Result.createUnsupportedOperationErrorResult("Not recognized object")
      sender ! "ko"
    }
  }
}
