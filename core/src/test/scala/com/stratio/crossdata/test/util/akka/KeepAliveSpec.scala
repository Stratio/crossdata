package com.stratio.crossdata.test.util.akka

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.testkit.TestKit
import com.stratio.crossdata.util.akka.KeepAlive.{DoCheck, HeartBeat, LiveMan, Master}
import org.scalatest.{FlatSpecLike, Matchers}

import scala.concurrent.duration._


class KeepAliveSpec extends TestKit(ActorSystem("KeepAliveSpec"))
  with FlatSpecLike with Matchers {

  class MonitoredActor(override val keepAliveId: Int, override val master: ActorRef) extends LiveMan[Int] {
    override val period: FiniteDuration = 100 milliseconds

    override def receive: Receive = PartialFunction.empty
  }

  abstract class MasterActorReceive extends Actor {
    override def receive: Receive = PartialFunction.empty
  }

  case class HeartbeatLost(x: Int)

  // https://issues.scala-lang.org/browse/SI-912
  class MasterActor extends MasterActorReceive with Master[Int] {
    override protected def onMiss(id: Int): Boolean = {
      testActor ! HeartbeatLost(id)
      false
    }
  }

  "A LiveMan Actor" should "periodically send HearBeat message providing its id" in {

    val kaId = 1

    val liveMan: ActorRef = system.actorOf(Props(new MonitoredActor(kaId, testActor)))
    expectMsg(HeartBeat(kaId))

    system.stop(liveMan)
  }



  "A Master Actor" should "detect when a LiveManActor stops beating" in {

    val master: ActorRef = system.actorOf(Props(new MasterActor))

    val liveMen: Seq[(Int, ActorRef)] = (1 to 5) map { idx =>
      master ! DoCheck(idx, 200 milliseconds)
      idx -> system.actorOf(Props(new MonitoredActor(idx, master)))
    }

    // All live actors are letting the master know that they're alive
    expectNoMsg(500 milliseconds)

    // Lets stop the first one
    system.stop(liveMen.head._2)

    // And wait for the right detection of its loss
    expectMsg(500 milliseconds, HeartbeatLost(liveMen.head._1))

    // Since the master is set to stop monitoring down actors and the rest of `liveMen` are working, no more loss
    // notifications should be expected.
    expectNoMsg(1 second)

    // Until another monitored actor is down
    val (lastId, lastActor) = liveMen.last

    system.stop(lastActor)

    expectMsg(500 milliseconds, HeartbeatLost(lastId))

    system.stop(master)
    liveMen foreach {
      case (_, monitoredActor) => system.stop(monitoredActor)
    }

  }


}
