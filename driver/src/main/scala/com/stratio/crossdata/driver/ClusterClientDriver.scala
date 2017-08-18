package com.stratio.crossdata.driver

import java.util.UUID

import akka.NotUsed
import akka.actor.{ActorPath, ActorRef}
import akka.cluster.ClusterEvent.CurrentClusterState
import akka.cluster.client.{ClusterClient, ClusterClientSettings}
import akka.stream.scaladsl.Source
import com.stratio.crossdata.common._
import com.stratio.crossdata.common.result._
import com.stratio.crossdata.common.security.Session
import com.stratio.crossdata.driver.actor.{ClusterClientSessionBeaconActor, ProxyActor, ServerClusterClientParameters}
import com.stratio.crossdata.driver.config.DriverConf
import com.stratio.crossdata.driver.session.{Authentication, SessionManager}
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future, Promise}
import scala.language.postfixOps
import scala.reflect.io.File
import scala.util.Try



class ClusterClientDriver private[driver](driverConf: DriverConf,
                                          auth: Authentication) extends Driver(driverConf) {

  import Driver._

  override protected def logger: Logger = LoggerFactory.getLogger(classOf[ClusterClientDriver])

  lazy val driverSession: Session = SessionManager.createSession(auth, proxyActor)

  override def sqlStreamedResult(query: String): Future[StreamedSQLResult] =
    throw new RuntimeException("ClusterClient implementation does not support streams API; use HttpDriver instead")

  private lazy val clusterClientActor = {

    if (logger.isDebugEnabled) {
      system.logConfiguration()
    }

    val contactPoints = driverConf.getClusterContactPoint
    val initialContacts = contactPoints.map(ActorPath.fromString).toSet

    logger.debug("Initial contacts: " + initialContacts)
    val remoteClientName: String = ServerClusterClientParameters.RemoteClientName + UUID.randomUUID()
    val actor = system.actorOf(ClusterClient.props(ClusterClientSettings(system).withInitialContacts(initialContacts)), remoteClientName)
    logger.debug(s"Cluster client actor created with name: $remoteClientName")

    actor
  }

  private val proxyActor = {
    val proxyActorName = ProxyActor.DefaultName + UUID.randomUUID()
    system.actorOf(ProxyActor.props(clusterClientActor, this), proxyActorName)
  }

  private val sessionBeaconProps = ClusterClientSessionBeaconActor.props(
    driverSession.id,
    5 seconds, /* This ins't configurable since it's simpler for the user
                  to play just with alert period time at server side. */
    clusterClientActor,
    ServerClusterClientParameters.ClientMonitorPath
  )

  private var sessionBeacon: Option[ActorRef] = None

  override protected[driver] def openSession(user:String): Try[Boolean] = {
    val res = Try {
      val promise = Promise[ServerReply]()
      proxyActor ! (securitizeCommand(OpenSessionCommand(user)), promise)
      Await.result(promise.future.mapTo[OpenSessionReply].map(_.isOpen), InitializationTimeout)
    }

    if (res.isSuccess)
      sessionBeacon = Some(system.actorOf(sessionBeaconProps))

    res
  }

  override def sql(query: String): SQLResponse = {
    //TODO remove this part when servers broadcast bus was realized
    //Preparse query to know if it is an special command sent from the shell or other driver user that is not a query
    val addJarPattern =
    """(\s*add)(\s+jar\s+)(.*)""".r
    val addAppWithAliasPattern ="""(\s*add)(\s+app\s+)(.*)(\s+as\s+)(.*)(\s+with\s+)(.*)""".r
    val addAppPattern ="""(\s*add)(\s+app\s+)(.*)(\s+with\s+)(.*)""".r

    query match { //TODO: Refactor this to re-use at all Driver implementations
      case addJarPattern(add, jar, path) =>
        addJar(path.trim)
      case addAppWithAliasPattern(add, app, path, as, alias, wth, clss) =>
        val realPath = path.replace("'", "")
        val res = addJar(realPath).waitForResult()
        val hdfspath = res.resultSet(0).getString(0)
        addApp(hdfspath, alias, clss)

      case addAppPattern(add, app, path, wth, clss) =>
        val realPath = path.replace("'", "")
        val res = addJar(realPath).waitForResult()
        val hdfspath = res.resultSet(0).getString(0)
        addApp(hdfspath, clss, realPath)
      case _ =>
        val sqlCommand = new SQLCommand(query, retrieveColNames = driverConf.getFlattenTables)
        val futureReply = askCommand(securitizeCommand(sqlCommand)).map {
          case SQLReply(_, sqlResult) =>
            sqlResult
          case other =>
            throw new RuntimeException(s"SQLReply expected. Received: $other")
        }
        new SQLResponse(sqlCommand.requestId, futureReply) {
          // TODO cancel sync => 5 secs
          override def cancelCommand(): Future[QueryCancelledReply] =
            askCommand(securitizeCommand(CancelQueryExecution(sqlCommand.queryId))) map {
              case reply: QueryCancelledReply => reply
              case other => throw new RuntimeException(s"CancelQueryExecution expected. Received: $other")
            }
        }
    }
  }

  /**
    * @param path  The path of the JAR
    * @param clss  The main class
    * @param alias The alias of the JAR
    * @return A SQLResponse with the id and the result set.
    */
  private def addApp(path: String, clss: String, alias: String): SQLResponse = {
    val addAppCommand = AddAppCommand(path, alias, clss)
    val futureReply = askCommand(securitizeCommand(addAppCommand)).map {
      case SQLReply(_, sqlResult) => sqlResult
      case other => throw new RuntimeException(s"SQLReply expected. Received: $other")
    }
    new SQLResponse(addAppCommand.requestId, futureReply)
  }

  private def askCommand(commandEnvelope: CommandEnvelope): Future[ServerReply] = {
    val promise = Promise[ServerReply]()
    proxyActor ! (commandEnvelope, promise)
    promise.future
  }

  override def addJar(path: String, toClassPath: Option[Boolean] = None): SQLResponse = {
    val addJarCommand = AddJARCommand(path, toClassPath = toClassPath)
    if (File(path).exists) {
      val futureReply = askCommand(securitizeCommand(addJarCommand)).map {
        case SQLReply(_, sqlResult) => sqlResult
        case other => throw new RuntimeException(s"SQLReply expected. Received: $other")
      }
      new SQLResponse(addJarCommand.requestId, futureReply)
    } else {
      new SQLResponse(addJarCommand.requestId, Future(ErrorSQLResult("File doesn't exist")))
    }
  }

  override def addAppCommand(path: String, clss: String, alias: Option[String]): SQLResponse = {
    val result = addJar(path, Option(false)).waitForResult()
    val hdfsPath = result.resultSet(0).getString(0)
    addApp(hdfsPath, clss, alias.getOrElse(path))
  }

  override def clusterState(): Future[CurrentClusterState] = {
    val promise = Promise[ServerReply]()
    proxyActor ! (securitizeCommand(ClusterStateCommand()), promise)
    promise.future.mapTo[ClusterStateReply].map(_.clusterState)
  }

  private[driver] def sessionProviderState(): Future[scala.collection.Set[String]] = {
    val promise = Promise[ServerReply]()
    proxyActor ! (securitizeCommand(ClusterStateCommand()), promise)
    promise.future.mapTo[ClusterStateReply].map(_.sessionCluster)
  }

  override def closeSession(): Unit = {
    proxyActor ! securitizeCommand(CloseSessionCommand())
    sessionBeacon.foreach(system.stop)
  }

}
