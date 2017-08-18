/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver

import java.util.UUID

import com.stratio.crossdata.server.CrossdataServer
import com.stratio.crossdata.server.config.ServerConfig
import com.stratio.crossdata.test.BaseXDTest
import org.apache.spark.sql.crossdata.XDSession
import org.apache.spark.sql.crossdata.session.XDSessionProvider.SessionID
import org.scalatest.BeforeAndAfterAll

trait EndToEndTest extends BaseXDTest with BeforeAndAfterAll {

  var crossdataServer: Option[CrossdataServer] = None
  var crossdataSession: Option[XDSession] = None
  val SessionID: SessionID = UUID.randomUUID()
  val UserId = "kravets"

  def init() = {
    crossdataServer = Some(new CrossdataServer(new ServerConfig))
    crossdataServer.foreach(_.start())
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.newSession(SessionID, UserId)))

  }

  def stop() = {
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.session(SessionID).get.dropAllTables()))
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.closeSession(SessionID)))
    crossdataServer.foreach(_.stop())
  }


  override protected def beforeAll(): Unit = {
    init()
  }

  override protected def afterAll(): Unit = {
    stop()
  }

  def assumeCrossdataUpAndRunning() = {
    assume(crossdataServer.isDefined, "Crossdata server is not up and running")
  }
}