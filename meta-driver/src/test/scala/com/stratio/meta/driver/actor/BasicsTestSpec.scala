package com.stratio.meta.driver.actor

import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.typesafe.config.ConfigFactory
import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import akka.contrib.pattern.ClusterClient.Send


/**
 * Created by aalcocer on 4/3/14.
 *To generate unit test of proxy actor
 */
class BasicsTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterAll
{
    val proxyRef=system.actorOf(Props(classOf[ProxyActor], testActor,"test"))

  override def afterAll() {
    shutdown(system)
  }

  test("ProxyActor Test send message"){
    within(500 millis) {
      proxyRef ! "test"
      expectMsg(Send(ProxyActor.remotePath("test"),"test",localAffinity = true))
    }
  }


}
object TestKitUsageSpec {
  // Define your test specific configuration here
  val config = """
    akka {
    loglevel = "WARNING"
    }
               """
}

