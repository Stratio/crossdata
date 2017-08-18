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
