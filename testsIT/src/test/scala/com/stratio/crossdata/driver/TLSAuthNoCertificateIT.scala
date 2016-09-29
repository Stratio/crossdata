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
      ServerConfig.AkkaHttpTLS.TlsEnable        -> ConfigValueFactory.fromAnyRef(true),
      ServerConfig.AkkaHttpTLS.TlsHost          -> ConfigValueFactory.fromAnyRef("localhost"),
      ServerConfig.AkkaHttpTLS.TlsPort          -> ConfigValueFactory.fromAnyRef(13422),
      ServerConfig.AkkaHttpTLS.TlsTrustStore    -> ConfigValueFactory.fromAnyRef(s"$basepath/truststore.jks"),
      ServerConfig.AkkaHttpTLS.TlsTrustStorePwd -> ConfigValueFactory.fromAnyRef("Pass1word"),
      ServerConfig.AkkaHttpTLS.TlsKeyStore      -> ConfigValueFactory.fromAnyRef(s"$basepath/ServerKeyStore.jks"),
      ServerConfig.AkkaHttpTLS.TlsKeystorePwd   -> ConfigValueFactory.fromAnyRef("Pass1word")
    )

    val tlsConfig = (ConfigFactory.empty /: configValues) {
      case (config, (key, value)) => config.withValue(key, value)
    }

    crossdataServer = Some(new CrossdataServer(Some(tlsConfig)))
    crossdataServer.foreach(_.start())
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.newSession(SessionID)))
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