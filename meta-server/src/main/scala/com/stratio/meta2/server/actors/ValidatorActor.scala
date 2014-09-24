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

package com.stratio.meta2.server.actors

import akka.actor.{Actor, ActorRef, Props}
import com.stratio.meta2.core.query.ParsedQuery
import com.stratio.meta2.core.statements.MetaStatement
import com.stratio.meta2.core.validator.Validator
import org.apache.log4j.Logger

object ValidatorActor{
  def props(planner:ActorRef, validator:Validator): Props= Props(new ValidatorActor(planner,validator))
}

/**
 * Actor in charge of the validation of sentences.
 * @param planner The associated planner actor.
 * @param validator The associated com.stratio.meta.core.validator.Validator}.
 */
class ValidatorActor(planner:ActorRef, validator:Validator) extends Actor with TimeTracker{

  /**
   * Class logger.
   */
  val log= Logger.getLogger(classOf[ValidatorActor])

  /**
   * Name of the timer published through JMX.
   */
  override lazy val timerName= this.getClass.getName

  override def receive: Receive = {
    case query: ParsedQuery => {
      log.info("Validator Actor received ParsedQuery ")
      val validatedquery=validator.validate(query)
      log.info("Validator Actor sends validated query to planner ")
      log.info("Validatedquery= "+validatedquery)
      planner forward validatedquery
      sender ! "Ok"
    }
    case statement: MetaStatement=> {
      
      log.info("validator metaStatement")
    }
    case _ => {
      log.info("validator _")
      sender ! "KO"
      //sender ! Result.createUnsupportedOperationErrorResult("Message not recognized")
    }
  }
}
