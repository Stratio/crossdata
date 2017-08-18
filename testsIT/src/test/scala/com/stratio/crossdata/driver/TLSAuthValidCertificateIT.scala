/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.driver

import com.stratio.crossdata.common.result.SuccessfulSQLResult
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.exceptions.TLSInvalidAuthException
import com.stratio.crossdata.driver.test.Utils._
import com.stratio.crossdata.server.CrossdataServer
import com.stratio.crossdata.server.config.ServerConfig
import com.typesafe.config.{ConfigFactory, ConfigValueFactory}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import scala.concurrent.duration._

import scala.language.postfixOps

@RunWith(classOf[JUnitRunner])
class TLSAuthValidCertificateIT extends EndToEndTest {

  val basepath = getClass.getResource("/certificates").getPath

  override def init() = {

    val configValues = Seq(
      ServerConfig.Http.Host                 -> ConfigValueFactory.fromAnyRef("localhost"),
      ServerConfig.Http.Port                 -> ConfigValueFactory.fromAnyRef(13422),
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

  "CrossdataDriver" should "return a result as usual using a valid TLS certificate from the driver" in {

    assumeCrossdataUpAndRunning()

    val settingsValues = Seq(
      DriverConf.Http.Host                 -> ConfigValueFactory.fromAnyRef("localhost"),
      DriverConf.Http.Port                 -> ConfigValueFactory.fromAnyRef("13422"),
      DriverConf.Http.TLS.TlsEnable        -> ConfigValueFactory.fromAnyRef(true),
      DriverConf.Http.TLS.TlsKeyStore      -> ConfigValueFactory.fromAnyRef(s"$basepath/goodclient/ClientKeyStore.jks"),
      DriverConf.Http.TLS.TlsKeystorePwd   -> ConfigValueFactory.fromAnyRef("Pass1word"),
      DriverConf.Http.TLS.TlsTrustStore    -> ConfigValueFactory.fromAnyRef(s"$basepath/truststore.jks"),
      DriverConf.Http.TLS.TlsTrustStorePwd -> ConfigValueFactory.fromAnyRef("Pass1word")
    )

    val setting = (new DriverConf() /: settingsValues) {
      case (config, (key, value)) => config.set(key, value)
    }

    implicit val _: DriverTestContext = DriverTestContext(Driver.http, Some(setting))

    withDriverDo { driver =>
      val result = driver.sql("show tables").waitForResult(10 seconds)
      result shouldBe an[SuccessfulSQLResult]
      result.hasError should be(false)
      val rows = result.resultSet
      rows should have length 0
    }

  }

}
