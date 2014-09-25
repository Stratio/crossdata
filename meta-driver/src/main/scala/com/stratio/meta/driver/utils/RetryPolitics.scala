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

package com.stratio.meta.driver.utils

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.stratio.meta.common.result.Result

import scala.concurrent.Await

/**
 * Retry mechanism that send a message to an actor a number of times,
 * waiting between each retry.
 * @param retryTimes The number of times the query is retried.
 * @param waitTime Waiting time between retries.
 */
class RetryPolitics(retryTimes: Int, waitTime: Timeout) {
  def askRetry(remoteActor: ActorRef, message: AnyRef, waitTime: Timeout = this.waitTime, retry: Int = 0): Result = {
    if (retry == retryTimes) {
      Result.createConnectionErrorResult("Not found answer. After " + retry
        + " retries, timeout was exceed.");
    } else {
      try {
        val future = remoteActor.ask(message)(waitTime)
        Await.result(future.mapTo[Result], waitTime.duration * 2)
      } catch {
        case ex: Exception => {
          println("Retry " + (retry + 1) + " timeout")
          askRetry(remoteActor, message, waitTime, retry + 1)
        }
      }
    }
  }
}
