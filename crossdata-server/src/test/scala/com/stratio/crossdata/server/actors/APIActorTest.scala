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
import com.stratio.crossdata.common.ask.{APICommand, Command}
import com.stratio.crossdata.common.result.Result
import com.stratio.crossdata.server.config.{ServerConfig, ActorReceiveUtils}
import org.apache.log4j.Logger
import org.scalatest.{Suite, FunSuiteLike}
import akka.testkit.TestActorRef
import akka.pattern.ask
import scala.concurrent.ExecutionContext.Implicits.global



class APIActorTest extends ActorReceiveUtils with FunSuiteLike with ServerConfig {
  this: Suite =>

  override lazy val logger = Logger.getLogger(classOf[APIActorTest])


  val actorRef= TestActorRef(APIActor.props(new APIManagerMock()))


  test("Send COMMAND must WORK"){
    val cmd=new Command("QID", APICommand.LIST_CONNECTORS,null);
    val future = (actorRef ? cmd).mapTo[Result]
    future.onSuccess {
      case r :Any => {
        assert(!r.hasError)
      }
    }
  }

  test("Send other object must FAIL"){
    val cmd=6
    val future = (actorRef ? cmd).mapTo[Result]
    future.onSuccess {
      case r : Any => {
        assert(r.hasError)
      }
    }
  }

}
