package com.stratio.meta.driver.actor

import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.typesafe.config.ConfigFactory
import akka.testkit.{ImplicitSender, DefaultTimeout, TestKit}
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import akka.contrib.pattern.ClusterClient.Send
import com.stratio.meta.driver.utils.RetryPolitics
import com.stratio.meta.common.result.ConnectResult
import org.testng.Assert._
import com.stratio.meta.driver.BasicDriver

/**
 * To generate unit test of proxy actor
 */
class BasicsTestSpec extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
                             with ImplicitSender with DefaultTimeout with FunSuiteLike with BeforeAndAfterAll{

    val proxyRef=system.actorOf(Props(classOf[ProxyActor], testActor, "test", null))

    val retryTestRef = system.actorOf(Props(classOf[ProxyActor], testActor, "test", null))
    val retryPolitics: RetryPolitics = new RetryPolitics(3,1)

  override def afterAll() {
    shutdown(system)
  }

  test("ProxyActor Test send message"){
    var messages = Seq[Send]()
    within(500 millis) {
      proxyRef ! "test"
      expectMsg("Message type not supported")
    }
  }

  test("testing retryPolitics 1"){
    within(500 millis){
      val r = retryPolitics.askRetry(retryTestRef, "Test").asInstanceOf[ConnectResult]
      assertTrue(r.hasError, "The actor should not forward the message");
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

