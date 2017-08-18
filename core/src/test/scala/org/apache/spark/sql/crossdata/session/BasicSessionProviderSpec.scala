/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package org.apache.spark.sql.crossdata.session

import java.util.UUID

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.catalyst.{CatalystConf, TableIdentifier}
import org.apache.spark.sql.catalyst.plans.logical.LocalRelation
import org.apache.spark.sql.crossdata.catalog.CatalogChain
import org.apache.spark.sql.crossdata.catalog.XDCatalog.CrossdataTable
import org.apache.spark.sql.crossdata.catalog.interfaces.{XDPersistentCatalog, XDTemporaryCatalog}
import org.apache.spark.sql.crossdata.catalog.temporary.HashmapCatalog
import org.apache.spark.sql.crossdata.test.SharedXDContextTest
import org.apache.spark.sql.crossdata.XDSession
import org.junit.runner.RunWith
import org.scalatest.Entry
import org.scalatest.junit.JUnitRunner

import scala.util.{Success, Try}
import org.apache.spark.sql.crossdata.catalog.interfaces.XDCatalogCommon._

@RunWith(classOf[JUnitRunner])
class BasicSessionProviderSpec extends SharedXDContextTest {

  val SparkSqlConfigString = "config.spark.sql.inMemoryColumnarStorage.batchSize=5000"
  val UserId = "kravets"

  implicit lazy val conf: CatalystConf = xdContext.catalog.conf


  "BasicSessionProvider" should "provides new sessions whose properties are initialized properly" in {

    val basicSessionProvider = new BasicSessionProvider(xdContext.sc, ConfigFactory.parseString(SparkSqlConfigString))

    val session = createNewSession(basicSessionProvider)

    session.conf.settings should contain(Entry("spark.sql.inMemoryColumnarStorage.batchSize", "5000"))

    val tempCatalogs = tempCatalogsFromSession(session)

    tempCatalogs should have length 1
    tempCatalogs.head shouldBe a[HashmapCatalog]

  }


  it should "provides a common persistent catalog and isolated catalogs" in {
    // TODO we should share the persistentCatalog

    val basicSessionProvider = new BasicSessionProvider(xdContext.sc, ConfigFactory.empty())

    val (sessionTempCatalogs, sessionPersCatalogs) = {
      val session = createNewSession(basicSessionProvider)
      (tempCatalogsFromSession(session), persistentCatalogsFromSession(session))
    }
    val (session2TempCatalogs, session2PersCatalogs) = {
      val session = createNewSession(basicSessionProvider)
      (tempCatalogsFromSession(session), persistentCatalogsFromSession(session))
    }

    Seq(sessionTempCatalogs, session2TempCatalogs) foreach (_ should have length 1)

    sessionTempCatalogs.head should not be theSameInstanceAs(session2TempCatalogs.head)

    Seq(sessionPersCatalogs, session2PersCatalogs) foreach (_ should have length 1)
    sessionPersCatalogs.head should be theSameInstanceAs session2PersCatalogs.head

  }


  it should "allow to lookup an existing session" in {

    val basicSessionProvider = new BasicSessionProvider(xdContext.sc, ConfigFactory.empty())
    val sessionId = UUID.randomUUID()
    val tableIdent = TableIdentifier("tab")

    val session = createNewSession(basicSessionProvider, sessionId)

    session.catalog.registerTable(tableIdent, LocalRelation(), Some(CrossdataTable(tableIdent.normalize, None, "fakedatasource")))

    basicSessionProvider.session(sessionId) should matchPattern {
      case Success(s: XDSession) if Try(s.catalog.lookupRelation(tableIdent)).isSuccess =>
    }

  }


  it should "fail when trying to lookup a non-existing session" in {

    val basicSessionProvider = new BasicSessionProvider(xdContext.sc, ConfigFactory.empty())

    basicSessionProvider.session(UUID.randomUUID()).isFailure shouldBe true

  }



  it should "remove the session metadata when closing an open session" in {
    val basicSessionProvider = new BasicSessionProvider(xdContext.sc, ConfigFactory.empty())
    val sessionId = UUID.randomUUID()

    val session = basicSessionProvider.newSession(sessionId, UserId)

    basicSessionProvider.closeSession(sessionId)

    basicSessionProvider.session(sessionId).isFailure shouldBe true

  }

  it should "fail when trying to close a non-existing session" in {

    val basicSessionProvider = new BasicSessionProvider(xdContext.sc, ConfigFactory.empty())

    val session = basicSessionProvider.newSession(UUID.randomUUID(), UserId)

    basicSessionProvider.closeSession(UUID.randomUUID()).isFailure shouldBe true

  }


  private def tempCatalogsFromSession(session: XDSession): Seq[XDTemporaryCatalog] = {
    session.catalog shouldBe a[CatalogChain]
    session.catalog.asInstanceOf[CatalogChain].temporaryCatalogs
  }

  private def persistentCatalogsFromSession(session: XDSession): Seq[XDPersistentCatalog] = {
    session.catalog shouldBe a[CatalogChain]
    session.catalog.asInstanceOf[CatalogChain].persistentCatalogs
  }

  private def createNewSession(sessionProvider: XDSessionProvider, uuid: UUID = UUID.randomUUID()): XDSession = {
    val optSession = sessionProvider.newSession(uuid, UserId).toOption
    optSession shouldBe defined
    optSession.get
  }

}