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

package com.stratio.meta2.server.parser

import akka.actor.{ActorSystem, actorRef2Scala}
import com.stratio.meta.common.ask.Query
import com.stratio.meta2.server.actors.{ParserActor, ValidatorActor}
import com.stratio.meta.server.config.{ActorReceiveUtils, ServerConfig}
import com.stratio.meta2.core.parser.Parser
import com.stratio.meta2.core.engine.Engine
import com.stratio.meta2.server.utilities.createEngine
import org.apache.log4j.Logger
import org.scalatest.{FunSuiteLike, Suite}

import scala.concurrent.duration.DurationInt

class ParserActorIntegrationTest  extends ActorReceiveUtils with FunSuiteLike with ServerConfig{
    this:Suite =>

    val engine:Engine =  createEngine.create()

    override lazy val logger =Logger.getLogger(classOf[ParserActorIntegrationTest])
    lazy val system1 = ActorSystem(clusterName,config)

    val validatorRef = system.actorOf(ValidatorActor.props(null,engine.getValidator()),"TestValidatorActor")
    val parserActor= {
      system1.actorOf(ParserActor.props(validatorRef,engine.getParser()), "TestParserActor")
    }

    test("Should return a KO message") {
		  within(1000 millis){
	  		parserActor! "non-sense making message"
	  		expectMsg("KO") // bounded to 1 second
        assert(true)
	  		}
		}

    test("Should send a message to the validator and return Ok") {
      within(5000 millis){
        val query=  new Query("queryId", "catalog", "select * from mytable;", "carlos")
        parserActor! query
        expectMsg("Ok") // bounded to 1 second
        assert(true)
      }
    }

}


