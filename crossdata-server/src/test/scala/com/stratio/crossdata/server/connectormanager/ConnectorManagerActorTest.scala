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

package com.stratio.crossdata.server.connectormanager

import com.stratio.crossdata.common.result.ErrorResult
import com.stratio.crossdata.server.ServerActorTest
import org.apache.log4j.Logger
import org.scalatest.Suite

import scala.concurrent.duration.DurationInt

class ConnectorManagerActorTest extends ServerActorTest{
  this: Suite =>


  override lazy val logger = Logger.getLogger(classOf[ConnectorManagerActorTest])

  test("Should return a KO message") {
      within(6000 millis) {
        connectorManagerActor ! "non making sense message"
        expectMsgType[ErrorResult]
      }
  }
        /*
        fishForMessage(6 seconds){
        case msg:StorageResult =>{
          logger.info("receiving message of type "+msg.getClass()+"from "+lastSender)
          assert(msg.getQueryId==queryId + (1))
          true
        }
        case other:Any =>{
          logger.info("receiving message of type "+other.getClass()+" and ignoring it")
          false
        }
      }
        */

  
      //expectMsg(Connect.getClass)

   

      /*
      val pq= new SelectPlannedQuery(null,null)
      expectMsg("Ok") // bounded to 1 second

      //val m = mock[IConnector]
      //(m.getConnectorName _).expects().returning("My New CONNECTOR")
      //assert(m.getConnectorName().equals("My New CONNECTOR"))
      */
  }


