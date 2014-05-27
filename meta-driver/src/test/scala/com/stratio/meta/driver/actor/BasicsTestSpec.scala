package com.stratio.meta.driver.actor

import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.typesafe.config.ConfigFactory
import akka.testkit.{DefaultTimeout, TestKit}
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
with DefaultTimeout with FunSuiteLike with BeforeAndAfterAll
{
    val proxyRef=system.actorOf(Props(classOf[ProxyActor], testActor,"test"))

    val retryTestRef = system.actorOf(Props(classOf[ProxyActor], testActor,"test"))
    val retryPolitics: RetryPolitics = new RetryPolitics(3,1)

  override def afterAll() {
    shutdown(system)
  }

  test("ProxyActor Test send message"){
    var messages = Seq[Send]()
    within(500 millis) {
      proxyRef ! "test"
      expectMsg(Send(ProxyActor.remotePath("test"),"test",localAffinity = true))
      proxyRef ! "test1"
      expectMsg(Send(ProxyActor.remotePath("test"),"test1",localAffinity = true))
      proxyRef ! "test2"
      proxyRef ! "test3"

      receiveWhile(500 millis) {
        case msg:Send ⇒ messages = msg +: messages
      }
    }
    assertEquals(messages.length,2)
    assertEquals(messages.reverse,Seq(Send(ProxyActor.remotePath("test"),"test2",localAffinity = true),Send(ProxyActor.remotePath("test"),"test3",localAffinity = true)))


  }

  test("testing retryPolitics 1"){
    within(500 millis){
      retryPolitics.askRetry(retryTestRef,"Test")
     expectMsg(Send("/user/test","Test",localAffinity = true))
    }
  }
  test("testing retryPolitics 2"){
    within(5000 millis){

      val metaResultTest=retryPolitics.askRetry(retryTestRef,"Test")

      assertEquals(metaResultTest.getErrorMessage,ConnectResult.createFailConnectResult("Not found answer. After 3 retries, timeout was exceed.").getErrorMessage)
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

