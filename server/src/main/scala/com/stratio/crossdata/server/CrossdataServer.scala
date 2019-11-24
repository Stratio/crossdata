/*
 * © 2017 Stratio Big Data Inc., Sucursal en España. All rights reserved.
 *
 * This software – including all its source code – contains proprietary information of Stratio Big Data Inc., Sucursal en España and may not be revealed, sold, transferred, modified, distributed or otherwise made available, licensed or sublicensed to third parties; nor reverse engineered, disassembled or decompiled, without express written authorization from Stratio Big Data Inc., Sucursal en España.
 */
package com.stratio.crossdata.server

import java.io.File
import java.security.SecureRandom
import javax.net.ssl.{KeyManagerFactory, SSLContext, TrustManagerFactory}

import akka.actor.{ActorSystem, Props}
import akka.cluster.Cluster
import akka.cluster.client.ClusterClientReceptionist
import akka.http.scaladsl.Http.ServerBinding
import akka.http.scaladsl.{Http, HttpsConnectionContext}
import akka.routing.{DefaultResizer, RoundRobinPool}
import akka.stream.{ActorMaterializer, TLSClientAuth}
import akka.cluster.pubsub.DistributedPubSub
import akka.cluster.pubsub.DistributedPubSubMediator.Put
import com.stratio.crossdata.common.security.KeyStoreUtils
import com.stratio.crossdata.common.util.akka.keepalive.KeepAliveMaster
import com.stratio.crossdata.server.actors.{ResourceManagerActor, ServerActor}
import com.stratio.crossdata.server.config.ServerConfig
import com.stratio.crossdata.server.discovery.{ServiceDiscoveryConfigHelper => SDCH, ServiceDiscoveryHelper => SDH}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.log4j.Logger
import org.apache.spark.sql.crossdata
import org.apache.spark.sql.crossdata.session.{BasicSessionProvider, HazelcastSessionProvider}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.util.Try


class CrossdataServer(sConfig: ServerConfig) extends ServiceDiscoveryProvider {

  override lazy val logger = Logger.getLogger(classOf[CrossdataServer])

  private var system: Option[ActorSystem] = None
  private var bindingFuture: Option[Future[ServerBinding]] = None

  override protected lazy val serverConfig = sConfig.config

  private lazy val sparkContext: SparkContext = {
    val sparkParams = serverConfig.entrySet()
      .map(e => (e.getKey, e.getValue.unwrapped().toString))
      .toMap
      .filterKeys(_.startsWith("config.spark"))
      .map(e => (e._1.replace("config.", ""), e._2))

    val metricsPath = Option(sparkParams.get("spark.metrics.conf"))
    val filteredSparkParams = metricsPath.fold(sparkParams)(m => checkMetricsFile(sparkParams, m.get))

    new SparkContext(new SparkConf().setAll(filteredSparkParams))
  }


