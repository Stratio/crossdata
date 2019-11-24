/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
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
