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

package com.stratio.meta.server.config

import com.stratio.meta.common.result.{ErrorResult, Result}
import com.stratio.meta.communication.ACK
import org.testng.Assert._
import scala.concurrent.duration._
import com.stratio.meta.communication.ACK
import akka.testkit.{DefaultTimeout, TestKit, ImplicitSender}
import akka.actor.{ActorRef, ActorSystem}
import com.typesafe.config.ConfigFactory
import com.stratio.meta.server.utilities.TestKitUsageSpec
import com.stratio.meta.common.ask.Query

/**
 * Class with utility methods for testing actor communication.
 */
class ActorReceiveUtils extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
                        with ImplicitSender with DefaultTimeout{

  /**
   * Try to receive 2 messages in the current Actor.
   * @param ackExpected Whether an ACK is expected.
   * @param inOrder Whether the ACK is expected to arrive before the Results.
   * @param errorExpected Whether an error is expected.
   * @return The result message.
   */
  def receiveActorMessages(ackExpected: Boolean, inOrder: Boolean, errorExpected: Boolean) : Result = {
    val f = receiveOne(5 second)
    val s = receiveOne(5 second)
    val r : Seq[AnyRef] = List(f, s)

    val errors = r.filter(msg => msg.isInstanceOf[ErrorResult])
    if(errorExpected){
      assertTrue(errors.size > 0, "Expecting error")
    }else {
      if (errors.size > 0) {
        var errorMsg: StringBuilder = new StringBuilder
        val errorIt = errors.iterator
        while (errorIt.hasNext) {
          val e = errorIt.next().asInstanceOf[ErrorResult]
          errorMsg.append(e.getType)
          errorMsg.append(" - ")
          errorMsg.append(e.getErrorMessage)
          errorMsg.append(System.lineSeparator())
        }
        fail("Error not expected: " + errorMsg.toString());
      }
    }

    if(ackExpected) {
      val ackFound = r.filter(msg => msg.isInstanceOf[ACK]).size
      assertEquals(ackFound, 1, "ACK not received");
    }
    val filteredResult = r.filter( msg => msg.isInstanceOf[Result])
    assertEquals(filteredResult.size, 1, "Result not received")
    if(inOrder){
      assertEquals(r.iterator.next().getClass, ACK.getClass, "ACK not received before Results")
    }
    filteredResult.iterator.next().asInstanceOf[Result]
  }

  /**
   * Receive 2 messages expecting an ACK.
   * @return The result message.
   */
  def receiveWithACK() : Result = {
    receiveActorMessages(true, false, false)
  }

  /**
   * Receive 2 messages without expecting an ACK.
   * @return The result message.
   */
  def receiveWithoutACK() : Result = {
    receiveActorMessages(false, false, false)
  }

  /**
   * Receive 1 non-ack message.
   * @return The result message.
   */
  def receiveError() : Result = {
    receiveActorMessages(false, false, true)
  }

  /**
   * Get the error message if a {@link ErrorResult} is received.
   * @param metaResult The result.
   * @return The Error message.
   */
  def getErrorMMessage(metaResult: Result): String = {
    var result: String = "Invalid class: " + metaResult.getClass
    if (classOf[ErrorResult].isInstance(metaResult)) {
      result = classOf[ErrorResult].cast(metaResult).getErrorMessage
    }
    return result
  }

  /**
   * Execute a query on a remote actor.
   * @param targetActor Target actor.
   * @param query The target query.
   * @param catalog The target catalog.
   * @return The result.
   */
  def executeQuery(targetActor: ActorRef, query: String, catalog: String) : Result = {
    val stmt = Query("create-index", catalog, query, "test_actor")
    targetActor ! stmt
    val result = receiveWithACK()
    assertFalse(result.hasError, "Statement execution failed for:\n" + stmt.toString
                                 + "\n error: " + getErrorMMessage(result))
    result
  }

}
