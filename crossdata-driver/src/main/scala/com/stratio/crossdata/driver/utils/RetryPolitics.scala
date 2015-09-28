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

import akka.actor.ActorRef
import akka.pattern.ask
import akka.util.Timeout
import com.stratio.crossdata.common.SQLResult
import org.apache.log4j.Logger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Future, TimeoutException}
import scala.language.postfixOps

object RetryPolitics {

  lazy val RetryLogger = Logger.getLogger(getClass)

  private def retryAction[T](n: Int = 2): ((() => Future[T]) => Future[T]) = {
    action: (() => Future[T]) =>
      if (n == 0) Future.failed(new TimeoutException)
      else action().recoverWith {
        case e: TimeoutException =>
          RetryLogger.error(s"Retry failed: ${e.getMessage}")
          retryAction(n - 1)(action)
      }
  }

  def askRetry(remoteActor: ActorRef,
               message: AnyRef,
               waitTime: Timeout = Timeout(10 seconds),
               n: Int = 2): Future[SQLResult] = {
    retryAction(n) { () =>
      remoteActor.ask(message)(waitTime).mapTo[SQLResult]
    }
  }

}
