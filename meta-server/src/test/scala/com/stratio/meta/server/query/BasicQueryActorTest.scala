package com.stratio.meta.server.query

import akka.testkit.{DefaultTimeout, TestKit}
import akka.actor.{Props, ActorSystem}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, FunSuiteLike}
import com.stratio.meta.server.actors.QueryActor

/**
 * Created by aalcocer on 4/4/14.
 * To generate unit test of proxy actor
 */
class BasicQueryActorTest extends TestKit(ActorSystem("TestKitUsageSpec",ConfigFactory.parseString(TestKitUsageSpec.config)))
with DefaultTimeout with FunSuiteLike with BeforeAndAfterAll
{

  val proxyRef=system.actorOf(Props(classOf[QueryActor], testActor,"test"))







}
object TestKitUsageSpec {
  // Define your test specific configuration here
  val config = """
    akka {
    loglevel = "WARNING"
    }
               """
}