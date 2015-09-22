/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.stratio.crossdata.driver.utils

import java.util.concurrent.TimeUnit

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import org.apache.log4j.Logger

import scala.concurrent.Await

class RetryPolitics {
  lazy val logger = Logger.getLogger(getClass)
  val retryTimes = 2

  def askRetry(remoteActor: ActorRef,
               message: AnyRef,
               waitTime: Timeout = Timeout(10, TimeUnit.SECONDS),
               retry: Int = 0): String =
    if (retry == retryTimes) s"Not found answer. After $retry + retries, timeout was exceed."
    else try {
      val future = remoteActor.ask(message)(waitTime)
      logger.info("Sending query...")
      Await.result(future.mapTo[String], waitTime.duration)
    } catch {
      case ex: Exception => {
        logger.error(ex.getMessage)
        logger.info(s"Retry ${retry + 1} timeout")
        askRetry(remoteActor, message, waitTime, retry + 1)
      }
    }
}
