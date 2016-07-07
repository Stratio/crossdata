package org.apache.spark.sql.crossdata

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import akka.testkit.{ImplicitSender, TestKit}
import com.hazelcast.config.Config
import com.hazelcast.core.{Hazelcast, HazelcastInstance}
import org.apache.spark.sql.SQLConf
import org.apache.spark.sql.crossdata.HazelcastSQLConfSpec.{InvalidatedSession, ProbedHazelcastSessionConfigManager}
import org.apache.spark.sql.crossdata.XDSessionProvider.SessionID
import org.apache.spark.sql.crossdata.session.HazelcastSessionConfigManager
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

object HazelcastSQLConfSpec {

  class ProbedHazelcastSessionConfigManager(hInstance: HazelcastInstance)(implicit monitorActor: ActorRef)
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

class HazelcastSQLConfSpec extends TestKit(ActorSystem("HZSessionConfigTest"))
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

        sqlConf.setConfString("spark.sql.parquet.filterPushdown","false")

        val sqlConfAtB: SQLConf = probedConfigManager.getResource(sessionID).get
        sqlConfAtB.getConfString("spark.sql.parquet.filterPushdown") shouldBe "false"

      }


    }




  }


  // Test plumbing

  // TODO: Extract common class providing this kind of tests plumbing
  private def createHazelcastInstance: HazelcastInstance = Hazelcast.newHazelcastInstance(new Config())

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
