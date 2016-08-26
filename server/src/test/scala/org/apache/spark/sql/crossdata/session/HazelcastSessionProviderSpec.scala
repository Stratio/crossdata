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

import com.hazelcast.config.{Config => HZConfig}
import com.hazelcast.core.Hazelcast
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.SparkContext
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.plans.logical.LocalRelation
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.catalog.CatalogChain
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDPersistentCatalog, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.catalog.temporary.{HashmapCatalog, HazelcastCatalog, XDTemporaryCatalogWithInvalidation}
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.junit.runner.RunWith
import org.scalatest.Entry
import org.scalatest.junit.JUnitRunner

import scala.util.{Success, Try}

@RunWith(classOf[JUnitRunner])
class HazelcastSessionProviderSpec extends SharedXDContextTest {

  val SparkSqlConfigString = "config.spark.sql.inMemoryColumnarStorage.batchSize=5000"


"HazelcastSessionProvider" should "provides new sessions whose properties are initialized properly" in {

    val hazelcastSessionProvider = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.parseString(SparkSqlConfigString))

    val session = createNewSession(hazelcastSessionProvider)

    session.conf.settings should contain(Entry("spark.sql.inMemoryColumnarStorage.batchSize", "5000"))

    val tempCatalogs = tempCatalogsFromSession(session)

    tempCatalogs should have length 2
    tempCatalogs.head shouldBe a[XDTemporaryCatalogWithInvalidation]
    tempCatalogs.head.asInstanceOf[XDTemporaryCatalogWithInvalidation].underlying shouldBe a[HashmapCatalog]
    tempCatalogs(1) shouldBe a[HazelcastCatalog]

    hazelcastSessionProvider.close()
  }


  it should "provides a common persistent catalog and isolated catalogs" in {
    // TODO we should share the persistentCatalog

    val hazelcastSessionProvider = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())

    val (sessionTempCatalogs, sessionPersCatalogs) = {
      val session = createNewSession(hazelcastSessionProvider)
      (tempCatalogsFromSession(session), persistentCatalogsFromSession(session))
    }
    val (session2TempCatalogs, session2PersCatalogs) = {
      val session = createNewSession(hazelcastSessionProvider)
      (tempCatalogsFromSession(session), persistentCatalogsFromSession(session))
    }

    Seq(sessionTempCatalogs, session2TempCatalogs) foreach (_ should have length 2)

    sessionTempCatalogs.head should not be theSameInstanceAs(session2TempCatalogs.head)
    sessionTempCatalogs(1) should not be theSameInstanceAs(session2TempCatalogs(1))

    Seq(sessionPersCatalogs, session2PersCatalogs) foreach (_ should have length 1)
    sessionPersCatalogs.head should be theSameInstanceAs session2PersCatalogs.head

    hazelcastSessionProvider.close()
  }

  it should "allow to lookup an existing session" in {

    val hazelcastSessionProvider = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())
    val sessionId = UUID.randomUUID()
    val tableIdent = TableIdentifier("tab")

    val session = createNewSession(hazelcastSessionProvider, sessionId)

    import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon._

    session.catalog.registerTable(tableIdent, LocalRelation(), Some(CrossdataTable(tableIdent.normalize(xdContext.catalog.conf), None, "fakedatasource")))

    hazelcastSessionProvider.session(sessionId) should matchPattern {
      case Success(s: XDSession) if Try(s.catalog.lookupRelation(tableIdent)).isSuccess =>
    }

    hazelcastSessionProvider.close()
  }


  it should "fail when trying to lookup a non-existing session" in {

    val hazelcastSessionProvider = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())

    hazelcastSessionProvider.session(UUID.randomUUID()).isFailure shouldBe true

    hazelcastSessionProvider.close()
  }



  it should "remove the session metadata when closing an open session" in {
    val hazelcastSessionProvider = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())
    val sessionId = UUID.randomUUID()

    val session = hazelcastSessionProvider.newSession(sessionId)

    hazelcastSessionProvider.closeSession(sessionId)

    hazelcastSessionProvider.session(sessionId).isFailure shouldBe true

    hazelcastSessionProvider.close()
  }

  it should "fail when trying to close a non-existing session" in {

    val hazelcastSessionProvider = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())

    val session = hazelcastSessionProvider.newSession(UUID.randomUUID())

    hazelcastSessionProvider.closeSession(UUID.randomUUID()).isFailure shouldBe true
    
    hazelcastSessionProvider.close()
  }

  it should "provide the same cached instance when it hasn't been invalidated" in {

    val hazelcastSessionProvider = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())
    val sessionID = UUID.randomUUID()

    val refA = hazelcastSessionProvider.newSession(sessionID).get
    val refB = hazelcastSessionProvider.session(sessionID).get

    refA should be theSameInstanceAs refB

    hazelcastSessionProvider.close()
  }

  testInvalidation("provide a new session instance after its invalidation by a SQLConf change")(
    // This changes a setting  value using a second hazelcast peer
    _.setConf("spark.sql.parquet.filterPushdown", "false")
  )

  testInvalidation("provide a new session instance after its invalidation by a Catalog change")(
    // This changes a setting  value using a second hazelcast peer
    _.catalog.unregisterTable(TableIdentifier("DUMMY_TABLE"))
  )

  def testInvalidation(testDescription: String)(invalidationAction: XDSession => Unit) =
    it should testDescription in {

      // Two hazelcast peers shall be created
      val hazelcastSessionProviderA = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())
      val hazelcastSessionProviderB = new HazelcastSessionProvider(xdContext.sc, ConfigFactory.empty())

      val sessionID = UUID.randomUUID()

      val newSessionAtPeerA = hazelcastSessionProviderA.newSession(sessionID).get
      val obtainedSessionFromPeerA = hazelcastSessionProviderA.session(sessionID).get

      obtainedSessionFromPeerA should be theSameInstanceAs newSessionAtPeerA

      val obtainedSessionFromPeerB = hazelcastSessionProviderB.session(sessionID).get

      invalidationAction(obtainedSessionFromPeerB)

      Thread.sleep(500)

      val obtainedSessionFromPeerAAfterChange = hazelcastSessionProviderA.session(sessionID).get

      newSessionAtPeerA shouldNot be theSameInstanceAs obtainedSessionFromPeerAAfterChange

      hazelcastSessionProviderA.close()
      hazelcastSessionProviderB.close()
    }

  private def tempCatalogsFromSession(session: XDSession): Seq[XDTemporaryCatalog] = {
    session.catalog shouldBe a[CatalogChain]
    session.catalog.asInstanceOf[CatalogChain].temporaryCatalogs
  }

  private def persistentCatalogsFromSession(session: XDSession): Seq[XDPersistentCatalog] = {
    session.catalog shouldBe a[CatalogChain]
    session.catalog.asInstanceOf[CatalogChain].persistentCatalogs
  }

  private def createNewSession(hazelcastSessionProvider: HazelcastSessionProvider, uuid: UUID = UUID.randomUUID()): XDSession = {
    val optSession = hazelcastSessionProvider.newSession(uuid).toOption
    optSession shouldBe defined
    optSession.get
  }

}