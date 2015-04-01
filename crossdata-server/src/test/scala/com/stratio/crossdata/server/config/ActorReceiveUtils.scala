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

package com.stratio.crossdata.server.config

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.stratio.crossdata.common.ask.Query
import com.stratio.crossdata.communication.ACK
import com.stratio.crossdata.server.utilities.TestKitUsageSpec
import com.stratio.crossdata.common.result.{ErrorResult, Result}
import com.typesafe.config.ConfigFactory
import org.testng.Assert.{assertTrue,assertEquals,fail,assertFalse}

import scala.concurrent.duration._

/**
 * Class with utility methods for testing actor communication.
 */
class ActorReceiveUtils extends TestKit(ActorSystem("TestKitUsageSpec", ConfigFactory.parseString(TestKitUsageSpec.config)))
with ImplicitSender with DefaultTimeout {

  /**
   * Receive 2 messages without expecting an ACK.
   * @return The result message.
   */
  def receiveWithoutACK(): Result = {
    receiveActorMessages(false, false, false)
  }

  /**
   * Try to receive 2 messages in the current Actor.
   * @param ackExpected Whether an ACK is expected.
   * @param inOrder Whether the ACK is expected to arrive before the Results.
   * @param errorExpected Whether an error is expected.
   * @return The result message.
   */
  def receiveActorMessages(ackExpected: Boolean, inOrder: Boolean, errorExpected: Boolean): Result = {
    val f = receiveOne(5 second)
    val s = receiveOne(5 second)
    val r: Seq[AnyRef] = List(f, s)

    val errors = r.filter(msg => msg.isInstanceOf[ErrorResult])
    if (errorExpected) {
      assertTrue(errors.size > 0, "Expecting error")
    } else {
      if (errors.size > 0) {
        var errorMsg: StringBuilder = new StringBuilder
        val errorIt = errors.iterator
        while (errorIt.hasNext) {
          val e = errorIt.next().asInstanceOf[ErrorResult]
          errorMsg.append(" - ")
          errorMsg.append(e.getErrorMessage)
          errorMsg.append(System.lineSeparator())
        }
        fail("Error not expected: " + errorMsg.toString());
      }
    }

    if (ackExpected) {
      val ackFound = r.filter(msg => msg.isInstanceOf[ACK]).size
      assertEquals(ackFound, 1, "ACK not received");
    }
    val filteredResult = r.filter(msg => msg.isInstanceOf[Result])
    assertEquals(filteredResult.size, 1, "Result not received")
    if (inOrder) {
      assertEquals(r.iterator.next().getClass, ACK.getClass, "ACK not received before Results")
    }
    filteredResult.iterator.next().asInstanceOf[Result]
  }

  /**
   * Receive 1 non-ack message.
   * @return The result message.
   */
  def receiveError(): Result = {
    receiveActorMessages(false, false, true)
  }

  /**
   * Execute a query on a remote actor.
   * @param targetActor Target actor.
   * @param query The target query.
   * @param catalog The target catalog.
   * @return The result.
   */
  def executeQuery(targetActor: ActorRef, query: String, catalog: String): Result = {
    val stmt = Query("create-index", catalog, query, "test_actor","sessionTest")
    targetActor ! stmt
    val result = receiveWithACK()
    assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
      + "\n error: " + getErrorMMessage(result))
    result
  }

  /**
   * Receive 2 messages expecting an ACK.
   * @return The result message.
   */
  def receiveWithACK(): Result = {
    receiveActorMessages(true, false, false)
  }

  /**
   * Get the error message if a {@link ErrorResult} is received.
   * @param crossDataResult The result.
   * @return The Error message.
   */
  def getErrorMMessage(crossDataResult: Result): String = {
    var result: String = "Invalid class: " + crossDataResult.getClass
    if (classOf[ErrorResult].isInstance(crossDataResult)) {
      result = classOf[ErrorResult].cast(crossDataResult).getErrorMessage
    }
    result
  }

}
