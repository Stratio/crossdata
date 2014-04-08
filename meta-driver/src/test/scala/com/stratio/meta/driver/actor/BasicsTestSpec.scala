package com.stratio.meta.driver.actor

import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.typesafe.config.ConfigFactory
import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.ActorSystem
import akka.actor.Props
import scala.concurrent.duration._
import akka.contrib.pattern.ClusterClient.Send
import com.stratio.meta.driver.utils.RetryPolitics
import com.stratio.meta.common.result.MetaResult
import org.testng.Assert._
import com.stratio.meta.driver.BasicDriver

/**
 * Created by aalcocer on 4/3/14.
 *To generate unit test of proxy actor
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
    within(500 millis) {
      proxyRef ! "test"
      expectMsg(Send(ProxyActor.remotePath("test"),"test",localAffinity = true))
    }
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

      assertEquals(metaResultTest.getErrorMessage,MetaResult.createMetaResultError("Not found answer").getErrorMessage)
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

