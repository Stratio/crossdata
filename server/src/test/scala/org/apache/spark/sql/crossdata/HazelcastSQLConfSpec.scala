/*
 * Copyright (C) 2015 Stratio (http://stratio.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.spark.sql.crossdata

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.hazelcast.core.{Hazelcast, HazelcastInstance}
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.HazelcastSQLConfSpec.{InvalidatedSession, ProbedHazelcastSessionConfigManager}
import org.apache.spark.sql.crossdata.session.HazelcastSessionConfigManager
import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

object HazelcastSQLConfSpec {

  class ProbedHazelcastSessionConfigManager(hInstance: HazelcastInstance)(
      implicit monitorActor: ActorRef)
      extends HazelcastSessionConfigManager(hInstance) {

    override def invalidateLocalCaches(key: SessionID): Unit = {
      super.invalidateLocalCaches(key)
      monitorActor ! InvalidatedSession(key)
    }

    override def invalidateAllLocalCaches: Unit = {
      super.invalidateAllLocalCaches
      monitorActor ! InvalidatedAllSessions
    }

  }

  // TODO: Extract common class providing this kind of tests plumbing
  private case class InvalidatedSession(sessionID: SessionID)
  private case object InvalidatedAllSessions

}

class HazelcastSQLConfSpec
    extends TestKit(ActorSystem("HZSessionConfigTest"))
    with WordSpecLike
    with BeforeAndAfterAll
    with ImplicitSender
    with Matchers {

  // Test description

  "HazelcastSessionConfigManager local cache" when {

    val sessionID: SessionID = UUID.randomUUID

    "a configuration change at other peer has been performed" should {

      "be invalidated if the change consist on adding or removing new sessions' configurations" in {

        configManager.newResource(sessionID)
        expectMsg(InvalidatedSession(sessionID))

        configManager.deleteSessionResource(sessionID)
        expectMsg(InvalidatedSession(sessionID))

      }

      "be invalidated if the change consists on altering one of the managed session configuration" in {

        configManager.newResource(sessionID)
        expectMsg(InvalidatedSession(sessionID))

        val sqlConf: SQLConf = configManager.getResource(sessionID).get
        expectNoMsg()

        import scala.util.Random

        sqlConf.unsetConf(Random.nextString(4))
        expectMsg(InvalidatedSession(sessionID))

      }

      "provide other peers with the updated value" in {

        configManager.newResource(sessionID)
        expectMsg(InvalidatedSession(sessionID))

        val sqlConf: SQLConf = configManager.getResource(sessionID).get
        expectNoMsg()

        sqlConf.setConfString("spark.sql.parquet.filterPushdown", "false")

        val sqlConfAtB: SQLConf =
          probedConfigManager.getResource(sessionID).get
        sqlConfAtB
          .getConfString("spark.sql.parquet.filterPushdown") shouldBe "false"

      }

    }

  }

  // Test plumbing

  // TODO: Extract common class providing this kind of tests plumbing
  private def createHazelcastInstance: HazelcastInstance =
    Hazelcast.newHazelcastInstance()

  var configManager: HazelcastSessionConfigManager = _
  var probedConfigManager: ProbedHazelcastSessionConfigManager = _

  override protected def beforeAll(): Unit = {
    val hInstanceA = createHazelcastInstance
    val hInstanceB = createHazelcastInstance

    configManager = new HazelcastSessionConfigManager(hInstanceA)
    probedConfigManager = new ProbedHazelcastSessionConfigManager(hInstanceB)

  }

  override protected def afterAll(): Unit = Hazelcast shutdownAll

}
