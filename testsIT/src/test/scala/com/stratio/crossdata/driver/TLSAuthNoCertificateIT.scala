/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver

import java.io.{FileInputStream, InputStream}
import java.nio.file.Paths
import java.security.KeyStore

import com.stratio.crossdata.common.result.{ErrorSQLResult, SuccessfulSQLResult}
import com.stratio.crossdata.driver.test.Utils._
import com.stratio.crossdata.server.CrossdataServer
import com.stratio.crossdata.server.config.ServerConfig
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps
import scala.reflect.io.File
import scala.util.Try

@RunWith(classOf[JUnitRunner])
class TLSAuthNoCertificateIT extends EndToEndTest {

  override def init() = {

    val basepath = getClass.getResource("/certificates").getPath

    val configValues = Seq(
      ServerConfig.Http.Host -> ConfigValueFactory.fromAnyRef("localhost"),
      ServerConfig.Http.Port -> ConfigValueFactory.fromAnyRef(13422),
      ServerConfig.Http.TLS.TlsEnable        -> ConfigValueFactory.fromAnyRef(true),
      ServerConfig.Http.TLS.TlsTrustStore    -> ConfigValueFactory.fromAnyRef(s"$basepath/truststore.jks"),
      ServerConfig.Http.TLS.TlsTrustStorePwd -> ConfigValueFactory.fromAnyRef("Pass1word"),
      ServerConfig.Http.TLS.TlsKeyStore      -> ConfigValueFactory.fromAnyRef(s"$basepath/ServerKeyStore.jks"),
      ServerConfig.Http.TLS.TlsKeystorePwd   -> ConfigValueFactory.fromAnyRef("Pass1word")
    )

    val tlsConfig = (ConfigFactory.empty /: configValues) {
      case (config, (key, value)) => config.withValue(key, value)
    }

    crossdataServer = Some(new CrossdataServer(new ServerConfig(Some(tlsConfig))))
    crossdataServer.foreach(_.start())
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.newSession(SessionID, this.getClass.getSimpleName)))
  }

  "CrossdataDriver" should "return a connection error when trying to query to a TLS securized CrossdataServer from a non TLS driver" in {

    assumeCrossdataUpAndRunning()
    val driverTry = Try { withDriverDo { driver => } (DriverTestContext(Driver.http)) }

    driverTry.isFailure should be(true)
    driverTry.failed map { th =>
      th.getMessage should startWith("Cannot establish")
    }

  }

}