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
package com.stratio.crossdata.common.security

import java.io.{FileInputStream, InputStream}
import java.security.KeyStore
import javax.net.ssl.{KeyManagerFactory, TrustManagerFactory}

import org.apache.log4j.Logger



object KeyStoreUtils {

  lazy val logger = Logger.getLogger(KeyStoreUtils.getClass)

  def getKeyManagerFactory(keyStorePath: String, password: String): KeyManagerFactory = {
    val keyManagerFactory = KeyManagerFactory.getInstance("SunX509")
    keyManagerFactory.init(getKeyStore(keyStorePath, password), password.toCharArray)
    logger.info(s"Valid keystore loaded from $keyStorePath")
    keyManagerFactory
  }

  def getTrustManagerFactory(trustStorePath: String, password: String): TrustManagerFactory = {
    val tmf: TrustManagerFactory = TrustManagerFactory.getInstance("SunX509")
    tmf.init(getKeyStore(trustStorePath, password))
    logger.info(s"Valid truststore loaded from $trustStorePath")
    tmf
  }

  def getKeyStore(keyStorePath: String, password: String): KeyStore = {
    val ks: KeyStore = KeyStore.getInstance("JKS")
    val keystore: InputStream = new FileInputStream(keyStorePath)
    require(keystore != null, "Keystore required!")
    ks.load(keystore, password.toCharArray)
    ks
  }

}
