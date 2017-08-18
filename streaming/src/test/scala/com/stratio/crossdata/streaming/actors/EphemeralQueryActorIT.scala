/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.streaming.actors

import akka.actor.{ActorSystem, Props}
import akka.testkit.{DefaultTimeout, ImplicitSender, TestKit}
import com.stratio.crossdata.streaming.actors.EphemeralQueryActor._
import com.stratio.crossdata.streaming.test.CommonValues
import org.apache.curator.test.TestingServer
import org.apache.curator.utils.CloseableUtils
import org.junit.runner.RunWith
import org.scalatest.concurrent.TimeLimitedTests
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterAll, WordSpecLike}
import org.scalatest.time.SpanSugar._

@RunWith(classOf[JUnitRunner])
class EphemeralQueryActorIT(_system: ActorSystem) extends TestKit(_system)
with DefaultTimeout
with ImplicitSender
with WordSpecLike
with BeforeAndAfterAll
with TimeLimitedTests {

  def this() = this(ActorSystem("EphemeralQueryActor"))

  var zkTestServer: TestingServer = _
  var zookeeperConnection: String = _

  val timeLimit = 2 minutes

  override def beforeAll: Unit = {
    zkTestServer = new TestingServer()
    zkTestServer.start()
    zookeeperConnection = zkTestServer.getConnectString
  }

  override def afterAll: Unit = {
    CloseableUtils.closeQuietly(zkTestServer)
    zkTestServer.stop()
  }

  "EphemeralQueryActor" should {
    "set up with zookeeper configuration without any error" in {
      _system.actorOf(Props(new EphemeralQueryActor(Map("connectionString" -> zookeeperConnection))))
    }
  }

  "EphemeralQueryActor" must {

    "AddListener the first message" in new CommonValues {

      val ephemeralQueryActor =
        _system.actorOf(Props(new EphemeralQueryActor(Map("connectionString" -> zookeeperConnection))))

      ephemeralQueryActor ! EphemeralQueryActor.AddListener

      expectMsg(new ListenerResponse(true))
    }

    "AddListener the not be the first message" in new CommonValues {

      val ephemeralQueryActor =
        _system.actorOf(Props(new EphemeralQueryActor(Map("connectionString" -> zookeeperConnection))))

      ephemeralQueryActor ! EphemeralQueryActor.GetQueries

      expectNoMsg()
    }

    "GetQueries is the second message" in new CommonValues {

      val ephemeralQueryActor =
        _system.actorOf(Props(new EphemeralQueryActor(Map("connectionString" -> zookeeperConnection))))

      ephemeralQueryActor ! EphemeralQueryActor.AddListener
      expectMsg(new ListenerResponse(true))

      ephemeralQueryActor ! EphemeralQueryActor.GetQueries
      expectMsg(new EphemeralQueriesResponse(Seq()))
    }
  }
}
