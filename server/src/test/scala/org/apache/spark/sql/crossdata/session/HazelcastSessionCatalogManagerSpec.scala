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
package org.apache.spark.sql.crossdata.session

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.hazelcast.config.Config
import com.hazelcast.core.{Hazelcast, HazelcastInstance}
import org.apache.spark.sql.catalyst.EmptyConf
import XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.catalog.interfaces.XDTemporaryCatalog
import org.apache.spark.sql.crossdata.session.HazelcastSessionCatalogManagerSpec.{InvalidatedSession, ProbedHazelcastSessionCatalogManager}
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}


object HazelcastSessionCatalogManagerSpec {

  private class ProbedHazelcastSessionCatalogManager(hInstance: HazelcastInstance)(implicit monitorActor: ActorRef)
    extends HazelcastSessionCatalogManager(hInstance, EmptyConf) {

    override def invalidateLocalCaches(key: SessionID): Unit = {
      super.invalidateLocalCaches(key)
      monitorActor ! InvalidatedSession(key)
    }

    override def invalidateAllLocalCaches: Unit = {
      super.invalidateAllLocalCaches
      monitorActor ! InvalidatedAllSessions
    }

  }

  case class InvalidatedSession(sessionID: SessionID)
  case object InvalidatedAllSessions

}

class HazelcastSessionCatalogManagerSpec extends TestKit(ActorSystem("HZSessionCatalogTest"))
  with WordSpecLike
  with BeforeAndAfterAll
  with ImplicitSender {

  // Test description

  "HazelcastSessionCatalogManager local cache" when {

    val sessionID: SessionID = UUID.randomUUID

    "a catalog change at other peer has been performed" should {

      "be invalidated if the change consist on adding or removing new catalogs" in {

        catalogManager.newResource(sessionID)
        expectMsg(InvalidatedSession(sessionID))

        catalogManager.deleteSessionResource(sessionID)
        expectMsg(InvalidatedSession(sessionID))

      }

      "be invalidated if the change consists on altering one of the managed in-memory catalogs" in {

        catalogManager.newResource(sessionID)
        expectMsg(InvalidatedSession(sessionID))

        val catalog: XDTemporaryCatalog = catalogManager.getResource(sessionID).get.head
        expectNoMsg()

        catalog.dropAllTables()
        expectMsg(InvalidatedSession(sessionID))

      }


    }

  }

  // Test plumbing

  private def createHazelcastInstance: HazelcastInstance = Hazelcast.newHazelcastInstance(new Config())

  var catalogManager: HazelcastSessionCatalogManager = _
  var probedCatalogManager: HazelcastSessionCatalogManager = _

  override protected def beforeAll(): Unit = {
    val hInstanceA = createHazelcastInstance
    val hInstanceB = createHazelcastInstance

    catalogManager = new HazelcastSessionCatalogManager(hInstanceA, EmptyConf)
    probedCatalogManager = new ProbedHazelcastSessionCatalogManager(hInstanceB)

  }

  override protected def afterAll(): Unit = Hazelcast shutdownAll

}
