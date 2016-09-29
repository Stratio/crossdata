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

import com.stratio.crossdata.common.result.SuccessfulSQLResult
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.error.TLSInvalidAuthException
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

    val tlsConfig = ConfigFactory.empty
      .withValue(ServerConfig.AkkaHttpTLS.TlsEnable, ConfigValueFactory.fromAnyRef(true))
      .withValue(ServerConfig.AkkaHttpTLS.TlsHost, ConfigValueFactory.fromAnyRef("crossdata.com"))
      .withValue(ServerConfig.AkkaHttpTLS.TlsPort, ConfigValueFactory.fromAnyRef(13422))
      .withValue(ServerConfig.AkkaHttpTLS.TlsTrustStore, ConfigValueFactory.fromAnyRef(s"$basepath/truststore.jks"))
      .withValue(ServerConfig.AkkaHttpTLS.TlsTrustStorePwd, ConfigValueFactory.fromAnyRef("Pass1word"))
      .withValue(ServerConfig.AkkaHttpTLS.TlsKeyStore, ConfigValueFactory.fromAnyRef(s"$basepath/ServerKeyStore.jks"))
      .withValue(ServerConfig.AkkaHttpTLS.TlsKeystorePwd, ConfigValueFactory.fromAnyRef("Pass1word"))


    crossdataServer = Some(new CrossdataServer(Some(tlsConfig)))
    crossdataServer.foreach(_.start())
    crossdataServer.foreach(_.sessionProviderOpt.foreach(_.newSession(SessionID)))
  }

  "CrossdataDriver" should "return a result as usual using a valid TLS certificate from the driver" in {

    assumeCrossdataUpAndRunning()

    import scala.collection.JavaConverters._

    val setting = Some(new DriverConf()
      .set(DriverConf.DriverConfigServerHttp, ConfigValueFactory.fromIterable(List("crossdata.com:13422") asJava))
      .set(DriverConf.AkkaHttpTLS.TlsEnable, ConfigValueFactory.fromAnyRef(true))
      .set(DriverConf.AkkaHttpTLS.TlsKeyStore, ConfigValueFactory.fromAnyRef(s"$basepath/goodclient/ClientKeyStore.jks"))
      .set(DriverConf.AkkaHttpTLS.TlsKeystorePwd, ConfigValueFactory.fromAnyRef("Pass1word"))
      .set(DriverConf.AkkaHttpTLS.TlsTrustStore, ConfigValueFactory.fromAnyRef(s"$basepath/truststore.jks"))
      .set(DriverConf.AkkaHttpTLS.TlsTrustStorePwd, ConfigValueFactory.fromAnyRef("Pass1word"))
    )

    implicit val _: DriverTestContext = DriverTestContext(Driver.http, setting)

    withDriverDo { driver =>
      val result = driver.sql("show tables").waitForResult(10 seconds)
      result shouldBe an[SuccessfulSQLResult]
      result.hasError should be(false)
      val rows = result.resultSet
      rows should have length 0
    }

  }

}