  def start(): Unit = {

    // Get service discovery configuration
    val sdConfig = Try(serverConfig.getConfig(SDCH.ServiceDiscoveryPrefix)).toOption

    val sdHelper: Option[SDH] = sdConfig flatMap { serConfig =>
      Try(serConfig.getBoolean("activated")).toOption collect {
        case true =>
          logger.info("Service discovery enabled")
          startServiceDiscovery(new SDCH(serConfig))
      }
    }

    val finalConfig = sdHelper.fold(serverConfig)(_.finalConfig)

    val finalHzConfig = sdHelper.fold(hzConfig)(_.hzConfig)

    sessionProviderOpt = Some {
      if (sConfig.isHazelcastEnabled)
        new HazelcastSessionProvider(
          sparkContext,
          serverConfig = serverConfig,
          userCoreConfig = ConfigFactory.empty(), // TODO allow to configure core parameters programmatically
          finalHzConfig)
      else
        new BasicSessionProvider(sparkContext, serverConfig)
    }

    val sessionProvider = sessionProviderOpt
      .getOrElse(throw new RuntimeException("Crossdata Server cannot be started because there is no session provider"))

    assert(
      sdHelper.nonEmpty || sessionProvider.isInstanceOf[HazelcastSessionProvider],
      "Service Discovery needs to have the Hazelcast session provider enabled")

    finalConfig.entrySet.filter { e =>
      e.getKey.contains("seed-nodes")
    }.foreach { e =>
      logger.info(s"Seed nodes: ${e.getValue}")
    }

    system = Some(ActorSystem(sConfig.clusterName, finalConfig))

    system.fold(throw new RuntimeException("Actor system cannot be started")) { actorSystem =>

      val xdCluster = Cluster(actorSystem)

      sdHelper foreach { sd =>
        // Once the Cluster has been started and the cluster leadership is gotten,
        // this sever will update the list of cluster seeds and provider members periodically
        // according to the Akka members.
        import scala.concurrent.ExecutionContext.Implicits.global
        sd.leadershipFuture onSuccess {
          case _ =>
            updateServiceDiscovery(xdCluster, sessionProvider.asInstanceOf[HazelcastSessionProvider], sd, actorSystem)
        }
      }

      val resizer = DefaultResizer(lowerBound = sConfig.minServerActorInstances, upperBound = sConfig.maxServerActorInstances)
      val serverActor = actorSystem.actorOf(
        RoundRobinPool(sConfig.minServerActorInstances, Some(resizer)).props(
          Props(
            classOf[ServerActor],
            xdCluster,
            sessionProvider,
            sConfig)),
        sConfig.actorName)

      val clientMonitor = actorSystem.actorOf(KeepAliveMaster.props(serverActor), "client-monitor")
      DistributedPubSub(actorSystem).mediator ! Put(clientMonitor)

      val resourceManagerActor = actorSystem.actorOf(ResourceManagerActor.props(Cluster(actorSystem), sessionProvider))

      //Enable only if cluster client is enabled
      if(serverConfig.getBoolean(ServerConfig.ClusterClientEnabled)){
        ClusterClientReceptionist(actorSystem).registerService(clientMonitor)
        ClusterClientReceptionist(actorSystem).registerService(serverActor)
        ClusterClientReceptionist(actorSystem).registerService(resourceManagerActor)
      }

      implicit val httpSystem = actorSystem
      implicit val materializer = ActorMaterializer()
      val httpServerActor = new CrossdataHttpServer(finalConfig, serverActor, actorSystem)

      bindingFuture = Some {

        val host = serverConfig.getString(ServerConfig.Http.Host)
        val port = serverConfig.getInt(ServerConfig.Http.Port)

        if (serverConfig.getBoolean(ServerConfig.Http.TLS.TlsEnable)) {

          logger.info(s"Securized server with client certificate authentication on https://$host:$port")
          Http().bindAndHandle(httpServerActor.route, host, port, getTlsContext)

        } else Http().bindAndHandle(httpServerActor.route, host, port)

      }

    }

    logger.info(s"Crossdata Server started --- v${crossdata.CrossdataVersion}")
  }

  /**
    * Just for test purposes
    */
  def stop(): Unit = {

    sessionProviderOpt.foreach(_.close())
    sessionProviderOpt.foreach(_.sc.stop())

    system.foreach { actSystem =>
      implicit val exContext = actSystem.dispatcher
      bindingFuture.foreach { bFuture =>
        bFuture.flatMap(_.unbind()).onComplete(_ => actSystem.terminate())
      }
    }

    logger.info("Crossdata Server stopped")
  }

  private def getTlsContext: HttpsConnectionContext = {
    val sslContext: SSLContext = SSLContext.getInstance("TLS")

    val keystorePath = serverConfig.getString(ServerConfig.Http.TLS.TlsKeyStore)
    val keyStorePwd = serverConfig.getString(ServerConfig.Http.TLS.TlsKeystorePwd)
    val keyManagerFactory: KeyManagerFactory = KeyStoreUtils.getKeyManagerFactory(keystorePath, keyStorePwd)

    val trustStorePath = serverConfig.getString(ServerConfig.Http.TLS.TlsTrustStore)
    val trustStorePwd = serverConfig.getString(ServerConfig.Http.TLS.TlsTrustStorePwd)
    val trustManagerFactory: TrustManagerFactory = KeyStoreUtils.getTrustManagerFactory(trustStorePath, trustStorePwd)

    sslContext.init(keyManagerFactory.getKeyManagers, trustManagerFactory.getTrustManagers, new SecureRandom())
    new HttpsConnectionContext(sslContext, clientAuth = Some(TLSClientAuth.Need))
  }

  def checkMetricsFile(params: Map[String, String], metricsPath: String): Map[String, String] = {
    val metricsFile = new File(metricsPath)
    if (!metricsFile.exists) {
      logger.warn(s"Metrics configuration file not found: ${metricsFile.getPath}")
      params - "spark.metrics.conf"
    } else {
      params
    }
  }


}
